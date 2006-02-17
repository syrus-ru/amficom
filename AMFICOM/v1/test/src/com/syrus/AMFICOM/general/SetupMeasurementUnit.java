/*-
 * $Id: SetupMeasurementUnit.java,v 1.1.2.1 2006/02/17 11:37:52 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/17 11:37:52 $
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

	public void testCreateAll() throws CreateObjectException {
		MeasurementUnitDatabase.insertAll();
	}
}
