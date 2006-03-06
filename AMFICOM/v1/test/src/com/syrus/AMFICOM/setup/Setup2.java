/*-
 * $Id: Setup2.java,v 1.1.2.2 2006/03/06 15:14:34 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.setup;

import com.syrus.AMFICOM.configuration.SetupEquipmentType;
import com.syrus.AMFICOM.configuration.SetupPortType;
import com.syrus.AMFICOM.configuration.SetupTransmissionPathType;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.measurement.SetupActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.SetupActionType;
import com.syrus.AMFICOM.measurement.SetupMeasurementPortType;
import com.syrus.AMFICOM.reflectometry.SetupActionParameter;
import com.syrus.AMFICOM.reflectometry.SetupParameterType;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/06 15:14:34 $
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

		//-Create parameter types 
		commonTest.addTest(new SetupParameterType("testCreate"));

		//-Create configuration objects.
		commonTest.addTest(new SetupEquipmentType("testCreate"));
		commonTest.addTest(new SetupPortType("testCreate"));
		commonTest.addTest(new SetupTransmissionPathType("testCreate"));

		//-Create measurement port types, and action types
		commonTest.addTest(new SetupMeasurementPortType("testCreate"));
		commonTest.addTest(new SetupActionType("testCreate"));

		//-Create action parameter type bindings
		commonTest.addTest(new SetupActionParameterTypeBinding("testCreate"));

		//-Create enumerate action parameters.
		commonTest.addTest(new SetupActionParameter("testCreate"));

		return commonTest.createTestSetup();
	}

}
