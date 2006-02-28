/*-
 * $Id: TestActionParameter.java,v 1.1.2.1 2006/02/28 15:45:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/28 15:45:48 $
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

		final Set<ActionParameter> actionParameters = ActionParameter.getValues(actionParameterTypeBinding);
		for (final ActionParameter actionParameter : actionParameters) {
			final int value = (new ByteArray(actionParameter.getValue())).toInt();
			System.out.println("Codename: '" + actionParameter.getTypeCodename() + "', value: " + value);
		}
	}
}
