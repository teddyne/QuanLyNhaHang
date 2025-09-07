package entity;

import java.sql.Date;
import java.sql.Time;

public class Ban {
    private String maBan;
    private String khuVuc;
    private int soGhe;
    private String trangThai;
    private int x;
    private int y;
    private String tenKhach;
    private String soDienThoai;
    private int soNguoi;
    private Date ngayDat;
    private Time gioDat;
    private String ghiChu;
    private double tienCoc;
    private String ghiChuCoc;

    public Ban() {}

    public Ban(String maBan, String khuVuc, int soGhe, String trangThai, int x, int y) {
        this.maBan = maBan;
        this.khuVuc = khuVuc;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
        this.x = x;
        this.y = y;
    }

    // Getters and Setters
    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }
    public String getKhuVuc() { return khuVuc; }
    public void setKhuVuc(String khuVuc) { this.khuVuc = khuVuc; }
    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public int getSoNguoi() { return soNguoi; }
    public void setSoNguoi(int soNguoi) { this.soNguoi = soNguoi; }
    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }
    public Time getGioDat() { return gioDat; }
    public void setGioDat(Time gioDat) { this.gioDat = gioDat; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }
    public String getGhiChuCoc() { return ghiChuCoc; }
    public void setGhiChuCoc(String ghiChuCoc) { this.ghiChuCoc = ghiChuCoc; }
}