/*
 * $Id: MeasurementSetupTestCase.java,v 1.2 2004/08/31 15:29:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/31 15:29:13 $
 * @author $Author: bob $
 * @module tools
 */
public class MeasurementSetupTestCase extends AbstractMesurementTestCase {

	public MeasurementSetupTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = MeasurementSetupTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MeasurementSetupTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();

		SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();

		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		List list = measurementSetupDatabase.retrieveAll();

		List setList = setDatabase.retrieveAll();

		if (setList.isEmpty())
			fail("must be at less one set at db");

		Set set = (Set) setList.get(0);

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		List monitoredElementIds = new ArrayList();
		monitoredElementIds.add(((MonitoredElement) monitoredElementList.get(0)).getId());

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup mSetup = MeasurementSetup.createInstance(id, creatorId, set, null, null, null,
										"created by MeasurementSetupTestCase",
										1000 * 60 * 10, monitoredElementIds);

		MeasurementSetup mSetup2 = MeasurementSetup.getInstance((MeasurementSetup_Transferable) mSetup
				.getTransferable());

		assertEquals(mSetup.getId(), mSetup2.getId());

		MeasurementSetup mSetup3 = new MeasurementSetup(mSetup2.getId());

		assertEquals(mSetup2.getId(), mSetup3.getId());

		if (!list.isEmpty())
			measurementSetupDatabase.delete(mSetup);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		List list = measurementSetupDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			MeasurementSetup measurementSetup2 = new MeasurementSetup(measurementSetup.getId());
			assertEquals(measurementSetup.getId(), measurementSetup2.getId());
		}
	}

}