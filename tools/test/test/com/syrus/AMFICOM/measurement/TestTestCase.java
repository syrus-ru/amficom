/*
 * $Id: TestTestCase.java,v 1.9 2004/10/18 09:46:19 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2004/10/18 09:46:19 $
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
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static junit.framework.Test suite() {
		return suiteWrapper(TestTestCase.class);
	}

	public void testCreationPeriodicalTest() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException, IllegalDataException {

		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
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

		if (measurementSetupList.isEmpty()){
			fail("must be at less one measurement setup at db");
		}

		List measurementSetupIds = new ArrayList();
		measurementSetupIds.add(((MeasurementSetup) measurementSetupList.get(0)).getId());
		
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

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Test test = Test.createInstance(id, creatorId, new Date(System.currentTimeMillis()), new Date(System
				.currentTimeMillis() + 1000 * 60 * 60 * 24), temporalPettern, temporalType, measurementType, analysisType,
										evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE,
										"cretated by TestTestCase", measurementSetupIds);

		Test test2 = Test.getInstance((Test_Transferable) test.getTransferable());

		Test test3 = new Test(test2.getId());

		assertEquals(test2, test3);

		//		if (!list.isEmpty())
		//			TestDatabase.delete(test);

	}

	public void _testCreationSimpleTest() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
		List list = testDatabase.retrieveByIds(null, TestDatabase.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
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

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Test test = Test.createInstance(id, creatorId, new Date(System.currentTimeMillis()), null, temporalPettern,
										temporalType, measurementType, analysisType, evaluationType, me,
										TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase",
										measurementSetupIds);

		Test test2 = Test.getInstance((Test_Transferable) test.getTransferable());

		Test test3 = new Test(test2.getId());

		assertEquals(test2, test3);

		if (!list.isEmpty())
			testDatabase.delete(test);

	}

	public void _testCreationContinualTest() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();

		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS;
		List list = testDatabase.retrieveByIds(null, TestDatabase.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
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

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Test test = Test.createInstance(id, creatorId, new Date(System.currentTimeMillis()), new Date(System
				.currentTimeMillis()
				+ 1000 * 60 * 60 * 14), temporalPettern, temporalType, measurementType, analysisType, evaluationType,
										me, TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase",
										measurementSetupIds);

		Test test2 = Test.getInstance((Test_Transferable) test.getTransferable());

		Test test3 = new Test(test2.getId());

		assertEquals(test2, test3);

		//		if (!list.isEmpty())
		//			TestDatabase.delete(test);

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
		List list = testDatabase.retrieveByIds(null, TestDatabase.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS
				+ temporalType.value());
	}

}