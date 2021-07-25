/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Penjualan;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wanda
 */
public class Transaksi extends javax.swing.JFrame {

    String Tanggal;
    private DefaultTableModel model;
    
    public void totalBiaya(){
        int jumlahBaris = tableTransaksi.getRowCount();
        int totalBiaya = 0;
        int jumlahBarang, hargaBarang;
        for (int i = 0; i < jumlahBaris; i++) {
            jumlahBarang = Integer.parseInt(tableTransaksi.getValueAt(i, 4).toString());
            hargaBarang = Integer.parseInt(tableTransaksi.getValueAt(i, 3).toString());
            totalBiaya = totalBiaya + (jumlahBarang * hargaBarang);
        }
        txTotal.setText(String.valueOf(totalBiaya));
        txTampil.setText("Rp "+ totalBiaya +",00");
    }
    
    /**
     * Creates new form Transaksi
     */
    public Transaksi() {
        initComponents();

        tampilpilih();
        tampiltabel();
        setLocationRelativeTo(null);

        utama();
        Date date = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        
        txTanggal.setText(s.format(date));
        txTotal.setText("");
        txBayar.setText("");
        txKembalian.setText("");
        txId.requestFocus();
    }

    public void tampiltabel(){
        DefaultTableModel tb = new DefaultTableModel();
        tb.addColumn("ID");
        tb.addColumn("Nama Barang");
        tb.addColumn("Kode Barang");
        tb.addColumn("Harga Satuan");
        tb.addColumn("Jumlah Beli");
        tb.addColumn("Total");
        tb.addColumn("Tanggal");
        tableTransaksi.setModel(tb);
        try{
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM transaksi";
            ResultSet rs = s.executeQuery(sql);
            
            while(rs.next()){
                tb.addRow(new Object[]{
                    rs.getString("id_transaksi"),
                    rs.getString("nama_barang"), 
                    rs.getString("kode_barang"),
                    rs.getString("harga_satuan"),
                    rs.getString("jumlah_beli"),
                    rs.getString("total"),
                    rs.getString("tanggal"),  
                });
                
            }
            
        }catch(Exception e){
            System.out.println("transaksi error");
        }
    }
    
