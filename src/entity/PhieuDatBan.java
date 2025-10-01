package entity;

import java.sql.Date;
import java.sql.Time;

public class PhieuDatBan {
    private String maPhieu;
    private String maBan;
    private String tenKhach;
    private String soDienThoai;
    private int soNguoi;
    private Date ngayDen;
    private Time gioDen;
    private String ghiChu;
    private double tienCoc;
    private String ghiChuCoc;
    private String trangThai;

    // Constructors
    public PhieuDatBan() {}

    public PhieuDatBan(String maPhieu, String maBan, String tenKhach, String soDienThoai, int soNguoi,
                  Date ngayDen, Time gioDen, String ghiChu, double tienCoc, String ghiChuCoc, String trangThai) {
        this.maPhieu = maPhieu;
        this.maBan = maBan;
        this.tenKhach = tenKhach;
        this.soDienThoai = soDienThoai;
        this.soNguoi = soNguoi;
        this.ngayDen = ngayDen;
        this.gioDen = gioDen;
        this.ghiChu = ghiChu;
        this.tienCoc = tienCoc;
        this.ghiChuCoc = ghiChuCoc;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }

    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public int getSoNguoi() { return soNguoi; }
    public void setSoNguoi(int soNguoi) { this.soNguoi = soNguoi; }

    public Date getNgayDen() { return ngayDen; }
    public void setNgayDen(Date ngayDen) { this.ngayDen = ngayDen; }

    public Time getGioDen() { return gioDen; }
    public void setGioDen(Time gioDen) { this.gioDen = gioDen; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }

    public String getGhiChuCoc() { return ghiChuCoc; }
    public void setGhiChuCoc(String ghiChuCoc) { this.ghiChuCoc = ghiChuCoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}