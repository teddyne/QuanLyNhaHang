package dao;

import entity.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectSQL.ConnectSQL;

public class ChiTietHoaDon_DAO {
    //private final Connection conn;

//    public ChiTietHoaDon_DAO() {
//        this.conn = conn;
//    }

    public static boolean themChiTiet(ChiTietHoaDon ct) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maMon, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaMon());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setString(5, ct.getGhiChu());
            return ps.executeUpdate() > 0;
        }
    }

    public List<ChiTietHoaDon> layChiTietTheoHoaDon(String maHoaDon) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon(sql, sql, 0, 0, sql);
                    ct.setMaHD(rs.getString("maHD"));
                    ct.setMaMon(rs.getString("maMon"));
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getDouble("donGia"));
                    ct.setGhiChu(rs.getString("ghiChu"));
                    list.add(ct);
                }
            }
        }
        return list;
    }

    public static boolean xoaChiTietTheoHoaDon(String maHoaDon) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHD = ?";
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<String> getDanhSachMonTheoHoaDon(String maHD) {
      List<String> dsMon = new ArrayList<>();
      Connection con = ConnectSQL.getConnection();
      PreparedStatement stmt = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT maMon FROM ChiTietHoaDon WHERE maHD = ?";
          stmt = con.prepareStatement(sql);
          stmt.setString(1, maHD);
          rs = stmt.executeQuery();

          while (rs.next()) {
              dsMon.add(rs.getString("maMon"));
          }

      } catch (SQLException e) {
          e.printStackTrace();
          System.err.println("❌ Lỗi khi lấy danh sách món theo hóa đơn: " + maHD);
      } finally {
          try {
              if (rs != null) rs.close();
              if (stmt != null) stmt.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }

      return dsMon;
  }
}
//package dao;
//
//import connectSQL.ConnectSQL;
//import entity.ChiTietHoaDon;
//import entity.HoaDon;
//import entity.MonAn;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChiTietHoaDon_DAO {
//
//    public List<ChiTietHoaDon> getChiTietTheoHoaDon(String maHD) {
//        List<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
//        Connection con = ConnectSQL.getConnection();
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            String sql = """
//                SELECT cthd.maHD, cthd.maMon, ma.tenMon, cthd.soLuong, cthd.donGia, cthd.ghiChu
//                FROM ChiTietHoaDon cthd
//                JOIN MonAn ma ON cthd.maMon = ma.maMon
//                WHERE cthd.maHD = ?
//            """;
//
//            stmt = con.prepareStatement(sql);
//            stmt.setString(1, maHD);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                HoaDon hd = new HoaDon();
//                hd.setMaHD(rs.getString("maHD"));
//
//                MonAn mon = new MonAn();
//                mon.setMaMon(rs.getString("maMon"));
//                mon.setTenMon(rs.getString("tenMon"));
//
//                int soLuong = rs.getInt("soLuong");
//                double donGia = rs.getDouble("donGia");
//                String ghiChu = rs.getString("ghiChu");
//
//                ChiTietHoaDon cthd = new ChiTietHoaDon(hd, mon, soLuong, donGia, ghiChu);
//                dsChiTiet.add(cthd);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("❌ Lỗi khi lấy chi tiết hóa đơn với mã: " + maHD);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return dsChiTiet;
//    }
//    
//    
//
//}