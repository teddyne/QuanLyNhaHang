﻿use QLNH

-- Bảng KhachHang
CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
    sdt NVARCHAR(15),
    ngaySinh Date,
    loaiKH NVARCHAR(50),
	CHECK (loaiKH IN (N'Khách thường', N'Thành viên'))
);

-- Bảng NhanVien
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    anhNV NVARCHAR(100),
    ngaySinh DATE,
    gioiTinh BIT,
    cccd NVARCHAR(50),
    email NVARCHAR(100),
    sdt NVARCHAR(15),
    trangThai BIT,
    chucVu NVARCHAR(50)
);

-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    maTaiKhoan VARCHAR(100) PRIMARY KEY DEFAULT ('{' + CONVERT(VARCHAR(36), NEWID()) + '}'),
    soDienThoai VARCHAR(10) NOT NULL UNIQUE,
    matKhau VARCHAR(255) NOT NULL CHECK (LEN(matKhau) >= 6),
    maNhanVien NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES NhanVien(maNhanVien),
    phanQuyen VARCHAR(20) NOT NULL CHECK (phanQuyen IN ('QuanLy', 'LeTan')),
    CONSTRAINT chk_sdt CHECK (soDienThoai LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' AND LEN(soDienThoai) = 10)
);

-- Bảng LichSuDangNhap
CREATE TABLE LichSuDangNhap (
    maLichSu INT IDENTITY(1,1) PRIMARY KEY,
    maTaiKhoan VARCHAR(100) NOT NULL FOREIGN KEY REFERENCES TaiKhoan(maTaiKhoan),
    thoiGianDangNhap DATETIME NOT NULL DEFAULT GETDATE(),
    trangThai BIT NOT NULL -- 1: Success, 0: Failure
);


-- Bảng KhuVuc
CREATE TABLE KhuVuc (
    maKhuVuc NVARCHAR(20) PRIMARY KEY,
    tenKhuVuc NVARCHAR(100) NOT NULL,
    soLuongBan INT NOT NULL,
    trangThai NVARCHAR(50) CHECK (trangThai IN (N'Hoạt động', N'Ngừng'))
);

-- Bảng Ban
CREATE TABLE Ban (
    maBan NVARCHAR(20) PRIMARY KEY,
	maKhuVuc NVARCHAR(20) FOREIGN KEY REFERENCES KhuVuc(maKhuVuc),
    soChoNgoi INT NOT NULL CHECK (soChoNgoi > 0),
    loaiBan NVARCHAR(50) CHECK (loaiBan IN (N'Thường', N'VIP')),
	trangThai NVARCHAR(50) CHECK (trangThai IN (N'Trống', N'Đặt', N'Đang phục vụ')),
	ghiChu NVARCHAR(200)
);

--Bảng loại món
CREATE TABLE LoaiMon (
    maLoai NVARCHAR(20) PRIMARY KEY,
    tenLoai NVARCHAR(100) NOT NULL
);

-- Bảng MonAn
CREATE TABLE MonAn (
    maMon NVARCHAR(20) PRIMARY KEY,
    tenMon NVARCHAR(100) NOT NULL,
	anhMon NVARCHAR(100),
	maLoai NVARCHAR(20) FOREIGN KEY REFERENCES LoaiMon(maLoai),
	donGia DECIMAL(18,2) NOT NULL CHECK (donGia > 0),
    trangThai BIT,
    moTa NVARCHAR(200)
);

-- Bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM NVARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    loaiKM NVARCHAR(50) CHECK (loaiKM IN (N'Phần trăm', N'Giảm trực tiếp', N'Sinh nhật', N'Tặng món')),
    giaTri DECIMAL(18,2),
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    trangThai NVARCHAR(50) CHECK (trangThai IN (N'Đang áp dụng', N'Sắp áp dụng', N'Hết hạn')),
	doiTuongApDung NVARCHAR(50) CHECK (doiTuongApDung IN (N'Khách thường', N'Thành viên', N'Tất cả')),
    donHangTu DECIMAL(18,2),
    mon1 NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    mon2 NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    monTang NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    ghiChu NVARCHAR(200),
	CONSTRAINT CK_NgayKhuyenMai CHECK (ngayKetThuc >= ngayBatDau)
);

