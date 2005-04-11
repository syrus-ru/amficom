/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.28 2005/04/11 12:42:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2005/04/11 12:42:38 $
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
			Log.debugMessage("MCMMeasurementObjectLoader.loadMeasurementType | MeasurementType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			MeasurementType measurementType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MeasurementType_Transferable transferable = mServerRef.transmitMeasurementType((Identifier_Transferable) id.getTransferable());
				measurementType = new MeasurementType(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadMeasurementType | MeasurementType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MeasurementType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MeasurementType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (measurementType != null) {
				try {
					MeasurementDatabaseContext.getMeasurementTypeDatabase().insert(measurementType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurementType;
			}
			throw new ObjectNotFoundException("MeasurementType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public AnalysisType loadAnalysisType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new AnalysisType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMMeasurementObjectLoader.loadAnalysisType | AnalysisType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			AnalysisType analysisType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				AnalysisType_Transferable transferable = mServerRef.transmitAnalysisType((Identifier_Transferable) id.getTransferable());
				analysisType = new AnalysisType(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadAnalysisType | AnalysisType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("AnalysisType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve AnalysisType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (analysisType != null) {
				try {
					MeasurementDatabaseContext.getAnalysisTypeDatabase().insert(analysisType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return analysisType;
			}
			throw new ObjectNotFoundException("AnalysisType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public EvaluationType loadEvaluationType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new EvaluationType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMMeasurementObjectLoader.loadEvaluationType | EvaluationType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			EvaluationType evaluationType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				EvaluationType_Transferable transferable = mServerRef.transmitEvaluationType((Identifier_Transferable) id.getTransferable());
				evaluationType = new EvaluationType(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadEvaluationType | EvaluationType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("EvaluationType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve EvaluationType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (evaluationType != null) {
				try {
					MeasurementDatabaseContext.getEvaluationTypeDatabase().insert(evaluationType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return evaluationType;
			}
			throw new ObjectNotFoundException("EvaluationType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Set loadSet(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Set(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMMeasurementObjectLoader.loadSet | Set '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Set set = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Set_Transferable transferable = mServerRef.transmitSet((Identifier_Transferable) id.getTransferable());
				set = new Set(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadSet | Set '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Set '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Set '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (set != null) {
				try {
					MeasurementDatabaseContext.getSetDatabase().insert(set);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return set;
			}
			throw new ObjectNotFoundException("Set '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementSetup(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMMeasurementObjectLoader.loadMeasurementSetup | MeasurementSetup '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			MeasurementSetup measurementSetup = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MeasurementSetup_Transferable transferable = mServerRef.transmitMeasurementSetup((Identifier_Transferable) id.getTransferable());
				measurementSetup = new MeasurementSetup(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadMeasurementSetup | MeasurementSetup '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MeasurementSetup '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MeasurementSetup '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (measurementSetup != null) {
				try {
					MeasurementDatabaseContext.getMeasurementSetupDatabase().insert(measurementSetup);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurementSetup;
			}
			throw new ObjectNotFoundException("MeasurementSetup '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public TemporalPattern loadTemporalPattern(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TemporalPattern(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMMeasurementObjectLoader.loadTemporalPattern | TemporalPattern '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			TemporalPattern temporalPattern = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				TemporalPattern_Transferable transferable = mServerRef.transmitTemporalPattern((Identifier_Transferable) id.getTransferable());
				temporalPattern = new TemporalPattern(transferable);
				Log.debugMessage("MCMMeasurementObjectLoader.loadTemporalPattern | TemporalPattern '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("TemporalPattern '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve TemporalPattern '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (temporalPattern != null) {
				try {
					MeasurementDatabaseContext.getTemporalPatternDatabase().insert(temporalPattern);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return temporalPattern;
			}
			throw new ObjectNotFoundException("TemporalPattern '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}





	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws RetrieveObjectException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			MeasurementType_Transferable[] transferables = mServerRef.transmitMeasurementTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws RetrieveObjectException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			AnalysisType_Transferable[] transferables = mServerRef.transmitAnalysisTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new AnalysisType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadAnalysisTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws RetrieveObjectException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			EvaluationType_Transferable[] transferables = mServerRef.transmitEvaluationTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new EvaluationType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadEvaluationTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws RetrieveObjectException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			MeasurementSetup_Transferable[] transferables = mServerRef.transmitMeasurementSetups(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementSetup(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementSetups | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadSets(java.util.Set ids) throws RetrieveObjectException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			Set_Transferable[] transferables = mServerRef.transmitSets(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Set(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadSets | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadTemporalPatterns(java.util.Set ids) throws RetrieveObjectException {
		TemporalPatternDatabase database = MeasurementDatabaseContext.getTemporalPatternDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			TemporalPattern_Transferable[] transferables = mServerRef.transmitTemporalPatterns(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new TemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadTemporalPatterns | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}





	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}




	/**
	 * This method actually updates status of test, nothing more
	 */
	public void saveTest(Test test, boolean force) throws ApplicationException {
		super.saveTest(test, force);

		MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
		try {
			mServerRef.updateTest((Test_Transferable) test.getTransferable(),
					(AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}
	}

	public void saveTests(java.util.Set collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("May be not need this? " + collection + ", " + force);
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





	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids);
	}




	
	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
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




	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}




	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}




	public void delete(java.util.Set objects) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + objects);
	}
	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
