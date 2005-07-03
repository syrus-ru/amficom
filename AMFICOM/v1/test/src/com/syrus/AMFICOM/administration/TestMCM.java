/*
 * $Id: TestMCM.java,v 1.4 2005/06/28 15:28:24 arseniy Exp $ Copyright � 2004 Syrus Systems. ������-����������� �����. ������:
 * �������.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/28 15:28:24 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMCM extends TestCase {

	public TestMCM(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestMCM.class);
		return databaseCommonTest.createTestSetup();
	}

	public void _testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SERVER_CODE);
		final Server server = (Server) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Server '" + server.getId() + "'");

		TypicalCondition tc = new TypicalCondition(SystemUserWrapper.MCM_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		final SystemUser mcmUser = (SystemUser) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		System.out.println("User '" + mcmUser.getId() + "'");

		final String hostname = "mongol";
		final MCM mcm = MCM.createInstance(DatabaseCommonTest.getSysUser().getId(),
				server.getDomainId(),
				"���",
				"��������� �����������",
				hostname,
				mcmUser.getId(),
				server.getId());
		StorableObjectPool.flush(mcm, true);
	}

	public void testRetrieve() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MCM_CODE);
		final MCM mcm = (MCM) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("MCM: " + mcm.getId() + ", hostname: " + mcm.getHostName());
	}
}
