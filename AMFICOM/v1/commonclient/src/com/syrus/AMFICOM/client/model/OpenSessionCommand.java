/*
 * $Id: OpenSessionCommand.java,v 1.25 2005/10/20 13:48:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.model;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Log;

/**
 * @author $Author: bob $
 * @version $Revision: 1.25 $, $Date: 2005/10/20 13:48:10 $
 * @module commonclient
 */
public class OpenSessionCommand extends AbstractCommand {

	protected Dispatcher			dispatcher;
	protected JPanel				mainPanel;
	protected JDialog				dialog;
	protected ActionListener		actionListener;
	String							okButton;
	String							cancelButton;
	JOptionPane						optionPane;

	protected JTextField			loginTextField;
	protected JPasswordField		passwordTextField;

	protected String				login;
	protected String				password;

	protected static final String	LOGIN_PROPERTY_KEY		= 
			"com.amficom.login";
	protected static final String	PASSWORD_PROPERTY_KEY	= 
			"com.amficom.password";

	protected boolean				logged					= false;

	public OpenSessionCommand(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		String loginProperty = System.getProperty(LOGIN_PROPERTY_KEY);
		String passwordProperty = System.getProperty(PASSWORD_PROPERTY_KEY);
		if (loginProperty != null) {
			this.login = loginProperty;
			this.password = passwordProperty;
			this.logged = true;
		}

	}

	@Override
	public void setParameter(String field, Object value) {
		if (field.equals("dispatcher"))
			this.setDispatcher((Dispatcher) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void execute() {

		boolean trying = false;
		do {
			try {
				trying = this.logging();
			} catch (final CommunicationException e) {
				Log.errorException(e);
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
//						LangModelGeneral.getString("Error.ServerConnection"),
						e.getMessage(),
						I18N.getString("Error.OpenSession"),
					JOptionPane.ERROR_MESSAGE, null);
				this.dispatcher.firePropertyChange(
					new StatusMessageEvent(this, 
						StatusMessageEvent.STATUS_PROGRESS_BAR,
						false));
				this.logged = false;
			} catch (final LoginException e) {
				Log.errorException(e);
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						e.getMessage(), 
						I18N.getString("Error.OpenSession"),
						JOptionPane.ERROR_MESSAGE, 
						null);
				this.dispatcher.firePropertyChange(
						new StatusMessageEvent(this, 
							StatusMessageEvent.STATUS_PROGRESS_BAR,
							false));
				this.logged = false;
			}			
		} while (!trying);
	}

