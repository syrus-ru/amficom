/*
 * $Id: MeasurementControlModuleSetup.java,v 1.1 2005/03/15 16:11:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm.setup;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mcm.DatabaseContextSetup;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:21 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public class MeasurementControlModuleSetup {
	public static final String KEY_ID = "ID";
	public static final String KEY_SETUP_SERVER_ID = "SetupServerID";	
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	public static final String ID = "mcm_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	object reference to Measurement Server	*/
	protected static MServer mServerRef;

	public static void main(String[] args) {

		String setupServerId = ApplicationProperties.getString(KEY_SETUP_SERVER_ID, null);
		if (setupServerId == null) {
			Log.errorMessage("Cannot find key '" + KEY_SETUP_SERVER_ID + "' in file " + ApplicationProperties.getFileName());
			System.exit(-1);
		}

		activateCORBASetupServer();
		activateSetupServerReference(setupServerId);

		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Load object types*/
		DatabaseContextSetup.initObjectPools();

		Identifier id;
		User user;
		Domain domain;
		Server server;
		MCM mcm;

		id = new Identifier(ApplicationProperties.getString(KEY_ID, ID));
		MCM_Transferable mcmT = null;
		try {
			Log.debugMessage("Fetching MCM '" + id + "' from server", Log.DEBUGLEVEL05);
			mcmT = mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}


		id = new Identifier(mcmT.domain_id);
		Domain_Transferable domainT = null;
		try {
			Log.debugMessage("Fetching domain '" + id + "' (mcm domain) from server", Log.DEBUGLEVEL05);
			domainT = mServerRef.transmitDomain((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}

		try {
			id = new Identifier(domainT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (domain creator)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(domainT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (domain modifier)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(domainT.header.id);
			Log.debugMessage("Getting domain '" + id + "' ", Log.DEBUGLEVEL05);
			domain = (Domain)AdministrationStorableObjectPool.getStorableObject(id, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(-1);
		}


		id = new Identifier(mcmT.server_id);
		Server_Transferable serverT = null;
		try {
			Log.debugMessage("Fetching server '" + id + "' (mcm server) from server", Log.DEBUGLEVEL05);
			serverT = mServerRef.transmitServer((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}

		try {
			id = new Identifier(serverT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (server creator)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (server modifier)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.user_id);
			Log.debugMessage("Getting user '" + id + "' (server user)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.header.id);
			Log.debugMessage("Getting server '" + id + "' ", Log.DEBUGLEVEL05);
			server = (Server)AdministrationStorableObjectPool.getStorableObject(id, true);
	
	
			id = new Identifier(mcmT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (mcm creator)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (mcm modifier)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.user_id);
			Log.debugMessage("Getting user '" + id + "' (mcm user)", Log.DEBUGLEVEL05);
			user = (User)AdministrationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.header.id);
			Log.debugMessage("Getting MCM '" + id + "' ", Log.DEBUGLEVEL05);
			mcm = (MCM)AdministrationStorableObjectPool.getStorableObject(id, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(-1);
		}

		/*	Close database connection*/
		DatabaseConnection.closeConnection();
	
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activateCORBASetupServer() {
		/*	Create local CORBA server*/
		try {
			corbaServer = new CORBAServer();
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activateSetupServerReference(String setupServerId) {
		/*	Obtain reference to setup server	*/
		try {
			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(setupServerId));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
	}
}
