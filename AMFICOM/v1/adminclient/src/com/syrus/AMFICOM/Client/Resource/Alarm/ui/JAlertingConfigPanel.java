/*
 * $Id: JAlertingConfigPanel.java,v 1.1 2004/06/24 10:53:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * @todo When deleting from alertingmessageuserlinks, also delete from
 *       alertings (prior to link removal), then perform sanity check upon
 *       alertingmessagetexts.
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
public final class JAlertingConfigPanel extends JPanel {
	/**
	 * Event source currently selected.
	 */
	private EventSourceImpl eventSource;
	
	/**
	 * Event type currently selected (based on event source and alarm type
	 * selected).
	 */
	private EventTypeImpl eventType;

	/**
	 * Alerting message user links currently selected (based on user selected).
	 */
	private AlertingMessageUserLinkImpl alertingMessageUserLinks[];

	public JAlertingConfigPanel() {
		initComponents();
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jAddUserDialog1 = null;
		jScrollPane1 = new JScrollPane();
		jTree1 = new JTree() {
			public void updateUI() {
				super.updateUI();
				setCellRenderer(new DefaultTreeCellRenderer() {
					public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
						super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
						Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
						if (userObject instanceof EventSourceTypeImpl) {
							if (sel)
								setForeground(UIManager.getColor("controlLightShadow"));
							else
								setForeground(Color.BLUE);
							setToolTipText(((EventSourceTypeImpl) userObject).getToolTipText());
						} else if (userObject instanceof EventSourceImpl) {
							if (sel)
								setForeground(UIManager.getColor("controlLightShadow"));
							else
								setForeground(Color.GREEN);
							setToolTipText(((EventSourceImpl) userObject).getToolTipText());
						} else
							if (sel)
								setForeground(UIManager.getColor("controlLightShadow"));
						try {
							setToolTipText(userObject.toString());
						} catch (NullPointerException npe) {
							;
						}
						return this;
					}
				});
			}
		};
		alarmsScrollPane = new JScrollPane();
		alarmsList = new JObjectResourceList();
		usersScrollPane = new JScrollPane();
		usersList = new JObjectResourceList();
		addremovePanel = new JPanel();
		addUserButton = new JButton();
		removeUserButton = new JButton();
		msgPropPanel = new JPanel();
		msgLabel = new JLabel();
		messageTypeComboBox = new JObjectResourceComboBox();
		alertLabel = new JLabel();
		alertTypeComboBox = new JObjectResourceComboBox();
		jScrollPane4 = new JScrollPane();
		messageTextPane = new JTextArea();
		jSeparator1 = new JSeparator();
		jPanel1 = new javax.swing.JPanel();
		jApplyButton3 = new JButton();
		jCancelButton4 = new javax.swing.JButton();
		jHelpButton5 = new javax.swing.JButton();
		
		
		setLayout(new java.awt.GridBagLayout());
		
		jScrollPane1.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Event_Sources")), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		defaultMutableTreeNode1.add(new DefaultMutableTreeNode(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Loading"), false));
		jTree1.setModel(defaultTreeModel1);
		jTree1.setRootVisible(false);
		jTree1.setShowsRootHandles(true);
		jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		new Thread() {
			public void run() {
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(null, true);
				EventSourceTypeImpl eventSourceTypes[] = EventSourceTypeImpl.getEventSourceTypes();
				for (int i = 0; i < eventSourceTypes.length; i ++) {
					DefaultMutableTreeNode eventSourceTypeNode = new DefaultMutableTreeNode(eventSourceTypes[i], true);
					eventSourceTypeNode.add(new DefaultMutableTreeNode(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Loading"), false));
					root.add(eventSourceTypeNode);
				}
				defaultTreeModel1.setRoot(root);
			}
		}.start();
		jTree1.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
			public void treeCollapsed(javax.swing.event.TreeExpansionEvent e) {
			}
			public void treeExpanded(javax.swing.event.TreeExpansionEvent e) {
				jTree1TreeExpanded(e);
			}
		});
		jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
				jTree1ValueChanged(e);
			}
		});
		
		jScrollPane1.setViewportView(jTree1);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
		add(jScrollPane1, gridBagConstraints);
		
		alarmsScrollPane.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Alarm_Types")), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		defaultObjectResourceListModel1.addElement(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Loading"));
		alarmsList.setModel(defaultObjectResourceListModel1);
		alarmsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		new Thread() {
			public void run() {
				Thread thread1 = new Thread() {
					public void run() {
						MessageTypeImpl messageTypes[] = MessageTypeImpl.getMessageTypes();
						defaultComboBoxModel1.removeAllElements();
						for (int i = 0; i < messageTypes.length; i ++)
							defaultComboBoxModel1.addElement(messageTypes[i]);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								messageTypeComboBox.updateUI();
							}
						});
					}
				};
				Thread thread2 = new Thread() {
					public void run() {
						AlertingTypeImpl alertingTypes[] = AlertingTypeImpl.getAlertingTypes();
						defaultComboBoxModel2.removeAllElements();
						for (int i = 0; i < alertingTypes.length; i ++)
							defaultComboBoxModel2.addElement(alertingTypes[i]);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								alertTypeComboBox.updateUI();
							}
						});
					}
				};
				thread1.start();
				thread2.start();
				
				AlarmTypeImpl alarmTypes[] = AlarmTypeImpl.getAlarmTypes();
				
				try {
					thread1.join();
				} catch (InterruptedException ie) {
					;
				}
				try {
					thread2.join();
				} catch (InterruptedException ie) {
					;
				}
				
				defaultObjectResourceListModel1.removeAllElements();
				for (int i = 0; i < alarmTypes.length; i ++)
					defaultObjectResourceListModel1.addElement(alarmTypes[i]);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						alarmsList.updateUI();
					}
				});
			}
		}.start();
		alarmsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				alarmsListValueChanged(e);
			}
		});
		
		alarmsScrollPane.setViewportView(alarmsList);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 5);
		add(alarmsScrollPane, gridBagConstraints);
		
		usersScrollPane.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Users")), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		usersList.setModel(defaultObjectResourceListModel2);
		usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				usersListValueChanged(e);
			}
		});
		
		usersScrollPane.setViewportView(usersList);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 10);
		add(usersScrollPane, gridBagConstraints);
		
		addremovePanel.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		addUserButton.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Add....Mnemonic").charAt(0));
		addUserButton.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Add..."));
		addUserButton.setDefaultCapable(false);
		addUserButton.setEnabled(false);
		addUserButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				addUserButtonActionPerformed(e);
			}
		});
		
		addremovePanel.add(addUserButton);
		
		removeUserButton.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Remove.Mnemonic").charAt(0));
		removeUserButton.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Remove"));
		removeUserButton.setDefaultCapable(false);
		removeUserButton.setEnabled(false);
		removeUserButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				removeUserButtonActionPerformed(e);
			}
		});
		
		addremovePanel.add(removeUserButton);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
		add(addremovePanel, gridBagConstraints);
		
		msgPropPanel.setLayout(new java.awt.GridBagLayout());
		
		msgPropPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Notification_Options")));
		msgLabel.setDisplayedMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Message_Type_.Mnemonic").charAt(0));
		msgLabel.setLabelFor(messageTypeComboBox);
		msgLabel.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Message_Type_"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
		msgPropPanel.add(msgLabel, gridBagConstraints);
		
		defaultComboBoxModel1.addElement(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Loading"));
		messageTypeComboBox.setModel(defaultComboBoxModel1);
		messageTypeComboBox.setEnabled(false);
		messageTypeComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {
				messageTypeComboBoxFocusGained(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		msgPropPanel.add(messageTypeComboBox, gridBagConstraints);
		
		alertLabel.setDisplayedMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Alerting_Type.Mnemonic").charAt(0));
		alertLabel.setLabelFor(alertTypeComboBox);
		alertLabel.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Alerting_Type"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		msgPropPanel.add(alertLabel, gridBagConstraints);
		
		defaultComboBoxModel2.addElement(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Loading"));
		alertTypeComboBox.setModel(defaultComboBoxModel2);
		alertTypeComboBox.setEnabled(false);
		alertTypeComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {
				alertTypeComboBoxFocusGained(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		msgPropPanel.add(alertTypeComboBox, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
		add(msgPropPanel, gridBagConstraints);
		
		jScrollPane4.setViewportBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Alerting_Message_Text")), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
		messageTextPane.setBackground(UIManager.getColor("text"));
		messageTextPane.setEditable(false);
		messageTextPane.setTabSize(4);
		messageTextPane.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {
				messageTextPaneFocusGained(e);
			}
		});
		
		jScrollPane4.setViewportView(messageTextPane);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
		add(jScrollPane4, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		add(jSeparator1, gridBagConstraints);
		
		jPanel1.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
		
		jApplyButton3.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Apply.Mnemonic").charAt(0));
		jApplyButton3.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Apply"));
		jApplyButton3.setEnabled(false);
		jApplyButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jApplyButton3ActionPerformed(e);
			}
		});
		
		jPanel1.add(jApplyButton3);
		
		jCancelButton4.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Cancel.Mnemonic").charAt(0));
		jCancelButton4.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Cancel"));
		jCancelButton4.setEnabled(false);
		jCancelButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jCancelButton4ActionPerformed(e);
			}
		});
		
		jPanel1.add(jCancelButton4);
		
		jHelpButton5.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Help.Mnemonic").charAt(0));
		jHelpButton5.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Help"));
		jHelpButton5.setEnabled(false);
		jHelpButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jHelpButton5ActionPerformed(e);
			}
		});
		
		jPanel1.add(jHelpButton5);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
		add(jPanel1, gridBagConstraints);
		
	}//GEN-END:initComponents

	private void jHelpButton5ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jHelpButton5ActionPerformed

	}//GEN-LAST:event_jHelpButton5ActionPerformed

	private void jCancelButton4ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jCancelButton4ActionPerformed
		setContentUpdated(false);
	}//GEN-LAST:event_jCancelButton4ActionPerformed

	private void messageTextPaneFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_messageTextPaneFocusGained
		if (messageTextPane.isEditable())
			setContentUpdating(true);
	}//GEN-LAST:event_messageTextPaneFocusGained

	private void alertTypeComboBoxFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_alertTypeComboBoxFocusGained
		if (alertTypeComboBox.isEnabled())
			setContentUpdating(true);
	}//GEN-LAST:event_alertTypeComboBoxFocusGained

	private void messageTypeComboBoxFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_messageTypeComboBoxFocusGained
		if (messageTypeComboBox.isEnabled())
			setContentUpdating(true);
	}//GEN-LAST:event_messageTypeComboBoxFocusGained

	private void usersListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_usersListValueChanged
		try {
			if (e.getValueIsAdjusting())
				return;
		} catch (NullPointerException npe) {
			;
		}
		Object selectedValue = usersList.getSelectedValue();
		if (selectedValue instanceof UserImpl) {
			removeUserButton.setEnabled(true);
			messageTypeComboBox.setEnabled(true);
			alertTypeComboBox.setEnabled(true);
			messageTextPane.setEditable(true);
			
			AlertingMessageTextImpl alertingMessageTexts[] = AlertingMessageTextImpl.getMatching(eventType);
			AlertingMessageTextImpl alertingMessageText = null;
			for (int i = 0; i < alertingMessageTexts.length; i ++) {
				AlertingMessageUserLinkImpl alertingMessageUserLinks[] = AlertingMessageUserLinkImpl.getMatching(alertingMessageTexts[i], eventSource, (UserImpl) selectedValue);
				/*
				 * Just find the first occurence. No sanity checks.
				 */
				if (alertingMessageUserLinks.length > 0) {
					this.alertingMessageUserLinks = alertingMessageUserLinks;
					alertingMessageText = alertingMessageTexts[i];
					break;
				}
				/**
				 * @todo must be an assertion
				 */
				if (i == (alertingMessageTexts.length - 1)) {
					System.err.println("WARNING: database integrity problem.");
					this.alertingMessageUserLinks = new AlertingMessageUserLinkImpl[0];
					return;
				}
			}

			alertTypeComboBox.setSelectedItem(alertingMessageUserLinks[0].getAlertingTypeId());
			messageTypeComboBox.setSelectedItem(alertingMessageText.getMessageTypeId());
			messageTextPane.setText(alertingMessageText.getText());
			messageTextPane.setToolTipText(alertingMessageText.getToolTipText());
		} else {
			removeUserButton.setEnabled(false);
			messageTypeComboBox.setEnabled(false);
			alertTypeComboBox.setEnabled(false);
			messageTextPane.setEditable(false);

			alertingMessageUserLinks = new AlertingMessageUserLinkImpl[0];

			/**
			 * @todo Move view resetting to a separate method???
			 */
			alertTypeComboBox.setSelectedIndex(0);
			messageTypeComboBox.setSelectedIndex(0);
			messageTextPane.setText("");
			messageTextPane.setToolTipText(null);
		}
	}//GEN-LAST:event_usersListValueChanged

	private void jApplyButton3ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jApplyButton3ActionPerformed
		setContentUpdated(true);
	}//GEN-LAST:event_jApplyButton3ActionPerformed

	private void removeUserButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_removeUserButtonActionPerformed
		for (int i = 0; i < alertingMessageUserLinks.length; i ++)
			try {
				AlertingMessageUserLinkImpl.delete(alertingMessageUserLinks[i].getId());
			} catch (DatabaseAccessException dae) {
				dae.printStackTrace();
			}
		populateUserList();
	}//GEN-LAST:event_removeUserButtonActionPerformed

	private void addUserButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addUserButtonActionPerformed
		LinkedList allUsers = new LinkedList();
		LinkedList availableUsers = new LinkedList();
		LinkedList selectedUsers = new LinkedList();

		UserImpl _allUsers[] = UserImpl.getUsers();
		for (int i = 0; i < _allUsers.length; i ++)
			allUsers.addLast(_allUsers[i]);
		for (int i = 0; i < defaultObjectResourceListModel2.size(); i ++)
			selectedUsers.addLast(defaultObjectResourceListModel2.get(i));

		try {
			while (true) {
				Object user = allUsers.removeFirst();
				try {
					selectedUsers.remove(selectedUsers.indexOf(user));
				} catch (IndexOutOfBoundsException ioobe) {
					availableUsers.addLast(user);
				}
			}
		} catch (NoSuchElementException nsee) {
			;
		}

		try {
			jAddUserDialog1.setAlertingConfigPanel(this);
			jAddUserDialog1.setUsers(availableUsers);
			jAddUserDialog1.pack();
			jAddUserDialog1.setVisible(true);
		} catch (NullPointerException npe) {
			Container parent = this;
			while (true) {
				if (parent == null)
					jAddUserDialog1 = new JAddUserDialog();
				else if (parent instanceof Dialog)
					jAddUserDialog1 = new JAddUserDialog((Dialog) parent);
				else if (parent instanceof Frame)
					jAddUserDialog1 = new JAddUserDialog((Frame) parent);
				else {
					parent = parent.getParent();
					continue;
				}
				break;
			}
			jAddUserDialog1.setAlertingConfigPanel(this);
			jAddUserDialog1.setUsers(availableUsers);
			jAddUserDialog1.pack();
			jAddUserDialog1.setVisible(true);
		}
	}//GEN-LAST:event_addUserButtonActionPerformed

	private void alarmsListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_alarmsListValueChanged
		if (e.getValueIsAdjusting())
			return;
		populateUserList();
	}//GEN-LAST:event_alarmsListValueChanged

	private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent e) {//GEN-FIRST:event_jTree1ValueChanged
		populateUserList();
	}//GEN-LAST:event_jTree1ValueChanged

	private void jTree1TreeExpanded(javax.swing.event.TreeExpansionEvent e) {//GEN-FIRST:event_jTree1TreeExpanded
		final DefaultMutableTreeNode eventSourceTypeNode = (DefaultMutableTreeNode) (e.getPath().getPathComponent(1));
		/*
		 * This is a lengthy check, more than one thread may enter this block at
		 * once. Using a linked list to store nodes that have already been
		 * expanded.
		 */
//		if ((eventSourceTypeNode.getChildCount() == 1) && (! (((DefaultMutableTreeNode) (eventSourceTypeNode.getChildAt(0))).getUserObject() instanceof EventSourceImpl)))
		synchronized (expandedTreeNodeList) {
			if (! expandedTreeNodeList.contains(eventSourceTypeNode)) {
				expandedTreeNodeList.addLast(eventSourceTypeNode);
				new Thread() {
					public void run() {
						try {
							interrupted();
							sleep(5000);
						} catch (InterruptedException ie) {
							;
						}
						eventSourceTypeNode.remove(0);
						EventSourceTypeImpl eventSourceType = (EventSourceTypeImpl) eventSourceTypeNode.getUserObject();
						EventSourceImpl eventSources[] = EventSourceImpl.getMatching(eventSourceType);
						for (int i = 0; i < eventSources.length; i ++)
							eventSourceTypeNode.add(new DefaultMutableTreeNode(eventSources[i], false));
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								jTree1.updateUI();
							}
						});
					}
				}.start();
			}
		}
	}//GEN-LAST:event_jTree1TreeExpanded

	/**
	 * Populates user list according to selections made in event source and
	 * alarm type lists. This method should only be invoked from
	 * event-dispatching thread.
	 */
	void populateUserList() {
		AlertingMessageTextImpl alertingMessageTexts[];
		Object selectedValue = alarmsList.getSelectedValue();
		if (selectedValue instanceof AlarmTypeImpl) {
			TreePath selectionPath = jTree1.getSelectionPath();
			if (selectionPath == null) {
				eventSource = null;
				eventType = null;
				alertingMessageTexts = new AlertingMessageTextImpl[0];
				addUserButton.setEnabled(false);
			} else {
				Object userObject = ((DefaultMutableTreeNode) (selectionPath.getLastPathComponent())).getUserObject();
				if (userObject instanceof EventSourceImpl) {
					eventSource = (EventSourceImpl) userObject;
					eventType = EventTypeImpl.getSelectedEventType(eventSource, (AlarmTypeImpl) selectedValue);
					alertingMessageTexts = AlertingMessageTextImpl.getMatching(eventType);
					if (eventType == null)
						addUserButton.setEnabled(false);
					else
						addUserButton.setEnabled(true);
				} else if (userObject instanceof EventSourceTypeImpl) {
					eventSource = null;
//					eventType = EventTypeImpl.getSelectedEventType((EventSourceTypeImpl) userObject, (AlarmTypeImpl) selectedValue);
					eventType = null;
//					alertingMessageTexts = AlertingMessageTextImpl.getMatching(eventType);
					alertingMessageTexts = new AlertingMessageTextImpl[0];
					addUserButton.setEnabled(false);
				} else {
					eventSource = null;
					eventType = null;
					alertingMessageTexts = new AlertingMessageTextImpl[0];
					addUserButton.setEnabled(false);
				}
			}
		} else {
			eventSource = null;
			eventType = null;
			alertingMessageTexts = new AlertingMessageTextImpl[0];
			addUserButton.setEnabled(false);
		}

		/*
		 * alertingMessageUserLinks variable declared below is a local one.
		 * Not to be confused with the corresponding field!
		 */
		defaultObjectResourceListModel2.removeAllElements();
		AlertingMessageUserLinkImpl alertingMessageUserLinks[];
		for (int i = 0; i < alertingMessageTexts.length; i ++) {
			alertingMessageUserLinks = AlertingMessageUserLinkImpl.getMatching(alertingMessageTexts[i], eventSource);
			for (int j = 0; j < alertingMessageUserLinks.length; j ++) {
				UserImpl user = alertingMessageUserLinks[j].getUserId();
				/*
				 * This check is valid only provided there exists only one local
				 * copy of a certain database record, i. e. users are equal if
				 * they are two different references to the same object.
				 * Currently, this assumption is valid.
				 */
				if (defaultObjectResourceListModel2.contains(user))
					continue;
				else
					defaultObjectResourceListModel2.addElement(user);
			}
		}
		usersList.updateUI();
	}

	private void setContentUpdating(boolean contentUpdating) {
		jTree1.setEnabled(! contentUpdating);
		alarmsList.setEnabled(! contentUpdating);
		usersList.setEnabled(! contentUpdating);
		addUserButton.setEnabled(! contentUpdating);
		removeUserButton.setEnabled(! contentUpdating);
		jApplyButton3.setEnabled(contentUpdating);
		jCancelButton4.setEnabled(contentUpdating);
	}

	private void setContentUpdated(boolean contentUpdated) {
		setContentUpdating(false);
		if (contentUpdated) {
			/**
			 * @todo Update model from view.
			 * @bug No need to update view from model here.
			 */
			try {
				alertingMessageUserLinks[0].setAlertingTypeId((AlertingTypeImpl) (alertTypeComboBox.getSelectedItem()));
			} catch (DatabaseAccessException dae) {
				dae.printStackTrace();
			}
			AlertingMessageTextImpl alertingMessageId = null;
			/**
			 * @todo must be a pref
			 */
			final boolean createNewAlertingMessageTexts = true;
			if (createNewAlertingMessageTexts) {
				try {
					alertingMessageId = AlertingMessageTextImpl.AlertingMessageTextImpl(eventType, (MessageTypeImpl) (messageTypeComboBox.getSelectedItem()));
				} catch (DatabaseAccessException dae) {
					dae.printStackTrace();
				} catch (Exception e) {
					;
				}
				try {
					alertingMessageUserLinks[0].setAlertingMessageId(alertingMessageId);
				} catch (DatabaseAccessException dae) {
					dae.printStackTrace();
				}
			} else {
				alertingMessageId = alertingMessageUserLinks[0].getAlertingMessageId();
				try {
					alertingMessageId.setMessageTypeId((MessageTypeImpl) (messageTypeComboBox.getSelectedItem()));
				} catch (DatabaseAccessException dae) {
					dae.printStackTrace();
				}
			}
			try {
				alertingMessageId.setText(messageTextPane.getText());
			} catch (DatabaseAccessException dae) {
				dae.printStackTrace();
			} catch (NullPointerException npe) {
				;
			}
		} else
			/**
			 * @todo Update view from model.
			 */
//			System.out.println("Changes discarded...");
			;

		usersListValueChanged(null);
	}

	EventSourceImpl getEventSource() {
		return eventSource;
	}

	EventTypeImpl getEventType() {
		return eventType;
	}

	/**
	 * ComboBox model for {@link messageTypeComboBox}
	 */
	private DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();

	/**
	 * ComboBox model for {@link alertTypeComboBox}
	 */
	private DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();

	/**
	 * List model for {@link alarmsList}
	 */
	private DefaultObjectResourceListModel defaultObjectResourceListModel1 = new DefaultObjectResourceListModel();
	
	/**
	 * List model for {@link usersList} 
	 */
	private DefaultObjectResourceListModel defaultObjectResourceListModel2 = new DefaultObjectResourceListModel();
	private DefaultMutableTreeNode defaultMutableTreeNode1 = new DefaultMutableTreeNode(null, true);
	private DefaultTreeModel defaultTreeModel1 = new DefaultTreeModel(defaultMutableTreeNode1, true);

	/**
	 * The list to store tree nodes that have already been expanded.
	 *
	 * @see #jTree1TreeExpanded(TreeExpansionEvent)
	 */
	private LinkedList expandedTreeNodeList = new LinkedList();

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton addUserButton;
	private javax.swing.JPanel addremovePanel;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList alarmsList;
	private javax.swing.JScrollPane alarmsScrollPane;
	private javax.swing.JLabel alertLabel;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox alertTypeComboBox;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JAddUserDialog jAddUserDialog1;
	private javax.swing.JButton jApplyButton3;
	private javax.swing.JButton jCancelButton4;
	private javax.swing.JButton jHelpButton5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTree jTree1;
	private javax.swing.JTextArea messageTextPane;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox messageTypeComboBox;
	private javax.swing.JLabel msgLabel;
	private javax.swing.JPanel msgPropPanel;
	private javax.swing.JButton removeUserButton;
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList usersList;
	private javax.swing.JScrollPane usersScrollPane;
	// End of variables declaration//GEN-END:variables
}
