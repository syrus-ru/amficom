/*
 * $Id: MeasurementObjectLoader.java,v 1.2 2004/08/09 10:25:17 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/09 10:25:17 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	ParameterType loadParameterType(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException;

	AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException;

	EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException;

	Set loadSet(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException;

	Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException;

	Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException;

	Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException;

	Test loadTest(Identifier id) throws DatabaseException, CommunicationException;

	Result loadResult(Identifier id) throws DatabaseException, CommunicationException;

	TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException;
}
