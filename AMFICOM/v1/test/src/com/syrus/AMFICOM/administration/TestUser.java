/*
 * $Id: TestUser.java,v 1.3 2005/06/02 14:31:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestUser extends CommonTest {
	public TestUser(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestUser.class);
	}

	public void testCreateInstance() throws ApplicationException {
//	Retrieve sys user
		TypicalCondition tc = new TypicalCondition(UserWrapper.SYS_LOGIN,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.USER_ENTITY_CODE,
				UserWrapper.COLUMN_LOGIN);
		Set users = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		User sysUser = (User) users.iterator().next();
		System.out.println("Sys user: '" + sysUser.getLogin() + "', id: '" + sysUser.getId() + "'");

//	1
		User loginUser = User.createInstance(sysUser.getId(),
				UserWrapper.LOGINPROCESSOR_LOGIN,
				UserSort.USER_SORT_SERVERPROCESS,
				"Login Processorovich Serverov",
				"Need for Login Processor");
		System.out.println("Login user: '" + loginUser.getLogin() + "', id: '" + loginUser.getId() + "'");

//	2
		User eventUser = User.createInstance(sysUser.getId(),
				UserWrapper.EVENTPROCESSOR_LOGIN,
				UserSort.USER_SORT_SERVERPROCESS,
				"Event Processorovich Serverov",
				"Need for Event Processor");
		System.out.println("Event user: '" + eventUser.getLogin() + "', id: '" + eventUser.getId() + "'");

//	3
		User mserverUser = User.createInstance(sysUser.getId(),
				UserWrapper.MSERVER_LOGIN,
				UserSort.USER_SORT_SERVERPROCESS,
				"Measurement Serverov",
				"Need for Measurement Server");
		System.out.println("MServer user: '" + mserverUser.getLogin() + "', id: '" + mserverUser.getId() + "'");

//	4
		User cmserverUser = User.createInstance(sysUser.getId(),
				UserWrapper.CMSERVER_LOGIN,
				UserSort.USER_SORT_SERVERPROCESS,
				"Client Measurementovich Serverov",
				"Need for Client Measurement Server");
		System.out.println("CMServer user: '" + cmserverUser.getLogin() + "', id: '" + cmserverUser.getId() + "'");

//	5
		User mshserverUser = User.createInstance(sysUser.getId(),
				UserWrapper.MSHSERVER_LOGIN,
				UserSort.USER_SORT_SERVERPROCESS,
				"mЫsh-server",
				"Need for Map-Scheme Server");
		System.out.println("MSHServer user: '" + mshserverUser.getLogin() + "', id: '" + mshserverUser.getId() + "'");

//	save all
		StorableObjectPool.flush(ObjectEntities.USER_ENTITY_CODE, true);
	}
}
