/*
 * $Id: ClientServantManager.java,v 1.17 2005/10/30 15:20:24 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
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
 * @version $Revision: 1.17 $, $Date: 2005/10/30 15:20:24 $
 * @author $Author: bass $
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
	private Map<String, Boolean>	connectionLostMap;

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
		
		this.connectionLostMap = new HashMap<String, Boolean>();
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		return LoginServerHelper.narrow(this.getVerifiableReference(this.loginServerServantName));
	}

	public EventServer getEventServerReference() throws CommunicationException {
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		return IdentifierGeneratorServerHelper.narrow(this.getVerifiableReference(this.commonServerServantName));
	}

	public CommonServer getServerReference() throws CommunicationException {
		return CommonServerHelper.narrow(this.getVerifiableReference(this.commonServerServantName));
	}

	@Override
	protected final void onLoseConnection(final String servantName) {
		this.connectionLostMap.put(servantName, Boolean.valueOf(true));
		assert Log.debugMessage("Connection with '" + servantName + "' lost",
			Log.DEBUGLEVEL08);
		final String msg = I18N.getString("Error.ConnectionWith.ConnectionWith") + " '" + servantName + "' " + I18N.getString("Error.ConnectionWith.Lost");
		final Boolean lost = this.connectionLostMap.get(servantName);
		if (lost != null && !lost.booleanValue()) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				msg,
				I18N.getString("Error.ErrorOccur"),
				JOptionPane.ERROR_MESSAGE);		
		}
		this.connectionLostMap.put(servantName, Boolean.TRUE);
	}

	@Override
	protected final void onRestoreConnection(final String servantName) {
		assert Log.debugMessage("Connection with '" + servantName + "' restored",
			Log.DEBUGLEVEL08);
		final String msg = I18N.getString("Common.ClientServantManager.ConnectionWith.ConnectionWith") 
			+ " '" + servantName 
			+ "' " + I18N.getString("Common.ClientServantManager.ConnectionWith.Restored");
		
		final Boolean lost = this.connectionLostMap.get(servantName);
		if (lost != null && lost.booleanValue()) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				msg,
				I18N.getString("Common.ClientServantManager.Title"),
				JOptionPane.INFORMATION_MESSAGE);
			this.connectionLostMap.put(servantName, Boolean.valueOf(false));
		}

	}
}
