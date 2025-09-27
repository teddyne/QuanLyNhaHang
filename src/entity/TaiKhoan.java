package entity;

public class TaiKhoan {
    private String maTaiKhoan;
    private String soDienThoai;
    private String matKhau;
    private String maNhanVien;
    private String phanQuyen;
    private String hoTen; // Changed from tenNhanVien to hoTen to match database

    public TaiKhoan() {
    }

    public TaiKhoan(String maTaiKhoan, String soDienThoai, String matKhau, String maNhanVien, String phanQuyen, String hoTen) {
        this.maTaiKhoan = maTaiKhoan;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.phanQuyen = phanQuyen;
        this.hoTen = hoTen;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getPhanQuyen() {
        return phanQuyen;
    }

    public void setPhanQuyen(String phanQuyen) {
        if (phanQuyen == null || (!phanQuyen.equals("QuanLy") && !phanQuyen.equals("LeTan"))) {
            throw new IllegalArgumentException("Phân quyền phải là 'QuanLy' hoặc 'LeTan'");
        }
        this.phanQuyen = phanQuyen;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
}