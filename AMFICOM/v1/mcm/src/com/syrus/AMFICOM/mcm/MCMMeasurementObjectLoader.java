/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.1 2004/08/14 19:37:27 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/08/14 19:37:27 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MCMMeasurementObjectLoader implements MeasurementObjectLoader {

	public MCMMeasurementObjectLoader() {
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException {
		ParameterType parameterType = null;
		try {
			parameterType = new ParameterType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable)id.getTransferable()));
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

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException {
		MeasurementType measurementType = null;
		try {
			measurementType = new MeasurementType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				measurementType = new MeasurementType(MeasurementControlModule.mServerRef.transmitMeasurementType((Identifier_Transferable)id.getTransferable()));
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

	public AnalysisType loadAnalysisType(Identifier id) throws RetrieveObjectException {
		AnalysisType analysisType = null;
		try {
			analysisType = new AnalysisType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("AnalysisType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				analysisType = new AnalysisType(MeasurementControlModule.mServerRef.transmitAnalysisType((Identifier_Transferable)id.getTransferable()));
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

	public EvaluationType loadEvaluationType(Identifier id) throws RetrieveObjectException {
		EvaluationType evaluationType = null;
		try {
			evaluationType = new EvaluationType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("EvaluationType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				evaluationType = new EvaluationType(MeasurementControlModule.mServerRef.transmitEvaluationType((Identifier_Transferable)id.getTransferable()));
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

	public Set loadSet(Identifier id) throws RetrieveObjectException {
		Set set = null;
		try {
			set = new Set(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Set '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				set = new Set(MeasurementControlModule.mServerRef.transmitSet((Identifier_Transferable)id.getTransferable()));
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

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException {
		MeasurementSetup measurementSetup = null;
		try {
			measurementSetup = new MeasurementSetup(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementSetup '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				measurementSetup = new MeasurementSetup(MeasurementControlModule.mServerRef.transmitMeasurementSetup((Identifier_Transferable)id.getTransferable()));
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

	public Test loadTest(Identifier id) throws RetrieveObjectException {
		Test test = null;
		try {
			test = new Test(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Test '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				test = new Test(MeasurementControlModule.mServerRef.transmitTest((Identifier_Transferable)id.getTransferable()));
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Test '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve test '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return test;
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws RetrieveObjectException {
		TemporalPattern temporalPattern = null;
		try {
			temporalPattern = new TemporalPattern(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("TemporalPattern '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL05);
			try {
				temporalPattern = new TemporalPattern(MeasurementControlModule.mServerRef.transmitTemporalPattern((Identifier_Transferable)id.getTransferable()));
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
