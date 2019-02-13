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
public class TextRowPanel extends javax.swing.JPanel {

    public Character character;
    public String itemIdentifier;

    public TextRowPanel(Character c, String id, String name) {
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
        if (txtRow.getText().isEmpty()) {
            character.szData.remove(itemIdentifier);
        } else {
            character.szData.put(itemIdentifier, txtRow.getText());
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
            txtRow.setText((String) o);
        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      txtRow = new javax.swing.JTextField();

      lblName.setText("LABEL");

      txtRow.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtRowKeyReleased(evt);
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
                  .addComponent(txtRow, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(txtRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblName)));
   }// </editor-fold>//GEN-END:initComponents

   private void txtRowKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRowKeyReleased
       SaveData();
   }//GEN-LAST:event_txtRowKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables

   public javax.swing.JLabel lblName;
   public javax.swing.JTextField txtRow;
   // End of variables declaration//GEN-END:variables
}
