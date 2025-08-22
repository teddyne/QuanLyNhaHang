create database QLNH
use QLNH
create table TaiKhoanNhanVien
( maTK nvarchar(15) not null,
  maNV nvarchar(15) null,
  SDT nvarchar(10) null,
  matKhau nvarchar(20) null
)
create table CaLamViec (
    maCa int identity(1,1) primary key,
    maNV nvarchar(15) not null,
    ngay date not null,
    gioMoCa datetime null,
    gioKetCa datetime null,
    foreign key (maNV) references TaiKhoanNhanVien(maNV)
);
