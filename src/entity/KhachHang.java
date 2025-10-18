package entity;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String cccd;
    private String email;
    private String loaiKH;

    // --- CONSTRUCTORS ---

    public KhachHang(String maKH, String tenKH, String sdt, String cccd, String email, String loaiKH) {
        setMaKH(maKH);
        setTenKH(tenKH);
        setSdt(sdt);
        setCccd(cccd);
        setEmail(email);
        setLoaiKH(loaiKH);
    }
    
    public KhachHang(String tenKH, String sdt, String cccd, String email, String loaiKH) {
        setTenKH(tenKH);
        setSdt(sdt);
        setCccd(cccd);
        setEmail(email);
        setLoaiKH(loaiKH);
    }

    // --- GETTERS AND SETTERS (với VALIDATION đã cập nhật) ---

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        String regexMaKH = "^KH\\d{4}$";
        if (!maKH.matches(regexMaKH)) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ! (VD: KH0001).");
        }
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được bỏ trống.");
        }
        String regexTenKH = "^[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*([ ]+[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*)*$";
        if (!tenKH.matches(regexTenKH)) {
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ! (Phải viết hoa chữ cái đầu mỗi từ).");
        }
        this.tenKH = tenKH;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được bỏ trống.");
        }
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(regexEmail)) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được bỏ trống.");
        }
        String regexSdt = "^(03|05|07|08|09)\\d{8}$";
        if (!sdt.matches(regexSdt)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ! (10 số, bắt đầu bằng 03,05,07,08,09).");
        }
        this.sdt = sdt;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("Căn cước công dân không được bỏ trống.");
        }
        String regexCccd = "^0\\d{11}$";
        if (!cccd.matches(regexCccd)) {
            throw new IllegalArgumentException("Căn cước công dân không hợp lệ! (Phải đủ 12 số).");
        }
        this.cccd = cccd;
    }

    public String getLoaiKH() {
        return loaiKH;
    }

    public void setLoaiKH(String loaiKH) {
        if (loaiKH != null && (loaiKH.equalsIgnoreCase("Thành viên") || loaiKH.equalsIgnoreCase("Khách thường"))) {
            this.loaiKH = loaiKH;
        } else {
            throw new IllegalArgumentException("Loại khách hàng phải là 'Thành viên' hoặc 'Khách thường'.");
        }
    }

    // --- HASHCODE, EQUALS, TOSTRING ---

    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKH, other.maKH);
    }

    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt + ", cccd=" + cccd + ", email=" + email + ", loaiKH=" + loaiKH + "]";
    }
}