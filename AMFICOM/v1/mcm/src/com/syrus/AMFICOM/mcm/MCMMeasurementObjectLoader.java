/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.14 2005/01/17 09:03:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/01/17 09:03:33 $
 * @author $Author: bob $
 * @module mcm_v1
 */

final class MCMMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		ParameterType parameterType = null;
		try {
			parameterType = new ParameterType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable)id.getTransferable()));
				parameterType.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Parameter type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve parameter type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return parameterType;
	}

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementType measurementType = null;
		try {
			measurementType = new MeasurementType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				measurementType = new MeasurementType(MeasurementControlModule.mServerRef.transmitMeasurementType((Identifier_Transferable)id.getTransferable()));
				measurementType.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Measurement type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve measurement type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				analysisType = new AnalysisType(MeasurementControlModule.mServerRef.transmitAnalysisType((Identifier_Transferable)id.getTransferable()));
				analysisType.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Analysis type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve analysis type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				evaluationType = new EvaluationType(MeasurementControlModule.mServerRef.transmitEvaluationType((Identifier_Transferable)id.getTransferable()));
				evaluationType.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Evaluation type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve evaluation type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				set = new Set(MeasurementControlModule.mServerRef.transmitSet((Identifier_Transferable)id.getTransferable()));
				set.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Set '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve set '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable)id.getTransferable()));
				measurementSetup.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Measurement setup '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve measurement setup '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				temporalPattern = new TemporalPattern(MeasurementControlModule.mServerRef.transmitTemporalPattern((Identifier_Transferable)id.getTransferable()));
				temporalPattern.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("TemporalPattern '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve temporal pattern '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return temporalPattern;
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		List list;
		List copyOfList;
		AnalysisType analysisType;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("AnalysisType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					analysisType = new AnalysisType(MeasurementControlModule.mServerRef.transmitAnalysisType((Identifier_Transferable)id.getTransferable()));
					analysisType.insert();
					list.add(analysisType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Analysis type '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve analysis type '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		List list;
		List copyOfList;
		EvaluationType evaluationType;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("EvaluationType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					evaluationType = new EvaluationType(MeasurementControlModule.mServerRef.transmitEvaluationType((Identifier_Transferable)id.getTransferable()));
					evaluationType.insert();
					list.add(evaluationType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Evaluation type '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve evaluation type '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadEvaluationType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadEvaluationType | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MCMMeasurementObjectLoader.loadModelings | mcm doesn't need in modeling");
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
		List list;
		List copyOfList;
		MeasurementSetup measurementSetup;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
				it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable)id.getTransferable()));
					measurementSetup.insert();
					list.add(measurementSetup);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Measurement setup '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve measurement setup '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		List list;
		List copyOfList;
		MeasurementType measurementType;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					measurementType = new MeasurementType(MeasurementControlModule.mServerRef.transmitMeasurementType((Identifier_Transferable)id.getTransferable()));
					measurementType.insert();
					list.add(measurementType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Measurement type '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve measurement type '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase();
		List list;
		List copyOfList;
		ParameterType parameterType;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable)id.getTransferable()));
					parameterType.insert();
					list.add(parameterType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Parameter type '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve parameter type '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
		List list;
		List copyOfList;
		Set set;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
					if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Set '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					set = new Set(MeasurementControlModule.mServerRef.transmitSet((Identifier_Transferable)id.getTransferable()));
					set.insert();
					list.add(set);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Set '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve set '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
		List list;
		List copyOfList;
		TemporalPattern temporalPattern;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
				it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("TemporalPattern '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					temporalPattern = new TemporalPattern(MeasurementControlModule.mServerRef.transmitTemporalPattern((Identifier_Transferable)id.getTransferable()));
					temporalPattern.insert();
					list.add(temporalPattern);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("TemporalPattern '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve temporal pattern '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

}
