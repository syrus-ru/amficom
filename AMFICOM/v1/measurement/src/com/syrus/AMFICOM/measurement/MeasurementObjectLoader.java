/*
 * $Id: MeasurementObjectLoader.java,v 1.23 2005/02/24 14:59:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.23 $, $Date: 2005/02/24 14:59:59 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	Collection loadAnalyses(Collection ids) throws ApplicationException;

	Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Analysis loadAnalysis(Identifier id) throws ApplicationException;

	AnalysisType loadAnalysisType(Identifier id) throws ApplicationException;

	Collection loadAnalysisTypes(Collection ids) throws ApplicationException;

	Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Evaluation loadEvaluation(Identifier id) throws ApplicationException;

	Collection loadEvaluations(Collection ids) throws ApplicationException;

	Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	EvaluationType loadEvaluationType(Identifier id) throws ApplicationException;

	Collection loadEvaluationTypes(Collection ids) throws ApplicationException;

	Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Measurement loadMeasurement(Identifier id) throws ApplicationException;

	Collection loadMeasurements(Collection ids) throws ApplicationException;

	Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException;

	Collection loadMeasurementSetups(Collection ids) throws ApplicationException;

	Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	MeasurementType loadMeasurementType(Identifier id) throws ApplicationException;

	Collection loadMeasurementTypes(Collection ids) throws ApplicationException;

	/* Load Measurement StorableObject but argument ids */

	Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Modeling loadModeling(Identifier id) throws ApplicationException;

	Collection loadModelings(Collection ids) throws ApplicationException;

	Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	ModelingType loadModelingType(Identifier id) throws ApplicationException;

	Collection loadModelingTypes(Collection ids) throws ApplicationException;

	Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Result loadResult(Identifier id) throws ApplicationException;

	Collection loadResults(Collection ids) throws ApplicationException;

	Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Set loadSet(Identifier id) throws ApplicationException;

	Collection loadSets(Collection ids) throws ApplicationException;

	Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	TemporalPattern loadTemporalPattern(Identifier id) throws ApplicationException;

	Collection loadTemporalPatterns(Collection ids) throws ApplicationException;

	Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Test loadTest(Identifier id) throws ApplicationException;

	Collection loadTests(Collection ids) throws ApplicationException;

	Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;



	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;



	void saveAnalyses(Collection list, boolean force) throws ApplicationException;

	void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException;

	void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException;

	void saveAnalysisTypes(Collection list, boolean force) throws ApplicationException;

	void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException;

	void saveEvaluations(Collection list, boolean force) throws ApplicationException;

	void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException;

	void saveEvaluationTypes(Collection list, boolean force) throws ApplicationException;

	void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException;

	void saveMeasurements(Collection list, boolean force) throws ApplicationException;

	void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException;

	void saveMeasurementSetups(Collection list, boolean force) throws ApplicationException;

	void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException;

	void saveMeasurementTypes(Collection list, boolean force) throws ApplicationException;

	void saveModeling(Modeling modeling, boolean force) throws ApplicationException;

	void saveModelings(Collection list, boolean force) throws ApplicationException;

	void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException;

	void saveModelingTypes(Collection list, boolean force) throws ApplicationException;

	void saveResult(Result result, boolean force) throws ApplicationException;

	void saveResults(Collection list, boolean force) throws ApplicationException;

	void saveSet(Set set, boolean force) throws ApplicationException;

	void saveSets(Collection list, boolean force) throws ApplicationException;

	void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws ApplicationException;

	void saveTemporalPatterns(Collection list, boolean force) throws ApplicationException;

	void saveTest(Test test, boolean force) throws ApplicationException;

	void saveTests(Collection list, boolean force) throws ApplicationException;



	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
