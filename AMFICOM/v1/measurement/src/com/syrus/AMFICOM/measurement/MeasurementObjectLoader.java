/*
 * $Id: MeasurementObjectLoader.java,v 1.22 2005/02/18 17:53:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.22 $, $Date: 2005/02/18 17:53:47 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	Collection loadAnalyses(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException;

	AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadAnalysisTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadEvaluations(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadEvaluationTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMeasurements(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMeasurementSetups(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMeasurementTypes(Collection ids) throws DatabaseException, CommunicationException;

	/* Load Measurement StorableObject but argument ids */

	Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadModelings(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadModelingTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Result loadResult(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadResults(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Set loadSet(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadSets(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadTemporalPatterns(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Test loadTest(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadTests(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;

	void saveAnalyses(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveAnalysisTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluations(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveEvaluationTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurements(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementSetups(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModelings(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModelingType(ModelingType modelingType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveModelingTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveResults(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveSets(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveTemporalPatterns(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTests(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;



	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
