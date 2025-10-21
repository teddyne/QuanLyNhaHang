-- Sử dụng cơ sở dữ liệu QLNH
USE QLNH
GO

-- Xóa các bảng nếu đã tồn tại để tránh lỗi khi chạy lại (đã cập nhật thứ tự)
IF OBJECT_ID('ChiTietHoaDon', 'U') IS NOT NULL DROP TABLE ChiTietHoaDon;
IF OBJECT_ID('HoaDon', 'U') IS NOT NULL DROP TABLE HoaDon;
IF OBJECT_ID('PhieuDatBan', 'U') IS NOT NULL DROP TABLE PhieuDatBan;
IF OBJECT_ID('KhuyenMai', 'U') IS NOT NULL DROP TABLE KhuyenMai;
IF OBJECT_ID('MonAn', 'U') IS NOT NULL DROP TABLE MonAn;
IF OBJECT_ID('LoaiMon', 'U') IS NOT NULL DROP TABLE LoaiMon;
IF OBJECT_ID('Ban', 'U') IS NOT NULL DROP TABLE Ban;
IF OBJECT_ID('KhuVuc', 'U') IS NOT NULL DROP TABLE KhuVuc;
IF OBJECT_ID('LichSuDangNhap', 'U') IS NOT NULL DROP TABLE LichSuDangNhap;
IF OBJECT_ID('TaiKhoan', 'U') IS NOT NULL DROP TABLE TaiKhoan;
IF OBJECT_ID('NhanVien', 'U') IS NOT NULL DROP TABLE NhanVien;
IF OBJECT_ID('KhachHang', 'U') IS NOT NULL DROP TABLE KhachHang;
IF OBJECT_ID('LoaiKhachHang', 'U') IS NOT NULL DROP TABLE LoaiKhachHang; -- Thêm drop bảng mới
GO

-- =================================================================
-- TẠO CẤU TRÚC BẢNG
-- =================================================================

-- Bảng LoaiKhachHang (Bảng mới)
-- Bảng này dùng để quản lý các loại khách hàng một cách linh hoạt
CREATE TABLE LoaiKhachHang (
    maLoaiKH NVARCHAR(20) PRIMARY KEY,
    tenLoaiKH NVARCHAR(100) NOT NULL ,
    moTa NVARCHAR(255) NULL -- Có thể thêm mô tả hoặc các thuộc tính khác sau này
);
GO

-- Bảng KhachHang (Đã sửa đổi)
-- Đã thay cột loaiKH bằng khóa ngoại maLoaiKH
CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
    sdt NVARCHAR(15) UNIQUE, -- Thêm UNIQUE để tránh trùng SĐT
    cccd NVARCHAR(20) UNIQUE, -- Thêm UNIQUE để tránh trùng CCCD
    maLoaiKH NVARCHAR(20) FOREIGN KEY REFERENCES LoaiKhachHang(maLoaiKH) -- Dùng khóa ngoại
);
GO

-- Bảng NhanVien
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    anhNV NVARCHAR(100),
    ngaySinh DATE,
    gioiTinh BIT, -- 0: Nữ, 1: Nam
    cccd NVARCHAR(50),
    email NVARCHAR(100),
    sdt NVARCHAR(15),
    trangThai BIT, -- 1: Đang làm, 0: Đã nghỉ
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
    trangThai BIT, -- 1: Còn, 0: Hết
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
    maNhanVien NVARCHAR(20) FOREIGN KEY REFERENCES NhanVien(maNhanVien)
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
GO

-- =================================================================
-- CHÈN DỮ LIỆU MẪU
-- =================================================================

-- 1. Chèn dữ liệu cho LoaiKhachHang trước
INSERT INTO LoaiKhachHang (maLoaiKH, tenLoaiKH, moTa) VALUES
('LKH01', N'Khách thường', N'Khách hàng vãng lai hoặc chưa đăng ký thành viên.'),
('LKH02', N'Thành viên', N'Khách hàng đã đăng ký và được hưởng ưu đãi riêng.');
GO

-- 2. Chèn dữ liệu cho KhachHang (đã cập nhật để dùng maLoaiKH)
INSERT INTO KhachHang (maKH, tenKH, email, sdt, cccd, maLoaiKH) VALUES 
('KH0001', N'Nguyễn Văn A', 'a@gmail.com', '0912345678', '045205001632', 'LKH01'), -- Khách thường
('KH0002', N'Trần Thị B', 'b@gmail.com', '0923456789', '045205001621', 'LKH02'), -- Thành viên
('KH0003', N'Lê Văn C', 'c@gmail.com', '0934567890', '045205001676', 'LKH02'); -- Thành viên
GO

-- Bảng NhanVien
INSERT INTO NhanVien (maNhanVien, hoTen, anhNV, ngaySinh, gioiTinh, cccd, email, sdt, trangThai, chucVu) VALUES 
('NV0001', N'Bùi Ngọc Hiền', 'nv1.img', '1995-08-12', 0, '075123456789', 'hien@gmail.com', '0987654321', 1, N'Quản lý'),
('NV0002', N'Nguyễn Trương An Thy', 'nv2.img', '1995-01-17', 0, '075987654321', 'thy@gmail.com', '0978123456', 1, N'Lễ tân'),
('NV0003', N'Ừng Thị Thanh Trúc', 'nv3.img', '1995-05-11', 0, '075654321987', 'truc@gmail.com', '0967812345', 1, N'Lễ tân'),
('NV0004', N'Nguyễn Nam Khánh', 'nv4.img', '1995-10-25', 1, '075289364950', 'khanh@gmail.com', '0345678912', 1, N'Lễ tân');
GO

