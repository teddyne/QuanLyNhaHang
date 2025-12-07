package dao;

import entity.LoaiMon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaiMon_DAO {
    private final Connection conn;

    public LoaiMon_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean themLoaiMon(LoaiMon loaiMon) {
        loaiMon.setMaLoai(taoMaLoaiMoi());
        String sql = "INSERT INTO LoaiMon (maLoai, tenLoai) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getMaLoai());
            ps.setString(2, loaiMon.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatLoaiMon(LoaiMon loaiMon) {
        String sql = "UPDATE LoaiMon SET tenLoai=? WHERE maLoai=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getTenLoai());
            ps.setString(2, loaiMon.getMaLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean anLoaiMon(String maLoai) {
        String sql = "UPDATE LoaiMon SET trangThai = 0 WHERE maLoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LoaiMon> getAllLoaiMon() {
        List<LoaiMon> list = new ArrayList<>();
        String sql = "SELECT maLoai, tenLoai, trangThai FROM LoaiMon WHERE trangThai = 1"; // CHỈ LẤY LOẠI ĐANG HOẠT ĐỘNG
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiMon lm = new LoaiMon(
                    rs.getString("maLoai"),
                    rs.getString("tenLoai"),
                    rs.getInt("trangThai")  // truyền cả trạng thái để entity biết
                );
                list.add(lm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<LoaiMon> timLoaiMonTheoTen(String ten) {
        List<LoaiMon> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMon WHERE tenLoai LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String taoMaLoaiMoi() {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maLoai, 3, 4) AS INT)), 0) + 1 AS newNum FROM LoaiMon";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int newNum = rs.getInt("newNum");
                return String.format("LM%04d", newNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "LM0001";
    }
}
