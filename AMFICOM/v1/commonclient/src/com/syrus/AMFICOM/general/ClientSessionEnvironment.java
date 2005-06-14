/*
 * $Id: ClientSessionEnvironment.java,v 1.13 2005/06/14 11:26:36 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TIntObjectHashMap;

import java.beans.PropertyChangeListener;
import java.util.Date;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/06/14 11:26:36 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {

	// TODO enum SessionKind
	public static final int				SESSION_KIND_MEASUREMENT	= 1;
	public static final int				SESSION_KIND_MAPSCHEME		= 2;
	// Other kinds -- Map, Scheme, Resources, etc.

	public static final String			SESSION_KIND_KEY			= "SessionKind";

	public static final String			XMLSESSION_KEY				= "XMLSession";
	public static final String			XML_PATH_KEY				= "XMLPath";

	private String						xmlPath;

	private static TIntObjectHashMap	kindInstanceMap;
	private static LoginRestorer		loginRestorer;

	private ClientSessionEnvironment(final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer);
	}

	private ClientSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final ClientPoolContext clientPoolContext,
			final String xmlPath) {
		super(baseConnectionManager, clientPoolContext, null);
		this.xmlPath = xmlPath;
	}

	public ClientServantManager getClientServantManager() {
		return (ClientServantManager) super.baseConnectionManager;
	}

	public String getServerName() {
		if (this.xmlPath == null) { return super.baseConnectionManager.getCORBAServer().getRootContextName(); }
		return "XML:" + this.xmlPath;
	}

	public static void setLoginRestorer(LoginRestorer loginRestorer1) {
		loginRestorer = loginRestorer1;
	}

	public static ClientSessionEnvironment getInstance(int sessionKind) throws CommunicationException, IllegalDataException {
		if (kindInstanceMap == null) {
			kindInstanceMap = new TIntObjectHashMap(2);
		}

		ClientSessionEnvironment instance = (ClientSessionEnvironment) kindInstanceMap.get(sessionKind);

		if (instance == null) {
			instance = createInstance(sessionKind);
			kindInstanceMap.put(sessionKind, instance);
		}

		return instance;
	}

	private static ClientSessionEnvironment createInstance(int sessionKind) throws CommunicationException, IllegalDataException {
		ClientSessionEnvironment instance = null;
		switch (sessionKind) {
			case SESSION_KIND_MEASUREMENT:
				instance = createMeasurementSession();
				break;
			case SESSION_KIND_MAPSCHEME:
				instance = createMapSchemeSession();
				break;
			default:
				String msg = "Unknown kind of session -- " + sessionKind;
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);
		}

		/* Print available objects -- for debugging purpose */
		if (instance != null && instance.xmlPath == null) {
			instance.baseConnectionManager.getCORBAServer().printNamingContext();
		}

		return instance;
	}

	public void login(	String login,
						String password) throws CommunicationException, LoginException {
		if (this.xmlPath == null) {
			super.login(login, password);
		} else {
			LoginManager.login(login, password);
			IdentifierPool.deserialize();
			super.poolContext.deserialize();

			super.sessionEstablishDate = new Date(System.currentTimeMillis());
			super.sessionEstablished = true;
		}
	}

	public void logout() throws CommunicationException, LoginException {
		if (this.xmlPath == null) {
			super.logout();
		} else {

			this.logout0();

			this.sessionEstablishDate = null;
			this.sessionEstablished = false;
		}
	}

	public void addPropertyListener(PropertyChangeListener listener) {
		if (this.xmlPath == null) {
			this.getClientServantManager().addPropertyListener(listener);
		}
	}

	public void removePropertyListener(PropertyChangeListener listener) {
		if (this.xmlPath == null) {
			this.getClientServantManager().removePropertyListener(listener);
		}
	}

	private static ClientSessionEnvironment createMeasurementSession() throws CommunicationException {
		ClientSessionEnvironment instance = null;

		String xmlSession = ApplicationProperties.getString(XMLSESSION_KEY, "false");
		boolean usingXMLSession = xmlSession.equalsIgnoreCase("true") || xmlSession.equalsIgnoreCase("yes");
		if (!usingXMLSession) {
			ClientServantManager mClientServantManager = MClientServantManager.create();
			ClientPoolContext clientPoolContext = new MClientPoolContext(mClientServantManager);
			instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
		} else {
			final String xmlPath = ApplicationProperties.getString(XML_PATH_KEY, null);
			Log.debugMessage("ClientSessionEnvironment.createMeasurementSession | xmlPath " + xmlPath, Log.FINEST);
			MClientXMLServantManager mClientServantManager = MClientXMLServantManager.create();
			ClientPoolContext clientPoolContext = new MClientPoolContext(xmlPath);
			instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, xmlPath);
		}

		return instance;
	}

	private static ClientSessionEnvironment createMapSchemeSession() throws CommunicationException {
		ClientSessionEnvironment instance = null;

		String xmlSession = ApplicationProperties.getString(XMLSESSION_KEY, "false");
		boolean usingXMLSession = xmlSession.equalsIgnoreCase("true") || xmlSession.equalsIgnoreCase("yes");
		if (usingXMLSession) {
			// final String xmlPath =
			// ApplicationProperties.getString(XML_PATH_KEY, null);
			// TODO init MClientXMLServantManager and
			// MscharClientXMLServantManager
		} else {
			MClientServantManager mClientServantManager = MClientServantManager.create();
			MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
			ClientPoolContext clientPoolContext = new MscharClientPoolContext(mClientServantManager,
																				mscharClientServantManager);
			instance = new ClientSessionEnvironment(mscharClientServantManager, clientPoolContext, loginRestorer);

		}

		return instance;
	}
	//
	// public static ClientSessionEnvironment getInstance() {
	// return instance;
	// }
}
