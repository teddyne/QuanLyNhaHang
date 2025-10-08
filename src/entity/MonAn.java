package entity;

public class MonAn {
    private String maMon;
    private String tenMon;
    private String anhMon;
    private LoaiMon loaiMon;
    private double donGia;
    private boolean trangThai;
    private String moTa;

    public MonAn() {}

    public MonAn(String maMon, String tenMon, String anhMon, LoaiMon loaiMon, double donGia, boolean trangThai, String moTa) {
        setMaMon(maMon);
        setTenMon(tenMon);
        setAnhMon(anhMon);
        setLoaiMon(loaiMon);
        setDonGia(donGia);
        setTrangThai(trangThai);
        setMoTa(moTa);
    }

    // Copy constructor
    public MonAn(MonAn other) {
        this(other.maMon, other.tenMon, other.anhMon, other.loaiMon, other.donGia, other.trangThai, other.moTa);
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        if (maMon == null || !maMon.matches("^MON\\d{4}$")) {
            throw new IllegalArgumentException("Mã món phải theo định dạng MONXXXX (XXXX là số)");
        }
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        if (tenMon == null || tenMon.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên món không được rỗng");
        }
        if (!tenMon.matches("^[\\p{L}][\\p{L}\\s]*$")) {
            throw new IllegalArgumentException("Tên món chỉ được chứa chữ cái và khoảng trắng");
        }
        this.tenMon = tenMon;
    }

    public String getAnhMon() {
        return anhMon;
    }

    public void setAnhMon(String anhMon) {
        this.anhMon = anhMon;
    }

    public LoaiMon getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(LoaiMon loaiMon) {
        if (loaiMon == null) {
            throw new IllegalArgumentException("Loại món không được để trống");
        }
        this.loaiMon = loaiMon;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Giá món không được nhỏ hơn 0");
        }
        this.donGia = donGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả không được rỗng");
        }
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "ThucDon{" +
                "maMon='" + maMon + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", anhMon='" + anhMon + '\'' +
                ", loaiMon=" + (loaiMon != null ? loaiMon.getTenLoai() : "null") +
                ", donGia=" + donGia +
                ", trangThai=" + (trangThai ? "Đang phục vụ" : "Ngừng phục vụ") +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
