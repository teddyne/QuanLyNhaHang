package dao;

import connectSQL.ConnectSQL;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO {

    public List<ChiTietHoaDon> getChiTietTheoHoaDon(String maHD) {
        List<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        Connection con = ConnectSQL.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = """
                SELECT cthd.maHD, cthd.maMon, ma.tenMon, cthd.soLuong, cthd.donGia, cthd.ghiChu
                FROM ChiTietHoaDon cthd
                JOIN MonAn ma ON cthd.maMon = ma.maMon
                WHERE cthd.maHD = ?
            """;

            stmt = con.prepareStatement(sql);
            stmt.setString(1, maHD);
            rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));

                MonAn mon = new MonAn();
                mon.setMaMon(rs.getString("maMon"));
                mon.setTenMon(rs.getString("tenMon"));

                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");
                String ghiChu = rs.getString("ghiChu");

                ChiTietHoaDon cthd = new ChiTietHoaDon(hd, mon, soLuong, donGia, ghiChu);
                dsChiTiet.add(cthd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi lấy chi tiết hóa đơn với mã: " + maHD);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsChiTiet;
    }
}
