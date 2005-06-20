/*
 * $Id: TestServer.java,v 1.3 2005/06/20 15:13:53 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/20 15:13:53 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServer extends TestCase {

	public TestServer(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestServer.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		final Domain domain = (Domain) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Domain '" + domain.getId() + "'");

		final Server server = Server.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"Первый сервер",
				"Хороший сервер",
				ApplicationProperties.getString(CORBACommonTest.KEY_SERVER_HOST_NAME, CORBACommonTest.SERVER_HOST_NAME));

		StorableObjectPool.flush(server, true);
	}
}
