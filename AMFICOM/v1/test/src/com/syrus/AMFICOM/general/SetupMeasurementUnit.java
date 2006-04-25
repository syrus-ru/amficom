/*-
 * $Id: SetupMeasurementUnit.java,v 1.2 2006/04/25 09:57:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 09:57:00 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupMeasurementUnit extends TestCase {

	public SetupMeasurementUnit(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(SetupMeasurementUnit.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws CreateObjectException {
		MeasurementUnitDatabase.insertAll();
	}
}
