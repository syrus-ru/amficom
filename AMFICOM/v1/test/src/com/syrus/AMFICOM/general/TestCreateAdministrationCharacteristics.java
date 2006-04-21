/*
 * $Id: TestCreateAdministrationCharacteristics.java,v 1.3 2006/04/21 13:07:35 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.resource.LayoutItem;

/**
 * @version $Revision: 1.3 $, $Date: 2006/04/21 13:07:35 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCreateAdministrationCharacteristics extends TestCase {

	public TestCreateAdministrationCharacteristics(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestCreateAdministrationCharacteristics.class);
		return commonTest.createTestSetup();
	}

	public void testCreateRoles() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();
		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_NAME, 
				"name", 
				"name", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				"x coordinate", 
				"x coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_Y, 
				"y coordinate", 
				"y coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_FULLNAME, 
				"Полное имя пользотеля", 
				"ФИО", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_POSITION, 
				"Должность", 
				"Должность", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_DEPARTEMENT, 
				"Подразделение", 
				"Подразделение", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_COMPANY, 
				"Организация", 
				"Организация", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_ROOM_NO, 
				"Номер комнаты", 
				"Номер комнаты", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CITY, 
				"Город", 
				"Город", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_STREET, 
				"Улица", 
				"Улица", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_BUILDING, 
				"Дом", 
				"Дом", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_EMAIL, 
				"Адрес электронной почты", 
				"Электронная почта", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_PHONE, 
				"Телефон", 
				"Телефон", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CELLULAR, 
				"Сотовый телефон", 
				"Сотовый телефон", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, true);
	}
}
