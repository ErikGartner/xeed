/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import xeed.Template;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class TemplateCreatorForm extends javax.swing.JFrame {

   DefaultMutableTreeNode rootNode;
   TreeItem rootItem;
   public TreeItem nameRow = null;

   public TemplateCreatorForm() {

      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/layout_add.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

      treeOverview.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      rootItem = new TreeItem();
      rootItem.boolRoot = true;
      rootItem.intType = 0;
      rootItem.szName = "Template";

      rootNode = new DefaultMutableTreeNode(rootItem);
      DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
      treeOverview.setModel(treeModel);
      treeOverview.setCellRenderer(new CustomRenderer(new javax.swing.ImageIcon(getClass().getResource(
            "/application_xp.png")), new javax.swing.ImageIcon(getClass().getResource("/layout.png")),
            new javax.swing.ImageIcon(getClass().getResource("/brick.png"))));

   }

   public Template GenerateTemplate() {

      Template t = new Template();
      for (int x = 0; x < rootNode.getChildCount(); x++) {

         TreeItem sectionObject = (TreeItem) ((DefaultMutableTreeNode) rootNode.getChildAt(x)).getUserObject();
         int SectionIndex = t.AddSection(sectionObject.szName);
         String szDataArray[];

         for (int y = 0; y < rootNode.getChildAt(x).getChildCount(); y++) {

            TreeItem ti = (TreeItem) ((DefaultMutableTreeNode) rootNode.getChildAt(x).getChildAt(y)).getUserObject();

            switch (ti.intType) {

            case 1: //TextRow
               t.AddTextRow(ti.szName, SectionIndex);
               break;

            case 2:
               t.AddTextBox(ti.szName, SectionIndex);
               break;

            case 3:
               szDataArray = ti.szData.split("\n");
               t.AddListRow(ti.szName, szDataArray, SectionIndex);
               break;

            case 4:
               t.AddIntegerRow(ti.szName, SectionIndex);
               break;

            case 5:
               t.AddIntegerADBCRow(ti.szName, SectionIndex);
               break;

            case 6:
               t.AddNameRow(ti.szName, SectionIndex);
               break;

            case 7:
               t.AddCheckboxRow(ti.szName, SectionIndex);
               break;

            case 8:
               t.AddTextBoxNoTitle(ti.szName, SectionIndex);
               break;

            case 9:
               t.AddTitle(ti.szName, SectionIndex);
               break;

            case 10:
               t.AddImageBox(ti.szName, SectionIndex);
               break;

            case 11:
               t.AddParentRow(ti.szName, SectionIndex);
               break;

            case 12:
               t.AddOffspringBox(ti.szName, SectionIndex);
               break;

            case 13:
               szDataArray = ti.szData.split("\n");
               t.AddExtendedRow(ti.szName, szDataArray, SectionIndex);
               break;

            case 14:
               szDataArray = ti.szData.split("\n");
               t.AddListBox(ti.szName, szDataArray, SectionIndex);
               break;

            }

         }

      }

      return t;

   }

   private boolean UploadTemplateToXEEDOnline(String szFile) {

      HttpClient httpclient = new DefaultHttpClient();
      httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
            "XEED v" + XEED.szVersion + " Build " + XEED.lngBuild + "." + XEED.GetXEEDCRC32());
      httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

      HttpPost httppost = new HttpPost(XEED.szTemplateUploadURL);
      File file = new File(szFile);

      MultipartEntity mpEntity = new MultipartEntity();
      ContentBody cbFile = new FileBody(file, "text/plain");
      mpEntity.addPart("template", cbFile);

      httppost.setEntity(mpEntity);
      System.out.println("Uploading template " + httppost.getRequestLine());

      try {

         HttpResponse response = httpclient.execute(httppost);

         HttpEntity resEntity = response.getEntity();

         System.out.println(response.getStatusLine());
         if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
         }
         if (resEntity != null) {
            EntityUtils.consume(resEntity);
         }

         httpclient.getConnectionManager().shutdown();

      } catch (Exception e) {
         return false;
      }
      chkSubmit.setSelected(false);
      return true;

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jButton1 = new javax.swing.JButton();
      pnlLayout = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      treeOverview = new javax.swing.JTree();
      btnAddItem = new javax.swing.JButton();
      btnAddSection = new javax.swing.JButton();
      btnRemove = new javax.swing.JButton();
      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      txtTemplateName = new javax.swing.JTextField();
      jLabel2 = new javax.swing.JLabel();
      txtAuthor = new javax.swing.JTextField();
      jLabel3 = new javax.swing.JLabel();
      txtVersion = new javax.swing.JTextField();
      chkSubmit = new javax.swing.JCheckBox();
      jLabel4 = new javax.swing.JLabel();
      txtGame = new javax.swing.JTextField();
      btnCreate = new javax.swing.JButton();
      jLabel5 = new javax.swing.JLabel();
      jScrollPane2 = new javax.swing.JScrollPane();
      txtDescription = new javax.swing.JTextArea();
      btnPreview = new javax.swing.JButton();

      jButton1.setText("jButton1");

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("Template Creator");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      pnlLayout.setBorder(javax.swing.BorderFactory.createTitledBorder("Layout Hierarchy"));

      treeOverview.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            treeOverviewMouseClicked(evt);
         }
      });
      jScrollPane1.setViewportView(treeOverview);

      btnAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/brick_add.png"))); // NOI18N
      btnAddItem.setToolTipText("Add item");
      btnAddItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAddItemActionPerformed(evt);
         }
      });

      btnAddSection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/layout_add.png"))); // NOI18N
      btnAddSection.setToolTipText("Add section");
      btnAddSection.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAddSectionActionPerformed(evt);
         }
      });

      btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
      btnRemove.setToolTipText("Remove item or section");
      btnRemove.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnRemoveActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout pnlLayoutLayout = new javax.swing.GroupLayout(pnlLayout);
      pnlLayout.setLayout(pnlLayoutLayout);
      pnlLayoutLayout.setHorizontalGroup(pnlLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  pnlLayoutLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              pnlLayoutLayout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(
                                          pnlLayoutLayout
                                                .createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                      159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.TRAILING,
                                          pnlLayoutLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnAddItem)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAddSection)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnRemove))).addContainerGap()));
      pnlLayoutLayout.setVerticalGroup(pnlLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  pnlLayoutLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                              pnlLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddItem, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnAddSection, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnRemove, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap()));

      jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Template Settings"));

      jLabel1.setText("Name");

      txtTemplateName.setText("Template");
      txtTemplateName.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtTemplateNameKeyReleased(evt);
         }
      });

      jLabel2.setText("Author");

      jLabel3.setText("Template Version");

      chkSubmit.setText("Submit to XEED Online");
      chkSubmit
            .setToolTipText("Submit your template to the online database. Your template will manually be reviewed by the developement crew.");

      jLabel4.setText("Game/System");

      btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cog_go.png"))); // NOI18N
      btnCreate.setText("Create");
      btnCreate.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCreateActionPerformed(evt);
         }
      });

      jLabel5.setText("Description");

      txtDescription.setColumns(20);
      txtDescription.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtDescription.setLineWrap(true);
      txtDescription.setRows(5);
      txtDescription.setWrapStyleWord(true);
      jScrollPane2.setViewportView(txtDescription);

      btnPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application_go.png"))); // NOI18N
      btnPreview.setText("Preview");
      btnPreview.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnPreviewActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout
            .setHorizontalGroup(jPanel1Layout
                  .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(
                        jPanel1Layout
                              .createSequentialGroup()
                              .addGroup(
                                    jPanel1Layout
                                          .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                          .addGroup(
                                                jPanel1Layout
                                                      .createSequentialGroup()
                                                      .addGroup(
                                                            jPanel1Layout
                                                                  .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                  .addGroup(
                                                                        jPanel1Layout.createSequentialGroup()
                                                                              .addGap(10, 10, 10).addComponent(jLabel1))
                                                                  .addGroup(
                                                                        jPanel1Layout.createSequentialGroup()
                                                                              .addContainerGap().addComponent(jLabel4))
                                                                  .addGroup(
                                                                        jPanel1Layout.createSequentialGroup()
                                                                              .addContainerGap().addComponent(jLabel2))
                                                                  .addGroup(
                                                                        jPanel1Layout
                                                                              .createSequentialGroup()
                                                                              .addContainerGap()
                                                                              .addComponent(
                                                                                    jLabel3,
                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                    Short.MAX_VALUE))
                                                                  .addGroup(
                                                                        jPanel1Layout.createSequentialGroup()
                                                                              .addContainerGap()
                                                                              .addComponent(txtVersion)))
                                                      .addGap(18, 18, 18).addComponent(chkSubmit)
                                                      .addGap(0, 32, Short.MAX_VALUE))
                                          .addGroup(
                                                jPanel1Layout
                                                      .createSequentialGroup()
                                                      .addContainerGap()
                                                      .addGroup(
                                                            jPanel1Layout
                                                                  .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                  .addComponent(txtTemplateName)
                                                                  .addComponent(txtGame,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                  .addComponent(txtAuthor,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                  .addComponent(jScrollPane2,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                  .addGroup(
                                                                        jPanel1Layout.createSequentialGroup()
                                                                              .addComponent(jLabel5)
                                                                              .addGap(0, 0, Short.MAX_VALUE))
                                                                  .addGroup(
                                                                        jPanel1Layout
                                                                              .createSequentialGroup()
                                                                              .addGap(0, 0, Short.MAX_VALUE)
                                                                              .addComponent(btnPreview)
                                                                              .addPreferredGap(
                                                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                              .addComponent(btnCreate)))))
                              .addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTemplateName, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGame, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                              jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtVersion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkSubmit))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                              jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnCreate).addComponent(btnPreview)).addContainerGap()));

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlLayout, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addComponent(pnlLayout, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void btnAddSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSectionActionPerformed

      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeOverview.getLastSelectedPathComponent();
      if (selectedNode == null) {
         return;
      }

      if (!selectedNode.getAllowsChildren()) { //if the selected node is an item 
         selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
      }

      if (selectedNode == null) {
         return;
      }

      TreeItem ti = new TreeItem();
      ti.intType = 0; //Section type id
      ti.szName = JOptionPane.showInputDialog(null, "Enter the name of the new section:", "Enter name",
            JOptionPane.PLAIN_MESSAGE);
      if (ti.szName == null) {
         return;
      }
      if (ti.szName.isEmpty()) {
         return;
      }

      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ti);
      newNode.setAllowsChildren(true);

      if (selectedNode.isRoot()) { //inserts node after selected node
         rootNode.insert(newNode, 0);
      } else {
         rootNode.insert(newNode, rootNode.getIndex(selectedNode) + 1);
      }

      for (int i = 0; i < treeOverview.getRowCount(); i++) {
         treeOverview.expandRow(i);
      }

      treeOverview.setSelectionPath(new TreePath(newNode.getPath()));
      UpdateTree();

   }//GEN-LAST:event_btnAddSectionActionPerformed

   private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed

      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeOverview.getLastSelectedPathComponent();
      if (selectedNode == null) {
         return;
      }

      if (selectedNode.isRoot()) {
         JOptionPane.showMessageDialog(null,
               "An item must be subsidiary to a section, please select a section or item to insert below.",
               "Invalid node selected!", JOptionPane.WARNING_MESSAGE);
         return;
      }

      TreeItem ti = new TreeItem();
      ti.szName = "New Item";
      ti.intType = 1; //anything but 0 so that it gets the right icon.
      if (ti.szName == null) {
         return;
      }
      if (ti.szName.isEmpty()) {
         return;
      }

      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ti);
      newNode.setAllowsChildren(false);
      if (!selectedNode.getAllowsChildren()) {
         DefaultMutableTreeNode sectionNode = (DefaultMutableTreeNode) selectedNode.getParent();
         sectionNode.insert(newNode, sectionNode.getIndex(selectedNode) + 1);

      } else {
         selectedNode.insert(newNode, 0);
      }

      this.setEnabled(false);
      TemplateItemPalettForm dp = new TemplateItemPalettForm(this, newNode, false);
      dp.setVisible(true);

      for (int i = 0; i < treeOverview.getRowCount(); i++) {
         treeOverview.expandRow(i);
      }

      treeOverview.setSelectionPath(new TreePath(newNode.getPath()));
      UpdateTree();

   }//GEN-LAST:event_btnAddItemActionPerformed

   private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed

      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeOverview.getLastSelectedPathComponent();
      if (selectedNode == null) {
         return;
      }

      if (selectedNode.isRoot()) {
         return;
      }

      TreeItem selectedObject = (TreeItem) selectedNode.getUserObject();
      if (selectedObject.intType == 6) {
         nameRow = null;
      }

      selectedNode.removeFromParent();
      UpdateTree();

   }//GEN-LAST:event_btnRemoveActionPerformed

   private void txtTemplateNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTemplateNameKeyReleased
      rootItem.szName = txtTemplateName.getText();
      treeOverview.repaint();
      treeOverview.updateUI();
   }//GEN-LAST:event_txtTemplateNameKeyReleased

   private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed

      Template t = GenerateTemplate();
      t.PreviewTemplate(txtTemplateName.getText());

   }//GEN-LAST:event_btnPreviewActionPerformed

   private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed

      if (txtTemplateName.getText().isEmpty() || txtGame.getText().isEmpty() || txtAuthor.getText().isEmpty()
            || txtDescription.getText().isEmpty()) {
         JOptionPane.showMessageDialog(null, "All fields are required", "Missing data", JOptionPane.WARNING_MESSAGE);
         return;
      }

      int i;
      try {
         i = Integer.parseInt(txtVersion.getText());
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Version must be a valid integer and a postivive value greater than 0",
               "Invalid data", JOptionPane.WARNING_MESSAGE);
         return;
      }

      if (nameRow == null) {
         JOptionPane.showMessageDialog(null, "A template MUST contain 1 'Name Row'", "Missing data",
               JOptionPane.WARNING_MESSAGE);
         return;
      }

      setEnabled(false);

      Template t = GenerateTemplate();
      if (t.FinalizeTemplate(txtTemplateName.getText(), txtAuthor.getText(), txtGame.getText(),
            txtDescription.getText(), i)) {

         XEED.LoadTemplate(t);

         String msg = "Template sucessfully created!";
         if (chkSubmit.isSelected()) {
            if (UploadTemplateToXEEDOnline(t.GetFilePath())) {
               msg += "\nThank you for submitting your template to XEED Online.\nYour template will be manually reviewed by the development team before being added to the public database.";
            } else {
               msg += "\nFailed to submited template to XEED online.";
            }
         }

         JOptionPane.showMessageDialog(null, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
      }

      setEnabled(true);

   }//GEN-LAST:event_btnCreateActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      XEED.hwndTemplateCreator = null;
      dispose();
   }//GEN-LAST:event_formWindowClosing

   private void treeOverviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeOverviewMouseClicked

      //Edit selected node
      if (evt.getButton() != MouseEvent.BUTTON1 || evt.getClickCount() != 2) {
         return;
      }

      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeOverview.getLastSelectedPathComponent();
      if (selectedNode == null) {
         return;
      }

      if (selectedNode.isRoot()) {
         return;
      }

      TreeItem ti = (TreeItem) selectedNode.getUserObject();

      if (ti.intType == 0) { //section

         String szName = JOptionPane.showInputDialog("Select the new name for the section '" + ti.szName + "'",
               ti.szName);
         if (szName != null) {
            if (!szName.isEmpty()) {
               ti.szName = szName;
            }
         }
         UpdateTree();

      } else { //property

         this.setEnabled(false);
         TemplateItemPalettForm dp = new TemplateItemPalettForm(this, selectedNode, true);
         dp.setVisible(true);

         selectedNode.removeFromParent();

      }

   }//GEN-LAST:event_treeOverviewMouseClicked

   public void ReturnFocus() {
      this.setEnabled(true);
   }

   public void UpdateTree() {
      treeOverview.updateUI();
      treeOverview.repaint();
   }

   public class TreeItem {

      String szName;
      String szData;
      int intType;
      boolean boolRoot = false;

      @Override
      public String toString() {
         return szName;
      }
   }

   private class CustomRenderer extends DefaultTreeCellRenderer {

      Icon iconSection;
      Icon iconItem;
      Icon iconRoot;

      public CustomRenderer(Icon root, Icon section, Icon item) {
         iconSection = section;
         iconItem = item;
         iconRoot = root;
      }

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {

         super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
         if (isRoot(value)) {
            setIcon(iconRoot);
         } else {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            TreeItem nodeInfo = (TreeItem) (node.getUserObject());
            if (nodeInfo.intType == 0) {
               setIcon(iconSection);
            } else {
               setIcon(iconItem);
            }
         }

         return this;
      }

      protected boolean isRoot(Object value) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
         TreeItem nodeInfo = (TreeItem) (node.getUserObject());
         return nodeInfo.boolRoot;
      }
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton btnAddItem;
   private javax.swing.JButton btnAddSection;
   private javax.swing.JButton btnCreate;
   private javax.swing.JButton btnPreview;
   private javax.swing.JButton btnRemove;
   private javax.swing.JCheckBox chkSubmit;
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JPanel pnlLayout;
   private javax.swing.JTree treeOverview;
   private javax.swing.JTextField txtAuthor;
   private javax.swing.JTextArea txtDescription;
   private javax.swing.JTextField txtGame;
   private javax.swing.JTextField txtTemplateName;
   private javax.swing.JTextField txtVersion;
   // End of variables declaration//GEN-END:variables
}
