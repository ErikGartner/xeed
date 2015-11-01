/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TemplateItems;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import xeed.clsCharacter;
import xeed.clsEngine;

/**
 *
 * @author Erik
 */
public class pnlParentRow extends javax.swing.JPanel {

    public clsCharacter character;
    public String itemIdentifier;
    private boolean loaded = false;
    private boolean updating = false;

    public pnlParentRow(clsCharacter c, String id, String name) {
        itemIdentifier = id;
        character = c;
        initComponents();
        lblName.setText(name);
        LoadData();
        loaded = true;
    }

    public void SaveData() {

        if (character == null) {
            return;
        }
        if (comboList.getSelectedItem().getClass() == clsCharacter.class) {
            character.chrData.put(itemIdentifier, comboList.getSelectedItem());
            character.szData.remove(itemIdentifier);
        } else {
            String s = (String) comboList.getSelectedItem();
            if (!s.isEmpty()) {
                character.szData.put(itemIdentifier, comboList.getSelectedItem());
            } else {
                character.szData.remove(itemIdentifier);
            }
            character.chrData.remove(itemIdentifier);
        }


        clsCharacter[] affectedcharacters = new clsCharacter[clsEngine.charDB.size() - 1];
        int y = 0;
        for (int x = 0; x < clsEngine.charDB.size(); x++) {
            if (clsEngine.charDB.get(x) != character) {
                affectedcharacters[y] = clsEngine.charDB.get(x);
                y++;
            }
        }
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, true, false, false, false, false, false, false, false);

        affectedcharacters = new clsCharacter[1];
        affectedcharacters[0] = character;
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);

    }

    public void LoadData() {

        RefreshParents();
        updating = true;

        if (character == null) {
            updating = false;
            return;
        }

        Object o = character.chrData.get(itemIdentifier);
        if (o == null) {
            o = character.szData.get(itemIdentifier);
            if (o == null) {
                updating = false;
                return;
            }
        }

        if (o.getClass().equals(clsCharacter.class) || o.getClass().equals(String.class)) {
            comboList.setSelectedItem(o);
        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

        updating = false;

    }

    private void RefreshParents() {

        updating = true;

        comboList.removeAllItems();
        comboList.addItem("");
        for (int x = 0; x < clsEngine.charDB.size(); x++) {
            if (clsEngine.charDB.get(x) != character) {
                comboList.addItem(clsEngine.charDB.get(x));
            }
        }

        updating = false;

    }

    private boolean _checkloop(ArrayList<clsCharacter> familytree, clsCharacter c) {

        if (familytree == null) {
            return false;
        }

        if (c == null) {
            return true;
        }

        if (familytree.contains(c)) {
            return false;
        } else {
            familytree.add(c);
        }

        Object[] parents = c.chrData.values().toArray();
        if (parents == null) {
            return true;
        }
        if (parents.length == 0) {
            return true;
        }

        for (int x = 0; x < parents.length; x++) {
            if (parents[x] != null) {
                if (parents[x].getClass() == clsCharacter.class) {
                    if (!_checkloop(familytree, (clsCharacter) parents[x])) {
                        return false;
                    }
                }
            }
        }

        return true;
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
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboList, 0, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblName))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboListActionPerformed

        if (!loaded || updating) {
            return;
        }

        Object o = comboList.getSelectedItem();

        //kontrollerar om ett namn manuellt skrivs in och isf matchar med en karaktär.
        if (o.getClass() == String.class) {
            for (int x = 0; x < clsEngine.charDB.size(); x++) {

                clsCharacter c = clsEngine.charDB.get(x);
                if (c.GetCharacterName().equalsIgnoreCase((String) o)) {
                    comboList.setSelectedItem(c);
                    return;
                }
            }
        }

        if (o.getClass() == clsCharacter.class) {
            ArrayList<clsCharacter> familytree = new ArrayList(0);
            familytree.add(character);
            if (!_checkloop(familytree, (clsCharacter) o)) {
                JOptionPane.showMessageDialog(null, "Invalid parent! The selected linage leads to an infinite loop (i.e. parents can't be children to their own children).", "Error", JOptionPane.ERROR_MESSAGE);
                comboList.setSelectedIndex(0);
            }
        }

        SaveData();
    }//GEN-LAST:event_comboListActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox comboList;
    public javax.swing.JLabel lblName;
    // End of variables declaration//GEN-END:variables
}
