/*
 * $Id: CharacteristicTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module tools
 */
public class CharacteristicTestCase extends ConfigureTestCase {

	public CharacteristicTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = CharacteristicTestCase.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(CharacteristicTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {
		
		//CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		//List list = characteristicDatabase.retrieveAll();
		
		CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		List chTypeList = characteristicTypeDatabase.retrieveAll();
		
		if (chTypeList.isEmpty())
			fail("must be at less one characteristic type at db");
		
		EquipmentDatabase equipmentDatabase = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
		List eqList = equipmentDatabase.retrieveAll();

		if (eqList.isEmpty())
			fail("must be at less one equipment at db");

		Equipment equipment = (Equipment) eqList.get(0);
		
		CharacteristicType characteristicType = (CharacteristicType)chTypeList.get(0);
		
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
		Characteristic chType = Characteristic.createInstance(id, ConfigureTestCase.creatorId,
															  characteristicType,
															  "CharacteristicTestCase",															  
															"characteristic created by CharacteristicTestCase",
															CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT,
															"testValue",
															equipment.getId());
		Characteristic chType2 = Characteristic.getInstance((Characteristic_Transferable) chType.getTransferable());

		assertEquals(chType.getId(), chType2.getId());

		Characteristic chType3 = new Characteristic(chType2.getId());

		assertEquals(chType2.getId(), chType3.getId());

		//if (list.size() > 3)
		//	characteristicTypeDatabase.delete(chType);

	}
	
	public void testRetrieveCharacteristics() throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException{
		
		EquipmentDatabase equipmentDatabase = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
		List eqList = equipmentDatabase.retrieveAll();

		if (eqList.isEmpty())
			fail("must be at less one equipment at db");

		Equipment equipment = (Equipment) eqList.get(0);
		
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		List charactericstics = characteristicDatabase.retrieveCharacteristics(equipment.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT);
		
		for(Iterator it=charactericstics.iterator();it.hasNext();){
			Characteristic ch = (Characteristic)it.next();
			Characteristic ch2 = new Characteristic(ch.getId());
			assertEquals(ch.getId(), ch2.getId());
		}
			
		
	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
//		CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
//		List list = characteristicTypeDatabase.retrieveAll();
//		for (Iterator it = list.iterator(); it.hasNext();) {
//			CharacteristicType chType = (CharacteristicType) it.next();
//			CharacteristicType chType2 = new CharacteristicType(chType.getId());
//			assertEquals(chType.getId(), chType2.getId());
//		}
	}

}

