/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.4 2005/05/23 12:56:33 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLObjectLoader;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/23 12:56:33 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class XMLMeasurementObjectLoader extends XMLObjectLoader implements MeasurementObjectLoader {

	private StorableObjectXML	measurementXML;

	public XMLMeasurementObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "measurement");
		this.measurementXML = new StorableObjectXML(driver);
	}

	public void delete(final java.util.Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.measurementXML.delete(id);
		}
		this.measurementXML.flush();
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}
	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}
	
	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}
	
	public Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}
	
	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}
	
	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set refresh(final java.util.Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);
	}	

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}
	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);

	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);
	}
	
	public void saveIntervalsTemporalPatterns(java.util.Set objects,boolean force) throws ApplicationException{
		this.saveStorableObjects(objects, force);
	}
	
	public void savePeriodicalTemporalPatterns(java.util.Set objects,boolean force) throws ApplicationException{
		this.saveStorableObjects(objects, force);
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		this.saveStorableObjects(objects, force);
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.measurementXML.retrieve(id);
	}

	private java.util.Set loadStorableObjectButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.measurementXML.retrieveByCondition(ids, condition);
	}

	private java.util.Set loadStorableObjects(java.util.Set ids) throws ApplicationException {
		java.util.Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.measurementXML.updateObject(storableObject, force);
	}

	private void saveStorableObjects(java.util.Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.measurementXML.flush();
	}
}
