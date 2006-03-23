/*-
 * $Id: TestAddTest.java,v 1.1.2.1 2006/03/23 07:40:16 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/23 07:40:16 $
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
		commonTest.addTestSuite(TestAddTest.class);
		return commonTest.createTestSetup();
	}

	public void testAdd() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final String monitoredElementDescription = "MonitoredElement_1_TransmissionPath_1_Equipment_1";
		final StorableObjectCondition monitoredElementCondition = new TypicalCondition(monitoredElementDescription,
				OPERATION_EQUALS,
				MONITOREDELEMENT_CODE,
				COLUMN_NAME);
		final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(monitoredElementCondition, true);
		if (monitoredElements.isEmpty()) {
			throw new ObjectNotFoundException("MonitoredElement " + monitoredElementDescription + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + monitoredElements, monitoredElements.size() == 1);
		final MonitoredElement monitoredElement = monitoredElements.iterator().next();

		final MeasurementType measurementType = MeasurementType.valueOf(REFLECTOMETRY.stringValue());

		final StorableObjectCondition measurementSetupCondition = new LinkedIdsCondition(monitoredElement, MEASUREMENTSETUP_CODE);
		final Set<MeasurementSetup> measurementSetups = StorableObjectPool.getStorableObjectsByCondition(measurementSetupCondition, true);
		if (measurementSetups.isEmpty()) {
			throw new ObjectNotFoundException("MeasurementSetups for MonitoredElement " + monitoredElementDescription + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + measurementSetups, measurementSetups.size() == 1);
		final MeasurementSetup measurementSetup = measurementSetups.iterator().next();

		final Test test = Test.createInstance(LoginManager.getUserId(),
				"test",
				VOID_IDENTIFIER,
				monitoredElement.getId(),
				new Date(),
				Collections.singleton(measurementSetup.getId()),
				measurementType.getId());

		StorableObjectPool.flush(test, creatorId, false);
	}
}
