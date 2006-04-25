/*-
 * $Id: Setup3.java,v 1.3 2006/04/25 10:28:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.setup;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.SetupEquipment;
import com.syrus.AMFICOM.configuration.SetupPort;
//import com.syrus.AMFICOM.configuration.SetupProtoEquipment;
import com.syrus.AMFICOM.configuration.SetupTransmissionPath;
import com.syrus.AMFICOM.general.CORBACommonTest;
//import com.syrus.AMFICOM.measurement.SetupKIS;
//import com.syrus.AMFICOM.measurement.SetupMeasurementPort;
//import com.syrus.AMFICOM.measurement.SetupMonitoredElement;

/**
 * @version $Revision: 1.3 $, $Date: 2006/04/25 10:28:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class Setup3 extends TestCase {

	public Setup3(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();

//		commonTest.addTest(new SetupProtoEquipment("testCreate"));
		commonTest.addTest(new SetupEquipment("testCreate"));
		commonTest.addTest(new SetupPort("testCreate"));
		commonTest.addTest(new SetupTransmissionPath("testCreate"));

//		commonTest.addTest(new SetupKIS("testCreate"));
//		commonTest.addTest(new SetupMeasurementPort("testCreate"));
//		commonTest.addTest(new SetupMonitoredElement("testCreate"));

		return commonTest.createTestSetup();
	}
}
