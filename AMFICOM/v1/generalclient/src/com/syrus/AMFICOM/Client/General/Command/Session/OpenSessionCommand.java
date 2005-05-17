/*
 * $Id: OpenSessionCommand.java,v 1.2 2005/05/17 08:33:30 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/05/17 08:33:30 $
 * @module generalclient_v1
 */
public class OpenSessionCommand extends VoidCommand {

	private Dispatcher				dispatcher;
	private ApplicationContext		aContext;

	/* static fields use due to dummy command using -> clone (new ...) */
	private static JPanel			mainPanel;

	private static JTextField		loginTextField;
	private static JPasswordField	passwordTextField;
	private static String			login;
	private static String			password;

	private static boolean			logged	= false;

	private static LoginRestorer	loginRestorer;

	public OpenSessionCommand() {
		// nothing
	}

	public OpenSessionCommand(Dispatcher dispatcher, ApplicationContext aContext) {
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher"))
			setDispatcher((Dispatcher) value);
		else if (field.equals("aContext"))
			setApplicationContext((ApplicationContext) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new OpenSessionCommand(dispatcher, aContext);
	}

	public void execute() {
		if (Environment.getConnectionType().equals(Environment.CONNECTION_EMPTY)) {
			executeLocal();
		} else {
			while (!this.executeRemote());
		}
	}

	private void executeLocal() {
		// try {
		// SessionInterface ssi = aContext.getSessionInterface().openSession();
		// } catch (ApplicationException e) {
		// Log.debugException(e, Log.WARNING);
		// }
		//
		// dispatcher.notify(new
		// StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "��������
		// ������..."));
		// dispatcher.notify(new
		// ContextChangeEvent(aContext.getSessionInterface(),
		// ContextChangeEvent.SESSION_CHANGING_EVENT));
		//
		// final DataSourceInterface dataSource =
		// aContext.getApplicationModel().getDataSource(
		// aContext.getSessionInterface());
		// //
		// dispatcher.notify(new
		// StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "�������������
		// ��������� ������"));
		//
		// dispatcher.notify(new
		// StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, true));
		//
		// dispatcher.notify(new
		// StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, false));
		//
		// SessionInterface sess = dataSource.getSession();
		//
		// dispatcher.notify(new
		// StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "������
		// �������"));
		// dispatcher.notify(new
		// ContextChangeEvent(aContext.getSessionInterface(),
		// ContextChangeEvent.SESSION_OPENED_EVENT));
	}

	private boolean executeRemote() {
		// ConnectionInterface connection = ConnectionInterface.getInstance();
		// if(!connection.isConnected())
		// {
		// new CheckConnectionCommand(dispatcher, aContext).execute();
		// if (!connection.isConnected())
		// {
		// dispatcher.notify(new ContextChangeEvent(connection,
		// ContextChangeEvent.CONNECTION_FAILED_EVENT));
		// return;
		// }
		// }
		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "�������� ������..."));
		dispatcher.notify(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CHANGING_EVENT));

		if (System.getProperty("com.amficom.login") == null || System.getProperty("com.amficom.password") == null) {
			this.showOpenSessionDialog(Environment.getActiveWindow());
		} else {
			OpenSessionCommand.login = System.getProperty("com.amficom.login");
			OpenSessionCommand.password = System.getProperty("com.amficom.password");
			OpenSessionCommand.logged = true;
		}

		if (!OpenSessionCommand.logged) {
			dispatcher
					.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModel.getString("Aborted")));
			return true;
		}
		// else
		{
			/* TODO login ! */
			final String login = OpenSessionCommand.login;
			final String password = OpenSessionCommand.password;

			if (loginRestorer == null) {
				loginRestorer = new LoginRestorer() {

					/* TODO just dummy login restorer */
					public String getLogin() {
						return login;
					}

					public String getPassword() {
						return password;
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

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE,
														"������������� ��������� ������"));

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, true));

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, false));

			Set availableDomains;
			try {
				clientSessionEnvironment.login(login, password);
				availableDomains = LoginManager.getAvailableDomains();
			} catch (CommunicationException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
						.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
					JOptionPane.ERROR_MESSAGE, null);
				return false;
			} catch (LoginException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel.getString("errorWrongLogin"),
					LangModel.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);
				return false;
			}

			final Set availableDomains1 = availableDomains;

			ReportBuilder.invokeAsynchronously(new Runnable() {

				public void run() {
					dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "������ �������"));
					dispatcher.notify(new ContextChangeEvent(OpenSessionCommand.this,
																ContextChangeEvent.SESSION_OPENED_EVENT));

					// ����� ����������� �������� � ������� ������ �����
					Identifier domainId = Environment.getDomainId();

					for (Iterator iterator = availableDomains1.iterator(); iterator.hasNext();) {
						Domain domain = (Domain) iterator.next();
						if (domain.getId().equals(domainId)) {
							try {
								LoginManager.selectDomain(domainId);
								dispatcher.notify(new ContextChangeEvent(OpenSessionCommand.this,
																			ContextChangeEvent.DOMAIN_SELECTED_EVENT));
							} catch (CommunicationException e) {
								JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
										.getString("Error server connection"), LangModel
										.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);
								return;
							}

							break;
						}
					}

				}
			}, "��� ��������. ����������, ���������.");

		}
		return true;
	}

	private void showOpenSessionDialog(JFrame frame) {
		if (mainPanel == null) {
			mainPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc1 = new GridBagConstraints();

			ImageIcon imageIcon = (ImageIcon) UIManager.get(ResourceKeys.IMAGE_LOGIN_LOGO);
			int iconWidth = imageIcon.getIconWidth();
			int iconHeight = imageIcon.getIconHeight();

			gbc1.gridwidth = GridBagConstraints.REMAINDER;
			mainPanel.add(new JLabel(imageIcon), gbc1);

			JPanel textFieldsPanel = new JPanel(new GridBagLayout());
			loginTextField = new JTextField();
			passwordTextField = new JPasswordField();

			{
				GridBagConstraints gbc = new GridBagConstraints();
				FontMetrics fontMetrics = loginTextField.getFontMetrics(loginTextField.getFont());

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

		int result = JOptionPane.showOptionDialog(frame, mainPanel, LangModel.getString("SessionOpenTitle"),
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {
					LangModel.getString("buttonEnter"), LangModel.getString("buttonCancel")}, LangModel
					.getString("buttonEnter"));
		if (result == JOptionPane.OK_OPTION) {
			login = loginTextField.getText();
			password = new String(passwordTextField.getPassword());
			logged = true;
		} else {
			logged = false;
		}

	}

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setVisible(true);
		System.out.println(Environment.class);
		OpenSessionCommand command = new OpenSessionCommand();
		command.showOpenSessionDialog(frame);
	}
}
