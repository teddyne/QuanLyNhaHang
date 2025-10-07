package gui;

import dao.KhuVuc_DAO;
import entity.KhuVuc;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmKhuVuc extends JDialog implements MouseListener{
    private KhuVuc_DAO khuDAO;
    private DefaultTableModel model;
    private JTextField txtMa, txtTen, txtSoLuong;
    private JComboBox<String> cbTrangThai;
    private Consumer<Void> refreshCallback;

    public FrmKhuVuc(JFrame parent, KhuVuc_DAO khuDAO, Consumer<Void> refreshCallback) {
        super(parent, "Quản lý khu vực", true);
        this.khuDAO = khuDAO;
        this.refreshCallback = refreshCallback;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(800, 550);
        setLocationRelativeTo(getParent());
        setLayout(null);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHU VỰC", SwingConstants.CENTER);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(new Color(128, 0, 0)); // Đỏ rượu
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        lblTieuDe.setBounds(0, 0, 800, 60);
        add(lblTieuDe);

        // Panel thông tin khu vực
        JPanel pForm = new JPanel(null);
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin khu vực"));
        pForm.setBounds(10, 70, 780, 210);
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        JLabel lblMa = new JLabel("Mã khu vực:");
        lblMa.setFont(labelFont);
        lblMa.setBounds(20, 40, 200, 25);
        txtMa = new JTextField();
        txtMa.setBounds(180, 30, 300, 35);

        JLabel lblTen = new JLabel("Tên khu vực:");
        lblTen.setFont(labelFont);
        lblTen.setBounds(20, 85, 200, 25);
        txtTen = new JTextField();
        txtTen.setBounds(180, 75, 300, 35);

        JLabel lblSoLuong = new JLabel("Số lượng bàn:");
        lblSoLuong.setFont(labelFont);
        lblSoLuong.setBounds(20, 130, 200, 25);
        txtSoLuong = new JTextField();
        txtSoLuong.setBounds(180, 120, 300, 35);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setBounds(20, 175, 200, 25);
        cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng"});
        cbTrangThai.setBounds(180, 165, 300, 35);

        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(550, 30, 120, 35);
        btnThem.setBackground(new Color(46, 204, 113)); // Xanh lá
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBounds(550, 75, 120, 35);
        btnXoa.setBackground(new Color(230, 126, 34)); // Cam
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSua = new JButton("Sửa");
        btnSua.setBounds(550, 120, 120, 35);
        btnSua.setBackground(new Color(192, 57, 43)); // Đỏ
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBounds(550, 165, 120, 35);
        btnLuu.setBackground(new Color(52, 152, 219)); // Xanh dương
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 18));

        pForm.add(lblMa);
        pForm.add(txtMa);
        pForm.add(lblTen);
        pForm.add(txtTen);
        pForm.add(lblSoLuong);
        pForm.add(txtSoLuong);
        pForm.add(lblTrangThai);
        pForm.add(cbTrangThai);
        pForm.add(btnThem);
        pForm.add(btnXoa);
        pForm.add(btnSua);
        pForm.add(btnLuu);
        add(pForm);

     // Bảng danh sách khu vực
        model = new DefaultTableModel(new Object[]{"Mã khu vực", "Tên khu vực", "Số lượng bàn", "Trạng thái"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 280, 780, 230);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khu vực"));
        add(scroll);

        // Thêm mouse click vào bảng
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText((String) model.getValueAt(row, 0));
                    txtTen.setText((String) model.getValueAt(row, 1));
                    txtSoLuong.setText(model.getValueAt(row, 2).toString());
                    cbTrangThai.setSelectedItem(model.getValueAt(row, 3));
                    txtMa.setEditable(false);
                }
            }
        });


        // Sự kiện nút
        btnThem.addActionListener(e -> themKhuVuc());
        btnXoa.addActionListener(e -> xoaKhuVuc(table));
        btnSua.addActionListener(e -> suaKhuVuc(table));
        btnLuu.addActionListener(e -> luuKhuVuc(table));
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<KhuVuc> list = khuDAO.getAll();
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có khu vực nào trong cơ sở dữ liệu!");
            }
            for (KhuVuc k : list) {
                model.addRow(new Object[]{
                    k.getMaKhuVuc(),
                    k.getTenKhuVuc(),
                    k.getSoLuongBan(),
                    k.getTrangThai()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void themKhuVuc() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty() || soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số không âm!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số hợp lệ!");
            return;
        }

        try {
            if (khuDAO.getByMa(ma) != null) {
                JOptionPane.showMessageDialog(this, "Mã khu vực đã tồn tại!");
                return;
            }
            KhuVuc k = new KhuVuc(ma, ten, soLuong, trangThai);
            khuDAO.add(k);
            model.addRow(new Object[]{ma, ten, soLuong, trangThai});
            refreshCallback.accept(null);
            JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
            txtMa.setText("");
            txtTen.setText("");
            txtSoLuong.setText("");
            cbTrangThai.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khu vực: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xoaKhuVuc(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String ma = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa khu vực " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    khuDAO.delete(ma);
                    model.removeRow(row);
                    refreshCallback.accept(null);
                    JOptionPane.showMessageDialog(this, "Xóa khu vực thành công!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa khu vực: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để xóa!");
        }
    }

    private void suaKhuVuc(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMa.setText((String) model.getValueAt(row, 0));
            txtMa.setEditable(false); // Không cho sửa mã
            txtTen.setText((String) model.getValueAt(row, 1));
            txtSoLuong.setText(model.getValueAt(row, 2).toString());
            cbTrangThai.setSelectedItem(model.getValueAt(row, 3));
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để sửa!");
        }
    }

    private void luuKhuVuc(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String ma = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            String soLuongStr = txtSoLuong.getText().trim();
            String trangThai = (String) cbTrangThai.getSelectedItem();

            if (ma.isEmpty() || ten.isEmpty() || soLuongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            int soLuong;
            try {
                soLuong = Integer.parseInt(soLuongStr);
                if (soLuong < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số không âm!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số hợp lệ!");
                return;
            }

            try {
                KhuVuc k = khuDAO.getByMa(ma);
                if (k == null) {
                    JOptionPane.showMessageDialog(this, "Khu vực không tồn tại!");
                    return;
                }
                k.setTenKhuVuc(ten);
                k.setSoLuongBan(soLuong);
                k.setTrangThai(trangThai);
                khuDAO.update(k);
                model.setValueAt(ten, row, 1);
                model.setValueAt(soLuong, row, 2);
                model.setValueAt(trangThai, row, 3);
                refreshCallback.accept(null);
                JOptionPane.showMessageDialog(this, "Cập nhật khu vực thành công!");
                txtMa.setEditable(true);
                txtMa.setText("");
                txtTen.setText("");
                txtSoLuong.setText("");
                cbTrangThai.setSelectedIndex(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật khu vực: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để lưu!");
        }
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}