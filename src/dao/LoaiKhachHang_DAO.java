package dao;

import entity.LoaiKhachHang;
import connectSQL.ConnectSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng LoaiKhachHang
 * Hỗ trợ đầy đủ các thao tác CRUD + phương thức tiện ích quan trọng
 */
public class LoaiKhachHang_DAO {

    private final Connection conn;

    /**
     * Constructor nhận Connection từ bên ngoài (tốt hơn là inject thay vì mở mới)
     */
    public LoaiKhachHang_DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lấy tất cả các loại khách hàng
     */
    public List<LoaiKhachHang> getAll() throws SQLException {
        List<LoaiKhachHang> list = new ArrayList<>();
        String sql = "SELECT maLoaiKH, tenLoaiKH FROM LoaiKhachHang ORDER BY maLoaiKH";
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
     * Lấy loại khách hàng theo mã
     */
    public LoaiKhachHang getLoaiKhachHangByMa(String maLoaiKH) throws SQLException {
        String sql = "SELECT maLoaiKH, tenLoaiKH FROM LoaiKhachHang WHERE maLoaiKH = ?";
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
     * QUAN TRỌNG NHẤT: Lấy mã loại khách hàng theo tên loại
     * Ví dụ: layMaLoaiTheoTen("Thành viên") → trả về "TV" hoặc "LKH001"
     */
    public String layMaLoaiTheoTen(String tenLoai) throws SQLException {
        if (tenLoai == null || tenLoai.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT maLoaiKH FROM LoaiKhachHang WHERE tenLoaiKH = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenLoai.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maLoaiKH");
                }
            }
        }
        return null; // Không tìm thấy
    }

    /**
     * Thêm loại khách hàng mới
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
     * Cập nhật loại khách hàng
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
     * Xóa loại khách hàng
     */
    public boolean xoaLoaiKhachHang(String maLoaiKH) throws SQLException {
        String sql = "DELETE FROM LoaiKhachHang WHERE maLoaiKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiKH);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tự động phát sinh mã loại khách hàng mới (LKH0001, LKH0002, ...)
     */
    public String phatSinhMaLoaiKH() throws SQLException {
        String sql = "SELECT TOP 1 maLoaiKH FROM LoaiKhachHang ORDER BY maLoaiKH DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maLoaiKH");
                try {
                    int number = Integer.parseInt(lastId.substring(3)); // Bỏ "LKH"
                    number++;
                    return String.format("LKH%04d", number);
                } catch (Exception e) {
                    // Nếu định dạng không đúng, bắt đầu lại từ 0001
                    return "LKH0001";
                }
            }
        }
        return "LKH0001";
    }
}