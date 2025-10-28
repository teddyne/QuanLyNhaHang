package dao;

import connectSQL.ConnectSQL;
import entity.ChiTietDatMon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDatMon_DAO {
    private Connection con;

        public ChiTietDatMon_DAO(Connection con) {
            this.con = con;
        }

        public void themChiTiet(ChiTietDatMon ct) throws SQLException {
            String sql = "INSERT INTO ChiTietDatMon (maPhieu, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ct.getMaPhieu());
                ps.setString(2, ct.getMaMon());
                ps.setInt(3, ct.getSoLuong());
                ps.setDouble(4, ct.getDonGia());
                ps.setString(5, ct.getGhiChu());
                ps.executeUpdate();
            }
            System.out.println("Thêm chi tiết món cho maPhieu: " + ct.getMaPhieu());

        }

        public void xoaTheoPhieu(String maPhieu) throws SQLException {
            String sql = "DELETE FROM ChiTietDatMon WHERE maPhieu = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPhieu);
                ps.executeUpdate();
            }
        }

        public List<ChiTietDatMon> layTheoPhieu(String maPhieu) throws SQLException {
            List<ChiTietDatMon> list = new ArrayList<>();
            String sql = "SELECT * FROM ChiTietDatMon WHERE maPhieu = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPhieu);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new ChiTietDatMon(
                        rs.getString("maPhieu"),
                        rs.getString("maMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("ghiChu")
                    ));
                }
            }
            return list;
        }
    // Cập nhật số lượng (nếu cần)
    public void capNhatSoLuong(String maPhieu, String maMon, int soLuongMoi) throws SQLException {
        String sql = "UPDATE ChiTietDatMon SET soLuong = ? WHERE maPhieu = ? AND maMon = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, maPhieu);
            ps.setString(3, maMon);
            ps.executeUpdate();
        }
    }
}