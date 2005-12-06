/*-
* $Id: Creator.java,v 1.2 2005/12/06 11:35:26 bass Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.2 $, $Date: 2005/12/06 11:35:26 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Creator extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(Creator.class);
	}

	void createRolePermissions() throws ApplicationException {
		
		assert false;
		
		final Identifier userId = LoginManager.getUserId();
		
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
						Module.ELEMENTS_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_ENTER, true);
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
						Module.MAP_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_ENTER, true);
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
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.OBSERVER);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_SCHEME, true);
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
						Module.ELEMENTS_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_ENTER, true);
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
						Module.MAP_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_ENTER, true);
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
						Module.OBSERVER);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ENTER, true);				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ALARM_MANAGE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_QUICK_TASK, true);
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
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						subscriber.getId(), 
						Module.OBSERVER);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ENTER, true);				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_QUICK_TASK, true);
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
						Module.ELEMENTS_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_CREATE_AND_EDIT, true);
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE, true);
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_SAVING, true);
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
						Module.MAP_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_EDIT_BINDING, true);
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_SAVE_BINDING, true);
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_VIEW, true);
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
						Module.ELEMENTS_EDITOR);
				
				attributes.setPermissionEnable(PermissionCodename.ELEMENTS_EDITOR_ENTER, true);
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
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE, true);
				attributes.setPermissionEnable(PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.OBSERVER);
				
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_ALARM_MANAGE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_OPEN_MEASUREMENT_ARCHIVE, true);
				attributes.setPermissionEnable(PermissionCodename.OBSERVER_QUICK_TASK, true);
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
	
	}
	
	void createRoles() throws ApplicationException {
		
		assert false;
		
		final Identifier userId = LoginManager.getUserId();
		
		for(final RoleCodename roleCodename : RoleCodename.values()) {
			Role.createInstance(userId, 
				roleCodename.getCodename(), 
				roleCodename.getDescription());
		}
		
		StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
	}
	
	void createCharacteristicTypes() 
	throws ApplicationException {

		assert false;
		
		final Identifier userId = LoginManager.getUserId();	
		
		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_NAME, 
				"name", 
				"name", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				"x coordinate", 
				"x coordinate", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_Y, 
				"y coordinate", 
				"y coordinate", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_FULLNAME, 
				"Полное имя пользотеля", 
				"ФИО", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_POSITION, 
				"Должность", 
				"Должность", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_DEPARTEMENT, 
				"Подразделение", 
				"Подразделение", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_COMPANY, 
				"Организация", 
				"Организация", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_ROOM_NO, 
				"Номер комнаты", 
				"Номер комнаты", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CITY, 
				"Город", 
				"Город", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_STREET, 
				"Улица", 
				"Улица", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_BUILDING, 
				"Дом", 
				"Дом", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_EMAIL, 
				"Адрес электронной почты", 
				"Электронная почта", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_PHONE, 
				"Телефон", 
				"Телефон", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CELLULAR, 
				"Сотовый телефон", 
				"Сотовый телефон", 
				DataType.STRING, 
				IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, true);
		
	}
}

