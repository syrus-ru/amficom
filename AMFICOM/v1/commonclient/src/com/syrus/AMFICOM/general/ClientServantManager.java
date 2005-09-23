/*
 * $Id: ClientServantManager.java,v 1.10 2005/09/23 15:02:33 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.CommonServerHelper;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServerHelper;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.leserver.corba.LoginServerHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/09/23 15:02:33 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
abstract class ClientServantManager extends VerifiedConnectionManager implements BaseConnectionManager, ServerConnectionManager {
	static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	static final String SERVER_HOST_NAME = "localhost";

	private String loginServerServantName;
	private String eventServerServantName;

	/**
	 * Currently, can hold the name of CMServer and MscharServer
	 * servants.
	 */
	private String commonServerServantName;

	/**
	 * @param corbaServer
	 * @param loginServerServantName
	 * @param eventServerServantName
	 * @param commonServerServantName
	 */

	public ClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String commonServerServantName) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, commonServerServantName});

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.commonServerServantName = commonServerServantName;
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return LoginServerHelper.narrow(super.getVerifiableReference(this.loginServerServantName));
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return EventServerHelper.narrow(super.getVerifiableReference(this.eventServerServantName));
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		try {
			return IdentifierGeneratorServerHelper.narrow(super.getVerifiableReference(this.commonServerServantName));
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public CommonServer getServerReference() throws CommunicationException {
		try {
			return CommonServerHelper.narrow(super.getVerifiableReference(this.commonServerServantName));
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	@Override
	protected final void onLoseConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onLoseConnection() | Connection with '" + servantName + "' lost",
			Log.DEBUGLEVEL08);
		final String msg = LangModelGeneral.getString("Error.ConnectionWith.ConnectionWith") + " '" + servantName + "' " + LangModelGeneral.getString("Error.ConnectionWith.Lost");
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
			msg,
			LangModelGeneral.getString("Error.ErrorOccur"),
			JOptionPane.ERROR_MESSAGE);		
	}

	@Override
	protected final void onRestoreConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onRestoreConnection() | Connection with '" + servantName + "' restored",
			Log.DEBUGLEVEL08);
		final String msg = LangModelGeneral.getString("Text.InfoMessage.ConnectionWith.ConnectionWith") 
			+ " '" + servantName 
			+ "' " + LangModelGeneral.getString("Text.InfoMessage.ConnectionWith.Restored");
//		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
//			msg,
//			LangModelGeneral.getString("Text.InfoMessage.Title"),
//			JOptionPane.INFORMATION_MESSAGE);

	}
}
