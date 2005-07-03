/*
 * $Id: TestMCM.java,v 1.5 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMCM extends TestCase {
	private static final String KEY_MCM_ID = "MCMID";
	private static final String KEY_LOGIN = "Login";
	private static final String KEY_PASSWORD = "Password";

	private static final String MCM_ID = "MCM_19";
	private static final String LOGIN = "mserver";
	private static final String PASSWORD = "MServer";

	private static LoginServer loginServerRef;
	private static SessionKey_Transferable sessionKeyT;
	private static MCM mcmRef;

	public TestMCM(final String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(new TestMCM("testTransmitMeasurements"));
		testSuite.addTest(new TestMCM("testTransmitMeasurementsButIdsByCondition"));
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
		System.out.println("********* one time set up **********");
		Application.init(APPLICATION_NAME);

		 try {
			final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
			final String contextName = ContextNameFactory.generateContextName(serverHostName);
			final CORBAServer corbaServer = new CORBAServer(contextName);

			loginServerRef = (LoginServer) corbaServer.resolveReference(ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			final String login = ApplicationProperties.getString(KEY_LOGIN, LOGIN);
			final String password = ApplicationProperties.getString(KEY_PASSWORD, PASSWORD);
			final IdlIdentifierHolder userIdH = new IdlIdentifierHolder();
			sessionKeyT = loginServerRef.login(login, password, userIdH);

			final String mcmIdString = ApplicationProperties.getString(KEY_MCM_ID, MCM_ID);
			mcmRef = (MCM) corbaServer.resolveReference(mcmIdString);
		}
		catch (Exception e) {
			Log.errorException(e);
			fail(e.getMessage());
		}
	}

	protected static void oneTimeTearDown() {
		System.out.println("********* one time tear down **********");
		try {
			loginServerRef.logout(sessionKeyT);
		}
		catch (Exception e) {
			Log.errorException(e);
			fail(e.getMessage());
		}
	}

	public void testTransmitMeasurements() throws AMFICOMRemoteException {
		final Set ids = new HashSet();
		Identifier id;

		id = new Identifier("Measurement_2701");
		ids.add(id);
		id = new Identifier("Measurement_2741");
		ids.add(id);
		id = new Identifier("Measurement_2742");
		ids.add(id);
		id = new Identifier("Measurement_2744");
		ids.add(id);
		id = new Identifier("Measurement_2754");
		ids.add(id);
		id = new Identifier("Measurement_2755");
		ids.add(id);

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		
		final Measurement_Transferable[] measurementsT = mcmRef.transmitMeasurements(idsT);
		for (int i = 0; i < measurementsT.length; i++) {
			Log.debugMessage("Loaded: " + measurementsT[i].header.id.identifier_string, Log.DEBUGLEVEL02);
		}
	}

	public void testTransmitMeasurementsButIdsByCondition() throws AMFICOMRemoteException {
		final Set ids = new HashSet();
		Identifier id;

		id = new Identifier("Measurement_2701");
		ids.add(id);
		id = new Identifier("Measurement_2741");
		ids.add(id);

		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		final StorableObjectCondition_Transferable conditionT = (StorableObjectCondition_Transferable) ec.getTransferable();

		final Measurement_Transferable[] measurementsT = mcmRef.transmitMeasurementsButIdsByCondition(idsT, conditionT);
		for (int i = 0; i < measurementsT.length; i++) {
			Log.debugMessage("Loaded: " + measurementsT[i].header.id.identifier_string, Log.DEBUGLEVEL02);
		}
	}

	public void test3() {
		assertEquals("test 3", 1, 1);
	}

	static class TestMCMLoginRestorer implements LoginRestorer {

		public boolean restoreLogin() {
			return true;
		}

		public String getLogin() {
			return LOGIN;
		}

		public String getPassword() {
			return PASSWORD;
		}
	}

}
