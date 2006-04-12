/*
 * $Id: TestAddUser.java,v 1.3 2006/04/12 07:21:13 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.3 $, $Date: 2006/04/12 07:21:13 $
 * @author $Author: bass $
 * @module test
 */
public class TestAddUser extends TestCase {

	public TestAddUser(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestAddUser.class);
		return commonTest.createTestSetup();
	}

	public void testCreateRoles() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();
		final SystemUser analyst = SystemUser.createInstance(userId, 
			"analyst", 
			SystemUserSort.USER_SORT_REGULAR, 
			"Аналитик", 
			"Аналитик");
		
		final RoleCodename[] roleCodenames = new RoleCodename[] {
				RoleCodename.ANALYST,
				RoleCodename.OPERATOR,
				RoleCodename.PLANNER,
				RoleCodename.SPECIALIST,
				RoleCodename.SUBSCRIBER};

		final Set<StorableObjectCondition> typicalConditions = new HashSet<StorableObjectCondition>();
		for (final RoleCodename codename : roleCodenames) {
			typicalConditions.add(new TypicalCondition(codename.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.ROLE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));

		}
		
		Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition( 
			new CompoundCondition(typicalConditions, 
				CompoundConditionSort.OR), 
			true);
		
		for(final Role role : roles) {
			analyst.addRole(role);
		}
		
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, analyst.getId(), true);
	}
}
