/*
 * $Id: TestRole.java,v 1.3 2006/02/17 12:04:55 arseniy Exp $
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
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2006/02/17 12:04:55 $
 * @author $Author: arseniy $
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

	public void testCreateRoles() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();
		final SystemUser sysUser = StorableObjectPool.getStorableObject(userId, true);
		for(final RoleCodename roleCodename : RoleCodename.values()) {
			sysUser.addRole(Role.createInstance(userId, 
				roleCodename.getCodename(), 
				roleCodename.getDescription()));
		}
		
		StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
	}
}
