/*-
 * $Id: SetupSystemUserPasswords.java,v 1.1.2.2 2006/02/27 16:20:51 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.ServerProcessWrapper.CMSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.EVENT_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.LOGIN_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.MSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.CMSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.EVENTPROCESSOR_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.LOGINPROCESSOR_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MCM_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MSCHARSERVER_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MSERVER_LOGIN;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/27 16:20:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class SetupSystemUserPasswords extends TestCase {

	public SetupSystemUserPasswords(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupSystemUserPasswords.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testSetPassword() throws ApplicationException {
		// login user
		TypicalCondition tc = new TypicalCondition(LOGINPROCESSOR_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		
		// event user
		TypicalCondition tc1 = new TypicalCondition(EVENTPROCESSOR_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		
		// Create compound condition
		final CompoundCondition cc = new CompoundCondition(tc, OR, tc1);
		
		// mserver user
		tc1 = new TypicalCondition(MSERVER_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		cc.addCondition(tc1);
		
		// cmserver user
		tc1 = new TypicalCondition(CMSERVER_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		cc.addCondition(tc1);
		
		// mscharserver user
		tc1 = new TypicalCondition(MSCHARSERVER_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		cc.addCondition(tc1);
		
		// mcm user
		tc1 = new TypicalCondition(MCM_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		cc.addCondition(tc1);
		
		final Set<SystemUser> users = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		final Map<String, SystemUser> usersMap = new HashMap<String, SystemUser>(users.size());
		for (final SystemUser user : users) {
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}
		
		SystemUser systemUser;
		
		// login user
		systemUser = usersMap.get(LOGINPROCESSOR_LOGIN);
		LoginManager.setPassword(systemUser.getId(), LOGIN_PROCESS_CODENAME);
		
		// event user
		systemUser = usersMap.get(EVENTPROCESSOR_LOGIN);
		LoginManager.setPassword(systemUser.getId(), EVENT_PROCESS_CODENAME);
		
		// mserver user
		systemUser = usersMap.get(MSERVER_LOGIN);
		LoginManager.setPassword(systemUser.getId(), MSERVER_PROCESS_CODENAME);
		
		// cmserver user
		systemUser = usersMap.get(CMSERVER_LOGIN);
		LoginManager.setPassword(systemUser.getId(), CMSERVER_PROCESS_CODENAME);
		
		// mscharserver user
		systemUser = usersMap.get(MSCHARSERVER_LOGIN);
		LoginManager.setPassword(systemUser.getId(), MSCHARSERVER_PROCESS_CODENAME);
		
		// mcm user
		systemUser = usersMap.get(MCM_LOGIN);
		LoginManager.setPassword(systemUser.getId(), MCM_LOGIN);
		
	}

}
