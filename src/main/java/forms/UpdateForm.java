/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmUpdate.java
 *
 * Created on 2011-apr-05, 14:18:36
 */
package forms;

import xeed.Constants;
import xeed.Notifier;
import xeed.XEED;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Erik
 */
public class UpdateForm extends javax.swing.JFrame {

   private String URL;

   public UpdateForm() {
      initComponents();
      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/drive_web.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

   }

   private void CreateMain() {

      if (XEED.hwndMain == null) {
         XEED.hwndMain = new MainForm();
         XEED.hwndMain.initMain();
      }

      if (XEED.hwndNotifier == null) {
         XEED.hwndNotifier = new Notifier();
      }

   }

   public void CheckForUpdates(boolean verbose) {

      String szData = XEED.DownloadURLToString(XEED.szUpdateURL);
      boolean betaData = false;
      if (szData.isEmpty()) {
         if (verbose) {
            JOptionPane.showMessageDialog(null, "An error occured while checking for updates.", "Error",
                  JOptionPane.ERROR_MESSAGE);
         }
         return;
      }

      long lngLatestBuild = Long.parseLong(XEED.GetElement(szData, Constants.UPDATE_VERSION, false));

      if (XEED.boolUpdateToBeta) {
         String szBetaUpdate = XEED.DownloadURLToString(XEED.szDevUpdateURL);
         if (!szBetaUpdate.isEmpty()) {
            if (Long.parseLong(XEED.GetElement(szBetaUpdate, Constants.UPDATE_VERSION, false)) > lngLatestBuild) {
               lngLatestBuild = Long.parseLong(XEED.GetElement(szBetaUpdate, Constants.UPDATE_VERSION, false));
               szData = szBetaUpdate;
               betaData = true;
            }
         }
      }

      if (XEED.lngBuild > lngLatestBuild) {
         if (verbose) {
            JOptionPane.showMessageDialog(null, "You are currently running the latests version of xeed!", "Up-to-date",
                  JOptionPane.INFORMATION_MESSAGE);
         }
         return;
      }

      if (XEED.lngBuild == lngLatestBuild) {

         if (!XEED.DeveloperMode) { //Du har samma version och är inte en beta.
            if (verbose) {
               JOptionPane.showMessageDialog(null, "You are currently running the latests version of xeed!",
                     "Up-to-date", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
         }

         if (betaData && XEED.DeveloperMode) { //Du är beta och senaste versionen är beta
            if (verbose) {
               JOptionPane.showMessageDialog(null, "You are currently running the latests version of xeed!",
                     "Up-to-date", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
         }

      }

      lblVersion.setText("XEED " + XEED.GetElement(szData, Constants.UPDATE_STRING_VERSION, false));
      URL = XEED.GetElement(szData, Constants.UPDATE_URL, false);
      lblUrl.setText(URL);
      lblUrl.setToolTipText(URL);
      txtDescription.setText(XEED.GetElement(szData, Constants.UPDATE_INFORMATION, false));
      txtDescription.setCaretPosition(0);
      lblDate.setText(XEED.GetElement(szData, Constants.UPDATE_DATE, false));

      setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2, (int) (Toolkit
            .getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
      setVisible(true);

   }

   private boolean UpdateXEED() {

      try {

         File updater = File.createTempFile("xeed_updater", ".jar");

         BufferedInputStream in = new BufferedInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream(
               "xeed_updater.jar"));
         FileOutputStream fos = new FileOutputStream(updater);
         BufferedOutputStream bout = new BufferedOutputStream(fos, 4096);

         byte data[] = new byte[4096];
         int count = 0;
         while ((count = in.read(data)) >= 0) {
            bout.write(data, 0, count);
            data = new byte[4096];
         }

         bout.close();
         fos.close();
         in.close();

         Process ps = Runtime.getRuntime().exec(
               new String[] {
                     "java",
                     "-jar",
                     updater.getAbsolutePath(),
                     URL,
                     new File(MainForm.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                           .getAbsolutePath() });
         //Process p = Runtime.getRuntime().exec(System.getProperties().getProperty("java.home") + "\\bin\\javaw.exe" + " -jar \"" + updater.getAbsolutePath() + "\" " + URL + " \"" + new File(frmMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "\"");
         System.exit(0);
         return true;
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "An error occured while preparing updater.\n" + e, "Error",
               JOptionPane.ERROR_MESSAGE);
         CreateMain();
         dispose();
         return false;
      }

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblVersion = new javax.swing.JLabel();
      jScrollPane1 = new javax.swing.JScrollPane();
      txtDescription = new javax.swing.JTextArea();
      btnUpdate = new javax.swing.JButton();
      lblUrl = new javax.swing.JLabel();
      lblDate = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("New version of XEED available!");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      lblVersion.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      lblVersion.setText("version");
      lblVersion.setName("lblVersion"); // NOI18N

      jScrollPane1.setName("jScrollPane1"); // NOI18N

      txtDescription.setColumns(5);
      txtDescription.setEditable(false);
      txtDescription.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtDescription.setLineWrap(true);
      txtDescription.setRows(5);
      txtDescription.setTabSize(3);
      txtDescription.setWrapStyleWord(true);
      txtDescription.setName("txtDescription"); // NOI18N
      jScrollPane1.setViewportView(txtDescription);

      btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drive_web.png"))); // NOI18N
      btnUpdate.setText("Update");
      btnUpdate.setName("btnUpdate"); // NOI18N
      btnUpdate.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnUpdateActionPerformed(evt);
         }
      });

      lblUrl.setForeground(new java.awt.Color(102, 102, 102));
      lblUrl.setText("url");
      lblUrl.setMinimumSize(new java.awt.Dimension(0, 0));
      lblUrl.setName("lblUrl"); // NOI18N

      lblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      lblDate.setText("date");
      lblDate.setName("lblDate"); // NOI18N

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            javax.swing.GroupLayout.Alignment.TRAILING,
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                              .addGroup(
                                    layout.createSequentialGroup()
                                          .addComponent(lblUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 225,
                                                Short.MAX_VALUE)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(btnUpdate))
                              .addComponent(lblVersion, javax.swing.GroupLayout.Alignment.LEADING,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                              .addComponent(lblDate, javax.swing.GroupLayout.Alignment.LEADING,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(lblVersion)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(lblDate)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(btnUpdate)
                              .addComponent(lblUrl, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addContainerGap()));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
      if (!XEED.IsSettingEmpty()) {
         int intRet = JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?",
               JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
         if (intRet == 0) {
            if (!XEED.SaveSetting(false, true)) {
               return;
            }
         } else if (intRet == -1) {
            return;
         }
      }
      UpdateXEED();
   }//GEN-LAST:event_btnUpdateActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      CreateMain();
      dispose();
   }//GEN-LAST:event_formWindowClosing

   /*
    * Derp
    */
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton btnUpdate;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JLabel lblDate;
   private javax.swing.JLabel lblUrl;
   private javax.swing.JLabel lblVersion;
   private javax.swing.JTextArea txtDescription;
   // End of variables declaration//GEN-END:variables
}
