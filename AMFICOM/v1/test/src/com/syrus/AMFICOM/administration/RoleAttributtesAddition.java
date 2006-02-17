/*
 * $Id: RoleAttributtesAddition.java,v 1.3.2.1 2006/02/17 12:28:05 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.USER_EMAIL;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.swingui.TestRunner;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;

/**
 * 
 * Sets Administration permission to Media Monitoring Administrator role added on 14/12/2005
 * 
 * @version $Revision: 1.3.2.1 $, $Date: 2006/02/17 12:28:05 $
 * @author $Author: arseniy $
 * @module test
 */
public class RoleAttributtesAddition extends TestCase {
	private static Identifier characteristicTypeId = null;

	private static StorableObjectCondition condition = null;

	public RoleAttributtesAddition(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(RoleAttributtesAddition.class);
		return commonTest.createTestSetup();
	}

	public void testCreateDefaultDeliveryRule() throws ApplicationException {
		final Identifier systemUserId = LoginManager.getUserId();
		final SystemUser systemUser = StorableObjectPool.getStorableObject(systemUserId, true);

		for (final Severity severity : Severity.values()) {
			DeliveryAttributes.getInstance(systemUserId, severity).addSystemUser(systemUser);
		}
		StorableObjectPool.flush(DELIVERYATTRIBUTES_CODE, systemUserId, true);

		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(
						getCondition(),
						AND,
						new LinkedIdsCondition(
								systemUserId,
								CHARACTERISTIC_CODE)),
				true);

		final int size = characteristics.size();
		if (size == 0) {
			final CharacteristicType characteristicType = 
				StorableObjectPool.getStorableObject(getCharacteristicTypeId(), true);
			final Characteristic characteristic = 
				Characteristic.createInstance(systemUserId, 
					characteristicType, 
					"name", 
					"description", 
					"amficom@cbr.ru", 
					systemUser, 
					true, 
					true);
			Log.debugMessage("Created e-mail address: " + characteristic.getValue(), 
				SEVERE);
			StorableObjectPool.flush(CHARACTERISTIC_CODE, systemUserId, true);
		} else if (size == 1) {
			Log.debugMessage("Loaded e-mail address: " + characteristics.iterator().next().getValue(), SEVERE);
		} else {
			fail(size + " e-mail addresses found");
		}
	}

	public void testCreateAdditionalRoleAttributtes() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		{
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
		}
		
		{
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.PLANNER.getCodename(), 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), true);
			
			assert !roles.isEmpty();
			
			final Role planner = roles.iterator().next(); 
			final Set<PermissionAttributes> attributes = 
				StorableObjectPool.getStorableObjectsByCondition(
					new CompoundCondition(
						new TypicalCondition(Module.SCHEME,
							OperationSort.OPERATION_EQUALS,
							ObjectEntities.PERMATTR_CODE,
							PermissionAttributesWrapper.COLUMN_MODULE),
						CompoundConditionSort.AND,
						new LinkedIdsCondition(planner, 
							ObjectEntities.PERMATTR_CODE)), 
					true);
			
			assert !attributes.isEmpty();

			final PermissionAttributes permissionAttributes = attributes.iterator().next();		
			
			permissionAttributes.setPermissionEnable(PermissionAttributes.PermissionCodename.SCHEME_DELETE, 
				true);
			
		}
		
		{
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.OPERATOR.getCodename(), 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), true);
			
			assert !roles.isEmpty();
			
			final Role mediaMonitoringAdministrator = roles.iterator().next(); 
			final Set<PermissionAttributes> attributes = 
				StorableObjectPool.getStorableObjectsByCondition(
					new CompoundCondition(
						new TypicalCondition(Module.REPORT,
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
				PermissionAttributes.PermissionCodename.REPORT_CREATE_CONSOLIDATED_REPORT ,
				PermissionAttributes.PermissionCodename.REPORT_CREATE_TEMPLATE ,
				PermissionAttributes.PermissionCodename.REPORT_ENTER ,
				PermissionAttributes.PermissionCodename.REPORT_SAVE_CONSOLIDATED_REPORT ,
				PermissionAttributes.PermissionCodename.REPORT_SAVE_TEMPLATE 
			};
			
			for (final PermissionAttributes.PermissionCodename codename : codenames) {
				permissionAttributes.setPermissionEnable(codename, true);
			}
		}
		
		StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, userId, true);
		StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
	
	
		
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
	}

	private static StorableObjectCondition getCondition() {
		synchronized (RoleAttributtesAddition.class) {
			if (condition == null) {
				condition = new LinkedIdsCondition(getCharacteristicTypeId(), CHARACTERISTIC_CODE);
			}
			return condition;
		}
	}


	private static Identifier getCharacteristicTypeId() {
		synchronized (RoleAttributtesAddition.class) {
			if (characteristicTypeId == null) {
				try {
					final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(
									USER_EMAIL,
									OPERATION_EQUALS,
									CHARACTERISTIC_TYPE_CODE,
									COLUMN_CODENAME),
							true);
					assert characteristicTypes != null : NON_NULL_EXPECTED;
					final int size = characteristicTypes.size();
					assert size == 1 : size;
					characteristicTypeId = characteristicTypes.iterator().next().getId();
				} catch (final ApplicationException ae) {
					Log.debugMessage(ae, SEVERE);
					characteristicTypeId = VOID_IDENTIFIER;
		
					/*
					 * Never. But log the exception prior to issuing an
					 * eror.
					 */
					assert false;
				}
			}
			return characteristicTypeId;
		}
	}

	public static void main(final String args[]) {
		TestRunner.run(RoleAttributtesAddition.class);
	}
}
