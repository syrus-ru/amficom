/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.4 2005/02/11 11:55:22 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/11 11:55:22 $
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

	public void delete(List ids) throws CommunicationException, DatabaseException {
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

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException {
		return (Analysis) this.loadStorableObject(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		return (AnalysisType) this.loadStorableObject(id);
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadAnalysisTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException {
		return (Evaluation) this.loadStorableObject(id);
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException {
		return (EvaluationType) this.loadStorableObject(id);
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadEvaluationTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException {
		return (Measurement) this.loadStorableObject(id);
	}

	public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementSetup) this.loadStorableObject(id);
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementType) this.loadStorableObject(id);
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		return (Modeling) this.loadStorableObject(id);
	}

	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadModelingsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException {
		return (ModelingType) this.loadStorableObject(id);
	}

	public List loadModelingTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadModelingTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Result loadResult(Identifier id) throws DatabaseException, CommunicationException {
		return (Result) this.loadStorableObject(id);
	}

	public List loadResults(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSet(Identifier id) throws DatabaseException, CommunicationException {
		return (Set) this.loadStorableObject(id);
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadSetsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException {
		return (TemporalPattern) this.loadStorableObject(id);
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadTemporalPatternsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Test loadTest(Identifier id) throws DatabaseException, CommunicationException {
		return (Test) this.loadStorableObject(id);
	}

	public List loadTests(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveAnalyses(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveAnalysis(Analysis analysis, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(analysis, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveAnalysisType(AnalysisType analysisType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(analysisType, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveAnalysisTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveEvaluation(Evaluation evaluation, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluation, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveEvaluations(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveEvaluationType(EvaluationType evaluationType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluationType, accessIdentity);
		this.measurementXML.flush();

	}

	public void saveEvaluationTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveMeasurement(Measurement measurement, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurement, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveMeasurements(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, AccessIdentity accessIdentity, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementSetup, accessIdentity);
		this.measurementXML.flush();

	}

	public void saveMeasurementSetups(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveMeasurementType(MeasurementType measurementType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementType, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveMeasurementTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveModeling(Modeling modeling, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(modeling, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveModelings(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveModelingType(ModelingType modelingType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(modelingType, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveModelingTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveResult(Result result, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(result, accessIdentity);
		this.measurementXML.flush();

	}

	public void saveResults(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveSet(Set set, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(set, accessIdentity);
		this.measurementXML.flush();
	}

	public void saveSets(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(temporalPattern, accessIdentity);
		this.measurementXML.flush();

	}

	public void saveTemporalPatterns(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveTest(Test test, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(test, accessIdentity);
		this.measurementXML.flush();

	}

	public void saveTests(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
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

	private List loadStorableObjectButIds(StorableObjectCondition condition, List ids) throws CommunicationException {
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

	private List loadStorableObjects(List ids) throws CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private void saveStorableObject(StorableObject storableObject, AccessIdentity accessIdentity) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.measurementXML.updateObject(storableObject, accessIdentity.getUserId());
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

	private void saveStorableObjects(List storableObjects, AccessIdentity accessIdentity) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, accessIdentity);
		}
		this.measurementXML.flush();
	}
}
