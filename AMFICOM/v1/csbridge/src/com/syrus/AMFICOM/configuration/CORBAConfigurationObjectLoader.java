/*-
 * $Id: CORBAConfigurationObjectLoader.java,v 1.26 2005/06/22 19:29:31 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
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
 * @version $Revision: 1.26 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {

	public CORBAConfigurationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadEquipmentTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadPortTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMeasurementPortTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadTransmissionPathTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadLinkTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableLinkTypes(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableThreadTypes(final Set<Identifier> ids) throws ApplicationException {
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



	public Set loadEquipments(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadPorts(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMeasurementPorts(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadTransmissionPaths(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadKISs(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMonitoredElements(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadLinks(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableLinks(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLELINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreads(idsT, sessionKey);
			}
		});
	}

	public Set loadCableThreads(final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadEquipmentTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMeasurementPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadTransmissionPathTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableThreadTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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



	public Set loadEquipmentsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMeasurementPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadTransmissionPathsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadKISsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadMonitoredElementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public Set loadCableLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLELINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
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

	public Set loadCableThreadsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
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

	public void saveEquipmentTypes(final Set<EquipmentType> storableObjects, final boolean force) throws ApplicationException {
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

	public void savePortTypes(final Set<PortType> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveMeasurementPortTypes(final Set<MeasurementPortType> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveTransmissionPathTypes(final Set<TransmissionPathType> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveLinkTypes(final Set<LinkType> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveCableLinkTypes(final Set<CableLinkType> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveCableThreadTypes(final Set<CableThreadType> storableObjects, final boolean force) throws ApplicationException {
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



	public void saveEquipments(final Set<Equipment> storableObjects, final boolean force) throws ApplicationException {
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

	public void savePorts(final Set<Port> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveMeasurementPorts(final Set<MeasurementPort> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveTransmissionPaths(final Set<TransmissionPath> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveKISs(final Set<KIS> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveMonitoredElements(final Set<MonitoredElement> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveLinks(final Set<Link> storableObjects, final boolean force) throws ApplicationException {
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

	public void saveCableLinks(final Set<CableLink> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLELINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableThreads((IdlCableThread[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableThreads(final Set<CableThread> storableObjects, final boolean force) throws ApplicationException {
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
