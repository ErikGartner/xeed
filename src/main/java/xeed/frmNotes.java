/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * setting.java
 *
 * Created on 2010-nov-24, 12:27:56
 */
package xeed;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class frmNotes extends javax.swing.JFrame {

    DefaultMutableTreeNode top;
    public clsNote openFile = null;
    private UndoManager undo = new UndoManager();
    private Document doc;

    /**
     * Creates new form setting
     */
    public frmNotes() {

        initComponents();


        //Undo listeners
        txtInformation.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });


        txtInformation.getActionMap().put("Undo",
                new AbstractAction("Undo") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canUndo()) {
                                undo.undo();
                            }
                        } catch (CannotUndoException e) {
                        }
                    }
                });


        txtInformation.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");


        txtInformation.getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canRedo()) {
                                undo.redo();
                            }
                        } catch (CannotRedoException e) {
                        }
                    }
                });

        txtInformation.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");


        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/page_edit.png")));
            this.setIconImages(images);
        } catch (IOException e) {
        }

        treeCategory.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        top = null;
        top = new DefaultMutableTreeNode(clsEngine.rootNode);

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        treeCategory.setModel(treeModel);
        treeCategory.setCellRenderer(new CustomRenderer(new javax.swing.ImageIcon(getClass().getResource("/page.png")), new javax.swing.ImageIcon(getClass().getResource("/folder.png"))));

        PurgeThenPrintInfo();

    }

    private void OpenNote() {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeCategory.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        clsNote s = (clsNote) nodeInfo;
        if (!s.boolFolder) {
            txtInformation.setText(s.szData);

            TreeNode nodes[] = (TreeNode[]) node.getPath();
            String szPath = "";
            for (int x = 0; x < nodes.length; x++) {
                szPath += ((clsNote) ((DefaultMutableTreeNode) nodes[x]).getUserObject()).szTitle;
                if (x != nodes.length - 1) {
                    szPath += " > ";
                }
            }
            lblContent.setText("Viewing: " + szPath);
            openFile = s;
            txtInformation.setEnabled(true);
            txtInformation.setCaretPosition(0);
            undo.discardAllEdits();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeCategory = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInformation = new javax.swing.JTextArea();
        btnRemove = new javax.swing.JButton();
        btnAddFolder = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        lblContent = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnRename = new javax.swing.JButton();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Notes");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        treeCategory.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeCategoryMouseClicked(evt);
            }
        });
        treeCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                treeCategoryKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(treeCategory);

        txtInformation.setColumns(20);
        txtInformation.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        txtInformation.setLineWrap(true);
        txtInformation.setRows(5);
        txtInformation.setTabSize(4);
        txtInformation.setWrapStyleWord(true);
        txtInformation.setEnabled(false);
        txtInformation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInformationKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInformationKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txtInformation);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        btnRemove.setToolTipText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAddFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder_add.png"))); // NOI18N
        btnAddFolder.setToolTipText("Add folder");
        btnAddFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFolderActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
        btnAdd.setToolTipText("Add note");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        lblContent.setText("Viewing: -");

        jLabel3.setText("Browser");

        btnRename.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wrench.png"))); // NOI18N
        btnRename.setToolTipText("Rename");
        btnRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRename, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblContent, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContent)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd)
                            .addComponent(btnAddFolder)
                            .addComponent(btnRename)
                            .addComponent(btnRemove)))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public final void PurgeThenPrintInfo() {

        /*
         * Not much purging to do...
         */
        top = new DefaultMutableTreeNode(clsEngine.rootNode);

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        PrintChildren(clsEngine.rootNode.GetChildren(), top);
        treeCategory.setModel(treeModel);

    }

    public void PrintChildren(clsNote[] s, DefaultMutableTreeNode parent) {

        if (s == null) {
            return;
        }

        for (int x = 0; x < s.length; x++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(s[x]);
            parent.add(node);
            PrintChildren(s[x].GetChildren(), node);
        }
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeCategory.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }

        Object nodeInfo = selectedNode.getUserObject();
        clsNote selectedObj = (clsNote) nodeInfo;
        if (!selectedObj.boolFolder || !selectedNode.getAllowsChildren()) {
            Object[] set = selectedNode.getUserObjectPath();
            selectedObj = (clsNote) set[set.length - 2];
            selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
        }

        clsNote s = new clsNote();
        s.boolFolder = false;
        s.szTitle = JOptionPane.showInputDialog(null, "Enter the name of the new file:", "Enter name", JOptionPane.PLAIN_MESSAGE);
        if (s.szTitle == null) {
            return;
        }
        if (s.szTitle.isEmpty()) {
            return;
        }

        selectedObj.AddChild(s);

        DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(s);
        new_node.setAllowsChildren(false);
        selectedNode.add(new_node);
        treeCategory.repaint();
        treeCategory.updateUI();
    }//GEN-LAST:event_btnAddActionPerformed

    private void treeCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeCategoryMouseClicked
        if (evt.getClickCount() != 2) {
            return;
        }

        OpenNote();

    }//GEN-LAST:event_treeCategoryMouseClicked

    private void btnAddFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFolderActionPerformed

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeCategory.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }

        Object nodeInfo = selectedNode.getUserObject();
        clsNote selectedObj = (clsNote) nodeInfo;
        if (!selectedObj.boolFolder || !selectedNode.getAllowsChildren()) {
            Object[] set = selectedNode.getUserObjectPath();
            selectedObj = (clsNote) set[set.length - 2];
            selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
        }

        clsNote s = new clsNote();
        s.boolFolder = true;
        s.szTitle = JOptionPane.showInputDialog(null, "Enter the name of the new folder:", "Enter name", JOptionPane.PLAIN_MESSAGE);
        if (s.szTitle == null) {
            return;
        }
        if (s.szTitle.isEmpty()) {
            return;
        }

        selectedObj.AddChild(s);

        DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(s);
        new_node.setAllowsChildren(true);
        selectedNode.add(new_node);
        treeCategory.repaint();
        treeCategory.updateUI();

    }//GEN-LAST:event_btnAddFolderActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeCategory.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }

        if (selectedNode == top) {        //Can't remove header.
            return;
        }

        Object childInfo = selectedNode.getUserObject();
        clsNote childObj = (clsNote) childInfo;

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
        Object nodeInfo = parentNode.getUserObject();
        clsNote parentObj = (clsNote) nodeInfo;

        if (openFile == childObj) {
            openFile = null;
            txtInformation.setText("");
            txtInformation.setEnabled(false);
        }

        parentObj.RemoveChild(childObj);
        selectedNode.removeFromParent();

        treeCategory.repaint();
        treeCategory.updateUI();

    }//GEN-LAST:event_btnRemoveActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        clsEngine.hwndNotes = null;
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void txtInformationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInformationKeyReleased
        if (openFile == null) {
            return;
        }
        openFile.szData = txtInformation.getText();
    }//GEN-LAST:event_txtInformationKeyReleased

    private void btnRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameActionPerformed
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeCategory.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }

        if (selectedNode == top) {        //Can't remove header.
            return;
        }

        Object childInfo = selectedNode.getUserObject();
        clsNote childObj = (clsNote) childInfo;

        String szNewName = JOptionPane.showInputDialog(null, "Enter the new name:", "Rename " + childObj.szTitle, JOptionPane.PLAIN_MESSAGE);
        if (szNewName == null) {
            return;
        }
        if (szNewName.isEmpty()) {
            return;
        }

        childObj.szTitle = szNewName;

        selectedNode.setUserObject(childObj);

        if (openFile == childObj) {
            lblContent.setText("Viewing: " + childObj.szTitle);
        }

        treeCategory.repaint();
        treeCategory.updateUI();
    }//GEN-LAST:event_btnRenameActionPerformed

    private void txtInformationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInformationKeyPressed
    }//GEN-LAST:event_txtInformationKeyPressed

    private void treeCategoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_treeCategoryKeyReleased
        
        if(evt.getKeyCode() != KeyEvent.VK_ENTER){
            return;
        }
        
        OpenNote();
        
    }//GEN-LAST:event_treeCategoryKeyReleased

    class CustomRenderer extends DefaultTreeCellRenderer {

        Icon iconFolder;
        Icon iconFile;

        public CustomRenderer(Icon file, Icon Folder) {
            iconFolder = Folder;
            iconFile = file;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (isFolder(value)) {
                setIcon(iconFolder);
            } else {
                setIcon(iconFile);
            }

            return this;
        }

        protected boolean isFolder(Object value) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            clsNote nodeInfo = (clsNote) (node.getUserObject());
            return nodeInfo.boolFolder;
        }
    }
    /*
     * derp
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddFolder;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnRename;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblContent;
    private javax.swing.JTree treeCategory;
    private javax.swing.JTextArea txtInformation;
    // End of variables declaration//GEN-END:variables
}
