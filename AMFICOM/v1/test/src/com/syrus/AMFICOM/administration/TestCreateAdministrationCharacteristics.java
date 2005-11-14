/*
 * $Id: TestCreateAdministrationCharacteristics.java,v 1.2 2005/11/14 11:32:12 bob Exp $
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
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.resource.LayoutItem;

/**
 * @version $Revision: 1.2 $, $Date: 2005/11/14 11:32:12 $
 * @author $Author: bob $
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
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL); 
			
		}
		
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
