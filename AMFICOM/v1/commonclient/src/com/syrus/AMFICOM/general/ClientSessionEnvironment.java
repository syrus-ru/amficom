/*
 * $Id: ClientSessionEnvironment.java,v 1.9 2005/06/07 17:58:14 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/07 17:58:14 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {
	//TODO enum SessionKind
	public static final int SESSION_KIND_MEASUREMENT = 1;
	public static final int SESSION_KIND_MAPSCHEME = 2;
	//Other kinds -- Map, Scheme, Resources, etc.

//
//	public static final String XML_KEY = "XMLPath";
//
//	private String xmlPath;

	private static ClientSessionEnvironment instance;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer);
	}
//
//	private ClientSessionEnvironment(final BaseConnectionManager baseConnectionManager,
//			final ClientPoolContext clientPoolContext,
//			final String xmlPath) {
//		super(baseConnectionManager, clientPoolContext, null);
//		this.xmlPath = xmlPath;
//	}

	public ClientServantManager getClientServantManager() {
		return (ClientServantManager) super.baseConnectionManager;
	}

	public static void createInstance(int sessionKind, LoginRestorer loginRestorer) throws CommunicationException {
		switch (sessionKind) {
			case SESSION_KIND_MEASUREMENT:
				createMeasurementSession(loginRestorer);
				break;
			case SESSION_KIND_MAPSCHEME:
				createMapSchemeSession(loginRestorer);
				break;
			default:
				Log.errorMessage("Unknown kind of session -- " + sessionKind);
		}
//
//		/*	Print available objects -- for debugging purpose*/
//		if (instance != null && instance.xmlPath == null) {
//			instance.baseConnectionManager.getCORBAServer().printNamingContext();
//		}
	}
//
//	public void login(	String login,
//						String password) throws CommunicationException, LoginException {
//		if (this.xmlPath == null) {
//			super.login(login, password);
//		} else {
//			LoginManager.login(login, password);
//			IdentifierPool.deserialize();
//			super.poolContext.deserialize();
//
//			super.sessionEstablishDate = new Date(System.currentTimeMillis());
//			super.sessionEstablished = true;
//		}
//	}
//
//	public void logout() throws CommunicationException, LoginException {
//		if (this.xmlPath == null) {
//			super.logout();
//		} else {
//
//			this.logout0();
//
//			this.sessionEstablishDate = null;
//			this.sessionEstablished = false;
//		}
//	}

	private static void createMeasurementSession(LoginRestorer loginRestorer) throws CommunicationException {
		ClientServantManager mClientServantManager = MClientServantManager.create();
		ClientPoolContext clientPoolContext = new MClientPoolContext(mClientServantManager);
		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
//
//		final String xmlPath = ApplicationProperties.getString(XML_KEY, null);
//		if (xmlPath == null) {
//			ClientServantManager mClientServantManager = MClientServantManager.create();
//			ClientPoolContext clientPoolContext = new MClientPoolContext(mClientServantManager);
//			instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
//		}
//		else {
//			Log.debugMessage("ClientSessionEnvironment.createMeasurementSession | xmlPath " + xmlPath, Log.FINEST);
//			MClientXMLServantManager mClientServantManager = MClientXMLServantManager.create();
//			ClientPoolContext clientPoolContext = new MClientPoolContext(xmlPath);
//			instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, xmlPath);
//		}
	}

	private static void createMapSchemeSession(LoginRestorer loginRestorer) throws CommunicationException {
		MClientServantManager mClientServantManager = MClientServantManager.create();
		MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
		ClientPoolContext clientPoolContext = new MscharClientPoolContext(
				mClientServantManager,
				mscharClientServantManager);
		instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, loginRestorer);
//
//		final String xmlPath = ApplicationProperties.getString(XML_KEY, null);
//		if (xmlPath == null) {
//			MClientServantManager mClientServantManager = MClientServantManager.create();
//			MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
//			ClientPoolContext clientPoolContext = new MscharClientPoolContext(
//					mClientServantManager,
//					mscharClientServantManager);
//			instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, loginRestorer);
//
//		} else {
//			// TODO init MClientXMLServantManager and MscharClientXMLServantManager
//		}
	}

	public static ClientSessionEnvironment getInstance() {
		return instance;
	}
}
