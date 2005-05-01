/*
 * $Id: MeasurementObjectLoader.java,v 1.31 2005/05/01 16:48:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.31 $, $Date: 2005/05/01 16:48:20 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public interface MeasurementObjectLoader {

	/* Load multiple objects*/

	java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException;



	java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelings(java.util.Set ids) throws ApplicationException;

	java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException;

	java.util.Set loadResults(java.util.Set ids) throws ApplicationException;

	java.util.Set loadSets(java.util.Set ids) throws ApplicationException;

	java.util.Set loadTests(java.util.Set ids) throws ApplicationException;

	java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException;
	
	java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws ApplicationException;
	
	java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws ApplicationException;


	/* Load multiple objects but ids*/

	java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;



	java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;

	java.util.Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;
	
	java.util.Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException;


	/* Save multiple objects*/

	void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException;

	void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException;

	void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException;

	void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException;



	void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException;

	void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException;

	void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException;

	void saveModelings(java.util.Set objects, boolean force) throws ApplicationException;

	void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException;

	void saveResults(java.util.Set objects, boolean force) throws ApplicationException;

	void saveSets(java.util.Set objects, boolean force) throws ApplicationException;

	void saveTests(java.util.Set objects, boolean force) throws ApplicationException;

	void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException;

	void saveIntervalsTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException;
	
	void savePeriodicalTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException;
	


	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;



	void delete(final java.util.Set identifiables);

}
