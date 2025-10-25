package dao;

import connectSQL.ConnectSQL;
import entity.TaiKhoan;
import entity.LichSuDangNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {
    private Connection conn;
    
    public static TaiKhoan currentTaiKhoan;

    public static TaiKhoan getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    public static void resetCurrentTaiKhoan() {
        currentTaiKhoan = null;
    }

    public TaiKhoan_DAO() {
        this.conn = ConnectSQL.getConnection();
    }
    public boolean kiemTraDangNhap(String user, String pass) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement logPs = null;
        try {
            con = ConnectSQL.getConnection();
            if (con == null) {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                return false;
            }

            String sql = "SELECT t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen " +
                         "FROM TaiKhoan t INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien " +
                         "WHERE t.soDienThoai = ? OR t.maNhanVien = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, user);
            rs = ps.executeQuery();
            boolean loginSuccess = false;
            String maTaiKhoan = null;

            if (rs.next() && rs.getString("matKhau").equals(pass)) {
                currentTaiKhoan = new TaiKhoan();
                currentTaiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                currentTaiKhoan.setSoDienThoai(rs.getString("soDienThoai"));
                currentTaiKhoan.setMatKhau(rs.getString("matKhau"));
                currentTaiKhoan.setMaNhanVien(rs.getString("maNhanVien"));
                currentTaiKhoan.setPhanQuyen(rs.getString("phanQuyen"));
                currentTaiKhoan.setHoTen(rs.getString("hoTen"));
                maTaiKhoan = rs.getString("maTaiKhoan");
                try {
                    currentTaiKhoan.setPhanQuyen(currentTaiKhoan.getPhanQuyen());
                    loginSuccess = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Phân quyền không hợp lệ: " + e.getMessage());
                }
            }

            if (maTaiKhoan == null) {
                String findSql = "SELECT maTaiKhoan FROM TaiKhoan WHERE soDienThoai = ? OR maNhanVien = ?";
                PreparedStatement findPs = con.prepareStatement(findSql);
                findPs.setString(1, user);
                findPs.setString(2, user);
                ResultSet findRs = findPs.executeQuery();
                if (findRs.next()) {
                    maTaiKhoan = findRs.getString("maTaiKhoan");
                }
                findRs.close();
                findPs.close();
            }

            if (maTaiKhoan != null) {
                String logSql = "INSERT INTO LichSuDangNhap (maTaiKhoan, trangThai) VALUES (?, ?)";
                logPs = con.prepareStatement(logSql);
                logPs.setString(1, maTaiKhoan);
                logPs.setBoolean(2, loginSuccess);
                logPs.executeUpdate();
            }

            return loginSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (logPs != null) logPs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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

	public boolean xoaTaiKhoan(String maTaiKhoan) {
	    String sql = "UPDATE taikhoan SET trangThai = 0 WHERE maTaiKhoan = ?";
	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setString(1, maTaiKhoan);
	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	public List<TaiKhoan> layDanhSachTaiKhoan() {
	    List<TaiKhoan> danhSach = new ArrayList<>();
	    String sql = "SELECT t.*, n.hoTen FROM taikhoan t INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien WHERE t.trangThai = 1 ORDER BY t.maTaiKhoan";
	    
	    try (Connection conn = ConnectSQL.getInstance().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            TaiKhoan tk = new TaiKhoan();
	            tk.setMaTaiKhoan(rs.getString("maTaiKhoan"));
	            tk.setSoDienThoai(rs.getString("soDienThoai"));
	            tk.setMatKhau(rs.getString("matKhau"));
	            tk.setMaNhanVien(rs.getString("maNhanVien"));
	            tk.setPhanQuyen(rs.getString("phanQuyen"));
	            tk.setHoTen(rs.getString("hoTen"));
	            danhSach.add(tk);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return danhSach;
	}

    public List<LichSuDangNhap> layLichSuDangNhap(String maTaiKhoan) {
        List<LichSuDangNhap> dsLichSu = new ArrayList<>();
        String sql = "SELECT maLichSu, maTaiKhoan, thoiGianDangNhap, trangThai FROM LichSuDangNhap WHERE maTaiKhoan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichSuDangNhap ls = new LichSuDangNhap();
                    ls.setMaLichSu(rs.getString("maLichSu"));
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
        return "TK001";
    }

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

    public TaiKhoan layTaiKhoanDangNhapNhieuNhat() {
        String sql = "SELECT TOP 1 t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen, COUNT(ls.maLichSu) as soLanDangNhap " +
                     "FROM TaiKhoan t " +
                     "INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien " +
                     "LEFT JOIN LichSuDangNhap ls ON t.maTaiKhoan = ls.maTaiKhoan " +
                     "GROUP BY t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen " +
                     "ORDER BY soLanDangNhap DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
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