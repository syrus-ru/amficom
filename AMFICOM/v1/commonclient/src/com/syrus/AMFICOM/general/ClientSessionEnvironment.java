/*
 * $Id: ClientSessionEnvironment.java,v 1.40 2005/11/29 08:18:55 bob Exp $
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
import com.syrus.AMFICOM.general.corba.CORBAClient;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.40 $, $Date: 2005/11/29 08:18:55 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {

	public enum SessionKind {
		UNKNOWN, MEASUREMENT, MAPSCHEME, ALL;

		private static SessionKind[] values = values();

		public static SessionKind valueOf(final int value) {
			return values[value];
		}
	}

	public static final String SESSION_KIND_KEY = "SessionKind";

	private static ClientSessionEnvironment instance;

	private CORBAClientImpl corbaClientImplServant;
	private PopupMessageReceiver	receiver;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final CORBAClient corbaClient,
			final LoginRestorer loginRestorer,
			final CORBAClientImpl servant) {
		super(clientServantManager, clientPoolContext, corbaClient, loginRestorer, new ClientCORBAActionProcessor());

		this.corbaClientImplServant = servant;
		this.receiver = new DefaultPopupMessageReceiver();
		this.corbaClientImplServant.addPopupMessageReceiver(this.receiver);
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

		final CORBAClientImpl servant = new CORBAClientImpl();
		final CORBAClient corbaClient = servant._this(mClientServantManager.getCORBAServer().getOrb());

		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, corbaClient, loginRestorer, servant);
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

		final CORBAClientImpl servant = new CORBAClientImpl();
		final CORBAClient corbaClient = servant._this(mClientServantManager.getCORBAServer().getOrb());

		instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, corbaClient, loginRestorer, servant);
	}
	
	private static void createAllSession(final LoginRestorer loginRestorer) throws CommunicationException {
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
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.EVENT_GROUP_CODE,
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

		final ClientPoolContext clientPoolContext = new AllClientPoolContext(objectLoader);

		final CORBAClientImpl servant = new CORBAClientImpl();
		final CORBAClient corbaClient = servant._this(mClientServantManager.getCORBAServer().getOrb());

		instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, corbaClient, loginRestorer, servant);
	}

	public void addPropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().addPropertyListener(listener);
	}

	public void removePropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().removePropertyListener(listener);
	}

	public void addPopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.corbaClientImplServant.addPopupMessageReceiver(popupMessageReceiver);
	}

	public void removePopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.corbaClientImplServant.removePopupMessageReceiver(popupMessageReceiver);
	}
}
