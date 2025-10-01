use QLNH

-- Create NhanVien table
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(50) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    diaChi NVARCHAR(200),
    soDienThoai VARCHAR(10),
    ngayVaoLam DATE
);

-- Create TaiKhoan table
CREATE TABLE TaiKhoan (
    maTaiKhoan VARCHAR(100) PRIMARY KEY DEFAULT ('{' + CONVERT(VARCHAR(36), NEWID()) + '}'),
    soDienThoai VARCHAR(10) NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    maNhanVien VARCHAR(50) NOT NULL,
    phanQuyen VARCHAR(20) NOT NULL,
    CONSTRAINT chk_sdt CHECK (soDienThoai LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' AND LEN(soDienThoai) = 10),
    CONSTRAINT chk_matKhau CHECK (LEN(matKhau) >= 6),
    CONSTRAINT chk_phanQuyen CHECK (phanQuyen IN ('QuanLy', 'LeTan')),
    CONSTRAINT fk_maNhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT uk_sdt UNIQUE (soDienThoai)
);

-- Create LichSuDangNhap table
CREATE TABLE LichSuDangNhap (
    maLichSu INT IDENTITY(1,1) PRIMARY KEY,
    maTaiKhoan VARCHAR(100) NOT NULL,
    thoiGianDangNhap DATETIME NOT NULL DEFAULT GETDATE(),
    trangThai BIT NOT NULL, -- 1: Success, 0: Failure
    CONSTRAINT fk_maTaiKhoan FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
);

-- Insert sample data into NhanVien
INSERT INTO NhanVien (maNhanVien, hoTen, diaChi, soDienThoai, ngayVaoLam)
SELECT 'NV001', N'Bùi Ngọc Hiền', N'Lý thường kiệt gò vấp', '0972606925', '2025-09-28'
WHERE NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV001');

INSERT INTO NhanVien (maNhanVien, hoTen, diaChi, soDienThoai, ngayVaoLam)
SELECT 'NV002', N'Ừng Thị Thanh Trúc', N'456 Nguyễn Huệ, TP HCM', '0987654321', '2023-02-01'
WHERE NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV002');

INSERT INTO NhanVien (maNhanVien, hoTen, diaChi, soDienThoai, ngayVaoLam)
SELECT 'NV003', N'Nguyễn Trương An Thy', N'789 Lê Lợi, TP HCM', '0972606924', '2023-03-01'
WHERE NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV003');

-- Insert sample data into TaiKhoan
INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen)
SELECT '0972606925', 'Ngochien1708', 'NV001', 'QuanLy'
WHERE EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV001')
AND NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE soDienThoai = '0123456789');

INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen)
SELECT '0987654321', 'letan123', 'NV002', 'LeTan'
WHERE EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV002')
AND NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE soDienThoai = '0987654321');

INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen)
SELECT '0972606924', 'user123', 'NV003', 'LeTan'
WHERE EXISTS (SELECT 1 FROM NhanVien WHERE maNhanVien = 'NV003')
AND NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE soDienThoai = '0972606924');

INSERT INTO TaiKhoan (soDienThoai, matKhau, maNhanVien, phanQuyen) VALUES
('0972606921', 'user123', 'NV005', 'LeTan');

CREATE TABLE KhuVuc (
    maKhuVuc VARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50) NOT NULL,
    soLuongBan INT NOT NULL,
    trangThai NVARCHAR(20) NOT NULL CHECK (trangThai IN ('Hoạt động', 'Ngừng'))
);
UPDATE KhuVuc
SET trangThai = 'Hoạt động'
WHERE trangThai NOT IN ('Hoạt động', 'Ngừng');
ALTER TABLE KhuVuc DROP CONSTRAINT CK__KhuVuc__trangTha__3B40CD36;
-- Thêm dữ liệu mẫu
INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, soLuongBan, trangThai) VALUES
('KV01', N'Trong nhà', 6, N'Hoạt động'),
('KV02', N'Sân thường', 4, N'Hoạt động'),
('KV03', N'Ngoài trời', 2, N'Hoạt động');

CREATE TABLE Ban (
    maBan VARCHAR(10) PRIMARY KEY,
    maKhuVuc VARCHAR(10) NOT NULL,
    soChoNgoi INT NOT NULL,
    loaiBan NVARCHAR(10) NOT NULL CHECK (loaiBan IN ('Thường', 'VIP')),
    trangThai NVARCHAR(20) NOT NULL CHECK (trangThai IN ('Trống', 'Đặt', 'Đang phục vụ')),
    ghiChu NVARCHAR(100),
    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc)
);

-- Thêm dữ liệu mẫu
INSERT INTO Ban (maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) VALUES
('B01', 'KV01', 4, 'Thường', 'Trống', NULL),
('B02', 'KV01', 4, 'VIP', 'Trống', N'Bàn góc'),
('B03', 'KV02', 4, 'Thường', 'Đặt', NULL),
('B04', 'KV03', 8, 'VIP', 'Đang phục vụ', N'Bàn ngoài trời');
use QLNH
CREATE TABLE PhieuDatBan (
    maPhieu      NVARCHAR(10) PRIMARY KEY,       
    maBan        VARCHAR(10) NOT NULL,          
    tenKhach     NVARCHAR(100) NOT NULL,         
    soDienThoai  NVARCHAR(15) NULL,              
    soNguoi      INT NOT NULL,                   
    ngayDen      DATE NOT NULL,                  
    gioDen       TIME NOT NULL,                  
    ghiChu       NVARCHAR(200) NULL,             
    tienCoc      DECIMAL(18,2) DEFAULT 0,        
    ghiChuCoc    NVARCHAR(200) NULL,             
    trangThai    NVARCHAR(20) NOT NULL DEFAULT N'Đặt',
    CONSTRAINT FK_PhieuDatBan_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);

sp_help PhieuDatBan;
