package dao;

import connectSQL.ConnectSQL;
import entity.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {

	public List<Object[]> layDanhSachHoaDonDayDu() {
	    List<Object[]> ds = new ArrayList<>();
	    String sql = "SELECT hd.maHD, b.maBan, hd.ngayLap, kh.tenKH, kh.sdt, nv.hoTen, km.tenKM, " +
	                 "ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongTien " +
	                 "FROM HoaDon hd " +
	                 "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH " +
	                 "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
	                 "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
	                 "LEFT JOIN PhieuDatBan p ON hd.maPhieu = p.maPhieu " +
	                 "LEFT JOIN Ban b ON p.maBan = b.maBan " +
	                 "LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD " +
	                 "GROUP BY hd.maHD, b.maBan, hd.ngayLap, kh.tenKH, kh.sdt, nv.hoTen, km.tenKM";

	    try (Connection con = ConnectSQL.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            Object[] row = {
	                    rs.getString("maHD"),
	                    rs.getString("maBan"),
	                    rs.getDate("ngayLap"),
	                    rs.getString("tenKH"),
	                    rs.getString("sdt"),
	                    rs.getString("hoTen"),
	                    rs.getString("tenKM"),
	                    rs.getDouble("tongTien")
	            };
	            ds.add(row);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	public Object[] layThongTinHoaDon(String maHD) {
        Object[] thongTin = null;
        Connection con = ConnectSQL.getConnection();
        try {
            String sql = """
                SELECT kh.tenKH, kh.sdt, b.maBan, nv.hoTen, hd.ngayLap, km.tenKM,
                       SUM(ct.soLuong * ct.donGia) AS tongTien
                FROM HoaDon hd
                JOIN KhachHang kh ON hd.maKH = kh.maKH
                JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
                JOIN PhieuDatBan pdb ON hd.maPhieu = pdb.maPhieu
                JOIN Ban b ON pdb.maBan = b.maBan
                LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM
                JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD
                WHERE hd.maHD = ?
                GROUP BY kh.tenKH, kh.sdt, b.maBan, nv.hoTen, hd.ngayLap, km.tenKM
            """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                thongTin = new Object[]{
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getString("maBan"),
                        rs.getString("hoTen"),
                        rs.getDate("ngayLap"),
                        rs.getString("tenKM"),
                        rs.getDouble("tongTien")
                };
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thongTin;
    }

    public List<Object[]> layChiTietHoaDon(String maHD) {
        List<Object[]> list = new ArrayList<>();
        Connection con = ConnectSQL.getConnection();
        try {
            String sql = """
                SELECT ma.maMon, ma.tenMon, ct.soLuong, ct.donGia, (ct.soLuong * ct.donGia) AS thanhTien
                FROM ChiTietHoaDon ct
                JOIN MonAn ma ON ct.maMon = ma.maMon
                WHERE ct.maHD = ?
            """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getDouble("thanhTien")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
