/*
 * $Id: SetupDomain.java,v 1.1.2.2 2006/02/17 12:36:20 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/17 12:36:20 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupDomain extends TestCase {

	public SetupDomain(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupDomain.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Domain domain = Domain.createInstance(creatorId,
				Identifier.VOID_IDENTIFIER,
				"Корневой домен",
				"Первый домен в иерархии");
		StorableObjectPool.flush(domain, creatorId, true);
	}
}
