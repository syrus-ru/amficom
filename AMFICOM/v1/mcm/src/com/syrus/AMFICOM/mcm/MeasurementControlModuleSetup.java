/*
 * $Id: MeasurementControlModuleSetup.java,v 1.2 2005/03/23 19:09:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/23 19:09:52 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public class MeasurementControlModuleSetup {
	public static final String APPLICATION_NAME = "mcm";

	public static final String KEY_ID = "ID";
	public static final String KEY_SETUP_SERVER_HOST_NAME = "SetupServerHostName";	
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

	private MeasurementControlModuleSetup() {
		//singleton
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		String setupServerHostName = ApplicationProperties.getString(KEY_SETUP_SERVER_HOST_NAME, null);
		String setupServerId = ApplicationProperties.getString(KEY_SETUP_SERVER_ID, null);
		if (setupServerHostName == null) {
			Log.errorMessage("Cannot find key '" + KEY_SETUP_SERVER_HOST_NAME + "' in file " + ApplicationProperties.getFileName());
			System.exit(-1);
		}
		if (setupServerId == null) {
			Log.errorMessage("Cannot find key '" + KEY_SETUP_SERVER_ID + "' in file " + ApplicationProperties.getFileName());
			System.exit(-1);
		}

		activateCORBASetupServer(setupServerHostName);
		activateSetupServerReference(setupServerId);

		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Load object types*/
		DatabaseContextSetup.initObjectPools();

		setup();

		DatabaseConnection.closeConnection();
	}
	
	private static void setup() {
		Identifier id;
		
		id = new Identifier(ApplicationProperties.getString(KEY_ID, ID));
		try {
			//MCM, Users, Server, Domain
			MCM mcm = new MCM(mServerRef.transmitMCM((Identifier_Transferable) id.getTransferable()));

			id = mcm.getCreatorId();
			User user = new User(mServerRef.transmitUser((Identifier_Transferable) id.getTransferable()));
			AdministrationDatabaseContext.getUserDatabase().insert(user);

			id = mcm.getDomainId();
			Domain domain = new Domain(mServerRef.transmitDomain((Identifier_Transferable) id.getTransferable()));
			AdministrationDatabaseContext.getDomainDatabase().insert(domain);

			id = mcm.getServerId();
			Server server = new Server(mServerRef.transmitServer((Identifier_Transferable) id.getTransferable()));

			id = server.getUserId();
			user = new User(mServerRef.transmitUser((Identifier_Transferable) id.getTransferable()));
			AdministrationDatabaseContext.getUserDatabase().insert(user);

			AdministrationDatabaseContext.getServerDatabase().insert(server);

			id = mcm.getUserId();
			user = new User(mServerRef.transmitUser((Identifier_Transferable) id.getTransferable()));
			AdministrationDatabaseContext.getUserDatabase().insert(user);

			AdministrationDatabaseContext.getMCMDatabase().insert(mcm);


			//KISs and Equipments
			LinkedIdsCondition lic = new LinkedIdsCondition(mcm.getId(), ObjectEntities.KIS_ENTITY_CODE);
			KIS_Transferable[] kissT = mServerRef.transmitKISsButIdsByCondition(new Identifier_Transferable[0],
					StorableObjectConditionBuilder.getConditionTransferable(lic));
			KIS kis;
			Equipment equipment;
			Set kiss = new HashSet();
			Set equipments = new HashSet();
			for (int i = 0; i < kissT.length; i++) {
				kis = new KIS(kissT[i]);
				kiss.add(kis);

				id = kis.getEquipmentId();
				equipment = new Equipment(mServerRef.transmitEquipment((Identifier_Transferable) id.getTransferable()));
				equipments.add(equipment);
			}
			ConfigurationDatabaseContext.getEquipmentDatabase().insert(equipments);
			ConfigurationDatabaseContext.getKISDatabase().insert(kiss);

			//Ports
			Set ids = new HashSet(equipments.size());
			for (Iterator it = equipments.iterator(); it.hasNext();) {
				equipment = (Equipment) it.next();
				ids.add(equipment.getId());
			}
			lic = new LinkedIdsCondition(ids, ObjectEntities.PORT_ENTITY_CODE);
			Port_Transferable[] portsT = mServerRef.transmitPortsButIdsByCondition(new Identifier_Transferable[0],
					StorableObjectConditionBuilder.getConditionTransferable(lic));
			Collection ports = new HashSet(portsT.length);
			for (int i = 0; i < portsT.length; i++)
				ports.add(new Port(portsT[i]));
			ConfigurationDatabaseContext.getPortDatabase().insert(ports);

			//Measurement Ports
			ids = new HashSet(kiss.size());
			for (Iterator it = kiss.iterator(); it.hasNext();) {
				kis = (KIS) it.next();
				ids.add(kis.getId());
			}
			lic = new LinkedIdsCondition(ids, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
			MeasurementPort_Transferable[] measurementPortsT = mServerRef.transmitMeasurementPortsButIdsByCondition(new Identifier_Transferable[0],
					StorableObjectConditionBuilder.getConditionTransferable(lic));
			Collection measurementPorts = new HashSet(measurementPortsT.length);
			for (int i = 0; i < measurementPortsT.length; i++)
				measurementPorts.add(new MeasurementPort(measurementPortsT[i]));
			ConfigurationDatabaseContext.getMeasurementPortDatabase().insert(measurementPorts);

			//Transmission Paths
			ids = new HashSet(ports.size());
			Port port;
			for (Iterator it = ports.iterator(); it.hasNext();) {
				port = (Port) it.next();
				ids.add(port.getId());
			}
			lic = new LinkedIdsCondition(ids, ObjectEntities.TRANSPATH_ENTITY_CODE);
			TransmissionPath_Transferable[] transmissionPathsT = mServerRef.transmitTransmissionPathsButIdsByCondition(new Identifier_Transferable[0],
					StorableObjectConditionBuilder.getConditionTransferable(lic));
			Collection transmissionPaths = new HashSet(transmissionPathsT.length);
			for (int i = 0; i < transmissionPathsT.length; i++)
				transmissionPaths.add(new TransmissionPath(transmissionPathsT[i]));
			ConfigurationDatabaseContext.getTransmissionPathDatabase().insert(transmissionPaths);

		}
		catch (Exception e) {
			Log.errorException(e);
		}

		
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

	private static void activateCORBASetupServer(String serverHostName) {
		/*	Create local CORBA server*/
		try {
			corbaServer = new CORBAServer(serverHostName);
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
			MeasurementControlModule.mServerRef = mServerRef;
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
	}
}
