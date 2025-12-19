package dao;

import entity.KhachHang;
import java.sql.*;
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
    private final Connection conn;

    public KhachHang_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<KhachHang> getAllKhachHang() throws SQLException {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.ngaySinh, kh.email, lkh.tenLoaiKH " +
                     "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                     "WHERE kh.trangThai = N'Hoạt động'"; 
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang kh = mapRowToKhachHang(rs);
                dsKhachHang.add(kh);
            }
        }
        return dsKhachHang;
    }

    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, ngaySinh, email, maLoaiKH, trangThai) VALUES (?, ?, ?, ?, ?, ?, N'Hoạt động')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String maTuDong = generateMaKH();

            String maLoaiKH = kh.getloaiKH().equals("Thành viên") ? "LKH02" : "LKH01"; // giữ nguyên logic bạn

            ps.setString(1, maTuDong);
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            if (kh.getNgaySinh() != null) {
                ps.setDate(4, java.sql.Date.valueOf(kh.getNgaySinh()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, kh.getEmail());
            ps.setString(6, maLoaiKH);
            // ps.setString(7, "Hoạt động"); // đã set mặc định trong SQL

            int rows = ps.executeUpdate();
            if (rows > 0) {
                kh.setMaKH(maTuDong); // cập nhật mã mới cho object
            }
            return rows > 0;
        }
    }
    
    public boolean suaKhachHang(KhachHang kh) throws SQLException {
      
        String sql = "UPDATE KhachHang SET tenKH = ?, sdt = ?, ngaySinh = ?, email = ?, maLoaiKH = ? WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String maLoaiKH = kh.getloaiKH().equals("Thành viên") ? "LKH02" : "LKH01";
            
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());

            if (kh.getNgaySinh() != null) {
                ps.setDate(3, java.sql.Date.valueOf(kh.getNgaySinh()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            ps.setString(4, kh.getEmail());
            ps.setString(5, maLoaiKH);
            ps.setString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        }
    }

    
    public boolean anKhachHang(String maKH) throws SQLException {
       
        String sql = "UPDATE KhachHang SET trangThai = N'Ẩn' WHERE maKH = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        }
    }
    

    public List<KhachHang> timKiemKhachHang(String keyword) throws SQLException {
        List<KhachHang> ds = new ArrayList<>();
        
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.ngaySinh, kh.email, lkh.tenLoaiKH " +
                     "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                     "WHERE (kh.maKH LIKE ? OR kh.tenKH LIKE ? OR kh.sdt LIKE ?) " +
                     "AND kh.trangThai = N'Hoạt động'"; 
        
        String searchTerm = "%" + keyword + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRowToKhachHang(rs));
                }
            }
        }
        return ds;
    }

    public String generateMaKH() throws SQLException {
        if (conn == null) {
            throw new SQLException("Kết nối cơ sở dữ liệu không tồn tại!");
        }
        String newId = "KH0001";
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maKH");
                int number = Integer.parseInt(lastId.substring(2));
                number++;
                newId = String.format("KH%04d", number);
            }
        }
        return newId;
    }

    private KhachHang mapRowToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getString("maKH");
        String tenKH = rs.getString("tenKH");
        String sdt = rs.getString("sdt");
        java.sql.Date sqlNgaySinh = rs.getDate("ngaySinh");
        LocalDate ngaySinh = (sqlNgaySinh != null) ? sqlNgaySinh.toLocalDate() : null;
        String email = rs.getString("email");
        String tenLoaiKH = rs.getString("tenLoaiKH");
        
        return new KhachHang(maKH, tenKH, sdt, ngaySinh, email, tenLoaiKH);
    }
    
    public KhachHang getKhachHangByMa(String maKH) throws SQLException {
       
        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.ngaySinh, kh.email, lkh.tenLoaiKH " +
                     "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                     "WHERE kh.maKH = ? AND kh.trangThai = N'Hoạt động'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKhachHang(rs);
                }
            }
        }
        return null; 
    }

    public KhachHang timKhachHangTheoSDT(String sdt) throws SQLException {

        String sql = "SELECT kh.maKH, kh.tenKH, kh.sdt, kh.ngaySinh, kh.email, lkh.tenLoaiKH " +
                     "FROM KhachHang kh LEFT JOIN LoaiKhachHang lkh ON kh.maLoaiKH = lkh.maLoaiKH " +
                     "WHERE kh.sdt = ? AND kh.trangThai = N'Hoạt động'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKhachHang(rs);
                }
            }
        }
        return null;
    }
}