/*
 * $Id: TestSystemUser.java,v 1.5 2005/06/28 15:28:24 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/28 15:28:24 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestSystemUser extends TestCase {

	public TestSystemUser(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestSystemUser.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
//	1
		final SystemUser loginUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.LOGINPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Login Processorovich Serverov",
				"Need for Login Processor");
		System.out.println("Login user: '" + loginUser.getLogin() + "', id: '" + loginUser.getId() + "'");

//	2
		final SystemUser eventUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.EVENTPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Event Processorovich Serverov",
				"Need for Event Processor");
		System.out.println("Event user: '" + eventUser.getLogin() + "', id: '" + eventUser.getId() + "'");

//	3
		final SystemUser mserverUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.MSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Measurement Serverov",
				"Need for Measurement Server");
		System.out.println("MServer user: '" + mserverUser.getLogin() + "', id: '" + mserverUser.getId() + "'");

//	4
		final SystemUser cmserverUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.CMSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Client Measurementovich Serverov",
				"Need for Client Measurement Server");
		System.out.println("CMServer user: '" + cmserverUser.getLogin() + "', id: '" + cmserverUser.getId() + "'");

//	5
		final SystemUser mshserverUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.MSCHARSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"m�sh-server",
				"Need for Map-Scheme Server");
		System.out.println("MSHServer user: '" + mshserverUser.getLogin() + "', id: '" + mshserverUser.getId() + "'");

//	save all
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, true);
	}

	public void testCreateMCMUsers() throws ApplicationException {
		final SystemUser mcmUser = SystemUser.createInstance(DatabaseCommonTest.getSysUser().getId(),
				SystemUserWrapper.MCM_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"mcm",
				"Need for MCM");
		System.out.println("MCM user: '" + mcmUser.getLogin() + "', id: '" + mcmUser.getId() + "'");

		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, true);
	}

	public void testSetPassword() throws ApplicationException, AMFICOMRemoteException {
		final String serverHostName = ApplicationProperties.getString(CORBACommonTest.KEY_SERVER_HOST_NAME,
				CORBACommonTest.SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);
		final LoginServer loginServerRef = (LoginServer) corbaServer.resolveReference(ServerProcessWrapper.LOGIN_PROCESS_CODENAME);

		final IdlIdentifierHolder userIdTH = new IdlIdentifierHolder();
		final IdlSessionKey sessionKeyT = loginServerRef.login(SystemUserWrapper.SYS_LOGIN, "sys", userIdTH);

//	login user
		TypicalCondition tc = new TypicalCondition(SystemUserWrapper.LOGINPROCESSOR_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);

//	event user
		TypicalCondition tc1 = new TypicalCondition(SystemUserWrapper.EVENTPROCESSOR_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);

//	Create compound condition
		final CompoundCondition cc = new CompoundCondition(tc, CompoundConditionSort.OR, tc1);

//	mserver user
		tc1 = new TypicalCondition(SystemUserWrapper.MSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	cmserver user
		tc1 = new TypicalCondition(SystemUserWrapper.CMSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	mscharserver user
		tc1 = new TypicalCondition(SystemUserWrapper.MSCHARSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	mcm user
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

//	login user
		systemUser = usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);

//	event user
		systemUser = usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);

//	mserver user
		systemUser = usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);

//	cmserver user
		systemUser = usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);

//	mscharserver user
		systemUser = usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);

//	mcm user
		systemUser = usersMap.get(SystemUserWrapper.MCM_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				systemUser.getId().getTransferable(),
				SystemUserWrapper.MCM_LOGIN);

		loginServerRef.logout(sessionKeyT);
	}
}
