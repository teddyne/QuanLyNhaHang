package entity;

public class Ban {
    private String maBan;
    private String maKhuVuc;
    private String tenKhuVuc;
    private String maLoai;
    private String tenLoai;
    private int soChoNgoi;
    private String trangThai;
    private String ghiChu;
    private int sucChua;
    private Integer soNguoiPackage;
    // Constructors
    public Ban() {}

    public Ban(String maBan, String maKhuVuc, String tenKhuVuc, String maLoai, String tenLoai, int soChoNgoi, String trangThai, String ghiChi) {
    	this.maBan = maBan;
    	this.maKhuVuc = maKhuVuc;
    	this.tenKhuVuc = tenKhuVuc;
    	this.maLoai = maLoai;
    	this.tenLoai = tenLoai;
    	this.trangThai = trangThai;
    	this.ghiChu = ghiChu;
    }

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public String getMaKhuVuc() {
		return maKhuVuc;
	}

	public void setMaKhuVuc(String maKhuVuc) {
		this.maKhuVuc = maKhuVuc;
	}

	public String getTenKhuVuc() {
		return tenKhuVuc;
	}

	public void setTenKhuVuc(String tenKhuVuc) {
		this.tenKhuVuc = tenKhuVuc;
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

	public int getSoChoNgoi() {
		return soChoNgoi;
	}

	public void setSoChoNgoi(int soChoNgoi) {
		this.soChoNgoi = soChoNgoi;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public int getSucChua() {
        return sucChua;
    }

    public Integer getSoNguoiPackage() {
        return soNguoiPackage;
    }

	public void setSucChua(int sucChua) {
		this.sucChua = sucChua;
	}

	public void setSoNguoiPackage(Integer soNguoiPackage) {
		this.soNguoiPackage = soNguoiPackage;
	}

}