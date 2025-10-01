package entity;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String cccd;
    private String email;

    public KhachHang(String maKH, String tenKH, String sdt, String cccd, String email) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.cccd = cccd;
        this.email = email;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
