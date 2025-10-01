package dao;

import entity.Ban;
import connectSQL.ConnectSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Ban_DAO {
    private Connection conn;

    public Ban_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<Ban> getAllBan(String maKhuVuc) throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.MaBan, b.MaKhuVuc, b.SoChoNgoi, b.LoaiBan, b.TrangThai, b.GhiChu, k.TenKhuVuc " +
                     "FROM Ban b " +
                     "LEFT JOIN KhuVuc k ON b.MaKhuVuc = k.MaKhuVuc " +
                     "WHERE b.MaKhuVuc = ? OR ? IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            pstmt.setString(2, maKhuVuc);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ban b = new Ban();
                    b.setMaBan(rs.getString("MaBan"));
                    b.setMaKhuVuc(rs.getString("MaKhuVuc"));
                    b.setSoChoNgoi(rs.getInt("SoChoNgoi"));
                    b.setLoaiBan(rs.getString("LoaiBan"));
                    b.setTrangThai(rs.getString("TrangThai"));
                    b.setGhiChu(rs.getString("GhiChu"));
                    b.setTenKhuVuc(rs.getString("TenKhuVuc"));
                    list.add(b);
                }
            }
        }
        return list;
    }
    public boolean checkTrung(String maBan, Date ngay, Time gio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DatBan WHERE MaBan = ? AND NgayDen = ? AND GioDen = CAST(? AS TIME) AND TrangThai NOT IN ('Hủy')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            pstmt.setDate(2, ngay);
            pstmt.setString(3, gio.toString()); // Sử dụng String để tránh xung đột TIME/DATETIME
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Ban getBanByMa(String maBan) throws SQLException {
        String sql = "SELECT b.MaBan, b.MaKhuVuc, b.SoChoNgoi, b.LoaiBan, b.TrangThai, b.GhiChu, k.TenKhuVuc " +
                     "FROM Ban b " +
                     "LEFT JOIN KhuVuc k ON b.MaKhuVuc = k.MaKhuVuc " +
                     "WHERE b.MaBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ban b = new Ban();
                    b.setMaBan(rs.getString("MaBan"));
                    b.setMaKhuVuc(rs.getString("MaKhuVuc"));
                    b.setSoChoNgoi(rs.getInt("SoChoNgoi"));
                    b.setLoaiBan(rs.getString("LoaiBan"));
                    b.setTrangThai(rs.getString("TrangThai"));
                    b.setGhiChu(rs.getString("GhiChu"));
                    b.setTenKhuVuc(rs.getString("TenKhuVuc"));
                    return b;
                }
            }
        }
        return null;
    }

    public void themBan(Ban b) throws SQLException {
        String sql = "INSERT INTO Ban (MaBan, MaKhuVuc, SoChoNgoi, LoaiBan, TrangThai, GhiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getMaBan());
            pstmt.setString(2, b.getMaKhuVuc());
            pstmt.setInt(3, b.getSoChoNgoi());
            pstmt.setString(4, b.getLoaiBan());
            pstmt.setString(5, b.getTrangThai());
            pstmt.setString(6, b.getGhiChu());
            pstmt.executeUpdate();
        }
    }

    public void capNhatBan(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET MaKhuVuc = ?, SoChoNgoi = ?, LoaiBan = ?, TrangThai = ?, GhiChu = ? " +
                     "WHERE MaBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getMaKhuVuc());
            pstmt.setInt(2, b.getSoChoNgoi());
            pstmt.setString(3, b.getLoaiBan());
            pstmt.setString(4, b.getTrangThai());
            pstmt.setString(5, b.getGhiChu());
            pstmt.setString(6, b.getMaBan());
            pstmt.executeUpdate();
        }
    }

    public void xoaBan(String maBan) throws SQLException {
        String sql = "DELETE FROM Ban WHERE MaBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            pstmt.executeUpdate();
        }
    }

 public void gopBan(String banChinh, String banGop) throws SQLException {
     // Chuyển lịch đặt bàn từ banGop sang banChinh
     String sqlUpdate = "UPDATE DatBan SET MaBan = ? WHERE MaBan = ?";
     try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
         pstmt.setString(1, banChinh);
         pstmt.setString(2, banGop);
         pstmt.executeUpdate();
     }
     // Xóa banGop
     xoaBan(banGop);
 }

 public void chuyenBan(String banCu, String banMoi) throws SQLException {
     // Chuyển lịch đặt bàn từ banCu sang banMoi
     String sqlUpdate = "UPDATE DatBan SET MaBan = ? WHERE MaBan = ?";
     try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
         pstmt.setString(1, banMoi);
         pstmt.setString(2, banCu);
         pstmt.executeUpdate();
     }
     // Cập nhật bàn cũ (nếu cần, ví dụ set TrangThai = 'Trống')
     String sqlBan = "UPDATE Ban SET TrangThai = 'Trống' WHERE MaBan = ?";
     try (PreparedStatement pstmt = conn.prepareStatement(sqlBan)) {
         pstmt.setString(1, banCu);
         pstmt.executeUpdate();
     }
 }
 }