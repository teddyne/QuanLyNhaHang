package entity;

public class LoaiKhuyenMai {
    private String maLoai;
    private String tenLoai;

    public LoaiKhuyenMai() {
    }

    public LoaiKhuyenMai(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return tenLoai; // để JComboBox hiển thị tên loại
    }
}