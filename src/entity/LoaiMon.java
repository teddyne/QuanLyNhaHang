// entity/LoaiMon.java
package entity;

public class LoaiMon {
    private String maLoai;
    private String tenLoai;
    private int trangThai = 1; // MẶC ĐỊNH HOẠT ĐỘNG

    public LoaiMon() {}

    public LoaiMon(String maLoai, String tenLoai) {
        this(maLoai, tenLoai, 1);
    }

    public LoaiMon(String maLoai, String tenLoai, int trangThai) {
        if (maLoai != null && !maLoai.trim().isEmpty()) {
            setMaLoai(maLoai); // chỉ set nếu có giá trị
        }
        setTenLoai(tenLoai);
        setTrangThai(trangThai);
    }


    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) {
        if (maLoai == null || maLoai.trim().isEmpty())
            throw new IllegalArgumentException("Mã loại không được rỗng");
        this.maLoai = maLoai;
    }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) {
        if (tenLoai == null || tenLoai.trim().isEmpty())
            throw new IllegalArgumentException("Tên loại không được rỗng");
        this.tenLoai = tenLoai;
    }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) {
        if (trangThai != 0 && trangThai != 1)
            throw new IllegalArgumentException("Trạng thái phải là 0 hoặc 1");
        this.trangThai = trangThai;
    }

    public String getTrangThaiText() {
        return trangThai == 1 ? "Hoạt động" : "Ẩn";
    }

    @Override
    public String toString() {
        return "LoaiMon{" +
                "maLoai='" + maLoai + '\'' +
                ", tenLoai='" + tenLoai + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}