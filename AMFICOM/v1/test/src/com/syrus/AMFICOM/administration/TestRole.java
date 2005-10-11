/*
 * $Id: TestRole.java,v 1.1 2005/10/11 12:51:55 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/11 12:51:55 $
 * @author $Author: bob $
 * @module test
 */
public class TestRole extends TestCase {

	public TestRole(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestRole.class);
		return commonTest.createTestSetup();
	}

	public void testAttachToMonitoredElement() throws ApplicationException {
		final SystemUser sysUser = DatabaseCommonTest.getSysUser();
		final Identifier userId = sysUser.getId();
		for(final RoleCodename roleCodename : RoleCodename.values()) {
			sysUser.addRole(Role.createInstance(userId, 
				roleCodename.getCodename(), 
				roleCodename.getDescription()));
		}
		
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
	}
}
