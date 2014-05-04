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
public class pnlTextBoxNoTitle extends javax.swing.JPanel {

    public clsCharacter character;
    public String itemIdentifier;

    public pnlTextBoxNoTitle(clsCharacter c, String id, String name) {
        itemIdentifier = id;
        character = c;
        initComponents();
        txtBox.setToolTipText(name);
        LoadData();
    }

    public void SaveData() {
        if (character == null) {
            return;
        }
        if (txtBox.getText().isEmpty()) {
            character.szData.remove(itemIdentifier);
        } else {
            character.szData.put(itemIdentifier, txtBox.getText());
        }

        clsCharacter[] affectedcharacters = new clsCharacter[1];
        affectedcharacters[0] = character;
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);
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
            txtBox.setText((String) o);
        } else {
            System.out.println(txtBox.getToolTipText() + " loaded invalid data");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtBox = new javax.swing.JTextArea();

        txtBox.setColumns(8);
        txtBox.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        txtBox.setLineWrap(true);
        txtBox.setRows(5);
        txtBox.setTabSize(3);
        txtBox.setWrapStyleWord(true);
        txtBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBoxKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBoxKeyReleased
        SaveData();
    }//GEN-LAST:event_txtBoxKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea txtBox;
    // End of variables declaration//GEN-END:variables
}
