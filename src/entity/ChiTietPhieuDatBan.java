package entity;

public class ChiTietPhieuDatBan {

    private String maPhieu;  
    private String maMon;     
    private String tenMon;    
    private int soLuong;      
    private double donGia;   
    private String ghiChu;

    public ChiTietPhieuDatBan() {
    }

    public ChiTietPhieuDatBan(String maPhieu, String maMon, int soLuong, double donGia, String ghiChu) {
        this.maPhieu = maPhieu;
        this.maMon = maMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }


    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
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
}
