/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.24 2005/03/31 16:01:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/03/31 16:01:56 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	public MeasurementType loadMeasurementType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MeasurementType_Transferable transferable = mServerRef.transmitMeasurementType((Identifier_Transferable) id.getTransferable());
					MeasurementType measurementType = new MeasurementType(transferable);

					try {
						MeasurementDatabaseContext.getMeasurementTypeDatabase().insert(measurementType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return measurementType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MeasurementType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MeasurementType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public AnalysisType loadAnalysisType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new AnalysisType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("AnalysisType '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					AnalysisType_Transferable transferable = mServerRef.transmitAnalysisType((Identifier_Transferable) id.getTransferable());
					AnalysisType analysisType = new AnalysisType(transferable);

					try {
						MeasurementDatabaseContext.getAnalysisTypeDatabase().insert(analysisType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return analysisType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("AnalysisType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve AnalysisType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public EvaluationType loadEvaluationType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new EvaluationType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("EvaluationType '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					EvaluationType_Transferable transferable = mServerRef.transmitEvaluationType((Identifier_Transferable) id.getTransferable());
					EvaluationType evaluationType = new EvaluationType(transferable);

					try {
						MeasurementDatabaseContext.getEvaluationTypeDatabase().insert(evaluationType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return evaluationType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("EvaluationType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve EvaluationType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public Set loadSet(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Set(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Set '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Set_Transferable transferable = mServerRef.transmitSet((Identifier_Transferable) id.getTransferable());
					Set set = new Set(transferable);

					try {
						MeasurementDatabaseContext.getSetDatabase().insert(set);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return set;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Set '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve Set '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementSetup(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MeasurementSetup_Transferable transferable = mServerRef.transmitMeasurementSetup((Identifier_Transferable) id.getTransferable());
					MeasurementSetup measurementSetup = new MeasurementSetup(transferable);

					try {
						MeasurementDatabaseContext.getMeasurementSetupDatabase().insert(measurementSetup);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return measurementSetup;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MeasurementSetup '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MeasurementSetup '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public TemporalPattern loadTemporalPattern(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TemporalPattern(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("TemporalPattern '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					TemporalPattern_Transferable transferable = mServerRef.transmitTemporalPattern((Identifier_Transferable) id.getTransferable());
					TemporalPattern temporalPattern = new TemporalPattern(transferable);

					try {
						MeasurementDatabaseContext.getTemporalPatternDatabase().insert(temporalPattern);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return temporalPattern;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("TemporalPattern '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve TemporalPattern '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}




	public Collection loadAnalysisTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadEvaluationTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMeasurementSetups(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMeasurementTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadSets(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadTemporalPatterns(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}




	/**
	 * This method actually updates status of test, nothing more
	 */
	public void saveTest(Test test, boolean force) throws ApplicationException {
		super.saveTest(test, force);

		try {
			MeasurementControlModule.mServerRef.updateTestStatus((Identifier_Transferable) test.getId().getTransferable(),
					test.getStatus(),
					(Identifier_Transferable) MeasurementControlModule.iAm.getId().getTransferable());
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
	}

	public void saveTests(Collection collection, boolean force) throws ApplicationException {
		if (collection == null || collection.isEmpty())
			return;

		super.saveTests(collection, force);
		
		Identifier_Transferable[] idsT = Identifier.createTransferables(collection);

		TestStatus status = ((Test) collection.iterator().next()).getStatus();

		try {
			MeasurementControlModule.mServerRef.updateTestsStatus(idsT,
					status,
					(Identifier_Transferable) MeasurementControlModule.iAm.getId().getTransferable());
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
	}




	/*
	 * MCM do not need in all below methods
	 * */

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, id: " + id);
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, id: " + id);
	}





	public Collection loadModelingTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids);
	}

	public Collection loadModelings(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids);
	}




	
	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids + ", condition: " + condition);
	}

	



	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, analysisType: " + analysisType + ", force: " + force);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, evaluationType: " + evaluationType + ", force: " + force);
	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, measurementType: " + measurementType + ", force: " + force);
	}
	
	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, modelingType: " + modelingType + ", force: " + force);
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, measurementSetup: " + measurementSetup + ", force: " + force);
	}
	
	public void saveSet(Set set, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, set: " + set + ", force: " + force);
	}
	
	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, modeling: " + modeling + ", force: " + force);
	}
	
	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, temporalPattern: " + temporalPattern + ", force: " + force);
	}




	public void saveAnalysisTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveEvaluationTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementSetups(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveModelings(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveSets(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveTemporalPatterns(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}




	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}




	public void delete(Collection objects) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + objects);
	}
	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
