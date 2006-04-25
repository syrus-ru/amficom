/*
 * $Id: TestRole.java,v 1.5 2006/04/25 09:28:57 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2006/04/25 09:28:57 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestRole extends TestCase {

	public TestRole(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestRole.class);
		return commonTest.createTestSetup();
	}

	public void testCreateRoles() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();
		final SystemUser sysUser = StorableObjectPool.getStorableObject(creatorId, true);

		final Set<Role> roles = new HashSet<Role>();
		for (final RoleCodename roleCodename : RoleCodename.values()) {
			final Role role = Role.createInstance(creatorId, roleCodename.stringValue(), roleCodename.getDescription());

			sysUser.addRole(role);
			roles.add(role);
		}

		StorableObjectPool.flush(roles, creatorId, true);
		StorableObjectPool.flush(sysUser, creatorId, true);
	}
}