    public void FilterHuruf(KeyEvent a){
       if(Character.isDigit(a.getKeyChar())){
           a.consume();
           JOptionPane.showMessageDialog(null, "masukan huruf saja!", "peringatan", JOptionPane.WARNING_MESSAGE);
       }
   }
    public void FilterAngka(KeyEvent a){
       if(Character.isAlphabetic(a.getKeyChar())){
           a.consume();
           JOptionPane.showMessageDialog(null, "masukan angka saja!", "peringatan", JOptionPane.WARNING_MESSAGE);
       }
   }
    private void autonumber(){
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM transaksi ORDER BY id_transaksi DESC";
            ResultSet r = s.executeQuery(sql);
            if (r.next()) {
                String id_transaksi = r.getString("id_transaksi").substring(2);
                String id = "" +(Integer.parseInt(id_transaksi)+1);
                String Nol = "";
                
                if(id.length()==1)
                {Nol = "00";}
                else if(id.length()==2)
                {Nol = "0";}
                else if(id.length()==3)
                {Nol = "";}
                txId.setText("T" + Nol + id);
            } else {
                txId.setText("T001");
            }
            r.close();
            s.close();
        } catch (Exception e) {
            System.out.println("autonumber error");
        }
    }
    
    public void loadData(){
        DefaultTableModel model = (DefaultTableModel) tableTransaksi.getModel();
        model.addRow(new Object[]{
            txId.getText(),
            namaBarang.getSelectedItem(),
            txKode.getText(),
            txHarga.getText(),
            txJumlah.getText(),
            txTotal.getText(),
            txTanggal.getText()
        });
    }
    
    public void kosong(){
        DefaultTableModel model = (DefaultTableModel) tableTransaksi.getModel();
        
        while (model.getRowCount()>0) {
            model.removeRow(0);
        }
    }
    
    public void utama(){
        autonumber();
        namaBarang.setSelectedItem("-pilih-");
        txKode.setText("");
        txHarga.setText("");
        txJumlah.setText("");
        
    }
    
    public void clear(){
        namaBarang.setSelectedItem("-pilih-");
        txKode.setText("");
        txHarga.setText("");
        txJumlah.setText("");
        txTotal.setText("");
        txBayar.setText("");
        txKembalian.setText("");
        txTampil.setText("");
    }
    
    public void tambahTransaksi(){
        int jumlah, harga, total;
        
        jumlah = Integer.valueOf(txJumlah.getText());
        harga = Integer.valueOf(txHarga.getText());
        total = jumlah * harga;
        
        txTotal.setText(String.valueOf(total));
        
        loadData();
        totalBiaya();
        clear();
        txId.requestFocus();
    }

    private void tampilpilih() {
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();

            String sql = "SELECT nama_barang FROM barang WHERE jumlah_barang !='0'";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                namaBarang.addItem(r.getString("nama_barang"));
            }

            r.last();
            int jumlahdata = r.getRow();
            r.first();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    public void showMessage(String message, int type) {
        if (type == 1) {
            JOptionPane.showMessageDialog(this, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
     }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        namaBarang = new javax.swing.JComboBox<>();
        txKode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txHarga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txJumlah = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txTanggal = new javax.swing.JTextField();
        txTampil = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txBayar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txKembalian = new javax.swing.JTextField();
        kembalianBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txTotal = new javax.swing.JTextField();
        totalbtn = new javax.swing.JButton();
        tambah = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        simpanBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransaksi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel8.setFont(new java.awt.Font("Tekton Pro", 0, 36)); // NOI18N
        jLabel8.setText("Transaksi");

        back.setText("Back");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(back)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(back))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 204, 255));

        jLabel1.setText("ID Transaksi");

        txId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txIdActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama Barang");

        jLabel3.setText("Kode Barang");

        namaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-pilih-" }));
        namaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaBarangActionPerformed(evt);
            }
        });

        jLabel4.setText("Harga Satuan");

        jLabel5.setText("Jumlah Beli");

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Rp.");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tanggal");

        txTampil.setBackground(new java.awt.Color(255, 204, 204));
        txTampil.setFont(new java.awt.Font("Impact", 0, 48)); // NOI18N
        txTampil.setForeground(new java.awt.Color(255, 255, 255));
        txTampil.setText("0");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Bayar");

        txBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBayarActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Kembalian");

        kembalianBtn.setText("Hitung");
        kembalianBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembalianBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txTampil)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(kembalianBtn))
                            .addComponent(txTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 35, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txTampil, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembalianBtn))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel7.setText("Total");

        totalbtn.setText("Hitung");
        totalbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalbtnActionPerformed(evt);
            }
        });

        tambah.setText("Tambah");
        tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahActionPerformed(evt);
            }
        });

        hapus.setText("Hapus");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        simpanBtn.setText("Simpan");
        simpanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanBtnActionPerformed(evt);
            }
        });

        tableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableTransaksi);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namaBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txKode)
                                    .addComponent(txId)
                                    .addComponent(txTotal)
                                    .addComponent(txJumlah)
                                    .addComponent(txHarga)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(simpanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(totalbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(27, 27, 27))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalbtn)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambah)
                    .addComponent(hapus)
                    .addComponent(simpanBtn))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void totalbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalbtnActionPerformed
        // TODO add your handling code here:
        int jumlah, harga, total;
       
        jumlah = Integer.parseInt(txJumlah.getText().toString());
        harga = Integer.parseInt(txHarga.getText().toString());
        total = jumlah * harga;
       
       
        txTotal.setText(Integer.toString(total));
        txTampil.setText(Integer.toString(total));
        
    }//GEN-LAST:event_totalbtnActionPerformed

    private void namaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaBarangActionPerformed
        // TODO add your handling code here:
         if (namaBarang.getSelectedItem().equals("pilih nama_barang")){
            txKode.setText("");
            txHarga.setText("");
        }else{
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();

            String sql = "SELECT kode_barang FROM barang WHERE nama_barang ='" + namaBarang.getSelectedItem() + "'";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                txKode.setText(r.getString("kode_barang"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();

            String sql = "SELECT harga_jual FROM barang WHERE nama_barang ='" + namaBarang.getSelectedItem() + "'";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                txHarga.setText(r.getString("harga_jual"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        }
    }//GEN-LAST:event_namaBarangActionPerformed

    private void txBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBayarActionPerformed

    private void tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahActionPerformed
        // TODO add your handling code here:
        tambahTransaksi();
//        tampilpilih();
    }//GEN-LAST:event_tambahActionPerformed

    private void kembalianBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembalianBtnActionPerformed
        // TODO add your handling code here:
        int total, bayar, kembalian;
        
        total = Integer.valueOf(txTotal.getText());
        bayar = Integer.valueOf(txBayar.getText());
        
        if (total > bayar) {
            JOptionPane.showMessageDialog(null, "Uang tidak cukup untuk melakukan pembayaran");
        } else {
            kembalian = bayar - total;
            txKembalian.setText(String.valueOf(kembalian));
        }
    }//GEN-LAST:event_kembalianBtnActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        // TODO add your handling code here:
        try {
            String sql ="delete from transaksi where id_transaksi ='"+txId.getText()+"'";
            java.sql.Connection conn=(Connection)koneksi.getKoneksi();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        txId.setText("");
        namaBarang.setSelectedItem("");
        txKode.setText("");
        txHarga.setText("");
        txJumlah.setText("");
        txTotal.setText("");
        txTanggal.setText("");
//        int selectedRow = tableTransaksi.getSelectedRow();
//        if (selectedRow == -1) {
//            showMessage("Pilih Baris Dulu!", 2);
//        }else {
//            int option = JOptionPane.showConfirmDialog(this, 
//                    "Apakah Anda Yakin Ingin Menghapus Data Ini ?", "HAPUS DATA", 
//                    JOptionPane.WARNING_MESSAGE);
//            if (option == JOptionPane.YES_OPTION) {
//                try{
//                
//                Connection c = koneksi.getKoneksi();
//                Statement s = c.createStatement();
//                
//                  s.executeUpdate("DELETE FROM transaksi WHERE id_transaksi = " + txId.getText());
//                  loadData();
//                  
//                } catch (SQLException e) {
//               
//                    System.out.println("Error" + e.getMessage());
//                    
//                }
//            }
//        }
    }//GEN-LAST:event_hapusActionPerformed

    private void simpanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanBtnActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) tableTransaksi.getModel();
        
        String id = txId.getText();
        String barang = (String) namaBarang.getSelectedItem();
        String kode = txKode.getText();
        String harga = txHarga.getText();
        String jumlah = txJumlah.getText();
        String total = txTotal.getText();
        String tanggal = txTanggal.getText();
        
        try {
            Connection c = koneksi.getKoneksi();
            String sql = "INSERT INTO transaksi VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, id);
            p.setString(2, barang);
            p.setString(3, kode);
            p.setString(4, harga);
            p.setString(5, jumlah);
            p.setString(6, total);
            p.setString(7, tanggal);
            p.executeUpdate();
            p.close();
        } catch (Exception e) {
            System.out.println("simpan transaksi error");
        }
        
        try {
            Connection c = koneksi.getKoneksi();
            int baris = tableTransaksi.getRowCount();
            
            for (int i = 0; i < baris; i++) {
                String sql = "INSERT INTO transaksi(id_transaksi, nama_barang, kode_barang, harga_satuan, jumlah_beli, total, tanggal) VALUES('"
                        + tableTransaksi.getValueAt(i, 0) +"','"+ tableTransaksi.getValueAt(i, 1) +"','"+ tableTransaksi.getValueAt(i, 2) 
                        +"','"+ tableTransaksi.getValueAt(i, 3) +"','"+ tableTransaksi.getValueAt(i, 4) +"','"+ tableTransaksi.getValueAt(i, 5)  
                        +"','"+ tableTransaksi.getValueAt(i, 6) +"')";
                PreparedStatement p = c.prepareStatement(sql);
                p.executeUpdate();
                p.close();
            }
        } catch (Exception e) {
            System.out.println("simpan transaksi error");
        }
        clear();
        utama();
        autonumber();
        loadData();
       
        txTampil.setText("Rp. 0");
