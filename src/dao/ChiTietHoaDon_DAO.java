package dao;

import entity.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO {
    private final Connection conn;

    public ChiTietHoaDon_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean themChiTiet(ChiTietHoaDon ct) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaMon());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getGhiChu());
            return ps.executeUpdate() > 0;
        }
    }

    public List<ChiTietHoaDon> layChiTietTheoHoaDon(String maHoaDon) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon(sql, sql, 0, 0, sql);
                    ct.setMaHD(rs.getString("maHD"));
                    ct.setMaMon(rs.getString("maMon"));
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getDouble("donGia"));
                    ct.setGhiChu(rs.getString("ghiChu"));
                    list.add(ct);
                }
            }
        }
        return list;
    }

    public boolean xoaChiTietTheoHoaDon(String maHoaDon) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            return ps.executeUpdate() > 0;
        }
    }
}