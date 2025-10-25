package entity;

import java.util.Objects;
import java.time.LocalDate; // --- THAY ĐỔI: Thêm import

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private LocalDate ngaySinh; 
    private String email;
    private String loaiKH;

    // --- CONSTRUCTORS ---

    public KhachHang(String maKH, String tenKH, String sdt, LocalDate ngaySinh, String email, String loaiKH) {
        setMaKH(maKH);
        setTenKH(tenKH);
        setSdt(sdt);
        setNgaySinh(ngaySinh); 
        setEmail(email);
        setLoaiKH(loaiKH); 
    }

    public KhachHang(String maKH, String tenKH, String sdt, LocalDate ngaySinh, String email) {
        this(maKH, tenKH, sdt, ngaySinh, email, "Khách thường"); 
    }

    public KhachHang() {
        // TODO Auto-generated constructor stub
    }

    // --- GETTERS AND SETTERS (với VALIDATION) ---

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKhachHang) {
        String regexMaKH = "^KH\\d{4}$";
        if (maKhachHang != null && maKhachHang.matches(regexMaKH))
            this.maKH = maKhachHang;
        else
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ! (VD: KH0001).");
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKhachHang) {
        String regexTenKH = "^[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*([ ]+[A-ZÀ-Ỵ][a-zà-ỹ\\p{L}]*)*$";
        if (tenKhachHang != null && !tenKhachHang.trim().isEmpty() && tenKhachHang.matches(regexTenKH))
            this.tenKH = tenKhachHang;
        else
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ! (Phải viết hoa chữ cái đầu).");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (email == null || email.matches(regexEmail))
            this.email = email;
        else
            throw new IllegalArgumentException("Email không hợp lệ!");
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String soDienThoai) {
        String regexSdt = "^(03|05|07|08|09)\\d{8}$";
        if (soDienThoai != null && soDienThoai.matches(regexSdt))
            this.sdt = soDienThoai;
        else
            throw new IllegalArgumentException("Số điện thoại không hợp lệ! (10 số, bắt đầu bằng 03,05,07,08,09).");
    }


    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
 
        if (ngaySinh != null && ngaySinh.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ (Không được là ngày trong tương lai).");
        }
        this.ngaySinh = ngaySinh;
    }
   

    public String getloaiKH() {
        return loaiKH;
    }

    public void setLoaiKH(String loaiKhachHang) {
        if (loaiKhachHang != null) {
            this.loaiKH = loaiKhachHang;
        } else {
            this.loaiKH = "Khách thường";
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
        // --- THAY ĐỔI ---
        return "KhachHang [maKhachHang=" + maKH + ", tenKhachHang=" + tenKH + ", soDienThoai=" + sdt + 
               ", ngaySinh=" + ngaySinh + ", email=" + email + ", loaiKhachHang=" + loaiKH + "]";
    }
}