/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import xeed.*;
import xeed.Character;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Erik
 */
public class CharacterConverterForm extends javax.swing.JFrame {

    public xeed.Character[] characters;
    private Vector jTableModel = new Vector(0);
    private Vector jTableHeader = new Vector(0);
    private property[] oldTemplate;
    private property[] newTemplate;
    private boolean working = false;

    public CharacterConverterForm(Character[] cs) {
        characters = cs;
        initComponents();

        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/user_edit.png")));
            this.setIconImages(images);
        } catch (IOException e) {
        }

        oldTemplate = LoadTemplate(characters[0].template);
        LoadTemplateList();
        LoadTableAndEditor();

        String comp = "";
        for (int x = 0; x < characters.length; x++) {
            comp += characters[x].GetCharacterName();
            if (x != characters.length - 1) {
                comp += ", ";
            }
        }
        txtCharacters.setText(comp);

    }

    //call to verfiy that a character loaded isn't removed.
    public void CharacterRemoved(Character c) {
        if (working) {
            return;
        }
        for (int x = 0; x < characters.length; x++) {
            if (c == characters[x]) {
                JOptionPane.showMessageDialog(null, "One of the characters selected for conversion was removed!\nThe characater converter will now close.", "Selected character was removed.", JOptionPane.WARNING_MESSAGE);
                XEED.hwndCharacterConverter = null;
                dispose();
            }
        }
    }

    public void LoadTemplateList() {

        comboTemplates.removeAllItems();
        for (int x = 0; x < XEED.templateDB.size(); x++) {
            if (XEED.templateDB.get(x) != characters[0].template) {
                comboTemplates.addItem(XEED.templateDB.get(x));
            }
        }
        if (comboTemplates.getItemCount() < 1) {
            JOptionPane.showMessageDialog(null, "You need atleast 2 templates loaded to use the converter.", "Not enough templates", JOptionPane.WARNING_MESSAGE);
            XEED.hwndCharacterConverter = null;
            dispose();
        }

    }

    private void LoadTableAndEditor() {

        if (newTemplate == null) {
            return;
        }

        jTableModel.clear();

        for (int x = 0; x < newTemplate.length; x++) {
            Vector o = new Vector(0);
            o.add(null);
            o.add(newTemplate[x]);
            if (!newTemplate[x].key.equals(Constants.CHARACTER_GROUPS)) {
                jTableModel.add(o);
            }
        }

        tblOverview.getColumnModel().getColumn(0).setCellEditor(new PropertyEditor(oldTemplate));
        DefaultTableModel df = (DefaultTableModel) tblOverview.getModel();
        df.fireTableDataChanged();

    }

    private property[] LoadTemplate(Template t) {

        if (t == null) {
            return null;
        }

        String[] keys = t.GetAllTemplateKeys();
        String[] names = t.GetAllTemplateNames();

        property[] props = new property[keys.length];
        for (int x = 0; x < keys.length; x++) {

            property prop = new property();
            prop.key = keys[x];
            prop.name = names[x];
            boolean type[] = t.GetDataTypeArray(keys[x]);

            prop.isChrData = type[0];
            prop.isExtData = type[1];
            prop.isImgData = type[2];
            prop.isSzData = type[3];

            props[x] = prop;
        }

        return props;

    }

    private class property {

        String name;
        String key;
        boolean isChrData = false;
        boolean isSzData = false;
        boolean isExtData = false;
        boolean isImgData = false;

        public String toString() {
            return name;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblOverview = new javax.swing.JTable();
        btnGo = new javax.swing.JButton();
        chkAccept = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        comboTemplates = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtCharacters = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Character converter");
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTableHeader.add("Old");
        jTableHeader.add("New");
        tblOverview.setModel(new javax.swing.table.DefaultTableModel(
            jTableModel,
            jTableHeader) {

            Class[] types = new Class[]{
                Object.class, Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        tblOverview.setToolTipText("Click in the left column to change the properties.");
        tblOverview.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblOverview.setShowHorizontalLines(false);
        tblOverview.setShowVerticalLines(false);
        tblOverview.getTableHeader().setReorderingAllowed(false);
        tblOverview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblOverviewMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOverviewMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblOverviewMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblOverview);

        btnGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cog_go.png"))); // NOI18N
        btnGo.setText("Go!");
        btnGo.setToolTipText("");
        btnGo.setDefaultCapable(false);
        btnGo.setEnabled(false);
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });

        chkAccept.setText("I accept");
        chkAccept.setToolTipText("I accept that the process is irreversible and may be subject to data corruption/loss.");
        chkAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptActionPerformed(evt);
            }
        });

        jLabel2.setText("Properties");

        comboTemplates.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboTemplates.setToolTipText("The template the character is to converted to");
        comboTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTemplatesActionPerformed(evt);
            }
        });

        jLabel5.setText("Target template");

        jLabel1.setText("Selected characters");

        txtCharacters.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chkAccept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(btnGo))
                    .addComponent(comboTemplates, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtCharacters))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCharacters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGo))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptActionPerformed
        btnGo.setEnabled(chkAccept.isSelected());
        if (chkAccept.isSelected()) {
            if (JOptionPane.showOptionDialog(null, "You are about to perform a character conversion.\nPlease note that is an irreversible action.\nIf an error occur it may result in data loss and/or corruption.\nDo you want to save the setting before you continue?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
                XEED.SaveSetting(false, true);
            }
        }
    }//GEN-LAST:event_chkAcceptActionPerformed

    private void comboTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTemplatesActionPerformed

        if (comboTemplates.getSelectedItem() != null) {
            newTemplate = LoadTemplate((Template) comboTemplates.getSelectedItem());
            LoadTableAndEditor();
        }

    }//GEN-LAST:event_comboTemplatesActionPerformed

    private void tblOverviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOverviewMouseClicked
    }//GEN-LAST:event_tblOverviewMouseClicked

    private void btnGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoActionPerformed

        Template targetTemplate = (Template) comboTemplates.getSelectedItem();
        if (targetTemplate == null) {
            JOptionPane.showMessageDialog(null, "Please select a target template!", "Missing data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        working = true;

        for (int x = 0; x < characters.length; x++) {

            HashMap old_chrData = characters[x].chrData;
            HashMap old_extData = characters[x].extData;
            HashMap old_imgData = characters[x].imgData;
            HashMap old_szData = characters[x].szData;

            characters[x].ChangeTemplate(targetTemplate);

            for (int y = 0; y < tblOverview.getRowCount(); y++) {

                property oldprop = (property) tblOverview.getValueAt(y, 0);
                if (oldprop != null) {
                    property newprop = (property) tblOverview.getValueAt(y, 1);

                    if (newprop.isChrData && old_chrData.containsKey(oldprop.key)) {
                        characters[x].chrData.put(newprop.key, old_chrData.get(oldprop.key));
                    }

                    if (newprop.isExtData && old_extData.containsKey(oldprop.key)) {
                        characters[x].extData.put(newprop.key, old_extData.get(oldprop.key));
                    }

                    if (newprop.isImgData && old_imgData.containsKey(oldprop.key)) {
                        characters[x].imgData.put(newprop.key, old_imgData.get(oldprop.key));
                    }

                    if (newprop.isSzData && old_szData.containsKey(oldprop.key)) {
                        characters[x].szData.put(newprop.key, old_szData.get(oldprop.key));
                    }
                }

            }
            
            Group.UpdateCharactersGroupList(characters[x].characterID);

        }

        
        JOptionPane.showMessageDialog(null, "Conversion complete!", "Done", JOptionPane.INFORMATION_MESSAGE);
        XEED.hwndCharacterConverter = null;
        dispose();

    }//GEN-LAST:event_btnGoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        XEED.hwndCharacterConverter = null;
        dispose();

    }//GEN-LAST:event_formWindowClosing

    private void tblOverviewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOverviewMousePressed
    }//GEN-LAST:event_tblOverviewMousePressed

    private void tblOverviewMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOverviewMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOverviewMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGo;
    private javax.swing.JCheckBox chkAccept;
    private javax.swing.JComboBox comboTemplates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblOverview;
    private javax.swing.JTextField txtCharacters;
    // End of variables declaration//GEN-END:variables

    private class PropertyEditor extends AbstractCellEditor implements TableCellEditor {

        JComponent component = new JComboBox();
        private property[] oldTemplate;

        public PropertyEditor(property[] props) {
            oldTemplate = props;
        }

        private void LoadProperties(property new_prop) {

            JComboBox comboProperties = (JComboBox) component;

            comboProperties.removeAllItems();
            comboProperties.addItem(null);

            for (int x = 0; x < oldTemplate.length; x++) {

                boolean add = false;
                if (new_prop.isChrData && oldTemplate[x].isChrData == new_prop.isChrData) {
                    add = true;
                }
                if (new_prop.isExtData && oldTemplate[x].isExtData == new_prop.isExtData) {
                    add = true;
                }
                if (new_prop.isImgData && oldTemplate[x].isImgData == new_prop.isImgData) {
                    add = true;
                }
                if (new_prop.isSzData && oldTemplate[x].isSzData == new_prop.isSzData) {
                    add = true;
                }

                if (add) {
                    comboProperties.addItem(oldTemplate[x]);
                }

            }

        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {

            property new_prop = (property) table.getValueAt(rowIndex, vColIndex + 1);
            LoadProperties(new_prop);

            ((JComboBox) component).setSelectedItem(value);
            return component;

        }

        public Object getCellEditorValue() {
            return ((JComboBox) component).getSelectedItem();
        }
    }
}
