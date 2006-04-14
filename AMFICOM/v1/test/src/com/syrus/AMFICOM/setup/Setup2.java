/*-
 * $Id: Setup2.java,v 1.1.2.6 2006/04/14 14:48:52 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.setup;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.SetupEquipmentType;
import com.syrus.AMFICOM.configuration.SetupPortType;
import com.syrus.AMFICOM.configuration.SetupTransmissionPathType;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.measurement.SetupActionParameter;
import com.syrus.AMFICOM.measurement.SetupActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.SetupActionType;
import com.syrus.AMFICOM.measurement.SetupMeasurementPortType;
import com.syrus.AMFICOM.reflectometry.SetupConstraintCharateristic;
import com.syrus.AMFICOM.reflectometry.SetupParameterType;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/04/14 14:48:52 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class Setup2 extends TestCase {

	public Setup2(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();

		// Create configuration objects.
		commonTest.addTest(new SetupEquipmentType("testCreate"));
		commonTest.addTest(new SetupPortType("testCreate"));
		commonTest.addTest(new SetupTransmissionPathType("testCreate"));

		// Create parameter types
		commonTest.addTest(new SetupParameterType("testCreate"));

		// Create measurement port types, and action types
		commonTest.addTest(new SetupMeasurementPortType("testCreate"));
		commonTest.addTest(new SetupActionType("testCreate"));

		// Create action parameter type bindings
		commonTest.addTest(new SetupActionParameterTypeBinding("testCreate"));

		// Create enumerate action parameters.
		commonTest.addTest(new SetupActionParameter("testCreate"));

		// Create constraint characteristic for action parameters.
		commonTest.addTest(new SetupConstraintCharateristic("testCreate"));

		return commonTest.createTestSetup();
	}

}
