/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmAbout.java
 *
 * Created on 2010-dec-19, 22:19:44
 */
package forms;

import xeed.XEED;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Erik
 */
public class AboutForm extends JFrame {

   Timer timer = null;
   CreditsRoller c = new CreditsRoller(this);

   public AboutForm() {

      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/information.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension screensize = tk.getScreenSize();
      this.setLocation(screensize.width / 2 - this.getWidth() / 2, screensize.height / 2 - this.getHeight() / 2); //Bra start pos

      jLabel3.setText(XEED.szHomePage);
      jLabel5.setText(XEED.szVersion);
      jLabel7.setText(Long.toString(XEED.lngBuild) + "." + XEED.GetXEEDCRC32());
      jLabel9.setText(XEED.szCompiledOn);

      timer = new Timer();
      timer.scheduleAtFixedRate(c, 0, 2500);
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel1 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      jLabel9 = new javax.swing.JLabel();
      jLabel11 = new javax.swing.JLabel();
      jLabel10 = new javax.swing.JLabel();
      jLabel12 = new javax.swing.JLabel();
      jLabel1 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setAlwaysOnTop(true);
      setResizable(false);
      setUndecorated(true);
      addWindowFocusListener(new java.awt.event.WindowFocusListener() {
         public void windowGainedFocus(java.awt.event.WindowEvent evt) {
         }

         public void windowLostFocus(java.awt.event.WindowEvent evt) {
            formWindowLostFocus(evt);
         }
      });

      jPanel1.setBackground(new java.awt.Color(102, 102, 102));
      jPanel1.setOpaque(false);

      jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
      jLabel2.setForeground(new java.awt.Color(255, 255, 255));
      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel2.setText("Homepage");

      jLabel3.setForeground(new java.awt.Color(255, 255, 255));
      jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel3.setText("http://www.smoiz.se/");
      jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel3MouseClicked(evt);
         }
      });

      jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
      jLabel4.setForeground(new java.awt.Color(255, 255, 255));
      jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel4.setText("Version");

      jLabel5.setForeground(new java.awt.Color(255, 255, 255));
      jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel5.setText("0.1B");

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
      jLabel6.setForeground(new java.awt.Color(255, 255, 255));
      jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel6.setText("Build");

      jLabel7.setForeground(new java.awt.Color(255, 255, 255));
      jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel7.setText("20");

      jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14));
      jLabel8.setForeground(new java.awt.Color(255, 255, 255));
      jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel8.setText("Compilation Date");

      jLabel9.setForeground(new java.awt.Color(255, 255, 255));
      jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel9.setText("The distant future");

      jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
      jLabel10.setForeground(new java.awt.Color(255, 255, 255));
      jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel10.setText("Credits");

      jLabel12.setForeground(new java.awt.Color(255, 255, 255));
      jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel12.setText("creds");

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel1Layout
                        .createSequentialGroup()
                        .addGroup(
                              jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.TRAILING,
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(
                                                      jPanel1Layout
                                                            .createParallelGroup(
                                                                  javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel4,
                                                                  javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                                  Short.MAX_VALUE)
                                                            .addComponent(jLabel5,
                                                                  javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                                  Short.MAX_VALUE)
                                                            .addComponent(jLabel7,
                                                                  javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                                  Short.MAX_VALUE)
                                                            .addComponent(jLabel3,
                                                                  javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                                  Short.MAX_VALUE)))
                                    .addGroup(
                                          jPanel1Layout.createSequentialGroup().addGap(128, 128, 128)
                                                .addComponent(jLabel11).addGap(40, 40, 40))
                                    .addGroup(
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))
                                    .addGroup(
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))
                                    .addGroup(
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))
                                    .addGroup(
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))
                                    .addGroup(
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.TRAILING,
                                          jPanel1Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 330,
                                                      Short.MAX_VALUE))).addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  javax.swing.GroupLayout.Alignment.TRAILING,
                  jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5)
                        .addGap(11, 11, 11).addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7)
                        .addGap(11, 11, 11).addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9)
                        .addGap(11, 11, 11).addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jLabel11)));

      jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/about.png"))); // NOI18N

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1));
      layout.setVerticalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel1));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
      XEED.hwndAbout = null;
      timer.cancel();
      timer.purge();
      dispose();
   }//GEN-LAST:event_formWindowLostFocus

   private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
      XEED.ExecuteLink(XEED.szHomePage);
   }//GEN-LAST:event_jLabel3MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel11;
   private javax.swing.JLabel jLabel12;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel jPanel1;

   // End of variables declaration//GEN-END:variables

   class CreditsRoller extends TimerTask {

      private AboutForm A = null;
      private int x = 0;

      public CreditsRoller(AboutForm a) {
         A = a;
      }

      public void run() {

         if (x >= XEED.szCredits.length) {
            x = 0;
         }

         A.jLabel12.setText(XEED.szCredits[x]);
         A.jLabel12.repaint();
         x++;
      }
   }
}
