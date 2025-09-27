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
create table Ban (
    maBan nvarchar(10) primary key,   -- mã bàn (ví dụ: b1, b2...)
    khuVuc nvarchar(50) not null,     -- khu vực (sảnh, vip, ngoài trời...)
    x int not null,                   -- tọa độ x (theo pixel)
    y int not null,                   -- tọa độ y (theo pixel)
    width int null,                   -- chiều rộng (nếu cần)
    height int null,                  -- chiều cao (nếu cần)
    soghe int null,                   -- số ghế
    tenKhach nvarchar(100) null,      -- tên khách đặt
    soDienThoai nvarchar(15) null,    -- sđt khách
    soNguoi int null,                 -- số lượng người
    ngayDat date null,                -- ngày đặt bàn
    gioDat time null,                 -- giờ đặt bàn
    ghiChu nvarchar(255) null,        -- ghi chú thêm
    trangThai nvarchar(20) not null,  -- trống / đặt / đang phục vụ
    tienCoc float null,               -- tiền cọc
    ghiChuCoc nvarchar(255) null      -- ghi chú tiền cọc
);

INSERT INTO ban (maban, khuvuc, x, y, width, height, soghe, trangthai) VALUES
('A1','A', 50, 100, 80, 80, 4, N'Trống'),
('A2','A', 150, 100, 80, 80, 4, N'Trống'),
('A3','A', 250, 100, 80, 80, 4, N'Trống'),
('A4','A', 350, 100, 80, 80, 4, N'Trống'),
('A5','A', 450, 100, 80, 80, 4, N'Trống'),
('A6','A', 550, 100, 80, 80, 4, N'Trống'),
('A7','A', 650, 100, 80, 80, 4, N'Trống'),
('A8','A', 750, 100, 80, 80, 4, N'Trống'),
('A9','A', 850, 100, 80, 80, 4, N'Trống'),
('A10','A', 950, 100, 80, 80, 4, N'Trống');

-- KHU B (10 bàn, 6 ghế mỗi bàn)
INSERT INTO ban (maban, khuvuc, x, y, width, height, soghe, trangthai) VALUES
('B1','B', 50, 250, 80, 80, 6, N'Trống'),
('B2','B', 150, 250, 80, 80, 6, N'Trống'),
('B3','B', 250, 250, 80, 80, 6, N'Trống'),
('B4','B', 350, 250, 80, 80, 6, N'Trống'),
('B5','B', 450, 250, 80, 80, 6, N'Trống'),
('B6','B', 550, 250, 80, 80, 6, N'Trống'),
('B7','B', 650, 250, 80, 80, 6, N'Trống'),
('B8','B', 750, 250, 80, 80, 6, N'Trống'),
('B9','B', 850, 250, 80, 80, 6, N'Trống'),
('B10','B', 950, 250, 80, 80, 6, N'Trống');

-- KHU C (10 bàn, 8 ghế mỗi bàn)
INSERT INTO ban (maban, khuvuc, x, y, width, height, soghe, trangthai) VALUES
('C1','C', 50, 400, 80, 80, 8, N'Trống'),
('C2','C', 150, 400, 80, 80, 8, N'Trống'),
('C3','C', 250, 400, 80, 80, 8, N'Trống'),
('C4','C', 350, 400, 80, 80, 8, N'Trống'),
('C5','C', 450, 400, 80, 80, 8, N'Trống'),
('C6','C', 550, 400, 80, 80, 8, N'Trống'),
('C7','C', 650, 400, 80, 80, 8, N'Trống'),
('C8','C', 750, 400, 80, 80, 8, N'Trống'),
('C9','C', 850, 400, 80, 80, 8, N'Trống'),
('C10','C', 950, 400, 80, 80, 8, N'Trống');
