/*
 * $Id: JPreferencesManagerFrame.java,v 1.3 2004/06/01 14:09:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.prefs.ui;

import com.syrus.util.prefs.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/01 14:09:15 $
 * @author $Author: bass $
 * @module util
 */
public class JPreferencesManagerFrame extends javax.swing.JFrame {
	static {
		try {
			Class.forName(PreferencesManager.class.getName());
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public JPreferencesManagerFrame() {
		initComponents();
	}
	
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jSplitPane1 = new JSplitPane();
		jScrollPane1 = new JScrollPane();
		jTree1 = new JTree();
		jPanel1 = new JPanel();
		jTextField1 = new JTextField();
		jSeparator1 = new JSeparator();
		jPanel2 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel3 = new JPanel();
		jButton3 = new JButton();
		jButton4 = new JButton();
		jButton5 = new JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Preferences Manager");
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				exitForm(e);
			}
		});
		
		jTree1.setModel(new DefaultTreeModel(PreferencesManager.getTreeNode(), true));
		jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) (e.getPath().getLastPathComponent());
				Object key = defaultMutableTreeNode.getUserObject();
				if ((e.isAddedPath()) && (key instanceof String)) {
					jButton3.setEnabled(true);
					jTextField1.setEnabled(true);
					jTextField1.setText(((PreferencesManager.PreferencesHolder) (((DefaultMutableTreeNode) (defaultMutableTreeNode.getParent())).getUserObject())).getPreferences().get((String) key, ""));
				} else {
					jButton3.setEnabled(false);
					jTextField1.setEnabled(false);
					jTextField1.setText("");
				}
			}
		});
		jScrollPane1.setViewportView(jTree1);
		
		jSplitPane1.setLeftComponent(jScrollPane1);
		
		jPanel1.setLayout(new java.awt.GridBagLayout());
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanel1.add(jTextField1, gridBagConstraints);
		
		jSplitPane1.setRightComponent(jPanel1);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(jSplitPane1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jSeparator1, gridBagConstraints);
		
		jPanel2.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		jButton1.setMnemonic('N');
		jButton1.setText("New...");
		jButton1.setDefaultCapable(false);
		jButton1.setEnabled(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton1ActionPerformed(e);
			}
		});
		
		jPanel2.add(jButton1);
		
		jButton2.setMnemonic('L');
		jButton2.setText("Delete");
		jButton2.setDefaultCapable(false);
		jButton2.setEnabled(false);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton2ActionPerformed(e);
			}
		});
		
		jPanel2.add(jButton2);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		getContentPane().add(jPanel2, gridBagConstraints);
		
		jPanel3.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		jButton3.setMnemonic('A');
		jButton3.setText("Apply");
		jButton3.setEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton3ActionPerformed(e);
			}
		});
		
		jPanel3.add(jButton3);
		
		jButton4.setMnemonic('D');
		jButton4.setText("Dismiss");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton4ActionPerformed(e);
			}
		});
		
		jPanel3.add(jButton4);
		
		jButton5.setMnemonic('H');
		jButton5.setText("Help");
		jButton5.setEnabled(false);
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton5ActionPerformed(e);
			}
		});
		
		jPanel3.add(jButton5);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel3, gridBagConstraints);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-512)/2, (screenSize.height-384)/2, 512, 384);
	}//GEN-END:initComponents

	private void jButton2ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton2ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton1ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton5ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton4ActionPerformed
		exitForm(null);
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton3ActionPerformed
		DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) (jTree1.getSelectionPath().getLastPathComponent());
		Preferences preferences = ((PreferencesManager.PreferencesHolder) (((DefaultMutableTreeNode) (defaultMutableTreeNode.getParent())).getUserObject())).getPreferences();
		preferences.put((String) (defaultMutableTreeNode.getUserObject()), jTextField1.getText());
		try {
			preferences.flush();
		} catch (BackingStoreException bse) {
			bse.printStackTrace();
		}
	}//GEN-LAST:event_jButton3ActionPerformed

	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTree jTree1;
	// End of variables declaration//GEN-END:variables
}
