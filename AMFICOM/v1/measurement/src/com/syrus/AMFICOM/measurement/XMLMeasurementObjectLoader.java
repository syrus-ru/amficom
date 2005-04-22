/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.13 2005/04/22 16:04:39 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.13 $, $Date: 2005/04/22 16:04:39 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class XMLMeasurementObjectLoader implements MeasurementObjectLoader {

	private StorableObjectXML	measurementXML;

	public XMLMeasurementObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "measurement");
		this.measurementXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) {
		this.measurementXML.delete(id);
		this.measurementXML.flush();
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

	public Analysis loadAnalysis(Identifier id) throws ApplicationException {
		return (Analysis) this.loadStorableObject(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		return (AnalysisType) this.loadStorableObject(id);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Evaluation loadEvaluation(Identifier id) throws ApplicationException {
		return (Evaluation) this.loadStorableObject(id);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		return (EvaluationType) this.loadStorableObject(id);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Measurement loadMeasurement(Identifier id) throws ApplicationException {
		return (Measurement) this.loadStorableObject(id);
	}

	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		return (MeasurementSetup) this.loadStorableObject(id);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		return (MeasurementType) this.loadStorableObject(id);
	}

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		return (Modeling) this.loadStorableObject(id);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		return (ModelingType) this.loadStorableObject(id);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		return (Result) this.loadStorableObject(id);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		return (Set) this.loadStorableObject(id);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CronTemporalPattern loadCronTemporalPattern(Identifier id) throws ApplicationException {
		return (CronTemporalPattern) this.loadStorableObject(id);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		return (Test) this.loadStorableObject(id);
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveAnalyses(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException {
		this.saveStorableObject(analysis, force);
		this.measurementXML.flush();
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException {
		this.saveStorableObject(analysisType, force);
		this.measurementXML.flush();
	}

	public void saveAnalysisTypes(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException {
		this.saveStorableObject(evaluation, force);
		this.measurementXML.flush();
	}

	public void saveEvaluations(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		this.saveStorableObject(evaluationType, force);
		this.measurementXML.flush();

	}

	public void saveEvaluationTypes(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException {
		this.saveStorableObject(measurement, force);
		this.measurementXML.flush();
	}

	public void saveMeasurements(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws ApplicationException {
		this.saveStorableObject(measurementSetup, force);
		this.measurementXML.flush();

	}

	public void saveMeasurementSetups(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		this.saveStorableObject(measurementType, force);
		this.measurementXML.flush();
	}

	public void saveMeasurementTypes(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		this.saveStorableObject(modeling, force);
		this.measurementXML.flush();
	}

	public void saveModelings(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		this.saveStorableObject(modelingType, force);
		this.measurementXML.flush();
	}

	public void saveModelingTypes(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveResult(Result result, boolean force) throws ApplicationException {
		this.saveStorableObject(result, force);
		this.measurementXML.flush();

	}

	public void saveResults(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveSet(Set set, boolean force) throws ApplicationException {
		this.saveStorableObject(set, force);
		this.measurementXML.flush();
	}

	public void saveSets(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveCronTemporalPattern(CronTemporalPattern cronTemporalPattern, boolean force) throws ApplicationException {
		this.saveStorableObject(cronTemporalPattern, force);
		this.measurementXML.flush();

	}

	public void saveCronTemporalPatterns(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTest(Test test, boolean force) throws ApplicationException {
		this.saveStorableObject(test, force);
		this.measurementXML.flush();

	}

	public void saveTests(java.util.Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.measurementXML.retrieve(id);
	}

	private java.util.Set loadStorableObjectButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return this.measurementXML.retrieveByCondition(ids, condition);
	}

	private java.util.Set loadStorableObjects(java.util.Set ids) throws ApplicationException {
		java.util.Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.measurementXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
	}

	private void saveStorableObjects(java.util.Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.measurementXML.flush();
	}
}
