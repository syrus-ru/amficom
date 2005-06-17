/*
 * $Id: TestDomain.java,v 1.1 2005/06/17 20:17:16 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/17 20:17:16 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestDomain extends CommonTest {

	public TestDomain(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestDomain.class);
	}

	public void testCreateInstance() throws ApplicationException {
		final Domain domain = Domain.createInstance(creatorUser.getId(),
				Identifier.VOID_IDENTIFIER,
				"Корневой домен",
				"Первый домен в иерархии");
		StorableObjectPool.flush(domain, true);
	}
}
