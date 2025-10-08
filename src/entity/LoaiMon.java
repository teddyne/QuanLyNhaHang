package entity;

public class LoaiMon {
    private String maLoai;
    private String tenLoai;

    public LoaiMon() {}

    public LoaiMon(String maLoai, String tenLoai) {
        setMaLoai(maLoai);
        setTenLoai(tenLoai);
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        if (maLoai == null || maLoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã loại không được rỗng");
        }
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        if (tenLoai == null || tenLoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại không được rỗng");
        }
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return "LoaiMon{" +
                "maLoai='" + maLoai + '\'' +
                ", tenLoai='" + tenLoai + '\'' +
                '}';
    }
}
