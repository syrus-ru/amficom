/*
 * $Id: TestServerProcess.java,v 1.13.2.1 2006/02/17 12:28:06 arseniy Exp $
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
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.13.2.1 $, $Date: 2006/02/17 12:28:06 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServerProcess extends TestCase {

	public TestServerProcess(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestServerProcess.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SERVER_CODE);
		final Server server = (Server) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Server '" + server.getId() + "'");

//	login user
		final TypicalCondition tc = new TypicalCondition(SystemUserWrapper.LOGINPROCESSOR_LOGIN,
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

		final Set users = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		final Map<String, SystemUser> usersMap = new HashMap<String, SystemUser>(users.size());
		for (Iterator it = users.iterator(); it.hasNext();) {
			final SystemUser user = (SystemUser) it.next();
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}

		SystemUser user;

		//	login process
		user = usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Login process");

//	event process
		user = usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Event process");

//	mserver process
		user = usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Measurement Server");

//	cmserver process
		user = usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Client Measurement Server");

//	mscharserver process
		user = usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Map/Scheme/Administration/Resource Server");

		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_CODE, userId, true);
	}

	public void testUpdate() throws ApplicationException {
		final TypicalCondition tc = new TypicalCondition(ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SERVERPROCESS_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set set = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		final ServerProcess serverProcess = (ServerProcess) set.iterator().next();
		System.out.println("Server process: " + serverProcess.getCodename());
	}
}
