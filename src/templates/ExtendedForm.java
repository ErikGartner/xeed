/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmExtended.java
 *
 * Created on 2010-nov-28, 19:12:02
 */
package templates;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import xeed.Character;
import xeed.XEED;
import xeed.ExtendedSheetData;

/**
 *
 * @author Erik
 */
public class ExtendedForm extends javax.swing.JFrame {

   //TODO Fixa drag'n'drop
   private Character characterHandle = null;
   private Vector jTableModel = new Vector(0, 2); // Model fÃ¶r tabellen
   private Vector jTableHeader = new Vector(0);
   private DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
   private DefaultTableModel df = null;
   private String identifier;
   private String[] template;

   /**
    * Creates new form frmExtended
    */
   public ExtendedForm(Character c, String id, String[] data, String name) {

      characterHandle = c;
      identifier = id;
      template = data;
      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/book_open.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

      jTableHeader.add("Property");
      jTableHeader.add("Value");

      df = new DefaultTableModel(jTableModel, jTableHeader) {

         Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

         @Override
         public Class getColumnClass(int columnhandleCharacter) {
            return types[columnhandleCharacter];
         }
      };
      tblProperties.setModel(df);
      df.fireTableStructureChanged();
      setTitle(name + ": " + characterHandle.GetCharacterName());
      LoadData();
   }

   public String[] GetArrayOfTableContent(int coloumn) {

      if (tblProperties.getRowCount() == 0) {
         return null;
      }

      String szContent[] = new String[tblProperties.getRowCount()];

      for (int x = 0; x < tblProperties.getRowCount(); x++) {
         szContent[x] = (String) tblProperties.getValueAt(x, coloumn);
      }
      return szContent;

   }

