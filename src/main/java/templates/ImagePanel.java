/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import xeed.Character;
import xeed.XEED;

/**
 *
 * @author Erik
 */
public class ImagePanel extends javax.swing.JPanel {

    public Character character;
    public String itemIdentifier;
    private String szName;
    private final int ImageMaxHeight = 194;
    private final int ImageMaxWidth = 167;

    public ImagePanel(Character c, String id, String name) {
        itemIdentifier = id;
        character = c;
        initComponents();
        szName = name;
        lblImage.setText("Ctrl + click to set " + name);
        lblImage.setToolTipText(name + ". Alt + click to clear.");
        LoadData();
    }

    public void SaveData() {

        if (character == null) {
            return;
        }
        character.imgData.put(itemIdentifier, lblImage.getIcon());
        Character[] affectedcharacters = new Character[1];
        affectedcharacters[0] = character;
        XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);
    }

    public void LoadData() {

        if (character == null) {
            return;
        }

        Object o = character.imgData.get(itemIdentifier);

        if (o == null) {
            return;
        }

        if (o.getClass().equals(ImageIcon.class)) {
            lblImage.setText("");
            ImageIcon i = XEED.RescaleImageIcon((ImageIcon) o, ImageMaxWidth, ImageMaxHeight);
            lblImage.setIcon(i);
            if (XEED.boolResize) {
                character.imgData.put(itemIdentifier, i);
            }
        } else {
            System.out.println(szName + " loaded invalid data");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblImage.setBackground(new java.awt.Color(255, 255, 255));
        lblImage.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("Click to set image");
        lblImage.setToolTipText("Ctrl + click to set. Alt + click to remove.");
        lblImage.setOpaque(true);
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseClicked

        if (evt.getClickCount() == 1) {

            if (evt.isControlDown()) {

                JFileChooser fc = new JFileChooser();
                fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
                fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "gif"));
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setMultiSelectionEnabled(false);

                int intRet = fc.showOpenDialog(null);
                if (intRet != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                ImageIcon i = new ImageIcon(fc.getSelectedFile().getAbsolutePath());
                if (XEED.boolResize) {
                    i = XEED.RescaleImageIcon(i, ImageMaxWidth, ImageMaxHeight);
                }

                lblImage.setText("");
                lblImage.setIcon(i);
                SaveData();
                if (!XEED.boolResize) {
                    LoadData();
                }
                return;
            }

            if (evt.isAltDown()) {
                lblImage.setText("Ctrl + click to set " + szName);
                lblImage.setIcon(null);
                SaveData();
                return;
            }
        }

    }//GEN-LAST:event_lblImageMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblImage;
    // End of variables declaration//GEN-END:variables
}
