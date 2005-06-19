/*
 * $Id: TestDomain.java,v 1.2 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestDomain extends DatabaseCommonTest {

	public TestDomain(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestDomain.class);
		return createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Domain domain = Domain.createInstance(creatorUser.getId(),
				Identifier.VOID_IDENTIFIER,
				"Корневой домен",
				"Первый домен в иерархии");
		StorableObjectPool.flush(domain, true);
	}
}
