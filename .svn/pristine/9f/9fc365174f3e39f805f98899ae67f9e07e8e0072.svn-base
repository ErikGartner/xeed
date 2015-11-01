package xeed;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import xeed.frmRestorer.IconCellRenderer;

public class frmRestorer extends javax.swing.JFrame {

    private Vector jTableModel = new Vector(0);
    private Vector jTableHeader = new Vector(0);
    private String szOrgPath = "";
    private String szPaths[];

    public frmRestorer(String szP[], String szOPath) {
        initComponents();

        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);

        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/application_error.png")));
            this.setIconImages(images);
        } catch (IOException e) {
        }

        tblSaves.setDefaultRenderer(Image.class, new IconCellRenderer());
        JustifyColumns();
        szPaths = szP;

        szOrgPath = szOPath;
        LoadList();

        tblSaves.setToolTipText("Green - The save works; Yellow - The save contains several errors; Red - The save is unreadable");
        jScrollPane1.setToolTipText("Green - The save works; Yellow - The save contains several errors; Red - The save is unreadable");

        setVisible(true);

    }

    private void JustifyColumns() {

        tblSaves.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tblSaves.getColumnModel().getColumn(0).setResizable(false);
        tblSaves.getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    private void LoadList() {

        jTableModel.clear();
        for (int x = 0; x < szPaths.length; x++) {
            File f = new File(szPaths[x]);

            if (f.exists()) {
                Vector o = new Vector(0);

                o.add(GetStatus(szPaths[x]));

                Date d = new Date(f.lastModified());
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM");

                o.add(dateFormat.format(d));
                o.add(String.valueOf(f.length()) + " Bytes");
                o.add(szPaths[x]);

                jTableModel.add(o);
            }
        }

        ((DefaultTableModel) tblSaves.getModel()).fireTableDataChanged();

    }

    private ImageIcon GetStatus(String szPath) {

        clsXDF x = new clsXDF(szPath);
        switch (x.ValidateSetting()) {

            case 0:
                return new javax.swing.ImageIcon(getClass().getResource("/accept.png"));

            case -1:
                return new javax.swing.ImageIcon(getClass().getResource("/delete.png"));

            default:
                return new javax.swing.ImageIcon(getClass().getResource("/error.png"));

        }

    }

    private void DeleteAutoSaves() {

        for (int x = 0; x < szPaths.length; x++) {
            new File(szPaths[x]).delete();
        }

    }

    class IconCellRenderer extends DefaultTableCellRenderer {

        @Override
        protected void setValue(Object value) {
            if (value instanceof ImageIcon) {

                setIcon((ImageIcon) value);
                super.setValue(null);
            } else {
                setIcon(null);
                super.setValue(value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSaves = new javax.swing.JTable();
        btnLoad = new javax.swing.JButton();
        btnContinue = new javax.swing.JButton();
        chkDelete = new javax.swing.JCheckBox();
        btnResave = new javax.swing.JButton();

        jLabel1.setText("jLabel1");
        jLabel1.setName("jLabel1"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Autosave Restorer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableHeader.add("");
        jTableHeader.add("Timestamp");
        jTableHeader.add("Size");
        jTableHeader.add("Path");
        tblSaves.setModel(new javax.swing.table.DefaultTableModel(
            jTableModel,
            jTableHeader
        ) {
            Class[] types = new Class [] {
                ImageIcon.class, String.class, String.class,String.class,String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }

        });
        tblSaves.setToolTipText("");
        tblSaves.setName("tblSaves"); // NOI18N
        tblSaves.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSaves.setShowHorizontalLines(false);
        tblSaves.setShowVerticalLines(false);
        tblSaves.getTableHeader().setReorderingAllowed(false);
        tblSaves.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSavesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSaves);

        btnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_go.png"))); // NOI18N
        btnLoad.setText("Resave & Load");
        btnLoad.setToolTipText("Resaves the selected save, then closes this form and load the save.");
        btnLoad.setName("btnLoad"); // NOI18N
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnContinue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_delete.png"))); // NOI18N
        btnContinue.setText("Skip");
        btnContinue.setToolTipText("Continue loading the original save and ignore the autosaves.");
        btnContinue.setName("btnContinue"); // NOI18N
        btnContinue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueActionPerformed(evt);
            }
        });

        chkDelete.setText("Delete unused autosaves");
        chkDelete.setToolTipText("All saves that hasn't been resaved will be deleted when this form is closed.");
        chkDelete.setName("chkDelete"); // NOI18N

        btnResave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_save.png"))); // NOI18N
        btnResave.setText("Resave");
        btnResave.setToolTipText("Resaves the selected save without closing this form and without loading it.");
        btnResave.setName("btnResave"); // NOI18N
        btnResave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .addComponent(chkDelete, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnResave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(btnContinue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLoad)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoad)
                    .addComponent(btnContinue)
                    .addComponent(btnResave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueActionPerformed

        if (chkDelete.isSelected()) {
            DeleteAutoSaves();
        }

        clsEngine.szPath = szOrgPath;
        clsEngine.LoadSetting(false, false);
        clsEngine.hwndMain.setEnabled(true);
        clsEngine.hwndMain.setVisible(true);
        dispose();

    }//GEN-LAST:event_btnContinueActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

        if (tblSaves.getSelectedRowCount() == 0) {
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Select were to resave your setting to");

        int intRet = fc.showSaveDialog(null);
        if (intRet != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "The setting wasn't resaved!", "Action cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File fdest = fc.getSelectedFile();

        if (!new File((String) tblSaves.getValueAt(tblSaves.getSelectedRow(), 3)).renameTo(fdest)) {
            JOptionPane.showMessageDialog(null, "An error occured while resaving the file!", "Action cancelled", JOptionPane.ERROR_MESSAGE);
            return;
        }


        clsEngine.szPath = fdest.getAbsolutePath();
        clsEngine.LoadSetting(false, false);
        clsEngine.hwndMain.setEnabled(true);
        clsEngine.hwndMain.setVisible(true);

        if (chkDelete.isSelected()) {
            DeleteAutoSaves();
        }

        dispose();

    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnResaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResaveActionPerformed

        if (tblSaves.getSelectedRowCount() == 0) {
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Select were to resave your setting to");

        int intRet = fc.showSaveDialog(null);
        if (intRet != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "The setting wasn't resaved!", "Action cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File fdest = fc.getSelectedFile();

        if (!new File((String) tblSaves.getValueAt(tblSaves.getSelectedRow(), 3)).renameTo(fdest)) {
            JOptionPane.showMessageDialog(null, "An error occured while resaving the file!", "Action cancelled", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tblSaves.setValueAt(fdest.getAbsolutePath(), tblSaves.getSelectedRow(), 3);

    }//GEN-LAST:event_btnResaveActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (chkDelete.isSelected()) {
            DeleteAutoSaves();
        }

        clsEngine.NewSetting(false);
        clsEngine.hwndMain.setEnabled(true);
        clsEngine.hwndMain.setVisible(true);
        dispose();

    }//GEN-LAST:event_formWindowClosing

    private void tblSavesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSavesMouseClicked
        
        if(evt.getClickCount()!=2){
            return;
        }
        
        if(evt.getButton()!=MouseEvent.BUTTON1){
            return;
        }
        
        if(tblSaves.getSelectedRow()==-1){
            return;
        }
        

        File save = new File((String) tblSaves.getValueAt(tblSaves.getSelectedRow(), 3)).getParentFile();
        try {
            Desktop.getDesktop().open(save);
        } catch (IOException ex) {
        }        
        
    }//GEN-LAST:event_tblSavesMouseClicked
    /*Derp*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnContinue;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnResave;
    private javax.swing.JCheckBox chkDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSaves;
    // End of variables declaration//GEN-END:variables
}
