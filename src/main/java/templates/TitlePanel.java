/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

/**
 *
 * @author Erik
 */
public class TitlePanel extends javax.swing.JPanel {

    public TitlePanel(String name) {
        initComponents();
        lblName.setText(name);
    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblName = new javax.swing.JLabel();

      lblName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
      lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      lblName.setText("LABEL");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addContainerGap()
                  .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addContainerGap()));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            lblName));
   }// </editor-fold>//GEN-END:initComponents

   // Variables declaration - do not modify//GEN-BEGIN:variables
   public javax.swing.JLabel lblName;
   // End of variables declaration//GEN-END:variables
}
