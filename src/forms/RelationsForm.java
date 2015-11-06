/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmRelations.java
 *
 * Created on 2011-feb-10, 19:19:16
 */
package forms;

import xeed.*;
import xeed.Character;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Erik
 */
public class RelationsForm extends javax.swing.JFrame {

   /**
    * Creates new form frmRelations
    */
   public RelationsForm() {
      initComponents();

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/group_link.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

      LoadLists();
   }

   public void LoadLists() {

      Object o1 = comboFrom.getSelectedItem();
      comboFrom.removeAllItems();
      if (XEED.charDB.size() > 0) {
         comboFrom.addItem("");
         comboFrom.addItem("Characters:");
         for (int x = 0; x < XEED.charDB.size(); x++) {
            comboFrom.addItem(XEED.charDB.get(x));
         }
      }
      if (XEED.groupDB.size() > 0) {
         comboFrom.addItem("");
         comboFrom.addItem("Groups:");
         for (int x = 0; x < XEED.groupDB.size(); x++) {
            comboFrom.addItem(XEED.groupDB.get(x));
         }
      }
      comboFrom.setSelectedItem(o1);

      Object o2 = comboTo.getSelectedItem();
      comboTo.removeAllItems();
      if (XEED.charDB.size() > 0) {
         comboTo.addItem("");
         comboTo.addItem("Characters:");
         for (int x = 0; x < XEED.charDB.size(); x++) {
            comboTo.addItem(XEED.charDB.get(x));
         }
      }
      if (XEED.groupDB.size() > 0) {
         comboTo.addItem("");
         comboTo.addItem("Groups:");
         for (int x = 0; x < XEED.groupDB.size(); x++) {
            comboTo.addItem(XEED.groupDB.get(x));
         }
      }
      comboTo.setSelectedItem(o2);

   }

   public void LoadRelationship() {

      Object o1 = comboFrom.getSelectedItem();
      Object o2 = comboTo.getSelectedItem();

      if (o1 == null || o2 == null) {
         lblTo.setText("");
         lblFrom.setText("");
         txtDescriptionTo.setText("");
         txtDescriptionTo.setEditable(false);
         txtDescriptionFrom.setText("");
         txtDescriptionFrom.setEditable(false);
         return;
      }

      if (o1.getClass() == String.class || o2.getClass() == String.class) {
         lblTo.setText("");
         lblFrom.setText("");
         txtDescriptionTo.setText("");
         txtDescriptionTo.setEditable(false);
         txtDescriptionFrom.setText("");
         txtDescriptionFrom.setEditable(false);
         return;
      }

      if (o1 == o2) {
         lblTo.setText("");
         lblFrom.setText("");
         txtDescriptionTo.setText("");
         txtDescriptionTo.setEditable(false);
         txtDescriptionFrom.setText("");
         txtDescriptionFrom.setEditable(false);
         return;
      }

      if (o1.getClass() == xeed.Character.class) {
         if (o2.getClass() == Character.class) {

            Character c1 = (Character) o1;
            Character c2 = (Character) o2;

            lblTo.setText(c2.GetCharacterName());
            txtDescriptionTo.setText(c1.GetRelation(c2.characterID, 0));
            lblFrom.setText(c1.GetCharacterName());
            txtDescriptionFrom.setText(c2.GetRelation(c1.characterID, 0));

         } else if (o2.getClass() == Group.class) {

            Character c1 = (Character) o1;
            Group g2 = (Group) o2;
            lblTo.setText(g2.szName);
            txtDescriptionTo.setText(c1.GetRelation(g2.lngID, 1));
            lblFrom.setText(c1.GetCharacterName());
            txtDescriptionFrom.setText(g2.GetRelation(c1.characterID, 0));

         }
      } else if (o1.getClass() == Group.class) {
         if (o2.getClass() == Character.class) {

            Group g1 = (Group) o1;
            Character c2 = (Character) o2;
            lblTo.setText(c2.GetCharacterName());
            txtDescriptionTo.setText(g1.GetRelation(c2.characterID, 0));
            lblFrom.setText(g1.szName);
            txtDescriptionFrom.setText(c2.GetRelation(g1.lngID, 1));

         } else if (o2.getClass() == Group.class) {

            Group g1 = (Group) o1;
            Group g2 = (Group) o2;
            lblTo.setText(g1.szName + g2.szName);
            txtDescriptionTo.setText(g1.GetRelation(g2.lngID, 1));
            lblFrom.setText(g2.szName + g1.szName);
            txtDescriptionFrom.setText(g2.GetRelation(g1.lngID, 1));

         }
      }

      txtDescriptionFrom.setEditable(true);
      txtDescriptionTo.setEditable(true);
      txtDescriptionTo.setCaretPosition(0);
      txtDescriptionFrom.setCaretPosition(0);

   }

