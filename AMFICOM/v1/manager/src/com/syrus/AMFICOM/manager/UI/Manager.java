/*-
* $Id: Manager.java,v 1.11 2005/10/11 15:36:25 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Toolkit;
import java.util.Set;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
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
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.11 $, $Date: 2005/10/11 15:36:25 $
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
			
			createCharacteristicTypes();
			
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
	

	void createCharacteristicTypes() 
	throws ApplicationException {
		


		final Identifier userId = LoginManager.getUserId();
		if (false) {
			for(final RoleCodename roleCodename : RoleCodename.values()) {
				Role.createInstance(userId, 
					roleCodename.getCodename(), 
					roleCodename.getDescription());
			}
			
			StorableObjectPool.flush(ObjectEntities.ROLE_CODE, userId, true);
		}
		
		if (false){
			
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
					"Тип должностного лица", 
					"Тип", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_FULLNAME, 
					"Полное имя пользотеля", 
					"ФИО", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_POSITION, 
					"Должность", 
					"Должность", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
	
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_DEPARTEMENT, 
					"Подразделение", 
					"Подразделение", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_COMPANY, 
					"Организация", 
					"Организация", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_ROOM_NO, 
					"Номер комнаты", 
					"Номер комнаты", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_CITY, 
					"Город", 
					"Город", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_STREET, 
					"Улица", 
					"Улица", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_BUILDING, 
					"Дом", 
					"Дом", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_EMAIL, 
					"Адрес электронной почты", 
					"Электронная почта", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_PHONE, 
					"Телефон", 
					"Телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(userId, 
					CharacteristicTypeCodenames.USER_CELLULAR, 
					"Сотовый телефон", 
					"Сотовый телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, true);
		}
		
	}
}
