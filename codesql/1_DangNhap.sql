use QLNH
create table TaiKhoanNhanVien (
    maTK nvarchar(15) not null,
    maNV nvarchar(15) not null primary key,
    SDT nvarchar(10) null,
    matKhau nvarchar(20) null
);
create table CaLamViec (
    maCa int identity(1,1) primary key,
    maNV nvarchar(15) not null,
    ngay date not null,
    gioMoCa datetime null,
    gioKetCa datetime null,
    foreign key (maNV) references TaiKhoanNhanVien(maNV)
);
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
