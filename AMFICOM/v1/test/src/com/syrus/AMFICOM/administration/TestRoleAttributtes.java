/*
 * $Id: TestRoleAttributtes.java,v 1.2 2005/10/20 13:14:54 bob Exp $
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
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/10/20 13:14:54 $
 * @author $Author: bob $
 * @module test
 */
public class TestRoleAttributtes extends TestCase {

	public TestRoleAttributtes(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestRoleAttributtes.class);
		return commonTest.createTestSetup();
	}

	public void testCreateRoles() throws ApplicationException {
		final SystemUser sysUser = DatabaseCommonTest.getSysUser();
		final Identifier userId = sysUser.getId();

		
		{
			// System Administator
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.SYSTEM_ADMINISTATOR.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role administrator = roles.iterator().next();
			
			final PermissionAttributes attributes = 
				PermissionAttributes.createInstance(userId, 
					Identifier.VOID_IDENTIFIER, 
					administrator.getId(), 
					Module.ADMINISTRATION);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_ENTER, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_DOMAIN, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_DOMAIN, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_DELETE_DOMAIN, true);
			
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_SERVER, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_SERVER, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_MEASUREMENT_MODULE, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_MEASUREMENT_MODULE, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_WORKSTATION, true);
			attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_WORKSTATION, true);
		}
		
		{
			// Media Monitoring Administator
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.MEDIA_MONITORING_ADMINISTATOR.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role mediaMonitoringAdministator = roles.iterator().next();
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						mediaMonitoringAdministator.getId(), 
						Module.ADMINISTRATION);
				
				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_DOMAIN, true);
				
//				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_GROUP, true);
//				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_GROUP, true);
//				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_DELETE_GROUP, true);
				
				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CREATE_USER, true);
				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_CHANGE_USER, true);
				attributes.setPermissionEnable(PermissionCodename.ADMINISTRATION_DELETE_USER, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						mediaMonitoringAdministator.getId(), 
//						Module.MODELING);
//				
//				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
//			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						mediaMonitoringAdministator.getId(), 
						Module.REPORT);
				
				attributes.setPermissionEnable(PermissionCodename.REPORT_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.REPORT_CREATE_TEMPLATE, true);
				attributes.setPermissionEnable(PermissionCodename.REPORT_SAVE_TEMPLATE, true);
			}
		}
		
		{
			// Analyst
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.ANALYST.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role analyst = roles.iterator().next();
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.SCHEME_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_ENTER, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.SCHEME);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_ENTER, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.MAPVIEW);
				
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_ENTER, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						analyst.getId(), 
//						Module.MODELING);
//				
//				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_MAP, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_SAVE_REFLECTOGRAM_MODEL, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_SET_MODELING_OPTIONS, true);
//			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.SCHEDULER);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_CREATE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_SAVE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_EDIT_TEST, true);
			}

			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.ANALYSIS);
				
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.RESEARCH);
				
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_MEASUREMENT_SETUP, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_SCHEME_BINDING, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.EVALUATION);
				
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_EDIT_ETALON, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.OBSERVE);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_SCHEME, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						analyst.getId(), 
//						Module.PREDICTION);
//				
//				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.PREDICTION_SAVE_PROGNOSTICATION_REFLECTOGRAM, true);
//			}
		}
		
		{
			// Operator
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.OPERATOR.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role operator = roles.iterator().next();
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.SCHEME_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_ENTER, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.SCHEME);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_ENTER, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.MAPVIEW);
				
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_ENTER, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						operator.getId(), 
//						Module.MODELING);
//				
//				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
//			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.SCHEDULER);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_CREATE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_SAVE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_EDIT_TEST, true);
			}

			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.ANALYSIS);
				
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.RESEARCH);
				
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.EVALUATION);
				
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.OBSERVE);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ENTER, true);				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ALARM_MANAGE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_QUICK_TASK, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						operator.getId(), 
//						Module.PREDICTION);
//				
//				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.PREDICTION_SAVE_PROGNOSTICATION_REFLECTOGRAM, true);
//			}
		}
		
		{
			// Subscriber
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.SUBSCRIBER.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role subscriber = roles.iterator().next();			

			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						subscriber.getId(), 
						Module.ANALYSIS);
				
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						subscriber.getId(), 
						Module.RESEARCH);
				
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						subscriber.getId(), 
						Module.EVALUATION);
				
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						subscriber.getId(), 
						Module.OBSERVE);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ENTER, true);				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_QUICK_TASK, true);
			}			
		}
		
		{

			// Planner
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.PLANNER.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role planner = roles.iterator().next();			

			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						planner.getId(), 
						Module.SCHEME_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_CREATE_AND_EDIT, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_CREATE_CHANGE_SAVE_TYPE, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_SAVING, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						planner.getId(), 
						Module.SCHEME);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEME_CREATE_AND_EDIT, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEME_SAVING, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						planner.getId(), 
						Module.MAPVIEW);
				
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_EDIT_BINDING, true);
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_EDIT_TOPOLOGICAL_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_SAVE_BINDING, true);
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_SAVE_TOPOLOGICAL_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MAPVIEW_SAVE_TOPOLOGICAL_VIEW, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						planner.getId(), 
//						Module.MODELING);
//				
//				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
//			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						planner.getId(), 
//						Module.OPTIMIZATION);
//				
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_OPEN_MAP, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_OPEN_SCHEME, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SET_OPTIMIZATION_OPTIONS, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_START_OPTIMIZATION, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_ABORT_OPTIMIZATION, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SAVE_OPTIMIZATION_OPTIONS, true);
//				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SAVE_OPTIMIZATION_RESULT, true);
//			}
		
		}
		
		{

			// Specialist
			final Set<Role> roles = 
				StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(RoleCodename.SPECIALIST.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.ROLE_CODE,
						StorableObjectWrapper.COLUMN_CODENAME), 
					true);
			
			final Role specialist = roles.iterator().next();
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.SCHEME_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_EDITOR_ENTER, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.SCHEME);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEME_ENTER, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						specialist.getId(), 
//						Module.MODELING);
//				
//				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_MAP, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_SAVE_REFLECTOGRAM_MODEL, true);
//				attributes.setPermissionEnable(PermissionCodename.MODELING_SET_MODELING_OPTIONS, true);
//			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.SCHEDULER);
				
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_CREATE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_SAVE_TEST, true);
				attributes.setPermissionEnable(PermissionCodename.SCHEDULER_EDIT_TEST, true);
			}

			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.ANALYSIS);
				
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.RESEARCH);
				
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.RESEARCH_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.EVALUATION);
				
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.OBSERVE);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_ALARM_MANAGE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVE_QUICK_TASK, true);
			}
			
//			{
//				final PermissionAttributes attributes = 
//					PermissionAttributes.createInstance(userId, 
//						Identifier.VOID_IDENTIFIER, 
//						specialist.getId(), 
//						Module.PREDICTION);
//				
//				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);				
//			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.REPORT);
				
				attributes.setPermissionEnable(PermissionCodename.REPORT_ENTER, true);				
				attributes.setPermissionEnable(PermissionCodename.REPORT_SAVE_TEMPLATE, true);
			}
		
		}
		
		StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, userId, true);
		StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
	
	
		
		StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
	}
}
