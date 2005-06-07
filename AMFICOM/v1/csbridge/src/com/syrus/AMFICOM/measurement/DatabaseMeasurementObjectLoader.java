/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.10 2005/06/07 13:18:51 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/07 13:18:51 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseMeasurementObjectLoader extends DatabaseObjectLoader implements MeasurementObjectLoader {

	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}
	
	public java.util.Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public java.util.Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveIntervalsTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		saveStorableObjects(objects, force);
	}
	
	public void savePeriodicalTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		saveStorableObjects(objects, force);
	}
}
