/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import xeed.Character;
import xeed.ExtendedSheetData;

/**
 *
 * @author Erik
 */
public class ExtendedSheetButtonPanel extends javax.swing.JPanel {

    public Character character;
    private String itemIdentifier;
    private String[] template;
    private String name;

    public ExtendedSheetButtonPanel(Character c, String id, String[] data, String n) {
        character = c;
        template = data;
        itemIdentifier = id;
        name = n;
        initComponents();
        btnExtend.setText(name);
    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      btnExtend = new javax.swing.JButton();

      btnExtend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/book_open.png"))); // NOI18N
      btnExtend.setText("Extended");
      btnExtend.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnExtendActionPerformed(evt);
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
                  .addComponent(btnExtend, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                  .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(lblName)
                  .addComponent(btnExtend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)));
   }// </editor-fold>//GEN-END:initComponents

   private void btnExtendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtendActionPerformed
       if (character == null) {
           return;
       }

       ExtendedSheetData esd = (ExtendedSheetData) character.extData.get(itemIdentifier);
       if (esd == null) {
           esd = new ExtendedSheetData();
           character.extData.put(itemIdentifier, esd);
       }
       if (esd.form == null) {
           esd.form = new ExtendedForm(character, itemIdentifier, template, name);
       }
       esd.form.setVisible(true);
   }//GEN-LAST:event_btnExtendActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables

   public javax.swing.JButton btnExtend;
   public javax.swing.JLabel lblName;
   // End of variables declaration//GEN-END:variables
}
