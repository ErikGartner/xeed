/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * pnlGroups.java
 *
 * Created on 2011-feb-11, 09:34:45
 */
package TemplateItems;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import xeed.clsCharacter;
import xeed.clsEngine;
import xeed.clsGroup;

/**
 *
 * @author Erik
 */
public final class pnlGroups extends javax.swing.JPanel implements TableModelListener {

    private Vector jTableModel = new Vector(0, 3);                               // Model för tabellen
    private Vector jTableHeader = new Vector(0);
    private clsCharacter characterHandle = null;
    private boolean boolReloading = false;                                      //Förhindrara update loopar

    /**
     * Creates new form pnlGroups
     */
    public pnlGroups(clsCharacter c) {
        initComponents();
        setVisible(true);

        characterHandle = c;
        tblGroups.getModel().addTableModelListener(this);
        LoadGroups();
    }

    public void JustifyColumns() {

        tblGroups.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tblGroups.getColumnModel().getColumn(0).setResizable(false);
        tblGroups.getColumnModel().getColumn(0).setPreferredWidth(20);
        tblGroups.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblGroups.getColumnModel().getColumn(2).setPreferredWidth(tblGroups.getWidth() - 120);

    }

    public void LoadGroups() {

        boolReloading = true;
        jTableModel.clear();
        for (int x = 0; x < clsEngine.groupDB.size(); x++) {

            Vector o = new Vector(0);

            o.add(clsEngine.groupDB.get(x).IsMember(characterHandle.characterID));
            o.add(clsEngine.groupDB.get(x));
            o.add(clsEngine.groupDB.get(x).szDescription);

            jTableModel.add(o);

        }
        DefaultTableModel df = (DefaultTableModel) tblGroups.getModel();
        df.fireTableDataChanged();
        boolReloading = false;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        
        if (e.getType() != TableModelEvent.UPDATE) {
            return;
        }

        if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
            return;
        }

        if (jTableModel.isEmpty()) {
            return;
        }

        if (boolReloading) {
            return;
        }

        for (int x = 0; x < jTableModel.size(); x++) {
            Vector o = (Vector) jTableModel.get(x);
            clsGroup g = (clsGroup) o.get(1);
            boolean b = (Boolean) o.get(0);
            if (b) {
                g.AddMember(characterHandle.characterID);
            } else {
                g.DeleteMember(characterHandle.characterID);
            }
        }

        clsCharacter[] affectedcharacters = new clsCharacter[1];
        affectedcharacters[0] = characterHandle;
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, true, false, true, false,false,false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblGroups = new javax.swing.JTable();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableHeader.add("");
        jTableHeader.add("Group");
        jTableHeader.add("Description");
        tblGroups.setAutoCreateRowSorter(true);
        tblGroups.setModel(new javax.swing.table.DefaultTableModel(
            jTableModel,
            jTableHeader
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, Object.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex!=0){
                    return false;
                }else{
                    return true;
                }
            }

        });
        tblGroups.setName("tblGroups"); // NOI18N
        tblGroups.setShowHorizontalLines(false);
        tblGroups.setShowVerticalLines(false);
        tblGroups.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblGroups);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        JustifyColumns();
    }//GEN-LAST:event_formComponentShown

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGroups;
    // End of variables declaration//GEN-END:variables
}
