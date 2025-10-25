create database QLNH

use QLNH
--Bảng LoaiKhachHang
CREATE TABLE LoaiKhachHang (
    maLoaiKH NVARCHAR(20) PRIMARY KEY,
    tenLoaiKH NVARCHAR(100) NOT NULL ,
    moTa NVARCHAR(255) NULL -- Có thể thêm mô tả hoặc các thuộc tính khác sau này
);

-- Bảng KhachHang
CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
	sdt VARCHAR(15) UNIQUE, -- Thêm UNIQUE để tránh trùng SĐT
    ngaySinh Date,
    maLoaiKH NVARCHAR(20) FOREIGN KEY REFERENCES LoaiKhachHang(maLoaiKH) -- Dùng khóa ngoại
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
    sdt VARCHAR(15) UNIQUE,
	trangThai NVARCHAR(50),
	chucVu NVARCHAR(50)
);


-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    maTaiKhoan VARCHAR(100) PRIMARY KEY DEFAULT ('{' + CONVERT(VARCHAR(36), NEWID()) + '}'),
    soDienThoai VARCHAR(15) NOT NULL UNIQUE,
    matKhau VARCHAR(255) NOT NULL CHECK (LEN(matKhau) >= 6),
    maNhanVien NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES NhanVien(maNhanVien),
    phanQuyen VARCHAR(20) NOT NULL CHECK (phanQuyen IN ('QuanLy', 'LeTan')),
	trangThai BIT DEFAULT 1,
    CONSTRAINT chk_sdt CHECK (soDienThoai LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' AND LEN(soDienThoai) = 10)
);

-- Bảng LichSuDangNhap
CREATE TABLE LichSuDangNhap (
    maLichSu INT IDENTITY(1,1) PRIMARY KEY,
    maTaiKhoan VARCHAR(100) NOT NULL FOREIGN KEY REFERENCES TaiKhoan(maTaiKhoan),
    thoiGianDangNhap DATETIME NOT NULL DEFAULT GETDATE(),
    trangThai BIT NOT NULL -- 1: Success, 0: Failure
);

-- Creating KhuVuc table
CREATE TABLE KhuVuc (
    maKhuVuc NVARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50) NOT NULL,
    soLuongBan INT NOT NULL,
    trangThai NVARCHAR(20) NOT NULL DEFAULT N'Hoạt động' CHECK (trangThai IN (N'Hoạt động', N'Ngừng'))
);

CREATE TABLE LoaiBan (
    maLoai NVARCHAR(10) PRIMARY KEY,
    tenLoai NVARCHAR(50) NOT NULL
);

-- Creating Ban table
CREATE TABLE Ban (
    maBan NVARCHAR(10) PRIMARY KEY,
    maKhuVuc NVARCHAR(10) NOT NULL,
    soChoNgoi INT NOT NULL,
    maLoai NVARCHAR(10) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL CHECK (trangThai IN (N'Trống', N'Đặt', N'Đang phục vụ')),
    ghiChu NVARCHAR(100),
    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc)
);

CREATE TABLE PhieuDatBan (
    maPhieu      NVARCHAR(10) PRIMARY KEY,       
    maBan        NVARCHAR(10) NOT NULL,          
    tenKhach     NVARCHAR(100) NOT NULL,         
    soDienThoai  VARCHAR(15) NULL,              
    soNguoi      INT NOT NULL,                   
    ngayDen      DATE NOT NULL,                  
    gioDen       TIME NOT NULL,                  
    ghiChu       NVARCHAR(200) NULL,             
    tienCoc      DECIMAL(18,2) DEFAULT 0,        
    ghiChuCoc    NVARCHAR(200) NULL,             
    trangThai    NVARCHAR(20) NOT NULL DEFAULT N'Đặt',
    CONSTRAINT FK_PhieuDatBan_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan)
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

CREATE TABLE LoaiKhuyenMai (
    maLoai NVARCHAR(20) PRIMARY KEY,
    tenLoai NVARCHAR(100) NOT NULL
);

-- Bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM NVARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    maLoai NVARCHAR(20) NOT NULL,
    giaTri DECIMAL(18,2),
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    trangThai NVARCHAR(50),
	doiTuongApDung NVARCHAR(50) CHECK (doiTuongApDung IN (N'Khách thường', N'Thành viên', N'Tất cả')),
    donHangTu DECIMAL(18,2),
    mon1 NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    mon2 NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    monTang NVARCHAR(20) FOREIGN KEY REFERENCES MonAn(maMon),
    ghiChu NVARCHAR(200),
	CONSTRAINT CK_NgayKhuyenMai CHECK (ngayKetThuc >= ngayBatDau)
);

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHD NVARCHAR(20) PRIMARY KEY,
	ngayLap DATETIME NOT NULL DEFAULT GETDATE(),
	trangThai NVARCHAR(50) CHECK (trangThai IN (N'Chưa thanh toán', N'Đã thanh toán', N'Đã cọc', N'Đã hủy')),
	maPhieu NVARCHAR(10) FOREIGN KEY REFERENCES PhieuDatBan(maPhieu),
    maKH NVARCHAR(20) FOREIGN KEY REFERENCES KhachHang(maKH),
    maKM NVARCHAR(20) NULL FOREIGN KEY REFERENCES KhuyenMai(maKM),
    maNhanVien NVARCHAR(20) FOREIGN KEY REFERENCES NhanVien(maNhanVien),
	phuThu DECIMAL(18,2) DEFAULT 0 CHECK (phuThu >= 0),
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

-- Bảng LoaiKhachHang
INSERT INTO LoaiKhachHang (maLoaiKH, tenLoaiKH, moTa) VALUES
('LKH01', N'Khách thường', N'Khách vãng lai'),
('LKH02', N'Thành viên', N'Khách hàng có tài khoản');

-- Bảng KhachHang
INSERT INTO KhachHang (maKH, tenKH, email, sdt, ngaySinh, maLoaiKH) VALUES
('KH0001', N'Nguyễn Văn A', 'a@gmail.com', '0912345678', '1995-05-10', 'LKH01'),
('KH0002', N'Trần Thị B', 'b@gmail.com', '0923456789', '2000-11-20', 'LKH02'),
('KH0003', N'Lê Văn C', 'c@gmail.com', '0934567890', '1988-03-15', 'LKH02');

-- Bảng NhanVien
INSERT INTO NhanVien (maNhanVien, hoTen, anhNV, ngaySinh, gioiTinh, cccd, email, sdt, trangThai, chucVu) VALUES
('NV0001', N'Bùi Ngọc Hiền', 'nv1.img', '1995-08-12', 0, '075123456789', 'hien@gmail.com', '0987654321', N'Đang làm việc', N'Quản lý'),
('NV0002', N'Nguyễn Trương An Thy', 'nv2.img', '1995-01-17', 0, '075987654321', 'thy@gmail.com', '0978123456', N'Đang làm việc', N'Lễ tân'),
('NV0003', N'Ừng Thị Thanh Trúc', 'nv3.img', '1995-05-11', 0, '075654321987', 'truc@gmail.com', '0967812345', N'Đang làm việc', N'Lễ tân'),
('NV0004', N'Nguyễn Nam Khánh', 'nv4.img', '1995-10-25', 1, '075289364950', 'khanh@gmail.com', '0345678912', N'Đang làm việc', N'Lễ tân');

-- Bảng TaiKhoan
INSERT INTO TaiKhoan (maTaiKhoan, soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES
('TK001','0987654321', 'hien123', 'NV0001', 'QuanLy'),
('TK002','0978123456', 'thy456',  'NV0002', 'LeTan'),
('TK003','0967812345', 'truc789', 'NV0003', 'LeTan'),
('TK004','0345678912', 'khanh321','NV0004', 'LeTan');

-- Bảng KhuVuc
INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, soLuongBan, trangThai) VALUES
('KV01', N'Trong nhà', 6, N'Hoạt động'),
('KV02', N'Sân thượng', 4, N'Hoạt động'),
('KV03', N'Ngoài trời', 2, N'Hoạt động');

-- Bảng LoaiBan
INSERT INTO LoaiBan (maLoai, tenLoai) VALUES
('L01', N'Thường'),
('L02', N'VIP');

