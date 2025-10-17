package dao;

import entity.LoaiBan;
import connectSQL.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaiBan_DAO {
    private final Connection conn;

    public LoaiBan_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<LoaiBan> getAll() throws SQLException {
        List<LoaiBan> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiBan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiBan lb = new LoaiBan();
                lb.setMaLoai(rs.getString("maLoai"));
                lb.setTenLoai(rs.getString("tenLoai"));
                list.add(lb);
            }
        }
        return list;
    }

    public LoaiBan getLoaiBanByMa(String maLoai) throws SQLException {
        String sql = "SELECT * FROM LoaiBan WHERE maLoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiBan lb = new LoaiBan();
                    lb.setMaLoai(rs.getString("maLoai"));
                    lb.setTenLoai(rs.getString("tenLoai"));
                    return lb;
                }
            }
        }
        return null;
    }

    public boolean themLoaiBan(LoaiBan lb) throws SQLException {
        String sql = "INSERT INTO LoaiBan (maLoai, tenLoai) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lb.getMaLoai());
            ps.setString(2, lb.getTenLoai());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean capNhatLoaiBan(LoaiBan lb) throws SQLException {
        String sql = "UPDATE LoaiBan SET tenLoai = ? WHERE maLoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lb.getTenLoai());
            ps.setString(2, lb.getMaLoai());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean xoaLoaiBan(String maLoai) throws SQLException {
        String sql = "DELETE FROM LoaiBan WHERE maLoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        }
    }
}