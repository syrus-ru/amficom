package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectLoader;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;

public interface MeasurementObjectLoader extends StorableObjectLoader {

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
