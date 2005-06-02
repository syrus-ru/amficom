/*
 * $Id: TestServerProcess.java,v 1.3 2005/06/02 14:31:02 arseniy Exp $
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
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestServerProcess extends CommonTest {
	public static final String LOGIN_PROCESS_CODENAME = "LoginServer";
	public static final String EVENT_PROCESS_CODENAME = "EventServer";
	public static final String MSERVER_PROCESS_CODENAME = "MServer";
	public static final String CMSERVER_PROCESS_CODENAME = "CMServer";
	public static final String MSHSERVER_PROCESS_CODENAME = "MSHServer";

	public TestServerProcess(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestServerProcess.class);
	}

	public void testCreateInstance() throws ApplicationException {
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
		tc1 = new TypicalCondition(UserWrapper.MSHSERVER_LOGIN,
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
		ServerProcess serverProcess;

//	login process
		user = (User) usersMap.get(UserWrapper.LOGINPROCESSOR_LOGIN);
		serverProcess = ServerProcess.createInstance(sysUser.getId(), LOGIN_PROCESS_CODENAME, server.getId(), user.getId(), "Login process");

//	event process
		user = (User) usersMap.get(UserWrapper.EVENTPROCESSOR_LOGIN);
		serverProcess = ServerProcess.createInstance(sysUser.getId(), EVENT_PROCESS_CODENAME, server.getId(), user.getId(), "Event process");

//	mserver process
		user = (User) usersMap.get(UserWrapper.MSERVER_LOGIN);
		serverProcess = ServerProcess.createInstance(sysUser.getId(), MSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Measurement Server");

//	cmserver process
		user = (User) usersMap.get(UserWrapper.CMSERVER_LOGIN);
		serverProcess = ServerProcess.createInstance(sysUser.getId(), CMSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Client Measurement Server");

//	mshserver process
		user = (User) usersMap.get(UserWrapper.MSHSERVER_LOGIN);
		serverProcess = ServerProcess.createInstance(sysUser.getId(), MSHSERVER_PROCESS_CODENAME, server.getId(), user.getId(), "Map/Scheme Server");

		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_ENTITY_CODE, true);
	}

}
