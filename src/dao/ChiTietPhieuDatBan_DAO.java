package dao;

import connectSQL.ConnectSQL;
import entity.ChiTietPhieuDatBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDatBan_DAO {

    private Connection con;

    public ChiTietPhieuDatBan_DAO() {
        con = ConnectSQL.getConnection();
    }

    public ChiTietPhieuDatBan_DAO(Connection con) {
        this.con = con;
    }

    public List<ChiTietPhieuDatBan> layTheoMaPhieu(String maPhieu) throws SQLException {

        List<ChiTietPhieuDatBan> danhSach = new ArrayList<>();

        String sql = """
            SELECT c.maPhieu, c.maMon, m.tenMon,
                   c.soLuong, c.donGia, c.ghiChu
            FROM ChiTietPhieuDatBan c
            JOIN MonAn m ON c.maMon = m.maMon
            WHERE c.maPhieu = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietPhieuDatBan ct = new ChiTietPhieuDatBan();
                ct.setMaPhieu(rs.getString("maPhieu"));
                ct.setMaMon(rs.getString("maMon"));
                ct.setTenMon(rs.getString("tenMon")); 
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setDonGia(rs.getDouble("donGia"));
                ct.setGhiChu(rs.getString("ghiChu"));
                danhSach.add(ct);
            }
        }
        return danhSach;
    }

    public void themMon(ChiTietPhieuDatBan ct) throws SQLException {

        String sql = """
            INSERT INTO ChiTietPhieuDatBan
            (maPhieu, maMon, soLuong, donGia, ghiChu)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPhieu());
            ps.setString(2, ct.getMaMon());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getGhiChu());
            ps.executeUpdate();
        }
    }

    public void capNhatMon(ChiTietPhieuDatBan ct) throws SQLException {

        String sql = """
            UPDATE ChiTietPhieuDatBan
            SET soLuong = ?, donGia = ?, ghiChu = ?
            WHERE maPhieu = ? AND maMon = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getGhiChu());
            ps.setString(4, ct.getMaPhieu());
            ps.setString(5, ct.getMaMon());
            ps.executeUpdate();
        }
    }

     public void xoaMon(String maPhieu, String maMon) throws SQLException {

        String sql = "DELETE FROM ChiTietPhieuDatBan WHERE maPhieu = ? AND maMon = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ps.setString(2, maMon);
            ps.executeUpdate();
        }
    }

    public boolean tonTaiMon(String maPhieu, String maMon) throws SQLException {

        String sql = "SELECT COUNT(*) FROM ChiTietPhieuDatBan WHERE maPhieu = ? AND maMon = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ps.setString(2, maMon);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void luuMon(ChiTietPhieuDatBan ct) throws SQLException {
        if (tonTaiMon(ct.getMaPhieu(), ct.getMaMon())) {
            capNhatMon(ct);
        } else {
            themMon(ct);
        }
    }

    public double tinhTongTien(String maPhieu) throws SQLException {

        String sql = """
            SELECT SUM(soLuong * donGia)
            FROM ChiTietPhieuDatBan
            WHERE maPhieu = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

 
    public void dongKetNoi() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
