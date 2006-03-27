/*-
 * $Id: TestMeasurementResultParameter.java,v 1.1.2.1 2006/03/27 09:33:59 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_SUBSTRING;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/27 09:33:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestMeasurementResultParameter extends TestCase {

	public TestMeasurementResultParameter(final String name) {
		super(name);
	}

	public static junit.framework.Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestMeasurementResultParameter.class);
		return commonTest.createTestSetup();
	}

	public void testRetrieve() throws ApplicationException, IOException {

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
		System.out.println("MonitoredElement: " + monitoredElement.getName());

		final StorableObjectCondition measurementCondition = new LinkedIdsCondition(monitoredElement, MEASUREMENT_CODE);
		final Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(measurementCondition, true);
		assertTrue(NON_EMPTY_EXPECTED + ": " + measurements, !measurements.isEmpty());
		final StorableObjectCondition measurementResultParameterCondition = new LinkedIdsCondition(measurements, MEASUREMENTRESULTPARAMETER_CODE);
		final Set<MeasurementResultParameter> measurementResultParameters = StorableObjectPool.getStorableObjectsByCondition(measurementResultParameterCondition, true);
		for (final MeasurementResultParameter measurementResultParameter : measurementResultParameters) {
			final Identifier measurementResultParameterId = measurementResultParameter.getId();
			final String fileName = measurementResultParameterId.toString() + ".sor";

			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(fileName);
				fileOutputStream.write(measurementResultParameter.getValue());
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
				ioe.printStackTrace();
			} finally {
				if (fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			}
		}
	}
}
