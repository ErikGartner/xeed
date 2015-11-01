/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * pnlRelations.java
 *
 * Created on 2011-feb-14, 12:16:39
 */
package TemplateItems;

import java.util.ArrayList;
import xeed.clsCharacter;
import xeed.clsEngine;
import xeed.clsGroup;

/**
 *
 * @author Erik
 */
public class pnlRelations extends javax.swing.JPanel {

    clsCharacter characterHandle = null;
    clsGroup groupHandle = null;
    int intType = -1;

    /**
     * Creates new form pnlRelations
     */
    public pnlRelations(clsCharacter c) {
        characterHandle = c;
        initComponents();
        LoadData();
        intType = 0;
    }

    public pnlRelations(clsGroup g) {
        groupHandle = g;
        initComponents();
        LoadData();
        intType = 1;
    }

    public void LoadData() {

        Object o = comboData.getSelectedItem();
        comboData.removeAllItems();
        if (clsEngine.charDB.size() > 0) {
            comboData.addItem("");
            comboData.addItem("Characters:");
            for (int x = 0; x < clsEngine.charDB.size(); x++) {
                if (characterHandle != clsEngine.charDB.get(x)) {
                    comboData.addItem(clsEngine.charDB.get(x));
                }
            }
        }
        if (clsEngine.groupDB.size() > 0) {
            comboData.addItem("");
            comboData.addItem("Groups:");
            for (int x = 0; x < clsEngine.groupDB.size(); x++) {
                if (groupHandle != clsEngine.groupDB.get(x)) {
                    comboData.addItem(clsEngine.groupDB.get(x));
                }
            }
        }
        comboData.setSelectedItem(o);
    }

