/*
 * $Id: ClientMeasurementServerTest.java,v 1.2 2004/09/21 14:28:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.util.Date;

import com.syrus.AMFICOM.cmserver.CMServerImpl;
import com.syrus.AMFICOM.cmserver.DatabaseContextSetup;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:28:04 $
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
			CMServerImpl server = new CMServerImpl();

			AccessIdentifier_Transferable accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			Identifier id = new Identifier("Null_0");
			
			Identifier domainId = new Identifier("Domain_19");
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable) domainId.getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

			long time0 = System.currentTimeMillis();
			// 2 month ago
			Date start = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 31 * 2);
			Date end = new Date(System.currentTimeMillis());
			Test_Transferable[] test_Transferables = server.transmitTestsByTime(start.getTime(), end.getTime(),
												accessIdentifier_Transferable);
			long time1 = System.currentTimeMillis();
			System.out.println("transmit " + test_Transferables.length + " test(s) for " + (time1 - time0) + " ms");

			Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[test_Transferables.length];
			for (int i = 0; i < identifier_Transferables.length; i++) {
				identifier_Transferables[i] = test_Transferables[i].id;
			}
			long time2 = System.currentTimeMillis();
			Measurement_Transferable[] measurement_Transferables = server
					.transmitMeasurementForTests(identifier_Transferables, accessIdentifier_Transferable);
			long time3 = System.currentTimeMillis();
			System.out.println("transmit " + measurement_Transferables.length + " measuremen(s) for "
					+ (time3 - time2) + " ms");

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
