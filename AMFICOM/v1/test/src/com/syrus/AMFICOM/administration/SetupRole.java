/*
 * $Id: SetupRole.java,v 1.1.2.1 2006/06/08 15:07:32 arseniy Exp $
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
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/08 15:07:32 $
 * @author $Author: arseniy $
 * @module test
 */
public class SetupRole extends TestCase {

	public SetupRole(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(SetupRole.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();
		final SystemUser sysUser = StorableObjectPool.getStorableObject(creatorId, true);

		final Set<Role> roles = new HashSet<Role>();
		for (final RoleCodename roleCodename : RoleCodename.values()) {
			final Role role = Role.createInstance(creatorId, roleCodename.stringValue(), roleCodename.getDescription());

			sysUser.addRole(role);
			roles.add(role);
		}

		StorableObjectPool.flush(roles, creatorId, true);
	}
}
