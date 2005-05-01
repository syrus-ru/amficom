/*
 * $Id: CORBAMeasurementObjectLoader.java,v 1.5 2005/05/01 17:29:09 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CMServerConnectionManager;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/01 17:29:09 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAMeasurementObjectLoader extends CORBAObjectLoader implements MeasurementObjectLoader {

	public CORBAMeasurementObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}



	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementType_Transferable[] transferables = cmServer.transmitMeasurementTypes(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			AnalysisType_Transferable[] transferables = cmServer.transmitAnalysisTypes(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new AnalysisType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			EvaluationType_Transferable[] transferables = cmServer.transmitEvaluationTypes(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new EvaluationType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			ModelingType_Transferable[] transferables = cmServer.transmitModelingTypes(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new ModelingType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Measurement_Transferable[] transferables = cmServer.transmitMeasurements(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Measurement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Analysis_Transferable[] transferables = cmServer.transmitAnalyses(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Analysis(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Evaluation_Transferable[] transferables = cmServer.transmitEvaluations(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Evaluation(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Modeling_Transferable[] transferables = cmServer.transmitModelings(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Modeling(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementSetup_Transferable[] transferables = cmServer.transmitMeasurementSetups(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementSetup(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Result_Transferable[] transferables = cmServer.transmitResults(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Result(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Set_Transferable[] transferables = cmServer.transmitSets(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Set(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Test_Transferable[] transferables = cmServer.transmitTests(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Test(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CronTemporalPattern_Transferable[] transferables = cmServer.transmitCronTemporalPatterns(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CronTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			IntervalsTemporalPattern_Transferable[] transferables = cmServer.transmitIntervalsTemporalPatterns(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new IntervalsTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			PeriodicalTemporalPattern_Transferable[] transferables = cmServer.transmitPeriodicalTemporalPatterns(idsT, securityKey);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new PeriodicalTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects but ids*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementType_Transferable[] transferables = cmServer.transmitMeasurementTypesButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			AnalysisType_Transferable[] transferables = cmServer.transmitAnalysisTypesButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new AnalysisType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			EvaluationType_Transferable[] transferables = cmServer.transmitEvaluationTypesButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new EvaluationType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			ModelingType_Transferable[] transferables = cmServer.transmitModelingTypesButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new ModelingType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}




	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Measurement_Transferable[] transferables = cmServer.transmitMeasurementsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Measurement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Analysis_Transferable[] transferables = cmServer.transmitAnalysesButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Analysis(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Evaluation_Transferable[] transferables = cmServer.transmitEvaluationsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Evaluation(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Modeling_Transferable[] transferables = cmServer.transmitModelingsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Modeling(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementSetup_Transferable[] transferables = cmServer.transmitMeasurementSetupsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementSetup(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Result_Transferable[] transferables = cmServer.transmitResultsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Result(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Set_Transferable[] transferables = cmServer.transmitSetsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Set(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Test_Transferable[] transferables = cmServer.transmitTestsButIdsCondition(idsT, securityKey, conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Test(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CronTemporalPattern_Transferable[] transferables = cmServer.transmitCronTemporalPatternsButIdsCondition(idsT,
					securityKey,
					conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CronTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			IntervalsTemporalPattern_Transferable[] transferables = cmServer.transmitIntervalsTemporalPatternsButIdsCondition(idsT,
					securityKey,
					conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new IntervalsTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public java.util.Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			PeriodicalTemporalPattern_Transferable[] transferables = cmServer.transmitPeriodicalTemporalPatternsButIdsCondition(idsT,
					securityKey,
					conditionT);
			java.util.Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new PeriodicalTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalysisTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluationTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelingTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurements(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalyses(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluations(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelings(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementSetups(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveResults(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Set_Transferable) ((Set) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveSets(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTests(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CronTemporalPattern_Transferable) ((CronTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCronTemporalPatterns(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveIntervalsTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (IntervalsTemporalPattern_Transferable) ((IntervalsTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveIntervalsTemporalPatterns(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePeriodicalTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (PeriodicalTemporalPattern_Transferable) ((PeriodicalTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePeriodicalTemporalPatterns(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	/*	Refresh*/

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		StorableObject_Transferable[] headersT = StorableObject.createHeadersTransferable(storableObjects);

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedMeasurementObjects(headersT, securityKey);

			java.util.Set refreshedIds = Identifier.fromTransferables(idsT);
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			throw new ApplicationException(are);
		}
	}

}
