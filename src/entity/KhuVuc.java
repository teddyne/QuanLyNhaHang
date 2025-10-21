package entity;

public class KhuVuc {
    private String maKhuVuc;
    private String tenKhuVuc;
    private int soLuongBan;
    private String trangThai;

    // Constructors
    public KhuVuc() {}

    public KhuVuc(String maKhuVuc, String tenKhuVuc, int soLuongBan, String trangThai) {
        this.maKhuVuc = maKhuVuc;
        this.tenKhuVuc = tenKhuVuc;
        this.soLuongBan = soLuongBan;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaKhuVuc() { return maKhuVuc; }
    public void setMaKhuVuc(String maKhuVuc) { this.maKhuVuc = maKhuVuc; }

    public String getTenKhuVuc() { return tenKhuVuc; }
    public void setTenKhuVuc(String tenKhuVuc) { this.tenKhuVuc = tenKhuVuc; }

    public int getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(int soLuongBan) { this.soLuongBan = soLuongBan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    @Override
    public String toString() {
        return tenKhuVuc != null ? tenKhuVuc : "";
    }
}