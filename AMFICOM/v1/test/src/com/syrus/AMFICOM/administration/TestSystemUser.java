/*
 * $Id: TestSystemUser.java,v 1.3 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.administration.corba.SystemUser_TransferablePackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestSystemUser extends DatabaseCommonTest {

	public TestSystemUser(String name) {
		super(name);
	}

	public static Test suite() {
		//addTestSuite(TestSystemUser.class);
		addTest(new TestSystemUser("testSetPassword"));
		return createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
//	1
		final SystemUser loginUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.LOGINPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Login Processorovich Serverov",
				"Need for Login Processor");
		System.out.println("Login user: '" + loginUser.getLogin() + "', id: '" + loginUser.getId() + "'");

//	2
		final SystemUser eventUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.EVENTPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Event Processorovich Serverov",
				"Need for Event Processor");
		System.out.println("Event user: '" + eventUser.getLogin() + "', id: '" + eventUser.getId() + "'");

//	3
		final SystemUser mserverUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.MSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Measurement Serverov",
				"Need for Measurement Server");
		System.out.println("MServer user: '" + mserverUser.getLogin() + "', id: '" + mserverUser.getId() + "'");

//	4
		final SystemUser cmserverUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.CMSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Client Measurementovich Serverov",
				"Need for Client Measurement Server");
		System.out.println("CMServer user: '" + cmserverUser.getLogin() + "', id: '" + cmserverUser.getId() + "'");

//	5
		final SystemUser mshserverUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.MSCHARSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"mЫsh-server",
				"Need for Map-Scheme Server");
		System.out.println("MSHServer user: '" + mshserverUser.getLogin() + "', id: '" + mshserverUser.getId() + "'");

//	save all
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, true);
	}

	public void testCreateMCMUsers() throws ApplicationException {
		final SystemUser mcmUser = SystemUser.createInstance(creatorUser.getId(),
				SystemUserWrapper.MCM_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"mcm",
				"Need for MCM");
		System.out.println("MCM user: '" + mcmUser.getLogin() + "', id: '" + mcmUser.getId() + "'");

		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, true);
	}

	public void testSetPassword() throws ApplicationException, AMFICOMRemoteException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);
		final LoginServer loginServerRef = (LoginServer) corbaServer.resolveReference(ServerProcessWrapper.LOGIN_PROCESS_CODENAME);

		final IdlIdentifierHolder userIdTH = new IdlIdentifierHolder();
		final SessionKey_Transferable sessionKeyT = loginServerRef.login(SystemUserWrapper.SYS_LOGIN, "sys", userIdTH);

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
		final Map usersMap = new HashMap(users.size());
		for (Iterator it = users.iterator(); it.hasNext();) {
			final SystemUser user = (SystemUser) it.next();
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}

		SystemUser systemUser;

//	login user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);

//	event user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);

//	mserver user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);

//	cmserver user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);

//	mscharserver user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);

//	mcm user
		systemUser = (SystemUser) usersMap.get(SystemUserWrapper.MCM_LOGIN);
		loginServerRef.setPassword(sessionKeyT,
				(IdlIdentifier) systemUser.getId().getTransferable(),
				SystemUserWrapper.MCM_LOGIN);

		loginServerRef.logout(sessionKeyT);
	}
}
