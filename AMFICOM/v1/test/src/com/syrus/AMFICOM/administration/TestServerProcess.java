/*
 * $Id: TestServerProcess.java,v 1.4 2005/06/08 09:43:17 arseniy Exp $
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
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/08 09:43:17 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServerProcess extends CommonTest {

	public TestServerProcess(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestServerProcess.class);
	}

	public void _testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SERVER_ENTITY_CODE);
		Server server = (Server) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Server '" + server.getId() + "'");

//	sys user
		TypicalCondition tc = new TypicalCondition(UserWrapper.SYS_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);

//	login user
		TypicalCondition tc1 = new TypicalCondition(UserWrapper.LOGINPROCESSOR_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);

//	Create compound condition
		CompoundCondition cc = new CompoundCondition(tc, CompoundConditionSort.OR, tc1);

//	event user
		tc1 = new TypicalCondition(UserWrapper.EVENTPROCESSOR_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	mserver user
		tc1 = new TypicalCondition(UserWrapper.MSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	cmserver user
		tc1 = new TypicalCondition(UserWrapper.CMSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

//	mshserver user
		tc1 = new TypicalCondition(UserWrapper.MSCHARSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);
		cc.addCondition(tc1);

		Set users = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		Map usersMap = new HashMap(users.size());
		for (Iterator it = users.iterator(); it.hasNext();) {
			final User user = (User) it.next();
			System.out.println("User: " + user.getId() + ", " + user.getLogin());
			usersMap.put(user.getLogin(), user);
		}

		User sysUser = (User) usersMap.get(UserWrapper.SYS_LOGIN);
		User user;

		//	login process
		user = (User) usersMap.get(UserWrapper.LOGINPROCESSOR_LOGIN);
		ServerProcess.createInstance(sysUser.getId(), ServerProcessWrapper.LOGIN_PROCESS_CODENAME, server.getId(), user.getId(), "Login process");

//	event process
		user = (User) usersMap.get(UserWrapper.EVENTPROCESSOR_LOGIN);
		ServerProcess.createInstance(sysUser.getId(), ServerProcessWrapper.EVENT_PROCESS_CODENAME, server.getId(), user.getId(), "Event process");

//	mserver process
		user = (User) usersMap.get(UserWrapper.MSERVER_LOGIN);
		ServerProcess.createInstance(sysUser.getId(), ServerProcessWrapper.MSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Measurement Server");

//	cmserver process
		user = (User) usersMap.get(UserWrapper.CMSERVER_LOGIN);
		ServerProcess.createInstance(sysUser.getId(), ServerProcessWrapper.CMSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Client Measurement Server");

//	mscharserver process
		user = (User) usersMap.get(UserWrapper.MSCHARSERVER_LOGIN);
		ServerProcess.createInstance(sysUser.getId(), ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Map/Scheme/Administration/Resource Server");

		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_ENTITY_CODE, true);
	}

	public void testUpdate() throws ApplicationException {
		final TypicalCondition tc = new TypicalCondition("MSHServer",
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SERVERPROCESS_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set set = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		final ServerProcess serverProcess = (ServerProcess) set.iterator().next();
		System.out.println("Server process: " + serverProcess.getCodename());

		serverProcess.setCodename(ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);
		serverProcess.setDescription("Map/Scheme/Administration/Resource Server");
		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_ENTITY_CODE, false);
	}
}
