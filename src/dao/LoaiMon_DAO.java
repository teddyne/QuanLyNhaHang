package dao;

import entity.LoaiMon;
import connectSQL.ConnectSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiMon_DAO {

    // Thêm loại món với mã tự tăng
    public boolean themLoaiMon(LoaiMon loaiMon) {
        loaiMon.setMaLoai(taoMaLoaiMoi());
        String sql = "INSERT INTO LoaiMon (maLoai, tenLoai) VALUES (?, ?)";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getMaLoai());
            ps.setString(2, loaiMon.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật tên loại món
    public boolean capNhatLoaiMon(LoaiMon loaiMon) {
        String sql = "UPDATE LoaiMon SET tenLoai=? WHERE maLoai=?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getTenLoai());
            ps.setString(2, loaiMon.getMaLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ẩn loại món
    public boolean anLoaiMon(String maLoai) {
        String sql = "UPDATE LoaiMon SET trangThai = 0 WHERE maLoai = ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả loại món
    public List<LoaiMon> getAllLoaiMon() {
        List<LoaiMon> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMon";
        try (Connection con = ConnectSQL.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm loại món theo tên (hoặc một phần tên)
    public List<LoaiMon> timLoaiMonTheoTen(String ten) {
        List<LoaiMon> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMon WHERE tenLoai LIKE ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
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

    // Tạo mã loại món mới tự tăng
    private String taoMaLoaiMoi() {
        List<LoaiMon> list = getAllLoaiMon();
        int max = 0;
        for (LoaiMon lm : list) {
            try {
                int num = Integer.parseInt(lm.getMaLoai().replaceAll("\\D", ""));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }
        return String.format("LM%04d", max + 1);
    }
}
