/*
 * $Id: MeasurementObjectLoader.java,v 1.27 2005/04/12 16:21:31 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.27 $, $Date: 2005/04/12 16:21:31 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Analysis loadAnalysis(Identifier id) throws ApplicationException;

	AnalysisType loadAnalysisType(Identifier id) throws ApplicationException;

	java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Evaluation loadEvaluation(Identifier id) throws ApplicationException;

	java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	EvaluationType loadEvaluationType(Identifier id) throws ApplicationException;

	java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Measurement loadMeasurement(Identifier id) throws ApplicationException;

	java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException;

	java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException;

	java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException;

	java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	MeasurementType loadMeasurementType(Identifier id) throws ApplicationException;

	java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException;

	/* Load Measurement StorableObject but argument ids */

	java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Modeling loadModeling(Identifier id) throws ApplicationException;

	java.util.Set loadModelings(java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	ModelingType loadModelingType(Identifier id) throws ApplicationException;

	java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Result loadResult(Identifier id) throws ApplicationException;

	java.util.Set loadResults(java.util.Set ids) throws ApplicationException;

	java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Set loadSet(Identifier id) throws ApplicationException;

	java.util.Set loadSets(java.util.Set ids) throws ApplicationException;

	java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	TemporalPattern loadTemporalPattern(Identifier id) throws ApplicationException;

	java.util.Set loadTemporalPatterns(java.util.Set ids) throws ApplicationException;

	java.util.Set loadTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	Test loadTest(Identifier id) throws ApplicationException;

	java.util.Set loadTests(java.util.Set ids) throws ApplicationException;

	java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;



	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;



	void saveAnalyses(java.util.Set list, boolean force) throws ApplicationException;

	void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException;

	void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException;

	void saveAnalysisTypes(java.util.Set list, boolean force) throws ApplicationException;

	void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException;

	void saveEvaluations(java.util.Set list, boolean force) throws ApplicationException;

	void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException;

	void saveEvaluationTypes(java.util.Set list, boolean force) throws ApplicationException;

	void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException;

	void saveMeasurements(java.util.Set list, boolean force) throws ApplicationException;

	void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException;

	void saveMeasurementSetups(java.util.Set list, boolean force) throws ApplicationException;

	void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException;

	void saveMeasurementTypes(java.util.Set list, boolean force) throws ApplicationException;

	void saveModeling(Modeling modeling, boolean force) throws ApplicationException;

	void saveModelings(java.util.Set list, boolean force) throws ApplicationException;

	void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException;

	void saveModelingTypes(java.util.Set list, boolean force) throws ApplicationException;

	void saveResult(Result result, boolean force) throws ApplicationException;

	void saveResults(java.util.Set list, boolean force) throws ApplicationException;

	void saveSet(Set set, boolean force) throws ApplicationException;

	void saveSets(java.util.Set list, boolean force) throws ApplicationException;

	void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws ApplicationException;

	void saveTemporalPatterns(java.util.Set list, boolean force) throws ApplicationException;

	void saveTest(Test test, boolean force) throws ApplicationException;

	void saveTests(java.util.Set list, boolean force) throws ApplicationException;



	void delete(Identifier id);

	void delete(final java.util.Set identifiables);

}
