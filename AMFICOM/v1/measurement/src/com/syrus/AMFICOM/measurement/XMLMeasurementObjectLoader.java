/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.9 2005/02/25 06:52:26 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/25 06:52:26 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class XMLMeasurementObjectLoader implements MeasurementObjectLoader {

	private StorableObjectXML	measurementXML;

	public XMLMeasurementObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "measurement");
		this.measurementXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws IllegalDataException {
		this.measurementXML.delete(id);
		this.measurementXML.flush();
	}

	public void delete(Collection ids) throws IllegalDataException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.measurementXML.delete(id);
		}
		this.measurementXML.flush();
	}

	public Collection loadAnalyses(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Analysis loadAnalysis(Identifier id) throws ApplicationException {
		return (Analysis) this.loadStorableObject(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		return (AnalysisType) this.loadStorableObject(id);
	}

	public Collection loadAnalysisTypes(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Evaluation loadEvaluation(Identifier id) throws ApplicationException {
		return (Evaluation) this.loadStorableObject(id);
	}

	public Collection loadEvaluations(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		return (EvaluationType) this.loadStorableObject(id);
	}

	public Collection loadEvaluationTypes(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Measurement loadMeasurement(Identifier id) throws ApplicationException {
		return (Measurement) this.loadStorableObject(id);
	}

	public Collection loadMeasurements(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		return (MeasurementSetup) this.loadStorableObject(id);
	}

	public Collection loadMeasurementSetups(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		return (MeasurementType) this.loadStorableObject(id);
	}

	public Collection loadMeasurementTypes(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		return (Modeling) this.loadStorableObject(id);
	}

	public Collection loadModelings(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		return (ModelingType) this.loadStorableObject(id);
	}

	public Collection loadModelingTypes(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		return (Result) this.loadStorableObject(id);
	}

	public Collection loadResults(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		return (Set) this.loadStorableObject(id);
	}

	public Collection loadSets(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws ApplicationException {
		return (TemporalPattern) this.loadStorableObject(id);
	}

	public Collection loadTemporalPatterns(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		return (Test) this.loadStorableObject(id);
	}

	public Collection loadTests(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveAnalyses(Collection list, boolean force) throws ApplicationException {
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

	public void saveAnalysisTypes(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException {
		this.saveStorableObject(evaluation, force);
		this.measurementXML.flush();
	}

	public void saveEvaluations(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		this.saveStorableObject(evaluationType, force);
		this.measurementXML.flush();

	}

	public void saveEvaluationTypes(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException {
		this.saveStorableObject(measurement, force);
		this.measurementXML.flush();
	}

	public void saveMeasurements(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws ApplicationException {
		this.saveStorableObject(measurementSetup, force);
		this.measurementXML.flush();

	}

	public void saveMeasurementSetups(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		this.saveStorableObject(measurementType, force);
		this.measurementXML.flush();
	}

	public void saveMeasurementTypes(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		this.saveStorableObject(modeling, force);
		this.measurementXML.flush();
	}

	public void saveModelings(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		this.saveStorableObject(modelingType, force);
		this.measurementXML.flush();
	}

	public void saveModelingTypes(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveResult(Result result, boolean force) throws ApplicationException {
		this.saveStorableObject(result, force);
		this.measurementXML.flush();

	}

	public void saveResults(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveSet(Set set, boolean force) throws ApplicationException {
		this.saveStorableObject(set, force);
		this.measurementXML.flush();
	}

	public void saveSets(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws ApplicationException {
		this.saveStorableObject(temporalPattern, force);
		this.measurementXML.flush();

	}

	public void saveTemporalPatterns(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTest(Test test, boolean force) throws ApplicationException {
		this.saveStorableObject(test, force);
		this.measurementXML.flush();

	}

	public void saveTests(Collection list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.measurementXML.retrieve(id);
	}

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.measurementXML.retrieveByCondition(ids, condition);
	}

	private Collection loadStorableObjects(Collection ids) throws ApplicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.measurementXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
	}

	private void saveStorableObjects(Collection storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.measurementXML.flush();
	}
}
