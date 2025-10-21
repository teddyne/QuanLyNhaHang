package dao;

import entity.LoaiKhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhachHang_DAO {
    private final Connection conn;

    /**
     * Constructor nhận vào một đối tượng Connection.
     */
    public LoaiKhachHang_DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lấy tất cả các loại khách hàng từ cơ sở dữ liệu.
     */
    public List<LoaiKhachHang> getAll() throws SQLException {
        List<LoaiKhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiKhachHang";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiKhachHang lkh = new LoaiKhachHang();
                lkh.setMaLoaiKH(rs.getString("maLoaiKH"));
                lkh.setTenLoaiKH(rs.getString("tenLoaiKH"));
                list.add(lkh);
            }
        }
        return list;
    }

    /**
     * Lấy thông tin một loại khách hàng dựa trên mã.
     */
    public LoaiKhachHang getLoaiKhachHangByMa(String maLoaiKH) throws SQLException {
        String sql = "SELECT * FROM LoaiKhachHang WHERE maLoaiKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiKhachHang lkh = new LoaiKhachHang();
                    lkh.setMaLoaiKH(rs.getString("maLoaiKH"));
                    lkh.setTenLoaiKH(rs.getString("tenLoaiKH"));
                    return lkh;
                }
            }
        }
        return null;
    }

    /**
     * Thêm một loại khách hàng mới vào cơ sở dữ liệu.
     */
    public boolean themLoaiKhachHang(LoaiKhachHang lkh) throws SQLException {
        String sql = "INSERT INTO LoaiKhachHang (maLoaiKH, tenLoaiKH) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lkh.getMaLoaiKH());
            ps.setString(2, lkh.getTenLoaiKH());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin của một loại khách hàng đã có.
     */
    public boolean capNhatLoaiKhachHang(LoaiKhachHang lkh) throws SQLException {
        String sql = "UPDATE LoaiKhachHang SET tenLoaiKH = ? WHERE maLoaiKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lkh.getTenLoaiKH());
            ps.setString(2, lkh.getMaLoaiKH());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một loại khách hàng khỏi cơ sở dữ liệu dựa trên mã.
     */
    public boolean xoaLoaiKhachHang(String maLoaiKH) throws SQLException {
        String sql = "DELETE FROM LoaiKhachHang WHERE maLoaiKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiKH);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tự động phát sinh mã loại khách hàng mới (ví dụ: LKH003 -> LKH004).
     */
    public String phatSinhMaLoaiKH() throws SQLException {
        String newId = "LKH0001";
        String sql = "SELECT TOP 1 maLoaiKH FROM LoaiKhachHang ORDER BY maLoaiKH DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maLoaiKH");
                // Tách phần số từ chuỗi mã (ví dụ: "LKH001" -> 1)
                int number = Integer.parseInt(lastId.substring(2));
                number++;
                // Định dạng lại thành chuỗi có 3 chữ số (ví dụ: 2 -> "002")
                newId = String.format("LKH%04d", number);
            }
        }
        return newId;
    }
}