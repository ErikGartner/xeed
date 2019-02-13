/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.*;
import xeed.Character;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public final class OffspringPanel extends javax.swing.JPanel {

    public Character character;
    private DefaultMutableTreeNode topNode = null;

    public OffspringPanel(Character c, String name) {
        character = c;
        initComponents();
        lblName.setText(name);
        treeOffspring.setToolTipText(name);
        LoadOffspring();
    }

    public void LoadOffspring() {

        treeOffspring.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeOffspring.setCellRenderer(new CustomRenderer(new javax.swing.ImageIcon(getClass().getResource("/user.png"))));

        topNode = new DefaultMutableTreeNode(character);
        _printchildren(character, topNode);

        DefaultTreeModel dtm = new DefaultTreeModel(topNode);
        treeOffspring.setModel(dtm);
        dtm.reload();
    }

    private void _printchildren(Character c, DefaultMutableTreeNode n) {

        if (c == null || n == null) {
            return;
        }

        for (int x = 0; x < XEED.charDB.size(); x++) {

            if (XEED.charDB.get(x).chrData.containsValue(c)) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(XEED.charDB.get(x));
                n.add(child);
                _printchildren(XEED.charDB.get(x), child);
            }

        }
    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jScrollPane1 = new javax.swing.JScrollPane();
      treeOffspring = new javax.swing.JTree();
      lblName = new javax.swing.JLabel();

      treeOffspring.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            treeOffspringMouseClicked(evt);
         }
      });
      jScrollPane1.setViewportView(treeOffspring);

      lblName.setText("jLabel1");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup()
                  .addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                              .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                              .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addGap(0, 0, Short.MAX_VALUE)));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            javax.swing.GroupLayout.Alignment.TRAILING,
            layout.createSequentialGroup().addComponent(lblName)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)));
   }// </editor-fold>//GEN-END:initComponents

   private void treeOffspringMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeOffspringMouseClicked

       if (evt.getClickCount() != 2) {
           return;
       }

       DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeOffspring.getLastSelectedPathComponent();
       if (node == null) {
           return;
       }

       Object nodeInfo = node.getUserObject();
       Character chr = (Character) nodeInfo;
       chr.DisplayCharacter();

   }//GEN-LAST:event_treeOffspringMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables

   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JLabel lblName;
   private javax.swing.JTree treeOffspring;

   // End of variables declaration//GEN-END:variables
    class CustomRenderer extends DefaultTreeCellRenderer {

        Icon main;

        public CustomRenderer(Icon i) {
            main = i;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            setIcon(main);
            return this;
        }
    }
}