    public void LoadRelationship() {

        Object o = comboData.getSelectedItem();
        if (o == null) {
            txtDescriptionTo.setEditable(false);
            txtDescriptionFrom.setEditable(false);
            txtDescriptionTo.setText("");
            txtDescriptionFrom.setText("");
            lblTitleFrom.setText("");
            return;
        }

        if (o.getClass() == String.class) {

            txtDescriptionTo.setEditable(false);
            txtDescriptionFrom.setEditable(false);
            txtDescriptionTo.setText("");
            txtDescriptionFrom.setText("");
            lblTitleFrom.setText("");
            return;

        } else if (o.getClass() == clsCharacter.class) {

            clsCharacter c = (clsCharacter) o;
            if (intType == 0) {
                txtDescriptionTo.setText(characterHandle.GetRelation(c.characterID, 0));
                lblTitleFrom.setText(characterHandle.GetCharacterName());
                txtDescriptionFrom.setText(c.GetRelation(characterHandle.characterID, 0));
            } else if (intType == 1) {
                lblTitleFrom.setText(groupHandle.szName);
                txtDescriptionTo.setText(groupHandle.GetRelation(c.characterID, 0));
                txtDescriptionFrom.setText(c.GetRelation(groupHandle.lngID, 1));
            }

        } else if (o.getClass() == clsGroup.class) {

            clsGroup g = (clsGroup) o;
            if (intType == 0) {
                txtDescriptionTo.setText(characterHandle.GetRelation(g.lngID, 1));
                lblTitleFrom.setText(characterHandle.GetCharacterName());
                txtDescriptionFrom.setText(g.GetRelation(characterHandle.characterID, 0));
            } else if (intType == 1) {
                txtDescriptionTo.setText(groupHandle.GetRelation(g.lngID, 1));
                lblTitleFrom.setText(groupHandle.szName);
                txtDescriptionFrom.setText(g.GetRelation(groupHandle.lngID, 1));
            }

        }

        txtDescriptionTo.setEditable(true);
        txtDescriptionFrom.setEditable(true);
        txtDescriptionFrom.setCaretPosition(0);
        txtDescriptionTo.setCaretPosition(0);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        comboData = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescriptionTo = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDescriptionFrom = new javax.swing.JTextArea();
        lblTitleFrom = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        filler1.setName("filler1"); // NOI18N

        comboData.setName("comboData"); // NOI18N
        comboData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDataActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

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
        jScrollPane2.setViewportView(txtDescriptionTo);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

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
        jScrollPane3.setViewportView(txtDescriptionFrom);

        lblTitleFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_right.png"))); // NOI18N
        lblTitleFrom.setText("-");
        lblTitleFrom.setName("lblTitleFrom"); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_right.png"))); // NOI18N
        jLabel1.setName("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboData, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTitleFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(comboData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTitleFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDescriptionToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionToKeyReleased

        Object o = comboData.getSelectedItem();
        if (o.getClass() == String.class) {
            return;
        }

        if (intType == 0) {

            if (o.getClass() == clsCharacter.class) {
                clsCharacter c = (clsCharacter) o;
                characterHandle.AddRelation(c.characterID, 0, txtDescriptionTo.getText());

                //Fireupdates
                clsCharacter affectedcharacters[] = new clsCharacter[1];
                affectedcharacters[0] = c;
                clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true, true);

            } else if (o.getClass() == clsGroup.class) {
                clsGroup g = (clsGroup) o;
                characterHandle.AddRelation(g.lngID, 1, txtDescriptionTo.getText());

                clsEngine.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, false, true);

            }


        } else if (intType == 1) {

            if (o.getClass() == clsCharacter.class) {
                clsCharacter c = (clsCharacter) o;
                groupHandle.AddRelation(c.characterID, 0, txtDescriptionTo.getText());

                clsCharacter affectedcharacters[] = new clsCharacter[1];
                affectedcharacters[0] = c;
                clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true, true);

            } else if (o.getClass() == clsGroup.class) {
                clsGroup g = (clsGroup) o;
                groupHandle.AddRelation(g.lngID, 1, txtDescriptionTo.getText());

                clsEngine.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, false, true);
            }

        }

    }//GEN-LAST:event_txtDescriptionToKeyReleased

    private void txtDescriptionFromKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionFromKeyReleased

        Object o = comboData.getSelectedItem();
        if (o.getClass() == String.class) {
            return;
        }

        if (intType == 0) {

            if (o.getClass() == clsCharacter.class) {
                clsCharacter c = (clsCharacter) o;
                c.AddRelation(characterHandle.characterID, 0, txtDescriptionFrom.getText());

                clsCharacter affectedcharacters[] = new clsCharacter[1];
                affectedcharacters[0] = c;
                clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true, true);

            } else if (o.getClass() == clsGroup.class) {
                clsGroup g = (clsGroup) o;
                g.AddRelation(characterHandle.characterID, 0, txtDescriptionFrom.getText());

                clsEngine.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, false, true);
            }


        } else if (intType == 1) {
            if (o.getClass() == clsCharacter.class) {
                clsCharacter c = (clsCharacter) o;
                c.AddRelation(groupHandle.lngID, 1, txtDescriptionFrom.getText());

                clsCharacter affectedcharacters[] = new clsCharacter[1];
                affectedcharacters[0] = c;
                clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, false, false, true, true);

            } else if (o.getClass() == clsGroup.class) {
                clsGroup g = (clsGroup) o;
                g.AddRelation(groupHandle.lngID, 1, txtDescriptionFrom.getText());

                clsEngine.hwndNotifier.FireUpdate(null, false, false, false, false, true, false, false, false, true);
            }

        }

    }//GEN-LAST:event_txtDescriptionFromKeyReleased

    private void comboDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDataActionPerformed

        LoadRelationship();

    }//GEN-LAST:event_comboDataActionPerformed
    /*
     * derp
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboData;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTitleFrom;
    private javax.swing.JTextArea txtDescriptionFrom;
    private javax.swing.JTextArea txtDescriptionTo;
    // End of variables declaration//GEN-END:variables
}
