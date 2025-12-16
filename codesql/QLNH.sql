create database QLNH

use QLNH

--Bảng LoaiKhachHang
CREATE TABLE LoaiKhachHang (
maLoaiKH NVARCHAR(20) PRIMARY KEY,
tenLoaiKH NVARCHAR(100) NOT NULL ,
moTa NVARCHAR(255) NULL
);

-- Bảng KhachHang
CREATE TABLE KhachHang (
maKH NVARCHAR(20) PRIMARY KEY,
tenKH NVARCHAR(100) NOT NULL,
email NVARCHAR(100),
sdt VARCHAR(15) UNIQUE, -- Thêm UNIQUE để tránh trùng SĐT
ngaySinh Date,
maLoaiKH NVARCHAR(20) FOREIGN KEY REFERENCES LoaiKhachHang(maLoaiKH),
trangThai NVARCHAR(20)
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
chucVu NVARCHAR(50),
trangThai BIT DEFAULT 1
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
CONSTRAINT FK_Ban_KhuVuc FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc),
CONSTRAINT FK_Ban_LoaiBan FOREIGN KEY (maLoai) REFERENCES LoaiBan(maLoai)
);

CREATE TABLE PhieuDatBan (
maPhieu NVARCHAR(10) PRIMARY KEY, 
maBan NVARCHAR(10) NOT NULL, 
tenKhach NVARCHAR(100) NOT NULL, 
soDienThoai VARCHAR(15) NULL, 
soNguoi INT NOT NULL, 
ngayDen DATE NOT NULL, 
gioDen TIME NOT NULL, 
ghiChu NVARCHAR(200) NULL, 
tienCoc DECIMAL(18,2) DEFAULT 0, 
ghiChuCoc NVARCHAR(200) NULL, 
trangThai NVARCHAR(20) NOT NULL DEFAULT N'Đặt',
CONSTRAINT FK_PhieuDatBan_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);

