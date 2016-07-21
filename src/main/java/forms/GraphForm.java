package forms;

import xeed.*;
import xeed.Character;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public final class GraphForm extends javax.swing.JFrame {

   /*
    * Relations character table
    */
   private Vector jTableModelRelChar = new Vector(0, 2);
   private Vector jTableHeaderRelChar = new Vector(0);
   private RelationGraphPanel RelationPanel = null;
   private GenealogyGraphPanel GenealogyPanel = null;
   /*
    * Relations groups table
    */
   private Vector jTableModelRelGroup = new Vector(0, 2);
   private Vector jTableHeaderRelGroup = new Vector(0);
   /*
    * Genealogy character table
    */
   private Vector jTableModelGenChar = new Vector(0, 2);
   private Vector jTableHeaderGenChar = new Vector(0);
   private boolean reloading_template_options = false;

   /**
    * Creates new form frmGraph
    */
   public GraphForm() {
      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/chart_organisation.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

      LoadCharactersAndGroups();
      JustifyColumns();

      LoadTemplates();
   }

   private void JustifyColumns() {

      tblRelChars.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      tblRelChars.getColumnModel().getColumn(0).setResizable(false);
      tblRelChars.getColumnModel().getColumn(0).setPreferredWidth(20);
      tblRelChars.getColumnModel().getColumn(1).setPreferredWidth(tblRelChars.getWidth() - 20);

      tblRelGroups.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      tblRelGroups.getColumnModel().getColumn(0).setResizable(false);
      tblRelGroups.getColumnModel().getColumn(0).setPreferredWidth(20);
      tblRelGroups.getColumnModel().getColumn(1).setPreferredWidth(tblRelGroups.getWidth() - 20);

      tblGenChars.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      tblGenChars.getColumnModel().getColumn(0).setResizable(false);
      tblGenChars.getColumnModel().getColumn(0).setPreferredWidth(20);
      tblGenChars.getColumnModel().getColumn(1).setPreferredWidth(tblGenChars.getWidth() - 20);

   }

   public void LoadTemplates() {

      comboGenTemplate.removeAllItems();
      for (int x = 0; x < XEED.templateDB.size(); x++) {
         comboGenTemplate.addItem(new TemplateOption(XEED.templateDB.get(x)));
      }

   }

   private void LoadGenTemplateOptions() {

      reloading_template_options = true;

      comboGenExtra.removeAllItems();
      comboGenExtra.addItem(new PropertyItem());
      comboGenPicture.removeAllItems();
      comboGenPicture.addItem(new PropertyItem());

      TemplateOption to = (TemplateOption) comboGenTemplate.getSelectedItem();
      lblGenColor.setBackground(to.c);

      String[] keys = to.t.GetAllTemplateKeys();
      String[] names = to.t.GetAllTemplateNames();

      for (int x = 0; x < keys.length; x++) {

         boolean[] bs = to.t.GetDataTypeArray(keys[x]);
         if (bs[3]) {
            PropertyItem pi = new PropertyItem();
            pi.key = keys[x];
            pi.name = names[x];
            comboGenExtra.addItem(pi);

            if (pi.key.equals(to.extra.key)) {
               comboGenExtra.setSelectedItem(pi);
            }
         }

         if (bs[2]) {
            PropertyItem pi = new PropertyItem();
            pi.key = keys[x];
            pi.name = names[x];
            comboGenPicture.addItem(pi);

            if (pi.key.equals(to.picture.key)) {
               comboGenPicture.setSelectedItem(pi);
            }
         }
      }

      reloading_template_options = false;

   }

   public void LoadCharactersAndGroups() {

      /*
       * Store selection
       */

      ArrayList<xeed.Character> selectedrelchars = new ArrayList(0);
      for (int x = 0; x < jTableModelRelChar.size(); x++) {
         Vector o = (Vector) jTableModelRelChar.get(x);
         if ((Boolean) o.get(0) == true) {
            selectedrelchars.add((Character) o.get(1));
         }
      }

      ArrayList<Group> selectedgroups = new ArrayList(0);
      for (int x = 0; x < jTableModelRelGroup.size(); x++) {
         Vector o = (Vector) jTableModelRelGroup.get(x);
         if ((Boolean) o.get(0) == true) {
            selectedgroups.add((Group) o.get(1));
         }
      }

      ArrayList<Character> selectedgenchars = new ArrayList(0);
      for (int x = 0; x < jTableModelGenChar.size(); x++) {
         Vector o = (Vector) jTableModelGenChar.get(x);
         if ((Boolean) o.get(0) == true) {
            selectedgenchars.add((Character) o.get(1));
         }
      }

      jTableModelRelChar.clear();
      jTableModelRelGroup.clear();
      jTableModelGenChar.clear();

      for (int x = 0; x < XEED.charDB.size(); x++) {
         Vector o2 = new Vector(0);
         o2.add(selectedrelchars.contains(XEED.charDB.get(x)));
         o2.add(XEED.charDB.get(x));
         jTableModelRelChar.add(o2);
      }

      for (int x = 0; x < XEED.groupDB.size(); x++) {
         Vector o = new Vector(0);
         o.add(selectedgroups.contains(XEED.groupDB.get(x)));
         o.add(XEED.groupDB.get(x));
         jTableModelRelGroup.add(o);
      }

      for (int x = 0; x < XEED.charDB.size(); x++) {
         Vector o2 = new Vector(0);
         o2.add(jTableModelGenChar.contains(XEED.charDB.get(x)));
         o2.add(XEED.charDB.get(x));
         jTableModelGenChar.add(o2);
      }

      DefaultTableModel df;
      df = (DefaultTableModel) tblRelChars.getModel();
      df.fireTableDataChanged();
      df = (DefaultTableModel) tblRelGroups.getModel();
      df.fireTableDataChanged();
      df = (DefaultTableModel) tblGenChars.getModel();
      df.fireTableDataChanged();

   }

   private void LoadRelationGraph() {

      UnloadAllGraphs();

      RelationPanel = new RelationGraphPanel();
      RelationPanel.setSize(500, 500);
      pnlGraph.add(RelationPanel, BorderLayout.CENTER);
      pnlGraph.validate();

   }

   private void LoadGenealogyGraph() {

      UnloadAllGraphs();

      ArrayList<Character> selectedgenchars = new ArrayList(0);
      for (int x = 0; x < jTableModelGenChar.size(); x++) {
         Vector o = (Vector) jTableModelGenChar.get(x);
         if ((Boolean) o.get(0) == true) {
            selectedgenchars.add((Character) o.get(1));
         }
      }

      Character[] cs = new Character[selectedgenchars.size()];
      selectedgenchars.toArray(cs);

      Template[] ts = new Template[comboGenTemplate.getItemCount()];
      Color[] c = new Color[comboGenTemplate.getItemCount()];
      String[] pics = new String[comboGenTemplate.getItemCount()];
      String[] extras = new String[comboGenTemplate.getItemCount()];
      for (int x = 0; x < ts.length; x++) {
         TemplateOption to = (TemplateOption) comboGenTemplate.getItemAt(x);
         ts[x] = to.t;
         c[x] = to.c;
         pics[x] = to.picture.key;
         extras[x] = to.extra.key;
      }

      if (cs.length > 0 && ts.length > 0) {

         GenealogyPanel = new GenealogyGraphPanel();
         GenealogyPanel.setSize(500, 500);
         pnlGraph.add(GenealogyPanel, BorderLayout.CENTER);
         pnlGraph.validate();
         GenealogyPanel.InitiateGraph(cs, ts, c, pics, extras);
         GenealogyPanel.setVisible(true);

      }

   }

   private void UnloadAllGraphs() {
      pnlGraph.removeAll();
      RelationPanel = null;
      GenealogyPanel = null;
      pnlGraph.validate();
   }

   private void UpdateRelations() {
      if (RelationPanel == null) {
         return;
      }

      Group[] g = null;
      Character[] c = null;

      ArrayList<Character> tmp = new ArrayList(0);
      for (int x = 0; x < jTableModelRelChar.size(); x++) {
         Vector o = (Vector) jTableModelRelChar.get(x);
         if ((Boolean) o.get(0) == true) {
            tmp.add((Character) o.get(1));
         }
      }

      ArrayList<Group> tmp2 = new ArrayList(0);
      for (int x = 0; x < jTableModelRelGroup.size(); x++) {
         Vector o = (Vector) jTableModelRelGroup.get(x);
         if ((Boolean) o.get(0) == true) {

            if (chkUseMembers.isSelected()) {
               long[] members = ((Group) o.get(1)).GetMemebers();
               for (int y = 0; y < members.length; y++) {
                  Character member = XEED.GetCharacterByID(members[y]);
                  if (!tmp.contains(member)) {
                     tmp.add(member);
                  }
               }
            } else {
               tmp2.add((Group) o.get(1));
            }

         }
      }
      if (tmp.size() > 0) {
         c = new Character[tmp.size()];
         tmp.toArray(c);
      } else {
         c = null;
      }
      if (tmp2.size() > 0) {
         g = new Group[tmp2.size()];
         tmp2.toArray(g);
      } else {
         g = null;
      }
      if (tmp.size() + tmp2.size() > 1) {
         RelationPanel.LoadData(c, g);
         RelationPanel.setVisible(true);
      } else {
         RelationPanel.setVisible(false);
      }
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jColorChooser1 = new javax.swing.JColorChooser();
      jTabbedPane1 = new javax.swing.JTabbedPane();
      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jScrollPane4 = new javax.swing.JScrollPane();
      tblGenChars = new javax.swing.JTable();
      jPanel3 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      comboGenTemplate = new javax.swing.JComboBox();
      jLabel5 = new javax.swing.JLabel();
      lblGenColor = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      comboGenPicture = new javax.swing.JComboBox();
      jLabel8 = new javax.swing.JLabel();
      comboGenExtra = new javax.swing.JComboBox();
      jPanel2 = new javax.swing.JPanel();
      jLabel3 = new javax.swing.JLabel();
      jScrollPane2 = new javax.swing.JScrollPane();
      tblRelChars = new javax.swing.JTable();
      jScrollPane3 = new javax.swing.JScrollPane();
      tblRelGroups = new javax.swing.JTable();
      jLabel4 = new javax.swing.JLabel();
      chkUseMembers = new javax.swing.JCheckBox();
      pnlGraph = new javax.swing.JPanel();

      jColorChooser1.setName("jColorChooser1"); // NOI18N

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("Graphs");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      jTabbedPane1.setName("jTabbedPane1"); // NOI18N
      jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
         public void stateChanged(javax.swing.event.ChangeEvent evt) {
            jTabbedPane1StateChanged(evt);
         }
      });

      jPanel1.setName("jPanel1");

      jLabel1.setText("Characters");
      jLabel1.setName("jLabel1");

      jScrollPane4.setName("jScrollPane4");

      jTableHeaderGenChar.add("");
      jTableHeaderGenChar.add("Name");
      tblGenChars.setModel(new javax.swing.table.DefaultTableModel(jTableModelGenChar, jTableHeaderGenChar) {
         Class[] types = new Class[] { java.lang.Boolean.class, Object.class };

         public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int mColIndex) {
            if (mColIndex != 0) {
               return false;
            } else {
               return true;
            }
         }
      });
      tblGenChars.setName("tblGenChars");
      tblGenChars.setShowHorizontalLines(false);
      tblGenChars.setShowVerticalLines(false);
      tblGenChars.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
         public void propertyChange(java.beans.PropertyChangeEvent evt) {
            tblGenCharsPropertyChange(evt);
         }
      });
      jScrollPane4.setViewportView(tblGenChars);

      jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Template Options"));
      jPanel3.setName("jPanel3");

      jLabel2.setText("Template");
      jLabel2.setName("jLabel2");

      comboGenTemplate.setName("comboGenTemplate");
      comboGenTemplate.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboGenTemplateActionPerformed(evt);
         }
      });

      jLabel5.setText("Node colour");
      jLabel5.setName("jLabel5");

      lblGenColor.setBackground(new java.awt.Color(255, 0, 0));
      lblGenColor.setName("lblGenColor");
      lblGenColor.setOpaque(true);
      lblGenColor.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            lblGenColorMouseClicked(evt);
         }
      });

      jLabel7.setText("Picture");
      jLabel7.setName("jLabel7");

      comboGenPicture.setName("comboGenPicture");
      comboGenPicture.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboGenPictureActionPerformed(evt);
         }
      });

      jLabel8.setText("Extra data");
      jLabel8.setName("jLabel8");

      comboGenExtra.setName("comboGenExtra");
      comboGenExtra.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboGenExtraActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              jPanel3Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGenColor, javax.swing.GroupLayout.Alignment.TRAILING,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          Short.MAX_VALUE)
                                    .addComponent(comboGenTemplate, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          Short.MAX_VALUE)
                                    .addComponent(comboGenPicture, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          Short.MAX_VALUE)
                                    .addGroup(
                                          jPanel3Layout
                                                .createSequentialGroup()
                                                .addGroup(
                                                      jPanel3Layout
                                                            .createParallelGroup(
                                                                  javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel2).addComponent(jLabel5)
                                                            .addComponent(jLabel7).addComponent(jLabel8))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(comboGenExtra, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          Short.MAX_VALUE)).addContainerGap()));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboGenTemplate, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGenColor, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                              javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboGenPicture, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboGenExtra, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  javax.swing.GroupLayout.Alignment.TRAILING,
                  jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING,
                                          javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                                    .addGroup(
                                          javax.swing.GroupLayout.Alignment.LEADING,
                                          jPanel1Layout.createSequentialGroup().addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()));

      jTabbedPane1.addTab("Genealogy", jPanel1);

      jPanel2.setName("jPanel2"); // NOI18N

      jLabel3.setText("Characters");
      jLabel3.setName("jLabel3"); // NOI18N

      jScrollPane2.setName("jScrollPane2"); // NOI18N

      jTableHeaderRelChar.add("");
      jTableHeaderRelChar.add("Name");
      tblRelChars.setModel(new javax.swing.table.DefaultTableModel(jTableModelRelChar, jTableHeaderRelChar) {
         Class[] types = new Class[] { java.lang.Boolean.class, Object.class };

         public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int mColIndex) {
            if (mColIndex != 0) {
               return false;
            } else {
               return true;
            }
         }
      });
      tblRelChars.setName("tblRelChars"); // NOI18N
      tblRelChars.setShowHorizontalLines(false);
      tblRelChars.setShowVerticalLines(false);
      tblRelChars.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
         public void propertyChange(java.beans.PropertyChangeEvent evt) {
            tblRelCharsPropertyChange(evt);
         }
      });
      jScrollPane2.setViewportView(tblRelChars);

      jScrollPane3.setName("jScrollPane3"); // NOI18N

      jTableHeaderRelGroup.add("");
      jTableHeaderRelGroup.add("Name");
      tblRelGroups.setModel(new javax.swing.table.DefaultTableModel(jTableModelRelGroup, jTableHeaderRelGroup) {
         Class[] types = new Class[] { java.lang.Boolean.class, Object.class };

         public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int mColIndex) {
            if (mColIndex != 0) {
               return false;
            } else {
               return true;
            }
         }
      });
      tblRelGroups.setName("tblRelGroups"); // NOI18N
      tblRelGroups.setShowHorizontalLines(false);
      tblRelGroups.setShowVerticalLines(false);
      tblRelGroups.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
         public void propertyChange(java.beans.PropertyChangeEvent evt) {
            tblRelGroupsPropertyChange(evt);
         }
      });
      jScrollPane3.setViewportView(tblRelGroups);

      jLabel4.setText("Groups");
      jLabel4.setName("jLabel4"); // NOI18N

      chkUseMembers.setText("Replace group with its members");
      chkUseMembers
            .setToolTipText("Instead of displaying the group as an object its members are. This is useful if you want to view the relations within the group fast by selecting the group.");
      chkUseMembers.setName("chkUseMembers"); // NOI18N
      chkUseMembers.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            chkUseMembersActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel2Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              jPanel2Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkUseMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 182,
                                          Short.MAX_VALUE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                          Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                          Short.MAX_VALUE)
                                    .addGroup(
                                          jPanel2Layout
                                                .createSequentialGroup()
                                                .addGroup(
                                                      jPanel2Layout
                                                            .createParallelGroup(
                                                                  javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel3).addComponent(jLabel4))
                                                .addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkUseMembers).addContainerGap()));

      jTabbedPane1.addTab("Relations", jPanel2);

      pnlGraph.setName("pnlGraph"); // NOI18N
      pnlGraph.setLayout(new java.awt.BorderLayout());

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207,
                              javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
                        .addComponent(pnlGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
                        .addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            javax.swing.GroupLayout.Alignment.TRAILING,
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                              .addComponent(jTabbedPane1)
                              .addComponent(pnlGraph, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
      JTabbedPane pane = (JTabbedPane) evt.getSource();

      int sel = pane.getSelectedIndex();
      if (sel == 1) {
         LoadRelationGraph();
         UpdateRelations();
      } else if (sel == 0) {
         LoadGenealogyGraph();
      }
   }//GEN-LAST:event_jTabbedPane1StateChanged

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      XEED.hwndGraph = null;
      dispose();
   }//GEN-LAST:event_formWindowClosing

   private void tblRelCharsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRelCharsPropertyChange
      UpdateRelations();
   }//GEN-LAST:event_tblRelCharsPropertyChange

   private void tblRelGroupsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRelGroupsPropertyChange
      UpdateRelations();
   }//GEN-LAST:event_tblRelGroupsPropertyChange

   private void chkUseMembersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUseMembersActionPerformed
      UpdateRelations();
   }//GEN-LAST:event_chkUseMembersActionPerformed

   private void tblGenCharsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblGenCharsPropertyChange
      LoadGenealogyGraph();
   }//GEN-LAST:event_tblGenCharsPropertyChange

   private void lblGenColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGenColorMouseClicked

      TemplateOption to = (TemplateOption) comboGenTemplate.getSelectedItem();
      if (to == null) {
         return;
      }

      JColorChooser cc = new JColorChooser();
      Color c = JColorChooser.showDialog(null, "Select node colour", Color.RED);
      lblGenColor.setBackground(c);
      to.c = c;

      LoadGenealogyGraph();

   }//GEN-LAST:event_lblGenColorMouseClicked

   private void comboGenTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGenTemplateActionPerformed
      LoadGenTemplateOptions();
   }//GEN-LAST:event_comboGenTemplateActionPerformed

   private void comboGenPictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGenPictureActionPerformed

      if (reloading_template_options) {
         return;
      }

      TemplateOption to = (TemplateOption) comboGenTemplate.getSelectedItem();
      to.picture = (PropertyItem) comboGenPicture.getSelectedItem();
      LoadGenealogyGraph();

   }//GEN-LAST:event_comboGenPictureActionPerformed

   private void comboGenExtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGenExtraActionPerformed

      if (reloading_template_options) {
         return;
      }

      TemplateOption to = (TemplateOption) comboGenTemplate.getSelectedItem();
      to.extra = (PropertyItem) comboGenExtra.getSelectedItem();
      LoadGenealogyGraph();

   }//GEN-LAST:event_comboGenExtraActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JCheckBox chkUseMembers;
   private javax.swing.JComboBox comboGenExtra;
   private javax.swing.JComboBox comboGenPicture;
   private javax.swing.JComboBox comboGenTemplate;
   private javax.swing.JColorChooser jColorChooser1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JScrollPane jScrollPane4;
   private javax.swing.JTabbedPane jTabbedPane1;
   private javax.swing.JLabel lblGenColor;
   private javax.swing.JPanel pnlGraph;
   private javax.swing.JTable tblGenChars;
   private javax.swing.JTable tblRelChars;
   private javax.swing.JTable tblRelGroups;

   // End of variables declaration//GEN-END:variables

   class TemplateOption {

      Template t;
      Color c = Color.RED;
      PropertyItem picture = new PropertyItem();
      PropertyItem extra = new PropertyItem();

      public TemplateOption(Template t) {
         this.t = t;
      }

      @Override
      public String toString() {
         return t.GetName();
      }
   }

   class PropertyItem {

      String name = "";
      String key = "";

      @Override
      public String toString() {
         return name;
      }
   }
}
