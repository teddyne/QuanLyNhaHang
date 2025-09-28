package entity;

public class TaiKhoan {
    private String maTaiKhoan;
    private String soDienThoai;
    private String matKhau;
    private String maNhanVien;
    private String phanQuyen;
    private String hoTen;

    // Constructor mặc định
    public TaiKhoan() {
    }

    // Constructor đầy đủ
    public TaiKhoan(String maTaiKhoan, String soDienThoai, String matKhau, String maNhanVien, String phanQuyen, String hoTen) {
        this.maTaiKhoan = maTaiKhoan;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.phanQuyen = phanQuyen;
        this.hoTen = hoTen;
    }

    // Getters và Setters
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
        if (soDienThoai != null && !soDienThoai.matches("0[0-9]{9}")) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số");
        }
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (matKhau != null && matKhau.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
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
        if (phanQuyen != null && !phanQuyen.equals("QuanLy") && !phanQuyen.equals("LeTan")) {
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

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTaiKhoan='" + maTaiKhoan + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", phanQuyen='" + phanQuyen + '\'' +
                ", hoTen='" + hoTen + '\'' +
                '}';
    }
}