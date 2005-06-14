/*
 * $Id: TestCMServer.java,v 1.1 2005/06/14 12:02:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/14 12:02:52 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestCMServer extends CommonTest {
	private static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	private static final String KEY_LOGIN = "Login";
	private static final String KEY_PASSWORD = "Password";

	private static final String SERVER_HOST_NAME = "localhost";
	private static final String LOGIN = "sys";
	private static final String PASSWORD = "sys";

	private static LoginServer loginServerRef;
	private static SessionKey_Transferable sessionKeyT;
	private static CMServer cmServerRef;

	public TestCMServer(final String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestCMServer.class);
		TestSetup testSetup = new TestSetup(testSuite) {
			protected void setUp() {
				oneTimeSetUp();
			}
			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return testSetup;
	}

	protected static void oneTimeSetUp() {
		 Application.init(APPLICATION_NAME);

		 try {
			final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
			final String contextName = ContextNameFactory.generateContextName(serverHostName);
			final CORBAServer corbaServer = new CORBAServer(contextName);

			loginServerRef = (LoginServer) corbaServer.resolveReference(ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			final String login = ApplicationProperties.getString(KEY_LOGIN, LOGIN);
			final String password = ApplicationProperties.getString(KEY_PASSWORD, PASSWORD);
			final Identifier_TransferableHolder userIdH = new Identifier_TransferableHolder();
			sessionKeyT = loginServerRef.login(login, password, userIdH);

			cmServerRef = (CMServer) corbaServer.resolveReference(ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);
		}
		catch (Exception e) {
			Log.errorException(e);
			fail(e.getMessage());
		}
	}

	protected static void oneTimeTearDown() {
		try {
			loginServerRef.logout(sessionKeyT);
		}
		catch (Exception e) {
			Log.errorException(e);
			fail(e.getMessage());
		}
	}

	public void testTransmitMeasurementsButIdsByCondition() throws AMFICOMRemoteException {
		final Set ids = new HashSet();
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		final StorableObjectCondition_Transferable conditionT = (StorableObjectCondition_Transferable) ec.getTransferable();
		final Measurement_Transferable[] measurementsT = cmServerRef.transmitMeasurementsButIdsByCondition(idsT,
				conditionT,
				sessionKeyT);
		for (int i = 0; i < measurementsT.length; i++) {
			Log.debugMessage("Loaded: " + measurementsT[i].header.id.identifier_string, Log.DEBUGLEVEL02);
		}
	}
}
