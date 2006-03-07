/*-
* $Id: DefaultPopupMessageReceiver.java,v 1.4 2005/11/22 11:27:16 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/22 11:27:16 $
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
			this.addMessage(popupNotificationEvent);
		}		
	}
	
	void cleanMessages() {
		this.model.clear();		
	}
	
	void addMessage(final PopupNotificationEvent notificationEvent) {
		this.model.addElement(notificationEvent);
		if (!this.dialog.isVisible()) {
			this.dialog.setVisible(true);
		}
	}
	
	private void createUI() {
		this.model = new DefaultListModel();
		final JList list = new JList(this.model);
		list.setCellRenderer(new PopupNotificationCellRenderer());
		this.dialog = new JDialog();
		this.dialog.setTitle(
			I18N.getString("Common.ClientServantManager.NotificationMessage"));
		
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		final JScrollPane pane = new JScrollPane(list, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(pane, gbc);
		
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
		final GraphicsEnvironment localGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle maximumWindowBounds = 
			localGraphicsEnvironment.getMaximumWindowBounds();
		this.dialog.setSize(new Dimension(maximumWindowBounds.width / 3, 
			maximumWindowBounds.height / 3));
		
		this.dialog.setLocationRelativeTo(this.dialog.getParent());
	}
}

