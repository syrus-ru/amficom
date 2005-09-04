/*-
* $Id: Manager.java,v 1.7 2005/09/04 11:28:17 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Toolkit;
import java.util.Set;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.7 $, $Date: 2005/09/04 11:28:17 $
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
		super.init();
		super.aContext.setApplicationModel(new ManagerModel(super.aContext));
		
		try {
			TypicalCondition tc = new TypicalCondition("sys", OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN);
			Set<SystemUser> systemUserWithLoginSys = StorableObjectPool.getStorableObjectsByCondition(tc, true);

			assert !systemUserWithLoginSys.isEmpty() : "There is no sys user";
			
			LoginManager.setUserId(systemUserWithLoginSys.iterator().next().getId());
			
//			createCharacteristicTypes();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.startMainFrame(new ManagerMainFrame(super.aContext), 
			Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif"));
	}

	public static void main(String[] args) {
		new Manager();
	}
	

	void createCharacteristicTypes() 
	throws ApplicationException {
		


		
		if (false){
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					LayoutItem.CHARACTERISCTIC_TYPE_X, 
					"x coordinate", 
					"x coordinate", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}

			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					LayoutItem.CHARACTERISCTIC_TYPE_Y, 
					"y coordinate", 
					"y coordinate", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_NATURE, 
					"Тип должностного лица", 
					"Тип", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_FULLNAME, 
					"Полное имя пользотеля", 
					"ФИО", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_POSITION, 
					"Должность", 
					"Должность", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
				
			}
	
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_DEPARTEMENT, 
					"Подразделение", 
					"Подразделение", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_COMPANY, 
					"Организация", 
					"Организация", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_ROOM_NO, 
					"Номер комнаты", 
					"Номер комнаты", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_CITY, 
					"Город", 
					"Город", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_STREET, 
					"Улица", 
					"Улица", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_BUILDING, 
					"Дом", 
					"Дом", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_EMAIL, 
					"Адрес электронной почты", 
					"Электронная почта", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_PHONE, 
					"Телефон", 
					"Телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
			
			{
				CharacteristicType.createInstance(LoginManager.getUserId(), 
					CharacteristicTypeCodenames.USER_CELLULAR, 
					"Сотовый телефон", 
					"Сотовый телефон", 
					DataType.STRING, 
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			}
		}
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, LoginManager.getUserId(), true);
	}
}