-- Bảng PhieuDatBan
CREATE TABLE PhieuDatBan (
    maPhieu NVARCHAR(20) PRIMARY KEY,
	maBan NVARCHAR(20) FOREIGN KEY REFERENCES Ban(maBan),
	maKH NVARCHAR(20) FOREIGN KEY REFERENCES KhachHang(maKH),
	soNguoi INT CHECK (soNguoi > 0),
	ngayDen DATE NOT NULL,
    gioDen TIME NOT NULL,
	ghiChu NVARCHAR(200) NULL,
    tienCoc DECIMAL(18,2) CHECK (tienCoc >= 0),
	ghiChuCoc NVARCHAR (200) NULL,
    trangThai NVARCHAR(20) NOT NULL DEFAULT N'Đặt',
    maNhanVien NVARCHAR(20) FOREIGN KEY REFERENCES NhanVien(maNhanVien),
);

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHD NVARCHAR(20) PRIMARY KEY,
    ngayLap DATE NOT NULL,
	trangThai NVARCHAR(50) CHECK (trangThai IN (N'Chưa thanh toán', N'Đã thanh toán', N'Đã cọc', N'Đã hủy')),
	maPhieu NVARCHAR(20) FOREIGN KEY REFERENCES PhieuDatBan(maPhieu),
    maKH NVARCHAR(20) FOREIGN KEY REFERENCES KhachHang(maKH),
    maKM NVARCHAR(20) NULL FOREIGN KEY REFERENCES KhuyenMai(maKM),
    maNhanVien NVARCHAR(20) FOREIGN KEY REFERENCES NhanVien(maNhanVien),
    ghiChu NVARCHAR(200)
);

-- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHD NVARCHAR(20) FOREIGN KEY REFERENCES HoaDon(maHD),
    maMon NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
	CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maHD, maMon),
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia DECIMAL(18,2) NOT NULL CHECK (donGia > 0),
    ghiChu NVARCHAR(200)
);



--INSERT dữ liệu
-- Bảng KhachHang
INSERT INTO KhachHang VALUES ('KH0001', N'Nguyễn Văn A', 'a@gmail.com', '0912345678', '1995-05-10', N'Khách thường');
INSERT INTO KhachHang VALUES ('KH0002', N'Trần Thị B', 'b@gmail.com', '0923456789', '2000-11-20', N'Thành viên');
INSERT INTO KhachHang VALUES ('KH0003', N'Lê Văn C', 'c@gmail.com', '0934567890', '1988-03-15', N'Thành viên');

-- Bảng NhanVien
INSERT INTO NhanVien VALUES ('NV0001', N'Bùi Ngọc Hiền', 'nv1.img', '1995-08-12', 0, '075123456789', 'hien@gmail.com', '0987654321', 1, N'Quản lý');
INSERT INTO NhanVien VALUES ('NV0002', N'Nguyễn Trương An Thy', 'nv2.img', '1995-01-17', 0, '075987654321', 'thy@gmail.com', '0978123456', 1, N'Lễ tân');
INSERT INTO NhanVien VALUES ('NV0003', N'Ừng Thị Thanh Trúc', 'nv3.img', '1995-05-11', 0, '075654321987', 'truc@gmail.com', '0967812345', 1, N'Lễ tân');
INSERT INTO NhanVien VALUES ('NV0004', N'Nguyễn Nam Khánh', 'nv4.img', '1995-10-25', 1, '075289364950', 'khanh@gmail.com', '0345678912', 1, N'Lễ tân');

-- Bảng TaiKhoan
INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES
('0987654321', 'hien123', 'NV0001', 'QuanLy'),
('0978123456', 'thy456',  'NV0002', 'LeTan'),
('0967812345', 'truc789', 'NV0003', 'LeTan'),
('0945678912', 'khanh321','NV0004', 'LeTan');


-- Bảng KhuVuc
INSERT INTO KhuVuc VALUES ('KV0001', N'Trong nhà', 6, N'Hoạt động');
INSERT INTO KhuVuc VALUES ('KV0002', N'Sân vườn', 4, N'Hoạt động');
INSERT INTO KhuVuc VALUES ('KV0003', N'Ngoài trời', 2, N'Hoạt động');

-- Bảng Ban
INSERT INTO Ban VALUES ('B0001', 4, N'Đang phục vụ', 'KV0001', N'Thường', NULL);
INSERT INTO Ban VALUES ('B0002', 6, N'Đã đặt', 'KV0001', N'VIP', NULL);
INSERT INTO Ban VALUES ('B0003', 8, N'Trống', 'KV0002', N'Thường', NULL);

-- Bảng Ban
INSERT INTO Ban VALUES ('B0001', 'KV0001', 4, N'Thường', N'Trống', NULL);
INSERT INTO Ban VALUES ('B0002', 'KV0001', 6, N'VIP', N'Trống', NULL);
INSERT INTO Ban VALUES ('B0003', 'KV0002', 8, N'Thường', N'Đang phục vụ', NULL);
INSERT INTO Ban VALUES ('B0004', 'KV0003', 2, N'Thường', N'Trống', NULL);
INSERT INTO Ban VALUES ('B0005', 'KV0001', 4, N'Thường', N'Đặt', NULL);