	protected boolean logging() throws CommunicationException, LoginException {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Common.StatusBar.OpeningSession")));
		this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CHANGING_EVENT));

		if (!this.logged) {
			boolean wannaNotLogin = !this.showOpenSessionDialog(Environment.getActiveWindow());
			if (wannaNotLogin) {
				this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Common.StatusBar.Aborted")));
				return wannaNotLogin;
			}
		}

		final Dispatcher dispatcher1 = this.dispatcher;
		final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();

		if (clientSessionEnvironment == null) {
			throw new LoginException(I18N.getString("Error.SessionHasNotEstablish"));
		}
		
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Common.StatusBar.InitStartupData")));

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));

		clientSessionEnvironment.login(this.login, this.password);
		final Set availableDomains = LoginManager.getAvailableDomains();
		this.disposeDialog();
		this.logged = true;

		new ProcessingDialog(new Runnable() {

			public void run() {
				dispatcher1.firePropertyChange(
					new StatusMessageEvent(
						OpenSessionCommand.this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Common.StatusBar.SessionHaveBeenOpened")));
				dispatcher1.firePropertyChange(
					new ContextChangeEvent(OpenSessionCommand.this,
						ContextChangeEvent.SESSION_OPENED_EVENT));
				dispatcher1.firePropertyChange(
					new ContextChangeEvent(OpenSessionCommand.this,
						ContextChangeEvent.CONNECTION_OPENED_EVENT));

				// Берем сохраненный локально с прошлой сессии домен
				Identifier domainId = Environment.getDomainId();

				for (final Iterator iterator = availableDomains.iterator(); iterator.hasNext();) {
					Domain domain = (Domain) iterator.next();
					if (domain.equals(domainId)) {
						try {
							LoginManager.selectDomain(domainId);
							dispatcher1.firePropertyChange(
								new ContextChangeEvent(											
									OpenSessionCommand.this,
									ContextChangeEvent.DOMAIN_SELECTED_EVENT));
						} catch (CommunicationException e) {
							JOptionPane.showMessageDialog(
								Environment.getActiveWindow(), 
								I18N.getString(
									"Error.ServerConnection"), 
								I18N.getString(
									"Error.OpenSession"), 
								JOptionPane.ERROR_MESSAGE, 
								null);
							break;
						}

						break;
					}
				}
				dispatcher1.firePropertyChange(
					new StatusMessageEvent(OpenSessionCommand.this,
						StatusMessageEvent.STATUS_PROGRESS_BAR, 
						false));
			}
		}, I18N.getString("Common.ProcessingDialog.LoadingPlsWait"));	
		
		return this.logged;
	}
	
	protected void createUIItems() {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc1 = new GridBagConstraints();

			final ImageIcon imageIcon = 
				(ImageIcon) UIManager.get(ResourceKeys.IMAGE_LOGIN_LOGO);
			final int iconWidth = imageIcon.getIconWidth();
			final int iconHeight = imageIcon.getIconHeight();

			gbc1.gridwidth = GridBagConstraints.REMAINDER;
			this.mainPanel.add(new JLabel(imageIcon), gbc1);

			final JPanel textFieldsPanel = new JPanel(new GridBagLayout());
			this.loginTextField = new JTextField();
			this.passwordTextField = new JPasswordField();

			{
				final GridBagConstraints gbc = new GridBagConstraints();
				final FontMetrics fontMetrics = 
					this.loginTextField.getFontMetrics(
							this.loginTextField.getFont());

				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.insets = new Insets(fontMetrics.getHeight() / 2, 
						iconWidth / 50, 
						fontMetrics.getHeight() / 2,
						iconWidth / 50);
				gbc.fill = GridBagConstraints.NONE;
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(
					new JLabel(
						I18N.getString("Common.Login.LoginName") + ':'),
					gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(this.loginTextField, gbc);

				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.fill = GridBagConstraints.NONE;
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(
					new JLabel(
						I18N.getString("Common.Login.Password") + ':'),
					gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(this.passwordTextField, gbc);

			}

			gbc1.gridwidth = GridBagConstraints.REMAINDER;

			gbc1.weightx = 1.0;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.insets = new Insets(iconHeight / 10, 
					iconWidth / 10, 
					0,
					iconWidth / 10);
			this.mainPanel.add(textFieldsPanel, gbc1);

		}
	}

	protected boolean showOpenSessionDialog(final JFrame frame) {
		
		this.createUIItems();
		
		this.loginTextField.setText(this.login);
		this.passwordTextField.setText("");

		if (this.dialog == null) {
			this.okButton = 
				I18N.getString("Common.Login.Button.ok");
			this.cancelButton = 
				I18N.getString("Common.Login.Button.cancel");
			if (this.optionPane == null) {
				this.optionPane = 
					new JOptionPane(this.mainPanel, 
							JOptionPane.PLAIN_MESSAGE, 
							JOptionPane.OK_CANCEL_OPTION,
							null, 
							new Object[] { 
								this.okButton, 
								this.cancelButton}, 
							null);
			}

			this.dialog = this.optionPane.createDialog(frame, 
					this.getDialogTitle());

			if (this.actionListener == null) {
				this.actionListener = new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						OpenSessionCommand.this.optionPane.setValue(
							OpenSessionCommand.this.okButton);
					}
				};
			}

			this.loginTextField.addActionListener(this.actionListener);
			this.passwordTextField.addActionListener(this.actionListener);
		}
		

		if (this.loginTextField.isEditable()) {
			this.loginTextField.requestFocus();
		} else {
			this.passwordTextField.requestFocus();
		}
		
		this.dialog.setModal(true);
		this.dialog.setVisible(true);

		final Object selectedValue = this.optionPane.getValue();
		if (selectedValue == this.okButton) {
			this.login = this.loginTextField.getText();
			this.password = new String(this.passwordTextField.getPassword());
			return true;
		}		
		this.disposeDialog();
		return false;
	}
	
	protected String getDialogTitle() {
		return I18N.getString("Common.Login.Login");
	}

	protected void disposeDialog() {
		if (this.actionListener != null) {
			if (this.loginTextField != null && this.passwordTextField != null) {
				this.loginTextField.removeActionListener(this.actionListener);
				this.passwordTextField.removeActionListener(this.actionListener);
			}
		}
		if (this.dialog != null) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
			this.dialog = null;
		}
	}	
	
}
