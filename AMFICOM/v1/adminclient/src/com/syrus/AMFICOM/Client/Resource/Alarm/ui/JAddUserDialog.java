/*
 * $Id: JAddUserDialog.java,v 1.1 2004/06/24 10:53:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import com.syrus.AMFICOM.corba.portable.reflect.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
public final class JAddUserDialog extends JDialog {
	private JAlertingConfigPanel alertingConfigPanel;

	private static final long serialVersionUID = 3983493918789182581L;

	public JAddUserDialog() {
		initComponents();
	}

	public JAddUserDialog(Frame parent) {
		super(parent);
		initComponents();
	}

	public JAddUserDialog(Dialog parent) {
		super(parent);
		initComponents();
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jLabel1 = new JLabel();
		usersComboBox = new JObjectResourceComboBox();
		jSeparator1 = new JSeparator();
		jPanel1 = new JPanel();
		addUserButton = new JButton();
		cancelButton = new JButton();
		jButton3 = new JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("NewUser.Title"));
		setModal(true);
		setResizable(false);
		jLabel1.setDisplayedMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Select_User.Mnemonic").charAt(0));
		jLabel1.setLabelFor(usersComboBox);
		jLabel1.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Select_User"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
		getContentPane().add(jLabel1, gridBagConstraints);
		
		usersComboBox.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
		getContentPane().add(usersComboBox, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		getContentPane().add(jSeparator1, gridBagConstraints);
		
		jPanel1.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		addUserButton.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Add.Mnemonic").charAt(0));
		addUserButton.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Add"));
		addUserButton.setEnabled(false);
		addUserButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				addUserButtonActionPerformed(e);
			}
		});
		
		jPanel1.add(addUserButton);
		
		cancelButton.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Cancel.Mnemonic").charAt(0));
		cancelButton.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Cancel"));
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				cancelButtonActionPerformed(e);
			}
		});
		
		jPanel1.add(cancelButton);
		
		jButton3.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Help.Mnemonic").charAt(0));
		jButton3.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Help"));
		jButton3.setEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton3ActionPerformed(e);
			}
		});
		
		jPanel1.add(jButton3);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
		getContentPane().add(jPanel1, gridBagConstraints);
		
	}//GEN-END:initComponents

	private void jButton3ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton3ActionPerformed

	}//GEN-LAST:event_jButton3ActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelButtonActionPerformed
		dispose();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void addUserButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addUserButtonActionPerformed
		try {
			UserImpl user = (UserImpl) (usersComboBox.getSelectedItem());
			AlertingMessageTextImpl alertingMessageText = AlertingMessageTextImpl.AlertingMessageTextImpl(alertingConfigPanel.getEventType(), MessageTypeImpl.DEFAULT_MESSAGE_TYPE);
			AlertingMessageUserLinkImpl alertingMessageUserLink = AlertingMessageUserLinkImpl.AlertingMessageUserLinkImpl(alertingMessageText, AlertingTypeImpl.DEFAULT_ALERTING_TYPE, alertingConfigPanel.getEventSource(), user);
			alertingConfigPanel.populateUserList();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dispose();
		}
	}//GEN-LAST:event_addUserButtonActionPerformed

	void setAlertingConfigPanel(JAlertingConfigPanel alertingConfigPanel) {
		this.alertingConfigPanel = alertingConfigPanel;
	}

	/**
	 * Should be invoked before this dialog is visible.
	 */
	void setUsers(List users) {
		usersComboBox.removeAllItems();
		if ((users == null) || (users.size() == 0)) {
			usersComboBox.setEnabled(false);
			addUserButton.setEnabled(false);
		} else {
			Iterator iterator = users.iterator();
			try {
				while (true)
					usersComboBox.addItem(iterator.next());
			} catch (NoSuchElementException nsee) {
				;
			}
			usersComboBox.setEnabled(true);
			addUserButton.setEnabled(true);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton addUserButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JSeparator jSeparator1;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox usersComboBox;
	// End of variables declaration//GEN-END:variables
}
