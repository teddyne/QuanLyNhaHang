use QLNH
-- Creating KhuVuc table
CREATE TABLE KhuVuc (
    maKhuVuc NVARCHAR(10) PRIMARY KEY,
    tenKhuVuc NVARCHAR(50) NOT NULL,
    soLuongBan INT NOT NULL,
    trangThai NVARCHAR(20) NOT NULL DEFAULT 'Hoạt động' CHECK (trangThai IN ('Hoạt động', 'Ngừng'))
);
ALTER TABLE KhuVuc DROP CONSTRAINT CK__KhuVuc__trangTha__662B2B3B;

INSERT INTO KhuVuc (maKhuVuc, tenKhuVuc, soLuongBan, trangThai) VALUES
('KV01', N'Trong nhà', 6, N'Hoạt động'),
('KV02', N'Sân thường', 4, N'Hoạt động'),
('KV03', N'Ngoài trời', 2, N'Hoạt động');
-- Creating Ban table
CREATE TABLE Ban (
    maBan NVARCHAR(10) PRIMARY KEY,
    maKhuVuc NVARCHAR(10) NOT NULL,
    soChoNgoi INT NOT NULL,
    loaiBan NVARCHAR(10) NOT NULL CHECK (loaiBan IN ('Thường', 'VIP')),
    trangThai NVARCHAR(20) NOT NULL CHECK (trangThai IN ('Trống', 'Đặt', 'Đang phục vụ')),
    ghiChu NVARCHAR(100),
    FOREIGN KEY (maKhuVuc) REFERENCES KhuVuc(maKhuVuc)
);

-- Thêm dữ liệu mẫu
-- Bàn Khu A (KV01)
INSERT INTO Ban(maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) VALUES
('A01', 'KV01', 4, N'Thường', N'Trống', NULL),
('A02', 'KV01', 4, N'VIP',     N'Trống', NULL),
('A03', 'KV01', 4, N'Thường', N'Trống', NULL),
('A04', 'KV01', 6, N'VIP',     N'Trống', NULL),
('A05', 'KV01', 4, N'Thường', N'Trống', NULL),
('A06', 'KV01', 6, N'VIP',     N'Trống', NULL),
('A07', 'KV01', 4, N'Thường', N'Trống', NULL),
('A08', 'KV01', 8, N'VIP',     N'Trống', N'Bàn ngoài trời');

-- Bàn Khu B (KV02)
INSERT INTO Ban(maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) VALUES
('B01', 'KV02', 4, N'Thường', N'Trống', NULL),
('B02', 'KV02', 4, N'VIP',     N'Trống', NULL),
('B03', 'KV02', 4, N'Thường', N'Trống', NULL),
('B04', 'KV02', 6, N'VIP',     N'Trống', NULL),
('B05', 'KV02', 4, N'Thường', N'Trống', NULL),
('B06', 'KV02', 6, N'VIP',     N'Trống', NULL),
('B07', 'KV02', 4, N'Thường', N'Trống', NULL),
('B08', 'KV02', 8, N'VIP',     N'Trống', N'Bàn ngoài trời');

-- Bàn Khu C (KV03)
INSERT INTO Ban(maBan, maKhuVuc, soChoNgoi, loaiBan, trangThai, ghiChu) VALUES
('C01', 'KV03', 4, N'Thường', N'Trống', NULL),
('C02', 'KV03', 4, N'VIP',     N'Trống', NULL),
('C03', 'KV03', 4, N'Thường', N'Trống', NULL),
('C04', 'KV03', 6, N'VIP',     N'Trống', NULL),
('C05', 'KV03', 4, N'Thường', N'Trống', NULL),
('C06', 'KV03', 6, N'VIP',     N'Trống', NULL),
('C07', 'KV03', 4, N'Thường', N'Trống', NULL),
('C08', 'KV03', 8, N'VIP',     N'Trống', N'Bàn ngoài trời');

EXEC sp_helpconstraint 'Ban';
-- Creating PhieuDatBan table

CREATE TABLE PhieuDatBan (
    maPhieu      NVARCHAR(10) PRIMARY KEY,       
    maBan        NVARCHAR(10) NOT NULL,          
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