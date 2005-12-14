/*
 * $Id: RoleAttributtesAddition.java,v 1.1 2005/12/14 15:49:46 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * 
 * Sets Administration permission to Media Monitoring Administrator role added on 14/12/2005
 * 
 * @version $Revision: 1.1 $, $Date: 2005/12/14 15:49:46 $
 * @author $Author: bob $
 * @module test
 */
public class RoleAttributtesAddition extends TestCase {

	public RoleAttributtesAddition(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(RoleAttributtesAddition.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAdditionalRoleAttributtes() throws ApplicationException {
		final SystemUser sysUser = DatabaseCommonTest.getSysUser();
		final Identifier userId = sysUser.getId();

		
		final Set<Role> roles = 
			StorableObjectPool.getStorableObjectsByCondition(
				new TypicalCondition(RoleCodename.MEDIA_MONITORING_ADMINISTATOR.getCodename(), 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.ROLE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME), true);
		
		assert !roles.isEmpty();
		
		final Role mediaMonitoringAdministrator = roles.iterator().next(); 
		final Set<PermissionAttributes> attributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(
					new TypicalCondition(Module.ADMINISTRATION,
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.PERMATTR_CODE,
						PermissionAttributesWrapper.COLUMN_MODULE),
					CompoundConditionSort.AND,
					new LinkedIdsCondition(mediaMonitoringAdministrator, 
						ObjectEntities.PERMATTR_CODE)), 
				true);
		
		assert !attributes.isEmpty();

		final PermissionAttributes permissionAttributes = attributes.iterator().next();		
		
		final PermissionAttributes.PermissionCodename[] codenames = new PermissionAttributes.PermissionCodename[]{		
			PermissionAttributes.PermissionCodename.ADMINISTRATION_ENTER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_DOMAIN,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_USER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_MEASUREMENT_MODULE,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_SERVER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_RTU,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_WORKSTATION,
			
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_DOMAIN,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_USER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_MEASUREMENT_MODULE,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_SERVER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_RTU,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_WORKSTATION,
	
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_DOMAIN,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_USER,		
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_MEASUREMENT_MODULE,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_SERVER,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_RTU,
			PermissionAttributes.PermissionCodename.ADMINISTRATION_DELETE_WORKSTATION,
		};
		
		for (final PermissionAttributes.PermissionCodename codename : codenames) {
			permissionAttributes.setPermissionEnable(codename, true);
		}
		
		
		StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, userId, true);
		StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
	
	
		
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
	}
}
