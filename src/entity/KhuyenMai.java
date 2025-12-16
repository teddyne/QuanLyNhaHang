package entity;

import java.sql.Date;

public class KhuyenMai {

    private String maKM;
    private String tenKM;
    private String maLoai;
    private double giaTri;
    private double donHangTu;
    private double giamToiDa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai;
    private String doiTuongApDung;
    private String ghiChu;
    
	public KhuyenMai(String maKM, String tenKM, String maLoai, double giaTri, double donHangTu, double giamToiDa,
			Date ngayBatDau, Date ngayKetThuc, String trangThai, String doiTuongApDung, String ghiChu) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.maLoai = maLoai;
		this.giaTri = giaTri;
		this.donHangTu = donHangTu;
		this.giamToiDa = giamToiDa;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.trangThai = trangThai;
		this.doiTuongApDung = doiTuongApDung;
		this.ghiChu = ghiChu;
	}

	public KhuyenMai() {
		super();
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public String getMaLoai() {
		return maLoai;
	}

	public void setMaLoai(String maLoai) {
		this.maLoai = maLoai;
	}

	public double getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(double giaTri) {
		this.giaTri = giaTri;
	}

	public double getDonHangTu() {
		return donHangTu;
	}

	public void setDonHangTu(double donHangTu) {
		this.donHangTu = donHangTu;
	}

	public double getGiamToiDa() {
		return giamToiDa;
	}

	public void setGiamToiDa(double giamToiDa) {
		this.giamToiDa = giamToiDa;
	}

	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getDoiTuongApDung() {
		return doiTuongApDung;
	}

	public void setDoiTuongApDung(String doiTuongApDung) {
		this.doiTuongApDung = doiTuongApDung;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	@Override
	public String toString() {
	    return tenKM;
	}
    
}