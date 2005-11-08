/*-
* $Id: DefaultPopupMessageReceiver.java,v 1.2 2005/11/08 08:15:31 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/08 08:15:31 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class DefaultPopupMessageReceiver implements PopupMessageReceiver {
	
	private DefaultListModel	model;

	JDialog	dialog;
	
	public DefaultPopupMessageReceiver() {
		this.createUI();
	}

	public void receiveMessage(final Event event) {
		if (event instanceof PopupNotificationEvent) {
			final PopupNotificationEvent popupNotificationEvent = 
				(PopupNotificationEvent) event;			
			this.addMessage(popupNotificationEvent.getMessage());
		}		
	}
	
	void cleanMessages() {
		this.model.clear();		
	}
	
	void addMessage(final String message) {
		this.model.addElement(message);
		if (!this.dialog.isVisible()) {
			this.dialog.setVisible(true);
		}
	}
	
	private void createUI() {
		this.model = new DefaultListModel();
		final JList list = new JList(this.model);
		this.dialog = new JDialog();
		this.dialog.setTitle(
			I18N.getString("Common.ClientServantManager.NotificationMessage"));
		this.dialog.setLocationRelativeTo(this.dialog.getParent());
		
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(list, gbc);
		
		final JButton button = new JButton(I18N.getString("Common.Button.OK"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanMessages();
				DefaultPopupMessageReceiver.this.dialog.setVisible(false);
			}
		});
		
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;

		panel.add(button, gbc);		
		
		this.dialog.getContentPane().add(panel);
		this.dialog.pack();
	}
}

