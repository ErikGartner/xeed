package xeed;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class SearchPanel extends javax.swing.JPanel {

   public SearchPanel(boolean all) {
      initComponents();
      chkAll.setSelected(all);
   }

   public void GetFocus() {
      txtSearch.requestFocusInWindow();
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      txtSearch = new javax.swing.JTextField();
      jLabel1 = new javax.swing.JLabel();
      chkAll = new javax.swing.JCheckBox();

      txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            txtSearchKeyPressed(evt);
         }

         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtSearchKeyReleased(evt);
         }
      });

      jLabel1.setText("Find:");

      chkAll.setText("All");
      chkAll.setToolTipText("Search all columns rather than just the names. Ctrl+shift+F checkes the checkbox.");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(chkAll, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel1).addComponent(chkAll)));
   } // </editor-fold>//GEN-END:initComponents

   private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
      if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
         XEED.hwndMain.RemoveSearchField();
         return;
      }

      if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

         Character[] cs = XEED.hwndMain.GetSelectedCharacters();
         if (cs == null) {
            return;
         }

         if (cs.length > 1) {
            int intRet = JOptionPane.showOptionDialog(null, "Are you sure you want to open the selected " + cs.length
                  + " characters?", "Open multiple characters?", JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
               return;
            }
         } else if (cs.length == 0) {
            return;
         }

         for (int x = 0; x < cs.length; x++) {
            if (cs[x] != null) {
               cs[x].DisplayCharacter();
            }
         }

         XEED.hwndMain.RemoveSearchField();
         return;

      }

      if (evt.getKeyCode() == KeyEvent.VK_F) {
         chkAll.setSelected(evt.isShiftDown());
      }

   } //GEN-LAST:event_txtSearchKeyPressed

   private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
      String txtSearchString = txtSearch.getText();
      if (txtSearchString.isEmpty()) {
         XEED.hwndMain.SetSelectedCharacters(null);
         return;
      }

      XEED.hwndMain.SelectCharactersByStringSearch(txtSearchString, chkAll.isSelected());

   } //GEN-LAST:event_txtSearchKeyReleased
     // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JCheckBox chkAll;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JTextField txtSearch;
   // End of variables declaration//GEN-END:variables
}
