package dao;

import entity.Ban;
import connectSQL.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Ban_DAO {
    private final Connection conn;

    public Ban_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<Ban> getAll() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.maBan, b.maKhuVuc, kv.tenKhuVuc, b.maLoai, lb.tenLoai, b.soChoNgoi, b.trangThai, b.ghiChu " +
                     "FROM QLNH.dbo.Ban b " +
                     "JOIN QLNH.dbo.KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc " +
                     "JOIN QLNH.dbo.LoaiBan lb ON b.maLoai = lb.maLoai";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ban ban = new Ban();
                ban.setMaBan(rs.getString("maBan"));
                ban.setMaKhuVuc(rs.getString("maKhuVuc"));
                ban.setTenKhuVuc(rs.getString("tenKhuVuc"));
                ban.setMaLoai(rs.getString("maLoai"));
                ban.setTenLoai(rs.getString("tenLoai"));
                ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
                ban.setTrangThai(rs.getString("trangThai"));
                ban.setGhiChu(rs.getString("ghiChu"));
                list.add(ban);
            }
        }
        return list;
    }

    public List<Ban> getAll(String maKhuVuc) throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.maBan, b.maKhuVuc, kv.tenKhuVuc, b.maLoai, lb.tenLoai, b.soChoNgoi, b.trangThai, b.ghiChu " +
                     "FROM QLNH.dbo.Ban b " +
                     "JOIN QLNH.dbo.KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc " +
                     "JOIN QLNH.dbo.LoaiBan lb ON b.maLoai = lb.maLoai " +
                     "WHERE b.maKhuVuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuVuc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ban ban = new Ban();
                    ban.setMaBan(rs.getString("maBan"));
                    ban.setMaKhuVuc(rs.getString("maKhuVuc"));
                    ban.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    ban.setMaLoai(rs.getString("maLoai"));
                    ban.setTenLoai(rs.getString("tenLoai"));
                    ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    ban.setTrangThai(rs.getString("trangThai"));
                    ban.setGhiChu(rs.getString("ghiChu"));
                    list.add(ban);
                }
            }
        }
        return list;
    }

    public Ban getBanByMa(String maBan) throws SQLException {
        String sql = "SELECT b.maBan, b.maKhuVuc, kv.tenKhuVuc, b.maLoai, lb.tenLoai, b.soChoNgoi, b.trangThai, b.ghiChu " +
                     "FROM QLNH.dbo.Ban b " +
                     "JOIN QLNH.dbo.KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc " +
                     "JOIN QLNH.dbo.LoaiBan lb ON b.maLoai = lb.maLoai " +
                     "WHERE b.maBan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ban ban = new Ban();
                    ban.setMaBan(rs.getString("maBan"));
                    ban.setMaKhuVuc(rs.getString("maKhuVuc"));
                    ban.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    ban.setMaLoai(rs.getString("maLoai"));
                    ban.setTenLoai(rs.getString("tenLoai"));
                    ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    ban.setTrangThai(rs.getString("trangThai"));
                    ban.setGhiChu(rs.getString("ghiChu"));
                    return ban;
                }
            }
        }
        return null;
    }

    public boolean themBan(Ban ban) throws SQLException {
        String sql = "INSERT INTO QLNH.dbo.Ban (maBan, maKhuVuc, soChoNgoi, maLoai, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ban.getMaBan());
            ps.setString(2, ban.getMaKhuVuc());
            ps.setInt(3, ban.getSoChoNgoi());
            ps.setString(4, ban.getMaLoai());
            ps.setString(5, ban.getTrangThai());
            ps.setString(6, ban.getGhiChu());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean capNhatBan(Ban ban) throws SQLException {
        String sql = "UPDATE QLNH.dbo.Ban SET maKhuVuc = ?, soChoNgoi = ?, maLoai = ?, trangThai = ?, ghiChu = ? WHERE maBan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ban.getMaKhuVuc());
            ps.setInt(2, ban.getSoChoNgoi());
            ps.setString(3, ban.getMaLoai());
            ps.setString(4, ban.getTrangThai());
            ps.setString(5, ban.getGhiChu());
            ps.setString(6, ban.getMaBan());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean xoaBan(String maBan) throws SQLException {
        String sql = "DELETE FROM QLNH.dbo.Ban WHERE maBan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean checkTrung(String maBan, Date ngayDen, Time gioDen) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuDatBan WHERE maBan = ? AND CAST(ngayDen AS DATE) = ? AND CAST(gioDen AS TIME) = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maBan);
        ps.setDate(2, ngayDen);
        ps.setTime(3, gioDen);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public List<Ban> traCuuBan(String maBan, String maKhuVuc, String maLoai, String trangThai) throws SQLException {
        List<Ban> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT b.maBan, b.maKhuVuc, kv.tenKhuVuc, b.maLoai, lb.tenLoai, b.soChoNgoi, b.trangThai, b.ghiChu " +
                "FROM QLNH.dbo.Ban b " +
                "JOIN QLNH.dbo.KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc " +
                "JOIN QLNH.dbo.LoaiBan lb ON b.maLoai = lb.maLoai WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (!maBan.isEmpty()) {
            sql.append(" AND b.maBan LIKE ?");
            params.add("%" + maBan + "%");
        }
        if (!maKhuVuc.isEmpty()) {
            sql.append(" AND b.maKhuVuc = ?");
            params.add(maKhuVuc);
        }
        if (!maLoai.isEmpty()) {
            sql.append(" AND b.maLoai = ?");
            params.add(maLoai);
        }
        if (!trangThai.isEmpty()) {
            sql.append(" AND b.trangThai = ?");
            params.add(trangThai);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ban ban = new Ban();
                    ban.setMaBan(rs.getString("maBan"));
                    ban.setMaKhuVuc(rs.getString("maKhuVuc"));
                    ban.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    ban.setMaLoai(rs.getString("maLoai"));
                    ban.setTenLoai(rs.getString("tenLoai"));
                    ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    ban.setTrangThai(rs.getString("trangThai"));
                    ban.setGhiChu(rs.getString("ghiChu"));
                    list.add(ban);
                }
            }
        }
        return list;
    }

    public List<Ban> getBanTrong() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.maBan, b.maKhuVuc, kv.tenKhuVuc, b.maLoai, lb.tenLoai, b.soChoNgoi, b.trangThai, b.ghiChu " +
                     "FROM QLNH.dbo.Ban b " +
                     "JOIN QLNH.dbo.KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc " +
                     "JOIN QLNH.dbo.LoaiBan lb ON b.maLoai = lb.maLoai " +
                     "WHERE b.trangThai = N'Trống'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ban ban = new Ban();
                ban.setMaBan(rs.getString("maBan"));
                ban.setMaKhuVuc(rs.getString("maKhuVuc"));
                ban.setTenKhuVuc(rs.getString("tenKhuVuc"));
                ban.setMaLoai(rs.getString("maLoai"));
                ban.setTenLoai(rs.getString("tenLoai"));
                ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
                ban.setTrangThai(rs.getString("trangThai"));
                ban.setGhiChu(rs.getString("ghiChu"));
                list.add(ban);
            }
        }
        return list;
    }

    public void gopBan(String maBanChinh, String maBanGop) throws SQLException {
        String sqlUpdate = "UPDATE PhieuDatBan SET maBan = ? WHERE maBan = ? AND trangThai IN ('Đặt', 'Phục vụ')";
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
        psUpdate.setString(1, maBanGop);
        psUpdate.setString(2, maBanChinh);
        psUpdate.executeUpdate();

        String sqlBanChinh = "UPDATE Ban SET trangThai = 'Trống' WHERE maBan = ?";
        PreparedStatement psBanChinh = conn.prepareStatement(sqlBanChinh);
        psBanChinh.setString(1, maBanChinh);
        psBanChinh.executeUpdate();
    }

    public void chuyenBan(String maBanCu, String maBanMoi) throws SQLException {
	    String sqlUpdate = "UPDATE PhieuDatBan SET maBan = ? WHERE maBan = ? AND trangThai IN ('Đặt', 'Phục vụ')";
	    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
	    psUpdate.setString(1, maBanMoi);
	    psUpdate.setString(2, maBanCu);
	    psUpdate.executeUpdate();
	
	    String sqlBanCu = "UPDATE Ban SET trangThai = 'Trống' WHERE maBan = ?";
	    PreparedStatement psBanCu = conn.prepareStatement(sqlBanCu);
	    psBanCu.setString(1, maBanCu);
	    psBanCu.executeUpdate();
	}
}