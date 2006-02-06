/*
 * $Id: TestServer.java,v 1.10 2005/12/15 13:41:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/15 13:41:40 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServer extends TestCase {
	private static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	private static final String SERVER_HOST_NAME = "amficom";

	public TestServer(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		//databaseCommonTest.addTestSuite(TestServer.class);
		databaseCommonTest.addTest(new TestServer("testRetrieve"));
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		final Domain domain = (Domain) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Domain '" + domain.getId() + "'");

		final Server server = Server.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"Первый сервер",
				"Единственный сервер",
				ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME));

		StorableObjectPool.flush(server, DatabaseCommonTest.getSysUser().getId(), true);
	}

	public void _testRetrieve() throws ApplicationException {
		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(new Identifier("Server_30"));
		System.out.println("Retrieved: " + server.getHostName());
		final Set<Characteristic> characteristics = server.getCharacteristics(true);
		for (final Characteristic characteristic : characteristics) {
			System.out.println("Characteristic: " + characteristic.getName());
		}
	}
}
