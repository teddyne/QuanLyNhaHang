package dao;

import entity.KhuVuc;
import connectSQL.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhuVuc_DAO {
    private Connection conn;

    public KhuVuc_DAO(Connection conn) {
        this.conn = conn;
    }

    public KhuVuc_DAO() {
		// TODO Auto-generated constructor stub
	}

	public List<KhuVuc> getAll() throws SQLException {
        List<KhuVuc> list = new ArrayList<>();
        String sql = "SELECT MaKhuVuc, TenKhuVuc, SoLuongBan, TrangThai FROM KhuVuc";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                KhuVuc k = new KhuVuc();
                k.setMaKhuVuc(rs.getString("MaKhuVuc"));
                k.setTenKhuVuc(rs.getString("TenKhuVuc"));
                k.setSoLuongBan(rs.getInt("SoLuongBan"));
                k.setTrangThai(rs.getString("TrangThai"));
                list.add(k);
            }
        }
        return list;
    }

    public KhuVuc getByMa(String maKhuVuc) throws SQLException {
        String sql = "SELECT MaKhuVuc, TenKhuVuc, SoLuongBan, TrangThai FROM KhuVuc WHERE MaKhuVuc = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    KhuVuc k = new KhuVuc();
                    k.setMaKhuVuc(rs.getString("MaKhuVuc"));
                    k.setTenKhuVuc(rs.getString("TenKhuVuc"));
                    k.setSoLuongBan(rs.getInt("SoLuongBan"));
                    k.setTrangThai(rs.getString("TrangThai"));
                    return k;
                }
            }
        }
        return null;
    }

    public void add(KhuVuc k) throws SQLException {
        String sql = "INSERT INTO KhuVuc (MaKhuVuc, TenKhuVuc, SoLuongBan, TrangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getMaKhuVuc());
            pstmt.setString(2, k.getTenKhuVuc());
            pstmt.setInt(3, k.getSoLuongBan());
            pstmt.setString(4, k.getTrangThai());
            pstmt.executeUpdate();
        }
    }

    public void update(KhuVuc k) throws SQLException {
        String sql = "UPDATE KhuVuc SET TenKhuVuc = ?, SoLuongBan = ?, TrangThai = ? WHERE MaKhuVuc = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getTenKhuVuc());
            pstmt.setInt(2, k.getSoLuongBan());
            pstmt.setString(3, k.getTrangThai());
            pstmt.setString(4, k.getMaKhuVuc());
            pstmt.executeUpdate();
        }
    }

    public void delete(String maKhuVuc) throws SQLException {
        String sql = "DELETE FROM KhuVuc WHERE MaKhuVuc = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            pstmt.executeUpdate();
        }
    }
}