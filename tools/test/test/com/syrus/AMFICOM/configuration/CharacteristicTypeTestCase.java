/*
 * $Id: CharacteristicTypeTestCase.java,v 1.2 2004/08/31 15:29:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/31 15:29:12 $
 * @author $Author: bob $
 * @module tools
 */
public class CharacteristicTypeTestCase extends ConfigureTestCase {

	public CharacteristicTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = CharacteristicTypeTestCase.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(CharacteristicTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		List list = characteristicTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
		CharacteristicType chType = CharacteristicType.createInstance(id, ConfigureTestCase.creatorId, "CharacteristicTypeTestCase",
															"characteristicType created by CharacteristicTypeTestCase",
															DataType._DATA_TYPE_STRING, false,false);
		CharacteristicType chType2 = CharacteristicType.getInstance((CharacteristicType_Transferable) chType.getTransferable());

		assertEquals(chType.getId(), chType2.getId());

		CharacteristicType chType3 = new CharacteristicType(chType2.getId());

		assertEquals(chType2.getId(), chType3.getId());

		if (list.size() > 3)
			characteristicTypeDatabase.delete(chType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		List list = characteristicTypeDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			CharacteristicType chType = (CharacteristicType) it.next();
			CharacteristicType chType2 = new CharacteristicType(chType.getId());
			assertEquals(chType.getId(), chType2.getId());
		}
	}

}