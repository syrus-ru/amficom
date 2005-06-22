/*-
 * $Id: CORBAConfigurationObjectLoader.java,v 1.25 2005/06/22 10:05:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThread;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlKIS;
import com.syrus.AMFICOM.configuration.corba.IdlLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlLink;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPortType;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPort;
import com.syrus.AMFICOM.configuration.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.configuration.corba.IdlPortType;
import com.syrus.AMFICOM.configuration.corba.IdlPort;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathType;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.25 $, $Date: 2005/06/22 10:05:19 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {

	public CORBAConfigurationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINK_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EQUIPMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipments(idsT, sessionKey);
			}
		});
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATH_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPaths(idsT, sessionKey);
			}
		});
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.KIS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElements(idsT, sessionKey);
			}
		});
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinks(idsT, sessionKey);
			}
		});
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREAD_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreads(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EQUIPMENT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATH_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINK_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLELINK_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREAD_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EQUIPMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATH_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.KIS_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MONITOREDELEMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElementsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinksButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREAD_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveEquipmentTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEquipmentTypes((IdlEquipmentType[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePortTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PORT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receivePortTypes((IdlPortType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurementPortTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementPortTypes((IdlMeasurementPortType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTransmissionPathTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveTransmissionPathTypes((IdlTransmissionPathType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.LINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveLinkTypes((IdlLinkType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableLinkTypes((IdlCableLinkType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableThreadTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableThreadTypes((IdlCableThreadType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveEquipments(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EQUIPMENT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEquipments((IdlEquipment[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PORT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receivePorts((IdlPort[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurementPorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementPorts((IdlMeasurementPort[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTransmissionPaths(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TRANSPATH_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveTransmissionPaths((IdlTransmissionPath[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveKISs(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.KIS_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveKISs((IdlKIS[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMonitoredElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMonitoredElements((IdlMonitoredElement[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.LINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveLinks((IdlLink[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableThreads(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLETHREAD_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableThreads((IdlCableThread[]) transferables, force, sessionKey);
			}
		});
	}
}
