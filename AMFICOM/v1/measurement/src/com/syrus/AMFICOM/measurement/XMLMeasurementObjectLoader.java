/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.6 2005/02/11 16:31:48 bob Exp $
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

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/11 16:31:48 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class XMLMeasurementObjectLoader implements MeasurementObjectLoader {

	private StorableObjectXML	measurementXML;

	public XMLMeasurementObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "measurement");
		this.measurementXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.measurementXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.measurementXML.flush();
	}

	public void delete(Collection ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.measurementXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.measurementXML.flush();
	}

	public Collection loadAnalyses(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException {
		return (Analysis) this.loadStorableObject(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		return (AnalysisType) this.loadStorableObject(id);
	}

	public Collection loadAnalysisTypes(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException {
		return (Evaluation) this.loadStorableObject(id);
	}

	public Collection loadEvaluations(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException {
		return (EvaluationType) this.loadStorableObject(id);
	}

	public Collection loadEvaluationTypes(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException {
		return (Measurement) this.loadStorableObject(id);
	}

	public Collection loadMeasurements(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementSetup) this.loadStorableObject(id);
	}

	public Collection loadMeasurementSetups(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementType) this.loadStorableObject(id);
	}

	public Collection loadMeasurementTypes(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		return (Modeling) this.loadStorableObject(id);
	}

	public Collection loadModelings(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException {
		return (ModelingType) this.loadStorableObject(id);
	}

	public Collection loadModelingTypes(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Result loadResult(Identifier id) throws DatabaseException, CommunicationException {
		return (Result) this.loadStorableObject(id);
	}

	public Collection loadResults(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSet(Identifier id) throws DatabaseException, CommunicationException {
		return (Set) this.loadStorableObject(id);
	}

	public Collection loadSets(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException {
		return (TemporalPattern) this.loadStorableObject(id);
	}

	public Collection loadTemporalPatterns(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Test loadTest(Identifier id) throws DatabaseException, CommunicationException {
		return (Test) this.loadStorableObject(id);
	}

	public Collection loadTests(Collection ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveAnalyses(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(analysis);
		this.measurementXML.flush();
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(analysisType);
		this.measurementXML.flush();
	}

	public void saveAnalysisTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluation);
		this.measurementXML.flush();
	}

	public void saveEvaluations(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluationType);
		this.measurementXML.flush();

	}

	public void saveEvaluationTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurement);
		this.measurementXML.flush();
	}

	public void saveMeasurements(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementSetup);
		this.measurementXML.flush();

	}

	public void saveMeasurementSetups(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementType);
		this.measurementXML.flush();
	}

	public void saveMeasurementTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(modeling);
		this.measurementXML.flush();
	}

	public void saveModelings(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(modelingType);
		this.measurementXML.flush();
	}

	public void saveModelingTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(result);
		this.measurementXML.flush();

	}

	public void saveResults(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(set);
		this.measurementXML.flush();
	}

	public void saveSets(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(temporalPattern);
		this.measurementXML.flush();

	}

	public void saveTemporalPatterns(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(test);
		this.measurementXML.flush();

	}

	public void saveTests(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	private StorableObject loadStorableObject(Identifier id) throws CommunicationException {
		try {
			return this.measurementXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws CommunicationException {
		try {
			return this.measurementXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		}

	}

	private Collection loadStorableObjects(Collection ids) throws CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private void saveStorableObject(StorableObject storableObject) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.measurementXML.updateObject(storableObject, SessionContext.getAccessIdentity().getUserId());
		} catch (UpdateObjectException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new CommunicationException("XMLMeasurementObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}

	}

	private void saveStorableObjects(Collection storableObjects) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject);
		}
		this.measurementXML.flush();
	}
}
