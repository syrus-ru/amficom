/*
 * $Id: SetupSystemUser.java,v 1.1.2.2 2006/02/21 15:55:45 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.CMSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.EVENTPROCESSOR_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.LOGINPROCESSOR_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MCM_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MSCHARSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort.USER_SORT_SERVERPROCESS;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/21 15:55:45 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupSystemUser extends TestCase {
	static final String KEY_LOGINSERVER_NAME = "Name.ServerProcess.LoginServer";
	static final String KEY_LOGINSERVER_DESCRIPTION = "Description.ServerProcess.LoginServer";
	static final String KEY_EVENTSERVER_NAME = "Name.ServerProcess.EventServer";
	static final String KEY_EVENTSERVER_DESCRIPTION = "Description.ServerProcess.EventServer";
	static final String KEY_MSERVER_NAME = "Name.ServerProcess.MServer";
	static final String KEY_MSERVER_DESCRIPTION = "Description.ServerProcess.MServer";
	static final String KEY_CMSERVER_NAME = "Name.ServerProcess.CMServer";
	static final String KEY_CMSERVER_DESCRIPTION = "Description.ServerProcess.CMServer";
	static final String KEY_MSCHARSERVER_NAME = "Name.ServerProcess.MSchARServer";
	static final String KEY_MSCHARSERVER_DESCRIPTION = "Description.ServerProcess.MSchARServer";
	static final String KEY_MCM_NAME = "Name.MCM.MCM";
	static final String KEY_MCM_DESCRIPTION = "Description.MCM.MCM";

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

//	1
		final SystemUser loginUser = SystemUser.createInstance(userId,
				LOGINPROCESSOR_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_LOGINSERVER_NAME),
				I18N.getString(KEY_LOGINSERVER_DESCRIPTION));
		System.out.println("Login user: '" + loginUser.getLogin() + "', id: '" + loginUser.getId() + "'");

//	2
		final SystemUser eventUser = SystemUser.createInstance(userId,
				EVENTPROCESSOR_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_EVENTSERVER_NAME),
				I18N.getString(KEY_EVENTSERVER_DESCRIPTION));
		System.out.println("Event user: '" + eventUser.getLogin() + "', id: '" + eventUser.getId() + "'");

//	3
		final SystemUser mserverUser = SystemUser.createInstance(userId,
				MSERVER_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_MSERVER_NAME),
				I18N.getString(KEY_MSERVER_DESCRIPTION));
		System.out.println("MServer user: '" + mserverUser.getLogin() + "', id: '" + mserverUser.getId() + "'");

//	4
		final SystemUser cmserverUser = SystemUser.createInstance(userId,
				CMSERVER_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_CMSERVER_NAME),
				I18N.getString(KEY_CMSERVER_DESCRIPTION));
		System.out.println("CMServer user: '" + cmserverUser.getLogin() + "', id: '" + cmserverUser.getId() + "'");

//	5
		final SystemUser mscharserverUser = SystemUser.createInstance(userId,
				MSCHARSERVER_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_MSCHARSERVER_NAME),
				I18N.getString(KEY_MSCHARSERVER_DESCRIPTION));
		System.out.println("MSHServer user: '" + mscharserverUser.getLogin() + "', id: '" + mscharserverUser.getId() + "'");

// MCM
		final SystemUser mcmUser = SystemUser.createInstance(userId,
				MCM_LOGIN,
				USER_SORT_SERVERPROCESS,
				I18N.getString(KEY_MCM_NAME),
				I18N.getString(KEY_MCM_DESCRIPTION));
		System.out.println("MCM user: '" + mcmUser.getLogin() + "', id: '" + mcmUser.getId() + "'");

//	save all
		StorableObjectPool.flush(SYSTEMUSER_CODE, userId, true);
	}
}
