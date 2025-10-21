package entity;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String cccd;
    private String email;
    private String maLoaiKH;

    // --- CONSTRUCTORS ---

    public KhachHang(String maKH, String tenKH, String sdt, String cccd, String email, String maLoaiKH) {
        setMaKH(maKH);
        setTenKH(tenKH);
        setSdt(sdt);
        setCccd(cccd);
        setEmail(email);
        setMaLoaiKH(maLoaiKH); 
    }

    public KhachHang(String maKH, String tenKH, String sdt, String cccd, String email) {
        this(maKH, tenKH, sdt, cccd, email, "Khách thường");
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

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        String regexCccd = "^0\\d{11}$";
        if (cccd != null && cccd.matches(regexCccd))
            this.cccd = cccd;
        else
            throw new IllegalArgumentException("Căn cước công dân không hợp lệ! (Phải đủ 12 số).");
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
               ", cccd=" + cccd + ", email=" + email + ", maLoaiKH=" + maLoaiKH + "]";
    }
}