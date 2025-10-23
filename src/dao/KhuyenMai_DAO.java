package dao;

import connectSQL.ConnectSQL;
import entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class KhuyenMai_DAO {
    private final Connection conn;

    public KhuyenMai_DAO() {
        conn = ConnectSQL.getInstance().getConnection();
    }
        
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
                km.setGiaTri(rs.getDouble("giaTri"));
                km.setNgayBatDau(rs.getDate("ngayBatDau"));
                km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                km.setTrangThai(rs.getString("trangThai"));
                km.setDoiTuongApDung(rs.getString("doiTuongApDung"));
                km.setDonHangTu(rs.getDouble("donHangTu"));
                km.setMon1(rs.getString("mon1"));
                km.setMon2(rs.getString("mon2"));
                km.setMonTang(rs.getString("monTang"));
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

   
 // Hàm lấy một khuyến mãi theo mã
    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
    	String sql = """
    		    SELECT KM.*, LK.tenLoai AS loaiKM
    		    FROM KhuyenMai KM
    		    JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
    		    WHERE KM.maKM=?
    		""";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("loaiKM"),
                        rs.getDouble("giaTri"),
                        rs.getDate("ngayBatDau"),
                        rs.getDate("ngayKetThuc"),
                        rs.getString("trangThai"),
                        rs.getString("doiTuongApDung"),
                        rs.getDouble("donHangTu"),
                        rs.getString("mon1"),
                        rs.getString("mon2"),
                        rs.getString("monTang"),
                        rs.getString("ghiChu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // nếu không tìm thấy
    }
    
 // Lấy danh sách loại KM từ database
    public String[] getLoaiKhuyenMai() {
        List<String> list = new ArrayList<>();
        try {
            String sql = "SELECT tenLoai FROM LoaiKhuyenMai";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                list.add(rs.getString("tenLoai"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }

    
    //Tra cứu KM theo nhiều tiêu chí
    public List<KhuyenMai> traCuuKhuyenMai(String maKM, String mon, String loai, String trangThai, Date tuNgay, Date denNgay) {
        List<KhuyenMai> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        	    SELECT KM.*, LK.tenLoai AS loaiKM
        	    FROM KhuyenMai KM
        	    JOIN LoaiKhuyenMai LK ON KM.maLoai = LK.maLoai
        	    WHERE 1=1
        	""");
        // Tạo điều kiện linh hoạt
        if (maKM != null && !maKM.isEmpty()) sql.append(" AND maKM LIKE ? ");
        if (mon != null && !mon.isEmpty()) sql.append(" AND (mon1 LIKE ? OR mon2 LIKE ? OR monTang LIKE ?) ");
        if (loai != null && !loai.isEmpty()) sql.append(" AND LK.tenLoai = ? ");
        if (trangThai != null && !trangThai.isEmpty()) sql.append(" AND trangThai = ? ");
        if (tuNgay != null) sql.append(" AND ngayBatDau >= ? ");
        if (denNgay != null) sql.append(" AND ngayKetThuc <= ? ");

        try (
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (maKM != null && !maKM.isEmpty()) ps.setString(index++, "%" + maKM + "%");
            if (mon != null && !mon.isEmpty()) {
                ps.setString(index++, "%" + mon + "%");
                ps.setString(index++, "%" + mon + "%");
                ps.setString(index++, "%" + mon + "%");
            }
            if (loai != null && !loai.isEmpty()) ps.setString(index++, loai);
            if (trangThai != null && !trangThai.isEmpty()) ps.setString(index++, trangThai);
            if (tuNgay != null) ps.setDate(index++, tuNgay);
            if (denNgay != null) ps.setDate(index++, denNgay);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("loaiKM"),
                        rs.getDouble("giaTri"),
                        rs.getDate("ngayBatDau"),
                        rs.getDate("ngayKetThuc"),
                        rs.getString("trangThai"),
                        rs.getString("doiTuongApDung"),
                        rs.getDouble("donHangTu"),
                        rs.getString("mon1"),
                        rs.getString("mon2"),
                        rs.getString("monTang"),
                        rs.getString("ghiChu")
                );
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, maLoai, giaTri, ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, donHangTu, mon1, mon2, monTang, ghiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, km.getMaKM());
            pstmt.setString(2, km.getTenKM());
            pstmt.setString(3, km.getMaLoai()); // Sử dụng maLoai thay vì loaiKM
            pstmt.setDouble(4, km.getGiaTri());
            pstmt.setDate(5, km.getNgayBatDau());
            pstmt.setDate(6, km.getNgayKetThuc());
            pstmt.setString(7, km.getTrangThai());
            pstmt.setString(8, km.getDoiTuongApDung());
            pstmt.setDouble(9, km.getDonHangTu());
            pstmt.setString(10, km.getMon1());
            pstmt.setString(11, km.getMon2());
            pstmt.setString(12, km.getMonTang());
            pstmt.setString(13, km.getGhiChu());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Log để kiểm tra
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean suaKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET maLoai = ?, tenKM = ?, giaTri = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThai = ?, doiTuongApDung = ?, donHangTu = ?, mon1 = ?, mon2 = ?, monTang = ?, ghiChu = ? WHERE maKM = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, km.getMaLoai()); // Sử dụng maLoai thay vì loaiKM
            pstmt.setString(2, km.getTenKM());
            pstmt.setDouble(3, km.getGiaTri());
            pstmt.setDate(4, km.getNgayBatDau());
            pstmt.setDate(5, km.getNgayKetThuc());
            pstmt.setString(6, km.getTrangThai());
            pstmt.setString(7, km.getDoiTuongApDung());
            pstmt.setDouble(8, km.getDonHangTu());
            pstmt.setString(9, km.getMon1());
            pstmt.setString(10, km.getMon2());
            pstmt.setString(11, km.getMonTang());
            pstmt.setString(12, km.getGhiChu());
            pstmt.setString(13, km.getMaKM());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        }
        return "";
    }
}