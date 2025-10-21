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
    private final Connection conn;

    public KhuVuc_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<KhuVuc> getAll() throws SQLException {
        List<KhuVuc> list = new ArrayList<>();
        String sql = "SELECT maKhuVuc, tenKhuVuc, trangThai, " +
                     "(SELECT COUNT(*) FROM Ban WHERE maKhuVuc = KhuVuc.maKhuVuc AND trangThai != 'Ẩn') AS soLuongBan " +
                     "FROM KhuVuc WHERE trangThai != 'Ẩn'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuVuc kv = new KhuVuc();
                kv.setMaKhuVuc(rs.getString("maKhuVuc"));
                kv.setTenKhuVuc(rs.getString("tenKhuVuc"));
                kv.setSoLuongBan(rs.getInt("soLuongBan"));
                kv.setTrangThai(rs.getString("trangThai"));
                list.add(kv);
            }
        }
        return list;
    }

    public KhuVuc getByMa(String maKhuVuc) throws SQLException {
        String sql = "SELECT maKhuVuc, tenKhuVuc, trangThai, " +
                     "(SELECT COUNT(*) FROM Ban WHERE maKhuVuc = KhuVuc.maKhuVuc AND trangThai != 'Ẩn') AS soLuongBan " +
                     "FROM KhuVuc WHERE maKhuVuc = ? AND trangThai != 'Ẩn'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuVuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhuVuc kv = new KhuVuc();
                    kv.setMaKhuVuc(rs.getString("maKhuVuc"));
                    kv.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    kv.setSoLuongBan(rs.getInt("soLuongBan"));
                    kv.setTrangThai(rs.getString("trangThai"));
                    return kv;
                }
            }
        }
        return null;
    }
    public List<KhuVuc> traCuuKhuVuc(String maKhuVuc, String tenKhuVuc, String trangThai) throws SQLException {
        List<KhuVuc> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT maKhuVuc, tenKhuVuc, trangThai, " +
            "(SELECT COUNT(*) FROM Ban WHERE maKhuVuc = KhuVuc.maKhuVuc AND trangThai != 'Ẩn') AS soLuongBan " +
            "FROM KhuVuc WHERE trangThai != 'Ẩn'"
        );
        List<String> params = new ArrayList<>();
        
        if (!maKhuVuc.isEmpty()) {
            sql.append(" AND maKhuVuc LIKE ?");
            params.add("%" + maKhuVuc + "%");
        }
        if (!tenKhuVuc.isEmpty()) {
            sql.append(" AND tenKhuVuc LIKE ?");
            params.add("%" + tenKhuVuc + "%");
        }
        if (!trangThai.isEmpty()) {
            sql.append(" AND trangThai = ?");
            params.add(trangThai);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhuVuc kv = new KhuVuc();
                    kv.setMaKhuVuc(rs.getString("maKhuVuc"));
                    kv.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    kv.setSoLuongBan(rs.getInt("soLuongBan"));
                    kv.setTrangThai(rs.getString("trangThai"));
                    list.add(kv);
                }
            }
        }
        return list;
    }

    public boolean them(KhuVuc kv) throws SQLException {
        String sql = "INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, trangThai) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kv.getMaKhuVuc());
            ps.setString(2, kv.getTenKhuVuc());
            ps.setString(3, kv.getTrangThai());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean capNhat(KhuVuc kv) throws SQLException {
        String sql = "UPDATE KhuVuc SET tenKhuVuc = ?, trangThai = ? WHERE maKhuVuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kv.getTenKhuVuc());
            ps.setString(2, kv.getTrangThai());
            ps.setString(3, kv.getMaKhuVuc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean anKhuVuc(String maKhuVuc) throws SQLException {
        String sql = "UPDATE KhuVuc SET trangThai = 'Ẩn' WHERE maKhuVuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuVuc);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean checkTrung(String maKhuVuc) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Ban WHERE maKhuVuc = ? AND trangThai = 'Đặt'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuVuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int countBan(String maKhuVuc) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Ban WHERE maKhuVuc = ? AND trangThai != 'Ẩn'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuVuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}