--Bảng loại món
CREATE TABLE LoaiMon (
maLoai NVARCHAR(20) PRIMARY KEY,
tenLoai NVARCHAR(100) NOT NULL,
trangThai BIT DEFAULT 1
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

CREATE TABLE ChiTietDatMon (
maPhieu NVARCHAR(10) NOT NULL,
maMon NVARCHAR(20) NOT NULL,
soLuong INT NOT NULL CHECK (soLuong > 0),
donGia DECIMAL(18,2) NOT NULL CHECK (donGia > 0),
ghiChu NVARCHAR(200) NULL,
CONSTRAINT PK_ChiTietDatMon PRIMARY KEY (maPhieu, maMon),
CONSTRAINT FK_ChiTietDatMon_Phieu FOREIGN KEY (maPhieu) REFERENCES PhieuDatBan(maPhieu) ON DELETE CASCADE,
CONSTRAINT FK_ChiTietDatMon_Mon FOREIGN KEY (maMon) REFERENCES MonAn(maMon)
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
donHangTu DECIMAL(18,2),
giamToiDa DECIMAL(18,2),
ngayBatDau DATE NOT NULL,
ngayKetThuc DATE NOT NULL,
trangThai NVARCHAR(50),
doiTuongApDung NVARCHAR(50) CHECK (doiTuongApDung IN (N'Khách thường', N'Thành viên', N'Tất cả')),
ghiChu NVARCHAR(200),
CONSTRAINT CK_NgayKhuyenMai CHECK (ngayKetThuc >= ngayBatDau),
CONSTRAINT FK_KM_LoaiKM FOREIGN KEY (maLoai) REFERENCES LoaiKhuyenMai(maLoai)
);

CREATE TABLE KhuyenMai_Mon (
maKM NVARCHAR(20),
maMon NVARCHAR(20),
vaiTro NVARCHAR(50) NOT NULL,
soLuong INT NOT NULL CHECK (soLuong > 0),
CONSTRAINT PK_KM_Mon PRIMARY KEY (maKM, maMon),
CONSTRAINT FK_KMMon_KM FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
CONSTRAINT FK_KMMon_Mon FOREIGN KEY (maMon) REFERENCES MonAn(maMon),
CONSTRAINT CK_VaiTro CHECK (vaiTro IN ('Dieu_kien', 'Tang'))
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
donGia DECIMAL(18,2) NOT NULL CHECK (donGia >= 0),
ghiChu NVARCHAR(200)
);

--INSERT dữ liệu
--Bảng LoaiMon
INSERT INTO LoaiMon (maLoai, tenLoai, trangThai) VALUES
('LM0001', N'Món chính', 1),
('LM0002', N'Tráng miệng', 1),
('LM0003', N'Nước uống', 1);

-- Bảng MonAn
-- MÓN CHÍNH
INSERT INTO MonAn (maMon, tenMon, anhMon, maLoai, donGia, trangThai, moTa) VALUES
('MON0001', N'Lẩu hải sản', 'img/lauhaisan.jpg', 'LM0001', 380000, 1, N'Hải sản tươi, nước đậm đà'),
('MON0002', N'Bò lúc lắc', 'img/boluclac.jpg', 'LM0001', 250000, 1, N'Bò mềm, sốt tiêu đen'),
('MON0003', N'Cơm tấm sườn bì', 'img/comtamsuon.jpg', 'LM0001', 105000, 1, N'Cơm dẻo, sườn nướng'),
('MON0004', N'Gà chiên mắm', 'img/gachienmam.jpg', 'LM0001', 185000, 1, N'Da giòn, mắm cay'),
('MON0005', N'Tôm chiên xù', 'img/tomchienxu.jpg', 'LM0001', 280000, 1, N'Tôm to, vỏ giòn'),
('MON0006', N'Cơm chiên Dương Châu', 'img/comchien.jpg', 'LM0001', 120000, 1, N'Cơm dẻo, tôm thịt thơm'),
('MON0007', N'Bún bò Huế', 'img/bunbohue.jpg', 'LM0001', 135000, 1, N'Nước cay, thịt mềm'),
('MON0008', N'Cá kho tộ', 'img/cakho.jpg', 'LM0001', 195000, 1, N'Cá mềm, nước kho đậm'),
('MON0009', N'Phở bò', 'img/phobo.jpg', 'LM0001', 125000, 1, N'Nước ngọt, thịt mềm'),
('MON0010', N'Sườn nướng mật ong', 'img/suonnuong.jpg', 'LM0001', 320000, 1, N'Sườn mềm, mật thơm'),
('MON0011', N'Bánh mì thịt nướng', 'img/banhmithit.jpg', 'LM0001', 100000, 1, N'Thịt nướng, pate béo'),
('MON0012', N'Mực xào chua ngọt', 'img/mucxao.jpg', 'LM0001', 240000, 1, N'Mực tươi, chua ngọt'),
-- TRÁNG MIỆNG 
('MON0013', N'Bánh flan', 'img/banhflan.jpg', 'LM0002', 35000, 1, N'Béo mịn, ngọt thanh'),
('MON0014', N'Chè khoai môn', 'img/chekhoaimon.jpg', 'LM0002', 45000, 1, N'Khoai bùi, nước cốt dừa'),
('MON0015', N'Bánh su kem', 'img/banhsukem.jpg', 'LM0002', 40000, 1, N'Vỏ giòn, kem béo'),
('MON0016', N'Rau câu trái cây', 'img/raucautraicay.jpg', 'LM0002', 30000, 1, N'Tươi mát, nhiều trái'),
('MON0017', N'Trà sữa pudding', 'img/trasuapudding.jpg', 'LM0002', 55000, 1, N'Pudding mềm, trà thơm'),
('MON0018', N'Sữa chua mít', 'img/suachuamit.jpg', 'LM0002', 38000, 1, N'Mít tươi, chua ngọt'),
('MON0019', N'Bánh tiramisu', 'img/tiramisu.jpg', 'LM0002', 85000, 1, N'Cà phê, phô mai béo'),
('MON0020', N'Chè thập cẩm', 'img/chethapcam.jpg', 'LM0002', 45000, 1, N'Topping đa dạng, cốt dừa'),
('MON0021', N'Kem dừa', 'img/kemdua.jpg', 'LM0002', 50000, 1, N'Mát lạnh, dừa tươi'),
('MON0022', N'Yaourt nhà làm', 'img/yaourt.jpg', 'LM0002', 35000, 1, N'Chua nhẹ, mịn màng'),
('MON0023', N'Bánh chuối nướng', 'img/banhchuoi.jpg', 'LM0002', 40000, 1, N'Chuối mềm, thơm béo'),
('MON0024', N'Chè bà ba', 'img/chebaba.jpg', 'LM0002', 45000, 1, N'Khoai, đậu, cốt dừa'),
-- NƯỚC UỐNG
('MON0025', N'Nước cam tươi', 'img/nuoccam.jpg', 'LM0003', 45000, 1, N'Cam tươi, vitamin C'),
('MON0026', N'Soda chanh bạc hà', 'img/sodachanh.jpg', 'LM0003', 50000, 1, N'Chanh tươi, bạc hà mát'),
('MON0027', N'Cà phê sữa đá', 'img/caphesuad.jpg', 'LM0003', 45000, 1, N'Đắng ngọt, đá mát'),
('MON0028', N'Trà đào cam sả', 'img/tradaocamsa.jpg', 'LM0003', 55000, 1, N'Đào tươi, sả thơm'),
('MON0029', N'Sinh tố bơ', 'img/sinhtobo.jpg', 'LM0003', 75000, 1, N'Bơ béo, mát lạnh'),
('MON0030', N'Trà sữa trân châu', 'img/trasuatranchau.jpg','LM0003', 55000, 1, N'Trân châu dai, trà thơm'),
('MON0031', N'Nước ép dưa hấu', 'img/nuocepduahau.jpg', 'LM0003', 40000, 1, N'Dưa ngọt, mát lạnh'),
('MON0032', N'Stoking lạnh', 'img/stoking.jpg', 'LM0003', 25000, 1, N'Ngọt mát, lạnh sâu'),
('MON0033', N'Sữa tươi đường đen', 'img/suatuduongden.jpg', 'LM0003', 60000, 1, N'Đường đen, sữa béo'),
('MON0034', N'Sinh tố dừa tắc', 'img/sinhtoductac.jpg', 'LM0003', 65000, 1, N'Dừa béo, tắc chua'),
('MON0035', N'Coca Cola', 'img/cocacola.jpg', 'LM0003', 25000, 1, N'Lạnh, ga mạnh'),
('MON0036', N'Nước dừa tươi', 'img/nuocdua.jpg', 'LM0003', 35000, 1, N'Dừa tươi, ngọt tự nhiên');

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
('A02', 'KV01', 4, 'L02', N'Trống', N'Bàn VIP trong nhà'),
('A03', 'KV01', 4, 'L01', N'Trống', NULL),
('A04', 'KV01', 6, 'L02', N'Trống', N'Bàn VIP trong nhà'),
('A05', 'KV01', 4, 'L01', N'Trống', NULL),
('A06', 'KV01', 6, 'L02', N'Trống', N'Bàn VIP trong nhà'),
('A07', 'KV01', 4, 'L01', N'Trống', NULL),
('A08', 'KV01', 4, 'L02', N'Trống', N'Bàn VIP trong nhà'),
('A09', 'KV01', 4, 'L01', N'Trống', NULL),
('A10', 'KV01', 6, 'L02', N'Trống', N'Bàn VIP trong nhà'),
-- KV02 - Sân thượng
('B01', 'KV02', 4, 'L01', N'Trống', NULL),
('B02', 'KV02', 4, 'L02', N'Trống', N'Bàn VIP sân thượng'),
('B03', 'KV02', 4, 'L01', N'Trống', NULL),
('B04', 'KV02', 6, 'L02', N'Trống', N'Bàn VIP sân thượng'),
('B05', 'KV02', 8, 'L02', N'Trống', N'Bàn VIP sân thượng'),
('B06', 'KV02', 4, 'L01', N'Trống', NULL),
('B07', 'KV02', 6, 'L01', N'Trống', NULL),
('B08', 'KV02', 4, 'L02', N'Trống', N'Bàn VIP sân thượng'),
('B09', 'KV02', 8, 'L02', N'Trống', N'Bàn VIP sân thượng'),
('B10', 'KV02', 4, 'L01', N'Trống', NULL),
-- KV03 - Ngoài trời
('C01', 'KV03', 4, 'L01', N'Trống', NULL),
('C02', 'KV03', 4, 'L02', N'Trống', N'Bàn VIP ngoài trời'),
('C03', 'KV03', 6, 'L02', N'Trống', N'Bàn VIP ngoài trời'),
('C04', 'KV03', 8, 'L01', N'Trống', NULL),
('C05', 'KV03', 6, 'L02', N'Trống', N'Bàn VIP ngoài trời'),
('C06', 'KV03', 4, 'L01', N'Trống', NULL),
('C07', 'KV03', 6, 'L02', N'Trống', N'Bàn VIP ngoài trời'),
('C08', 'KV03', 8, 'L01', N'Trống', NULL),
('C09', 'KV03', 4, 'L02', N'Trống', N'Bàn VIP ngoài trời'),
('C10', 'KV03', 6, 'L01', N'Trống', NULL);

-- Bảng NhanVien
INSERT INTO NhanVien (maNhanVien, hoTen, anhNV, ngaySinh, gioiTinh, cccd, email, sdt, chucVu, trangThai) VALUES
('NV0001', N'Bùi Ngọc Hiền', 'img/nv1.img', '1995-08-12', 0, '075123456789', 'hien@gmail.com', '0987654321', N'Quản lý', 1),
('NV0002', N'Nguyễn Trương An Thy', 'img/nv2.img', '1995-01-17', 0, '075987654321', 'thy@gmail.com', '0978123456', N'Lễ tân', 1),
('NV0003', N'Ừng Thị Thanh Trúc', 'img/nv3.img', '1995-05-11', 0, '075654321987', 'truc@gmail.com', '0967812345', N'Lễ tân', 1),
('NV0004', N'Nguyễn Nam Khánh', 'img/nv4.img', '1995-10-25', 1, '075289364950', 'khanh@gmail.com', '0345678912', N'Lễ tân', 1);

-- Bảng TaiKhoan
INSERT INTO TaiKhoan (maTaiKhoan, soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES
('TK001','0987654321', 'hien123', 'NV0001', 'QuanLy'),
('TK002','0978123456', 'thy456', 'NV0002', 'LeTan'),
('TK003','0967812345', 'truc789', 'NV0003', 'LeTan'),
('TK004','0345678912', 'khanh321','NV0004', 'LeTan');

-- Bảng LoaiKhachHang
INSERT INTO LoaiKhachHang (maLoaiKH, tenLoaiKH, moTa) VALUES
('LKH01', N'Khách thường', N'Khách vãng lai'),
('LKH02', N'Thành viên', N'Khách hàng có tài khoản');

--Bảng LoaiKhuyenMai
INSERT INTO LoaiKhuyenMai(maLoai, tenLoai) VALUES
('L01', N'Phần trăm'),
('L02', N'Giảm trực tiếp'),
('L03', N'Tặng món');


--Bảng KhuyenMai
INSERT INTO KhuyenMai
(maKM, tenKM, maLoai, giaTri, donHangTu, giamToiDa,
 ngayBatDau, ngayKetThuc, trangThai, doiTuongApDung, ghiChu)
VALUES
-- KM0001: Giảm 10%
('KM0001', N'Giảm 10% hóa đơn', 'L01', 10, 0, 200000,
 '2025-01-01', '2025-12-31', N'Hoạt động', N'Tất cả', N'Giảm theo %'),
-- KM0003: Tặng sữa chua mít
('KM0003', N'Tặng sữa chua mít', 'L03', NULL, 0, NULL,
 '2025-01-01', '2025-12-31', N'Hoạt động', N'Tất cả', N'Mua món chính tặng tráng miệng'),
-- KM0004: Thành viên giảm 10%
('KM0004', N'Ưu đãi thành viên', 'L01', 10, 0, 150000,
 '2025-01-01', '2025-12-31', N'Hoạt động', N'Thành viên', N'Chỉ áp dụng thành viên'),
-- KM0005: Combo gà + phở tặng Coca
('KM0005', N'Combo gà + phở', 'L03', NULL, 0, NULL,
 '2025-01-01', '2025-12-31', N'Hoạt động', N'Tất cả', N'Mua combo tặng Coca');

--Bảng KhuyenMai_Mon
INSERT INTO KhuyenMai_Mon (maKM, maMon, vaiTro, soLuong)
VALUES
('KM0003', 'MON0001', 'Dieu_kien', 1), -- Lẩu hải sản
('KM0003', 'MON0018', 'Tang', 1),      -- Tặng sữa chua mít
('KM0005', 'MON0004', 'Dieu_kien', 1), -- Gà chiên mắm
('KM0005', 'MON0009', 'Dieu_kien', 1), -- Phở bò
('KM0005', 'MON0035', 'Tang', 1);      -- Tặng Coca

INSERT INTO KhachHang (maKH, tenKH, email, sdt, ngaySinh, maLoaiKH, trangThai) VALUES
('KH0001', N'Nguyễn Văn Nam', 'nam@gmail.com', '0905123456', '1994-03-12', 'LKH01', N'Hoạt động'),
('KH0002', N'Lê Thị Hồng', 'hong@gmail.com', '0916345678', '1996-06-20', 'LKH02', N'Hoạt động'),
('KH0003', N'Trần Anh Dũng', 'dung@gmail.com', '0927456789', '1990-11-15', 'LKH01', N'Hoạt động'),
('KH0004', N'Phạm Quỳnh Hoa', 'hoa@gmail.com', '0938567890', '1998-08-09', 'LKH02', N'Hoạt động'),
('KH0005', N'Đỗ Minh Tú', 'tu@gmail.com', '0949678901', '1992-02-22', 'LKH01', N'Hoạt động'),
('KH0006', N'Nguyễn Hải Yến', 'yen@gmail.com', '0950789012', '1997-05-30', 'LKH02', N'Hoạt động');

INSERT INTO PhieuDatBan (maPhieu, maBan, tenKhach, soDienThoai, soNguoi, ngayDen, gioDen, ghiChu, tienCoc, ghiChuCoc, trangThai) VALUES
('P0001', 'A02', N'Nguyễn Văn Nam', '0905123456', 4, '2024-01-15', '18:00', N'Đặt bàn sinh nhật', 500000, N'Cọc 50%', N'Đã cọc'),
('P0002', 'A04', N'Lê Thị Hồng', '0916345678', 2, '2024-04-20', '19:00', N'Hẹn hò', 300000, NULL, N'Đã cọc'),
('P0003', 'B05', N'Trần Anh Dũng', '0927456789', 6, '2024-09-10', '18:30', N'Đặt tiệc công ty', 800000, N'Cọc 40%', N'Đã cọc'),
('P0004', 'B02', N'Phạm Quỳnh Hoa', '0938567890', 3, '2025-02-05', '17:30', N'Đặt bàn view đẹp', 400000, NULL, N'Đã cọc'),
('P0005', 'C02', N'Đỗ Minh Tú', '0949678901', 5, '2025-07-18', '19:00', N'Tiệc bạn bè', 600000, NULL, N'Đã cọc'),
('P0006', 'A10', N'Nguyễn Hải Yến', '0950789012', 8, '2025-10-10', '18:45', N'Sinh nhật', 1000000, N'Cọc đủ', N'Đã cọc');

INSERT INTO HoaDon (maHD, ngayLap, trangThai, maPhieu, maKH, maKM, maNhanVien, phuThu, ghiChu) VALUES
('HD0001', '2025-10-27 19:00', N'Đã thanh toán', 'P0001', 'KH0001', 'KM0003', 'NV0002', 0, N'Mua lẩu hải sản, tặng sữa chua mít'),
('HD0002', '2025-10-27 19:30', N'Đã thanh toán', 'P0002', 'KH0002', 'KM0001', 'NV0003', 0, N'Giảm 10% tổng hóa đơn'),
('HD0003', '2025-10-28 12:30', N'Đã thanh toán', 'P0003', 'KH0003', 'KM0005', 'NV0004', 0, N'Mua gà chiên mắm + phở bò, tặng Coca'),
('HD0004', '2025-10-28 13:00', N'Đã thanh toán', 'P0004', 'KH0001', NULL, 'NV0001', 0, N'Không áp dụng khuyến mãi'),
('HD0005', '2025-10-29 18:30', N'Đã thanh toán', 'P0005', 'KH0002', 'KM0004', 'NV0003', 0, N'Thành viên giảm 10%');

INSERT INTO ChiTietHoaDon VALUES
('HD0001', 'MON0001', 1, 380000, N'Lẩu hải sản'),
('HD0001', 'MON0005', 1, 280000, N'Tôm chiên xù'),
('HD0001', 'MON0018', 1, 0, N'Món tặng theo KM0003'),
('HD0002', 'MON0002', 2, 250000, N'Bò lúc lắc'),
('HD0002', 'MON0006', 1, 120000, N'Cơm chiên Dương Châu'),
('HD0003', 'MON0004', 1, 185000, N'Gà chiên mắm'),
('HD0003', 'MON0009', 1, 125000, N'Phở bò'),
('HD0003', 'MON0035', 1, 0, N'Món tặng theo KM0005'),
('HD0004', 'MON0007', 2, 135000, N'Bún bò Huế'),
('HD0004', 'MON0016', 2, 30000, N'Rau câu trái cây'),
('HD0004', 'MON0027', 1, 45000, N'Cà phê sữa đá'),
('HD0005', 'MON0001', 1, 380000, N'Lẩu hải sản'),
('HD0005', 'MON0003', 1, 105000, N'Cơm tấm sườn bì'),
('HD0005', 'MON0013', 1, 35000, N'Bánh flan tráng miệng');

select * from KhachHang
select * from NhanVien
select * from Ban
select * from KhuyenMai
select * from PhieuDatBan
select * from HoaDon
select * from ChiTietHoaDon
select * from MonAn
select * from LoaiMon
select * from TaiKhoan
select * from LoaiKhuyenMai
select * from LoaiKhachHang

ALTER TABLE Ban DROP CONSTRAINT CK__Ban__trangThai__534D60F1;

SELECT * 
FROM sys.check_constraints 
WHERE parent_object_id = OBJECT_ID('Ban');
SELECT name 
FROM sys.check_constraints 
WHERE parent_object_id = OBJECT_ID('Ban');