/*
 * $Id: ClientSessionEnvironment.java,v 1.45 2006/04/14 11:18:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectGroupEntities.ADMINISTRATION_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.CONFIGURATION_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.EVENT_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.GENERAL_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.MEASUREMENT_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.SCHEME_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.REPORT_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.RESOURCE_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.MAP_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.MAPVIEW_GROUP_CODE;

import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.UI.dialogs.PopupNotificationEventReceiver;
import com.syrus.AMFICOM.client.event.EventReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.corba.CORBAClient;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.45 $, $Date: 2006/04/14 11:18:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {

	public enum SessionKind {
		UNKNOWN, MEASUREMENT, MAPSCHEME, ALL;

		private static final SessionKind[] VALUES = values();

		public static SessionKind valueOf(final int value) {
			return VALUES[value];
		}
	}

	public static final String SESSION_KIND_KEY = "SessionKind";

	private static ClientSessionEnvironment instance;

	private CORBAClientImpl corbaClientImplServant;
	private EventReceiver	receiver;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final CORBAClient corbaClient,
			final LoginRestorer loginRestorer,
			final CORBAClientImpl servant) {
		super(clientServantManager, clientPoolContext, corbaClient, loginRestorer, new ClientCORBAActionProcessor());

		this.corbaClientImplServant = servant;
		this.receiver = new PopupNotificationEventReceiver();
		this.corbaClientImplServant.addEventReceiver(this.receiver);
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
		objectLoader.addCORBAObjectLoader(GENERAL_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ADMINISTRATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(CONFIGURATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MEASUREMENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(EVENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(REPORT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);

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
		objectLoader.addCORBAObjectLoader(GENERAL_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ADMINISTRATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(CONFIGURATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MEASUREMENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(EVENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(RESOURCE_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(REPORT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MAP_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(SCHEME_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MAPVIEW_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);

		final ClientPoolContext clientPoolContext = new MscharClientPoolContext(objectLoader);

		final CORBAClientImpl servant = new CORBAClientImpl();
		final CORBAClient corbaClient = servant._this(mClientServantManager.getCORBAServer().getOrb());

		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, corbaClient, loginRestorer, servant);
	}
	
	private static void createAllSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final MClientServantManager mClientServantManager = MClientServantManager.create();
		final MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
		final ClientCORBAActionProcessor clientCORBAActionProcessor = new ClientCORBAActionProcessor();

		final MultiServantCORBAObjectLoader objectLoader = new MultiServantCORBAObjectLoader();
		objectLoader.addCORBAObjectLoader(GENERAL_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(ADMINISTRATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(CONFIGURATION_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MEASUREMENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(EVENT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(RESOURCE_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(REPORT_GROUP_CODE, mClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MAP_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(SCHEME_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);
		objectLoader.addCORBAObjectLoader(MAPVIEW_GROUP_CODE, mscharClientServantManager, clientCORBAActionProcessor);

		final ClientPoolContext clientPoolContext = new AllClientPoolContext(objectLoader);

		final CORBAClientImpl servant = new CORBAClientImpl();
		final CORBAClient corbaClient = servant._this(mClientServantManager.getCORBAServer().getOrb());

		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, corbaClient, loginRestorer, servant);
	}

	public void addPropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().addPropertyListener(listener);
	}

	public void removePropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().removePropertyListener(listener);
	}

	public void addEventReceiver(final EventReceiver eventReceiver) {
		this.corbaClientImplServant.addEventReceiver(eventReceiver);
	}

	public void removeEventReceiver(final EventReceiver eventReceiver) {
		this.corbaClientImplServant.removeEventReceiver(eventReceiver);
	}
}
