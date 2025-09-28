package entity;

import java.sql.Timestamp;

public class LichSuDangNhap {
    private int maLichSu;
    private String maTaiKhoan;
    private Timestamp thoiGianDangNhap;
    private boolean trangThai;

    // Constructor mặc định
    public LichSuDangNhap() {
    }

    // Constructor đầy đủ
    public LichSuDangNhap(int maLichSu, String maTaiKhoan, Timestamp thoiGianDangNhap, boolean trangThai) {
        this.maLichSu = maLichSu;
        this.maTaiKhoan = maTaiKhoan;
        this.thoiGianDangNhap = thoiGianDangNhap;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public int getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(int maLichSu) {
        this.maLichSu = maLichSu;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public Timestamp getThoiGianDangNhap() {
        return thoiGianDangNhap;
    }

    public void setThoiGianDangNhap(Timestamp thoiGianDangNhap) {
        this.thoiGianDangNhap = thoiGianDangNhap;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "LichSuDangNhap{" +
                "maLichSu=" + maLichSu +
                ", maTaiKhoan='" + maTaiKhoan + '\'' +
                ", thoiGianDangNhap=" + thoiGianDangNhap +
                ", trangThai=" + trangThai +
                '}';
    }
}