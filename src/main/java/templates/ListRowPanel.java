/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import xeed.Character;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class ListRowPanel extends javax.swing.JPanel {

    public Character character;
    public String itemIdentifier;
    private boolean loaded = false;

    public ListRowPanel(Character c, String id, String name, String data[]) {
        itemIdentifier = id;
        character = c;
        initComponents();
        lblName.setText(name);

        comboList.addItem("");
        for (int x = 0; x < data.length; x++) {
            comboList.addItem(data[x]);
        }
        LoadData();
        loaded = true;
    }

    public void SaveData() {

        if (character == null) {
            return;
        }

        String s = (String) comboList.getSelectedItem();
        if (s.isEmpty()) {
            character.szData.remove(itemIdentifier);
        } else {
            character.szData.put(itemIdentifier, comboList.getSelectedItem());
        }
        Character[] affectedcharacters = new Character[1];
        affectedcharacters[0] = character;
        XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);

    }

    public void LoadData() {

        if (character == null) {
            return;
        }

        Object o = character.szData.get(itemIdentifier);

        if (o == null) {
            return;
        }

        if (o.getClass().equals(String.class)) {
            comboList.setSelectedItem(o);
        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();
      comboList = new javax.swing.JComboBox();

      lblName.setText("LABEL");

      comboList.setEditable(true);
      comboList.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboListActionPerformed(evt);
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
                  .addComponent(comboList, 0, 169, Short.MAX_VALUE).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(comboList, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(lblName)));
   }// </editor-fold>//GEN-END:initComponents

   private void comboListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboListActionPerformed

       if (!loaded) {
           return;
       }

       SaveData();
   }//GEN-LAST:event_comboListActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables

   public javax.swing.JComboBox comboList;
   public javax.swing.JLabel lblName;
   // End of variables declaration//GEN-END:variables
}
