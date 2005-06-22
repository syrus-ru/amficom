/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.12 2005/06/22 19:29:32 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/22 19:29:32 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseMeasurementObjectLoader extends DatabaseObjectLoader implements MeasurementObjectLoader {

	/* Load multiple objects*/

	public Set loadMeasurementTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadAnalysisTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEvaluationTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadModelingTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}



	public Set loadMeasurements(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadAnalyses(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEvaluations(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadModelings(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMeasurementSetups(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadParameterSets(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadResults(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadTests(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCronTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}
	
	public Set loadIntervalsTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPeriodicalTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadMeasurementTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadAnalysisTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadEvaluationTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadModelingTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}



	public Set loadMeasurementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadAnalysesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadEvaluationsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadModelingsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMeasurementSetupsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadParameterSetsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadResultsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadTestsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCronTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadPeriodicalTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(final Set<MeasurementType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveAnalysisTypes(final Set<AnalysisType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvaluationTypes(final Set<EvaluationType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveModelingTypes(final Set<ModelingType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}



	public void saveMeasurements(final Set<Measurement> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveAnalyses(final Set<Analysis> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvaluations(final Set<Evaluation> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveModelings(final Set<Modeling> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementSetups(final Set<MeasurementSetup> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveParameterSets(final Set<ParameterSet> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveResults(final Set<Result> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTests(final Set<Test> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCronTemporalPatterns(final Set<CronTemporalPattern> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveIntervalsTemporalPatterns(final Set<IntervalsTemporalPattern> objects, final boolean force) throws ApplicationException{
		saveStorableObjects(objects, force);
	}
	
	public void savePeriodicalTemporalPatterns(final Set<PeriodicalTemporalPattern> objects, final boolean force) throws ApplicationException{
		saveStorableObjects(objects, force);
	}
}