//        String id = txId.getText();
//        String barang = (String) namaBarang.getSelectedItem();
//        String kode = txKode.getText();
//        String harga = txHarga.getText();
//        String jumlah = txJumlah.getText();
//        String total = txTotal.getText();
//        String tanggal = txTanggal.getText();
//
//        if (id.equals("") || barang.equals("") || harga.equals("") || jumlah.equals("") || total.equals("") || tanggal.equals("")){
//            JOptionPane.showMessageDialog(null, "Isi Data Dengan Lengkap!");
//        }else{
//            try{
//                Connection c = koneksi.getKoneksi();
//                String sql = "INSERT INTO transaksi VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//                PreparedStatement p = c.prepareStatement(sql);
//                p.setString(1, id);
//                p.setString(2, barang);
//                p.setString(3, kode);
//                p.setString(4, harga);
//                p.setString(5, jumlah);
//                p.setString(6, total);
//                p.setString(7, tanggal);
//                p.executeUpdate();
//                p.close();
//                JOptionPane.showMessageDialog(null, "Data Ditambahkan");
//            } catch (SQLException ex) {
//                System.out.println("Error");
//            }
//            clear();
//            utama();
//            loadData();
//        }
    }//GEN-LAST:event_simpanBtnActionPerformed

    private void txIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txIdActionPerformed
        // TODO add your handling code here:
        autonumber();
    }//GEN-LAST:event_txIdActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kembalianBtn;
    private javax.swing.JComboBox<String> namaBarang;
    private javax.swing.JButton simpanBtn;
    private javax.swing.JTable tableTransaksi;
    private javax.swing.JButton tambah;
    private javax.swing.JButton totalbtn;
    private javax.swing.JTextField txBayar;
    private javax.swing.JTextField txHarga;
    private javax.swing.JTextField txId;
    private javax.swing.JTextField txJumlah;
    private javax.swing.JTextField txKembalian;
    private javax.swing.JTextField txKode;
    private javax.swing.JTextField txTampil;
    private javax.swing.JTextField txTanggal;
    private javax.swing.JTextField txTotal;
    // End of variables declaration//GEN-END:variables
}
