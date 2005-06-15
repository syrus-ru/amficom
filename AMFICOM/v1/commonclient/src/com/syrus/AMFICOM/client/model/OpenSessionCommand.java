/*
 * $Id: OpenSessionCommand.java,v 1.10 2005/06/15 05:43:54 bob Exp $
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
// import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author $Author: bob $
 * @version $Revision: 1.10 $, $Date: 2005/06/15 05:43:54 $
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

	private static final String		LOGIN_PROPERTY_KEY		= "com.amficom.login";
	private static final String		PASSWORD_PROPERTY_KEY	= "com.amficom.password";

	private static boolean			logged					= false;

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
		String loginProperty = System.getProperty(LOGIN_PROPERTY_KEY);
		String passwordProperty = System.getProperty(PASSWORD_PROPERTY_KEY);
		if (loginProperty != null) {
			login = loginProperty;
			password = passwordProperty;
			logged = true;
		}

		while (!this.executeRemote()) {
			Log.debugMessage("OpenSessionCommand.execute | try again ", Log.FINEST);
		}
	}

	private boolean executeRemote() {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																	LangModelGeneral
																			.getString("StatusBar.OpeningSession")));
		this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CHANGING_EVENT));

		if (!logged) {
			this.showOpenSessionDialog(Environment.getActiveWindow());
		}

		if (!logged) {
			this.dispatcher
					.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																LangModelGeneral.getString("StatusBar.Aborted")));
			return true;
		}

		final Dispatcher dispatcher1 = this.dispatcher;

		Set availableDomains;
		try {
			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment
					.getInstance(ApplicationProperties.getInt(ClientSessionEnvironment.SESSION_KIND_KEY, -1));

			this.dispatcher
					.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																LangModelGeneral.getString("StatusBar.InitStartupData")));

			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR,
																		true));

			clientSessionEnvironment.login(login, password);
			availableDomains = LoginManager.getAvailableDomains();
		} catch (IllegalDataException e) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelGeneral
					.getString("StatusBar.IllegalSessionKind"), LangModelGeneral.getString("Error.OpenSession"),
				JOptionPane.ERROR_MESSAGE, null);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR,
																		false));
			logged = false;
			return false;
		} catch (CommunicationException e) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelGeneral
					.getString("Error.ServerConnection"), LangModelGeneral.getString("Error.OpenSession"),
				JOptionPane.ERROR_MESSAGE, null);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR,
																		false));
			logged = false;
			return false;
		} catch (LoginException e) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelGeneral.getString("Error.WrongLogin"), LangModelGeneral.getString("Error.OpenSession"),
				JOptionPane.ERROR_MESSAGE, null);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR,
																		false));
			logged = false;
			return false;
		}

		final Set availableDomains1 = availableDomains;

		CommonUIUtilities.invokeAsynchronously(new Runnable() {

			public void run() {
				dispatcher1
						.firePropertyChange(new StatusMessageEvent(
																	OpenSessionCommand.this,
																	StatusMessageEvent.STATUS_MESSAGE,
																	LangModelGeneral
																			.getString("StatusBar.SessionHaveBeenOpened")));
				dispatcher1.firePropertyChange(new ContextChangeEvent(OpenSessionCommand.this,
																		ContextChangeEvent.SESSION_OPENED_EVENT));
				dispatcher1.firePropertyChange(new ContextChangeEvent(OpenSessionCommand.this,
																		ContextChangeEvent.CONNECTION_OPENED_EVENT));

				// Берем сохраненный локально с прошлой сессии домен
				Identifier domainId = Environment.getDomainId();

				for (Iterator iterator = availableDomains1.iterator(); iterator.hasNext();) {
					Domain domain = (Domain) iterator.next();
					if (domain.getId().equals(domainId)) {
						try {
							LoginManager.selectDomain(domainId);
							dispatcher1
									.firePropertyChange(new ContextChangeEvent(
																				OpenSessionCommand.this,
																				ContextChangeEvent.DOMAIN_SELECTED_EVENT));
						} catch (CommunicationException e) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelGeneral
									.getString("Error.ServerConnection"), LangModelGeneral
									.getString("Error.OpenSession"), JOptionPane.ERROR_MESSAGE, null);
							break;
						}

						break;
					}
				}
				dispatcher1.firePropertyChange(new StatusMessageEvent(OpenSessionCommand.this,
																		StatusMessageEvent.STATUS_PROGRESS_BAR, false));
			}
		}, LangModelGeneral.getString("Message.Information.LoadingPlsWait"));
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
				textFieldsPanel.add(new JLabel(LangModelGeneral.getString("Login.LoginName") + ':'), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(loginTextField, gbc);

				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.fill = GridBagConstraints.NONE;
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(new JLabel(LangModelGeneral.getString("Login.Password") + ':'), gbc);
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

		loginTextField.setText(login);
		passwordTextField.setText("");
		
		loginTextField.requestFocus();
		final String okButton = LangModelGeneral.getString("Login.Button.ok");
		final String cancelButton = LangModelGeneral.getString("Login.Button.cancel");
		final JOptionPane optionPane = new JOptionPane(mainPanel, JOptionPane.PLAIN_MESSAGE,
														JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
																cancelButton}, null);

		final JDialog dialog = optionPane.createDialog(frame, LangModelGeneral.getString("Login.Login"));

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