-- Bảng LoaiMon
INSERT INTO LoaiMon VALUES ('LM0001', N'Món chính');
INSERT INTO LoaiMon VALUES ('LM0002', N'Tráng miệng');

-- Bảng MonAn
INSERT INTO MonAn VALUES ('MON0001', N'Lẩu hải sản', 'lauhaisan.img', 'LM0001', 150000, 1, NULL);
INSERT INTO MonAn VALUES ('MON0002', N'Cơm chiên dương châu', 'comchien.img', 'LM0001', 40000, 1, NULL);
INSERT INTO MonAn VALUES ('MON0003', N'Bánh flan', 'flan.img', 'LM0002', 15000, 1, NULL);

-- Bảng KhuyenMai
INSERT INTO KhuyenMai VALUES ('KM0001', N'Giảm 10%', N'Phần trăm', 10, '2024-07-10', '2024-07-20', N'Hết hạn', N'Tất cả', 2000000, NULL, NULL, NULL, NULL);
INSERT INTO KhuyenMai VALUES ('KM0002', N'Giảm 50k', N'Giảm trực tiếp', 50000, '2025-10-27', '2025-11-15', N'Sắp áp dụng', N'Tất cả', 500000, NULL, NULL, NULL, NULL);
INSERT INTO KhuyenMai VALUES ('KM0003', N'Mua 2 tặng 1 flan', N'Tặng món', 0, '2025-09-27', '2025-10-15', N'Đang áp dụng', N'Tất cả', 0, 'MON0003', 'MON0003', 'MON0003', NULL);

-- Bảng PhieuDatBan
INSERT INTO PhieuDatBan VALUES ('P0001', '2025-09-20', '18:00:00', 4, N'Đã đặt', 0, NULL, 'KH0001', 'NV0002', 'B0001', 'KV0001');
INSERT INTO PhieuDatBan VALUES ('P0002', '2025-09-24', '19:30:00', 6, N'Đã đặt', 0, NULL, 'KH0002', 'NV0002', 'B0002', 'KV0001');

-- Bảng PhieuDatBan
INSERT INTO PhieuDatBan VALUES ('P0001', 'B0005', 'KH0001', 4, '2025-10-20', '18:00:00', NULL, 200000, NULL, N'Đặt', 'NV0002');
INSERT INTO PhieuDatBan VALUES ('P0002', 'B0002', 'KH0002', 6, '2025-10-04', '19:30:00', N'Hẹn gặp mặt', 0, NULL, N'Đặt', 'NV0003');
INSERT INTO PhieuDatBan VALUES ('P0003', 'B0003', 'KH0003', 2, '2025-10-07', '12:00:00', NULL, 0, NULL, N'Đặt', 'NV0004');
INSERT INTO PhieuDatBan VALUES ('P0004', 'B0001', 'KH0002', 6, '2025-09-04', '19:30:00', NULL, 0, NULL, N'Đặt', 'NV0003');
INSERT INTO PhieuDatBan VALUES ('P0005', 'B0001', 'KH0001', 2, '2025-09-07', '12:00:00', NULL, 0, NULL, N'Đặt', 'NV0004');

-- Bảng HoaDon
INSERT INTO HoaDon VALUES ('HD0001', '2025-09-04', N'Đã thanh toán', 'P0004', 'KH0002', NULL, 'NV0003', NULL);
INSERT INTO HoaDon VALUES ('HD0002', '2025-09-07', N'Đã thanh toán', 'P0005', 'KH0001', NULL, 'NV0004', NULL);

-- Bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon VALUES ('HD0001', 'MON0001', 1, 150000, NULL);
INSERT INTO ChiTietHoaDon VALUES ('HD0001', 'MON0002', 2, 40000, NULL);
INSERT INTO ChiTietHoaDon VALUES ('HD0001', 'MON0003', 2, 15000, NULL);

INSERT INTO ChiTietHoaDon VALUES ('HD0002', 'MON0001', 1, 40000, NULL);
INSERT INTO ChiTietHoaDon VALUES ('HD0002', 'MON0003', 1, 15000, NULL);

select * from KhachHang
select * from NhanVien
select * from Ban
select * from KhuyenMai
select * from PhieuDatBan
select * from HoaDon
select * from ChiTietHoaDon
select * from MonAn
select * from TaiKhoan