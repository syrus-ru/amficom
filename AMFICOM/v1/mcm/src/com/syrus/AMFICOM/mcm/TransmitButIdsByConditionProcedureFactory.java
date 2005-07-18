/*-
 * $Id: TransmitButIdsByConditionProcedureFactory.java,v 1.2 2005/07/18 11:15:39 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import gnu.trove.TShortObjectHashMap;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitButIdsByConditionProcedure;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.2 $, $Date: 2005/07/18 11:15:39 $
 * @author $Author: arseniy $
 * @module mcm
 */
final class TransmitButIdsByConditionProcedureFactory {
	private static final TShortObjectHashMap PROCEDURE_MAP = new TShortObjectHashMap();

	protected static TransmitButIdsByConditionProcedure getProcedure(final short entityCode) throws IllegalObjectEntityException {
		TransmitButIdsByConditionProcedure procedure = (TransmitButIdsByConditionProcedure) PROCEDURE_MAP.get(entityCode);
		if (procedure != null) {
			return procedure;
		}

		procedure = createProcedure(entityCode);
		PROCEDURE_MAP.put(entityCode, procedure);
		return procedure;
	}

	private static TransmitButIdsByConditionProcedure createProcedure(final short entityCode) throws IllegalObjectEntityException {
		switch (entityCode) {

			/* General */
			case ObjectEntities.PARAMETER_TYPE_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitParameterTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};

			/* Configuration */
			case ObjectEntities.KIS_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};

			/* Measurement */
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitAnalysisTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.EVALUATION_TYPE_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitEvaluationTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementSetupsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.PARAMETERSET_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitParameterSetsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.CRONTEMPORALPATTERN_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitCronTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitIntervalsTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				return new TransmitButIdsByConditionProcedure() {
					public IdlStorableObject[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey sessionKey,
							final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitPeriodicalTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				};
			default:
				throw new IllegalObjectEntityException(ErrorMessages.ILLEGAL_ENTITY_CODE + ": '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, entityCode);
		}
	}
}
