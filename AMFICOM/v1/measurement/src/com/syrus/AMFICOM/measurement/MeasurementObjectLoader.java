/*
 * $Id: MeasurementObjectLoader.java,v 1.10 2004/10/01 14:01:15 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.10 $, $Date: 2004/10/01 14:01:15 $
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
	
	/* Load Measurement StorableObject but argument ids */
	
	List loadParameterTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadAnalysisTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadEvaluationTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadSetsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;
	
	List loadModelingsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadTemporalPatternsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;
	
	public void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveParameterTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveAnalysisTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEvaluationTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveSets(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveModelings(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementSetups(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurements(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveAnalyses(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEvaluations(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTests(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveResults(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTemporalPatterns(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

}
