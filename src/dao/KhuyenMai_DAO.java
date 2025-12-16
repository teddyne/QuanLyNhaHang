//package dao;
//
//import connectSQL.ConnectSQL;
//import entity.KhuyenMai;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JOptionPane;
//
//public class KhuyenMai_DAO {
//    private final Connection conn;
//
//    public KhuyenMai_DAO() {
//        conn = ConnectSQL.getInstance().getConnection();
//    }
//        
//    public List<KhuyenMai> layDanhSachKhuyenMai() {
//        List<KhuyenMai> list = new ArrayList<>();
//        String sql = """
//            SELECT KM.*, LK.tenLoai AS loaiKM
//            FROM KhuyenMai KM
//            JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
//        """;
//        Date today = new Date(System.currentTimeMillis());
//
//        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                KhuyenMai km = new KhuyenMai();
//                km.setMaKM(rs.getString("maKM"));
//                km.setTenKM(rs.getString("tenKM"));
//                km.setMaLoai(rs.getString("loaiKM"));
//                km.setGiaTri(rs.getDouble("giaTri"));
//                km.setNgayBatDau(rs.getDate("ngayBatDau"));
//                km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
//                km.setTrangThai(rs.getString("trangThai"));
//                km.setDoiTuongApDung(rs.getString("doiTuongApDung"));
//                km.setDonHangTu(rs.getDouble("donHangTu"));
//                km.setMon1(rs.getString("mon1"));
//                km.setMon2(rs.getString("mon2"));
//                km.setMonTang(rs.getString("monTang"));
//                km.setGhiChu(rs.getString("ghiChu"));
//                
//             // --- Tính lại trạng thái ---
//                String trangThaiMoi;
//                if (today.before(km.getNgayBatDau())) {
//                    trangThaiMoi = "Sắp áp dụng";
//                } else if (!today.after(km.getNgayKetThuc())) {
//                    trangThaiMoi = "Đang áp dụng";
//                } else {
//                    trangThaiMoi = "Hết hạn";
//                }
//
//                // Nếu trạng thái trong DB khác thì update lại
//                if (!trangThaiMoi.equals(km.getTrangThai())) {
//                    capNhatTrangThai(km.getMaKM(), trangThaiMoi);
//                    km.setTrangThai(trangThaiMoi);
//                }
//                
//                list.add(km);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    
//    //Cập nhật lại db
//    private void capNhatTrangThai(String maKM, String trangThaiMoi) {
//        String sql = "UPDATE KhuyenMai SET trangThai=? WHERE maKM=?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, trangThaiMoi);
//            ps.setString(2, maKM);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//   
// // Hàm lấy một khuyến mãi theo mã
//    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
//    	String sql = """
//    		    SELECT KM.*, LK.tenLoai AS loaiKM
//    		    FROM KhuyenMai KM
//    		    JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
//    		    WHERE KM.maKM=?
//    		""";
//        try (
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            
//            ps.setString(1, maKM);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new KhuyenMai(
//                        rs.getString("maKM"),
//                        rs.getString("tenKM"),
//                        rs.getString("loaiKM"),
//                        rs.getDouble("giaTri"),
//                        rs.getDate("ngayBatDau"),
//                        rs.getDate("ngayKetThuc"),
//                        rs.getString("trangThai"),
//                        rs.getString("doiTuongApDung"),
//                        rs.getDouble("donHangTu"),
//                        rs.getString("mon1"),
//                        rs.getString("mon2"),
//                        rs.getString("monTang"),
//                        rs.getString("ghiChu")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null; // nếu không tìm thấy
//    }
//    
// // Lấy danh sách loại KM từ database
//    public String[] getLoaiKhuyenMai() {
//        List<String> list = new ArrayList<>();
//        try {
//            String sql = "SELECT tenLoai FROM LoaiKhuyenMai";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//                list.add(rs.getString("tenLoai"));
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list.toArray(new String[0]);
//    }
//
//    
//    //Tra cứu KM theo nhiều tiêu chí
//    public List<KhuyenMai> traCuuKhuyenMai(String maKM, String mon, String loai, String trangThai, Date tuNgay, Date denNgay) {
//        List<KhuyenMai> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder("""
//        	    SELECT KM.*, LK.tenLoai AS loaiKM
//        	    FROM KhuyenMai KM
//        	    JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
//        	    WHERE 1=1
//        	""");
//        // Tạo điều kiện linh hoạt
//        if (maKM != null && !maKM.isEmpty()) sql.append(" AND maKM LIKE ? ");
//        if (mon != null && !mon.isEmpty()) sql.append(" AND (mon1 LIKE ? OR mon2 LIKE ? OR monTang LIKE ?) ");
//        if (loai != null && !loai.isEmpty()) sql.append(" AND LK.tenLoai = ? ");
//        if (trangThai != null && !trangThai.isEmpty()) sql.append(" AND trangThai = ? ");
//        if (tuNgay != null) sql.append(" AND ngayBatDau >= ? ");
//        if (denNgay != null) sql.append(" AND ngayKetThuc <= ? ");
//
//        try (
//             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//
//            int index = 1;
//            if (maKM != null && !maKM.isEmpty()) ps.setString(index++, "%" + maKM + "%");
//            if (mon != null && !mon.isEmpty()) {
//                ps.setString(index++, "%" + mon + "%");
//                ps.setString(index++, "%" + mon + "%");
//                ps.setString(index++, "%" + mon + "%");
//            }
//            if (loai != null && !loai.isEmpty()) ps.setString(index++, loai);
//            if (trangThai != null && !trangThai.isEmpty()) ps.setString(index++, trangThai);
//            if (tuNgay != null) ps.setDate(index++, tuNgay);
//            if (denNgay != null) ps.setDate(index++, denNgay);
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                KhuyenMai km = new KhuyenMai(
//                        rs.getString("maKM"),
//                        rs.getString("tenKM"),
//                        rs.getString("loaiKM"),
//                        rs.getDouble("giaTri"),
//                        rs.getDate("ngayBatDau"),
//                        rs.getDate("ngayKetThuc"),
//                        rs.getString("trangThai"),
//                        rs.getString("doiTuongApDung"),
//                        rs.getDouble("donHangTu"),
//                        rs.getString("mon1"),
//                        rs.getString("mon2"),
//                        rs.getString("monTang"),
//                        rs.getString("ghiChu")
//                );
//                list.add(km);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    public boolean themKhuyenMai(KhuyenMai km) {
//        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, maLoai, giaTri, ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, donHangTu, mon1, mon2, monTang, ghiChu) " +
//                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, km.getMaKM());
//            pstmt.setString(2, km.getTenKM());
//            pstmt.setString(3, km.getMaLoai()); // Sử dụng maLoai thay vì loaiKM
//            pstmt.setDouble(4, km.getGiaTri());
//            pstmt.setDate(5, km.getNgayBatDau());
//            pstmt.setDate(6, km.getNgayKetThuc());
//            pstmt.setString(7, km.getTrangThai());
//            pstmt.setString(8, km.getDoiTuongApDung());
//            pstmt.setDouble(9, km.getDonHangTu());
//            pstmt.setString(10, km.getMon1());
//            pstmt.setString(11, km.getMon2());
//            pstmt.setString(12, km.getMonTang());
//            pstmt.setString(13, km.getGhiChu());
//            int rowsAffected = pstmt.executeUpdate();
//            System.out.println("Rows affected: " + rowsAffected); // Log để kiểm tra
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi cơ sở dữ liệu: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    
//    public boolean suaKhuyenMai(KhuyenMai km) {
//        String sql = "UPDATE KhuyenMai SET maLoai = ?, tenKM = ?, giaTri = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThai = ?, doiTuongApDung = ?, donHangTu = ?, mon1 = ?, mon2 = ?, monTang = ?, ghiChu = ? WHERE maKM = ?";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, km.getMaLoai()); // Sử dụng maLoai thay vì loaiKM
//            pstmt.setString(2, km.getTenKM());
//            pstmt.setDouble(3, km.getGiaTri());
//            pstmt.setDate(4, km.getNgayBatDau());
//            pstmt.setDate(5, km.getNgayKetThuc());
//            pstmt.setString(6, km.getTrangThai());
//            pstmt.setString(7, km.getDoiTuongApDung());
//            pstmt.setDouble(8, km.getDonHangTu());
//            pstmt.setString(9, km.getMon1());
//            pstmt.setString(10, km.getMon2());
//            pstmt.setString(11, km.getMonTang());
//            pstmt.setString(12, km.getGhiChu());
//            pstmt.setString(13, km.getMaKM());
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    public String taoMaKhuyenMaiMoi() {
//        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
//        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
//            if (rs.next()) {
//                String last = rs.getString("maKM");
//                int num = Integer.parseInt(last.substring(2)) + 1;
//                return String.format("KM%04d", num);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    
//    public List<KhuyenMai> getDanhSachKhuyenMaiHopLe(String maHD, double tongHoaDon) {
//        List<KhuyenMai> dsKM = new ArrayList<>();
//
//        try (Connection conn = ConnectSQL.getInstance().getConnection()) { // tạo connection mới mỗi lần gọi
//            if (conn == null) {
//                System.err.println("❌ Không thể kết nối CSDL");
//                return dsKM;
//            }
//
//            String sql = """
//                SELECT * FROM KhuyenMai
//                WHERE trangThai = N'Đang áp dụng'
//                AND GETDATE() BETWEEN ngayBatDau AND ngayKetThuc
//            """;
//
//            try (PreparedStatement stmt = conn.prepareStatement(sql);
//                 ResultSet rs = stmt.executeQuery()) {
//
//                ChiTietHoaDon_DAO chiTietDAO = new ChiTietHoaDon_DAO();
//
//                while (rs.next()) {
//                    double donHangTu = rs.getDouble("donHangTu");
//                    if (tongHoaDon < donHangTu) continue;
//
//                    KhuyenMai km = new KhuyenMai(
//                        rs.getString("maKM"),
//                        rs.getString("tenKM"),
//                        rs.getString("maLoai"),
//                        rs.getDouble("giaTri"),
//                        rs.getDate("ngayBatDau"),
//                        rs.getDate("ngayKetThuc"),
//                        rs.getString("trangThai"),
//                        rs.getString("doiTuongApDung"),
//                        donHangTu,
//                        rs.getString("mon1"),
//                        rs.getString("mon2"),
//                        rs.getString("monTang"),
//                        rs.getString("ghiChu")
//                    );
//
//                    // Kiểm tra khuyến mãi tặng món
//                    if ("L03".equals(km.getMaLoai())) {
//                        List<String> monTrongHD = chiTietDAO.getDanhSachMonTheoHoaDon(maHD);
//
//                        boolean coMonHopLe =
//                            (km.getMon1() != null && monTrongHD.contains(km.getMon1())) ||
//                            (km.getMon2() != null && monTrongHD.contains(km.getMon2()));
//
//                        if (!coMonHopLe) continue;
//                    }
//
//                    dsKM.add(km);
//                }
//
//                // Sắp xếp theo ưu tiên và giá trị
//                dsKM.sort((a, b) -> {
//                    int uuTienA = getUuTien(a.getMaLoai());
//                    int uuTienB = getUuTien(b.getMaLoai());
//                    if (uuTienA != uuTienB)
//                        return Integer.compare(uuTienA, uuTienB);
//                    return Double.compare(b.getGiaTri(), a.getGiaTri());
//                });
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dsKM;
//    }
//
//    private int getUuTien(String maLoai) {
//        return switch (maLoai) {
//            case "L03" -> 1; // Tặng món (cao nhất)
//            case "L01" -> 2; // Giảm %
//            case "L02" -> 3; // Giảm tiền mặt
//            default -> 4;
//        };
//    }
//
//    public KhuyenMai getKhuyenMaiTotNhat(double tongHoaDon, String maHD) {
//        List<KhuyenMai> dsHopLe = getDanhSachKhuyenMaiHopLe(maHD, tongHoaDon);
//        if (dsHopLe.isEmpty()) return null;
//
//        // ✅ Lấy khuyến mãi đầu tiên (đã sắp xếp theo ưu tiên và giá trị)
//        return dsHopLe.get(0);
//    }
//}

package dao;

import connectSQL.ConnectSQL;
import entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}
