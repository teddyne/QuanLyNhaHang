package entity;

public class LoaiBan {
    private String maLoai;
    private String tenLoai;

    public LoaiBan() {}

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoaiBan loaiBan = (LoaiBan) obj;
        return maLoai.equals(loaiBan.maLoai);
    }


    @Override
    public String toString() {
        return tenLoai != null ? tenLoai : ""; // Trả về tenLoai để hiển thị trong JComboBox
    }
}