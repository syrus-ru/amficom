/*
 * $Id: ClientMeasurementServerTest.java,v 1.6 2004/09/23 10:14:44 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.util.Date;

import com.syrus.AMFICOM.cmserver.CMServerImpl;
import com.syrus.AMFICOM.cmserver.DatabaseContextSetup;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.6 $, $Date: 2004/09/23 10:14:44 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public class ClientMeasurementServerTest {

	public static final String		APPLICATION_NAME		= "cmserver";

	public static final String		KEY_DB_HOST_NAME		= "DBHostName";
	public static final String		KEY_DB_SID			= "DBSID";
	public static final String		KEY_DB_CONNECTION_TIMEOUT	= "DBConnectionTimeout";
	public static final String		KEY_DB_LOGIN_NAME		= "DBLoginName";
	public static final String		KEY_TICK_TIME			= "TickTime";
	public static final String		KEY_MAX_FALLS			= "MaxFalls";

	public static final String		DB_SID				= "amficom";
	public static final int			DB_CONNECTION_TIMEOUT		= 120;
	public static final String		DB_LOGIN_NAME			= "amficom";

	public static final int			TICK_TIME			= 5;

	private CMServerImpl			server;
	private AccessIdentifier_Transferable	accessIdentifier_Transferable;

	public ClientMeasurementServerTest() {
		try {
			this.server = new CMServerImpl();

			this.accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			Identifier id = new Identifier("Null_0");

			Identifier domainId = new Identifier("Domain_19");
			this.accessIdentifier_Transferable.domain_id = (Identifier_Transferable) domainId.getTransferable();
			this.accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			this.accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

			testTransmitMeasurementX();
			testTransmitMeasurementX();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testTransmitMeasurementX() throws AMFICOMRemoteException {
		//      Checking method transmitMeasurements(null , acc)
		System.out.println("Checking method transmitMeasurementX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables = this.server
				.transmitMeasurements(identifier_Transferables, this.accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + measurement_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitMeasurement(Id_Trans , acc)
		Measurement_Transferable measurement_Transferable = measurement_Transferables[0];
		Identifier_Transferable id_Tf = measurement_Transferable.id;
		long time2 = System.currentTimeMillis();
		Measurement_Transferable measurement_Transferable2 = this.server
				.transmitMeasurement(id_Tf, this.accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit  measurement_Transferable2  for " + (time3 - time2) + " ms");
		//      Checking method transmitMeasurement(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables3 = this.server
				.transmitMeasurements(identifier_Transferables2, this.accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
	}

	private static void startup() {
		/* Establish connection with database */
		establishDatabaseConnection();

		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		/* Start main loop */
		final ClientMeasurementServerTest clientMeasurementServer = new ClientMeasurementServerTest();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}
}
