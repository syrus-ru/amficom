/*
 * $Id: GeneralXMLTestCase.java,v 1.2 2005/02/04 14:21:34 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.general;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.XMLIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/04 14:21:34 $
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
				ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader());
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

	public void _testAddCharacteristicType() throws CreateObjectException, IllegalObjectEntityException {
		CharacteristicType chType = CharacteristicType.createInstance(new Identifier("User_1"),
			"CharacteristicTypeTestCase", "characteristicType created by CharacteristicTypeTestCase",
			DataType._DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL);

		GeneralStorableObjectPool.putStorableObject(chType);

	}

	public void _testRetrieveCharacteristicType() throws DatabaseException, CommunicationException {
		CharacteristicType storableObject = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(
			new Identifier("CharacteristicType_0"), true);
		System.out.println(storableObject.getId().getIdentifierString());
		System.out.println(storableObject.getCodename());
	}

	public void testRetrieveSet() throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		System.out.println("testRetrieveSet:");
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(new File("/tmp/amficom"), "measurement");
		StorableObjectXML objectXML = new StorableObjectXML(driver);
		Set set = (Set) objectXML.retrieve(new Identifier("Set_0"));
		SetParameter[] setParameters = set.getParameters();
		for (int i = 0; i < setParameters.length; i++) {
			byte[] value = setParameters[i].getValue();
			System.out.print("#" + i + "\t");
			for (int j = 0; j < value.length; j++)
				System.out.print(value[j] + (j != value.length - 1 ? ", " : ""));
			System.out.println();
		}

	}

	public void _testAddSet() throws IOException, IllegalDataException, VersionCollisionException,
			IllegalObjectEntityException, DatabaseException, CommunicationException {
		System.out.println("testAddSet:");
		Identifier creatorId = new Identifier("User_1");
		ParameterType parameterType = ParameterType
				.createInstance(creatorId, "testType", "test parameter type", "type");
		GeneralStorableObjectPool.putStorableObject(parameterType);
		GeneralStorableObjectPool.flush(true);
		SetParameter[] setParameters = new SetParameter[3];
		ByteArray byteArray = new ByteArray(1.125);
		setParameters[0] = SetParameter.createInstance(parameterType, byteArray.getBytes());
		ByteArray byteArray2 = new ByteArray((int) 3123);
		setParameters[1] = SetParameter.createInstance(parameterType, byteArray2.getBytes());
		ByteArray byteArray3 = new ByteArray(3123L);
		setParameters[2] = SetParameter.createInstance(parameterType, byteArray3.getBytes());

		for (int i = 0; i < setParameters.length; i++) {
			byte[] value = setParameters[i].getValue();
			System.out.print("#" + i + "\t");
			for (int j = 0; j < value.length; j++)
				System.out.print(value[j] + (j != value.length - 1 ? ", " : ""));
			System.out.println();
		}
		Set set = Set.createInstance(creatorId, SetSort.SET_SORT_MEASUREMENT_PARAMETERS, "test", setParameters,
			Collections.EMPTY_LIST);
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(new File("/tmp/amficom"), "measurement");
		StorableObjectXML objectXML = new StorableObjectXML(driver);
		objectXML.updateObject(set);
		objectXML.flush();

	}
}
