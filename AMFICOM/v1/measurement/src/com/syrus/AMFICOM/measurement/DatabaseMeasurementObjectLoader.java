/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.1 2004/08/09 10:25:17 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/09 10:25:17 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader implements MeasurementObjectLoader {

	public DatabaseMeasurementObjectLoader() {
	}

	public ParameterType loadParameterType(Identifier id) throws DatabaseException {
		return new ParameterType(id);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException {
		return new EvaluationType(id);
	}

	public Set loadSet(Identifier id) throws DatabaseException {
		return new Set(id);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException {
		return new MeasurementSetup(id);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException {
		return new Measurement(id);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException {
		return new Analysis(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException {
		return new Evaluation(id);
	}

	public Test loadTest(Identifier id) throws DatabaseException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException {
		return new TemporalPattern(id);
	}
}
