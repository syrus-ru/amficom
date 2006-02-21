/*
 * $Id: SetupMCM.java,v 1.1.2.1 2006/02/21 15:53:40 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
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
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.setup.I18N;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/21 15:53:40 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupMCM extends TestCase {
	private static final String KEY_NAME = "Name.MCM.MCM";
	private static final String KEY_DESCRIPTION = "Description.MCM.MCM";
	private static final String KEY_MCM_HOST_NAME = "MCMHostName";

	private static final String SERVER_MCM_NAME = "amficom";

	public SetupMCM(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupMCM.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SERVER_CODE);
		final Set<Server> servers = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		assertTrue("No servers", servers.size() > 0);
		final Server server = servers.iterator().next();
		System.out.println("Server '" + server.getId() + "'");

		final TypicalCondition tc = new TypicalCondition(SystemUserWrapper.MCM_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		final Set<SystemUser> systemUsers = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		assertTrue("No MCM users", systemUsers.size() > 0);
		final SystemUser mcmUser = systemUsers.iterator().next();
		System.out.println("User '" + mcmUser.getId() + "'");

		final MCM mcm = MCM.createInstance(userId,
				server.getDomainId(),
				I18N.getString(SetupSystemUser.KEY_MCM_NAME),
				I18N.getString(SetupSystemUser.KEY_MCM_DESCRIPTION),
				ApplicationProperties.getString(KEY_MCM_HOST_NAME, SERVER_MCM_NAME),
				mcmUser.getId(),
				server.getId());
		StorableObjectPool.flush(mcm, userId, true);
	}
}
