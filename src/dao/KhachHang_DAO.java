package dao;

import entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
    private final Connection conn;

    public KhachHang_DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lấy danh sách tất cả khách hàng từ cơ sở dữ liệu.
     */
    public List<KhachHang> getAllKhachHang() throws SQLException {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang kh = mapRowToKhachHang(rs);
                dsKhachHang.add(kh);
            }
        }
        return dsKhachHang;
    }

    /**
     * Thêm một khách hàng mới vào cơ sở dữ liệu. Mã KH sẽ được tự động phát sinh.
     */
    public boolean themKhachHang(KhachHang kh) throws SQLException {

        String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, cccd, email, maLoaiKH) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String maTuDong = phatSinhMaTuDong();
            
            ps.setString(1, maTuDong);
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCccd());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getMaLoaiKH());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin của một khách hàng đã có trong CSDL.
     */
    public boolean suaKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET tenKH = ?, sdt = ?, cccd = ?, email = ?, maLoaiKH = ? WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getCccd());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getMaLoaiKH());
            ps.setString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một khách hàng khỏi cơ sở dữ liệu dựa trên mã.
     */
    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa (Mã, Tên, SĐT, CCCD).
     */
    public List<KhachHang> timKiemKhachHang(String keyword) throws SQLException {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ? OR tenKH LIKE ? OR sdt LIKE ? OR cccd LIKE ?";
        String searchTerm = "%" + keyword + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
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
     * Phát sinh mã khách hàng mới một cách tự động và hiệu quả.
     */
    public String phatSinhMaTuDong() throws SQLException {
        String newId = "KH0001"; 
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maKH");
                
                int number = Integer.parseInt(lastId.substring(2));
                number++; 
                newId = String.format("KH%04d", number);
            }
        }
        return newId;
    }

    /**
     * Ánh xạ một dòng dữ liệu từ ResultSet sang đối tượng KhachHang.
     */
    private KhachHang mapRowToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getString("maKH");
        String tenKH = rs.getString("tenKH");
        String sdt = rs.getString("sdt");
        String cccd = rs.getString("cccd");
        String email = rs.getString("email");
        String maLoaiKH = rs.getString("maLoaiKH"); // Lấy mã loại khách hàng

        return new KhachHang(maKH, tenKH, sdt, cccd, email, maLoaiKH);
    }
}