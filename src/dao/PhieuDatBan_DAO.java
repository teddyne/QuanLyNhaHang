package dao;

import connectSQL.ConnectSQL;
import entity.PhieuDatBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatBan_DAO {
    private Connection conn;

    public PhieuDatBan_DAO(Connection conn) {
        this.conn = conn;
    }

    public List<PhieuDatBan> getDatBanByBan(String maBan) throws SQLException {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(rs.getString("maPhieu"));
                    d.setMaBan(rs.getString("maBan"));
                    d.setTenKhach(rs.getString("tenKhach"));
                    d.setSoDienThoai(rs.getString("soDienThoai"));
                    d.setSoNguoi(rs.getInt("soNguoi"));
                    d.setNgayDen(rs.getDate("ngayDen"));
                    d.setGioDen(rs.getTime("gioDen"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setTienCoc(rs.getDouble("tienCoc"));
                    d.setGhiChuCoc(rs.getString("ghiChuCoc"));
                    d.setTrangThai(rs.getString("trangThai"));
                    list.add(d);
                }
            }
        }
        return list;
    }

    public List<PhieuDatBan> getDatBanByBanAndNgay(String maBan, Date ngay) throws SQLException {
    	List<PhieuDatBan> list = new ArrayList<>();
    	String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ? AND ngayDen = ? AND trangThai IN (N'Đặt', N'Phục vụ')";
    	try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    		stmt.setString(1, maBan);
    		stmt.setDate(2, ngay);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			PhieuDatBan p = new PhieuDatBan();
    			p.setMaPhieu(rs.getString("maPhieu"));
    			p.setMaBan(rs.getString("maBan"));
    			p.setTenKhach(rs.getString("tenKhach"));
    			p.setSoDienThoai(rs.getString("soDienThoai"));
    			p.setSoNguoi(rs.getInt("soNguoi"));
    			p.setNgayDen(rs.getDate("ngayDen"));
            	p.setGioDen(rs.getTime("gioDen"));
            	p.setGhiChu(rs.getString("ghiChu"));
            	p.setTienCoc(rs.getDouble("tienCoc"));
            	p.setGhiChuCoc(rs.getString("ghiChuCoc"));
            	p.setTrangThai(rs.getString("trangThai"));
            	list.add(p);
    		}
    	}
    	return list;
    }

    public String layTenKhachHang(String maPhieu) throws SQLException {
        String tenKhach = null;
        String sql = "SELECT tenKhach FROM PhieuDatBan WHERE maPhieu = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenKhach = rs.getString("tenKhach");
                }
            }
        }
        return tenKhach;
    }
    public PhieuDatBan getByMa(String maPhieu) throws SQLException {
        String sql = "SELECT * FROM PhieuDatBan WHERE maPhieu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhieu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(rs.getString("maPhieu"));
                    d.setMaBan(rs.getString("maBan"));
                    d.setTenKhach(rs.getString("tenKhach"));
                    d.setSoDienThoai(rs.getString("soDienThoai"));
                    d.setSoNguoi(rs.getInt("soNguoi"));
                    d.setNgayDen(rs.getDate("ngayDen"));
                    d.setGioDen(rs.getTime("gioDen"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setTienCoc(rs.getDouble("tienCoc"));
                    d.setGhiChuCoc(rs.getString("ghiChuCoc"));
                    d.setTrangThai(rs.getString("trangThai"));
                    return d;
                }
            }
        }
        return null;
    }
    public void add(PhieuDatBan phieu) throws SQLException {
        String sql = "INSERT INTO PhieuDatBan (maPhieu, maBan, tenKhach, soDienThoai, soNguoi, ngayDen, gioDen, ghiChu, tienCoc, ghiChuCoc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phieu.getMaPhieu());
            stmt.setString(2, phieu.getMaBan());
            stmt.setString(3, phieu.getTenKhach());
            stmt.setString(4, phieu.getSoDienThoai());
            stmt.setInt(5, phieu.getSoNguoi());
            stmt.setDate(6, phieu.getNgayDen());
            stmt.setTime(7, phieu.getGioDen());
            stmt.setString(8, phieu.getGhiChu());
            stmt.setDouble(9, phieu.getTienCoc());
            stmt.setString(10, phieu.getGhiChuCoc());
            stmt.setString(11, phieu.getTrangThai().trim()); // Trim trước khi lưu
            stmt.executeUpdate();
        }
    }

    public boolean update(PhieuDatBan phieu) throws SQLException {
        String sql = "UPDATE phieudatban SET MaBan = ?, NgayDen = ?, GioDen = ?, " +
                    "TenKhach = ?, SoDienThoai = ?, SoNguoi = ?, GhiChu = ?, " +
                    "TienCoc = ?, GhiChuCoc = ?, TrangThai = ? " +
                    "WHERE MaPhieu = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phieu.getMaBan());
            ps.setDate(2, phieu.getNgayDen());
            ps.setTime(3, phieu.getGioDen());
            ps.setString(4, phieu.getTenKhach());
            ps.setString(5, phieu.getSoDienThoai());
            ps.setInt(6, phieu.getSoNguoi());
            ps.setString(7, phieu.getGhiChu());
            ps.setDouble(8, phieu.getTienCoc());
            ps.setString(9, phieu.getGhiChuCoc());
            ps.setString(10, phieu.getTrangThai());
            ps.setString(11, phieu.getMaPhieu());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean checkTrung(String maBan, java.sql.Date ngay, java.sql.Time gio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuDatBan " +
                     "WHERE maBan = ? AND ngayDen = ? AND gioDen = CAST(? AS TIME) " +
                     "AND trangThai IN (N'Đặt', N'Đang phục vụ')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            ps.setDate(2, ngay);
            ps.setString(3, gio.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public String generateMaPhieu() throws SQLException {
        String sql = "SELECT 'MP' + RIGHT('000' + CAST(ISNULL(MAX(CAST(SUBSTRING(maPhieu, 3, 3) AS INT)), 0) + 1 AS VARCHAR(3)), 3) AS NewmaPhieu FROM PhieuDatBan";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("NewmaPhieu");
            }
        }
        return "MP001"; // Giá trị mặc định nếu bảng rỗng
    }

    public void delete(String maPhieu) throws SQLException {
        String sql = "DELETE FROM PhieuDatBan WHERE maPhieu = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieu);
            stmt.executeUpdate();
        }
    }

	public PhieuDatBan getPhucVuHienTai(String maBan) throws SQLException {
	        String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ? AND trangThai = 'Phục vu' AND CAST(ngayDen AS date) = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, maBan);
	            stmt.setDate(2, new Date(System.currentTimeMillis()));
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                PhieuDatBan phieu = new PhieuDatBan();
	                phieu.setMaPhieu(rs.getString("maPhieu"));
	                phieu.setMaBan(rs.getString("maBan"));
	                phieu.setTenKhach(rs.getString("tenKhach"));
	                phieu.setSoDienThoai(rs.getString("soDienThoai"));
	                phieu.setSoNguoi(rs.getInt("soNguoi"));
	                phieu.setNgayDen(rs.getDate("ngayDen"));
	                phieu.setGioDen(rs.getTime("gioDen"));
	                phieu.setGhiChu(rs.getString("ghiChu"));
	                phieu.setTienCoc(rs.getDouble("tienCoc"));
	                phieu.setGhiChuCoc(rs.getString("ghiChuCoc"));
	                phieu.setTrangThai(rs.getString("trangThai"));
	                return phieu;
	            }
	        }
	        return null;
	}

	public boolean checkCachGio(String maBan, Date ngay, Time gio, int gioCach) throws SQLException {
        String sql = "SELECT gioDen FROM PhieuDatBan WHERE maBan = ? AND ngayDen = ? AND trangThai NOT IN ('Hủy')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            ps.setDate(2, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                long gioDatMillis = gio.getTime();
                while (rs.next()) {
                    long gioKhacMillis = rs.getTime("gioDen").getTime();
                    long diff = Math.abs(gioDatMillis - gioKhacMillis);
                    if (diff < gioCach * 3600000L) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
	
	public PhieuDatBan getByMaBanAndTrangThai(String maBan, String trangThai) {
	    String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ? AND trangThai = ?";
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, maBan);
	        ps.setString(2, trangThai);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new PhieuDatBan(
	                rs.getString("maPhieu"),
	                rs.getString("maBan"),
	                rs.getString("tenKhach"),
	                rs.getString("soDienThoai"),
	                rs.getInt("soNguoi"),
	                rs.getDate("ngayDen"),
	                rs.getTime("gioDen"),
	                rs.getString("ghiChu"),
	                rs.getDouble("tienCoc"),
	                rs.getString("ghiChuCoc"),
	                rs.getString("trangThai")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
	    if (maPhieu == null || maPhieu.trim().isEmpty() || trangThaiMoi == null) {
	        return false;
	    }

	    String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPhieu = ?";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, trangThaiMoi.trim());
	        ps.setString(2, maPhieu.trim());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Lỗi cập nhật trạng thái phiếu: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	public String taoPhieuMoi(String maBan) {
		// TODO Auto-generated method stub
		return null;
	}
}