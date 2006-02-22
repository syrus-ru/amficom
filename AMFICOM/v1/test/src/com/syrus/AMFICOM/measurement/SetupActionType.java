/*
 * $Id: SetupActionType.java,v 1.1.2.1 2006/02/22 15:00:58 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/22 15:00:58 $
 * @author $Author: arseniy $
 * @module test
 */
public class SetupActionType extends TestCase {
	private static String KEY_MEASUREMENT_TYPE_DESCRIPTION_ROOT = "Description.MeasurementType.";
	private static String KEY_ANALYSIS_TYPE_DESCRIPTION_ROOT = "Description.AnalysisType.";
	private static String KEY_MODELING_TYPE_DESCRIPTION_ROOT = "Description.ModelingType.";

	public SetupActionType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(SetupActionType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Set<ActionType> actionTypes = new HashSet<ActionType>();

		for (final MeasurementTypeCodename measurementTypeCodename : MeasurementTypeCodename.values()) {
			final String codename = measurementTypeCodename.stringValue();
			actionTypes.add(MeasurementType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_MEASUREMENT_TYPE_DESCRIPTION_ROOT + codename)));
		}
		for (final AnalysisTypeCodename analysisTypeCodename : AnalysisTypeCodename.values()) {
			final String codename = analysisTypeCodename.stringValue();
			actionTypes.add(AnalysisType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_ANALYSIS_TYPE_DESCRIPTION_ROOT + codename)));
		}
		for (final ModelingTypeCodename modelingTypeCodename : ModelingTypeCodename.values()) {
			final String codename = modelingTypeCodename.stringValue();
			actionTypes.add(ModelingType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_MODELING_TYPE_DESCRIPTION_ROOT + codename)));
		}

		StorableObjectPool.flush(actionTypes, creatorId, false);
	}
}
