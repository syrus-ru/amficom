/*
 * $Id: TestTestCase.java,v 1.2 2005/02/08 11:15:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 11:15:36 $
 * @author $Author: bob $
 * @module tools
 */
public class TestTestCase extends AbstractMesurementTestCase {

	public TestTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = TestTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		// junit.swingui.TestRunner.run(clazz);
		// junit.textui.TestRunner.run(clazz);
	}

	public static junit.framework.Test suite() {
		return suiteWrapper(TestTestCase.class);
	}

	public void _testCreationPeriodicalTest() throws CreateObjectException, ObjectNotFoundException,
			RetrieveObjectException {

		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
		TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
				.getTemporalPatternDatabase();
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;

		List measurementSetupList = measurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty()) {
			fail("must be at less one measurement setup at db");
		}

		List measurementSetupIds = Collections.singletonList(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = measurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List temporalPatternList = temporalPatternDatabase.retrieveAll();

		if (temporalPatternList.isEmpty())
			fail("must be at less one temporal pattern at db");

		TemporalPattern temporalPettern = (TemporalPattern) temporalPatternList.get(0);

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		Date startDate = new Date(System.currentTimeMillis());

		Test test = Test.createInstance(creatorId, startDate,
			new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24), temporalPettern, temporalType, measurementType,
			analysisType, evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase at "
					+ DatabaseDate.SDF.format(startDate), measurementSetupIds);

		test.insert();

		Test test3 = new Test(test.getId());

		assertEquals(test, test3);

		// if (!list.isEmpty())
		// TestDatabase.delete(test);

	}

	public void testSearchTest() throws ApplicationException {

		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
		TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
				.getTemporalPatternDatabase();
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;

		List measurementSetupList = measurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty()) {
			fail("must be at less one measurement setup at db");
		}

		List measurementSetupIds = Collections.singletonList(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = measurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List temporalPatternList = temporalPatternDatabase.retrieveAll();

		if (temporalPatternList.isEmpty())
			fail("must be at less one temporal pattern at db");

		TemporalPattern temporalPettern = (TemporalPattern) temporalPatternList.get(0);

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		Date startDate = new Date(System.currentTimeMillis());
		Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

		Test test = Test.createInstance(creatorId, startDate, endDate, temporalPettern, temporalType, measurementType,
			analysisType, evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase at "
					+ DatabaseDate.SDF.format(startDate), measurementSetupIds);

		MeasurementStorableObjectPool.putStorableObject(test);

		Date date1 = new Date(startDate.getTime() - 1000L * 60L * 60L);
		Date date2 = new Date(endDate.getTime() + 1000L * 60L * 60L);
		TypicalCondition typicalCondition = new TypicalCondition(date1, date2, OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_START_TIME);

		TypicalCondition typicalCondition2 = new TypicalCondition(date1, date2, OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_END_TIME);

		CompoundCondition compoundCondition = new CompoundCondition(typicalCondition, CompoundConditionSort.AND,
																	typicalCondition2);

		List storableObjectsByCondition = MeasurementStorableObjectPool.getStorableObjectsByCondition(
			compoundCondition, true);
		for (Iterator it = storableObjectsByCondition.iterator(); it.hasNext();) {
			Test test2 = (Test) it.next();
			System.out.println("id:" + test2.getId());
		}

	}

	public void _testCreationSimpleTest() throws CreateObjectException, ObjectNotFoundException,
			RetrieveObjectException, IllegalDataException {
		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
		List list = testDatabase.retrieveByIds(null, TestWrapper.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
				+ temporalType.value());

		List measurementSetupList = measurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty())
			fail("must be at less one measurement setup at db");

		List measurementSetupIds = new ArrayList();
		measurementSetupIds.add(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = measurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		TemporalPattern temporalPettern = null;

		Test test = Test.createInstance(creatorId, new Date(System.currentTimeMillis()), null, temporalPettern,
			temporalType, measurementType, analysisType, evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE,
			"cretated by TestTestCase", measurementSetupIds);

		test.insert();

		Test test3 = new Test(test.getId());

		assertEquals(test, test3);

		if (!list.isEmpty())
			testDatabase.delete(test);

	}

	public void _testCreationContinualTest() throws CreateObjectException, ObjectNotFoundException,
			RetrieveObjectException, IllegalDataException {
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();

		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS;
		// TestDatabase testDatabase = (TestDatabase)
		// MeasurementDatabaseContext.getTestDatabase();
		// List list = testDatabase.retrieveByIds(null,
		// TestWrapper.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
		// + temporalType.value());

		List measurementSetupList = measurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty())
			fail("must be at less one measurement setup at db");

		List measurementSetupIds = new ArrayList();
		measurementSetupIds.add(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = measurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		TemporalPattern temporalPettern = null;

		Test test = Test.createInstance(creatorId, new Date(System.currentTimeMillis()), new Date(System
				.currentTimeMillis()
				+ 1000 * 60 * 60 * 14), temporalPettern, temporalType, measurementType, analysisType, evaluationType,
			me, TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase", measurementSetupIds);

		test.insert();

		Test test3 = new Test(test.getId());

		assertEquals(test, test3);

		// if (!list.isEmpty())
		// TestDatabase.delete(test);

	}

	public void _testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
		List list = testDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			Test test2 = new Test(test.getId());
			assertEquals(test.getId(), test2.getId());
		}
	}

	public void _testRetriveByCondition() throws RetrieveObjectException, IllegalDataException {

		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
		List list = testDatabase.retrieveByIds(null, TestWrapper.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
				+ temporalType.value());
	}

}