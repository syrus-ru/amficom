/*
 * $Id: CORBAMeasurementObjectLoader.java,v 1.2 2005/04/22 20:11:59 arseniy Exp $
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
import com.syrus.AMFICOM.general.ClientSession;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 20:11:59 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAMeasurementObjectLoader extends CORBAObjectLoader implements MeasurementObjectLoader {

	public CORBAMeasurementObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}



	/* Load single object*/

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementType_Transferable transferable = cmServer.transmitMeasurementType((Identifier_Transferable) id.getTransferable(), ait);
			MeasurementType storableObject = new MeasurementType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			AnalysisType_Transferable transferable = cmServer.transmitAnalysisType((Identifier_Transferable) id.getTransferable(), ait);
			AnalysisType storableObject = new AnalysisType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			EvaluationType_Transferable transferable = cmServer.transmitEvaluationType((Identifier_Transferable) id.getTransferable(), ait);
			EvaluationType storableObject = new EvaluationType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			ModelingType_Transferable transferable = cmServer.transmitModelingType((Identifier_Transferable) id.getTransferable(), ait);
			ModelingType storableObject = new ModelingType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}



	public Measurement loadMeasurement(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Measurement_Transferable transferable = cmServer.transmitMeasurement((Identifier_Transferable) id.getTransferable(), ait);
			Measurement storableObject = new Measurement(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Analysis loadAnalysis(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Analysis_Transferable transferable = cmServer.transmitAnalysis((Identifier_Transferable) id.getTransferable(), ait);
			Analysis storableObject = new Analysis(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Evaluation loadEvaluation(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Evaluation_Transferable transferable = cmServer.transmitEvaluation((Identifier_Transferable) id.getTransferable(), ait);
			Evaluation storableObject = new Evaluation(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Modeling_Transferable transferable = cmServer.transmitModeling((Identifier_Transferable) id.getTransferable(), ait);
			Modeling storableObject = new Modeling(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementSetup_Transferable transferable = cmServer.transmitMeasurementSetup((Identifier_Transferable) id.getTransferable(), ait);
			MeasurementSetup storableObject = new MeasurementSetup(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Result_Transferable transferable = cmServer.transmitResult((Identifier_Transferable) id.getTransferable(), ait);
			Result storableObject = new Result(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Set_Transferable transferable = cmServer.transmitSet((Identifier_Transferable) id.getTransferable(), ait);
			Set storableObject = new Set(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Test_Transferable transferable = cmServer.transmitTest((Identifier_Transferable) id.getTransferable(), ait);
			Test storableObject = new Test(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public CronTemporalPattern loadCronTemporalPattern(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			CronTemporalPattern_Transferable transferable = cmServer.transmitCronTemporalPattern((Identifier_Transferable) id.getTransferable(), ait);
			CronTemporalPattern storableObject = new CronTemporalPattern(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementType_Transferable[] transferables = cmServer.transmitMeasurementTypes(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			AnalysisType_Transferable[] transferables = cmServer.transmitAnalysisTypes(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			EvaluationType_Transferable[] transferables = cmServer.transmitEvaluationTypes(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			ModelingType_Transferable[] transferables = cmServer.transmitModelingTypes(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Measurement_Transferable[] transferables = cmServer.transmitMeasurements(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Analysis_Transferable[] transferables = cmServer.transmitAnalyses(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Evaluation_Transferable[] transferables = cmServer.transmitEvaluations(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Modeling_Transferable[] transferables = cmServer.transmitModelings(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementSetup_Transferable[] transferables = cmServer.transmitMeasurementSetups(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Result_Transferable[] transferables = cmServer.transmitResults(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Set_Transferable[] transferables = cmServer.transmitSets(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Test_Transferable[] transferables = cmServer.transmitTests(idsT, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			CronTemporalPattern_Transferable[] transferables = cmServer.transmitCronTemporalPatterns(idsT, ait);
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



	/* Load multiple objects but ids*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementType_Transferable[] transferables = cmServer.transmitMeasurementTypesButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			AnalysisType_Transferable[] transferables = cmServer.transmitAnalysisTypesButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			EvaluationType_Transferable[] transferables = cmServer.transmitEvaluationTypesButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			ModelingType_Transferable[] transferables = cmServer.transmitModelingTypesButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Measurement_Transferable[] transferables = cmServer.transmitMeasurementsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Analysis_Transferable[] transferables = cmServer.transmitAnalysesButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Evaluation_Transferable[] transferables = cmServer.transmitEvaluationsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Modeling_Transferable[] transferables = cmServer.transmitModelingsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			MeasurementSetup_Transferable[] transferables = cmServer.transmitMeasurementSetupsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Result_Transferable[] transferables = cmServer.transmitResultsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Set_Transferable[] transferables = cmServer.transmitSetsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			Test_Transferable[] transferables = cmServer.transmitTestsButIdsCondition(idsT, ait, conditionT);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		try {
			CronTemporalPattern_Transferable[] transferables = cmServer.transmitCronTemporalPatternsButIdsCondition(idsT, ait, conditionT);
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



	/* Save single object*/

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		MeasurementType_Transferable transferable = (MeasurementType_Transferable) measurementType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMeasurementType(transferable, force, ait);
			measurementType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + measurementType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		AnalysisType_Transferable transferable = (AnalysisType_Transferable) analysisType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveAnalysisType(transferable, force, ait);
			analysisType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + analysisType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		EvaluationType_Transferable transferable = (EvaluationType_Transferable) evaluationType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveEvaluationType(transferable, force, ait);
			evaluationType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + evaluationType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		ModelingType_Transferable transferable = (ModelingType_Transferable) modelingType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveModelingType(transferable, force, ait);
			modelingType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + modelingType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}




	public void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Measurement_Transferable transferable = (Measurement_Transferable) measurement.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMeasurement(transferable, force, ait);
			measurement.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + measurement.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Analysis_Transferable transferable = (Analysis_Transferable) analysis.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveAnalysis(transferable, force, ait);
			analysis.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + analysis.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Evaluation_Transferable transferable = (Evaluation_Transferable) evaluation.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveEvaluation(transferable, force, ait);
			evaluation.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + evaluation.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Modeling_Transferable transferable = (Modeling_Transferable) modeling.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveModeling(transferable, force, ait);
			modeling.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + modeling.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		MeasurementSetup_Transferable transferable = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMeasurementSetup(transferable, force, ait);
			measurementSetup.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + measurementSetup.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveResult(Result result, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Result_Transferable transferable = (Result_Transferable) result.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveResult(transferable, force, ait);
			result.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + result.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveSet(Set set, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Set_Transferable transferable = (Set_Transferable) set.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveSet(transferable, force, ait);
			set.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + set.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTest(Test test, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Test_Transferable transferable = (Test_Transferable) test.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveTest(transferable, force, ait);
			test.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + test.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCronTemporalPattern(CronTemporalPattern cronTemporalPattern, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		CronTemporalPattern_Transferable transferable = (CronTemporalPattern_Transferable) cronTemporalPattern.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCronTemporalPattern(transferable, force, ait);
			cronTemporalPattern.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + cronTemporalPattern.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementTypes(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalysisTypes(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluationTypes(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelingTypes(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurements(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalyses(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluations(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelings(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementSetups(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveResults(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Set_Transferable) ((Set) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveSets(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTests(transferables, force, ait);
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
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CronTemporalPattern_Transferable) ((CronTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCronTemporalPatterns(transferables, force, ait);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		StorableObject_Transferable[] headersT = StorableObject.createHeadersTransferable(storableObjects);

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedMeasurementObjects(headersT, ait);

			java.util.Set refreshedIds = Identifier.fromTransferables(idsT);
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			throw new ApplicationException(are);
		}
	}

}
