/*
 * $Id: MeasurementSetupTestCase.java,v 1.6 2004/10/29 07:30:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2004/10/29 07:30:48 $
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

		List monitoredElementIds = Collections.singletonList(((MonitoredElement) monitoredElementList.get(0)).getId());

		Identifier measurementSetupId = IdentifierGenerator.generateIdentifier(ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup mSetup = MeasurementSetup.createInstance(measurementSetupId, creatorId, set, null, null, null,
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

	public void _testMultipleCreationAndUpdate() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, IllegalDataException, UpdateObjectException, RetrieveObjectException,
			ObjectNotFoundException {

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

		MeasurementSetup mSetup1 = MeasurementSetup.createInstance(id, creatorId, set, null, null, null,
										"created by MeasurementSetupTestCase",
										1000 * 60 * 10, monitoredElementIds);

		id = IdentifierGenerator.generateIdentifier(ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup mSetup2 = MeasurementSetup.createInstance(id, creatorId, set, null, null, null,
										"created by MeasurementSetupTestCase",
										1000 * 60 * 10, monitoredElementIds);

		List measurementSetupList = new LinkedList();
		measurementSetupList.add(mSetup1);
		measurementSetupList.add(mSetup2);

		boolean versionCollision = false;

		try {
			measurementSetupDatabase
					.update(measurementSetupList, StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (VersionCollisionException e) {
			versionCollision = true;
		}

		if (versionCollision)
			fail("VersionCollision occur, but dont");

		versionCollision = false;

		MeasurementSetup measurementSetup3 = new MeasurementSetup(mSetup1.getId());
		MeasurementSetup measurementSetup4 = new MeasurementSetup(mSetup2.getId());

		List measurementSetupList2 = new LinkedList();
		measurementSetupList2.add(measurementSetup3);
		measurementSetupList2.add(measurementSetup4);

		mSetup1.setDescription("newOneDesc1");
		mSetup2.setDescription("newOneDesc2");

		try {
			measurementSetupDatabase
					.update(measurementSetupList, StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (VersionCollisionException e) {
			versionCollision = true;
		}

		if (versionCollision)
			fail("VersionCollision occur, but dont");

		versionCollision = false;

		try {
			measurementSetupDatabase.update(measurementSetupList2, StorableObjectDatabase.UPDATE_CHECK,
							null);
		} catch (VersionCollisionException vce) {
			versionCollision = true;
		}

		if (!versionCollision)
			fail("VersionCollision must be occur");

		if (!list.isEmpty()) {
			for (Iterator it = measurementSetupList.iterator(); it.hasNext();) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				measurementSetupDatabase.delete(measurementSetup);
			}

		}

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