   public boolean SaveData() {
      ExtendedSheetData esd = new ExtendedSheetData();
      esd.Properties = GetArrayOfTableContent(0);
      esd.Values = GetArrayOfTableContent(1);

      if (esd.Values == null || esd.Properties == null) {
         characterHandle.extData.put(identifier, null);
      } else {
         characterHandle.extData.put(identifier, esd);
      }

      Character[] affectedcharacters = new Character[1];
      affectedcharacters[0] = characterHandle;
      XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);
      return true;
   }

   public boolean LoadData() {

      jTableModel.clear();
      ExtendedSheetData esd = (ExtendedSheetData) characterHandle.extData.get(identifier);
      if (esd == null) {
         return true;
      }

      if (esd.Properties.length != esd.Values.length) {
         JOptionPane.showMessageDialog(null, "Error, corrupt/missing data in extended sheet.", "Error",
               JOptionPane.ERROR_MESSAGE);
         return false;
      }

      try {

         for (int x = 0; x < esd.Properties.length; x++) {
            Vector o = new Vector(0);
            o.add(esd.Properties[x]);
            o.add(esd.Values[x]);
            jTableModel.add(o);
         }

      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error when loading the character!", "Error", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      df.fireTableDataChanged();
      return true;
   }

   private String padRight(String s, int n) {
      return String.format("%1$-" + n + "s", s);
   }

   public void HandlePopUpRequest(MouseEvent evt) {

      if (!evt.isPopupTrigger()) {
         return;
      }

      mnuPop.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mnuPop = new javax.swing.JPopupMenu();
      mnuAdd = new javax.swing.JMenuItem();
      mnuAddTemplate = new javax.swing.JMenuItem();
      jSeparator1 = new javax.swing.JPopupMenu.Separator();
      mnuRemove = new javax.swing.JMenuItem();
      jScrollPane1 = new javax.swing.JScrollPane();
      tblProperties = new javax.swing.JTable();

      mnuAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
      mnuAdd.setText("Add row");
      mnuAdd.setToolTipText("");
      mnuAdd.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuAddActionPerformed(evt);
         }
      });
      mnuPop.add(mnuAdd);

      mnuAddTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/table_add.png"))); // NOI18N
      mnuAddTemplate.setText("Add template");
      mnuAddTemplate.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuAddTemplateActionPerformed(evt);
         }
      });
      mnuPop.add(mnuAddTemplate);
      mnuPop.add(jSeparator1);

      mnuRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
      mnuRemove.setText("Remove row(s)");
      mnuRemove.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuRemoveActionPerformed(evt);
         }
      });
      mnuPop.add(mnuRemove);

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mousePressed(java.awt.event.MouseEvent evt) {
            jScrollPane1MousePressed(evt);
         }

         public void mouseReleased(java.awt.event.MouseEvent evt) {
            jScrollPane1MouseReleased(evt);
         }
      });

      tblProperties.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null }, { null, null },
            { null, null }, { null, null } }, new String[] { "Property", "Value" }) {

         Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

         public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
         }
      });
      tblProperties.setToolTipText("Right click to bring up menu, double click on cell to edit.");
      tblProperties.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
      tblProperties.setDragEnabled(true);
      tblProperties.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
      tblProperties.setGridColor(new java.awt.Color(204, 204, 204));
      tblProperties.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      tblProperties.getTableHeader().setReorderingAllowed(false);
      tblProperties.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mousePressed(java.awt.event.MouseEvent evt) {
            tblPropertiesMousePressed(evt);
         }

         public void mouseReleased(java.awt.event.MouseEvent evt) {
            tblPropertiesMouseReleased(evt);
         }
      });
      jScrollPane1.setViewportView(tblProperties);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jScrollPane1));

      pack();
   } // </editor-fold>//GEN-END:initComponents

   private void formWindowClosing(java.awt.event.WindowEvent evt) {
      //GEN-FIRST:event_formWindowClosing
      if (tblProperties.getCellEditor() != null) {
         tblProperties.getCellEditor().stopCellEditing();
      }
      SaveData();
      ExtendedSheetData esd = (ExtendedSheetData) characterHandle.extData.get(identifier);
      if (esd != null) {
         esd.form = null;
      }
      dispose();
   } //GEN-LAST:event_formWindowClosing

   private void mnuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddActionPerformed
      Vector o = new Vector(0);
      o.add("");
      o.add("");
      if (tblProperties.getSelectedRow() != -1) {
         df.insertRow(tblProperties.getSelectedRow(), o);
      } else {
         df.addRow(o);
      }
      df.fireTableDataChanged();
   } //GEN-LAST:event_mnuAddActionPerformed

   private void mnuAddTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddTemplateActionPerformed
      int sel = tblProperties.getSelectedRow();
      for (int x = 0; x < template.length; x++) {
         Vector o = new Vector(0);
         o.add(template[x]);
         o.add("");
         if (sel != -1) {
            df.insertRow(sel + x, o);
         } else {
            df.addRow(o);
         }
      }
      df.fireTableDataChanged();
   } //GEN-LAST:event_mnuAddTemplateActionPerformed

   private void mnuRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemoveActionPerformed
      if (tblProperties.getSelectedRow() == -1) {
         return;
      }

      int intSelected[] = tblProperties.getSelectedRows();
      for (int x = intSelected.length - 1; x >= 0; x--) {
         jTableModel.remove(intSelected[x]);
      }
      df.fireTableDataChanged();
      tblProperties.clearSelection();
   } //GEN-LAST:event_mnuRemoveActionPerformed

   private void tblPropertiesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPropertiesMouseReleased
      HandlePopUpRequest(evt);
   } //GEN-LAST:event_tblPropertiesMouseReleased

   private void tblPropertiesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPropertiesMousePressed
      HandlePopUpRequest(evt);
   } //GEN-LAST:event_tblPropertiesMousePressed

   private void jScrollPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseReleased
      HandlePopUpRequest(evt);
   } //GEN-LAST:event_jScrollPane1MouseReleased

   private void jScrollPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MousePressed
      HandlePopUpRequest(evt);
   } //GEN-LAST:event_jScrollPane1MousePressed

   /*
    * derp
    */
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JPopupMenu.Separator jSeparator1;
   private javax.swing.JMenuItem mnuAdd;
   private javax.swing.JMenuItem mnuAddTemplate;
   private javax.swing.JPopupMenu mnuPop;
   private javax.swing.JMenuItem mnuRemove;
   private javax.swing.JTable tblProperties;
   // End of variables declaration//GEN-END:variables
}
