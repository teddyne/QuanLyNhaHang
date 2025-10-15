package dao;

import entity.Ban;
import connectSQL.ConnectSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Ban_DAO {
    private Connection conn;

    public Ban_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<Ban> getAllBan(String maKhuVuc) throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.maBan, b.maKhuVuc, b.soChoNgoi, b.loaiBan, b.trangThai, b.ghiChu, k.tenKhuVuc " +
                     "FROM Ban b " +
                     "LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc " +
                     "WHERE b.maKhuVuc = ? OR ? IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            pstmt.setString(2, maKhuVuc);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ban b = new Ban();
                    b.setMaBan(rs.getString("maBan"));
                    b.setMaKhuVuc(rs.getString("maKhuVuc"));
                    b.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    b.setLoaiBan(rs.getString("loaiBan"));
                    b.setTrangThai(rs.getString("trangThai"));
                    b.setGhiChu(rs.getString("ghiChu"));
                    b.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    list.add(b);
                }
            }
        }
        return list;
    }

    public boolean checkTrung(String maBan, Date ngay, Time gio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuDatBan WHERE maBan = ? AND ngayDen = ? AND gioDen = ? AND trangThai NOT IN ('Hủy')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            pstmt.setDate(2, ngay);
            pstmt.setTime(3, gio);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Ban getBanByMa(String maBan) throws SQLException {
        String sql = "SELECT b.maBan, b.maKhuVuc, b.soChoNgoi, b.loaiBan, b.trangThai, b.ghiChu, k.tenKhuVuc " +
                     "FROM Ban b " +
                     "LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc " +
                     "WHERE b.maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ban b = new Ban();
                    b.setMaBan(rs.getString("maBan"));
                    b.setMaKhuVuc(rs.getString("maKhuVuc"));
                    b.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    b.setLoaiBan(rs.getString("loaiBan"));
                    b.setTrangThai(rs.getString("trangThai"));
                    b.setGhiChu(rs.getString("ghiChu"));
                    b.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    return b;
                }
            }
        }
        return null;
    }

    public void themBan(Ban b) throws SQLException {
        String sql = "INSERT INTO Ban (maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getMaBan());
            pstmt.setString(2, b.getMaKhuVuc());
            pstmt.setInt(3, b.getSoChoNgoi());
            pstmt.setString(4, b.getLoaiBan());
            pstmt.setString(5, b.getTrangThai());
            pstmt.setString(6, b.getGhiChu());
            pstmt.executeUpdate();
        }
    }

    public void capNhatBan(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET maKhuVuc = ?, soChoNgoi = ?, loaiBan = ?, trangThai = ?, ghiChu = ? " +
                     "WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getMaKhuVuc());
            pstmt.setInt(2, b.getSoChoNgoi());
            pstmt.setString(3, b.getLoaiBan());
            pstmt.setString(4, b.getTrangThai());
            pstmt.setString(5, b.getGhiChu());
            pstmt.setString(6, b.getMaBan());
            pstmt.executeUpdate();
        }
    }

    public void xoaBan(String maBan) throws SQLException {
        String sql = "DELETE FROM Ban WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            pstmt.executeUpdate();
        }
    }

    public void gopBan(String banChinh, String banGop) throws SQLException {
        String sqlUpdate = "UPDATE PhieuDatBan SET maBan = ? WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, banChinh);
            pstmt.setString(2, banGop);
            pstmt.executeUpdate();
        }
        xoaBan(banGop);
    }

    public void chuyenBan(String banCu, String banMoi) throws SQLException {
        String sqlUpdate = "UPDATE PhieuDatBan SET maBan = ? WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, banMoi);
            pstmt.setString(2, banCu);
            pstmt.executeUpdate();
        }
        String sqlBan = "UPDATE Ban SET trangThai = 'Trống' WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlBan)) {
            pstmt.setString(1, banCu);
            pstmt.executeUpdate();
        }
    }

    // Trong lớp Ban_DAO
	public List<Ban> getBanTrong() throws SQLException {
	    List<Ban> danhSachBan = new ArrayList<>();
	    String sql = "SELECT b.maBan, b.soChoNgoi, b.loaiBan, k.tenKhuVuc " +
	                 "FROM Ban b " +
	                 "LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc " +
	                 "LEFT JOIN PhieuDatBan pdb ON b.maBan = pdb.maBan " +
	                 "AND pdb.ngayDen = CAST(GETDATE() AS DATE) " +
	                 "WHERE pdb.maPhieu IS NULL OR pdb.trangThai NOT IN (N'Đặt', N'Đang phục vụ')";
	
        PreparedStatement stmt = conn.prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
	
	    while (rs.next()) {
	        Ban ban = new Ban();
	        ban.setMaBan(rs.getString("maBan"));
	        ban.setSoChoNgoi(rs.getInt("soChoNgoi"));
	        ban.setLoaiBan(rs.getString("loaiBan"));
	        String tenKhuVuc = rs.getString("tenKhuVuc");
	        ban.setTenKhuVuc(tenKhuVuc != null ? tenKhuVuc : "Không xác định");
	        danhSachBan.add(ban);
	    }
	    rs.close();
	    stmt.close();
	    return danhSachBan;
	}
    public List<Ban> getAll() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT b.maBan, b.maKhuVuc, b.soChoNgoi, b.loaiBan, b.trangThai, b.ghiChu, k.tenKhuVuc " +
                     "FROM Ban b " +
                     "LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Ban b = new Ban();
                b.setMaBan(rs.getString("maBan"));
                b.setMaKhuVuc(rs.getString("maKhuVuc"));
                b.setSoChoNgoi(rs.getInt("soChoNgoi"));
                b.setLoaiBan(rs.getString("loaiBan"));
                b.setTrangThai(rs.getString("trangThai"));
                b.setGhiChu(rs.getString("ghiChu"));
                b.setTenKhuVuc(rs.getString("tenKhuVuc"));
                list.add(b);
            }
        }
        return list;
    }

    public String layMaBan(String maBan) throws SQLException {
        String sql = "SELECT maBan FROM Ban WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maBan");
                }
            }
        }
        return null;
    }
    
    public List<Ban> traCuuBan(String maBan, String maKhuVuc, String trangThai, String loaiBan) throws SQLException {
        List<Ban> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT b.maBan, b.maKhuVuc, b.soChoNgoi, b.loaiBan, b.trangThai, b.ghiChu, k.tenKhuVuc " +
            "FROM Ban b " +
            "LEFT JOIN KhuVuc k ON b.maKhuVuc = k.maKhuVuc WHERE 1=1"
        );

        List<String> params = new ArrayList<>();
        if (maBan != null && !maBan.isEmpty()) {
            sql.append(" AND b.maBan LIKE ?");
            params.add("%" + maBan + "%");
        }
        if (maKhuVuc != null && !maKhuVuc.isEmpty()) {
            sql.append(" AND b.maKhuVuc = ?");
            params.add(maKhuVuc);
        }
        if (trangThai != null && !trangThai.isEmpty()) {
            sql.append(" AND b.trangThai = ?");
            params.add(trangThai);
        }
        if (loaiBan != null && !loaiBan.isEmpty()) {
            sql.append(" AND b.loaiBan = ?");
            params.add(loaiBan);
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ban b = new Ban();
                    b.setMaBan(rs.getString("maBan"));
                    b.setMaKhuVuc(rs.getString("maKhuVuc"));
                    b.setSoChoNgoi(rs.getInt("soChoNgoi"));
                    b.setLoaiBan(rs.getString("loaiBan"));
                    b.setTrangThai(rs.getString("trangThai"));
                    b.setGhiChu(rs.getString("ghiChu"));
                    b.setTenKhuVuc(rs.getString("tenKhuVuc"));
                    list.add(b);
                }
            }
        }
        return list;
    }
}