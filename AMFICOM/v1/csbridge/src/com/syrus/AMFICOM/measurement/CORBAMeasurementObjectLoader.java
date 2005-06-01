/*-
 * $Id: CORBAMeasurementObjectLoader.java,v 1.12 2005/06/01 13:02:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
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
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/01 13:02:06 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAMeasurementObjectLoader extends CORBAObjectLoader implements MeasurementObjectLoader {
	public CORBAMeasurementObjectLoader(ServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}

	public Set loadMeasurementTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadAnalysisTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.ANALYSISTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysisTypes(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationTypes(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MODELINGTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingTypes(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurements(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.ANALYSIS_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalyses(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.EVALUATION_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluations(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MODELING_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelings(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementSetups(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.RESULT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitResults(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SET_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitSets(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.TEST_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTests(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCronTemporalPatterns(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitIntervalsTemporalPatterns(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPeriodicalTemporalPatterns(ids1, sessionKey);
			}
		});
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.ANALYSISTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysisTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MODELINGTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MEASUREMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.ANALYSIS_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.EVALUATION_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MODELING_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementSetupsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.RESULT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitResultsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SET_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitSetsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.TEST_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTestsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCronTemporalPatternsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitIntervalsTemporalPatternsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public java.util.Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPeriodicalTemporalPatternsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementTypes(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalysisTypes(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluationTypes(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelingTypes(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurements(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveAnalyses(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEvaluations(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveModelings(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementSetups(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveResults(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Set_Transferable) ((com.syrus.AMFICOM.measurement.Set) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveSets(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTests(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CronTemporalPattern_Transferable) ((CronTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCronTemporalPatterns(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveIntervalsTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (IntervalsTemporalPattern_Transferable) ((IntervalsTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveIntervalsTemporalPatterns(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePeriodicalTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (PeriodicalTemporalPattern_Transferable) ((PeriodicalTemporalPattern) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePeriodicalTemporalPatterns(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}
}
