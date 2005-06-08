/*
 * $Id: JPreferencesManagerFrame.java,v 1.9 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.prefs.ui;

import com.syrus.util.prefs.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/08 13:49:06 $
 * @author $Author: bass $
 * @deprecated
 * @module util
 */
public class JPreferencesManagerFrame extends JFrame {
	private static final long serialVersionUID = 4194609427306425958L;
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
		
		this.jSplitPane1 = new JSplitPane();
		this.jScrollPane1 = new JScrollPane();
		this.jTree1 = new JTree();
		this.jPanel1 = new JPanel();
		this.jTextField1 = new JTextField();
		this.jSeparator1 = new JSeparator();
		this.jPanel2 = new javax.swing.JPanel();
		this.jButton1 = new javax.swing.JButton();
		this.jButton2 = new javax.swing.JButton();
		this.jPanel3 = new JPanel();
		this.jButton3 = new JButton();
		this.jButton4 = new JButton();
		this.jButton5 = new JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Preferences Manager");
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				exitForm();
			}
		});
		
		this.jTree1.setModel(new DefaultTreeModel(PreferencesManager.getTreeNode(), true));
		this.jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.jTree1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) (e.getPath().getLastPathComponent());
				Object key = defaultMutableTreeNode.getUserObject();
				if ((e.isAddedPath()) && (key instanceof String)) {
					JPreferencesManagerFrame.this.jButton3.setEnabled(true);
					JPreferencesManagerFrame.this.jTextField1.setEnabled(true);
					JPreferencesManagerFrame.this.jTextField1.setText(((PreferencesManager.PreferencesHolder) (((DefaultMutableTreeNode) (defaultMutableTreeNode.getParent())).getUserObject())).getPreferences().get((String) key, ""));
				} else {
					JPreferencesManagerFrame.this.jButton3.setEnabled(false);
					JPreferencesManagerFrame.this.jTextField1.setEnabled(false);
					JPreferencesManagerFrame.this.jTextField1.setText("");
				}
			}
		});
		this.jScrollPane1.setViewportView(this.jTree1);
		
		this.jSplitPane1.setLeftComponent(this.jScrollPane1);
		
		this.jPanel1.setLayout(new java.awt.GridBagLayout());
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		this.jPanel1.add(this.jTextField1, gridBagConstraints);
		
		this.jSplitPane1.setRightComponent(this.jPanel1);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(this.jSplitPane1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(this.jSeparator1, gridBagConstraints);
		
		this.jPanel2.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		this.jButton1.setMnemonic('N');
		this.jButton1.setText("New...");
		this.jButton1.setDefaultCapable(false);
		this.jButton1.setEnabled(false);
		this.jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton1ActionPerformed();
			}
		});
		
		this.jPanel2.add(this.jButton1);
		
		this.jButton2.setMnemonic('L');
		this.jButton2.setText("Delete");
		this.jButton2.setDefaultCapable(false);
		this.jButton2.setEnabled(false);
		this.jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton2ActionPerformed();
			}
		});
		
		this.jPanel2.add(this.jButton2);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		getContentPane().add(this.jPanel2, gridBagConstraints);
		
		this.jPanel3.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		this.jButton3.setMnemonic('A');
		this.jButton3.setText("Apply");
		this.jButton3.setEnabled(false);
		this.jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton3ActionPerformed();
			}
		});
		
		this.jPanel3.add(this.jButton3);
		
		this.jButton4.setMnemonic('D');
		this.jButton4.setText("Dismiss");
		this.jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton4ActionPerformed();
			}
		});
		
		this.jPanel3.add(this.jButton4);
		
		this.jButton5.setMnemonic('H');
		this.jButton5.setText("Help");
		this.jButton5.setEnabled(false);
		this.jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton5ActionPerformed();
			}
		});
		
		this.jPanel3.add(this.jButton5);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(this.jPanel3, gridBagConstraints);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-512)/2, (screenSize.height-384)/2, 512, 384);
	}//GEN-END:initComponents

	void jButton2ActionPerformed() {//GEN-FIRST:event_jButton2ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton2ActionPerformed

	void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton1ActionPerformed

	void jButton5ActionPerformed() {//GEN-FIRST:event_jButton5ActionPerformed
		// Add your handling code here:
	}//GEN-LAST:event_jButton5ActionPerformed

	void jButton4ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
		exitForm();
	}//GEN-LAST:event_jButton4ActionPerformed

	void jButton3ActionPerformed() {//GEN-FIRST:event_jButton3ActionPerformed
		DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) (this.jTree1.getSelectionPath().getLastPathComponent());
		Preferences preferences = ((PreferencesManager.PreferencesHolder) (((DefaultMutableTreeNode) (defaultMutableTreeNode.getParent())).getUserObject())).getPreferences();
		preferences.put((String) (defaultMutableTreeNode.getUserObject()), this.jTextField1.getText());
		try {
			preferences.flush();
		} catch (BackingStoreException bse) {
			bse.printStackTrace();
		}
	}//GEN-LAST:event_jButton3ActionPerformed

	void exitForm() {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSplitPane jSplitPane1;
	javax.swing.JTextField jTextField1;
	private javax.swing.JTree jTree1;
	// End of variables declaration//GEN-END:variables
}
