/*-
 * $Id: TestActionParameter.java,v 1.1.2.4 2006/03/22 08:22:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/22 08:22:12 $
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
		final ParameterType parameterType = ParameterType.valueOf(ReflectometryParameterTypeCodename.PULSE_WIDTH_M.stringValue());
		final MeasurementType measurementType = MeasurementType.valueOf(MeasurementTypeCodename.REFLECTOMETRY.stringValue());
		final MeasurementPortType measurementPortType = MeasurementPortType.valueOf(MeasurementPortTypeCodename.REFLECTOMETRY_PK7600.stringValue());

		final ActionParameterTypeBinding actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		final ParameterValueKind parameterValueKind = actionParameterTypeBinding.getParameterValueKind();
		System.out.println("Parameter value kind: " + parameterValueKind);

		final Set<ActionParameter> actionParameters = ActionParameter.getValues(actionParameterTypeBinding);
		for (final ActionParameter actionParameter : actionParameters) {
			System.out.println("Codename: '" + actionParameter.getTypeCodename() + "', value: " + actionParameter.stringValue());
		}
	}

	public void testSingularCodenameIdentifierMap() throws ApplicationException {
		final String[] codenames = new String[] { ReflectometryParameterTypeCodename.PULSE_WIDTH_M.stringValue() };
		final Map<String, Identifier> typeCodenameIdMap = ParameterType.getCodenameIdentifierMap(codenames);
		System.out.println("Map: " + typeCodenameIdMap);
	}

	public void testMultipleCodenameIdentifierMap() throws ApplicationException {
		final String[] codenames = new String[] { ReflectometryParameterTypeCodename.PULSE_WIDTH_M.stringValue(),
				ReflectometryParameterTypeCodename.PULSE_WIDTH_NS.stringValue(),
				ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue(),
				ReflectometryParameterTypeCodename.PREDICTION_TIME.stringValue() };
		final Map<String, Identifier> typeCodenameIdMap = ParameterType.getCodenameIdentifierMap(codenames);
		System.out.println("Map: " + typeCodenameIdMap);
	}
}
