/*
 * $Id: TestServer.java,v 1.8 2005/08/30 14:24:53 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.administration;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/30 14:24:53 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServer extends TestCase {

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
				"������ ������",
				"������������ ������",
				ApplicationProperties.getString(CORBACommonTest.KEY_SERVER_HOST_NAME, CORBACommonTest.SERVER_HOST_NAME));

		StorableObjectPool.flush(server, DatabaseCommonTest.getSysUser().getId(), true);
	}

	public void _testRetrieve() throws ApplicationException {
		final Server server = new Server(new Identifier("Server_30"));
		System.out.println("Retrieved: " + server.getHostName());
		final Set<Characteristic> characteristics = server.getCharacteristics(true);
		for (final Characteristic characteristic : characteristics) {
			System.out.println("Characteristic: " + characteristic.getName());
		}
	}
}
