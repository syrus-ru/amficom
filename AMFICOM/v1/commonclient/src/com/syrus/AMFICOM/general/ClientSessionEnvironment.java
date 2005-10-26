/*
 * $Id: ClientSessionEnvironment.java,v 1.32 2005/10/26 15:30:01 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.UI.dialogs.DefaultPopupMessageReceiver;
import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.corba.CORBAClientPOATie;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.32 $, $Date: 2005/10/26 15:30:01 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {

	public enum SessionKind {
		UNKNOWN, MEASUREMENT, MAPSCHEME;

		public static SessionKind valueOf(final int value) {
			switch (value) {
				case 1:
					return MEASUREMENT;
				case 2:
					return MAPSCHEME;
				default:
					return UNKNOWN;
			}
		}
	}

	public static final String SESSION_KIND_KEY = "SessionKind";

	private static ClientSessionEnvironment instance;

	private CORBAClientImpl corbaClientImpl;

	private PopupMessageReceiver	receiver;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer, new ClientCORBAActionProcessor());
	}

	public ClientServantManager getClientServantManager() {
		return (ClientServantManager) super.baseConnectionManager;
	}

	public final String getServerName() {
		return super.baseConnectionManager.getCORBAServer().getRootContextName();
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
			default:
				final String msg = I18N.getString("Error.UnknownSessionKind") + " -- " + sessionKind;
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);
		}

		/* Print available objects -- for debugging purpose */
		if (instance != null) {
			instance.baseConnectionManager.getCORBAServer().printNamingContext();
		}
	}

	private static void createMeasurementSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final ClientServantManager mClientServantManager = MClientServantManager.create();
		final ClientCORBAActionProcessor clientCORBAActionProcessor = new ClientCORBAActionProcessor();

		final MultiServantCORBAObjectLoader objectLoader = new MultiServantCORBAObjectLoader();
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.GENERAL_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.CONFIGURATION_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MEASUREMENT_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.REPORT_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);

		final ClientPoolContext clientPoolContext = new MClientPoolContext(objectLoader);

		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
	}

	private static void createMapSchemeSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final MClientServantManager mClientServantManager = MClientServantManager.create();
		final MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
		final ClientCORBAActionProcessor clientCORBAActionProcessor = new ClientCORBAActionProcessor();

		final MultiServantCORBAObjectLoader objectLoader = new MultiServantCORBAObjectLoader();
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.GENERAL_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.CONFIGURATION_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MEASUREMENT_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.RESOURCE_GROUP_CODE,
				mscharClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MAP_GROUP_CODE,
				mscharClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.SCHEME_GROUP_CODE,
				mscharClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MAPVIEW_GROUP_CODE,
				mscharClientServantManager,
				clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.REPORT_GROUP_CODE,
				mClientServantManager,
				clientCORBAActionProcessor);

		final ClientPoolContext clientPoolContext = new MscharClientPoolContext(objectLoader);

		instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, loginRestorer);
	}

	public void addPropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().addPropertyListener(listener);
	}

	public void removePropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().removePropertyListener(listener);
	}

	@Override
	public void login(final String login, final String password, final Identifier domainId)
			throws CommunicationException,
				LoginException {
		super.login(login, password, domainId);
		this.activateServant();
	}

	@Override
	public void logout() throws CommunicationException, LoginException {
		try {
			this.deactivateServant();
		} catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		super.logout();
	}

	void activateServant() throws CommunicationException {
		final String servantName = LoginManager.getSessionKey().toString()
				+ Identifier.SEPARATOR
				+ ContextNameFactory.generateContextName();
		final CORBAServer corbaServer = instance.baseConnectionManager.getCORBAServer();
		this.corbaClientImpl = new CORBAClientImpl();
		if (this.receiver == null) {
			this.receiver = new DefaultPopupMessageReceiver();
		}
		this.corbaClientImpl.addPopupMessageReceiver(this.receiver);
		corbaServer.activateServant(new CORBAClientPOATie(this.corbaClientImpl, corbaServer.getPoa()), servantName);
		corbaServer.printNamingContext();
	}

	private void deactivateServant() throws CommunicationException {
		final String servantName = LoginManager.getSessionKey().toString()
				+ Identifier.SEPARATOR
				+ ContextNameFactory.generateContextName();
		final CORBAServer corbaServer = instance.baseConnectionManager.getCORBAServer();
		corbaServer.deactivateServant(servantName, true);
		this.corbaClientImpl.removePopupMessageReceiver(this.receiver);
	}

}
