/*-
 * $Id: SetupSystemUserPasswords.java,v 1.1.2.1 2006/02/17 15:53:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.administration;

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
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/17 15:53:53 $
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
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.DOMAIN_CODE), true);
		assert !domains.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		final Domain domain = domains.iterator().next();

		ClientSessionEnvironment.createInstance(SessionKind.ALL, null);
		ClientSessionEnvironment.getInstance().login("sys", "sys", domain.getId());
		
		try {
			// login user
			TypicalCondition tc = new TypicalCondition(SystemUserWrapper.LOGINPROCESSOR_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			
			// event user
			TypicalCondition tc1 = new TypicalCondition(SystemUserWrapper.EVENTPROCESSOR_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			
			// Create compound condition
			final CompoundCondition cc = new CompoundCondition(tc, CompoundConditionSort.OR, tc1);
			
			// mserver user
			tc1 = new TypicalCondition(SystemUserWrapper.MSERVER_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			cc.addCondition(tc1);
			
			// cmserver user
			tc1 = new TypicalCondition(SystemUserWrapper.CMSERVER_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			cc.addCondition(tc1);
			
			// mscharserver user
			tc1 = new TypicalCondition(SystemUserWrapper.MSCHARSERVER_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			cc.addCondition(tc1);
			
			// mcm user
			tc1 = new TypicalCondition(SystemUserWrapper.MCM_LOGIN,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SYSTEMUSER_CODE,
					SystemUserWrapper.COLUMN_LOGIN);
			cc.addCondition(tc1);
			
			final Set users = StorableObjectPool.getStorableObjectsByCondition(cc, true);
			final Map<String, SystemUser> usersMap = new HashMap<String, SystemUser>(users.size());
			for (Iterator it = users.iterator(); it.hasNext();) {
				final SystemUser user = (SystemUser) it.next();
				System.out.println("User: " + user.getId() + ", " + user.getLogin());
				usersMap.put(user.getLogin(), user);
			}
			
			SystemUser systemUser;
			
			// login user
			systemUser = usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
			LoginManager.setPassword(systemUser.getId(), ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			
			// event user
			systemUser = usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
			LoginManager.setPassword(systemUser.getId(), ServerProcessWrapper.EVENT_PROCESS_CODENAME);
			
			// mserver user
			systemUser = usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
			LoginManager.setPassword(systemUser.getId(), ServerProcessWrapper.MSERVER_PROCESS_CODENAME);
			
			// cmserver user
			systemUser = usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
			LoginManager.setPassword(systemUser.getId(), ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);
			
			// mscharserver user
			systemUser = usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
			LoginManager.setPassword(systemUser.getId(), ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);
			
			// mcm user
			systemUser = usersMap.get(SystemUserWrapper.MCM_LOGIN);
			LoginManager.setPassword(systemUser.getId(), SystemUserWrapper.MCM_LOGIN);
		} finally {
			ClientSessionEnvironment.getInstance().logout();
		}
		
	}

}
