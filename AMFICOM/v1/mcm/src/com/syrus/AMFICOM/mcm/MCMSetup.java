/*
 * $Id: MCMSetup.java,v 1.9 2005/08/29 10:28:40 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_MCM_ID;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.MCM_ID;

import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataTypeDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.MeasurementUnitDatabase;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;


final class MCMSetup {
	private static final String KEY_SETUP_SERVER_HOST_NAME = "SetupServerHostName";
	private static final String KEY_MCM_USER_LOGIN = "MCMUserLogin";
	private static final String KEY_MCM_USER_PASSWORD = "MCMUserPassword";

	private static final String SETUP_SERVER_HOST_NAME = "amficom";
	private static final String MCM_USER_LOGIN = "mcm";
	private static final String MCM_USER_PASSWORD = "MCM";

	private static final Identifier ID;

	static {
		ID = new Identifier(ApplicationProperties.getString(KEY_MCM_ID, MCM_ID));
	}

	static void setup() {
		/*-Establish connection with database*/
		MeasurementControlModule.establishDatabaseConnection();

		try {
			/*Create locally hardcoded data*/
			createDictionaries();

			/*-Setup all base information about this MCM*/
			bootstrap();

//			final MCM mcm = (MCM) StorableObjectPool.getStorableObject(ID, true);
//			final LinkedIdsCondition lic = new LinkedIdsCondition(ID, ObjectEntities.KIS_CODE);
//			final Set kiss = StorableObjectPool.getStorableObjectsByCondition(lic, true);
			
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		/*-Close connection with database*/
		DatabaseConnection.closeConnection();

		/*-Exit application*/
		System.exit(1);
	}

	private static void bootstrap() throws ApplicationException {
		/*-Initialize database context*/
		DatabaseContextSetup.initDatabaseContext();

		/*-Create SessionEnvironment*/
		final String setupServerHostName = ApplicationProperties.getString(KEY_SETUP_SERVER_HOST_NAME, SETUP_SERVER_HOST_NAME);
		MCMSessionEnvironment.createInstance(setupServerHostName);

		/*-Login*/
		final String mcmUserLogin = ApplicationProperties.getString(KEY_MCM_USER_LOGIN, MCM_USER_LOGIN);
		final String mcmUserPassword = ApplicationProperties.getString(KEY_MCM_USER_PASSWORD, MCM_USER_PASSWORD);
		MCMSessionEnvironment.getInstance().login(mcmUserLogin, mcmUserPassword);

		/*-Retrieve data for this MCM*/
		final IdlMCM idlMCM = retrieveIdlMCM();

		/*Init database loader with retrieved from server identifier of the user, corresponding to this MCM.*/
		Identifier id = new Identifier(idlMCM.creatorId);
		StorableObjectPool.getStorableObject(id, true);

		id = new Identifier(idlMCM.modifierId);
		StorableObjectPool.getStorableObject(id, true);

		id = new Identifier(idlMCM.domainId);
		StorableObjectPool.getStorableObject(id, true);

		id = new Identifier(idlMCM.serverId);
		StorableObjectPool.getStorableObject(id, true);

		id = new Identifier(idlMCM.userId);
		StorableObjectPool.getStorableObject(id, true);

		id = new Identifier(idlMCM.id);
		StorableObjectPool.getStorableObject(id, true);
	}

	private static IdlMCM retrieveIdlMCM() throws ApplicationException {
		final MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
		final IdlStorableObject[] idlStorableObjects;
		try {
			idlStorableObjects = mServerRef.transmitStorableObjects(new IdlIdentifier[] {ID.getTransferable()}, LoginManager.getSessionKeyTransferable());
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
		if (idlStorableObjects.length < 1) {
			throw new IllegalDataException("Cannot find MCM '" + ID + "'");
		}
		return (IdlMCM) idlStorableObjects[0];
	}

	private static void createDictionaries() throws CreateObjectException {
		DataTypeDatabase.insertAll();
		MeasurementUnitDatabase.insertAll();
		ParameterTypeDatabase.insertAll();
		MeasurementTypeDatabase.insertAll();
		AnalysisTypeDatabase.insertAll();
		ModelingTypeDatabase.insertAll();
	}
}
