/*
 * $Id: ClientSessionEnvironment.java,v 1.21 2005/10/05 10:56:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.beans.PropertyChangeListener;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.21 $, $Date: 2005/10/05 10:56:10 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {

	public enum SessionKind {
		UNKNOWN, MEASUREMENT, MAPSCHEME;

		public static SessionKind fromInt(final int value) {
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

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer);
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
				final String msg = LangModelGeneral.getString("Error.UnknownSessionKind") + " -- " + sessionKind;
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);
		}

		/* Print available objects -- for debugging purpose */
		if (instance != null) {
			instance.baseConnectionManager.getCORBAServer().printNamingContext();
		}
	}

	public void addPropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().addPropertyListener(listener);
	}

	public void removePropertyListener(final PropertyChangeListener listener) {
		this.getClientServantManager().removePropertyListener(listener);
	}

	private static void createMeasurementSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final ClientServantManager mClientServantManager = MClientServantManager.create();

		final MultiServantCORBAObjectLoader objectLoader = new MultiServantCORBAObjectLoader();
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.GENERAL_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.REPORT_GROUP_CODE, mClientServantManager);

		final ClientPoolContext clientPoolContext = new MClientPoolContext(objectLoader);

		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
	}

	private static void createMapSchemeSession(final LoginRestorer loginRestorer) throws CommunicationException {
		final MClientServantManager mClientServantManager = MClientServantManager.create();
		final MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();

		final MultiServantCORBAObjectLoader objectLoader = new MultiServantCORBAObjectLoader();
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.GENERAL_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, mClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.RESOURCE_GROUP_CODE, mscharClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MAP_GROUP_CODE, mscharClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.SCHEME_GROUP_CODE, mscharClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.MAPVIEW_GROUP_CODE, mscharClientServantManager);
		objectLoader.addCORBAObjectLoader(ObjectGroupEntities.REPORT_GROUP_CODE, mClientServantManager);

		final ClientPoolContext clientPoolContext = new MscharClientPoolContext(objectLoader);

		instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, loginRestorer);
	}

}
