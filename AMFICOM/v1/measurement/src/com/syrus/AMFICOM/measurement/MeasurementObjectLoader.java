/*
 * $Id: MeasurementObjectLoader.java,v 1.34 2005/06/22 19:19:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.34 $, $Date: 2005/06/22 19:19:22 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	/* Load multiple objects*/

	Set loadMeasurementTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadAnalysisTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadEvaluationTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadModelingTypes(final Set<Identifier> ids) throws ApplicationException;



	Set loadMeasurements(final Set<Identifier> ids) throws ApplicationException;

	Set loadAnalyses(final Set<Identifier> ids) throws ApplicationException;

	Set loadEvaluations(final Set<Identifier> ids) throws ApplicationException;

	Set loadModelings(final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementSetups(final Set<Identifier> ids) throws ApplicationException;

	Set loadResults(final Set<Identifier> ids) throws ApplicationException;

	Set loadParameterSets(final Set<Identifier> ids) throws ApplicationException;

	Set loadTests(final Set<Identifier> ids) throws ApplicationException;

	Set loadCronTemporalPatterns(final Set<Identifier> ids) throws ApplicationException;
	
	Set loadIntervalsTemporalPatterns(final Set<Identifier> ids) throws ApplicationException;
	
	Set loadPeriodicalTemporalPatterns(final Set<Identifier> ids) throws ApplicationException;


	/* Load multiple objects but ids*/

	Set loadMeasurementTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadAnalysisTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadEvaluationTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadModelingTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	Set loadMeasurementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadAnalysesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadEvaluationsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadModelingsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementSetupsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadResultsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadParameterSetsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadTestsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCronTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadIntervalsTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;
	
	Set loadPeriodicalTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;


	/* Save multiple objects*/

	void saveMeasurementTypes(final Set<MeasurementType> objects, final boolean force) throws ApplicationException;

	void saveAnalysisTypes(final Set<AnalysisType> objects, final boolean force) throws ApplicationException;

	void saveEvaluationTypes(final Set<EvaluationType> objects, final boolean force) throws ApplicationException;

	void saveModelingTypes(final Set<ModelingType> objects, final boolean force) throws ApplicationException;



	void saveMeasurements(final Set<Measurement> objects, final boolean force) throws ApplicationException;

	void saveAnalyses(final Set<Analysis> objects, final boolean force) throws ApplicationException;

	void saveEvaluations(final Set<Evaluation> objects, final boolean force) throws ApplicationException;

	void saveModelings(final Set<Modeling> objects, final boolean force) throws ApplicationException;

	void saveMeasurementSetups(final Set<MeasurementSetup> objects, final boolean force) throws ApplicationException;

	void saveResults(final Set<Result> objects, final boolean force) throws ApplicationException;

	void saveParameterSets(final Set<ParameterSet> objects, final boolean force) throws ApplicationException;

	void saveTests(final Set<Test> objects, final boolean force) throws ApplicationException;

	void saveCronTemporalPatterns(final Set<CronTemporalPattern> objects, final boolean force) throws ApplicationException;

	void saveIntervalsTemporalPatterns(final Set<IntervalsTemporalPattern> objects, final boolean force) throws ApplicationException;
	
	void savePeriodicalTemporalPatterns(final Set<PeriodicalTemporalPattern> objects, final boolean force) throws ApplicationException;
	


	/*	Refresh*/

	Set refresh(Set<? extends StorableObject> storableObjects) throws ApplicationException;



	/*	Delete*/

	void delete(final Set<? extends Identifiable> identifiables);

}
