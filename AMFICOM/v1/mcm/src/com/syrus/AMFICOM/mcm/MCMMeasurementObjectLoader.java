/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.17 2005/03/05 21:37:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
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
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/03/05 21:37:45 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementType measurementType = null;
		try {
			measurementType = new MeasurementType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				measurementType = new MeasurementType(MeasurementControlModule.mServerRef.transmitMeasurementType((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getMeasurementTypeDatabase().update(measurementType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Measurement type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve measurement type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return measurementType;
	}

	public AnalysisType loadAnalysisType(Identifier id) throws RetrieveObjectException, CommunicationException {
		AnalysisType analysisType = null;
		try {
			analysisType = new AnalysisType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("AnalysisType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				analysisType = new AnalysisType(MeasurementControlModule.mServerRef.transmitAnalysisType((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getAnalysisTypeDatabase().update(analysisType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Analysis type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve analysis type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return analysisType;
	}

	public EvaluationType loadEvaluationType(Identifier id) throws RetrieveObjectException, CommunicationException {
		EvaluationType evaluationType = null;
		try {
			evaluationType = new EvaluationType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("EvaluationType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				evaluationType = new EvaluationType(MeasurementControlModule.mServerRef.transmitEvaluationType((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getEvaluationTypeDatabase().update(evaluationType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Evaluation type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve evaluation type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return evaluationType;
	}

	public Set loadSet(Identifier id) throws RetrieveObjectException, CommunicationException {
		Set set = null;
		try {
			set = new Set(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Set '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				set = new Set(MeasurementControlModule.mServerRef.transmitSet((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getSetDatabase().update(set, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Set '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve set '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return set;
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MCMMeasurementObjectLoader.loadModeling | MCM doesn't need in modeling");
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementSetup measurementSetup = null;
		try {
			measurementSetup = new MeasurementSetup(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getMeasurementSetupDatabase().update(measurementSetup, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Measurement setup '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve measurement setup '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return measurementSetup;
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws RetrieveObjectException, CommunicationException {
		TemporalPattern temporalPattern = null;
		try {
			temporalPattern = new TemporalPattern(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("TemporalPattern '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				temporalPattern = new TemporalPattern(MeasurementControlModule.mServerRef.transmitTemporalPattern((Identifier_Transferable) id.getTransferable()));
				MeasurementDatabaseContext.getTemporalPatternDatabase().update(temporalPattern, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "TemporalPattern '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve temporal pattern '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return temporalPattern;
	}





	public Collection loadAnalysisTypes(Collection ids) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.getAnalysisTypeDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		AnalysisType analysisType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("AnalysisType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					analysisType = new AnalysisType(MeasurementControlModule.mServerRef.transmitAnalysisType((Identifier_Transferable) id.getTransferable()));
					collection.add(analysisType);
					loadedObjects.add(analysisType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Analysis type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve analysis type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadEvaluationTypes(Collection ids) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.getEvaluationTypeDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		EvaluationType evaluationType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("EvaluationType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					evaluationType = new EvaluationType(MeasurementControlModule.mServerRef.transmitEvaluationType((Identifier_Transferable) id.getTransferable()));
					collection.add(evaluationType);
					loadedObjects.add(evaluationType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Evaluation type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve evaluation type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadEvaluationType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadEvaluationType | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadModelings(Collection ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MCMMeasurementObjectLoader.loadModelings | mcm doesn't need in modeling");
	}

	public Collection loadMeasurementSetups(Collection ids) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getMeasurementSetupDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		MeasurementSetup measurementSetup;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable) id.getTransferable()));
					collection.add(measurementSetup);
					loadedObjects.add(measurementSetup);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Measurement setup '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve measurement setup '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadMeasurementTypes(Collection ids) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.getMeasurementTypeDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		MeasurementType measurementType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					measurementType = new MeasurementType(MeasurementControlModule.mServerRef.transmitMeasurementType((Identifier_Transferable) id.getTransferable()));
					collection.add(measurementType);
					loadedObjects.add(measurementType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Measurement type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve measurement type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadSets(Collection ids) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		Set set;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Set '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					set = new Set(MeasurementControlModule.mServerRef.transmitSet((Identifier_Transferable) id.getTransferable()));
					collection.add(set);
					loadedObjects.add(set);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Set '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve set '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadTemporalPatterns(Collection ids) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.getTemporalPatternDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		TemporalPattern temporalPattern;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("TemporalPattern '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					temporalPattern = new TemporalPattern(MeasurementControlModule.mServerRef.transmitTemporalPattern((Identifier_Transferable) id.getTransferable()));
					collection.add(temporalPattern);
					loadedObjects.add(temporalPattern);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "TemporalPattern '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve temporal pattern '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasurementObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

}
