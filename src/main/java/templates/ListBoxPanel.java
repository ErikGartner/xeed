/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import xeed.Character;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class ListBoxPanel extends javax.swing.JPanel implements TableModelListener {

    public Character character;
    public String itemIdentifier;
    private boolean loaded = false;
    private Vector jTableModel = new Vector(0, 2);                               // Model för tabellen
    private Vector jTableHeader = new Vector(0);
    private String[] properties;

    public ListBoxPanel(Character c, String id, String name, String data[]) {
        itemIdentifier = id;
        character = c;
        properties = data;

        initComponents();

        lblName.setText(name);
        LoadData();
        loaded = true;

        tblList.setTableHeader(null);

        //ful hack för att componentshown ska bliv invokad.
        tblList.setVisible(false);
        tblList.setVisible(true);

        tblList.getModel().addTableModelListener(this);

    }

    private void LoadProperties() {

        jTableModel.clear();
        for (int x = 0; x < properties.length; x++) {
            Vector o = new Vector(0);
            o.add(false);
            o.add(properties[x]);
            jTableModel.add(o);
        }

    }

    public void CalculateTableSizes() {

        tblList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tblList.getColumnModel().getColumn(0).setResizable(false);
        tblList.getColumnModel().getColumn(0).setPreferredWidth(20);
        tblList.getColumnModel().getColumn(1).setPreferredWidth(tblList.getWidth() - 20);

        setMaximumSize(new Dimension(this.getMaximumSize().width, tblList.getHeight() + 5));
    }

    public void SaveData() {

        if (character == null) {
            return;
        }

        String comp = "";
        for (int x = 0; x < jTableModel.size(); x++) {
            Vector v = (Vector) jTableModel.get(x);
            if ((Boolean) v.get(0)) {
                if (comp.isEmpty()) {
                    comp = (String) v.get(1);
                } else {
                    comp += ",\n" + v.get(1);
                }
            }
        }

        character.szData.put(itemIdentifier, comp);

        Character[] affectedcharacters = new Character[1];
        affectedcharacters[0] = character;
        XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);

    }

    public void LoadData() {

        LoadProperties();
        CalculateTableSizes();

        if (character == null) {
            return;
        }

        Object o = character.szData.get(itemIdentifier);

        if (o == null) {
            return;
        }

        if (o.getClass().equals(String.class)) {

            String[] props = ((String) o).split(",\n");

            for (int y = 0; y < jTableModel.size(); y++) {
                Vector v = (Vector) jTableModel.get(y);
                String prop = (String) v.get(1);
                for (int x = 0; x < props.length; x++) {
                    if (prop.equalsIgnoreCase((String) props[x])) {
                        v.set(0, true);
                        jTableModel.set(y, v);
                    }
                }
            }

            ((DefaultTableModel) tblList.getModel()).fireTableDataChanged();

        } else {
            System.out.println(lblName.getText() + " loaded invalid data");
        }

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() != TableModelEvent.UPDATE) {
            return;
        }

        if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
            return;
        }

        SaveData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblList = new javax.swing.JTable();

        lblName.setText("LABEL");

        jTableHeader.add("");
        jTableHeader.add("Property");
        tblList.setModel(new javax.swing.table.DefaultTableModel(
            jTableModel,
            jTableHeader) {

            Class[] types = new Class[]{
                java.lang.Boolean.class, Object.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex != 0) {
                    return false;
                }else{
                    return true;
                }
            }
        });
        tblList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblList.setShowHorizontalLines(false);
        tblList.setShowVerticalLines(false);
        tblList.getTableHeader().setReorderingAllowed(false);
        tblList.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tblListComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tblListComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(tblList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblName)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblListComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tblListComponentShown
        CalculateTableSizes();
    }//GEN-LAST:event_tblListComponentShown

    private void tblListComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tblListComponentResized
        CalculateTableSizes();
    }//GEN-LAST:event_tblListComponentResized
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblName;
    private javax.swing.JTable tblList;
    // End of variables declaration//GEN-END:variables
}
