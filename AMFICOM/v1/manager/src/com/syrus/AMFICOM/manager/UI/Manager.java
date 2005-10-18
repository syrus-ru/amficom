/*-
* $Id: Manager.java,v 1.13 2005/10/18 15:10:39 bob Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Toolkit;
import java.util.Set;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.administration.Role.RoleCodename;
import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.AbstractApplication;
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
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.13 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Manager extends AbstractApplication {
	private static final String APPLICATION_NAME = "manager";

	
	public Manager() {
		super(APPLICATION_NAME);
	}
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));
		
		try {
			TypicalCondition tc = new TypicalCondition("sys", OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN);
			Set<SystemUser> systemUserWithLoginSys = StorableObjectPool.getStorableObjectsByCondition(tc, true);

			assert !systemUserWithLoginSys.isEmpty() : "There is no sys user";
			
			LoginManager.setUserId(systemUserWithLoginSys.iterator().next().getId());
			
//			createRoles();
//			createCharacteristicTypes();
//			createRolePermissions();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.startMainFrame(new ManagerMainFrame(super.aContext), 
			Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif"));
	}

	public static void main(String[] args) {
		Launcher.launchApplicationClass(Manager.class);
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						mediaMonitoringAdministator.getId(), 
						Module.MODELING);
				
				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
			}
			
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.MODELING);
				
				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_SAVE_REFLECTOGRAM_MODEL, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_SET_MODELING_OPTIONS, true);
			}
			
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						analyst.getId(), 
						Module.PREDICTION);
				
				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.PREDICTION_SAVE_PROGNOSTICATION_REFLECTOGRAM, true);
			}
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.MODELING);
				
				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
			}
			
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						operator.getId(), 
						Module.PREDICTION);
				
				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.PREDICTION_SAVE_PROGNOSTICATION_REFLECTOGRAM, true);
			}
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						planner.getId(), 
						Module.MODELING);
				
				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
			}
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						planner.getId(), 
						Module.OPTIMIZATION);
				
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SET_OPTIMIZATION_OPTIONS, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_START_OPTIMIZATION, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_ABORT_OPTIMIZATION, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SAVE_OPTIMIZATION_OPTIONS, true);
				attributes.setPermissionEnable(PermissionCodename.OPTIMIZATION_SAVE_OPTIMIZATION_RESULT, true);
			}
		
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.MODELING);
				
				attributes.setPermissionEnable(PermissionCodename.MODELING_ENTER, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_MAP, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_OPEN_SCHEME, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_SAVE_REFLECTOGRAM_MODEL, true);
				attributes.setPermissionEnable(PermissionCodename.MODELING_SET_MODELING_OPTIONS, true);
			}
			
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
			
			{
				final PermissionAttributes attributes = 
					PermissionAttributes.createInstance(userId, 
						Identifier.VOID_IDENTIFIER, 
						specialist.getId(), 
						Module.PREDICTION);
				
				attributes.setPermissionEnable(PermissionCodename.PREDICTION_ENTER, true);				
			}
			
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
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				"x coordinate", 
				"x coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_Y, 
				"y coordinate", 
				"y coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_NATURE, 
				"��� ������������ ����", 
				"���", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_FULLNAME, 
				"������ ��� ����������", 
				"���", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_POSITION, 
				"���������", 
				"���������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_DEPARTEMENT, 
				"�������������", 
				"�������������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_COMPANY, 
				"�����������", 
				"�����������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_ROOM_NO, 
				"����� �������", 
				"����� �������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CITY, 
				"�����", 
				"�����", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_STREET, 
				"�����", 
				"�����", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_BUILDING, 
				"���", 
				"���", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_EMAIL, 
				"����� ����������� �����", 
				"����������� �����", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_PHONE, 
				"�������", 
				"�������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CELLULAR, 
				"������� �������", 
				"������� �������", 
				DataType.STRING, 
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
		}
		
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, true);
		
	}
}
