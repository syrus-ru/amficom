/*-
 * $Id: AbstractApplication.java,v 1.1 2005/06/14 11:23:33 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/14 11:23:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public abstract class AbstractApplication {

	protected ApplicationContext	aContext;

	protected Dispatcher			dispatcher;

	private static LoginRestorer	loginRestorer;

	public AbstractApplication() {
		this.aContext = new ApplicationContext();
		this.dispatcher = new Dispatcher();
		this.aContext.setDispatcher(this.dispatcher);

		this.init();
	}

	protected void init() {
		if (loginRestorer == null) {
			synchronized (this) {
				if (loginRestorer == null) {
					loginRestorer = new LoginRestorer() {

						// TODO rebuild
						public String getLogin() {
							return null;
						}

						public String getPassword() {
							return null;
						}

						public boolean restoreLogin() {
							return false;
						}
					};
				}
			}

			ClientSessionEnvironment.setLoginRestorer(loginRestorer);

		}

		ClientSessionEnvironment clientSessionEnvironment1;
		try {
			clientSessionEnvironment1 = ClientSessionEnvironment.getInstance(ApplicationProperties.getInt(
				ClientSessionEnvironment.SESSION_KIND_KEY, -1));
		} catch (CommunicationException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.ConnectionError")));
			return;
		} catch (IllegalDataException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.IllegalSessionKind")));
			return;
		}
		final ClientSessionEnvironment clientSessionEnvironment = clientSessionEnvironment1;

		final Dispatcher dispatcher1 = this.dispatcher;

		PropertyChangeListener connectionListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				Boolean boolean1 = (Boolean) evt.getNewValue();
				if (boolean1.booleanValue()) {
					dispatcher1.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																			clientSessionEnvironment.getServerName()));
				} else {
					dispatcher1
							.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.ConnectionError")));
				}

			}
		};

		clientSessionEnvironment.addPropertyListener(connectionListener);
	}

	public void startMainFrame(	final AbstractMainFrame mainFrame,
								Image image) {
		mainFrame.setIconImage(image);
		mainFrame.setVisible(true);
	}

}