-- Bảng TaiKhoan 
INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES
('0987654321', 'hien123', 'NV0001', 'QuanLy'),
('0978123456', 'thy456', 'NV0002', 'LeTan'),
('0967812345', 'truc789', 'NV0003', 'LeTan'),
('0345678912', 'khanh321', 'NV0004', 'LeTan');
GO

-- Bảng KhuVuc
INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, soLuongBan, trangThai) VALUES 
('KV0001', N'Trong nhà', 6, N'Hoạt động'),
('KV0002', N'Sân vườn', 4, N'Hoạt động'),
('KV0003', N'Ngoài trời', 2, N'Hoạt động');
GO

-- Bảng Ban
INSERT INTO Ban (maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) VALUES 
('B0001', 'KV0001', 4, N'Thường', N'Trống', NULL),
('B0002', 'KV0001', 6, N'VIP', N'Trống', NULL),
('B0003', 'KV0002', 8, N'Thường', N'Đang phục vụ', NULL),
('B0004', 'KV0003', 2, N'Thường', N'Trống', NULL),
('B0005', 'KV0001', 4, N'Thường', N'Đặt', NULL);
GO

-- Bảng LoaiMon
INSERT INTO LoaiMon (maLoai, tenLoai) VALUES 
('LM0001', N'Món chính'),
('LM0002', N'Tráng miệng');
GO

-- Bảng MonAn
INSERT INTO MonAn (maMon, tenMon, anhMon, maLoai, donGia, trangThai, moTa) VALUES 
('MON0001', N'Lẩu hải sản', 'lauhaisan.img', 'LM0001', 150000, 1, NULL),
('MON0002', N'Cơm chiên dương châu', 'comchien.img', 'LM0001', 40000, 1, NULL),
('MON0003', N'Bánh flan', 'flan.img', 'LM0002', 15000, 1, NULL);
GO

-- Bảng KhuyenMai
INSERT INTO KhuyenMai (maKM, tenKM, loaiKM, giaTri, ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, donHangTu, mon1, mon2, monTang, ghiChu) VALUES 
('KM0001', N'Giảm 10%', N'Phần trăm', 10, '2024-07-10', '2024-07-20', N'Hết hạn', N'Tất cả', 2000000, NULL, NULL, NULL, NULL),
('KM0002', N'Giảm 50k', N'Giảm trực tiếp', 50000, '2025-10-27', '2025-11-15', N'Sắp áp dụng', N'Tất cả', 500000, NULL, NULL, NULL, NULL),
('KM0003', N'Mua 2 tặng 1 flan', N'Tặng món', 0, '2025-09-27', '2025-10-15', N'Đang áp dụng', N'Tất cả', 0, 'MON0003', 'MON0003', 'MON0003', NULL);
GO

-- Bảng PhieuDatBan
INSERT INTO PhieuDatBan (maPhieu, maBan, maKH, soNguoi, ngayDen, gioDen, ghiChu, tienCoc, ghiChuCoc, trangThai, maNhanVien) VALUES 
('PDB001', 'B0005', 'KH0001', 4, '2025-10-20', '18:00:00', NULL, 200000, NULL, N'Đặt', 'NV0002'),
('PDB002', 'B0002', 'KH0002', 6, '2025-10-04', '19:30:00', N'Hẹn gặp mặt', 0, NULL, N'Đặt', 'NV0003'),
('PDB003', 'B0003', 'KH0003', 2, '2025-10-07', '12:00:00', NULL, 0, NULL, N'Đặt', 'NV0004'),
('PDB004', 'B0001', 'KH0002', 6, '2025-09-04', '19:30:00', NULL, 0, NULL, N'Đặt', 'NV0003'),
('PDB005', 'B0001', 'KH0001', 2, '2025-09-07', '12:00:00', NULL, 0, NULL, N'Đặt', 'NV0004');
GO

-- Bảng HoaDon
INSERT INTO HoaDon (maHD, ngayLap, trangThai, maPhieu, maKH, maKM, maNhanVien, ghiChu) VALUES 
('HD0001', '2025-09-04', N'Đã thanh toán', 'PDB004', 'KH0002', NULL, 'NV0003', NULL),
('HD0002', '2025-09-07', N'Đã thanh toán', 'PDB005', 'KH0001', NULL, 'NV0004', NULL);
GO

-- Bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maMon, soLuong, donGia, ghiChu) VALUES 
('HD0001', 'MON0001', 1, 150000, NULL),
('HD0001', 'MON0002', 2, 40000, NULL),
('HD0001', 'MON0003', 2, 15000, NULL),
('HD0002', 'MON0002', 1, 40000, NULL),
('HD0002', 'MON0003', 1, 15000, NULL);
GO

-- =================================================================
-- TRUY VẤN KIỂM TRA DỮ LIỆU
-- =================================================================
PRINT 'Da them du lieu mau thanh cong!'
SELECT * FROM LoaiKhachHang;
SELECT * FROM KhachHang;
SELECT * FROM NhanVien;
SELECT * FROM TaiKhoan;
SELECT * FROM Ban;
SELECT * FROM KhuyenMai;
SELECT * FROM PhieuDatBan;
SELECT * FROM HoaDon;
SELECT * FROM ChiTietHoaDon;
SELECT * FROM MonAn;
GO