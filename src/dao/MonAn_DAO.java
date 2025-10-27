package dao;

import connectSQL.ConnectSQL;
import entity.LoaiMon;
import entity.MonAn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonAn_DAO {
    private final Connection conn;

    public MonAn_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean themMonAn(MonAn mon) throws SQLException {
        mon.setMaMon(taoMaMonMoi());
        String sql = "INSERT INTO MonAn (maMon, tenMon, anhMon, maLoai, donGia, trangThai, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMon());
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getAnhMon());
            ps.setString(4, mon.getLoaiMon().getMaLoai());
            ps.setDouble(5, mon.getDonGia());
            ps.setBoolean(6, mon.isTrangThai());
            ps.setString(7, mon.getMoTa());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean capNhatMonAn(MonAn mon) throws SQLException {
        String sql = "UPDATE MonAn SET tenMon=?, anhMon=?, maLoai=?, donGia=?, trangThai=?, moTa=? WHERE maMon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setString(2, mon.getAnhMon());
            ps.setString(3, mon.getLoaiMon().getMaLoai());
            ps.setDouble(4, mon.getDonGia());
            ps.setBoolean(5, mon.isTrangThai());
            ps.setString(6, mon.getMoTa());
            ps.setString(7, mon.getMaMon());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean anMonAn(String maMon) throws SQLException {
        String sql = "UPDATE MonAn SET trangThai = 0 WHERE maMon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        }
    }

    public List<MonAn> getAllMonAn() {
        List<MonAn> list = new ArrayList<>();
        String sql = """
            SELECT
                m.maMon, m.tenMon, m.anhMon, m.donGia, m.trangThai, m.moTa,
                lm.maLoai, lm.tenLoai
            FROM MonAn m
            LEFT JOIN LoaiMon lm ON m.maLoai = lm.maLoai
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiMon loaiMon = new LoaiMon(
                    rs.getString("maLoai"),
                    rs.getString("tenLoai")
                );
                MonAn mon = new MonAn(
                    rs.getString("maMon"),
                    rs.getString("tenMon"),
                    rs.getString("anhMon"),
                    loaiMon,
                    rs.getDouble("donGia"),
                    rs.getBoolean("trangThai"),
                    rs.getString("moTa")
                );
                list.add(mon);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn MonAn: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<MonAn> timMonAnTheoTen(String ten) throws SQLException {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT m.*, l.tenLoai FROM MonAn m JOIN LoaiMon l ON m.maLoai = l.maLoai WHERE tenMon LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoaiMon loai = new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai"));
                    MonAn mon = new MonAn(
                            rs.getString("maMon"),
                            rs.getString("tenMon"),
                            rs.getString("anhMon"),
                            loai,
                            rs.getDouble("donGia"),
                            rs.getBoolean("trangThai"),
                            rs.getString("moTa")
                    );
                    list.add(mon);
                }
            }
        }
        return list;
    }

    public List<MonAn> timMonTheoLoai(String tenLoai) throws SQLException {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT m.*, l.maLoai, l.tenLoai FROM MonAn m JOIN LoaiMon l ON m.maLoai = l.maLoai WHERE l.tenLoai LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tenLoai + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoaiMon loai = new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai"));
                    MonAn mon = new MonAn(
                            rs.getString("maMon"),
                            rs.getString("tenMon"),
                            rs.getString("anhMon"),
                            loai,
                            rs.getDouble("donGia"),
                            rs.getBoolean("trangThai"),
                            rs.getString("moTa")
                    );
                    list.add(mon);
                }
            }
        }
        return list;
    }

    public MonAn layMonAnTheoMa(String maMon) {
        String sql = """
            SELECT m.*, lm.maLoai, lm.tenLoai
            FROM MonAn m
            LEFT JOIN LoaiMon lm ON m.maLoai = lm.maLoai
            WHERE m.maMon = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiMon loaiMon = new LoaiMon(
                        rs.getString("maLoai"),
                        rs.getString("tenLoai")
                    );
                    return new MonAn(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("anhMon"),
                        loaiMon,
                        rs.getDouble("donGia"),
                        rs.getBoolean("trangThai"),
                        rs.getString("moTa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String taoMaMonMoi() throws SQLException {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maMon, 4, 4) AS INT)), 0) + 1 as newNum FROM MonAn";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int newNum = rs.getInt("newNum");
                return String.format("MON%04d", newNum);
            }
        }
        return "MON0001";
    }
}