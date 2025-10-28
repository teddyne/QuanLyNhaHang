package entity;

public class MonDat {
    public MonAn mon;
    public int soLuong;
    public double donGia;
    public String ghiChu;

    public MonDat(MonAn mon, int soLuong) {
        this(mon, soLuong, mon != null ? mon.getDonGia() : 0, "");
    }

    public MonDat(MonAn mon, int soLuong, String ghiChu) {
        this(mon, soLuong, mon != null ? mon.getDonGia() : 0, ghiChu);
    }

    public MonDat(MonAn mon, int soLuong, double donGia, String ghiChu) {
        this.mon = mon;
        this.soLuong = soLuong > 0 ? soLuong : 1;
        this.donGia = donGia;
        this.ghiChu = ghiChu != null ? ghiChu : "";
    }

    public MonAn getMon() { return mon; }
    public void setMon(MonAn mon) { this.mon = mon; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong > 0 ? soLuong : 1; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu != null ? ghiChu : ""; }

    public double getThanhTien() { return soLuong * donGia; }
}
