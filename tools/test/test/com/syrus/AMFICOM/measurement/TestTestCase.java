/*
 * $Id: TestTestCase.java,v 1.2 2004/08/16 16:21:28 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/16 16:21:28 $
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
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
		List list = TestDatabase.retrieveAll(TestDatabase.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS + temporalType.value());


		List measurementSetupList = MeasurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty())
			fail("must be at less one measurement setup at db");

		List measurementSetupIds = new ArrayList();
		measurementSetupIds.add(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = MeasurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List temporalPatternList = TemporalPatternDatabase.retrieveAll();

		if (temporalPatternList.isEmpty())
			fail("must be at less one temporal pattern at db");

		Identifier temporalPetternId = ((TemporalPattern) temporalPatternList.get(0)).getId();

		List monitoredElementList = MonitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Test test = Test.createInstance(id, creatorId, new Date(System.currentTimeMillis()), new Date(System
				.currentTimeMillis() + 1000 * 60 * 30), temporalPetternId,
										temporalType, measurementType, analysisType,
										evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE,
										"cretated by TestTestCase", measurementSetupIds);

		Test test2 = new Test((Test_Transferable) test.getTransferable());

		Test test3 = new Test(test2.getId());

		assertEquals(test2.getId(), test3.getId());

		/**
		 * FIXME почему-то тут ооочень долго тормозит...
		 */
		if (!list.isEmpty())
			TestDatabase.delete(test);

	}

	public void testCreationSimpleTest() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
		List list = TestDatabase.retrieveAll(TestDatabase.COLUMN_TEMPORAL_TYPE + StorableObjectDatabase.EQUALS + temporalType.value());

		List measurementSetupList = MeasurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty())
			fail("must be at less one measurement setup at db");

		List measurementSetupIds = new ArrayList();
		measurementSetupIds.add(((MeasurementSetup) measurementSetupList.get(0)).getId());

		List measurementTypeList = MeasurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);


		List monitoredElementList = MonitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;
		
		Identifier temporalPetternId = null;

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Test test = Test.createInstance(id, creatorId, new Date(System.currentTimeMillis()), new Date(System
				.currentTimeMillis() + 1000 * 60 * 30), temporalPetternId,
										temporalType, measurementType, analysisType,
										evaluationType, me, TestReturnType.TEST_RETURN_TYPE_WHOLE,
										"cretated by TestTestCase", measurementSetupIds);

		Test test2 = new Test((Test_Transferable) test.getTransferable());


		Test test3 = new Test(test2.getId());

		assertEquals(test2.getId(), test3.getId());

		/**
		 * FIXME почему-то тут ооочень долго тормозит...
		 */
		if (!list.isEmpty())
			TestDatabase.delete(test);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		List list = TestDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			Test test2 = new Test(test.getId());
			assertEquals(test.getId(), test2.getId());
		}
	}

}