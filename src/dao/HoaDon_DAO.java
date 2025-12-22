package dao;

import connectSQL.ConnectSQL;
//import entity.HoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class HoaDon_DAO {
    private Connection conn;
    private Object tenKhach;
    private Object maNhanVien;
    
    public static HoaDon_DAO getInstance() {
        return new HoaDon_DAO();
    }


    public static String taoHoaDonMoi(String maPhieu, String maNhanVien, String maKH, String maKM, double phuThu, String ghiChu) throws SQLException {
        String sql = "INSERT INTO HoaDon (maHD, maPhieu, maKH, maKM, maNhanVien, phuThu, ghiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String maHD = taoMaHoaDon(); // tự sinh mã HD
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maPhieu);
            ps.setString(3, maKH);
            ps.setString(4, maKM);
            ps.setString(5, maNhanVien);
            ps.setDouble(6, phuThu);
            ps.setString(7, ghiChu);
            ps.executeUpdate();
        }
        return maHD;
    }

 // Lấy maHD từ maPhieu
    public String layMaHoaDonTheoPhieu(String maPhieu) throws SQLException {
        String sql = "SELECT maHD FROM HoaDon WHERE maPhieu = ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maHD");
            }
        }
        return null;
    }

    // Tự sinh mã hóa đơn mới
    private static String taoMaHoaDon() {
        String sql = """
            SELECT 'HD' + RIGHT(
        		    '0000' + CAST(
        		        ISNULL(MAX(CAST(SUBSTRING(maHD, 3, LEN(maHD)) AS INT)), 0) + 1 AS VARCHAR
        			), 4
        		)
            FROM HoaDon WHERE maHD LIKE 'HD%'
            AND ISNUMERIC(SUBSTRING(maHD, 3, LEN(maHD))) = 1
        	""";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD0001";
    }

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
        String sql = """
            SELECT kh.tenKH, kh.sdt, b.maBan, nv.hoTen, hd.ngayLap, km.tenKM,
                   ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongTien
            FROM HoaDon hd
            LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
            LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            LEFT JOIN PhieuDatBan pdb ON hd.maPhieu = pdb.maPhieu
            LEFT JOIN Ban b ON pdb.maBan = b.maBan
            LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM
            LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD
            WHERE hd.maHD = ?
            GROUP BY kh.tenKH, kh.sdt, b.maBan, nv.hoTen, hd.ngayLap, km.tenKM
        """;

        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    thongTin = new Object[]{
                        rs.getString("tenKH"),    // có thể NULL
                        rs.getString("sdt"),      // có thể NULL
                        rs.getString("maBan"),    // có thể NULL
                        rs.getString("hoTen"),    // có thể NULL
                        rs.getTimestamp("ngayLap"),
                        rs.getString("tenKM"),    // có thể NULL
                        rs.getDouble("tongTien")  // 0 nếu không có chi tiết
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thongTin;
    }

    public List<Object[]> layChiTietHoaDon(String maHD) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT ma.maMon, ma.tenMon, ct.soLuong, ct.donGia, (ct.soLuong * ct.donGia) AS thanhTien
            FROM ChiTietHoaDon ct
            JOIN MonAn ma ON ct.maMon = ma.maMon
            WHERE ct.maHD = ?
        """;
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getDouble("thanhTien")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> timKiemHoaDon(String maHD, String tenKH, String sdt, String tenNV, String maBan, Date ngayLap) {
        List<Object[]> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT hd.maHD, b.maBan, hd.ngayLap, kh.tenKH, kh.sdt, nv.hoTen, km.tenKM,
                   ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongTien
            FROM HoaDon hd
            LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH
            LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM
            LEFT JOIN PhieuDatBan p ON hd.maPhieu = p.maPhieu
            LEFT JOIN Ban b ON p.maBan = b.maBan
            LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD
            WHERE 1=1
        """);
        if (maHD != null && !maHD.isEmpty()) sql.append(" AND hd.maHD LIKE ? ");
        if (tenKH != null && !tenKH.isEmpty()) sql.append(" AND kh.tenKH LIKE ? ");
        if (sdt != null && !sdt.isEmpty()) sql.append(" AND kh.sdt LIKE ? ");
        if (tenNV != null && !tenNV.isEmpty()) sql.append(" AND nv.hoTen LIKE ? ");
        if (maBan != null && !maBan.isEmpty()) sql.append(" AND b.maBan LIKE ? ");
        if (ngayLap != null) sql.append(" AND CONVERT(date, hd.ngayLap) = ? ");
        sql.append(" GROUP BY hd.maHD, b.maBan, hd.ngayLap, kh.tenKH, kh.sdt, nv.hoTen, km.tenKM");

        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (maHD != null && !maHD.isEmpty()) ps.setString(index++, "%" + maHD + "%");
            if (tenKH != null && !tenKH.isEmpty()) ps.setString(index++, "%" + tenKH + "%");
            if (sdt != null && !sdt.isEmpty()) ps.setString(index++, "%" + sdt + "%");
            if (tenNV != null && !tenNV.isEmpty()) ps.setString(index++, "%" + tenNV + "%");
            if (maBan != null && !maBan.isEmpty()) ps.setString(index++, "%" + maBan + "%");
            if (ngayLap != null) ps.setDate(index++, ngayLap);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public String getMaHoaDonTheoBan(String maBan) {
        String maHD = null;
        String sql = """
            SELECT TOP 1 hd.maHD
            FROM HoaDon hd
            JOIN PhieuDatBan p ON hd.maPhieu = p.maPhieu
            WHERE p.maBan = ?
            ORDER BY hd.ngayLap DESC
        """;
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maHD = rs.getString("maHD");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHD;
    }
    
    public Object[] layThongTinKhachTuPhieuDatBan(String maHD) {
        String sql = "SELECT pdb.tenKhach, pdb.soDienThoai " +
                     "FROM HoaDon hd " +
                     "JOIN PhieuDatBan pdb ON hd.maPhieu = pdb.maPhieu " +
                     "WHERE hd.maHD = ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{ rs.getString("tenKhach"), rs.getString("soDienThoai") };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean thanhToanHoaDon(String maHD,
            double tienKhachDua,      // chỉ để kiểm tra ở UI
            double tienSauGiam,       // chỉ để kiểm tra ở UI
            String maKM, String maKH, double phuThu, String ghiChu) {
    	
        String sqlUpdateHD =
                "UPDATE HoaDon SET " +
                "trangThai = N'Đã thanh toán', " +
                "maKM = ?, maKH = ?, phuThu = ?, ghiChu = ?, ngayLap = GETDATE() " +
                "WHERE maHD = ?";
        String sqlGetBan = "SELECT p.maBan FROM HoaDon hd " +
                           "JOIN PhieuDatBan p ON hd.maPhieu = p.maPhieu " +
                           "WHERE hd.maHD = ?";
        String sqlUpdateBan = "UPDATE Ban SET trangThai = N'Trống' WHERE maBan = ?";
        Connection conn = null;
        PreparedStatement psHD = null, psGet = null, psBan = null;
        ResultSet rs = null;

        try {
            conn = ConnectSQL.getInstance().getConnection();
            conn.setAutoCommit(false);   // transaction

            // ---- 1. Cập nhật HoaDon ----
            psHD = conn.prepareStatement(sqlUpdateHD);
            psHD.setString(1, maKM);
            psHD.setString(2, maKH);
            psHD.setDouble(3, phuThu);
            // ghi chú thông minh
            String ghiChuFinal = (maKM != null)
                    ? "Áp dụng khuyến mãi: " + maKM
                    : (ghiChu != null && !ghiChu.trim().isEmpty() ? ghiChu : null);
            psHD.setString(4, ghiChuFinal);
            psHD.setString(5, maHD);

            int rows = psHD.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                JOptionPane.showMessageDialog(null, "Không tìm thấy hoá đơn: " + maHD, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // ---- 2. Lấy maBan ----
            psGet = conn.prepareStatement(sqlGetBan);
            psGet.setString(1, maHD);
            rs = psGet.executeQuery();
            if (rs.next()) {
                String maBan = rs.getString("maBan");
                // ---- 3. Cập nhật trạng thái bàn ----
                psBan = conn.prepareStatement(sqlUpdateBan);
                psBan.setString(1, maBan);
                psBan.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Thanh toán thành công hoá đơn " + maHD, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thanh toán: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psHD != null) psHD.close();
                if (psGet != null) psGet.close();
                if (psBan != null) psBan.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public String layMaBanTheoHoaDon(String maHD) {
        String sql = "SELECT b.maBan FROM HoaDon hd " +
                     "JOIN PhieuDatBan p ON hd.maPhieu = p.maPhieu " +
                     "JOIN Ban b ON p.maBan = b.maBan " +
                     "WHERE hd.maHD = ?";
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String layMaKhachTuTen(String tenKH, String sdt) {
        String sql = "SELECT maKH FROM KhachHang WHERE tenKH = ? AND sdt = ?";
        try (Connection con = ConnectSQL.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenKH);
            ps.setString(2, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Object[]> getHoaDonTheoThoiGian(java.sql.Date ngayBatDau, java.sql.Date ngayKetThuc) {
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
                     "WHERE CONVERT(date, hd.ngayLap) BETWEEN ? AND ? " +
                     "GROUP BY hd.maHD, b.maBan, hd.ngayLap, kh.tenKH, kh.sdt, nv.hoTen, km.tenKM " +
                     "ORDER BY hd.ngayLap ASC";
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, ngayBatDau);
            stmt.setDate(2, ngayKetThuc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("maHD"),
                        rs.getString("maBan"),
                        rs.getTimestamp("ngayLap"),
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getString("hoTen"),
                        rs.getString("tenKM"),
                        rs.getDouble("tongTien")
                    };
                    ds.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<Object[]> getTopMonAnBanChay(java.sql.Date ngayBatDau, java.sql.Date ngayKetThuc) {
        List<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT TOP 10
            ma.tenMon,
            SUM(ct.soLuong) AS tongSoLuong
            FROM ChiTietHoaDon ct
            JOIN MonAn ma ON ct.maMon = ma.maMon
            JOIN HoaDon hd ON ct.maHD = hd.maHD
            WHERE
            CONVERT(date, hd.ngayLap) BETWEEN ? AND ?
            GROUP BY
            ma.tenMon
            ORDER BY
            tongSoLuong DESC
        """;
        try (Connection con = ConnectSQL.getConnection();
        	PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, ngayBatDau);
            stmt.setDate(2, ngayKetThuc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("tenMon"),
                        rs.getInt("tongSoLuong")
                    };
                    ds.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public boolean taoHoaDonTuPhieuDatBan(String maPDB, String tenKH, String sdt, 
    		double tongTien, double giamGia, double phuThu, String maKM, double tienKhachDua) {
    	
        String maHD = taoMaHoaDon();
        try {
            conn.setAutoCommit(false);
            //Lấy mã khách
            String maKH = layHoacTaoKhachHang(tenKH, sdt);
            //Insert hóa đơn
            String sqlHD = """
                INSERT INTO HoaDon(maHD, ngayLap, tongTien, giamGia, phuThu, tienKhachDua, maKM, maKH)
                VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?)
                """;
            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setString(1, maHD);
                ps.setDouble(2, tongTien);
                ps.setDouble(3, giamGia);
                ps.setDouble(4, phuThu);
                ps.setDouble(5, tienKhachDua);
                ps.setString(6, maKM);
                ps.setString(7, maKH);
                ps.executeUpdate();
            }
            //Copy chi tiết
            String sqlCT = """
                INSERT INTO ChiTietHoaDon(maHD, maMon, soLuong, donGia)
                SELECT ?, maMon, soLuong, donGia
                FROM ChiTietPhieuDatBan
                WHERE maPDB = ?
            	""";
            try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
                ps.setString(1, maHD);
                ps.setString(2, maPDB);
                ps.executeUpdate();
            }
            //Cập nhật trạng thái phiếu
            String sqlUpdate = "UPDATE PhieuDatBan SET trangThai = N'Hoàn thành' WHERE maPDB = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setString(1, maPDB);
                ps.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    private String layHoacTaoKhachHang(String tenKH, String sdt) throws SQLException {
        //Nếu khách đã tồn tại → trả về luôn
        String sqlCheck = "SELECT maKH FROM KhachHang WHERE tenKH = ? AND sdt = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setString(1, tenKH);
            ps.setString(2, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maKH");
            }
        }
        
        //Sinh mã KH mới
        String sqlGenMa = """
            SELECT 'KH' + RIGHT('0000' + CAST(
        			  ISNULL(MAX(CAST(SUBSTRING(maKH, 3, LEN(maKH)) AS INT)), 0) + 1 AS VARCHAR
        		   ), 4
        		)
            FROM KhachHang WHERE maKH LIKE 'KH%'
            AND ISNUMERIC(SUBSTRING(maKH, 3, LEN(maKH))) = 1
        	""";
        String maKH = null;
        try (PreparedStatement ps = conn.prepareStatement(sqlGenMa);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                maKH = rs.getString(1);
            }
        }
        if (maKH == null) maKH = "KH0001";
        //Insert khách hàng mới
        String sqlInsert = "INSERT INTO KhachHang(maKH, tenKH, sdt) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setString(1, maKH);
            ps.setString(2, tenKH);
            ps.setString(3, sdt);
            ps.executeUpdate();
        } return maKH;
    }

    public String taoHoaDonTuPhieuDatBan(String maPhieu) throws SQLException {
    	String maHD = taoMaHoaDon();
        String sql = """
            INSERT INTO HoaDon (maHD, maPhieu, ngayLap, trangThai)
            VALUES (?, ?, GETDATE(), N'Chưa thanh toán')
        	""";
        try (Connection con = ConnectSQL.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maPhieu);
            ps.executeUpdate();
        } return maHD;
    }

    public String layMaHoaDonTheoPhieuDatBan(String maPDB) {
        String sql = """
            SELECT maHD FROM HoaDon
            WHERE maPhieu = ?
        	""";
        try (Connection con = ConnectSQL.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maHD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }

    public void themMonTangVaoHoaDon(String maHD, String maMon, int soLuong) {
        String sql = """
            INSERT INTO ChiTietHoaDon(maHD, maMon, soLuong, donGia, ghiChu)
            VALUES (?, ?, ?, 0, N'Tặng')
        	""";
        try (Connection conn = ConnectSQL.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maMon);
            ps.setInt(3, soLuong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String layMaPhieuTuHoaDon(String maHD) throws SQLException {
        String sql = "SELECT maPhieu FROM HoaDon WHERE maHD = ?";
        try (Connection con = ConnectSQL.getInstance().getConnection(); // hoặc ConnectSQL.getConnection()
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maPhieu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // ném lại để form xử lý nếu cần
        }
        return null;
    }
    
}