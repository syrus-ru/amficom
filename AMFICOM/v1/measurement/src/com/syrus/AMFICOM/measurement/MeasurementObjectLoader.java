/*
 * $Id: MeasurementObjectLoader.java,v 1.18 2005/02/11 11:04:55 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.18 $, $Date: 2005/02/11 11:04:55 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException;

	List loadAnalyses(List ids) throws DatabaseException, CommunicationException;

	List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException;

	AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException;

	List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException;

	List loadAnalysisTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException;

	List loadEvaluations(List ids) throws DatabaseException, CommunicationException;

	List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException;

	List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException;

	List loadEvaluationTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException;

	List loadMeasurements(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException;

	List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException;

	List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException;

	/* Load Measurement StorableObject but argument ids */

	List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException;

	List loadModelings(List ids) throws DatabaseException, CommunicationException;

	List loadModelingsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException;

	List loadModelingTypes(List ids) throws DatabaseException, CommunicationException;

	List loadModelingTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Result loadResult(Identifier id) throws DatabaseException, CommunicationException;

	List loadResults(List ids) throws DatabaseException, CommunicationException;

	List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Set loadSet(Identifier id) throws DatabaseException, CommunicationException;

	List loadSets(List ids) throws DatabaseException, CommunicationException;

	List loadSetsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException;

	List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException;

	List loadTemporalPatternsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Test loadTest(Identifier id) throws DatabaseException, CommunicationException;

	List loadTests(List ids) throws DatabaseException, CommunicationException;

	List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;

	void saveAnalyses(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveAnalysis(Analysis analysis, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveAnalysisType(AnalysisType analysisType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveAnalysisTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluation(Evaluation evaluation, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluations(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEvaluationType(EvaluationType evaluationType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveEvaluationTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurement(Measurement measurement, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurements(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementSetup(MeasurementSetup measurementSetup, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementSetups(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementType(MeasurementType measurementType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModeling(Modeling modeling, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModelings(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveModelingType(ModelingType modelingType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveModelingTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveResult(Result result, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveResults(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSet(Set set, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveSets(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTemporalPattern(TemporalPattern temporalPattern, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveTemporalPatterns(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTest(Test test, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTests(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

}
