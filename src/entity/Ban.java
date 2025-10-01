package entity;

public class Ban {
    private String maBan;
    private String maKhuVuc;
    private int soChoNgoi;
    private String loaiBan;
    private String trangThai;
    private String ghiChu;
    private String tenKhuVuc; // Thêm để lưu tên khu vực từ join với KhuVuc

    // Constructors
    public Ban() {}

    public Ban(String maBan, String maKhuVuc, int soChoNgoi, String loaiBan, String trangThai, String ghiChu) {
        this.maBan = maBan;
        this.maKhuVuc = maKhuVuc;
        this.soChoNgoi = soChoNgoi;
        this.loaiBan = loaiBan;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // Getters and Setters
    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }

    public String getMaKhuVuc() { return maKhuVuc; }
    public void setMaKhuVuc(String maKhuVuc) { this.maKhuVuc = maKhuVuc; }

    public int getSoChoNgoi() { return soChoNgoi; }
    public void setSoChoNgoi(int soChoNgoi) { this.soChoNgoi = soChoNgoi; }

    public String getLoaiBan() { return loaiBan; }
    public void setLoaiBan(String loaiBan) { this.loaiBan = loaiBan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTenKhuVuc() { return tenKhuVuc; }
    public void setTenKhuVuc(String tenKhuVuc) { this.tenKhuVuc = tenKhuVuc; }
}