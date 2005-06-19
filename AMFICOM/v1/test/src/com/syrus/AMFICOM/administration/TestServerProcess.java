/*
 * $Id: TestServerProcess.java,v 1.8 2005/06/19 18:43:56 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.8 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServerProcess extends DatabaseCommonTest {

	public TestServerProcess(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestServerProcess.class);
		return createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
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
		final Map usersMap = new HashMap(users.size());
		for (Iterator it = users.iterator(); it.hasNext();) {
			final SystemUser user = (SystemUser) it.next();
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}

		SystemUser user;

		//	login process
		user = (SystemUser) usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
		ServerProcess.createInstance(creatorUser.getId(),
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Login process");

//	event process
		user = (SystemUser) usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
		ServerProcess.createInstance(creatorUser.getId(),
				ServerProcessWrapper.EVENT_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Event process");

//	mserver process
		user = (SystemUser) usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
		ServerProcess.createInstance(creatorUser.getId(),
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Measurement Server");

//	cmserver process
		user = (SystemUser) usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
		ServerProcess.createInstance(creatorUser.getId(),
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Client Measurement Server");

//	mscharserver process
		user = (SystemUser) usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
		ServerProcess.createInstance(creatorUser.getId(),
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME,
				server.getId(),
				user.getId(),
				"Map/Scheme/Administration/Resource Server");

		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_CODE, true);
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
