/*
 * $Id: TestDomain.java,v 1.5 2005/08/18 10:53:04 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/18 10:53:04 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestDomain extends TestCase {

	public TestDomain(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestDomain.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Domain domain = Domain.createInstance(DatabaseCommonTest.getSysUser().getId(),
				Identifier.VOID_IDENTIFIER,
				"Корневой домен",
				"Первый домен в иерархии");
		StorableObjectPool.flush(domain, DatabaseCommonTest.getSysUser().getId(), true);
	}
}
