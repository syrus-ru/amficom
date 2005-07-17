/*-
 * $Id: TransmitProcedureFactory.java,v 1.1 2005/07/17 04:57:03 arseniy Exp $
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
import com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitProcedure;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/17 04:57:03 $
 * @author $Author: arseniy $
 * @module mcm
 */
final class TransmitProcedureFactory {
	private static final TShortObjectHashMap PROCEDURE_MAP = new TShortObjectHashMap();

	protected static TransmitProcedure getProcedure(final short entityCode) throws IllegalObjectEntityException {
		TransmitProcedure procedure = (TransmitProcedure) PROCEDURE_MAP.get(entityCode);
		if (procedure != null) {
			return procedure;
		}

		procedure = createProcedure(entityCode);
		PROCEDURE_MAP.put(entityCode, procedure);
		return procedure;
	}

	private static TransmitProcedure createProcedure(final short entityCode) throws IllegalObjectEntityException {
		switch (entityCode) {

			/* General */
			case ObjectEntities.PARAMETER_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitParameterTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitCharacteristicTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.CHARACTERISTIC_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitCharacteristics(idsT, idlSessionKey);
					}
				};

			/* Administration */
			case ObjectEntities.SYSTEMUSER_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitSystemUsers(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.DOMAIN_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitDomains(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.SERVER_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitServers(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.MCM_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMCMs(idsT, idlSessionKey);
					}
				};

			/* Configuration */
			case ObjectEntities.EQUIPMENT_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitEquipmentTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.PORT_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitPortTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementPortTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.TRANSPATH_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitTransmissionPathTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.EQUIPMENT_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitEquipments(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.PORT_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitPorts(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.MEASUREMENTPORT_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementPorts(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.TRANSPATH_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitTransmissionPaths(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.KIS_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitKISs(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.MONITOREDELEMENT_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMonitoredElements(idsT, idlSessionKey);
					}
				};

			/* Measurement */
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitAnalysisTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.EVALUATION_TYPE_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitEvaluationTypes(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitMeasurementSetups(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.PARAMETERSET_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitParameterSets(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.CRONTEMPORALPATTERN_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitCronTemporalPatterns(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitIntervalsTemporalPatterns(idsT, idlSessionKey);
					}
				};
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				return new TransmitProcedure() {
					public IdlStorableObject[] transmitStorableObjects(final CommonServer commonServer,
							final IdlIdentifier[] idsT,
							final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
						return (MServerHelper.narrow(commonServer)).transmitPeriodicalTemporalPatterns(idsT, idlSessionKey);
					}
				};
			default:
				throw new IllegalObjectEntityException(ErrorMessages.ILLEGAL_ENTITY_CODE + ": '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, entityCode);
		}

	}
}
