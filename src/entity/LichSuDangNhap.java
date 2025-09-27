package entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class LichSuDangNhap {
    private String maLichSu;
    private String maTaiKhoan;
    private LocalDateTime thoiGianDangNhap;

    public LichSuDangNhap() {
        this.maLichSu = "{" + UUID.randomUUID().toString() + "}";
        this.thoiGianDangNhap = LocalDateTime.now();
    }

    public LichSuDangNhap(String maTaiKhoan) {
        this();
        this.maTaiKhoan = maTaiKhoan;
    }

    // Getter Setter
    public String getMaLichSu() { return maLichSu; }
    public String getMaTaiKhoan() { return maTaiKhoan; }
    public LocalDateTime getThoiGianDangNhap() { return thoiGianDangNhap; }

    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }
    public void setThoiGianDangNhap(LocalDateTime thoiGianDangNhap) { this.thoiGianDangNhap = thoiGianDangNhap; }
}
