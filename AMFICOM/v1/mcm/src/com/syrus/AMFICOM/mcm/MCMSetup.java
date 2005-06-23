/*
 * $Id: MCMSetup.java,v 1.1 2005/06/23 12:39:30 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_MCM_ID;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.MCM_ID;

import java.util.Set;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;


public final class MCMSetup {
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
			/*-Setup all base information about this MCM*/
			bootstrap();

			final MCM mcm = (MCM) StorableObjectPool.getStorableObject(ID, true);
			final LinkedIdsCondition lic = new LinkedIdsCondition(ID, ObjectEntities.KIS_CODE);
			final Set kiss = StorableObjectPool.getStorableObjectsByCondition(lic, true);
			
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
		/*-Establish connection with database*/
		MeasurementControlModule.establishDatabaseConnection();

		/*-Initialize database context*/
		DatabaseContextSetup.initDatabaseContext();

		/*-Create SessionEnvironment*/
		final String setupServerHostName = ApplicationProperties.getString(KEY_SETUP_SERVER_HOST_NAME, SETUP_SERVER_HOST_NAME);
		MCMSessionEnvironment.createInstance(setupServerHostName);

		/*-Login*/
		final String mcmUserLogin = ApplicationProperties.getString(KEY_MCM_USER_LOGIN, MCM_USER_LOGIN);
		final String mcmUserPassword = ApplicationProperties.getString(KEY_MCM_USER_PASSWORD, MCM_USER_PASSWORD);
		MCMSessionEnvironment.getInstance().login(mcmUserLogin, mcmUserPassword);

		/*Init database loader with retrieved from server identifier of the user, corresponding to this MCM.*/
		DatabaseObjectLoader.init(retrieveUserId());
	}

	private static Identifier retrieveUserId() throws ApplicationException {
		final MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
		final IdlMCM[] idlmcms;
		try {
			idlmcms = mServerRef.transmitMCMs(new IdlIdentifier[] {ID.getTransferable()}, LoginManager.getSessionKeyTransferable());
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
		if (idlmcms.length < 1)
			throw new IllegalDataException("Cannot find MCM '" + ID + "'");

		final Identifier mcmUserId = new Identifier(idlmcms[0].userId);
		return mcmUserId;
	}
}
