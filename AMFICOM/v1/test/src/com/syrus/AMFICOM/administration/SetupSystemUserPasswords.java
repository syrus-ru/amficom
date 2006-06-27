/*-
 * $Id: SetupSystemUserPasswords.java,v 1.2.2.1 2006/06/27 17:28:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.MCM_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.SYSTEMSERVER_LOGIN;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.2.2.1 $, $Date: 2006/06/27 17:28:17 $
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
		// system server user
		TypicalCondition tc = new TypicalCondition(SYSTEMSERVER_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);

		// mcm user
		TypicalCondition tc1 = new TypicalCondition(MCM_LOGIN,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);

		// Create compound condition
		final CompoundCondition cc = new CompoundCondition(tc, OR, tc1);

		final Set<SystemUser> users = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		final Map<String, SystemUser> usersMap = new HashMap<String, SystemUser>(users.size());
		for (final SystemUser user : users) {
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}

		SystemUser systemUser;

		// system server user
		systemUser = usersMap.get(SYSTEMSERVER_LOGIN);
		LoginManager.setPassword(systemUser.getId(), SYSTEMSERVER_LOGIN);
		
		// mcm user
		systemUser = usersMap.get(MCM_LOGIN);
		LoginManager.setPassword(systemUser.getId(), MCM_LOGIN);
		
	}

}
