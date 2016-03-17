/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.gui.plugin;

import com.imag.nespros.Simulation;
import com.imag.nespros.network.devices.Device;
import com.imag.nespros.network.routing.EPGraph;
import com.imag.nespros.runtime.client.EventConsumer;
import com.imag.nespros.runtime.core.EPUnit;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author epaln
 */
public class AddOperatorDialog extends javax.swing.JDialog {

    /**
     * Creates new form AddOperatorDialog
     */
    private Device device;

    public AddOperatorDialog(java.awt.Frame parent, Device device) {
        super(parent, true);
        this.device = device;
        initComponents();
        nodeNameLabel.setText(device.getDeviceName());
        setOperatorList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        operatorList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        nodeNameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Done");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        operatorList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        operatorList.setToolTipText("Undeployed operator list");
        jScrollPane1.setViewportView(operatorList);

        jLabel1.setText("Select operators you want to assign to node");

        nodeNameLabel.setText("node");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(nodeNameLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nodeNameLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        for (Iterator it = operatorList.getSelectedValuesList().iterator(); it.hasNext();) {
            EPUnit epu = (EPUnit) it.next();           
            device.addEPUnit(epu);
        }
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /*
     public static void main(String args[]) {
        
     try {
     for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
     if ("Nimbus".equals(info.getName())) {
     javax.swing.UIManager.setLookAndFeel(info.getClassName());
     break;
     }
     }
     } catch (ClassNotFoundException ex) {
     java.util.logging.Logger.getLogger(AddOperatorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
     java.util.logging.Logger.getLogger(AddOperatorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
     java.util.logging.Logger.getLogger(AddOperatorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (javax.swing.UnsupportedLookAndFeelException ex) {
     java.util.logging.Logger.getLogger(AddOperatorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
     }
     //</editor-fold>

        
     java.awt.EventQueue.invokeLater(new Runnable() {
     public void run() {
     AddOperatorDialog dialog = new AddOperatorDialog(new javax.swing.JFrame(), true);
     dialog.addWindowListener(new java.awt.event.WindowAdapter() {
     @Override
     public void windowClosing(java.awt.event.WindowEvent e) {
     System.exit(0);
     }
     });
     dialog.setVisible(true);
     }
     });
     }
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nodeNameLabel;
    private javax.swing.JList operatorList;
    // End of variables declaration//GEN-END:variables

    private void setOperatorList() {
        Vector<EPUnit> operators = new Vector<>();
        Vector<EPUnit> allOp = new Vector<>();        
        Simulation s = GraphEditor.simu;      
        allOp.addAll(s.getProducers());
        allOp.addAll(s.getConsumers());
        for(EventConsumer c: s.getConsumers()){
             allOp.addAll(c.getEPUList());                            
        }
        
        for (EPUnit epu : allOp) {
            if (!epu.isMapped() && epu.getDevice() == null) {
                if (epu.getUsedMemory() <= device.getRemainingMemory()) {
                    operators.add(epu);
                }
            }
        }
        operatorList.setListData(operators);
    }
}
