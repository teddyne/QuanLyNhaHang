package entity;

import java.sql.Date;

public class KhuyenMai {

	private String maKM;
    private String tenKM;
    private String loaiKM;
    private double giaTri;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai;
    private String doiTuongApDung;
    private double donHangTu;
    private String mon1;
    private String mon2;
    private String monTang;
    private String ghiChu;
    
	public KhuyenMai(String maKM, String tenKM, String loaiKM, double giaTri, Date ngayBatDau, Date ngayKetThuc,
			String trangThai, String doiTuongApDung, double donHangTu, String mon1, String mon2, String monTang,
			String ghiChu) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.loaiKM = loaiKM;
		this.giaTri = giaTri;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.trangThai = trangThai;
		this.doiTuongApDung = doiTuongApDung;
		this.donHangTu = donHangTu;
		this.mon1 = mon1;
		this.mon2 = mon2;
		this.monTang = monTang;
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

	public String getLoaiKM() {
		return loaiKM;
	}

	public void setLoaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public double getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(double giaTri) {
		this.giaTri = giaTri;
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

	public double getDonHangTu() {
		return donHangTu;
	}

	public void setDonHangTu(double donHangTu) {
		this.donHangTu = donHangTu;
	}

	public String getMon1() {
		return mon1;
	}

	public void setMon1(String mon1) {
		this.mon1 = mon1;
	}

	public String getMon2() {
		return mon2;
	}

	public void setMon2(String mon2) {
		this.mon2 = mon2;
	}

	public String getMonTang() {
		return monTang;
	}

	public void setMonTang(String monTang) {
		this.monTang = monTang;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", loaiKM=" + loaiKM + ", giaTri=" + giaTri
				+ ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", trangThai=" + trangThai
				+ ", doiTuongApDung=" + doiTuongApDung + ", donHangTu=" + donHangTu + ", mon1=" + mon1 + ", mon2="
				+ mon2 + ", monTang=" + monTang + ", ghiChu=" + ghiChu + "]";
	}
    
}