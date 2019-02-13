/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import forms.TemplateCreatorForm.TreeItem;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Erik
 */
public class TemplateItemPalettForm extends javax.swing.JFrame {

    DefaultMutableTreeNode editingNode;
    TreeItem editingItem;
    TemplateCreatorForm form;
    boolean DiscardInsteadOfDeleting;

    public TemplateItemPalettForm(TemplateCreatorForm f, DefaultMutableTreeNode e, boolean discard) {

        form = f;
        editingNode = e;
        editingItem = (TreeItem) e.getUserObject();
        DiscardInsteadOfDeleting = discard;

        initComponents();
        LoadList();
        txtName.requestFocus();

        SetComboToItem(editingItem.intType);
        txtName.setText(editingItem.szName);
        txtData.setText(editingItem.szData);

        if (DiscardInsteadOfDeleting) {
            btnCancel.setText("Discard");
        }

        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/brick.png")));
            this.setIconImages(images);
        } catch (IOException ex) {
        }

    }

    private void SetComboToItem(int Type) {

        if (Type == -1) {
            return;
        }

        for (int x = 0; x < comboType.getItemCount(); x++) {
            ComboItem c = (ComboItem) comboType.getItemAt(x);
            if (c.Type == Type) {
                comboType.setSelectedIndex(x);
            }
        }
    }

    public void LoadList() {

        comboType.removeAllItems();
        ComboItem c;

        c = new ComboItem();
        c.Type = 7;
        c.szName = "Checkbox";
        c.szHelp = "A switch that can be true or false.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 13;
        c.szName = "Extended sheet row";
        c.szHelp = "A link to an extended sheet. Ie a representation of a character sheet. \nThe user can add properties manually and you can define a template. \nUse the data textbox below to define the properties, one per line.";
        c.use_data = true;
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 10;
        c.szName = "Image";
        c.szHelp = "The item allows the user to store and display an image on the sheet.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 14;
        c.szName = "List box";
        c.szHelp = "A list of options. Multiple options may be selected. \nUse the data textbox below to define the options, one per line. \nDo not add duplicate options or empty rows.";
        c.use_data = true;
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 3;
        c.szName = "List row";
        c.szHelp = "A list of options where only one can be selected. The user can input a custom option aswell. \nUse the data textbox below to define the options, one per line. \nDo not add duplicate options or empty rows. XEED will add an empty row automatically.";
        c.use_data = true;
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 6;
        c.szName = "Name row";
        c.szHelp = "A special text row that defines the main identifing string i.e the name of the character.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 4;
        c.szName = "Number row";
        c.szHelp = "A single text row that supports postive and negative numbers (with decimals).";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 12;
        c.szName = "Offspring hierarchy";
        c.szHelp = "A special item that display the characters children in a hierarchy. \nIs located on the right column.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 11;
        c.szName = "Parent row";
        c.szHelp = "A special list row that allows the user to select another character as parent, multiple parent rows are allowed.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 2;
        c.szName = "Text box";
        c.szHelp = "A text box that support multiple rows of text.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 8;
        c.szName = "Text box w/o label";
        c.szHelp = "A text box without a label. The name is shown if hoovered above. \nRecommended for large texts in it's own section (with a fitting section name).";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 1;
        c.szName = "Text row";
        c.szHelp = "A text row that supports a single row of text.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 9;
        c.szName = "Title";
        c.szHelp = "A single row with a centered title. \nDo not add too many of these, instead rely on the sections and the built in labels of the other items. \nIt leads to cluttered design, instead rely on the labels of the items and grouping with sections.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 15;
        c.szName = "URL row";
        c.szHelp = "A single text row that may contain 1 URL/URI. Next to it is a go button that executes the link.\nCan be used for http-addresses or others such as Spotify-URIs.";
        comboType.addItem(c);

        c = new ComboItem();
        c.Type = 5;
        c.szName = "Year row";
        c.szHelp = "A single text row that supports postive integers, it also includes an switch for AD/BC. \nIf the switch is unwanted, use number row.";
        comboType.addItem(c);

    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel3 = new javax.swing.JPanel();
      btnOK = new javax.swing.JButton();
      btnCancel = new javax.swing.JButton();
      comboType = new javax.swing.JComboBox();
      jLabel6 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      txtName = new javax.swing.JTextField();
      lblData = new javax.swing.JLabel();
      jScrollPane1 = new javax.swing.JScrollPane();
      txtData = new javax.swing.JTextArea();
      jButton1 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("Item Properties");
      setLocationByPlatform(true);
      setResizable(false);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accept.png"))); // NOI18N
      btnOK.setText("Save");
      btnOK.setToolTipText("OK");
      btnOK.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnOKActionPerformed(evt);
         }
      });

      btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
      btnCancel.setText("Delete");
      btnCancel.setToolTipText("Cancel");
      btnCancel.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCancelActionPerformed(evt);
         }
      });

      comboType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
      comboType.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboTypeActionPerformed(evt);
         }
      });

      jLabel6.setText("Type");

      jLabel7.setText("Display Name");

      txtName.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtNameKeyReleased(evt);
         }
      });

      lblData.setText("Data - one item on each line");

      txtData.setColumns(20);
      txtData.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtData.setRows(5);
      jScrollPane1.setViewportView(txtData);

      jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/help.png"))); // NOI18N
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              jPanel3Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215,
                                          Short.MAX_VALUE)
                                    .addComponent(txtName)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(
                                          jPanel3Layout.createSequentialGroup().addComponent(lblData)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.TRAILING,
                                          jPanel3Layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnCancel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnOK))
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.TRAILING,
                                          jPanel3Layout
                                                .createSequentialGroup()
                                                .addComponent(comboType, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                      Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                                      javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                              jPanel3Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(comboType)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                              jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnOK).addComponent(btnCancel)).addContainerGap()));

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed

       if (txtName.getText().isEmpty()) {
           JOptionPane.showMessageDialog(null, "The name property cannot be empty.", "Missing data",
                   JOptionPane.WARNING_MESSAGE);
           return;
       }

       if (((ComboItem) comboType.getSelectedItem()).use_data) {
           if (txtData.getText().isEmpty()) {
               JOptionPane.showMessageDialog(null, "The data property cannot be empty.", "Missing data",
                       JOptionPane.WARNING_MESSAGE);
               return;
           }
           String[] values = txtData.getText().split("\n");
           for (int x = 0; x < values.length; x++) {
               if (values[x].isEmpty()) {
                   JOptionPane.showMessageDialog(null, "The data property shouldn't and can't contain any empty rows.",
                           "Missing data", JOptionPane.WARNING_MESSAGE);
                   return;
               }
           }

       }

       editingItem.szName = txtName.getText();
       if ((((ComboItem) comboType.getSelectedItem()).use_data)) {
           editingItem.szData = txtData.getText();
       }
       editingItem.intType = ((ComboItem) comboType.getSelectedItem()).Type;
       if (((ComboItem) comboType.getSelectedItem()).Type == 6) { //namerow
           if (form.nameRow != null) {
               JOptionPane.showMessageDialog(null, "A name row already exists. There can be only one!", "Invalid data!",
                       JOptionPane.WARNING_MESSAGE);
               return;
           } else {
               form.nameRow = editingItem;
           }
       } else {
           if (form.nameRow == editingItem) {
               form.nameRow = null;
           }
       }

       form.ReturnFocus();
       dispose();

   }//GEN-LAST:event_btnOKActionPerformed

   private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

       if (!DiscardInsteadOfDeleting) {
           editingNode.removeFromParent();
       }
       form.UpdateTree();
       form.ReturnFocus();
       dispose();

   }//GEN-LAST:event_btnCancelActionPerformed

   private void comboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeActionPerformed

       if (comboType.getSelectedIndex() == -1) {
           return;
       }

       comboType.setToolTipText(((ComboItem) comboType.getSelectedItem()).szHelp);

       if (((ComboItem) comboType.getSelectedItem()).use_data) {
           txtData.setText("");
           lblData.setText("Data");
           txtData.setEnabled(true);
           txtData.setBackground(new Color(255, 255, 255));
       } else {
           txtData.setEnabled(false);
           txtData.setBackground(new Color(240, 240, 240));
       }

   }//GEN-LAST:event_comboTypeActionPerformed

   private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased

       ((TreeItem) editingNode.getUserObject()).szName = txtName.getText();
       form.UpdateTree();

   }//GEN-LAST:event_txtNameKeyReleased

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

       btnCancelActionPerformed(null);

   }//GEN-LAST:event_formWindowClosing

   private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

       JOptionPane.showMessageDialog(null, ((ComboItem) comboType.getSelectedItem()).szHelp, "Help: "
               + ((ComboItem) comboType.getSelectedItem()).szName, JOptionPane.INFORMATION_MESSAGE);

   }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JButton btnCancel;
   private javax.swing.JButton btnOK;
   private javax.swing.JComboBox comboType;
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JLabel lblData;
   private javax.swing.JTextArea txtData;
   private javax.swing.JTextField txtName;

   // End of variables declaration//GEN-END:variables
    class ComboItem {

        public String szName;
        public String szHelp;
        public int Type;
        public boolean use_data = false;

        public String toString() {
            return szName;
        }
    }
}
