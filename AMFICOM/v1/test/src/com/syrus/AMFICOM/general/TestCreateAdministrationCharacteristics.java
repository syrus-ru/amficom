/*
 * $Id: TestCreateAdministrationCharacteristics.java,v 1.1.2.1 2006/04/18 17:33:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.resource.LayoutItem;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 17:33:52 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCreateAdministrationCharacteristics extends TestCase {

	public TestCreateAdministrationCharacteristics(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestCreateAdministrationCharacteristics.class);
		return commonTest.createTestSetup();
	}

	public void testCreateRoles() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final Set<StorableObjectCondition> codenameConditions = new HashSet<StorableObjectCondition>();
		codenameConditions.add(new TypicalCondition(LayoutItem.CHARACTERISCTIC_TYPE_NAME,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(LayoutItem.CHARACTERISCTIC_TYPE_X,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(LayoutItem.CHARACTERISCTIC_TYPE_Y,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_FULLNAME,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_POSITION,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_DEPARTEMENT,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_COMPANY,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_ROOM_NO,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_CITY,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_STREET,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_BUILDING,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_EMAIL,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_PHONE,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		codenameConditions.add(new TypicalCondition(CharacteristicTypeCodenames.USER_CELLULAR,
				OPERATION_EQUALS,
				CHARACTERISTIC_TYPE_CODE,
				COLUMN_CODENAME));
		final CompoundCondition codenameCondition = new CompoundCondition(codenameConditions, OR);
		final Set<CharacteristicType> eCharacteristicTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		final Set<String> characteristicTypeCodenames = new HashSet<String>();
		for (final CharacteristicType characteristicType : eCharacteristicTypes) {
			final String codename = characteristicType.getCodename();
			System.out.println("CharacteristicType: '" + codename + "'");
			characteristicTypeCodenames.add(codename);
		}

		final Set<CharacteristicType> characteristicTypes = new HashSet<CharacteristicType>();
		if (!characteristicTypeCodenames.contains(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_NAME, 
				"name", 
				"name", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				"x coordinate", 
				"x coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				LayoutItem.CHARACTERISCTIC_TYPE_Y, 
				"y coordinate", 
				"y coordinate", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_FULLNAME)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_FULLNAME, 
				"Полное имя пользотеля", 
				"ФИО", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_POSITION)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_POSITION, 
				"Должность", 
				"Должность", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_DEPARTEMENT)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_DEPARTEMENT, 
				"Подразделение", 
				"Подразделение", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_COMPANY)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_COMPANY, 
				"Организация", 
				"Организация", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_ROOM_NO)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_ROOM_NO, 
				"Номер комнаты", 
				"Номер комнаты", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_CITY)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CITY, 
				"Город", 
				"Город", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_STREET)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_STREET, 
				"Улица", 
				"Улица", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_BUILDING)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_BUILDING, 
				"Дом", 
				"Дом", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_EMAIL)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_EMAIL, 
				"Адрес электронной почты", 
				"Электронная почта", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_PHONE)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_PHONE, 
				"Телефон", 
				"Телефон", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}
		if (!characteristicTypeCodenames.contains(CharacteristicTypeCodenames.USER_CELLULAR)) {
			characteristicTypes.add(CharacteristicType.createInstance(userId, 
				CharacteristicTypeCodenames.USER_CELLULAR, 
				"Сотовый телефон", 
				"Сотовый телефон", 
				DataType.STRING, 
				CharacteristicTypeSort.VISUAL));
		}

		StorableObjectPool.flush(characteristicTypes, userId, true);
	}
}
