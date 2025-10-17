package dao;

import connectSQL.ConnectSQL; 
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    /**
     * Lấy danh sách tất cả khách hàng từ cơ sở dữ liệu.
     */
    public List<KhachHang> getAllKhachHang() throws SQLException {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = mapRowToKhachHang(rs);
                dsKhachHang.add(kh);
            }
        }
        return dsKhachHang;
    }

    /**
     * Thêm một khách hàng mới vào cơ sở dữ liệu.
     */
    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, email, sdt, cccd, loaiKH) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, kh.getMaKH());
            ps.setNString(2, kh.getTenKH());
            ps.setNString(3, kh.getEmail());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getCccd());
            ps.setNString(6, kh.getLoaiKH());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin của một khách hàng đã có trong cơ sở dữ liệu.
     */
    public boolean suaKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET tenKH = ?, email = ?, sdt = ?, cccd = ?, loaiKH = ? WHERE maKH = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, kh.getTenKH());
            ps.setNString(2, kh.getEmail());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCccd());
            ps.setNString(5, kh.getLoaiKH());
            ps.setNString(6, kh.getMaKH()); 

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một khách hàng khỏi cơ sở dữ liệu dựa trên mã KH.
     */
    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, maKH);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa.
     */
    public List<KhachHang> timKiemKhachHang(String keyword) throws SQLException {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ? OR tenKH LIKE ? OR cccd LIKE ? OR sdt LIKE ?";
        String searchTerm = "%" + keyword + "%";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, searchTerm);
            ps.setNString(2, searchTerm);
            ps.setString(3, searchTerm);
            ps.setString(4, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRowToKhachHang(rs));
                }
            }
        }
        return ds;
    }

    /**
     * Phương thức helper để ánh xạ dữ liệu từ một dòng ResultSet sang một đối tượng KhachHang.
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
}

