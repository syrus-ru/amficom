/*
 * $Id: MeasurementDatabaseContext.java,v 1.10 2004/07/28 11:58:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.10 $, $Date: 2004/07/28 11:58:31 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public abstract class MeasurementDatabaseContext {
	protected static StorableObjectDatabase	analysisDatabase;
	protected static StorableObjectDatabase	analysisTypeDatabase;
	protected static StorableObjectDatabase	evaluationDatabase;
	protected static StorableObjectDatabase	evaluationTypeDatabase;
	protected static StorableObjectDatabase	measurementDatabase;
	protected static StorableObjectDatabase	measurementSetupDatabase;
	protected static StorableObjectDatabase	measurementTypeDatabase;
	protected static StorableObjectDatabase	parameterTypeDatabase;
	protected static StorableObjectDatabase	resultDatabase;
	protected static StorableObjectDatabase	setDatabase;
	protected static StorableObjectDatabase	temporalPatternDatabase;
	protected static StorableObjectDatabase	testDatabase;
	
	private MeasurementDatabaseContext() {
	}

	public static void init(StorableObjectDatabase analysisDatabase1,
													StorableObjectDatabase analysisTypeDatabase1,
													StorableObjectDatabase evaluationDatabase1,
													StorableObjectDatabase evaluationTypeDatabase1,
													StorableObjectDatabase measurementDatabase1,
													StorableObjectDatabase measurementSetupDatabase1,
													StorableObjectDatabase measurementTypeDatabase1,
													StorableObjectDatabase parameterTypeDatabase1,
													StorableObjectDatabase resultDatabase1,
													StorableObjectDatabase setDatabase1,
													StorableObjectDatabase temporalPatternDatabase1,
													StorableObjectDatabase testDatabase1) {
		analysisDatabase = analysisDatabase1;
		analysisTypeDatabase = analysisTypeDatabase1;
		evaluationDatabase = evaluationDatabase1;
		evaluationTypeDatabase = evaluationTypeDatabase1;
		measurementDatabase = measurementDatabase1;
		measurementSetupDatabase = measurementSetupDatabase1;
		measurementTypeDatabase = measurementTypeDatabase1;
		parameterTypeDatabase = parameterTypeDatabase1;
		resultDatabase = resultDatabase1;
		setDatabase = setDatabase1;
		temporalPatternDatabase = temporalPatternDatabase1;
		testDatabase = testDatabase1;
	}
}
