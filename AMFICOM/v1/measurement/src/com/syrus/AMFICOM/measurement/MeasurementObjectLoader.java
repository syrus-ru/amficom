/*
 * $Id: MeasurementObjectLoader.java,v 1.6 2004/09/28 06:45:44 bob Exp $
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
 * @version $Revision: 1.6 $, $Date: 2004/09/28 06:45:44 $
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
	
	Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException;

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
	
	List loadModelings(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurements(List ids) throws DatabaseException, CommunicationException;

	List loadAnalyses(List ids) throws DatabaseException, CommunicationException;

	List loadEvaluations(List ids) throws DatabaseException, CommunicationException;

	List loadTests(List ids) throws DatabaseException, CommunicationException;

	List loadResults(List ids) throws DatabaseException, CommunicationException;

	List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException;
	
	public void saveParameterType(ParameterType parameterType) throws DatabaseException, CommunicationException;

	public void saveMeasurementType(MeasurementType measurementType) throws DatabaseException, CommunicationException;

	public void saveAnalysisType(AnalysisType analysisType) throws DatabaseException, CommunicationException;

	public void saveEvaluationType(EvaluationType evaluationType) throws DatabaseException, CommunicationException;

	public void saveSet(Set set) throws DatabaseException, CommunicationException;

	public void saveMeasurementSetup(MeasurementSetup measurementSetup) throws DatabaseException, CommunicationException;

	public void saveModeling(Modeling modeling) throws DatabaseException, CommunicationException;

	public void saveMeasurement(Measurement measurement) throws DatabaseException, CommunicationException;

	public void saveAnalysis(Analysis analysis) throws DatabaseException, CommunicationException;

	public void saveEvaluation(Evaluation evaluation) throws DatabaseException, CommunicationException;

	public void saveTest(Test test) throws DatabaseException, CommunicationException;

	public void saveResult(Result result) throws DatabaseException, CommunicationException;

	public void saveTemporalPattern(TemporalPattern temporalPattern) throws DatabaseException, CommunicationException;

	public void saveParameterTypes(List list) throws DatabaseException, CommunicationException;

	public void saveMeasurementTypes(List list) throws DatabaseException, CommunicationException;

	public void saveAnalysisTypes(List list) throws DatabaseException, CommunicationException;

	public void saveEvaluationTypes(List list) throws DatabaseException, CommunicationException;

	public void saveSets(List list) throws DatabaseException, CommunicationException;

	public void saveModelings(List list) throws DatabaseException, CommunicationException;

	public void saveMeasurementSetups(List list) throws DatabaseException, CommunicationException;

	public void saveMeasurements(List list) throws DatabaseException, CommunicationException;

	public void saveAnalyses(List list) throws DatabaseException, CommunicationException;

	public void saveEvaluations(List list) throws DatabaseException, CommunicationException;

	public void saveTests(List list) throws DatabaseException, CommunicationException;

	public void saveResults(List list) throws DatabaseException, CommunicationException;

	public void saveTemporalPatterns(List list) throws DatabaseException, CommunicationException;

}
