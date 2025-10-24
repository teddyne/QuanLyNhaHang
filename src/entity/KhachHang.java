package entity;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String ngaySinh; // Đã đổi
    private String email;
    private String maLoaiKH;

    // --- CONSTRUCTORS ---

    public KhachHang(String maKH, String tenKH, String sdt, String ngaySinh, String email, String maLoaiKH) { // Đã đổi
        setMaKH(maKH);
        setTenKH(tenKH);
        setSdt(sdt);
        setNgaySinh(ngaySinh); // Đã đổi
        setEmail(email);
        setMaLoaiKH(maLoaiKH); 
    }

    // Constructor này có thể cần xem lại logic, tạm thời đổi cccd -> ngaySinh
    public KhachHang(String maKH, String tenKH, String sdt, String ngaySinh, String email) { // Đã đổi
        this(maKH, tenKH, sdt, ngaySinh, email, "Khách thường"); // Đã đổi
    }

    // --- GETTERS AND SETTERS (với VALIDATION) ---

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        String regexMaKH = "^KH\\d{4}$";
        if (maKH != null && maKH.matches(regexMaKH))
            this.maKH = maKH;
        else
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ! (VD: KH0001).");
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        String regexTenKH = "^[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*([ ]+[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*)*$";
        if (tenKH != null && !tenKH.trim().isEmpty() && tenKH.matches(regexTenKH))
            this.tenKH = tenKH;
        else
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ! (Phải viết hoa chữ cái đầu).");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (email != null && email.matches(regexEmail))
            this.email = email;
        else
            throw new IllegalArgumentException("Email không hợp lệ!");
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        String regexSdt = "^(03|05|07|08|09)\\d{8}$";
        if (sdt != null && sdt.matches(regexSdt))
            this.sdt = sdt;
        else
            throw new IllegalArgumentException("Số điện thoại không hợp lệ! (10 số, bắt đầu bằng 03,05,07,08,09).");
    }

    // Đã đổi getCccd
    public String getNgaySinh() {
        return ngaySinh;
    }

    // Đã đổi setCccd
    public void setNgaySinh(String ngaySinh) {
        // Cho phép ngày sinh null hoặc rỗng
        if (ngaySinh == null || ngaySinh.trim().isEmpty()) {
            this.ngaySinh = null;
            return;
        }
        
        String regexNgaySinh = "^\\d{4}-\\d{2}-\\d{2}$"; // Ví dụ format YYYY-MM-DD
        if (ngaySinh.matches(regexNgaySinh)) {
            this.ngaySinh = ngaySinh;
        } else {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ! (Định dạng YYYY-MM-DD hoặc để trống).");
        }

        // Hiện tại chỉ gán trực tiếp
         this.ngaySinh = ngaySinh;
    }

    public String getMaLoaiKH() {
        return maLoaiKH;
    }

    public void setMaLoaiKH(String maLoaiKH) {

        String regexMaLoaiKH = "^LKH\\d{4}$";
        if (maLoaiKH != null && maLoaiKH.matches(regexMaLoaiKH)) {
            this.maLoaiKH = maLoaiKH;
        } else {
             this.maLoaiKH = maLoaiKH;
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
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt + 
               ", ngaySinh=" + ngaySinh + ", email=" + email + ", maLoaiKH=" + maLoaiKH + "]"; // Đã đổi
    }
}