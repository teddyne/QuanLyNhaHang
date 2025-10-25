package dao;

import connectSQL.ConnectSQL;
import entity.LoaiKhuyenMai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhuyenMai_DAO {
    private Connection conn;

    public LoaiKhuyenMai_DAO() {
        try {
            conn = ConnectSQL.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean themLoaiKhuyenMai(LoaiKhuyenMai l) {
        String sql = "INSERT INTO LoaiKhuyenMai(maLoai, tenLoai) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getMaLoai());
            ps.setString(2, l.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
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
            return false;
        }
    }

    public boolean xoaLoaiKhuyenMai(String maLoai) {
        String sql = "DELETE FROM LoaiKhuyenMai WHERE maLoai=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<LoaiKhuyenMai> getAllLoaiKhuyenMai() throws SQLException {
        List<LoaiKhuyenMai> list = new ArrayList<>();
        String sql = "SELECT maLoai, tenLoai FROM LoaiKhuyenMai";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LoaiKhuyenMai lkm = new LoaiKhuyenMai(
                rs.getString("maLoai"),
                rs.getString("tenLoai")
            );
            list.add(lkm);
        }
        rs.close();
        ps.close();
        return list;
    }
    
    public String getMaLoaiByTenLoai(String tenLoai) {
        String sql = "SELECT maLoai FROM LoaiKhuyenMai WHERE tenLoai = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenLoai);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("maLoai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}