/*
 * $Id: SetupSystemUser.java,v 1.2.2.1 2006/06/27 17:28:17 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.MCM_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.SYSTEMSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort.USER_SORT_MCM;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort.USER_SORT_SERVER;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.2.2.1 $, $Date: 2006/06/27 17:28:17 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupSystemUser extends TestCase {
	static final String KEY_SYSTEMSERVER_NAME = "Name.SystemUser.Server";
	static final String KEY_SYSTEMSERVER_DESCRIPTION = "Description.SystemUser.Server";
	static final String KEY_MCM_NAME = "Name.SystemUser.MCM";
	static final String KEY_MCM_DESCRIPTION = "Description.SystemUser.MCM";

	public SetupSystemUser(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupSystemUser.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final Set<SystemUser> systemUsers = new HashSet<SystemUser>();

//	System server
		systemUsers.add(SystemUser.createInstance(userId,
				SYSTEMSERVER_LOGIN,
				USER_SORT_SERVER,
				I18N.getString(KEY_SYSTEMSERVER_NAME),
				I18N.getString(KEY_SYSTEMSERVER_DESCRIPTION)));

// MCM
		systemUsers.add(SystemUser.createInstance(userId,
				MCM_LOGIN,
				USER_SORT_MCM,
				I18N.getString(KEY_MCM_NAME),
				I18N.getString(KEY_MCM_DESCRIPTION)));

//	save all
		StorableObjectPool.flush(SYSTEMUSER_CODE, userId, true);
	}
}
