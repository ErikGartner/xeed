/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import templates.*;
import xeed.Character;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Erik
 */
public class CharacterForm extends javax.swing.JFrame {

   private CharacterPanel topPanel;
   public xeed.Character character;

   public CharacterForm(CharacterPanel panel, Character c) {

      initComponents();
      character = c;
      topPanel = panel;
      getRootPane().setContentPane(topPanel);
      pack();
      this.setVisible(true);

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/user.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }

   }

   public void ReloadItems(boolean RelationCharacters, boolean Groups, boolean boolItems, boolean Relations) {

      //Only reloads item which can be changed from outside the character sheet. (To optimise the method).

      if (boolItems) {

         for (int x = 0; x < topPanel.items.size(); x++) {

            if (topPanel.items.get(x).getClass() == OffspringPanel.class) {

               ((OffspringPanel) topPanel.items.get(x)).LoadOffspring();

            }
            if (topPanel.items.get(x).getClass() == ParentRowPanel.class) {

               ((ParentRowPanel) topPanel.items.get(x)).LoadData();

            }

         }
      }

      if (Groups) {
         if (character.hwndGroups != null) {
            character.hwndGroups.LoadGroups();
         }
      }
      if (RelationCharacters) {
         if (character.hwndRelations != null) {
            character.hwndRelations.LoadData();
         }
      }
      if (Relations) {
         if (character.hwndRelations != null) {
            character.hwndRelations.LoadRelationship();
         }
      }

   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("CHARACTER");
      setLocationByPlatform(true);
      addWindowFocusListener(new java.awt.event.WindowFocusListener() {
         public void windowGainedFocus(java.awt.event.WindowEvent evt) {
         }

         public void windowLostFocus(java.awt.event.WindowEvent evt) {
            formWindowLostFocus(evt);
         }
      });
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 340,
            Short.MAX_VALUE));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 484,
            Short.MAX_VALUE));

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      character.CloseForm();
   }//GEN-LAST:event_formWindowClosing

   private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
   }//GEN-LAST:event_formWindowLostFocus
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
