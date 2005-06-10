/*
 * $Id: OpenSessionCommand.java,v 1.6 2005/06/10 07:53:34 bob Exp $
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
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.util.Log;

/**
 * @author $Author: bob $
 * @version $Revision: 1.6 $, $Date: 2005/06/10 07:53:34 $
 * @module generalclient_v1
 */
public class OpenSessionCommand extends AbstractCommand {

	private Dispatcher				dispatcher;
	/* static fields use due to dummy command using -> clone (new ...) */
	private static JPanel			mainPanel;

	private static JTextField		loginTextField;
	private static JPasswordField	passwordTextField;
	private static String			login;
	private static String			password;

	private static boolean			logged	= false;

	private static LoginRestorer	loginRestorer;

	public OpenSessionCommand(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher"))
			this.setDispatcher((Dispatcher) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void execute() {
		while (!this.executeRemote()) {
			Log.debugMessage("OpenSessionCommand.execute | try again ", Log.FINEST);
		}
	}

	private boolean executeRemote() {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelGeneral.getString("StatusBar.OpeningSession")));
		this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CHANGING_EVENT));

		if (System.getProperty("com.amficom.login") == null || System.getProperty("com.amficom.password") == null) {
			this.showOpenSessionDialog(Environment.getActiveWindow());
		} else {
			OpenSessionCommand.login = System.getProperty("com.amficom.login");
			OpenSessionCommand.password = System.getProperty("com.amficom.password");
			OpenSessionCommand.logged = true;
		}

		if (!OpenSessionCommand.logged) {
			this.dispatcher
					.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelGeneral.getString("StatusBar.Aborted")));
			return true;
		}
		// else
		{
			/* TODO login ! */
			final String login1 = OpenSessionCommand.login;
			final String password1 = OpenSessionCommand.password;

			if (loginRestorer == null) {
				loginRestorer = new LoginRestorer() {

					/* TODO just dummy login restorer */
					public String getLogin() {
						return login1;
					}

					public String getPassword() {
						return password1;
					}

					public boolean restoreLogin() {
						return true;
					}
				};
			}
			try {

				ClientSessionEnvironment.createInstance(ClientSessionEnvironment.SESSION_KIND_MEASUREMENT,
					loginRestorer);
			} catch (CommunicationException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
						.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
					JOptionPane.ERROR_MESSAGE, null);
				return false;
			}

			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();

			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
														LangModelGeneral.getString("StatusBar.InitStartupData")));

			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));
			

			Set availableDomains;
			try {
				clientSessionEnvironment.login(login1, password1);
				availableDomains = LoginManager.getAvailableDomains();
			} catch (CommunicationException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
						.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
					JOptionPane.ERROR_MESSAGE, null);
				this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
				return false;
			} catch (LoginException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel.getString("errorWrongLogin"),
					LangModel.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);
				this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
				return false;
			}

			final Set availableDomains1 = availableDomains;
			
			final Dispatcher dispatcher1 = this.dispatcher;

			CommonUIUtilities.invokeAsynchronously(new Runnable() {

				public void run() {
					dispatcher1.firePropertyChange(new StatusMessageEvent(OpenSessionCommand.this, StatusMessageEvent.STATUS_MESSAGE, LangModelGeneral.getString("StatusBar.SessionHaveBeenOpened")));
					dispatcher1.firePropertyChange(new ContextChangeEvent(OpenSessionCommand.this,
																ContextChangeEvent.SESSION_OPENED_EVENT));

					// Берем сохраненный локально с прошлой сессии домен
					Identifier domainId = Environment.getDomainId();

					for (Iterator iterator = availableDomains1.iterator(); iterator.hasNext();) {
						Domain domain = (Domain) iterator.next();
						if (domain.getId().equals(domainId)) {
							try {
								LoginManager.selectDomain(domainId);
								dispatcher1.firePropertyChange(new ContextChangeEvent(OpenSessionCommand.this,
																			ContextChangeEvent.DOMAIN_SELECTED_EVENT));
							} catch (CommunicationException e) {
								JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
										.getString("Error server connection"), LangModel
										.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);								
								break;
							}

							break;
						}
					}
					dispatcher1.firePropertyChange(new StatusMessageEvent(OpenSessionCommand.this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
				}
			}, "Идёт загрузка. Пожалуйста, подождите.");

		}
		return true;
	}

	private void showOpenSessionDialog(final JFrame frame) {
		if (mainPanel == null) {
			mainPanel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc1 = new GridBagConstraints();

			final ImageIcon imageIcon = (ImageIcon) UIManager.get(ResourceKeys.IMAGE_LOGIN_LOGO);
			final int iconWidth = imageIcon.getIconWidth();
			final int iconHeight = imageIcon.getIconHeight();

			gbc1.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(new JLabel(imageIcon), gbc1);

			final JPanel textFieldsPanel = new JPanel(new GridBagLayout());
			loginTextField = new JTextField();
			passwordTextField = new JPasswordField();

			{
				final GridBagConstraints gbc = new GridBagConstraints();
				final FontMetrics fontMetrics = loginTextField.getFontMetrics(loginTextField.getFont());

				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.insets = new Insets(fontMetrics.getHeight() / 2, iconWidth / 50, fontMetrics.getHeight() / 2,
										iconWidth / 50);
				gbc.fill = GridBagConstraints.NONE;
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(new JLabel(LangModel.getString("labelName") + ':'), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(loginTextField, gbc);

				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.fill = GridBagConstraints.NONE;
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(new JLabel(LangModel.getString("labelPassword") + ':'), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(passwordTextField, gbc);

			}

			gbc1.gridwidth = GridBagConstraints.REMAINDER;

			gbc1.weightx = 1.0;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.insets = new Insets(iconHeight / 10, iconWidth / 10, 0, iconWidth / 10);
			mainPanel.add(textFieldsPanel, gbc1);

		}

		loginTextField.requestFocus();
		final String okButton = LangModel.getString("buttonEnter");
		final String cancelButton = LangModel.getString("buttonCancel");
		final JOptionPane optionPane = new JOptionPane(mainPanel, JOptionPane.PLAIN_MESSAGE,
														JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
																cancelButton}, null);

		final JDialog dialog = optionPane.createDialog(frame, LangModel.getString("SessionOpenTitle"));

		final ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(okButton);
			}
		};

		loginTextField.addActionListener(actionListener);
		passwordTextField.addActionListener(actionListener);

		dialog.show();
		dialog.dispose();

		final Object selectedValue = optionPane.getValue();

		if (selectedValue == okButton) {
			login = loginTextField.getText();
			password = new String(passwordTextField.getPassword());
			logged = true;
		} else {
			logged = false;
		}

	}
}
