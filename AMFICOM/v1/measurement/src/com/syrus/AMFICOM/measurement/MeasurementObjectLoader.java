/*
 * $Id: MeasurementObjectLoader.java,v 1.3 2004/09/14 14:14:14 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.3 $, $Date: 2004/09/14 14:14:14 $
 * @author $Author: bob $
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
	
	List loadParameterTypes(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException;

	List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException;

	List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException;

	List loadSets(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurements(List ids) throws DatabaseException, CommunicationException;

	List loadAnalyses(List ids) throws DatabaseException, CommunicationException;

	List loadEvaluations(List ids) throws DatabaseException, CommunicationException;

	List loadTests(List ids) throws DatabaseException, CommunicationException;

	List loadResults(List ids) throws DatabaseException, CommunicationException;

	List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException;
	
}
