package entity;

public class MonAn {
    private String maMon;
    private String tenMon;
    private String anhMon;
    private LoaiMon loaiMon;
    private double donGia;
    private boolean trangThai;
    private String moTa;

    public MonAn(String maMon, String tenMon, String anhMon, LoaiMon loaiMon, double donGia, boolean trangThai, String moTa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.anhMon = anhMon;
        this.loaiMon = loaiMon;
        this.donGia = donGia;
        this.trangThai = trangThai;
        this.moTa = moTa;
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

    public String getAnhMon() {
        return anhMon;
    }

    public void setAnhMon(String anhMon) {
        this.anhMon = anhMon;
    }

    public LoaiMon getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(LoaiMon loaiMon) {
        this.loaiMon = loaiMon;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}