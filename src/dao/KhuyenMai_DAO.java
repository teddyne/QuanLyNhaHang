package dao;

import connectSQL.ConnectSQL;
import entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_DAO {
    private final Connection conn;

    public KhuyenMai_DAO() {
        conn = ConnectSQL.getInstance().getConnection();
    }
    

    public List<KhuyenMai> layDanhSachKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";
        Date today = new Date(System.currentTimeMillis());

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setLoaiKM(rs.getString("loaiKM"));
                km.setGiaTri(rs.getDouble("giaTri"));
                km.setNgayBatDau(rs.getDate("ngayBatDau"));
                km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                km.setTrangThai(rs.getString("trangThai"));
                km.setDoiTuongApDung(rs.getString("doiTuongApDung"));
                km.setDonHangTu(rs.getDouble("donHangTu"));
                km.setMon1(rs.getString("mon1"));
                km.setMon2(rs.getString("mon2"));
                km.setMonTang(rs.getString("monTang"));
                km.setGhiChu(rs.getString("ghiChu"));

                // --- Tính lại trạng thái ---
                String trangThaiMoi;
                if (today.before(km.getNgayBatDau())) {
                    trangThaiMoi = "Sắp áp dụng";
                } else if (!today.after(km.getNgayKetThuc())) {
                    trangThaiMoi = "Đang áp dụng";
                } else {
                    trangThaiMoi = "Hết hạn";
                }

                // Nếu trạng thái trong DB khác thì update lại
                if (!trangThaiMoi.equals(km.getTrangThai())) {
                    capNhatTrangThai(km.getMaKM(), trangThaiMoi);
                    km.setTrangThai(trangThaiMoi);
                }

                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //Cập nhật lại db
    private void capNhatTrangThai(String maKM, String trangThaiMoi) {
        String sql = "UPDATE KhuyenMai SET trangThai=? WHERE maKM=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThaiMoi);
            ps.setString(2, maKM);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
 // Hàm lấy một khuyến mãi theo mã
    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM=?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("loaiKM"),
                        rs.getDouble("giaTri"),
                        rs.getDate("ngayBatDau"),
                        rs.getDate("ngayKetThuc"),
                        rs.getString("trangThai"),
                        rs.getString("doiTuongApDung"),
                        rs.getDouble("donHangTu"),
                        rs.getString("mon1"),
                        rs.getString("mon2"),
                        rs.getString("monTang"),
                        rs.getString("ghiChu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // nếu không tìm thấy
    }

    
    //Tra cứu KM theo nhiều tiêu chí
    public List<KhuyenMai> traCuuKhuyenMai(String maKM, String mon, String loai, String trangThai, Date tuNgay, Date denNgay) {
        List<KhuyenMai> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM KhuyenMai WHERE 1=1 ");

        // Tạo điều kiện linh hoạt
        if (maKM != null && !maKM.isEmpty()) sql.append(" AND maKM LIKE ? ");
        if (mon != null && !mon.isEmpty()) sql.append(" AND (mon1 LIKE ? OR mon2 LIKE ? OR monTang LIKE ?) ");
        if (loai != null && !loai.isEmpty()) sql.append(" AND loaiKM = ? ");
        if (trangThai != null && !trangThai.isEmpty()) sql.append(" AND trangThai = ? ");
        if (tuNgay != null) sql.append(" AND ngayBatDau >= ? ");
        if (denNgay != null) sql.append(" AND ngayKetThuc <= ? ");

        try (
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (maKM != null && !maKM.isEmpty()) ps.setString(index++, "%" + maKM + "%");
            if (mon != null && !mon.isEmpty()) {
                ps.setString(index++, "%" + mon + "%");
                ps.setString(index++, "%" + mon + "%");
                ps.setString(index++, "%" + mon + "%");
            }
            if (loai != null && !loai.isEmpty()) ps.setString(index++, loai);
            if (trangThai != null && !trangThai.isEmpty()) ps.setString(index++, trangThai);
            if (tuNgay != null) ps.setDate(index++, tuNgay);
            if (denNgay != null) ps.setDate(index++, denNgay);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("loaiKM"),
                        rs.getDouble("giaTri"),
                        rs.getDate("ngayBatDau"),
                        rs.getDate("ngayKetThuc"),
                        rs.getString("trangThai"),
                        rs.getString("doiTuongApDung"),
                        rs.getDouble("donHangTu"),
                        rs.getString("mon1"),
                        rs.getString("mon2"),
                        rs.getString("monTang"),
                        rs.getString("ghiChu")
                );
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai(maKM, tenKM, loaiKM, giaTri, ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, donHangTu, mon1, mon2, monTang, ghiChu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setString(3, km.getLoaiKM());
            ps.setDouble(4, km.getGiaTri());
            ps.setDate(5, km.getNgayBatDau());
            ps.setDate(6, km.getNgayKetThuc());
            ps.setString(7, km.getTrangThai());
            ps.setString(8, km.getDoiTuongApDung());
            ps.setDouble(9, km.getDonHangTu());

            // Xử lý khóa ngoại
            if (km.getMon1() == null || km.getMon1().trim().isEmpty())
                ps.setNull(10, Types.NVARCHAR);
            else
                ps.setString(10, km.getMon1());

            if (km.getMon2() == null || km.getMon2().trim().isEmpty())
                ps.setNull(11, Types.NVARCHAR);
            else
                ps.setString(11, km.getMon2());

            if (km.getMonTang() == null || km.getMonTang().trim().isEmpty())
                ps.setNull(12, Types.NVARCHAR);
            else
                ps.setString(12, km.getMonTang());

            ps.setString(13, km.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaKhuyenMai(KhuyenMai km){
        String sql = "UPDATE KhuyenMai SET tenKM=?, loaiKM=?, giaTri=?, ngayBatDau=?, ngayKetThuc=?, " +
                     "trangThai=?, doiTuongApDung=?, donHangTu=?, mon1=?, mon2=?, monTang=?, ghiChu=? " +
                     "WHERE maKM=?";
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, km.getTenKM());
            stmt.setString(2, km.getLoaiKM());
            stmt.setDouble(3, km.getGiaTri());
            stmt.setDate(4, km.getNgayBatDau());
            stmt.setDate(5, km.getNgayKetThuc());
            stmt.setString(6, km.getTrangThai());
            stmt.setString(7, km.getDoiTuongApDung());
            stmt.setDouble(8, km.getDonHangTu());
            stmt.setString(9, km.getMon1());
            stmt.setString(10, km.getMon2());
            stmt.setString(11, km.getMonTang());
            stmt.setString(12, km.getGhiChu());
            stmt.setString(13, km.getMaKM());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public String taoMaKhuyenMaiMoi() {
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String last = rs.getString("maKM");
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("KM%04d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}