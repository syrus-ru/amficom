/*
 * $Id: TestServer.java,v 1.2 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServer extends DatabaseCommonTest {

	public TestServer(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestServer.class);
		return createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		final Domain domain = (Domain) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Domain '" + domain.getId() + "'");

		final Server server = Server.createInstance(creatorUser.getId(),
				domain.getId(),
				"Первый сервер",
				"Хороший сервер",
				ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME));

		StorableObjectPool.flush(server, true);
	}
}
