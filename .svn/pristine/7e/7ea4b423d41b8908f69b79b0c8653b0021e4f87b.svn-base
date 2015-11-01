/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TemplateItems;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import xeed.clsCharacter;
import xeed.clsEngine;

/**
 *
 * @author Erik
 */
public class pnlNumberRow extends javax.swing.JPanel {

    public clsCharacter character;
    public String itemIdentifier;

    public pnlNumberRow(clsCharacter c, String id, String name) {
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
        character.szData.put(itemIdentifier, txtNumber.getText());
        
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
            txtNumber.setText((String) o);
        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        txtNumber = new javax.swing.JTextField();

        lblName.setText("LABEL");

        txtNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumberKeyReleased(evt);
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
                .addComponent(txtNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblName))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumberKeyReleased
        ValidateNumber(txtNumber);
        SaveData();
    }//GEN-LAST:event_txtNumberKeyReleased

    private void ValidateNumber(JTextField txtField) {
        if (txtField.getText().isEmpty()) {
            return;
        }

        try {
            float y = Float.parseFloat(txtField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The textfield '" + lblName.getText() + "' must contain a number.", "Invalid data", JOptionPane.WARNING_MESSAGE);
            txtField.setText("");
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblName;
    public javax.swing.JTextField txtNumber;
    // End of variables declaration//GEN-END:variables
}
