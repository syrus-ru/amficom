/*
 * $Id: ClientSessionEnvironment.java,v 1.48.2.2 2006/06/27 15:46:18 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.UI.dialogs.PopupNotificationEventReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.event.EventReceiver;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUser;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUserPOATie;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.48.2.2 $, $Date: 2006/06/27 15:46:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class ClientSessionEnvironment extends AbstractSessionEnvironment<ClientServantManager> {

	public enum SessionKind {
		UNKNOWN, MEASUREMENT, MAPSCHEME, ALL;

		private static final SessionKind[] VALUES = values();

		public static SessionKind valueOf(final int value) {
			return VALUES[value];
		}
	}

	public static final String SESSION_KIND_KEY = "SessionKind";

	private static ClientSessionEnvironment instance;

	private CORBASystemUserImpl corbaClientImplServant;
	private EventReceiver receiver;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final CORBASystemUser corbaSystemUser,
			final CORBASystemUserImpl corbaSystemUserImpl,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, corbaSystemUser, loginRestorer, new ClientCORBAActionProcessor());

		this.corbaClientImplServant = corbaSystemUserImpl;
		this.receiver = new PopupNotificationEventReceiver();
		this.corbaClientImplServant.addEventReceiver(this.receiver);
	}

	public final String getServerName() {
		return this.getConnectionManager().getCORBAServer().getRootContextName();
	}

	public static ClientSessionEnvironment getInstance() {
		return instance;
	}

	public static void createInstance(final SessionKind sessionKind, final LoginRestorer loginRestorer)
			throws CommunicationException, IllegalDataException {
		switch (sessionKind) {
			case MEASUREMENT:
				createMeasurementSession(loginRestorer);
				break;
			case MAPSCHEME:
				createMapSchemeSession(loginRestorer);
				break;
			case ALL:
				createAllSession(loginRestorer);
				break;
			default:
				final String msg = I18N.getString("Error.UnknownSessionKind") + " -- " + sessionKind;
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);
		}

		/* Print available objects -- for debugging purpose */
		if (instance != null) {
			instance.getConnectionManager().getCORBAServer().printNamingContext();
		}
	}

	private static void createMeasurementSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final ClientServantManager clientServantManager = ClientServantManager.create();

		final ObjectLoader objectLoader = new CORBAObjectLoader(clientServantManager);
		final ClientPoolContext clientPoolContext = new MClientPoolContext(objectLoader);

		final CORBAServer corbaServer = clientServantManager.getCORBAServer();
		final CORBASystemUserImpl corbaSystemUserImpl =  new CORBASystemUserImpl();
		final CORBASystemUser corbaSystemUser = (new CORBASystemUserPOATie(corbaSystemUserImpl, corbaServer.getPoa()))._this(corbaServer.getOrb());

		instance = new ClientSessionEnvironment(clientServantManager,
				clientPoolContext,
				corbaSystemUser,
				corbaSystemUserImpl,
				loginRestorer);
	}

	private static void createMapSchemeSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final MapClientServantManager clientServantManager = MapClientServantManager.create();

		final ObjectLoader objectLoader = new CORBAObjectLoader(clientServantManager);
		final ClientPoolContext clientPoolContext = new MscharClientPoolContext(objectLoader);

		final CORBAServer corbaServer = clientServantManager.getCORBAServer();
		final CORBASystemUserImpl corbaSystemUserImpl =  new CORBASystemUserImpl();
		final CORBASystemUser corbaSystemUser = (new CORBASystemUserPOATie(corbaSystemUserImpl, corbaServer.getPoa()))._this(corbaServer.getOrb());

		instance = new ClientSessionEnvironment(clientServantManager,
				clientPoolContext,
				corbaSystemUser,
				corbaSystemUserImpl,
				loginRestorer);
	}

	private static void createAllSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final MapClientServantManager clientServantManager = MapClientServantManager.create();

		final ObjectLoader objectLoader = new CORBAObjectLoader(clientServantManager);
		final ClientPoolContext clientPoolContext = new AllClientPoolContext(objectLoader);

		final CORBAServer corbaServer = clientServantManager.getCORBAServer();
		final CORBASystemUserImpl corbaSystemUserImpl =  new CORBASystemUserImpl();
		final CORBASystemUser corbaSystemUser = (new CORBASystemUserPOATie(corbaSystemUserImpl, corbaServer.getPoa()))._this(corbaServer.getOrb());

		instance = new ClientSessionEnvironment(clientServantManager,
				clientPoolContext,
				corbaSystemUser,
				corbaSystemUserImpl,
				loginRestorer);
	}

	public void addPropertyListener(final PropertyChangeListener listener) {
		this.getConnectionManager().addPropertyListener(listener);
	}

	public void removePropertyListener(final PropertyChangeListener listener) {
		this.getConnectionManager().removePropertyListener(listener);
	}

	public void addEventReceiver(final EventReceiver eventReceiver) {
		this.corbaClientImplServant.addEventReceiver(eventReceiver);
	}

	public void removeEventReceiver(final EventReceiver eventReceiver) {
		this.corbaClientImplServant.removeEventReceiver(eventReceiver);
	}
}
