package entity;

import java.sql.Date;
import java.time.LocalDate;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String anhNV;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String cccd;
    private String email;
    private String sdt;
    private boolean trangThai;
    private String chucVu;

    public NhanVien() {}

    public NhanVien(String maNhanVien, String hoTen, String anhNV,
                    LocalDate ngaySinh, boolean gioiTinh,
                    String cccd, String email, String sdt,
                    boolean trangThai, String chucVu) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.anhNV = anhNV;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.cccd = cccd;
        this.email = email;
        this.sdt = sdt;
        this.trangThai = trangThai;
        this.chucVu = chucVu;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty())
            throw new IllegalArgumentException("Mã nhân viên không được rỗng");
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty())
            throw new IllegalArgumentException("Họ tên không được rỗng");
        this.hoTen = hoTen;
    }

    public String getAnhNV() {
        return anhNV;
    }

    public void setAnhNV(String anhNV) {
        this.anhNV = anhNV;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", anhNV='" + anhNV + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + (gioiTinh ? "Nam" : "Nữ") +
                ", cccd='" + cccd + '\'' +
                ", email='" + email + '\'' +
                ", sdt='" + sdt + '\'' +
                ", trangThai=" + (trangThai ? "Đang làm" : "Nghỉ") +
                ", chucVu='" + chucVu + '\'' +
                '}';
    }
}
