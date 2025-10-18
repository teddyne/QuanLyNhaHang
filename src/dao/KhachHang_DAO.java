package dao;

import connectSQL.ConnectSQL;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    /**
     * Lấy danh sách tất cả khách hàng.
     */
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = mapRowToKhachHang(rs);
                dsKhachHang.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return dsKhachHang;
    }

    /**
     * Thêm một khách hàng mới.
     */
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, email, sdt, cccd, loaiKH) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            String maTuDong = phatSinhMaTuDong();
            kh.setMaKH(maTuDong);

            ps.setNString(1, kh.getMaKH());
            ps.setNString(2, kh.getTenKH());
            ps.setNString(3, kh.getEmail());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getCccd());
            ps.setNString(6, kh.getLoaiKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return false;
    }

    /**
     * Cập nhật thông tin khách hàng.
     */
    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, email = ?, sdt = ?, cccd = ?, loaiKH = ? WHERE maKH = ?";
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setNString(1, kh.getTenKH());
            ps.setNString(2, kh.getEmail());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCccd());
            ps.setNString(5, kh.getLoaiKH());
            ps.setNString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return false;
    }

    /**
     * Xóa một khách hàng.
     */
    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setNString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return false;
    }

    /**
     * Tìm kiếm khách hàng.
     */
    public List<KhachHang> timKiemKhachHang(String keyword) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ? OR tenKH LIKE ? OR cccd LIKE ? OR sdt LIKE ?";
        String searchTerm = "%" + keyword + "%";
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setNString(1, searchTerm);
            ps.setNString(2, searchTerm);
            ps.setString(3, searchTerm);
            ps.setString(4, searchTerm);

            rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(mapRowToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return ds;
    }

    /**
     * Phát sinh mã khách hàng tự động.
     */
    public String phatSinhMaTuDong() throws SQLException {
        String sql = "SELECT maKH FROM KhachHang";
        int maxId = 0;
        Connection conn = ConnectSQL.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String maKH = rs.getString("maKH");
                try {
                    int currentId = Integer.parseInt(maKH.substring(2));
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua các mã không đúng định dạng
                }
            }
        } finally {
            close(ps, rs);
        }
        
        int newId = maxId + 1;
        return "KH" + String.format("%04d", newId);
    }

    /**
     * Ánh xạ dữ liệu từ ResultSet sang đối tượng KhachHang.
     */
    private KhachHang mapRowToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getNString("maKH");
        String tenKH = rs.getNString("tenKH");
        String sdt = rs.getString("sdt");
        String email = rs.getNString("email");
        String cccd = rs.getString("cccd");
        String loaiKH = rs.getNString("loaiKH");

        return new KhachHang(maKH, tenKH, sdt, cccd, email, loaiKH);
    }

    /**
     * Phương thức tiện ích để đóng PreparedStatement.
     */
    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Phương thức tiện ích để đóng PreparedStatement và ResultSet.
     */
    private void close(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(ps);
    }
}