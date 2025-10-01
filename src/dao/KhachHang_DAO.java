package dao;

import connectSQL.ConnectSQL;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
    private Connection conn;

    public KhachHang_DAO() {
        this.conn = ConnectSQL.getConnection();
    }

    // Thêm khách hàng
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, email, cccd) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getCccd());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa khách hàng
    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, sdt = ?, email = ?, cccd = ? WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getCccd());
            ps.setString(5, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa khách hàng
    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả khách hàng
    public List<KhachHang> layDanhSachKhachHang() {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getString("email"),
                        rs.getString("cccd")
                );
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Tìm kiếm khách hàng theo tên hoặc CCCD hoặc SDT
    public List<KhachHang> timKiemKhachHang(String keyword) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKH LIKE ? OR cccd LIKE ? OR sdt LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("sdt"),
                            rs.getString("email"),
                            rs.getString("cccd")
                    );
                    ds.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Tạo mã khách hàng mới dạng KH001
    public String taoMaKhachHangMoi() {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maKH, 3, LEN(maKH)) AS INT)), 0) AS maxId FROM KhachHang WHERE maKH LIKE 'KH[0-9][0-9][0-9]'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                return String.format("KH%03d", maxId + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH001"; // Nếu không có mã nào, bắt đầu từ KH001
    }

    // Lấy thông tin khách hàng theo mã
    public KhachHang layKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("sdt"),
                            rs.getString("email"),
                            rs.getString("cccd")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