   public void LoadSpecificRelation(Object o1, Object o2) {

      LoadLists();
      comboFrom.setSelectedItem(o1);
      comboTo.setSelectedItem(o2);

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLabel3 = new javax.swing.JLabel();
      jScrollPane3 = new javax.swing.JScrollPane();
      txtDescriptionTo = new javax.swing.JTextArea();
      jScrollPane5 = new javax.swing.JScrollPane();
      txtDescriptionFrom = new javax.swing.JTextArea();
      comboFrom = new javax.swing.JComboBox();
      comboTo = new javax.swing.JComboBox();
      lblFrom = new javax.swing.JLabel();
      lblTo = new javax.swing.JLabel();

      jLabel3.setText("jLabel3");
      jLabel3.setName("jLabel3"); // NOI18N

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("Relations Manager");
      setLocationByPlatform(true);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      jScrollPane3.setName("jScrollPane3"); // NOI18N

      txtDescriptionTo.setColumns(20);
      txtDescriptionTo.setEditable(false);
      txtDescriptionTo.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtDescriptionTo.setLineWrap(true);
      txtDescriptionTo.setRows(5);
      txtDescriptionTo.setWrapStyleWord(true);
      txtDescriptionTo.setName("txtDescriptionTo"); // NOI18N
      txtDescriptionTo.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtDescriptionToKeyReleased(evt);
         }
      });
      jScrollPane3.setViewportView(txtDescriptionTo);

      jScrollPane5.setName("jScrollPane5"); // NOI18N

      txtDescriptionFrom.setColumns(20);
      txtDescriptionFrom.setEditable(false);
      txtDescriptionFrom.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
      txtDescriptionFrom.setLineWrap(true);
      txtDescriptionFrom.setRows(5);
      txtDescriptionFrom.setWrapStyleWord(true);
      txtDescriptionFrom.setName("txtDescriptionFrom"); // NOI18N
      txtDescriptionFrom.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyReleased(java.awt.event.KeyEvent evt) {
            txtDescriptionFromKeyReleased(evt);
         }
      });
      jScrollPane5.setViewportView(txtDescriptionFrom);

      comboFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
      comboFrom.setName("comboFrom");
      comboFrom.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboFromActionPerformed(evt);
         }
      });

      comboTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
      comboTo.setName("comboTo");
      comboTo.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboToActionPerformed(evt);
         }
      });

      lblFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_right.png"))); // NOI18N
      lblFrom.setText("jLabel2");
      lblFrom.setName("lblFrom");

      lblTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_right.png"))); // NOI18N
      lblTo.setText("jLabel4");
      lblTo.setName("lblTo");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(
                                    layout.createSequentialGroup()
                                          .addComponent(comboFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 134,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(lblTo, javax.swing.GroupLayout.DEFAULT_SIZE, 118,
                                                Short.MAX_VALUE))
                              .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(
                                    layout.createSequentialGroup()
                                          .addComponent(comboTo, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(lblFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 128,
                                                Short.MAX_VALUE)).addComponent(jScrollPane5)).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            javax.swing.GroupLayout.Alignment.TRAILING,
            layout.createSequentialGroup()
                  .addGap(11, 11, 11)
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(comboFrom, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(comboTo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(lblFrom).addComponent(lblTo))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                              .addComponent(jScrollPane5)).addContainerGap()));

      pack();
   } // </editor-fold>//GEN-END:initComponents

   private void txtDescriptionToKeyReleased(java.awt.event.KeyEvent evt) {
      //GEN-FIRST:event_txtDescriptionToKeyReleased
      Object o1 = comboFrom.getSelectedItem();
      Object o2 = comboTo.getSelectedItem();
      if (o1 == null || o2 == null) {
         return;
      }
      if (o1.getClass() == String.class || o2.getClass() == String.class) {
         return;
      }

      if (o1.getClass() == Character.class) {
         if (o2.getClass() == Character.class) {

            Character c1 = (Character) o1;
            Character c2 = (Character) o2;
            c1.AddRelation(c2.characterID, 0, txtDescriptionTo.getText());

            Character[] affectedcharacters = new Character[2];
            affectedcharacters[0] = c1;
            affectedcharacters[1] = c2;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true,
                  false);

         } else if (o2.getClass() == Group.class) {

            Character c1 = (Character) o1;
            Group g2 = (Group) o2;
            c1.AddRelation(g2.lngID, 1, txtDescriptionTo.getText());

            Character[] affectedcharacters = new Character[1];
            affectedcharacters[0] = c1;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, true, false, false, true,
                  false);

         }
      } else if (o1.getClass() == Group.class) {
         if (o2.getClass() == Character.class) {

            Group g1 = (Group) o1;
            Character c2 = (Character) o2;
            g1.AddRelation(c2.characterID, 0, txtDescriptionTo.getText());

            Character[] affectedcharacters = new Character[1];
            affectedcharacters[0] = c2;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, true, false, false, true,
                  false);

         } else if (o2.getClass() == Group.class) {

            Group g1 = (Group) o1;
            Group g2 = (Group) o2;
            g1.AddRelation(g2.lngID, 1, txtDescriptionTo.getText());

            XEED.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, true, false);

         }
      }

   } //GEN-LAST:event_txtDescriptionToKeyReleased

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      XEED.hwndRelations = null;
      dispose();
   } //GEN-LAST:event_formWindowClosing

   private void txtDescriptionFromKeyReleased(java.awt.event.KeyEvent evt) {
      //GEN-FIRST:event_txtDescriptionFromKeyReleased
      Object o1 = comboFrom.getSelectedItem();
      Object o2 = comboTo.getSelectedItem();
      if (o1 == null || o2 == null) {
         return;
      }
      if (o1.getClass() == String.class || o2.getClass() == String.class) {
         return;
      }

      if (o1.getClass() == Character.class) {
         if (o2.getClass() == Character.class) {

            Character c1 = (Character) o1;
            Character c2 = (Character) o2;
            c2.AddRelation(c1.characterID, 0, txtDescriptionFrom.getText());

            Character[] affectedcharacters = new Character[2];
            affectedcharacters[0] = c1;
            affectedcharacters[1] = c2;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true,
                  false);

         } else if (o2.getClass() == Group.class) {

            Character c1 = (Character) o1;
            Group g2 = (Group) o2;
            g2.AddRelation(c1.characterID, 0, txtDescriptionFrom.getText());

            Character[] affectedcharacters = new Character[1];
            affectedcharacters[0] = c1;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, true, false, false, true,
                  false);

         }
      } else if (o1.getClass() == Group.class) {
         if (o2.getClass() == Character.class) {

            Group g1 = (Group) o1;
            Character c2 = (Character) o2;
            c2.AddRelation(g1.lngID, 1, txtDescriptionFrom.getText());

            Character[] affectedcharacters = new Character[1];
            affectedcharacters[0] = c2;
            XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, true, false, false, true,
                  false);

         } else if (o2.getClass() == Group.class) {

            Group g1 = (Group) o1;
            Group g2 = (Group) o2;
            g2.AddRelation(g1.lngID, 1, txtDescriptionFrom.getText());

            XEED.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, true, false);

         }
      }

   } //GEN-LAST:event_txtDescriptionFromKeyReleased

   private void comboFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFromActionPerformed
      LoadRelationship();
   } //GEN-LAST:event_comboFromActionPerformed

   private void comboToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboToActionPerformed
      LoadRelationship();
   } //GEN-LAST:event_comboToActionPerformed
     // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JComboBox comboFrom;
   private javax.swing.JComboBox comboTo;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JScrollPane jScrollPane5;
   private javax.swing.JLabel lblFrom;
   private javax.swing.JLabel lblTo;
   private javax.swing.JTextArea txtDescriptionFrom;
   private javax.swing.JTextArea txtDescriptionTo;
   // End of variables declaration//GEN-END:variables
}
