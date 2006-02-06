/*
 * $Id: TestDomain.java,v 1.7 2005/12/15 14:16:36 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.7 $, $Date: 2005/12/15 14:16:36 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestDomain extends TestCase {

	public TestDomain(final String name) {
		super(name);
	}

	public static Test suite() {
//		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
//		databaseCommonTest.addTestSuite(TestDomain.class);
//		return databaseCommonTest.createTestSetup();
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTest(new TestDomain("testRetrieve"));
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Domain domain = Domain.createInstance(DatabaseCommonTest.getSysUser().getId(),
				Identifier.VOID_IDENTIFIER,
				"Корневой домен",
				"Первый домен в иерархии");
		StorableObjectPool.flush(domain, DatabaseCommonTest.getSysUser().getId(), true);
	}

	public void testRetrieve() throws ApplicationException {
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(DOMAIN_CODE), true);
		System.out.println("Domains: " + domains);

		Domain.createInstance(LoginManager.getUserId(), domains.iterator().next().getId(), "test", "test");
		Domain.createInstance(LoginManager.getUserId(), domains.iterator().next().getId(), "test", "test");
		Domain.createInstance(LoginManager.getUserId(), domains.iterator().next().getId(), "test", "test");

		final LinkedIdsCondition condition = new LinkedIdsCondition(domains, DOMAIN_CODE);
		final Set<Domain> condDomains = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		System.out.println("Condition domains: " + condDomains);
	}
}
