/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TemplateItems;

import xeed.clsCharacter;
import xeed.clsEngine;

/**
 *
 * @author Erik
 */
public class pnlCheckbox extends javax.swing.JPanel {

    public clsCharacter character;
    public String itemIdentifier;

    public pnlCheckbox(clsCharacter c, String id, String name) {
        itemIdentifier = id;
        character = c;
        initComponents();
        lblName.setText(name);
        LoadData();
    }

    public void SaveData() {

        if (character == null) {
            return;
        }
        character.szData.put(itemIdentifier, Boolean.toString(chkRow.isSelected()));

        clsCharacter[] affectedcharacters = new clsCharacter[1];
        affectedcharacters[0] = character;
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false,false,false);

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
            chkRow.setSelected(Boolean.parseBoolean((String) o));
        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        chkRow = new javax.swing.JCheckBox();

        lblName.setText("LABEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkRow, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chkRow, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblName)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkRow;
    public javax.swing.JLabel lblName;
    // End of variables declaration//GEN-END:variables
}
