package dao;

import connectSQL.ConnectSQL;
import entity.TaiKhoan;
import entity.LichSuDangNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {
    private Connection conn;

    public TaiKhoan_DAO() {
        this.conn = ConnectSQL.getConnection();
    }

    // Thêm tài khoản mới
    public boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (maTaiKhoan, soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTaiKhoan());
            ps.setString(2, tk.getSoDienThoai());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getMaNhanVien());
            ps.setString(5, tk.getPhanQuyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa tài khoản
    public boolean suaTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET soDienThoai = ?, matKhau = ?, maNhanVien = ?, phanQuyen = ? WHERE maTaiKhoan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getSoDienThoai());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNhanVien());
            ps.setString(4, tk.getPhanQuyen());
            ps.setString(5, tk.getMaTaiKhoan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật mật khẩu theo số điện thoại (dùng cho quên mật khẩu)
    public boolean capNhatMatKhauTheoSDT(String soDienThoai, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE soDienThoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setString(2, soDienThoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra số điện thoại tồn tại
    public boolean kiemTraSoDienThoaiTonTai(String soDienThoai) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE soDienThoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tài khoản (bao gồm xóa lịch sử đăng nhập liên quan)
    public boolean xoaTaiKhoan(String maTaiKhoan) {
        String deleteLichSu = "DELETE FROM LichSuDangNhap WHERE maTaiKhoan = ?";
        String deleteTaiKhoan = "DELETE FROM TaiKhoan WHERE maTaiKhoan = ?";
        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch
            try (PreparedStatement psLichSu = conn.prepareStatement(deleteLichSu);
                 PreparedStatement psTaiKhoan = conn.prepareStatement(deleteTaiKhoan)) {
                psLichSu.setString(1, maTaiKhoan);
                psTaiKhoan.setString(1, maTaiKhoan);

                int rowsLichSu = psLichSu.executeUpdate();
                int rowsTaiKhoan = psTaiKhoan.executeUpdate();

                if (rowsTaiKhoan > 0) {
                    conn.commit(); // Xác nhận giao dịch
                    return true;
                } else {
                    conn.rollback(); // Hoàn tác nếu không xóa được tài khoản
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // Khôi phục auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Lấy danh sách tất cả tài khoản
    public List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> dsTaiKhoan = new ArrayList<>();
        String sql = "SELECT t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen " +
                     "FROM TaiKhoan t INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                tk.setSoDienThoai(rs.getString("soDienThoai"));
                tk.setMatKhau(rs.getString("matKhau"));
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setPhanQuyen(rs.getString("phanQuyen"));
                tk.setHoTen(rs.getString("hoTen"));
                dsTaiKhoan.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsTaiKhoan;
    }

    // Lấy lịch sử đăng nhập theo mã tài khoản
    public List<LichSuDangNhap> layLichSuDangNhap(String maTaiKhoan) {
        List<LichSuDangNhap> dsLichSu = new ArrayList<>();
        String sql = "SELECT maLichSu, maTaiKhoan, thoiGianDangNhap, trangThai FROM LichSuDangNhap WHERE maTaiKhoan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichSuDangNhap ls = new LichSuDangNhap();
                    ls.setMaLichSu(rs.getInt("maLichSu"));
                    ls.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                    ls.setThoiGianDangNhap(rs.getTimestamp("thoiGianDangNhap"));
                    ls.setTrangThai(rs.getBoolean("trangThai"));
                    dsLichSu.add(ls);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLichSu;
    }

    // Tạo mã tài khoản mới dạng TK001
    public String taoMaTaiKhoanMoi() {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maTaiKhoan, 3, LEN(maTaiKhoan)) AS INT)), 0) AS maxId FROM TaiKhoan WHERE maTaiKhoan LIKE 'TK[0-9][0-9][0-9]'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                return String.format("TK%03d", maxId + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TK001"; // Nếu không có mã nào, bắt đầu từ TK001
    }

    // Lấy tài khoản đăng nhập gần nhất
    public TaiKhoan layTaiKhoanDangNhapGanNhat() {
        String sql = "SELECT TOP 1 t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen " +
                     "FROM TaiKhoan t INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien " +
                     "INNER JOIN LichSuDangNhap l ON t.maTaiKhoan = l.maTaiKhoan " +
                     "WHERE l.trangThai = 1 " +
                     "ORDER BY l.thoiGianDangNhap DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                tk.setSoDienThoai(rs.getString("soDienThoai"));
                tk.setMatKhau(rs.getString("matKhau"));
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setPhanQuyen(rs.getString("phanQuyen"));
                tk.setHoTen(rs.getString("hoTen"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}