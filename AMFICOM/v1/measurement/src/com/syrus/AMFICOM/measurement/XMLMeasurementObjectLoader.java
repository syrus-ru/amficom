/*
 * $Id: XMLMeasurementObjectLoader.java,v 1.2 2005/02/02 06:15:26 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/02/02 06:15:26 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class XMLMeasurementObjectLoader implements MeasurementObjectLoader {

	private StorableObjectXML	measurementXML;

	public XMLMeasurementObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "measurement");
		this.measurementXML = new StorableObjectXML(driver);
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

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementType) this.loadStorableObject(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		return (AnalysisType) this.loadStorableObject(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException {
		return (EvaluationType) this.loadStorableObject(id);
	}

	public Set loadSet(Identifier id) throws DatabaseException, CommunicationException {
		return (Set) this.loadStorableObject(id);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementSetup) this.loadStorableObject(id);
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		return (Modeling) this.loadStorableObject(id);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException {
		return (Measurement) this.loadStorableObject(id);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException {
		return (Analysis) this.loadStorableObject(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException {
		return (Evaluation) this.loadStorableObject(id);
	}

	public Test loadTest(Identifier id) throws DatabaseException, CommunicationException {
		return (Test) this.loadStorableObject(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException, CommunicationException {
		return (Result) this.loadStorableObject(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException {
		return (TemporalPattern) this.loadStorableObject(id);
	}

	private List loadStorableObjects(List ids) throws CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadTests(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadResults(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		return this.loadStorableObjects(ids);
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

	private void saveStorableObject(StorableObject storableObject) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.measurementXML.updateObject(storableObject);
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

	public List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadAnalysisTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadEvaluationTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadSetsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadModelingsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadTemporalPatternsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementType);
		this.measurementXML.flush();
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(analysisType);
		this.measurementXML.flush();
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluationType);
		this.measurementXML.flush();

	}

	public void saveSet(Set set, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(set);
		this.measurementXML.flush();
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementSetup);
		this.measurementXML.flush();

	}

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(modeling);
		this.measurementXML.flush();
	}

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurement);
		this.measurementXML.flush();
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(analysis);
		this.measurementXML.flush();
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(evaluation);
		this.measurementXML.flush();
	}

	public void saveTest(Test test, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(test);
		this.measurementXML.flush();

	}

	public void saveResult(Result result, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(result);
		this.measurementXML.flush();

	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(temporalPattern);
		this.measurementXML.flush();

	}

	private void saveStorableObjects(List storableObjects) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject);
		}
		this.measurementXML.flush();
	}

	public void saveMeasurementTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveAnalysisTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveEvaluationTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveSets(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveModelings(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveMeasurementSetups(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveMeasurements(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);

	}

	public void saveAnalyses(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveEvaluations(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveTests(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveResults(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveTemporalPatterns(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
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
}
