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

    public List<KhachHang> getAllKhachHang() throws SQLException {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.cccd, kh.email, lkh.tenLoaiKH " +
                    "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang kh = mapRowToKhachHang(rs);
                dsKhachHang.add(kh);
            }
        }
        return dsKhachHang;
    }

    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, cccd, email, maLoaiKH) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String maTuDong = generateMaKH();
            String maLoaiKH = kh.getloaiKH().equals("Thành viên") ? "LKH002" : "LKH001";
            
            ps.setString(1, maTuDong);
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCccd());
            ps.setString(5, kh.getEmail());
            ps.setString(6, maLoaiKH);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean suaKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET tenKH = ?, sdt = ?, cccd = ?, email = ?, maLoaiKH = ? WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String maLoaiKH = kh.getloaiKH().equals("Thành viên") ? "LKH002" : "LKH001";
            
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getCccd());
            ps.setString(4, kh.getEmail());
            ps.setString(5, maLoaiKH);
            ps.setString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        }
    }

    public List<KhachHang> timKiemKhachHang(String keyword) throws SQLException {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.cccd, kh.email, lkh.tenLoaiKH " +
                    "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                    "WHERE kh.maKH LIKE ? OR kh.tenKH LIKE ? OR kh.sdt LIKE ? OR kh.cccd LIKE ?";
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

    public String generateMaKH() throws SQLException {
        if (conn == null) {
            throw new SQLException("Kết nối cơ sở dữ liệu không tồn tại!");
        }
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

    private KhachHang mapRowToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getString("maKH");
        String tenKH = rs.getString("tenKH");
        String sdt = rs.getString("sdt");
        String cccd = rs.getString("cccd");
        String email = rs.getString("email");
        String tenLoaiKH = rs.getString("tenLoaiKH"); // Lấy tenLoaiKH từ LoaiKhachHang

        return new KhachHang(maKH, tenKH, sdt, cccd, email, tenLoaiKH);
    }

    public KhachHang timKhachHangTheoSDT(String sdt) throws SQLException {
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.cccd, kh.email, lkh.tenLoaiKH " +
                    "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                    "WHERE kh.sdt = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKhachHang(rs);
                }
            }
        }
        return null;
    }
}