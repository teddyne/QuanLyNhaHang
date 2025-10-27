package dao;

import entity.NhanVien;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {
    private final Connection conn;

    public NhanVien_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NhanVien> getNhanVienDangLam() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, hoTen, anhNV, ngaySinh, gioiTinh, cccd, email, sdt, trangThai, chucVu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getAnhNV());
            ps.setDate(4, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setBoolean(5, nv.isGioiTinh());
            ps.setString(6, nv.getCccd());
            ps.setString(7, nv.getEmail());
            ps.setString(8, nv.getSdt());
            ps.setBoolean(9, nv.isTrangThai());
            ps.setString(10, nv.getChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTen=?, anhNV=?, ngaySinh=?, gioiTinh=?, cccd=?, email=?, sdt=?, trangThai=?, chucVu=? " +
                     "WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getAnhNV());
            ps.setDate(3, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setBoolean(4, nv.isGioiTinh());
            ps.setString(5, nv.getCccd());
            ps.setString(6, nv.getEmail());
            ps.setString(7, nv.getSdt());
            ps.setBoolean(8, nv.isTrangThai());
            ps.setString(9, nv.getChucVu());
            ps.setString(10, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean anNhanVien(String maNhanVien) {
        String sql = "UPDATE NhanVien SET trangThai = 0 WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public NhanVien timNhanVienTheoMa(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        LocalDate ngaySinh = null;
        Date sqlDate = rs.getDate("ngaySinh");
        if (sqlDate != null) {
            ngaySinh = sqlDate.toLocalDate();
        }
        return new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("hoTen"),
                rs.getString("anhNV"),
                ngaySinh,
                rs.getBoolean("gioiTinh"),
                rs.getString("cccd"),
                rs.getString("email"),
                rs.getString("sdt"),
                rs.getBoolean("trangThai"),
                rs.getString("chucVu")
        );
    }
}