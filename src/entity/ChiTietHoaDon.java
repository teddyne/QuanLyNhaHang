package entity;

public class ChiTietHoaDon {
    private String maHD;
    private String maMon;
    private int soLuong;
    private double donGia;
    private String ghiChu;
    private String tenMon; // Thêm để lưu tên món

    public ChiTietHoaDon(String maHD, String maMon, int soLuong, double donGia, String ghiChu) {
        this.maHD = maHD;
        this.maMon = maMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public String getMaHD() {
        return maHD;
    }

    public String getMaMon() {
        return maMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

	public void setMaMon(String maMon) {
		this.maMon = maMon;
	}

}