-- Bảng Ban
INSERT INTO Ban(maBan, maKhuVuc, soChoNgoi, maLoai, trangThai, ghiChu) VALUES
-- KV01 - Trong nhà
('A01', 'KV01', 4, 'L01', N'Trống', NULL),
('A02', 'KV01', 4, 'L02', N'Trống', NULL),
('A03', 'KV01', 4, 'L01', N'Trống', NULL),
('A04', 'KV01', 6, 'L02', N'Trống', NULL),
('A05', 'KV01', 4, 'L01', N'Trống', NULL),
('A06', 'KV01', 6, 'L02', N'Trống', NULL),
('A07', 'KV01', 4, 'L01', N'Trống', NULL),
('A08', 'KV01', 4, 'L02', N'Trống', NULL),
('A09', 'KV01', 4, 'L01', N'Trống', NULL),
('A10', 'KV01', 6, 'L02', N'Trống', NULL),
-- KV02 - Sân thượng
('B01', 'KV02', 4, 'L01', N'Trống', NULL),
('B02', 'KV02', 4, 'L02', N'Trống', NULL),
('B03', 'KV02', 4, 'L01', N'Trống', NULL),
('B04', 'KV02', 6, 'L02', N'Trống', NULL),
('B05', 'KV02', 8, 'L02', N'Trống', N'Bàn ngoài trời'),
-- KV03 - Ngoài trời
('C01', 'KV03', 4, 'L01', N'Trống', NULL),
('C02', 'KV03', 4, 'L02', N'Trống', NULL),
('C03', 'KV03', 6, 'L02', N'Trống', NULL),
('C04', 'KV03', 8, 'L01', N'Trống', N'Bàn VIP ngoài trời');

-- Bảng LoaiMon
INSERT INTO LoaiMon (maLoai, tenLoai) VALUES
('LM0001', N'Món chính'),
('LM0002', N'Tráng miệng');

-- Bảng MonAn
INSERT INTO MonAn (maMon, tenMon, anhMon, maLoai, donGia, trangThai, moTa) VALUES
('MON0001', N'Lẩu hải sản', 'lauhaisan.img', 'LM0001', 150000, 1, NULL),
('MON0002', N'Cơm chiên Dương Châu', 'comchien.img', 'LM0001', 40000, 1, NULL),
('MON0003', N'Bánh flan', 'flan.img', 'LM0002', 15000, 1, N'Món tráng miệng phổ biến');

--Bảng LoaiKhuyenMai
INSERT INTO LoaiKhuyenMai(maLoai, tenLoai) VALUES
('L01', N'Phần trăm'),
('L02', N'Giảm trực tiếp'),
('L03', N'Tặng món');

-- Bảng KhuyenMai
INSERT INTO KhuyenMai (maKM, tenKM, maLoai, giaTri, ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, donHangTu, mon1, mon2, monTang, ghiChu) VALUES
('KM0001', N'Giảm 10%', 'L01', 10, '2024-07-10', '2024-07-20', N'Hết hạn', N'Tất cả', 2000000, NULL, NULL, NULL, NULL),
('KM0002', N'Giảm 50k', 'L02', 50000, '2025-10-27', '2025-11-15', N'Sắp áp dụng', N'Tất cả', 500000, NULL, NULL, NULL, NULL),
('KM0003', N'Mua 2 tặng 1 flan', 'L03', 0, '2025-09-27', '2025-10-15', N'Đang áp dụng', N'Tất cả', 0, 'MON0003', 'MON0003', 'MON0003', NULL);

-- Bảng HoaDon
INSERT INTO HoaDon (maHD, ngayLap, trangThai, maPhieu, maKH, maKM, maNhanVien, phuThu, ghiChu) VALUES
('HD0001', GETDATE(), N'Đã thanh toán', 'P0003', 'KH0003', 'KM0003', 'NV0002', 0, N'Áp dụng khuyến mãi tặng flan'),
('HD0002', GETDATE(), N'Chưa thanh toán', 'P0001', 'KH0001', NULL, 'NV0003', 0, N'Chưa thanh toán đủ'),
('HD0003', GETDATE(), N'Đã cọc', 'P0002', 'KH0002', 'KM0001', 'NV0004', 0, N'Đặt bàn buổi trưa, giảm 10%');

-- Bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maMon, soLuong, donGia, ghiChu) VALUES
('HD0001', 'MON0001', 2, 150000, N'Lẩu hải sản cho nhóm 6 người'),
('HD0001', 'MON0003', 3, 15000, N'Tặng thêm 1 flan'),
('HD0002', 'MON0002', 2, 40000, N'Cơm chiên Dương Châu'),
('HD0002', 'MON0003', 2, 15000, NULL),
('HD0003', 'MON0001', 1, 150000, NULL),
('HD0003', 'MON0002', 1, 40000, NULL);

ALTER TABLE KhachHang
ADD trangThai NVARCHAR(20) DEFAULT N'Hoạt động';
select * from KhachHang
select * from NhanVien
select * from Ban
select * from KhuyenMai
select * from PhieuDatBan
select * from HoaDon
select * from ChiTietHoaDon
select * from MonAn
select * from TaiKhoan
select * from LoaiKhuyenMai
use QLNH