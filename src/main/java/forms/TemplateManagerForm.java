/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import xeed.Constants;
import xeed.Template;
import xeed.XEED;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erik
 */
// Fixa bug som  ger error när man  laddar in flera samtidigt.
public class TemplateManagerForm extends javax.swing.JFrame {

   private Vector jTableModel = new Vector(0);
   private Vector jTableHeader = new Vector(0);
   private ArrayList<TemplateItem> templateList = new ArrayList(0);
   private boolean downloading = false;

   /**
    * Creates new form frmTemplateManager
    */
   public TemplateManagerForm() {

      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/layout_edit.png")));
         setIconImages(images);
      } catch (IOException e) {
      }

      tblTemplates.getColumnModel().getColumn(0).setMinWidth(20);
      tblTemplates.getColumnModel().getColumn(0).setMaxWidth(20);
      tblTemplates.getColumnModel().getColumn(0).setPreferredWidth(20);

      LoadTableList();

   }

   private void LoadTableList() {

      FileFilter xdtFilter = new FileFilter() {
         @Override
         public boolean accept(File file) {

            if (file.isDirectory()) {
               return false;
            }

            if (!file.canRead()) {
               return false;
            }

            if (!file.isFile()) {
               return false;
            }

            if (!file.getName().toLowerCase().endsWith(".xdt")) {
               return false;
            }

            if (file.length() < 1) {
               return false;
            }

            return true;

         }
      };

      templateList.clear();

      File dir = new File(XEED.szTemplateFolder);
      File fileList[] = dir.listFiles(xdtFilter);

      if (fileList != null) {

         //Load templates in local folder.
         for (int x = 0; x < fileList.length; x++) {

            Template t = new Template();
            if (t.LoadTemplateFromFile(fileList[x].getAbsolutePath())) {

               TemplateItem ti = new TemplateItem();
               ti.sourceLocal = true;
               ti.template = t;
               ti.templateID = t.GetTemplateID();
               ti.name = t.GetName();
               ti.game = t.GetGame();
               ti.author = t.GetAuthor();
               ti.loaded = XEED.IsTemplateLoaded(t);
               ti.path = t.GetFilePath();
               ti.date = t.GetCreationDate();
               ti.version = String.valueOf(t.GetVersion());
               ti.description = t.GetDescription();

               templateList.add(ti);

            }

         }

      }

      for (int x = 0; x < XEED.templateDB.size(); x++) {

         boolean found = false;
         for (int y = 0; y < templateList.size(); y++) {

            TemplateItem ti = templateList.get(y);
            if (ti.templateID.equalsIgnoreCase(XEED.templateDB.get(x).GetTemplateID())) {
               found = true;
               ti.loaded = true;
               if (!XEED.szPath.isEmpty()) {
                  ti.path = XEED.szPath;
               }
               ti.sourceLocal = true;
            }

         }

         if (!found) {

            TemplateItem newTi = new TemplateItem();
            newTi.path = XEED.templateDB.get(x).GetFilePath();
            newTi.author = XEED.templateDB.get(x).GetAuthor();
            newTi.game = XEED.templateDB.get(x).GetGame();
            newTi.name = XEED.templateDB.get(x).GetName();
            newTi.sourceLocal = true;
            newTi.templateID = XEED.templateDB.get(x).GetTemplateID();
            newTi.version = Long.toString(XEED.templateDB.get(x).GetVersion());
            newTi.description = XEED.templateDB.get(x).GetDescription();
            newTi.date = XEED.templateDB.get(x).GetCreationDate();
            newTi.loaded = true;
            newTi.template = XEED.templateDB.get(x);
            templateList.add(newTi);

         }

      }

      RefreshTable();

      new Thread(new Runnable() {
         @Override
         public void run() {
            DownloadTemplateList();
         }
      }).start();

   }

   private void DownloadTemplateList() {

      if (downloading) {
         return;
      }
      downloading = true;

      String szURL;
      if (XEED.boolCustomRepository) {
         szURL = XEED.szCustomReposityURL;
      } else {
         szURL = XEED.szTemplateListURL;
      }

      try {

         String szData = XEED.DownloadURLToString(szURL);
         if (szData.isEmpty()) {
            downloading = false;
            return;
         }

         String szRepositoryHeader = XEED.GetElement(szData, Constants.TEMPALTE_REPOSITORY, false); //HEADER that identifies it as an template repository
         if (szRepositoryHeader.isEmpty()) {
            downloading = false;
            return;
         }
         if (Long.parseLong(XEED.GetElement(szRepositoryHeader, Constants.TEMPLATE_REPOSITORY_REQUIREDBUILD, false)) > XEED.lngBuild) {
            downloading = false;
            return;
         }

         String templates[] = XEED.GetElements(szData, Constants.TEMPLATE_HEADER, false);
         for (int x = 0; x < templates.length; x++) {

            String name = XEED.GetElement(templates[x], Constants.TEMPLATE_NAME, true);
            String game = XEED.GetElement(templates[x], Constants.TEMPLATE_GAME, true);
            String author = XEED.GetElement(templates[x], Constants.TEMPLATE_AUTHOR, true);
            String version = XEED.GetElement(templates[x], Constants.TEMPLATE_VERSION, false);
            String date = XEED.GetElement(templates[x], Constants.TEMPLATE_DATE, false);
            String description = XEED.GetElement(templates[x], Constants.TEMPLATE_DESCRIPTION, true);
            String url = XEED.GetElement(templates[x], Constants.TEMPLATE_URL, false);
            String build = XEED.GetElement(templates[x], Constants.TEMPLATE_XEED_BUILD, false);
            String id = XEED.GetElement(templates[x], Constants.TEMPLATE_ID, false);

            boolean found = false;
            for (int y = 0; y < templateList.size(); y++) {

               TemplateItem ti = templateList.get(y);
               if (ti.templateID.equalsIgnoreCase(id)) {
                  found = true;
                  ti.URL = url;
                  ti.sourceOnline = true;
               }

            }

            if (!found) {

               TemplateItem ti = new TemplateItem();
               ti.URL = url;
               ti.author = author;
               ti.game = game;
               ti.name = name;
               ti.sourceOnline = true;
               ti.templateID = id;
               ti.version = version;
               ti.description = description;
               ti.date = date;
               templateList.add(ti);

            }

         }

      } catch (Exception e) {
         downloading = false;
         return;
      }

      RefreshTable();
      downloading = false;
   }

   private void RefreshTable() {

      //TODO: Adapt to new version.
      jTableModel.clear();

      for (int x = 0; x < templateList.size(); x++) {

         Vector o = new Vector(0);
         if (templateList.get(x).loaded) {
            o.add(new ImageIcon(getClass().getResource("/accept.png")));
         } else {
            o.add(new ImageIcon(getClass().getResource("/delete.png")));
         }
         o.add(templateList.get(x));
         o.add(templateList.get(x).game);
         o.add(templateList.get(x).author);
         o.add(templateList.get(x).version);
         o.add(templateList.get(x).date);

         jTableModel.add(o);
      }
      DefaultTableModel df = (DefaultTableModel) tblTemplates.getModel();
      df.fireTableDataChanged();

   }

   private void HandlePopUpRequest(MouseEvent evt) {

      if (!evt.isPopupTrigger()) {
         return;
      }

      if (tblTemplates.getSelectedRow() == -1) {
         mnuDescription.setEnabled(false);
      } else {
         mnuDescription.setEnabled(true);
      }

      mnuPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   private boolean LoadTemplateFromTemplateItem(TemplateItem ti) {

      if (ti.template != null) {

         if (XEED.IsTemplateLoaded(ti.template)) {
            return true;
         }

         if (XEED.LoadTemplate(ti.template)) {
            return true;
         }

         ti.template = null; //probably someting wrong with template.

      }

      if (ti.sourceLocal) {

         Template t = new Template();

         if (t.LoadTemplateFromFile(ti.path)) {
            return XEED.LoadTemplate(t);
         } else {
            ti.sourceLocal = false;
            ti.path = "";
            if (!ti.sourceOnline) {
               templateList.remove(ti); //erronous info, remove template.
            }
            return false;
         }

      }

      if (ti.sourceOnline) {

         String data = XEED.DownloadURLToString(ti.URL);
         if (data.isEmpty()) {
            ti.sourceOnline = false;
            ti.URL = "";
            return false;
         }

         Template t = new Template();
         t.ParseTemplate(data);

         if (XEED.IsTemplateLoaded(t)) {
            return true;
         }

         if (!XEED.LoadTemplate(t)) {
            ti.sourceOnline = false;
            ti.URL = "";
            return false;
         }

         //save file
         new File(XEED.szTemplateFolder).mkdirs();
         File path = new File(XEED.szTemplateFolder + File.separator + t.GetName() + ".xdt");
         if (!path.exists()) {
            if (XEED.DownloadURLToFile(ti.URL, path.getAbsolutePath())) {
               return true;
            }
         }

         //allow user to select custom location.
         JFileChooser fc = new JFileChooser();
         fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
         fc.addChoosableFileFilter(new FileNameExtensionFilter("XEED Template", "xdt"));
         fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
         fc.setMultiSelectionEnabled(false);
         fc.setDialogTitle("A local file conflict was detected, select were to save a local copy of " + t.GetName()
               + " (version: " + t.GetVersion() + ") by " + t.GetAuthor());

         fc.setCurrentDirectory(new File(XEED.szTemplateFolder));
         fc.setSelectedFile(new File(t.GetName() + ".xdt"));

         int intRet = fc.showSaveDialog(null);
         if (intRet != JFileChooser.APPROVE_OPTION) { //spara inte filen.
            return true;
         }

         if (!XEED.DownloadURLToFile(ti.URL, fc.getSelectedFile().getAbsolutePath())) {
            return false;
         }

      }

      return true;

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mnuPopUp = new javax.swing.JPopupMenu();
      mnuLoad = new javax.swing.JMenuItem();
      mnuLoadCustom = new javax.swing.JMenuItem();
      mnuUnload = new javax.swing.JMenuItem();
      jSeparator1 = new javax.swing.JPopupMenu.Separator();
      mnuDescription = new javax.swing.JMenuItem();
      frmRefresh = new javax.swing.JMenuItem();
      jScrollPane1 = new javax.swing.JScrollPane();
      tblTemplates = new javax.swing.JTable();

      mnuLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
      mnuLoad.setText("Load");
      mnuLoad.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuLoadActionPerformed(evt);
         }
      });
      mnuPopUp.add(mnuLoad);

      mnuLoadCustom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
      mnuLoadCustom.setText("Manual Loading");
      mnuLoadCustom.setToolTipText("Download from XEED Online");
      mnuLoadCustom.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuLoadCustomActionPerformed(evt);
         }
      });
      mnuPopUp.add(mnuLoadCustom);

      mnuUnload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
      mnuUnload.setText("Unload");
      mnuUnload.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuUnloadActionPerformed(evt);
         }
      });
      mnuPopUp.add(mnuUnload);
      mnuPopUp.add(jSeparator1);

      mnuDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/information.png"))); // NOI18N
      mnuDescription.setText("Show Description");
      mnuDescription.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuDescriptionActionPerformed(evt);
         }
      });
      mnuPopUp.add(mnuDescription);

      frmRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_refresh.png"))); // NOI18N
      frmRefresh.setText("Refresh");
      frmRefresh.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            frmRefreshActionPerformed(evt);
         }
      });
      mnuPopUp.add(frmRefresh);

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("Template Manager");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseReleased(java.awt.event.MouseEvent evt) {
            jScrollPane1MouseReleased(evt);
         }

         public void mousePressed(java.awt.event.MouseEvent evt) {
            jScrollPane1MousePressed(evt);
         }
      });

      jTableHeader.add("");
      jTableHeader.add("Name");
      jTableHeader.add("Game");
      jTableHeader.add("Author");
      jTableHeader.add("Version");
      jTableHeader.add("Created On");
      tblTemplates.setAutoCreateRowSorter(true);
      tblTemplates.setModel(new javax.swing.table.DefaultTableModel(jTableModel, jTableHeader) {
         Class[] types = new Class[] { ImageIcon.class, Object.class, String.class, String.class, String.class,
               String.class, String.class };

         public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
         }
      });
      tblTemplates.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      tblTemplates.setShowHorizontalLines(false);
      tblTemplates.setShowVerticalLines(false);
      tblTemplates.getTableHeader().setReorderingAllowed(false);
      tblTemplates.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseReleased(java.awt.event.MouseEvent evt) {
            tblTemplatesMouseReleased(evt);
         }

         public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblTemplatesMouseClicked(evt);
         }

         public void mousePressed(java.awt.event.MouseEvent evt) {
            tblTemplatesMousePressed(evt);
         }
      });
      tblTemplates.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            tblTemplatesKeyPressed(evt);
         }
      });
      jScrollPane1.setViewportView(tblTemplates);
      tblTemplates.getColumnModel().getSelectionModel()
            .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addContainerGap()
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                  .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addContainerGap()
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                  .addContainerGap()));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      XEED.hwndTemplateManager = null;
      dispose();
   }//GEN-LAST:event_formWindowClosing

   private void tblTemplatesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTemplatesMousePressed
      HandlePopUpRequest(evt);
   }//GEN-LAST:event_tblTemplatesMousePressed

   private void tblTemplatesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTemplatesMouseReleased
      HandlePopUpRequest(evt);
   }//GEN-LAST:event_tblTemplatesMouseReleased

   private void jScrollPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MousePressed
      HandlePopUpRequest(evt);
   }//GEN-LAST:event_jScrollPane1MousePressed

   private void jScrollPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseReleased
      HandlePopUpRequest(evt);
   }//GEN-LAST:event_jScrollPane1MouseReleased

   private void mnuLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoadActionPerformed

      if (tblTemplates.getSelectedRow() == -1) {
         return;
      }

      int rows[] = tblTemplates.getSelectedRows();
      for (int x = 0; x < rows.length; x++) {

         TemplateItem t = (TemplateItem) tblTemplates.getValueAt(rows[x], 1);
         if (t != null) {
            LoadTemplateFromTemplateItem(t);
         }

      }

      LoadTableList();

   }//GEN-LAST:event_mnuLoadActionPerformed

   private void mnuUnloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUnloadActionPerformed
      if (tblTemplates.getSelectedRow() == -1) {
         return;
      }

      int intRet = JOptionPane
            .showOptionDialog(
                  null,
                  "Warning! Unloading templates will remove all characters that use those templates.\nDo you wish to proceed?",
                  "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
      if (intRet != 0) {
         return;
      }

      int rows[] = tblTemplates.getSelectedRows();
      for (int x = 0; x < rows.length; x++) {

         TemplateItem t = (TemplateItem) tblTemplates.getValueAt(rows[x], 1);
         if (t != null) {

            XEED.UnloadTemplate(t.template);

         }

      }

      LoadTableList();

   }//GEN-LAST:event_mnuUnloadActionPerformed

   private void mnuLoadCustomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoadCustomActionPerformed

      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new FileNameExtensionFilter("xeed Template", "xdt"));
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.setMultiSelectionEnabled(true);

      fc.setCurrentDirectory(new File(XEED.GetCurrentDirectory() + File.separator + "templates"));

      fc.setDialogTitle("Select the template file(s).");
      int intRet = fc.showOpenDialog(null);

      if (intRet != JFileChooser.APPROVE_OPTION) {
         return;
      }

      File files[] = fc.getSelectedFiles();
      for (int x = 0; x < files.length; x++) {
         Template t = new Template();
         t.LoadTemplateFromFile(files[x].getAbsolutePath());
         XEED.LoadTemplate(t);
      }

      LoadTableList();

   }//GEN-LAST:event_mnuLoadCustomActionPerformed

   private void mnuDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDescriptionActionPerformed

      if (tblTemplates.getSelectedRow() == -1) {
         return;
      }

      TemplateItem t = (TemplateItem) tblTemplates.getValueAt(tblTemplates.getSelectedRow(), 1);
      if (t == null) {
         return;
      }

      JOptionPane.showMessageDialog(null, t.description, "Description of " + t.name, JOptionPane.INFORMATION_MESSAGE);

   }//GEN-LAST:event_mnuDescriptionActionPerformed

   private void frmRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frmRefreshActionPerformed
      LoadTableList();
   }//GEN-LAST:event_frmRefreshActionPerformed

   private void tblTemplatesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTemplatesMouseClicked
      if (evt.getButton() != MouseEvent.BUTTON1) {
         return;
      }

      if (evt.getClickCount() != 2) {
         return;
      }

      mnuLoadActionPerformed(null);

   }//GEN-LAST:event_tblTemplatesMouseClicked

   private void tblTemplatesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTemplatesKeyPressed

      if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
         return;
      }

      mnuLoadActionPerformed(null);

   }//GEN-LAST:event_tblTemplatesKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JMenuItem frmRefresh;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JPopupMenu.Separator jSeparator1;
   private javax.swing.JMenuItem mnuDescription;
   private javax.swing.JMenuItem mnuLoad;
   private javax.swing.JMenuItem mnuLoadCustom;
   private javax.swing.JPopupMenu mnuPopUp;
   private javax.swing.JMenuItem mnuUnload;
   private javax.swing.JTable tblTemplates;

   // End of variables declaration//GEN-END:variables

   private class TemplateItem {

      //visible info
      public boolean loaded = false;
      public String name = "";
      public String game = "";
      public String author = "";
      public String version = "";
      public String description = "";
      public String date = "";
      //backend
      public boolean sourceOnline = false; //found on server
      public String URL = ""; //URL to copy on server
      public boolean sourceLocal = false; //in template folder.
      public String path = "";
      //templatedata
      public Template template = null;
      public String templateID = "";

      public String toString() {
         return name;
      }
   }
}
