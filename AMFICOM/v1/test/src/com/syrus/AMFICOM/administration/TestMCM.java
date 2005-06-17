/*
 * $Id: TestMCM.java,v 1.1 2005/06/17 20:17:16 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/17 20:17:16 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMCM extends CommonTest {

	public TestMCM(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestMCM.class);
	}

public void testCreateInstance() throws ApplicationException {
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
		final MCM mcm = MCM.createInstance(creatorUser.getId(),
				server.getDomainId(),
				"МУИ",
				"Управляет измерениями",
				hostname,
				mcmUser.getId(),
				server.getId());
		StorableObjectPool.flush(mcm, true);
	}}
