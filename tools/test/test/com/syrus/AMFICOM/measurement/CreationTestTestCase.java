/*
 * $Id: CreationTestTestCase.java,v 1.1 2004/10/29 07:30:42 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.ByteArray;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/29 07:30:42 $
 * @author $Author: bob $
 * @module tools
 */
public class CreationTestTestCase extends AbstractMesurementTestCase {

	public CreationTestTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = CreationTestTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static junit.framework.Test suite() {
		return suiteWrapper(CreationTestTestCase.class);
	}

	public void testCreationPeriodicalTest() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException, IOException {

		TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
		TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
				.getTemporalPatternDatabase();
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();
		ParameterTypeDatabase parameterTypeDatabase = (ParameterTypeDatabase) MeasurementDatabaseContext
				.getParameterTypeDatabase();
		SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		List monitoredElementIds = Collections.singletonList(((MonitoredElement) monitoredElementList.get(0)).getId());

		TestTemporalType temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;

		List measurementSetupList = measurementSetupDatabase.retrieveAll();

		if (measurementSetupList.isEmpty()) {
			fail("must be at less one measurement setup at db");
		}

		List setList = setDatabase.retrieveAll();
		//List measurementSetupIds = new ArrayList();
		//measurementSetupIds.add(((MeasurementSetup)
		// measurementSetupList.get(0)).getId());
		ParameterType wvlenParam = parameterTypeDatabase.retrieveForCodename("ref_wvlen");
		ParameterType trclenParam = parameterTypeDatabase.retrieveForCodename("ref_trclen");
		ParameterType resParam = parameterTypeDatabase.retrieveForCodename("ref_res");
		ParameterType pulswdParam = parameterTypeDatabase.retrieveForCodename("ref_pulswd");
		ParameterType iorParam = parameterTypeDatabase.retrieveForCodename("ref_ior");
		ParameterType scansParam = parameterTypeDatabase.retrieveForCodename("ref_scans");

		SetParameter[] params = new SetParameter[6];

		Identifier paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[0] = new SetParameter(paramId, wvlenParam, new ByteArray((double) 1625).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		//params[1] = new SetParameter(paramId, trclenParam, new
		// ByteArray((double) 131072).getBytes());
		params[1] = new SetParameter(paramId, trclenParam, new ByteArray((double) 125000).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[2] = new SetParameter(paramId, resParam, new ByteArray((double) 8).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[3] = new SetParameter(paramId, pulswdParam, new ByteArray((long) 5000).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[4] = new SetParameter(paramId, iorParam, new ByteArray((double) 1.46820).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[5] = new SetParameter(paramId, scansParam, new ByteArray((long) 4096).getBytes());

		Identifier setId = IdentifierGenerator.generateIdentifier(ObjectEntities.SET_ENTITY_CODE);
		Set set = Set.createInstance(setId, creatorId, SetSort.SET_SORT_MEASUREMENT_PARAMETERS, "setTestCase#"
				+ ((setList != null) ? (setList.size() + 1) : 0), params, monitoredElementIds);

		Set set2 = Set.getInstance((Set_Transferable) set.getTransferable());

		// set created
		
		// create measurement setup
		Identifier measurementSetupId = IdentifierGenerator.generateIdentifier(ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup mSetup = MeasurementSetup.createInstance(measurementSetupId, creatorId, set, null, null, null,
										"created by MeasurementSetupTestCase",
										1000 * 60 * 10, monitoredElementIds);

		MeasurementSetup mSetup2 = MeasurementSetup.getInstance((MeasurementSetup_Transferable) mSetup
				.getTransferable());

		List measurementSetupIds = Collections.singletonList(mSetup2.getId());
		
		// measurement setup created

		List measurementTypeList = measurementTypeDatabase.retrieveAll();

		if (measurementTypeList.isEmpty())
			fail("must be at less one measurement type at db");

		MeasurementType measurementType = (MeasurementType) measurementTypeList.get(0);

		List temporalPatternList = temporalPatternDatabase.retrieveAll();

		if (temporalPatternList.isEmpty())
			fail("must be at less one temporal pattern at db");

		TemporalPattern temporalPettern = (TemporalPattern) temporalPatternList.get(0);

		MonitoredElement me = (MonitoredElement) monitoredElementList.get(0);

		AnalysisType analysisType = null;

		EvaluationType evaluationType = null;

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

		Date startDate = new Date(System.currentTimeMillis());

		Test test = Test.createInstance(id, creatorId, startDate, new Date(System.currentTimeMillis() + 1000 * 60 * 60
				* 24), temporalPettern, temporalType, measurementType, analysisType, evaluationType, me,
										TestReturnType.TEST_RETURN_TYPE_WHOLE, "cretated by TestTestCase at "
												+ DatabaseDate.SDF.format(startDate), measurementSetupIds);

		Test test2 = Test.getInstance((Test_Transferable) test.getTransferable());

		Test test3 = new Test(test2.getId());

		assertEquals(test2, test3);

		//		if (!list.isEmpty())
		//			TestDatabase.delete(test);

	}
}