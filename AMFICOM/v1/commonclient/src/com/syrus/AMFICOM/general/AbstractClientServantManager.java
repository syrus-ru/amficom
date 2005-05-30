/*-
 * $Id: AbstractClientServantManager.java,v 1.1 2005/05/30 15:13:02 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/30 15:13:02 $
 * @module commonclient_v1
 */
public abstract class AbstractClientServantManager extends VerifiedConnectionManager implements ClientServantManager, ServerConnectionManager {
	private String loginServerServantName;

	private String eventServerServantName;

	/**
	 * Currently, can hold the name of CMServer, MSHServer and ARServer
	 * servants.
	 */
	private String commonServerServantName;

	/**
	 * @param corbaServer
	 * @param loginServerServantName
	 * @param eventServerServantName
	 * @param commonServerServantName
	 */
	public AbstractClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String commonServerServantName) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, commonServerServantName});

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.commonServerServantName = commonServerServantName;
	}

	public final LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return (LoginServer) super.getVerifiableReference(this.loginServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public final EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public final IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		try {
			return (IdentifierGeneratorServer) super.getVerifiableReference(this.commonServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}
	
	public final CommonServer getServerReference() throws CommunicationException {
		try {
			return (CommonServer) super.getVerifiableReference(this.commonServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	protected final void onLoseConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onLoseConnection() | Connection with '"
				+ servantName + "' lost",
				Log.DEBUGLEVEL08);
		/**
		 * @todo Maybe, GUI-specific actions
		 */
	}

	protected final void onRestoreConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onRestoreConnection() | Connection with '"
				+ servantName
				+ "' restored",
				Log.DEBUGLEVEL08);
		/**
		 * @todo Maybe, GUI-specific actions
		 */
	}
}
