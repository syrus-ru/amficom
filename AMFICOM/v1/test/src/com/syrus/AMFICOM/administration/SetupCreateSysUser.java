/*
 * $Id: SetupCreateSysUser.java,v 1.1.2.1 2006/02/17 11:37:51 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.SYS_LOGIN;

import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.security.ShadowDatabase;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/17 11:37:51 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupCreateSysUser extends TestCase {
	private static final String SYSADMIN_NAME = SYS_LOGIN;
	private static final String SYSADMIN_DESCRIPTION = "System administrator";
	private static final String SYS_PASSWORD = SYS_LOGIN;

	public SetupCreateSysUser(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(SetupCreateSysUser.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final SystemUser systemUser = SystemUser.createSysAdminInstance(SYS_LOGIN, SYSADMIN_NAME, SYSADMIN_DESCRIPTION);

		final SystemUserDatabase systemUserDatabase = new SystemUserDatabase();
		systemUserDatabase.save(Collections.singleton(systemUser));

		final ShadowDatabase shadowDatabase = new ShadowDatabase();
		shadowDatabase.updateOrInsert(systemUser.getId(), SYS_PASSWORD);
	}

}
