/*
 * $Id: TestCreateAdministrationCharacteristics.java,v 1.3 2005/12/14 15:55:55 bass Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LayoutItem;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/14 15:55:55 $
 * @author $Author: bass $
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
		final SystemUser sysUser = DatabaseCommonTest.getSysUser();
		Identifier userId = sysUser.getId();
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
				"������ ��� ����������", 
				"���", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_POSITION, 
				"���������", 
				"���������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
			
		}

		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_DEPARTEMENT, 
				"�������������", 
				"�������������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_COMPANY, 
				"�����������", 
				"�����������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_ROOM_NO, 
				"����� �������", 
				"����� �������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CITY, 
				"�����", 
				"�����", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_STREET, 
				"�����", 
				"�����", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_BUILDING, 
				"���", 
				"���", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_EMAIL, 
				"����� ����������� �����", 
				"����������� �����", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_PHONE, 
				"�������", 
				"�������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		{
			CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CELLULAR, 
				"������� �������", 
				"������� �������", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL); 
		}
		
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, userId, true);
	}
}
