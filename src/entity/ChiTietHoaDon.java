package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;   // Quan hệ n-n giữa HoaDon và MonAn
    private MonAn monAn;     // Tham chiếu đến món ăn
    private int soLuong;
    private double donGia;
    private String ghiChu;

    // === Constructor ===
    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong, double donGia, String ghiChu) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }

    // === Getter & Setter ===
    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // === Thuộc tính dẫn xuất (thành tiền) ===
    public double getThanhTien() {
        return donGia * soLuong;
    }

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + hoaDon + ", monAn=" + monAn + ", soLuong=" + soLuong + ", donGia=" + donGia
				+ ", ghiChu=" + ghiChu + "]";
	}
}
