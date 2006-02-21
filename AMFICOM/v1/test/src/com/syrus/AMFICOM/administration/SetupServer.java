/*
 * $Id: SetupServer.java,v 1.1.2.2 2006/02/21 15:55:15 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/21 15:55:15 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupServer extends TestCase {
	private static final String KEY_NAME = "Name.Server";
	private static final String KEY_DESCRIPTION = "Description.Server";
	private static final String KEY_SERVER_HOST_NAME = "ServerHostName";

	private static final String SERVER_HOST_NAME = "amficom";

	public SetupServer(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupServer.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		assertTrue("No domains", domains.size() > 0);
		final Domain domain = domains.iterator().next();
		System.out.println("Domain '" + domain.getId() + "'");

		final Server server = Server.createInstance(userId,
				domain.getId(),
				I18N.getString(KEY_NAME),
				I18N.getString(KEY_DESCRIPTION),
				ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME));

		StorableObjectPool.flush(server, userId, true);
	}

}
