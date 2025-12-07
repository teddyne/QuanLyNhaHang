package dao;

import connectSQL.ConnectSQL;
import entity.ChiTietDatMon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDatMon_DAO {
    private final Connection con;

    public ChiTietDatMon_DAO(Connection con) {
        this.con = con;
    }

    public boolean themChiTiet(ChiTietDatMon ct) throws SQLException {
        String sql = "INSERT INTO ChiTietDatMon (maPhieu, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPhieu()); // ← dùng getMaPhieu() như cũ
            ps.setString(2, ct.getMaMon());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getGhiChu() != null ? ct.getGhiChu() : "");
            return ps.executeUpdate() > 0;
        }
    }

    public boolean tonTaiMonTrongPhieu(String maPhieu, String maMon) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietDatMon WHERE maPhieu = ? AND maMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ps.setString(2, maMon);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean tangSoLuong(String maPhieu, String maMon, int soLuongTang) throws SQLException {
        String sql = "UPDATE ChiTietDatMon SET soLuong = soLuong + ? WHERE maPhieu = ? AND maMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongTang);
            ps.setString(2, maPhieu);
            ps.setString(3, maMon);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean themMonThongMinh(String maPhieu, String maMon, int soLuong, double donGia, String ghiChu) throws SQLException {
        if (tonTaiMonTrongPhieu(maPhieu, maMon)) {
            return tangSoLuong(maPhieu, maMon, soLuong);
        } else {
            ChiTietDatMon ct = new ChiTietDatMon(maPhieu, maMon, soLuong, donGia, ghiChu);
            return themChiTiet(ct);
        }
    }

    public boolean xoaTheoPhieu(String maPhieu) throws SQLException {
        String sql = "DELETE FROM ChiTietDatMon WHERE maPhieu = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;
        }
    }

    public List<ChiTietDatMon> layTheoPhieu(String maPhieu) throws SQLException {
        List<ChiTietDatMon> list = new ArrayList<>();
        String sql = """
            SELECT ct.*, ma.tenMon
            FROM ChiTietDatMon ct
            JOIN MonAn ma ON ct.maMon = ma.maMon
            WHERE ct.maPhieu = ?
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietDatMon ct = new ChiTietDatMon(
                    rs.getString("maPhieu"),
                    rs.getString("maMon"),
                    rs.getInt("soLuong"),
                    rs.getDouble("donGia"),
                    rs.getString("ghiChu")
                );
                ct.setTenMon(rs.getString("tenMon"));
                list.add(ct);
            }
        }
        return list;
    }

    public boolean capNhatSoLuong(String maPhieu, String maMon, int soLuongMoi) throws SQLException {
        String sql = "UPDATE ChiTietDatMon SET soLuong = ? WHERE maPhieu = ? AND maMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, maPhieu);
            ps.setString(3, maMon);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean xoaMonKhoiPhieu(String maPhieu, String maMon) throws SQLException {
        String sql = "DELETE FROM ChiTietDatMon WHERE maPhieu = ? AND maMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ps.setString(2, maMon);
            return ps.executeUpdate() > 0;
        }
    }
}