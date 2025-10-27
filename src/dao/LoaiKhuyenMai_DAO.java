package dao;

import entity.LoaiKhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhuyenMai_DAO {
    private final Connection conn;

    public LoaiKhuyenMai_DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean themLoaiKhuyenMai(LoaiKhuyenMai l) {
        String sql = "INSERT INTO LoaiKhuyenMai(maLoai, tenLoai) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getMaLoai());
            ps.setString(2, l.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatLoaiKhuyenMai(LoaiKhuyenMai l) {
        String sql = "UPDATE LoaiKhuyenMai SET tenLoai=? WHERE maLoai=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getTenLoai());
            ps.setString(2, l.getMaLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaLoaiKhuyenMai(String maLoai) {
        String sql = "DELETE FROM LoaiKhuyenMai WHERE maLoai=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LoaiKhuyenMai> getAllLoaiKhuyenMai() throws SQLException {
        List<LoaiKhuyenMai> list = new ArrayList<>();
        String sql = "SELECT maLoai, tenLoai FROM LoaiKhuyenMai";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiKhuyenMai lkm = new LoaiKhuyenMai(
                    rs.getString("maLoai"),
                    rs.getString("tenLoai")
                );
                list.add(lkm);
            }
        }
        return list;
    }

    public String getMaLoaiByTenLoai(String tenLoai) {
        String sql = "SELECT maLoai FROM LoaiKhuyenMai WHERE tenLoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenLoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maLoai");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}