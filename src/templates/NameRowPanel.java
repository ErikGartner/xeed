/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import xeed.Character;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class NameRowPanel extends javax.swing.JPanel {

   public Character character;
   public String itemIdentifier;

   public NameRowPanel(Character c, String id, String name) {
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
      character.szData.put(itemIdentifier, txtName.getText());
      character.hwndForm.setTitle(txtName.getText());

      Character[] affectedcharacters = new Character[XEED.charDB.size()];
      XEED.charDB.toArray(affectedcharacters);
      XEED.hwndNotifier.FireUpdate(affectedcharacters, false, true, true, true, true, true, true, false, false);

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

         txtName.setText((String) o);

      } else {
         System.out.println(lblName.getText() + " loaded invalid data");
      }

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      txtName = new javax.swing.JTextField();

      lblName.setText("LABEL");

      txtName.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtNameActionPerformed(evt);
         }
      });
      txtName.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtNameKeyReleased(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblName)));
   } // </editor-fold>//GEN-END:initComponents

   private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
      SaveData();
   } //GEN-LAST:event_txtNameKeyReleased

   private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
      // TODO add your handling code here:
   } //GEN-LAST:event_txtNameActionPerformed
     // Variables declaration - do not modify//GEN-BEGIN:variables

   public javax.swing.JLabel lblName;
   public javax.swing.JTextField txtName;
   // End of variables declaration//GEN-END:variables
}
