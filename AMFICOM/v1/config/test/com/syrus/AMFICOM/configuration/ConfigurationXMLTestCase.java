/*
 * $Id: ConfigurationXMLTestCase.java,v 1.3 2005/02/08 10:19:46 bob Exp $
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.XMLConfigurationObjectLoader;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.XMLIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/08 10:19:46 $
 * @author $Author: bob $
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
					GeneralStorableObjectPool.flush(true);
					ConfigurationStorableObjectPool.flush(true);
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
				System.out.println();
				System.out.println("--tearDown--");
				System.out.println();

			}
		};
		return wrapper;
	}

	
	public void testLinkType() throws DatabaseException, CommunicationException, IllegalObjectEntityException {
		System.out.println("testLinkType:");
		LinkType linkType = (LinkType) ConfigurationStorableObjectPool.getStorableObject(
			new Identifier("LinkType_1107771596853"), true);
		CharacteristicType characteristicType = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(
			new Identifier("CharacteristicType_0"), true);
		Characteristic characteristic = Characteristic.createInstance(new Identifier("User_0"), characteristicType,
			"testCharacteristic", "test", CharacteristicSort._CHARACTERISTIC_SORT_LINKTYPE, "t e s t",
			linkType.getId(), false, false);
		GeneralStorableObjectPool.putStorableObject(characteristic);		
		linkType.addCharacteristic(characteristic);
	}
}
