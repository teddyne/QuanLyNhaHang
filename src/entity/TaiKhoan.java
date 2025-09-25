package entity;

import java.util.UUID;

public class TaiKhoan {
    private String maTaiKhoan;
    private String soDienThoai;
    private String matKhau;
    private String maNhanVien;
    private String phanQuyen;

    public TaiKhoan() {
        this.maTaiKhoan = "{" + UUID.randomUUID().toString() + "}";
        this.soDienThoai = "";
        this.matKhau = "";
        this.maNhanVien = "";
        this.phanQuyen = "LeTan";
    }

    public TaiKhoan(String soDienThoai, String matKhau, String maNhanVien, String phanQuyen) {
        this.maTaiKhoan = "{" + UUID.randomUUID().toString() + "}";
        setSoDienThoai(soDienThoai);
        setMatKhau(matKhau);
        this.maNhanVien = maNhanVien;
        setPhanQuyen(phanQuyen);
    }

    public TaiKhoan(TaiKhoan other) {
        this.maTaiKhoan = other.maTaiKhoan;
        this.soDienThoai = other.soDienThoai;
        this.matKhau = other.matKhau;
        this.maNhanVien = other.maNhanVien;
        this.phanQuyen = other.phanQuyen;
    }

    // Getter
    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getPhanQuyen() {
        return phanQuyen;
    }

    // Setter
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai.matches("0\\d{9}")) {
            this.soDienThoai = soDienThoai;
        } else {
            throw new IllegalArgumentException("SĐT không hợp lệ");
        }
    }

    public void setMatKhau(String matKhau) {
        if (matKhau.length() >= 6) {
            this.matKhau = matKhau;
        } else {
            throw new IllegalArgumentException("Mật khẩu >= 6 ký tự");
        }
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public void setPhanQuyen(String phanQuyen) {
        if (phanQuyen.equals("QuanLy") || phanQuyen.equals("LeTan")) {
            this.phanQuyen = phanQuyen;
        } else {
            throw new IllegalArgumentException("Phân quyền chỉ nhận QuanLy hoặc LeTan");
        }
    }

    @Override
    public String toString() {
        return "Tài khoản: " + soDienThoai + " – Quyền: " + phanQuyen;
    }
}