package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import entity.Ban;

public class Ban_DAO {
    private Connection conn;

    public Ban_DAO(Connection conn) {
        this.conn = conn;
    }

    public Ban getBanByMa(String maBan) throws SQLException {
        String sql = "SELECT * FROM Ban WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maBan);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("maBan"));
            b.setKhuVuc(rs.getString("khuVuc"));
            b.setSoGhe(rs.getInt("soGhe"));
            b.setTrangThai(rs.getString("trangThai"));
            b.setX(rs.getInt("x"));
            b.setY(rs.getInt("y"));
            b.setTenKhach(rs.getString("tenKhach"));
            b.setSoDienThoai(rs.getString("soDienThoai"));
            b.setSoNguoi(rs.getInt("soNguoi"));
            b.setNgayDat(rs.getDate("ngayDat"));
            b.setGioDat(rs.getTime("gioDat"));
            b.setGhiChu(rs.getString("ghiChu"));
            b.setTienCoc(rs.getDouble("tienCoc"));
            b.setGhiChuCoc(rs.getString("ghiChuCoc"));
            return b;
        }
        return null;
    }

    public List<Ban> getAllBan(String khuVuc) throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE khuVuc = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, khuVuc);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("maBan"));
            b.setKhuVuc(rs.getString("khuVuc"));
            b.setSoGhe(rs.getInt("soGhe"));
            b.setTrangThai(rs.getString("trangThai"));
            b.setX(rs.getInt("x"));
            b.setY(rs.getInt("y"));
            list.add(b);
        }
        return list;
    }

    public List<Ban> getAllBan() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("maBan"));
            b.setKhuVuc(rs.getString("khuVuc"));
            b.setSoGhe(rs.getInt("soGhe"));
            b.setTrangThai(rs.getString("trangThai"));
            b.setX(rs.getInt("x"));
            b.setY(rs.getInt("y"));
            list.add(b);
        }
        return list;
    }

    public List<Ban> getAllBanDat() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE trangThai = 'Đặt' OR trangThai = 'Phục vụ'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("maBan"));
            b.setKhuVuc(rs.getString("khuVuc"));
            b.setSoGhe(rs.getInt("soGhe"));
            b.setTrangThai(rs.getString("trangThai"));
            b.setX(rs.getInt("x"));
            b.setY(rs.getInt("y"));
            b.setTenKhach(rs.getString("tenKhach"));
            b.setSoDienThoai(rs.getString("soDienThoai"));
            b.setSoNguoi(rs.getInt("soNguoi"));
            b.setNgayDat(rs.getDate("ngayDat"));
            b.setGioDat(rs.getTime("gioDat"));
            b.setGhiChu(rs.getString("ghiChu"));
            b.setTienCoc(rs.getDouble("tienCoc"));
            b.setGhiChuCoc(rs.getString("ghiChuCoc"));
            list.add(b);
        }
        return list;
    }

    public List<Ban> getAllBanTrong() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE trangThai = 'Trống'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("maBan"));
            b.setKhuVuc(rs.getString("khuVuc"));
            b.setSoGhe(rs.getInt("soGhe"));
            b.setTrangThai(rs.getString("trangThai"));
            b.setX(rs.getInt("x"));
            b.setY(rs.getInt("y"));
            list.add(b);
        }
        return list;
    }

    public void datBan(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET tenKhach = ?, soDienThoai = ?, soNguoi = ?, ngayDat = ?, gioDat = ?, ghiChu = ?, tienCoc = ?, ghiChuCoc = ?, trangThai = ? WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getTenKhach());
        ps.setString(2, b.getSoDienThoai());
        ps.setInt(3, b.getSoNguoi());
        ps.setDate(4, b.getNgayDat());
        ps.setTime(5, b.getGioDat());
        ps.setString(6, b.getGhiChu());
        ps.setDouble(7, b.getTienCoc());
        ps.setString(8, b.getGhiChuCoc());
        ps.setString(9, b.getTrangThai());
        ps.setString(10, b.getMaBan());
        ps.executeUpdate();
    }

    public void huyBan(String maBan, String lyDo) throws SQLException {
        String sql = "UPDATE Ban SET tenKhach = NULL, soDienThoai = NULL, soNguoi = 0, ngayDat = NULL, gioDat = NULL, ghiChu = ?, tienCoc = 0, ghiChuCoc = NULL, trangThai = 'Trống' WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "Hủy: " + lyDo);
        ps.setString(2, maBan);
        ps.executeUpdate();
    }

    public void gopBan(String banChinh, String banGop) throws SQLException {
        Ban bGop = getBanByMa(banGop);
        Ban bChinh = getBanByMa(banChinh);
        bChinh.setSoNguoi(bChinh.getSoNguoi() + bGop.getSoNguoi());
        bChinh.setGhiChu(bChinh.getGhiChu() + "; Gộp từ " + banGop);
        bChinh.setTrangThai("Đặt");
        capNhatBan(bChinh);
        huyBan(banGop, "Gộp vào " + banChinh);
    }

    public void chuyenBan(String banCu, String banMoi) throws SQLException {
        Ban bCu = getBanByMa(banCu);
        Ban bMoi = getBanByMa(banMoi);
        bMoi.setTenKhach(bCu.getTenKhach());
        bMoi.setSoDienThoai(bCu.getSoDienThoai());
        bMoi.setSoNguoi(bCu.getSoNguoi());
        bMoi.setNgayDat(bCu.getNgayDat());
        bMoi.setGioDat(bCu.getGioDat());
        bMoi.setGhiChu(bCu.getGhiChu() + "; Chuyển từ " + banCu);
        bMoi.setTienCoc(bCu.getTienCoc());
        bMoi.setGhiChuCoc(bCu.getGhiChuCoc());
        bMoi.setTrangThai(bCu.getTrangThai());
        capNhatBan(bMoi);
        huyBan(banCu, "Chuyển sang " + banMoi);
    }

    public void capNhatTrangThai(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET trangThai = ? WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getTrangThai());
        ps.setString(2, b.getMaBan());
        ps.executeUpdate();
    }

    public void capNhatBan(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET khuVuc = ?, soGhe = ?, trangThai = ?, x = ?, y = ?, tenKhach = ?, soDienThoai = ?, soNguoi = ?, ngayDat = ?, gioDat = ?, ghiChu = ?, tienCoc = ?, ghiChuCoc = ? WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getKhuVuc());
        ps.setInt(2, b.getSoGhe());
        ps.setString(3, b.getTrangThai());
        ps.setInt(4, b.getX());
        ps.setInt(5, b.getY());
        ps.setString(6, b.getTenKhach());
        ps.setString(7, b.getSoDienThoai());
        ps.setInt(8, b.getSoNguoi());
        ps.setDate(9, b.getNgayDat());
        ps.setTime(10, b.getGioDat());
        ps.setString(11, b.getGhiChu());
        ps.setDouble(12, b.getTienCoc());
        ps.setString(13, b.getGhiChuCoc());
        ps.setString(14, b.getMaBan());
        ps.executeUpdate();
    }

    public void themBan(Ban b) throws SQLException {
        String sql = "INSERT INTO Ban (maBan, khuVuc, soGhe, trangThai, x, y) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getMaBan());
        ps.setString(2, b.getKhuVuc());
        ps.setInt(3, b.getSoGhe());
        ps.setString(4, b.getTrangThai());
        ps.setInt(5, b.getX());
        ps.setInt(6, b.getY());
        ps.executeUpdate();
    }

    public void xoaBan(String maBan) throws SQLException {
        String sql = "DELETE FROM Ban WHERE maBan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maBan);
        ps.executeUpdate();
    }
    
    
}