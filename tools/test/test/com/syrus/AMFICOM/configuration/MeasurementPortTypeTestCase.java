/*
 * $Id: MeasurementPortTypeTestCase.java,v 1.2 2004/08/31 15:29:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/31 15:29:12 $
 * @author $Author: bob $
 * @module tools
 */
public class MeasurementPortTypeTestCase extends ConfigureTestCase {

	public MeasurementPortTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {		
		Class clazz = MeasurementPortTypeTestCase.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MeasurementPortTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {
		MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext
		.getMeasurementPortTypeDatabase();
		List list = measurementPortTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		MeasurementPortType mPortType = MeasurementPortType.createInstance(id, ConfigureTestCase.creatorId,
																			"testCaseMeasurementPortType",
																			"measurementPortType created by MeasurementPortTypeTestCase");
		MeasurementPortType mPortType2 = MeasurementPortType.getInstance((MeasurementPortType_Transferable) mPortType
				.getTransferable());

		assertEquals(mPortType.getId(), mPortType2.getId());

		MeasurementPortType mPortType3 = new MeasurementPortType(mPortType2.getId());

		assertEquals(mPortType2.getId(), mPortType3.getId());

		if (!list.isEmpty())
			measurementPortTypeDatabase.delete(mPortType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext
		.getMeasurementPortTypeDatabase();
		List list = measurementPortTypeDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			MeasurementPortType mPortType = (MeasurementPortType) it.next();
			MeasurementPortType mPortType2 = new MeasurementPortType(mPortType.getId());
			assertEquals(mPortType.getId(), mPortType2.getId());

		}
	}

}