/*
 * $Id: ClientMeasurementServerTest.java,v 1.1 2004/09/21 05:42:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import com.syrus.AMFICOM.cmserver.CMServerImpl;
import com.syrus.AMFICOM.cmserver.DatabaseContextSetup;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/21 05:42:03 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public class ClientMeasurementServerTest {

	public static final String	APPLICATION_NAME	= "cmserver";
	
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String	KEY_TICK_TIME		= "TickTime";
	public static final String	KEY_MAX_FALLS		= "MaxFalls";
	
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";


	public static final int		TICK_TIME		= 5;

	public ClientMeasurementServerTest() {
		try {
			CMServerImpl impl = new CMServerImpl();

			AccessIdentifier_Transferable accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			Identifier id = new Identifier("Null_0");
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

			Domain_Transferable[] domain_Transferables = impl
					.transmitDomains(new Identifier_Transferable[0], accessIdentifier_Transferable);

			for (int i = 0; i < domain_Transferables.length; i++) {
				Domain domain = new Domain(domain_Transferables[i]);
				System.out.println(domain.getId().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
	}

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();
		
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		/* Start main loop */
		final ClientMeasurementServerTest clientMeasurementServer = new ClientMeasurementServerTest();
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
}
