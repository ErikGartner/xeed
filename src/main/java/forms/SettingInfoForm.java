/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import xeed.XEED;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Erik
 */
public class SettingInfoForm extends javax.swing.JFrame {

    public SettingInfoForm() {

        initComponents();

        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/world.png")));
            this.setIconImages(images);
        } catch (IOException e) {
        }

        lblRevision.setText("Revision: " + XEED.lngSettingRevision + " (" + XEED.szLastSaved + ")");
        txtName.setText(XEED.szSettingName);
        txtThemes.setText(XEED.szThemes);
        txtOutline.setText(XEED.szOutline);

    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLabel1 = new javax.swing.JLabel();
      txtName = new javax.swing.JTextField();
      jLabel2 = new javax.swing.JLabel();
      txtThemes = new javax.swing.JTextField();
      jLabel3 = new javax.swing.JLabel();
      jScrollPane1 = new javax.swing.JScrollPane();
      txtOutline = new javax.swing.JTextArea();
      lblRevision = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Setting Overview");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      jLabel1.setText("Setting Name");

      txtName.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtNameKeyReleased(evt);
         }
      });

      jLabel2.setText("Themes");

      txtThemes.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtThemesKeyReleased(evt);
         }
      });

      jLabel3.setText("Outline");

      txtOutline.setColumns(10);
      txtOutline.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtOutline.setLineWrap(true);
      txtOutline.setRows(5);
      txtOutline.setWrapStyleWord(true);
      txtOutline.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtOutlineKeyReleased(evt);
         }
      });
      jScrollPane1.setViewportView(txtOutline);

      lblRevision.setForeground(new java.awt.Color(102, 102, 102));
      lblRevision.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      lblRevision.setText("Revision: 0");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                              .addComponent(txtName)
                              .addComponent(txtThemes)
                              .addGroup(
                                    layout.createSequentialGroup()
                                          .addGroup(
                                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jLabel1).addComponent(jLabel2)
                                                      .addComponent(jLabel3)).addGap(0, 0, Short.MAX_VALUE))
                              .addComponent(lblRevision, javax.swing.GroupLayout.Alignment.TRAILING,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    Short.MAX_VALUE)).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jLabel2)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtThemes, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jLabel3)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(lblRevision, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
       XEED.SetSettingTitle(txtName.getText());
   }//GEN-LAST:event_txtNameKeyReleased

   private void txtThemesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtThemesKeyReleased
       XEED.szThemes = txtThemes.getText();
   }//GEN-LAST:event_txtThemesKeyReleased

   private void txtOutlineKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOutlineKeyReleased
       XEED.szOutline = txtOutline.getText();
   }//GEN-LAST:event_txtOutlineKeyReleased

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       XEED.hwndSettingInfo = null;
       dispose();
   }//GEN-LAST:event_formWindowClosing

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JLabel lblRevision;
   private javax.swing.JTextField txtName;
   private javax.swing.JTextArea txtOutline;
   private javax.swing.JTextField txtThemes;
   // End of variables declaration//GEN-END:variables
}
