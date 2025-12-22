package dao;

import connectSQL.ConnectSQL;
import entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhuyenMai_DAO {

	private final Connection conn = ConnectSQL.getInstance().getConnection();
    
    public List<KhuyenMai> layDanhSachKhuyenMai() {
    	List<KhuyenMai> list = new ArrayList<>();
    	String sql = """
    		SELECT KM.*, LK.tenLoai AS loaiKM
    		FROM KhuyenMai KM
    		JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
    		""";
    	Date today = new Date(System.currentTimeMillis());
	
    	try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
    		while (rs.next()) {
    			KhuyenMai km = new KhuyenMai();
    			km.setMaKM(rs.getString("maKM"));
    			km.setTenKM(rs.getString("tenKM"));
    			km.setMaLoai(rs.getString("loaiKM"));
    			km.setNgayBatDau(rs.getDate("ngayBatDau"));
    			km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
    			km.setTrangThai(rs.getString("trangThai"));
    			km.setDoiTuongApDung(rs.getString("doiTuongApDung"));
    			km.setGhiChu(rs.getString("ghiChu"));
	          
	       // --- Tính lại trạng thái ---
	          String trangThaiMoi;
	          if (today.before(km.getNgayBatDau())) {
	              trangThaiMoi = "Sắp áp dụng";
	          } else if (!today.after(km.getNgayKetThuc())) {
	              trangThaiMoi = "Đang áp dụng";
	          } else {
	              trangThaiMoi = "Hết hạn";
	          }
	
	          // Nếu trạng thái trong DB khác thì update lại
	          if (!trangThaiMoi.equals(km.getTrangThai())) {
	              capNhatTrangThai(km.getMaKM(), trangThaiMoi);
	              km.setTrangThai(trangThaiMoi);
	          }
	          
	          list.add(km);
	      }
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	  return list;
	}
    
	 //Cập nhật lại db
    private void capNhatTrangThai(String maKM, String trangThaiMoi) {
    	String sql = "UPDATE KhuyenMai SET trangThai=? WHERE maKM=?";
    	try (PreparedStatement ps = conn.prepareStatement(sql)) {
    		ps.setString(1, trangThaiMoi);
    		ps.setString(2, maKM);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
	    }
    }

    // ==============================
    // 2. LẤY THEO MÃ
    // ==============================
    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = """
            INSERT INTO KhuyenMai
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ganDuLieu(ps, km);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================
    // 4. SỬA
    // ==============================
    public boolean suaKhuyenMai(KhuyenMai km) {
        String sql = """
            UPDATE KhuyenMai SET
            tenKM=?, maLoai=?, giaTri=?, donHangTu=?, giamToiDa=?,
            ngayBatDau=?, ngayKetThuc=?, trangThai=?,
            doiTuongApDung=?, ghiChu=?
            WHERE maKM=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getMaLoai());
            ps.setDouble(3, km.getGiaTri());
            ps.setDouble(4, km.getDonHangTu());
            ps.setDouble(5, km.getGiamToiDa());
            ps.setDate(6, km.getNgayBatDau());
            ps.setDate(7, km.getNgayKetThuc());
            ps.setString(8, km.getTrangThai());
            ps.setString(9, km.getDoiTuongApDung());
            ps.setString(10, km.getGhiChu());
            ps.setString(11, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<KhuyenMai> traCuuKhuyenMai(String maKM, String doiTuong, String loai, String trangThai, Date tuNgay, Date denNgay) {
        List<KhuyenMai> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT KM.*, LK.tenLoai AS loaiKM
            FROM KhuyenMai KM
            JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
            WHERE 1=1
        """);

        if (maKM != null && !maKM.isEmpty()) sql.append(" AND KM.maKM LIKE ? ");
        if (doiTuong != null && !doiTuong.isEmpty()) sql.append(" AND KM.doiTuongApDung LIKE ? ");
        if (loai != null && !loai.isEmpty()) sql.append(" AND LK.tenLoai = ? ");
        if (trangThai != null && !trangThai.isEmpty()) sql.append(" AND KM.trangThai LIKE ? ");
        if (tuNgay != null) sql.append(" AND KM.ngayBatDau >= ? ");
        if (denNgay != null) sql.append(" AND KM.ngayKetThuc <= ? ");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (maKM != null && !maKM.isEmpty()) ps.setString(i++, "%" + maKM + "%");
            if (doiTuong != null && !doiTuong.isEmpty()) ps.setString(i++, "%" + doiTuong + "%");
            if (loai != null && !loai.isEmpty()) ps.setString(i++, loai);
            if (trangThai != null && !trangThai.isEmpty()) ps.setString(i++, "%" + trangThai + "%");
            if (tuNgay != null) ps.setDate(i++, tuNgay);
            if (denNgay != null) ps.setDate(i++, denNgay);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setMaLoai(rs.getString("loaiKM"));
                km.setNgayBatDau(rs.getDate("ngayBatDau"));
                km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                km.setTrangThai(rs.getString("trangThai"));
                km.setDoiTuongApDung(rs.getString("doiTuongApDung"));
                km.setGhiChu(rs.getString("ghiChu"));
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }return list;
    }
    
    public boolean themKhuyenMaiMon(String maKM, String maMon, String vaiTro, int soLuong) {
        String sql = """
            INSERT INTO KhuyenMai_Mon (maKM, maMon, vaiTro, soLuong)
            VALUES (?, ?, ?, ?)
        	""";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ps.setString(2, maMon);
            ps.setString(3, vaiTro);      // "Dieu_kien" hoặc "Tang"
            ps.setInt(4, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }return false;
    }
    
    public boolean xoaKhuyenMaiMonTheoMaKM(String maKM) {
        String sql = "DELETE FROM KhuyenMai_Mon WHERE maKM = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } return false;
    }

    public String taoMaKhuyenMaiMoi() {
    	String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			if (rs.next()) {
				String last = rs.getString("maKM");
				int num = Integer.parseInt(last.substring(2)) + 1;
				return String.format("KM%04d", num);
		      	}
		  	} catch (SQLException e) {
		  		e.printStackTrace();
		  	} return "";
	}

    public List<Object[]> layMonTheoKhuyenMai(String maKM, String vaiTro) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT m.maMon, m.tenMon, kmm.soLuong
            FROM KhuyenMai_Mon kmm
            JOIN MonAn m ON kmm.maMon = m.maMon
            WHERE kmm.maKM = ? AND kmm.vaiTro = ?
        	""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ps.setString(2, vaiTro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maMon"),
                    rs.getString("tenMon"),
                    rs.getInt("soLuong")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return list;
    }

    //Hàm phụ
    private KhuyenMai map(ResultSet rs) throws SQLException {
        return new KhuyenMai(
            rs.getString("maKM"),
            rs.getString("tenKM"),
            rs.getString("maLoai"),
            rs.getDouble("giaTri"),
            rs.getDouble("donHangTu"),
            rs.getDouble("giamToiDa"),
            rs.getDate("ngayBatDau"),
            rs.getDate("ngayKetThuc"),
            rs.getString("trangThai"),
            rs.getString("doiTuongApDung"),
            rs.getString("ghiChu")
        );
    }

    private void ganDuLieu(PreparedStatement ps, KhuyenMai km) throws SQLException {
        ps.setString(1, km.getMaKM());
        ps.setString(2, km.getTenKM());
        ps.setString(3, km.getMaLoai());
        ps.setDouble(4, km.getGiaTri());
        ps.setDouble(5, km.getDonHangTu());
        ps.setDouble(6, km.getGiamToiDa());
        ps.setDate(7, km.getNgayBatDau());
        ps.setDate(8, km.getNgayKetThuc());
        ps.setString(9, km.getTrangThai());
        ps.setString(10, km.getDoiTuongApDung());
        ps.setString(11, km.getGhiChu());
    }
    
    
    
    public Map<String, Integer> laySoLuongMonTrongPhieu(String maPhieu) {
        Map<String, Integer> map = new HashMap<>();

        String sql = """
            SELECT maMon, SUM(soLuong) AS sl
            FROM ChiTietPhieuDatBan
            WHERE maPhieu = ?
            GROUP BY maMon
        	""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("maMon"), rs.getInt("sl"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    public boolean duDieuKienTangMon(String maKM, String maPhieu) {
        String sqlDK = """
            SELECT maMon, soLuong
            FROM KhuyenMai_Mon
            WHERE maKM = ? AND vaiTro = 'Dieu_kien'
        	""";

        String sqlCheck = """
            SELECT SUM(soLuong)
            FROM ChiTietPhieuDatBan
            WHERE maPhieu = ? AND maMon = ?
        	""";

        try (Connection con = ConnectSQL.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDK)) {

            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maMon = rs.getString("maMon");
                int slCan = rs.getInt("soLuong");

                try (PreparedStatement ps2 = con.prepareStatement(sqlCheck)) {
                    ps2.setString(1, maPhieu);
                    ps2.setString(2, maMon);
                    ResultSet rs2 = ps2.executeQuery();

                    int slThucTe = 0;
                    if (rs2.next()) slThucTe = rs2.getInt(1);

                    if (slThucTe < slCan) return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



	
	public double tinhGiaTriGiam(KhuyenMai km, String maPhieu, double tongTien) throws SQLException {
	    switch (km.getMaLoai()) {
	        // PHẦN TRĂM
	        case "L01": {
	            double giam = tongTien * km.getGiaTri() / 100.0;
	            return Math.min(giam, km.getGiamToiDa());
	        }
	        // GIẢM TIỀN
	        case "L02": {
	            return Math.min(km.getGiaTri(), km.getGiamToiDa());
	        }
	        // TẶNG MÓN
	        case "L03": {
	            if (!duDieuKienTangMon(km.getMaKM(), maPhieu))
	                return 0;
	            double tongGiaTriMonTang = 0;
	            String sql = """
	                SELECT kmm.soLuong, m.donGia
	                FROM KhuyenMai_Mon kmm
	                JOIN MonAn m ON kmm.maMon = m.maMon
	                WHERE kmm.maKM = ? AND kmm.vaiTro = 'Tang'
	            	""";
	            try (PreparedStatement ps = conn.prepareStatement(sql)) {
	                ps.setString(1, km.getMaKM());
	                ResultSet rs = ps.executeQuery();
	                while (rs.next()) {
	                    tongGiaTriMonTang +=
	                        rs.getInt("soLuong") * rs.getDouble("donGia");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            } return tongGiaTriMonTang;
	        }
	    } return 0;
	}

	public List<KhuyenMai> layKhuyenMaiDangApDung(double tongTien) throws SQLException {
	    List<KhuyenMai> ds = new ArrayList<>();
	    String sql = """
	        SELECT * FROM KhuyenMai
	        WHERE 
	            GETDATE() BETWEEN ngayBatDau AND ngayKetThuc
	            AND (trangThai IS NULL OR trangThai = N'Đang áp dụng')
	            AND donHangTu <= ?
	        ORDER BY donHangTu DESC
	    	""";
	    try (Connection con = ConnectSQL.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setDouble(1, tongTien);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            KhuyenMai km = new KhuyenMai(
	                rs.getString("maKM"),
	                rs.getString("tenKM"),
	                rs.getString("maLoai"),
	                rs.getDouble("giaTri"),
	                rs.getDouble("donHangTu"),
	                rs.getDouble("giamToiDa"),
	                rs.getDate("ngayBatDau"),
	                rs.getDate("ngayKetThuc"),
	                rs.getString("trangThai"),
	                rs.getString("doiTuongApDung"),
	                rs.getString("ghiChu")
	            );
	            ds.add(km);
	        }
	    } return ds;
	}


}