/*
 * $Id: MCMSetup.java,v 1.23.2.2 2006/06/07 10:27:44 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
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
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.23.2.2 $, $Date: 2006/06/07 10:27:44 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMSetup {
	private static final String KEY_SETUP_SERVER_HOST_NAME = "SetupServerHostName";
	private static final String KEY_MCM_USER_LOGIN = "MCMUserLogin";
	private static final String KEY_MCM_USER_PASSWORD = "MCMUserPassword";
	private static final String KEY_MCM_DOMAIN_ID = "MCMDomainId";

	private static final String SETUP_SERVER_HOST_NAME = "amficom";
	private static final String MCM_USER_LOGIN = "mcm";
	private static final String MCM_USER_PASSWORD = "MCM";
	private static final String MCM_DOMAIN_ID = "Domain_1";

	private static final Identifier ID;

	static {
		ID = Identifier.valueOf(ApplicationProperties.getString(KEY_MCM_ID, MCM_ID));
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
			Log.errorMessage(ae);
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
		MCMSessionEnvironment.createInstance(setupServerHostName, ID.toString());

		/*-Login*/
		final String mcmUserLogin = ApplicationProperties.getString(KEY_MCM_USER_LOGIN, MCM_USER_LOGIN);
		final String mcmUserPassword = ApplicationProperties.getString(KEY_MCM_USER_PASSWORD, MCM_USER_PASSWORD);
		final Identifier mcmDomainId = Identifier.valueOf(ApplicationProperties.getString(KEY_MCM_DOMAIN_ID, MCM_DOMAIN_ID));
		MCMSessionEnvironment.getInstance().login(mcmUserLogin, mcmUserPassword, mcmDomainId);

		/*-Retrieve data for this MCM*/
		final IdlMCM idlMCM = retrieveIdlMCM();

		/*Init database loader with retrieved from server identifier of the user, corresponding to this MCM.*/
		Identifier id = Identifier.valueOf(idlMCM.creatorId);
		StorableObjectPool.getStorableObject(id, true);

		id = Identifier.valueOf(idlMCM.modifierId);
		StorableObjectPool.getStorableObject(id, true);

		id = Identifier.valueOf(idlMCM.domainId);
		StorableObjectPool.getStorableObject(id, true);

		id = Identifier.valueOf(idlMCM.serverId);
		StorableObjectPool.getStorableObject(id, true);

		id = Identifier.valueOf(idlMCM.userId);
		StorableObjectPool.getStorableObject(id, true);

		id = Identifier.valueOf(idlMCM.id);
		StorableObjectPool.getStorableObject(id, true);
	}

	private static IdlMCM retrieveIdlMCM() throws ApplicationException {
		final MServer mServerRef = MCMSessionEnvironment.getInstance().getConnectionManager().getMServerReference();
		final IdlStorableObject[] idlStorableObjects;
		try {
			idlStorableObjects = mServerRef.transmitStorableObjects(new IdlIdentifier[] { ID.getIdlTransferable() },
					LoginManager.getSessionKey().getIdlTransferable());
		} catch (AMFICOMRemoteException are) {
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
	}
}
