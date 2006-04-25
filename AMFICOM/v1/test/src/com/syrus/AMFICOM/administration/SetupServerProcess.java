/*
 * $Id: SetupServerProcess.java,v 1.2 2006/04/25 09:31:58 arseniy Exp $
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
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 09:31:58 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupServerProcess extends TestCase {

	public SetupServerProcess(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupServerProcess.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SERVER_CODE);
		final Set<Server> servers = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		assertTrue("No servers", servers.size() > 0);
		final Server server = servers.iterator().next();
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

		SystemUser serverProcessUser;

		//	login process
		serverProcessUser = usersMap.get(SystemUserWrapper.LOGINPROCESSOR_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME,
				server.getId(),
				serverProcessUser.getId(),
				I18N.getString(SetupSystemUser.KEY_LOGINSERVER_DESCRIPTION));

//	event process
		serverProcessUser = usersMap.get(SystemUserWrapper.EVENTPROCESSOR_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME,
				server.getId(),
				serverProcessUser.getId(),
				I18N.getString(SetupSystemUser.KEY_EVENTSERVER_DESCRIPTION));

//	mserver process
		serverProcessUser = usersMap.get(SystemUserWrapper.MSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME,
				server.getId(),
				serverProcessUser.getId(),
				I18N.getString(SetupSystemUser.KEY_MSERVER_DESCRIPTION));

//	cmserver process
		serverProcessUser = usersMap.get(SystemUserWrapper.CMSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME,
				server.getId(),
				serverProcessUser.getId(),
				I18N.getString(SetupSystemUser.KEY_CMSERVER_DESCRIPTION));

//	mscharserver process
		serverProcessUser = usersMap.get(SystemUserWrapper.MSCHARSERVER_LOGIN);
		ServerProcess.createInstance(userId,
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME,
				server.getId(),
				serverProcessUser.getId(),
				I18N.getString(SetupSystemUser.KEY_MSCHARSERVER_DESCRIPTION));

		StorableObjectPool.flush(ObjectEntities.SERVERPROCESS_CODE, userId, true);
	}
}
