package dao;

import connectSQL.ConnectSQL;
import entity.PhieuDatBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatBan_DAO {
    private Connection conn;

    public PhieuDatBan_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<PhieuDatBan> getDatBanByBan(String maBan) throws SQLException {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(rs.getString("maPhieu"));
                    d.setMaBan(rs.getString("maBan"));
                    d.setTenKhach(rs.getString("tenKhach"));
                    d.setSoDienThoai(rs.getString("soDienThoai"));
                    d.setSoNguoi(rs.getInt("soNguoi"));
                    d.setNgayDen(rs.getDate("ngayDen"));
                    d.setGioDen(rs.getTime("gioDen"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setTienCoc(rs.getDouble("tienCoc"));
                    d.setGhiChuCoc(rs.getString("ghiChuCoc"));
                    d.setTrangThai(rs.getString("trangThai"));
                    list.add(d);
                }
            }
        }
        return list;
    }

    public List<PhieuDatBan> getDatBanByBanAndNgay(String maBan, Date ngay) throws SQLException {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan WHERE MaBan = ? AND NgayDen = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            pstmt.setDate(2, ngay);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(rs.getString("maPhieu"));
                    d.setMaBan(rs.getString("maBan"));
                    d.setTenKhach(rs.getString("tenKhach"));
                    d.setSoDienThoai(rs.getString("soDienThoai"));
                    d.setSoNguoi(rs.getInt("soNguoi"));
                    d.setNgayDen(rs.getDate("ngayDen"));
                    d.setGioDen(rs.getTime("gioDen"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setTienCoc(rs.getDouble("tienCoc"));
                    d.setGhiChuCoc(rs.getString("ghiChuCoc"));
                    d.setTrangThai(rs.getString("trangThai"));
                    list.add(d);
                }
            }
        }
        return list;
    }

    public PhieuDatBan getByMa(String maPhieu) throws SQLException {
        String sql = "SELECT * FROM PhieuDatBan WHERE maPhieu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhieu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(rs.getString("maPhieu"));
                    d.setMaBan(rs.getString("maBan"));
                    d.setTenKhach(rs.getString("tenKhach"));
                    d.setSoDienThoai(rs.getString("soDienThoai"));
                    d.setSoNguoi(rs.getInt("soNguoi"));
                    d.setNgayDen(rs.getDate("ngayDen"));
                    d.setGioDen(rs.getTime("gioDen"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setTienCoc(rs.getDouble("tienCoc"));
                    d.setGhiChuCoc(rs.getString("ghiChuCoc"));
                    d.setTrangThai(rs.getString("trangThai"));
                    return d;
                }
            }
        }
        return null;
    }

    public void add(PhieuDatBan d) throws SQLException {
        String sql = "INSERT INTO PhieuDatBan (maPhieu, maBan, tenKhach, soDienThoai, soNguoi, ngayDen, gioDen, ghiChu, tienCoc, ghiChuCoc, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, CAST(? AS TIME), ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getMaPhieu());
            pstmt.setString(2, d.getMaBan());
            pstmt.setString(3, d.getTenKhach());
            pstmt.setString(4, d.getSoDienThoai());
            pstmt.setInt(5, d.getSoNguoi());
            pstmt.setDate(6, d.getNgayDen());
            pstmt.setString(7, d.getGioDen().toString()); // Sử dụng string để tránh xung đột
            pstmt.setString(8, d.getGhiChu());
            pstmt.setDouble(9, d.getTienCoc());
            pstmt.setString(10, d.getGhiChuCoc());
            pstmt.setString(11, d.getTrangThai());
            pstmt.executeUpdate();
        }
    }

    public void update(PhieuDatBan d) throws SQLException {
        String sql = "UPDATE PhieuDatBan SET maBan = ?, tenKhach = ?, soDienThoai = ?, soNguoi = ?, ngayDen = ?, gioDen = CAST(? AS TIME), " +
                     "ghiChu = ?, tienCoc = ?, ghiChuCoc = ?, trangThai = ? WHERE maPhieu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getMaBan());
            pstmt.setString(2, d.getTenKhach());
            pstmt.setString(3, d.getSoDienThoai());
            pstmt.setInt(4, d.getSoNguoi());
            pstmt.setDate(5, d.getNgayDen());
            pstmt.setString(6, d.getGioDen().toString()); // Sử dụng string để tránh xung đột
            pstmt.setString(7, d.getGhiChu());
            pstmt.setDouble(8, d.getTienCoc());
            pstmt.setString(9, d.getGhiChuCoc());
            pstmt.setString(10, d.getTrangThai());
            pstmt.setString(11, d.getMaPhieu());
            pstmt.executeUpdate();
        }
    }

    public boolean checkTrung(String maBan, java.sql.Date ngay, java.sql.Time gio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuDatBan " +
                     "WHERE maBan = ? AND ngayDen = ? AND gioDen = CAST(? AS TIME) " +
                     "AND trangThai IN (N'Đặt', N'Đang phục vụ')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            ps.setDate(2, ngay);
            ps.setString(3, gio.toString()); // Sử dụng string và CAST để tránh xung đột TIME/DATETIME
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public String generateMaPhieu() throws SQLException {
        String sql = "SELECT 'MP' + RIGHT('000' + CAST(ISNULL(MAX(CAST(SUBSTRING(maPhieu, 3, 3) AS INT)), 0) + 1 AS VARCHAR(3)), 3) AS NewmaPhieu FROM PhieuDatBan";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("NewmaPhieu");
            }
        }
        return "MP001"; // Giá trị mặc định nếu bảng rỗng
    }
}