/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.2 2005/04/27 15:39:00 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 15:39:00 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseMeasurementObjectLoader extends DatabaseObjectLoader implements MeasurementObjectLoader {

	/* Load multiple objects*/

	private java.util.Set loadStorableObjects(java.util.Set ids) throws ApplicationException {
		if (ids.isEmpty())
			return Collections.EMPTY_SET;
		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(((Identifier)ids.iterator().next()).getMajor());
		return super.retrieveFromDatabase(database, ids);
	}
	
	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}



	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}
	
	public java.util.Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}



	/* Load multiple objects but ids*/
	private java.util.Set loadStorableObjectsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		if (ids.isEmpty())
			return Collections.EMPTY_SET;
		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(((Identifier)ids.iterator().next()).getMajor());
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}
	
	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}



	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		return this.loadStorableObjectsButIds(condition, ids);
	}


	/* Save single object*/

	private void saveStorableObjects(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(((Identifier)storableObjects.iterator().next()).getMajor());
		database.update(storableObjects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}	

	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveAnalysisTypes(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveEvaluationTypes(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveModelingTypes(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}



	public void saveMeasurements(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveAnalyses(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveEvaluations(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveModelings(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveMeasurementSetups(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveSets(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveResults(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveTests(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveCronTemporalPatterns(java.util.Set storableObjects, boolean force) throws ApplicationException {
		this.saveStorableObjects(storableObjects, force);
	}

	public void saveIntervalsTemporalPatterns(Set storableObjects,boolean force) throws ApplicationException{
		this.saveStorableObjects(storableObjects, force);
	}
	
	public void savePeriodicalTemporalPatterns(Set storableObjects,boolean force) throws ApplicationException{
		this.saveStorableObjects(storableObjects, force);
	}


	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}



	public void delete(Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(final java.util.Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			java.util.Set entityObjects = (java.util.Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final java.util.Set entityObjects = (java.util.Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
