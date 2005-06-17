/*-
 * $Id: CORBAConfigurationObjectLoader.java,v 1.22 2005/06/17 11:01:02 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable;
import com.syrus.AMFICOM.configuration.corba.CableThread_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/06/17 11:01:02 $
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
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINK_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EQUIPMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipments(idsT, sessionKey);
			}
		});
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATH_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPaths(idsT, sessionKey);
			}
		});
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.KIS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElements(idsT, sessionKey);
			}
		});
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinks(idsT, sessionKey);
			}
		});
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREAD_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
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
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATH_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINK_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLELINK_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREAD_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EQUIPMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATH_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.KIS_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MONITOREDELEMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElementsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinksButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREAD_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveEquipmentTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEquipmentTypes((EquipmentType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePortTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PORT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receivePortTypes((PortType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurementPortTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementPortTypes((MeasurementPortType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTransmissionPathTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveTransmissionPathTypes((TransmissionPathType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.LINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveLinkTypes((LinkType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableLinkTypes((CableLinkType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableThreadTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableThreadTypes((CableThreadType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveEquipments(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EQUIPMENT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEquipments((Equipment_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PORT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receivePorts((Port_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurementPorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementPorts((MeasurementPort_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTransmissionPaths(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TRANSPATH_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveTransmissionPaths((TransmissionPath_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveKISs(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.KIS_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveKISs((KIS_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMonitoredElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMonitoredElements((MonitoredElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.LINK_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveLinks((Link_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCableThreads(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLETHREAD_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCableThreads((CableThread_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
