/*
 * $Id: SetupMeasurementPortType.java,v 1.1.2.1 2006/02/22 15:00:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

public final class SetupMeasurementPortType extends TestCase {
	private static String KEY_DESCRIPTION_ROOT = "Description.MeasurementPortType.";
	private static String KEY_NAME_ROOT = "Name.MeasurementPortType.";

	public SetupMeasurementPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupMeasurementPortType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Set<MeasurementPortType> measurementPortTypes = new HashSet<MeasurementPortType>();
		String codename;

		codename = MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A.stringValue();
		measurementPortTypes.add(MeasurementPortType.createInstance(creatorId,
				codename,
				I18N.getString(KEY_DESCRIPTION_ROOT + codename),
				I18N.getString(KEY_NAME_ROOT + codename)));

		codename = MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A.stringValue();
		measurementPortTypes.add(MeasurementPortType.createInstance(creatorId,
				codename,
				I18N.getString(KEY_DESCRIPTION_ROOT + codename),
				I18N.getString(KEY_NAME_ROOT + codename)));

		codename = MeasurementPortTypeCodename.REFLECTOMETRY_PK7600.stringValue();
		measurementPortTypes.add(MeasurementPortType.createInstance(creatorId,
				codename,
				I18N.getString(KEY_DESCRIPTION_ROOT + codename),
				I18N.getString(KEY_NAME_ROOT + codename)));

		StorableObjectPool.flush(measurementPortTypes, creatorId, false);
	}
}
