/*
 * $Id: GeneralXMLTestCase.java,v 1.1 2005/01/24 15:23:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.general;

import java.io.File;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 15:23:27 $
 * @author $Author: bob $
 * @module tools
 */
public class GeneralXMLTestCase extends TestCase {

	public GeneralXMLTestCase(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(GeneralXMLTestCase.class);
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				IdentifierPool.init(new XMLIdentifierGeneratorServer());
				GeneralStorableObjectPool.init(new XMLGeneralObjectLoader(new File("/tmp/amficom")));
			}

			protected void tearDown() {
				try {
					GeneralStorableObjectPool.flush(true);
				} catch (VersionCollisionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GeneralStorableObjectPool.serializePool();

			}
		};
		return wrapper;
	}

	public void testAddCharacteristicType() throws CreateObjectException, IllegalObjectEntityException {
		CharacteristicType chType = CharacteristicType.createInstance(new Identifier("User_1"),
			"CharacteristicTypeTestCase", "characteristicType created by CharacteristicTypeTestCase",
			DataType._DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL);
		
		GeneralStorableObjectPool.putStorableObject(chType);
		
	}

	public void testRetrieveCharacteristicType() throws DatabaseException, CommunicationException {
		CharacteristicType storableObject = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(new Identifier("CharacteristicType_0"), true);
		System.out.println(storableObject.getId().getIdentifierString());
		System.out.println(storableObject.getCodename());
	}
}
