/*-
 * $Id: TestAddTest.java,v 1.1.2.6 2006/04/17 09:43:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_SUBSTRING;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_SCHEDULED;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/04/17 09:43:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestAddTest extends TestCase {

	public TestAddTest(final String name) {
		super(name);
	}

	public static junit.framework.Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTest(new TestAddTest("testRetrieve"));
		return commonTest.createTestSetup();
	}

	public void testAdd() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final MeasurementType measurementType = MeasurementType.valueOf(REFLECTOMETRY.stringValue());
		final MeasurementPortType measurementPortType = MeasurementPortType.valueOf(REFLECTOMETRY_PK7600.stringValue());

		final String measurementPortDescriptionSubstring = "MeasurementPort_1_";
		final StorableObjectCondition measurementPortCondition = new CompoundCondition(new LinkedIdsCondition(measurementPortType,
						MEASUREMENTPORT_CODE),
				AND,
				new TypicalCondition(measurementPortDescriptionSubstring,
						OPERATION_SUBSTRING,
						MEASUREMENTPORT_CODE,
						COLUMN_DESCRIPTION));
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true);
		if (measurementPorts.isEmpty()) {
			throw new ObjectNotFoundException("MeasurementPort like " + measurementPortDescriptionSubstring + " of type " + measurementPortType.getCodename() + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + measurementPorts, measurementPorts.size() == 1);
		final MeasurementPort measurementPort = measurementPorts.iterator().next();

		final StorableObjectCondition monitoredElementCondition = new LinkedIdsCondition(measurementPort, MONITOREDELEMENT_CODE);
		final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(monitoredElementCondition, true);
		if (monitoredElements.isEmpty()) {
			throw new ObjectNotFoundException("MonitoredElement for MeasurementPort " + measurementPort.getDescription() + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + monitoredElements, monitoredElements.size() == 1);
		final MonitoredElement monitoredElement = monitoredElements.iterator().next();
		System.out.println("MonitoredElement: " + monitoredElement.getName() + ", local address: " + monitoredElement.getLocalAddress());

		final KIS kis = measurementPort.getKIS();
		System.out.println("KIS: " + kis.getDescription() + ", " + kis.getHostName() + ":" + kis.getTCPPort() + ", on service: " + kis.isOnService());

		final StorableObjectCondition measurementSetupCondition = new LinkedIdsCondition(monitoredElement, MEASUREMENTSETUP_CODE);
		final Set<MeasurementSetup> measurementSetups = StorableObjectPool.getStorableObjectsByCondition(measurementSetupCondition, true);
		if (measurementSetups.isEmpty()) {
			throw new ObjectNotFoundException("MeasurementSetups for MonitoredElement " + monitoredElement.getName() + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + measurementSetups, measurementSetups.size() == 1);
		final MeasurementSetup measurementSetup = measurementSetups.iterator().next();

		final Test test = Test.createInstance(LoginManager.getUserId(),
				"test",
				VOID_IDENTIFIER,
				monitoredElement.getId(),
				new Date(System.currentTimeMillis() + 30L * 1000L),
				Collections.singleton(measurementSetup.getId()),
				measurementType.getId(),
				VOID_IDENTIFIER);
		test.setStatus(TEST_STATUS_SCHEDULED);

		StorableObjectPool.flush(test, creatorId, false);
	}

	public void testRetrieve() throws ApplicationException {
		final StorableObjectCondition testCondition = new EquivalentCondition(TEST_CODE);
		final Set<Test> tests = StorableObjectPool.getStorableObjectsByCondition(testCondition, true);
		for (final Test test : tests) {
			System.out.println("Test: " + test.getId() + ", temporal type: " + test.getTemporalType() + ", status: " + test.getStatus());
		}
	}
}
