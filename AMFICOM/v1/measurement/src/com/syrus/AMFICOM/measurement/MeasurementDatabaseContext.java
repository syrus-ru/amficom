/*
 * $Id: MeasurementDatabaseContext.java,v 1.12 2004/08/09 14:21:33 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/09 14:21:33 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementDatabaseContext {
	protected static StorableObjectDatabase	parameterTypeDatabase;
	protected static StorableObjectDatabase	measurementTypeDatabase;
	protected static StorableObjectDatabase	analysisTypeDatabase;
	protected static StorableObjectDatabase	evaluationTypeDatabase;
	
	protected static StorableObjectDatabase	setDatabase;
	protected static StorableObjectDatabase	measurementSetupDatabase;
	protected static StorableObjectDatabase	measurementDatabase;
	protected static StorableObjectDatabase	analysisDatabase;
	protected static StorableObjectDatabase	evaluationDatabase;
	protected static StorableObjectDatabase	testDatabase;
	protected static StorableObjectDatabase	resultDatabase;
	protected static StorableObjectDatabase	temporalPatternDatabase;

	private MeasurementDatabaseContext() {
	}

	public static void init(StorableObjectDatabase parameterTypeDatabase1,
													StorableObjectDatabase measurementTypeDatabase1,
													StorableObjectDatabase analysisTypeDatabase1,
													StorableObjectDatabase evaluationTypeDatabase1,
													StorableObjectDatabase setDatabase1,
													StorableObjectDatabase measurementSetupDatabase1,
													StorableObjectDatabase measurementDatabase1,
													StorableObjectDatabase analysisDatabase1,
													StorableObjectDatabase evaluationDatabase1,
													StorableObjectDatabase testDatabase1,
													StorableObjectDatabase resultDatabase1,
													StorableObjectDatabase temporalPatternDatabase1) {
		parameterTypeDatabase = parameterTypeDatabase1;
		measurementTypeDatabase = measurementTypeDatabase1;
		analysisTypeDatabase = analysisTypeDatabase1;
		evaluationTypeDatabase = evaluationTypeDatabase1;

		setDatabase = setDatabase1;
		measurementSetupDatabase = measurementSetupDatabase1;
		measurementDatabase = measurementDatabase1;
		analysisDatabase = analysisDatabase1;
		evaluationDatabase = evaluationDatabase1;
		testDatabase = testDatabase1;
		resultDatabase = resultDatabase1;
		temporalPatternDatabase = temporalPatternDatabase1;
		
	}
}
