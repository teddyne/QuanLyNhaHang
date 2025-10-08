package dao;

import entity.MonAn;
import entity.LoaiMon;
import connectSQL.ConnectSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAn_DAO {

    // Thêm món ăn
    public boolean themMonAn(MonAn mon) {
        mon.setMaMon(taoMaMonMoi());
        String sql = "INSERT INTO MonAn (maMon, tenMon, anhMon, maLoai, donGia, trangThai, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMon());
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getAnhMon());
            ps.setString(4, mon.getLoaiMon().getMaLoai());
            ps.setDouble(5, mon.getDonGia());
            ps.setBoolean(6, mon.isTrangThai());
            ps.setString(7, mon.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật món ăn
    public boolean capNhatMonAn(MonAn mon) {
        String sql = "UPDATE MonAn SET tenMon=?, anhMon=?, maLoai=?, donGia=?, trangThai=?, moTa=? WHERE maMon=?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setString(2, mon.getAnhMon());
            ps.setString(3, mon.getLoaiMon().getMaLoai());
            ps.setDouble(4, mon.getDonGia());
            ps.setBoolean(5, mon.isTrangThai());
            ps.setString(6, mon.getMoTa());
            ps.setString(7, mon.getMaMon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ẩn món ăn
    public boolean anMonAn(String maMon) {
        String sql = "UPDATE MonAn SET trangThai = 0 WHERE maMon = ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả món ăn
    public List<MonAn> getAllMonAn() {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT m.*, l.tenLoai FROM MonAn m JOIN LoaiMon l ON m.maLoai = l.maLoai";
        try (Connection con = ConnectSQL.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiMon loai = new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai"));
                MonAn mon = new MonAn(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("anhMon"),
                        loai,
                        rs.getDouble("donGia"),
                        rs.getBoolean("trangThai"),
                        rs.getString("moTa")
                );
                list.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm món ăn theo tên (hoặc một phần tên)
    public List<MonAn> timMonAnTheoTen(String ten) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT m.*, l.tenLoai FROM MonAn m JOIN LoaiMon l ON m.maLoai = l.maLoai WHERE tenMon LIKE ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoaiMon loai = new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai"));
                    MonAn mon = new MonAn(
                            rs.getString("maMon"),
                            rs.getString("tenMon"),
                            rs.getString("anhMon"),
                            loai,
                            rs.getDouble("donGia"),
                            rs.getBoolean("trangThai"),
                            rs.getString("moTa")
                    );
                    list.add(mon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm món theo loại món
    public List<MonAn> timMonTheoLoai(String tenLoai) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT * FROM MonAn JOIN LoaiMon ON MonAn.maLoai = LoaiMon.maLoai WHERE LoaiMon.tenLoai LIKE ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenLoai + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoaiMon loai = new LoaiMon(rs.getString("maLoai"), rs.getString("tenLoai"));
                    MonAn mon = new MonAn(
                            rs.getString("maMon"),
                            rs.getString("tenMon"),
                            rs.getString("anhMon"),
                            loai,
                            rs.getDouble("donGia"),
                            rs.getBoolean("trangThai"),
                            rs.getString("moTa")
                    );
                    list.add(mon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tạo mã món mới tự tăng
    private String taoMaMonMoi() {
        List<MonAn> list = getAllMonAn();
        int max = 0;
        for (MonAn m : list) {
            try {
                int num = Integer.parseInt(m.getMaMon().replaceAll("\\D", ""));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }
        return String.format("MON%04d", max + 1);
    }
}
