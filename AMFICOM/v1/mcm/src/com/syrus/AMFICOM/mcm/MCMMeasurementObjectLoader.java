/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.2 2004/08/22 19:10:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/22 19:10:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public final class MCMMeasurementObjectLoader implements MeasurementObjectLoader {

	public MCMMeasurementObjectLoader() {
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		ParameterType parameterType = null;
		try {
			parameterType = new ParameterType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable)id.getTransferable()));
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

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementSetup measurementSetup = null;
		try {
			measurementSetup = new MeasurementSetup(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable)id.getTransferable()));
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

	public Measurement loadMeasurement(Identifier id) throws DatabaseException {
		return new Measurement(id);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException {
		return new Analysis(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException {
		return new Evaluation(id);
	}

	public Test loadTest(Identifier id) throws DatabaseException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		return new Result(id);
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
}
