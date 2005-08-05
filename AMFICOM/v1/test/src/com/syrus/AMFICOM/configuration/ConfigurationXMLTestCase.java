/*
 * $Id: ConfigurationXMLTestCase.java,v 1.1.1.1 2005/04/13 17:48:43 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.io.File;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.XMLIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/13 17:48:43 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class ConfigurationXMLTestCase extends TestCase {

	public ConfigurationXMLTestCase(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(ConfigurationXMLTestCase.class);
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				System.out.println();
				System.out.println("--setUp--");
				System.out.println();
				IdentifierPool.init(new XMLIdentifierGeneratorServer());
				GeneralStorableObjectPool.init(new XMLGeneralObjectLoader(new File("/tmp/amficom")));
				ConfigurationStorableObjectPool.init(new XMLConfigurationObjectLoader(new File("/tmp/amficom")));
				System.out.println();
				System.out.println("--setUp--");
				System.out.println();

			}

			protected void tearDown() {
				System.out.println();
				System.out.println("--tearDown--");
				System.out.println();
				try {
					// ConfigurationStorableObjectPool.flush(true);
					GeneralStorableObjectPool.flush(true);
				} catch (VersionCollisionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GeneralStorableObjectPool.serializePool();
				System.out.println();
				System.out.println("--tearDown--");
				System.out.println();

			}
		};
		return wrapper;
	}

	
	public void testLinkType() throws ApplicationException {
		System.out.println("testLinkType:");
		LinkType linkType = (LinkType) ConfigurationStorableObjectPool.getStorableObject(
			new Identifier("LinkType_1107771596853"), true);
		CharacteristicType characteristicType = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(
			new Identifier("CharacteristicType_0"), true);
		Characteristic characteristic = Characteristic.createInstance(new Identifier("User_0"),
				characteristicType,
				"testCharacteristic",
				"test",
				CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
				"t e s t",
				linkType.getId(),
				false,
				false);
		GeneralStorableObjectPool.putStorableObject(characteristic);		
		linkType.addCharacteristic(characteristic);
	}
}
