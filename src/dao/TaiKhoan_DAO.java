package dao;

import connectSQL.ConnectSQL;
import entity.TaiKhoan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaiKhoan_DAO {

    public ArrayList<TaiKhoan> getAll() {
        ArrayList<TaiKhoan> ds = new ArrayList<>();
        try {
            Connection con = ConnectSQL.getConnection();
            String sql = "SELECT t.*, " +
                         " (SELECT TOP 1 thoiGianDangNhap FROM LichSuDangNhap l WHERE l.maTaiKhoan = t.maTaiKhoan ORDER BY thoiGianDangNhap DESC) AS lastLogin " +
                         "FROM TaiKhoan t";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setSoDienThoai(rs.getString("soDienThoai"));
                tk.setMatKhau(rs.getString("matKhau"));
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setPhanQuyen(rs.getString("phanQuyen"));

                Timestamp ts = rs.getTimestamp("lastLogin");
                
                ds.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public void ghiLichSuDangNhap(String maTaiKhoan) {
        try {
            Connection con = ConnectSQL.getConnection();
            String sql = "INSERT INTO LichSuDangNhap(maTaiKhoan, thoiGianDangNhap) VALUES (?, GETDATE())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maTaiKhoan);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
