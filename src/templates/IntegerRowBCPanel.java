/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import xeed.Character;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class IntegerRowBCPanel extends javax.swing.JPanel {

   public Character character;
   public String itemIdentifier;

   public IntegerRowBCPanel(Character c, String id, String name) {
      itemIdentifier = id;
      character = c;
      initComponents();
      lblName.setText(name);
      LoadData();
   }

   public void SaveData() {

      if (character == null) {
         return;
      }

      if (!txtInteger.getText().isEmpty()) {
         if (comboADBC.getSelectedIndex() == 0) {
            character.szData.put(itemIdentifier, txtInteger.getText() + " AD");
         } else {
            character.szData.put(itemIdentifier, txtInteger.getText() + " BC");
         }
      } else {
         character.szData.remove(itemIdentifier);
      }

      Character[] affectedcharacters = new Character[1];
      affectedcharacters[0] = character;
      XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);
   }

   public void LoadData() {

      if (character == null) {
         return;
      }

      Object o = character.szData.get(itemIdentifier);
      if (o == null) {
         return;
      }

      if (o.getClass().equals(String.class)) {

         String data[] = ((String) o).split(" ");

         txtInteger.setText(data[0]);
         if (data[1].equalsIgnoreCase("AD")) {
            comboADBC.setSelectedIndex(0);
         } else {
            comboADBC.setSelectedIndex(1);
         }

      } else {
         System.out.println(lblName.getText() + " loaded invalid data");
      }

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      txtInteger = new javax.swing.JTextField();
      comboADBC = new javax.swing.JComboBox();

      lblName.setText("LABEL");

      txtInteger.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtIntegerKeyReleased(evt);
         }
      });

      comboADBC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AD", "BC" }));
      comboADBC.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboADBCActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                              javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInteger, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboADBC, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(txtInteger, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(lblName)
                  .addComponent(comboADBC, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
   } // </editor-fold>//GEN-END:initComponents

   private void validateInteger(JTextField txtField) {
      if (txtField.getText().isEmpty()) {
         return;
      }

      try {
         int y = Integer.parseInt(txtField.getText());
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "The textfield '" + lblName.getText() + "' must contain an integer.",
               "Invalid data", JOptionPane.WARNING_MESSAGE);
         txtField.setText("");
      }

   }

   private void txtIntegerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIntegerKeyReleased
      validateInteger(txtInteger);
      SaveData();

   } //GEN-LAST:event_txtIntegerKeyReleased

   private void comboADBCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboADBCActionPerformed
      SaveData();
   } //GEN-LAST:event_comboADBCActionPerformed
     // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JComboBox comboADBC;
   public javax.swing.JLabel lblName;
   public javax.swing.JTextField txtInteger;
   // End of variables declaration//GEN-END:variables
}
