package entity;

import java.util.Date;

public class HoaDon {
    private String maHD;
    private Date ngayLap;
    private String trangThai;
    private String maBan;
    private String maPhieu;
    private double tongTien;
    private String maKH;
    private String maNV;
    private String maKM;
    private String ghiChu;

    public HoaDon() {
    }

    public HoaDon(String maHD, Date ngayLap, String trangThai, String maBan, String maPhieu, double tongTien,
			String maKH, String maNV, String maKM, String ghiChu) {
		super();
		this.maHD = maHD;
		this.ngayLap = ngayLap;
		this.trangThai = trangThai;
		this.maBan = maBan;
		this.maPhieu = maPhieu;
		this.tongTien = tongTien;
		this.maKH = maKH;
		this.maNV = maNV;
		this.maKM = maKM;
		this.ghiChu = ghiChu;
	}

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        if (maHD == null || !maHD.matches("^HD\\d{4}$")) {
            throw new IllegalArgumentException("Mã hóa đơn không đúng định dạng (HDxxxx).");
        }
        this.maHD = maHD;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        if (ngayLap == null) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được rỗng.");
        }
        this.ngayLap = ngayLap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        String[] ttHopLe = {"Chưa thanh toán", "Đã thanh toán", "Đã cọc", "Đã hủy"};
        boolean ok = false;
        for (String tt : ttHopLe) {
            if (tt.equals(trangThai)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ.");
        }
        this.trangThai = trangThai;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bàn không được rỗng.");
        }
        this.maBan = maBan;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        if (maPhieu == null || maPhieu.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu không được rỗng.");
        }
        this.maPhieu = maPhieu;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        if (tongTien <= 0) {
            throw new IllegalArgumentException("Tổng tiền phải > 0.");
        }
        this.tongTien = tongTien;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được rỗng.");
        }
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được rỗng.");
        }
        this.maNV = maNV;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", ngayLap=" + ngayLap + ", trangThai=" + trangThai + ", maBan=" + maBan
				+ ", maPhieu=" + maPhieu + ", tongTien=" + tongTien + ", maKH=" + maKH + ", maNV=" + maNV + ", maKM="
				+ maKM + ", ghiChu=" + ghiChu + "]";
	}

    
}
