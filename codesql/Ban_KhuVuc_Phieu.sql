use QLNH
CREATE TABLE KhuVuc (
    maKhuVuc NVARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50) NOT NULL,
    soLuongBan INT NOT NULL,
    trangThai NVARCHAR(20) NOT NULL DEFAULT N'Hoạt động'
        CHECK (trangThai IN (N'Hoạt động', N'Ngừng'))
);

INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, soLuongBan, trangThai) VALUES
('KV01', N'Trong nhà', 6, N'Hoạt động'),
('KV02', N'Sân thượng', 4, N'Hoạt động'),
('KV03', N'Ngoài trời', 2, N'Hoạt động');

CREATE TABLE LoaiBan (
    maLoai NVARCHAR(10) PRIMARY KEY,
    tenLoai NVARCHAR(50) NOT NULL
);

INSERT INTO LoaiBan (maLoai, tenLoai) VALUES
('L01', N'Thường'),
('L02', N'VIP');

CREATE TABLE Ban (
    maBan NVARCHAR(10) PRIMARY KEY,
    maKhuVuc NVARCHAR(10) NOT NULL,
    soChoNgoi INT NOT NULL,
    maLoai NVARCHAR(10) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL
        CHECK (trangThai IN (N'Trống', N'Đặt', N'Đang phục vụ')),
    ghiChu NVARCHAR(100),
    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc),
    FOREIGN KEY (maLoai) REFERENCES LoaiBan(maLoai)
);
EXEC sp_helpconstraint 'Ban';
ALTER TABLE ban DROP CONSTRAINT CK__Ban__trangThai__6501FCD8;
INSERT INTO Ban (maBan, maKhuVuc, soChoNgoi, maLoai, trangThai, ghiChu) VALUES
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
('A10', 'KV01', 6, 'L02', N'Trống', NULL)

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
GO

CREATE TABLE PhieuDatBan (
    maPhieu NVARCHAR(10) PRIMARY KEY,
    maBan NVARCHAR(10) NOT NULL,
    tenKhach NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15) NULL,
    soNguoi INT NOT NULL,
    ngayDen DATE NOT NULL,
    gioDen TIME NOT NULL,
    ghiChu NVARCHAR(200) NULL,
    tienCoc DECIMAL(18,2) DEFAULT 0,
    ghiChuCoc NVARCHAR(200) NULL,
    trangThai NVARCHAR(20) NOT NULL DEFAULT N'Đặt',
    CONSTRAINT FK_PhieuDatBan_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);

SELECT 
    b.maBan,
    kv.tenKhuVuc,
    lb.tenLoai AS LoaiBan,
    b.soChoNgoi,
    b.trangThai,
    b.ghiChu
FROM Ban b
JOIN KhuVuc kv ON b.maKhuVuc = kv.maKhuVuc
JOIN LoaiBan lb ON b.maLoai = lb.maLoai;
GO