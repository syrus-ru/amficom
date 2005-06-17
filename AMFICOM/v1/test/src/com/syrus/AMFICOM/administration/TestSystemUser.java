/*
 * $Id: TestSystemUser.java,v 1.1 2005/06/17 17:14:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.administration.corba.SystemUser_TransferablePackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/17 17:14:10 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestSystemUser extends CommonTest {
	public TestSystemUser(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestSystemUser.class);
	}

	public void testCreateSysUser() throws ApplicationException {
		final Identifier sysUserId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
		SystemUser sysUser = new SystemUser(sysUserId,
				sysUserId,
				0,
				SystemUserWrapper.SYS_LOGIN,
				SystemUserSort._USER_SORT_SYSADMIN,
				"sys",
				"System administrator");
		SystemUserDatabase database = (SystemUserDatabase) DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
		database.insert(sysUser);
	}

	public void _testCreateInstance() throws ApplicationException {
//	Retrieve sys user
		TypicalCondition tc = new TypicalCondition(SystemUserWrapper.SYS_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		Set users = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		SystemUser sysUser = (SystemUser) users.iterator().next();
		System.out.println("Sys user: '" + sysUser.getLogin() + "', id: '" + sysUser.getId() + "'");

//	1
		SystemUser loginUser = SystemUser.createInstance(sysUser.getId(),
				SystemUserWrapper.LOGINPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Login Processorovich Serverov",
				"Need for Login Processor");
		System.out.println("Login user: '" + loginUser.getLogin() + "', id: '" + loginUser.getId() + "'");

//	2
		SystemUser eventUser = SystemUser.createInstance(sysUser.getId(),
				SystemUserWrapper.EVENTPROCESSOR_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Event Processorovich Serverov",
				"Need for Event Processor");
		System.out.println("Event user: '" + eventUser.getLogin() + "', id: '" + eventUser.getId() + "'");

//	3
		SystemUser mserverUser = SystemUser.createInstance(sysUser.getId(),
				SystemUserWrapper.MSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Measurement Serverov",
				"Need for Measurement Server");
		System.out.println("MServer user: '" + mserverUser.getLogin() + "', id: '" + mserverUser.getId() + "'");

//	4
		SystemUser cmserverUser = SystemUser.createInstance(sysUser.getId(),
				SystemUserWrapper.CMSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"Client Measurementovich Serverov",
				"Need for Client Measurement Server");
		System.out.println("CMServer user: '" + cmserverUser.getLogin() + "', id: '" + cmserverUser.getId() + "'");

//	5
		SystemUser mshserverUser = SystemUser.createInstance(sysUser.getId(),
				SystemUserWrapper.MSCHARSERVER_LOGIN,
				SystemUserSort.USER_SORT_SERVERPROCESS,
				"mЫsh-server",
				"Need for Map-Scheme Server");
		System.out.println("MSHServer user: '" + mshserverUser.getLogin() + "', id: '" + mshserverUser.getId() + "'");

//	save all
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, true);
	}

	public void _testUpdate() throws ApplicationException {
		final TypicalCondition tc = new TypicalCondition(SystemUserWrapper.MSCHARSERVER_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SYSTEMUSER_CODE,
				SystemUserWrapper.COLUMN_LOGIN);
		final Set set = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		final SystemUser user = (SystemUser) set.iterator().next();
		System.out.println("User: " + user.getLogin() + ", '" + user.getId() + "'");
	}
}
