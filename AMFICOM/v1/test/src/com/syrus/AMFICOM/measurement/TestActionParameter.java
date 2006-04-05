/*-
 * $Id: TestActionParameter.java,v 1.1.2.5 2006/04/05 14:31:27 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;


import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PREDICTION_TIME;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_M;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_NS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.REFLECTOGRAMMA;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;

/**
 * @version $Revision: 1.1.2.5 $, $Date: 2006/04/05 14:31:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestActionParameter extends TestCase {

	public TestActionParameter(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestActionParameter.class);
		return commonTest.createTestSetup();
	}

	public void testRetrieve() throws ApplicationException, IOException {
		final ParameterType parameterType = ParameterType.valueOf(PULSE_WIDTH_M);
		final MeasurementType measurementType = MeasurementType.valueOf(REFLECTOMETRY);
		final MeasurementPortType measurementPortType = MeasurementPortType.valueOf(REFLECTOMETRY_PK7600);

		final ActionParameterTypeBinding actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		final ParameterValueKind parameterValueKind = actionParameterTypeBinding.getParameterValueKind();
		System.out.println("Parameter value kind: " + parameterValueKind);

		final Set<ActionParameter> actionParameters = ActionParameter.getValues(actionParameterTypeBinding);
		for (final ActionParameter actionParameter : actionParameters) {
			System.out.println("Codename: '" + actionParameter.getTypeCodename() + "', value: " + actionParameter.stringValue());
		}
	}

	public void testSingularCodenameIdentifierMap() throws ApplicationException {
		final String[] codenames = new String[] { PULSE_WIDTH_M.stringValue() };
		final Map<String, Identifier> typeCodenameIdMap = ParameterType.getCodenameIdentifierMap(codenames);
		System.out.println("Map: " + typeCodenameIdMap);
	}

	public void testMultipleCodenameIdentifierMap() throws ApplicationException {
		final String[] codenames = new String[] { PULSE_WIDTH_M.stringValue(),
				PULSE_WIDTH_NS.stringValue(),
				DADARA_ANALYSIS_RESULT.stringValue(),
				PREDICTION_TIME.stringValue() };
		final Map<String, Identifier> typeCodenameIdMap = ParameterType.getCodenameIdentifierMap(codenames);
		System.out.println("Map: " + typeCodenameIdMap);
	}

	public void testActionResultParameter() throws ApplicationException {
		final ParameterType parameterType = ParameterType.valueOf(REFLECTOGRAMMA);

		final StorableObjectCondition measurementCondition = new EquivalentCondition(MEASUREMENT_CODE);
		final Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(measurementCondition, true);
		for (final Measurement measurement : measurements) {
			final MeasurementResultParameter measurementResultParameter = measurement.getActionResultParameter(parameterType);
			System.out.println("Measurement: '" + measurement.getId() + "', reflectogramma length: " + measurementResultParameter.getValue().length);
		}
	}
}
