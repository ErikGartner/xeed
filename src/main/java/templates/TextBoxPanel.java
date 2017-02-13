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
public class TextBoxPanel extends javax.swing.JPanel {

   public Character character;
   public String itemIdentifier;

   public TextBoxPanel(Character c, String id, String name) {
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
      if (txtBox.getText().isEmpty()) {
         character.szData.remove(itemIdentifier);
      } else {
         character.szData.put(itemIdentifier, txtBox.getText());
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

         txtBox.setText((String) o);

      } else {
         System.out.println(lblName.getText() + " loaded invalid data");
      }

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      jScrollPane1 = new javax.swing.JScrollPane();
      txtBox = new javax.swing.JTextArea();

      lblName.setText("LABEL");

      txtBox.setColumns(8);
      txtBox.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtBox.setLineWrap(true);
      txtBox.setRows(5);
      txtBox.setTabSize(3);
      txtBox.setWrapStyleWord(true);
      txtBox.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtBoxKeyReleased(evt);
         }
      });
      jScrollPane1.setViewportView(txtBox);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                  .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addComponent(lblName).addContainerGap(82, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE));
   }// </editor-fold>//GEN-END:initComponents

   private void txtBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBoxKeyReleased
      SaveData();
   }//GEN-LAST:event_txtBoxKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JScrollPane jScrollPane1;
   public javax.swing.JLabel lblName;
   public javax.swing.JTextArea txtBox;
   // End of variables declaration//GEN-END:variables
}
