/*-
 * $Id: CORBAConfigurationObjectLoader.java,v 1.20 2005/06/07 13:53:51 arseniy Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/06/07 13:53:51 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {

	public CORBAConfigurationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORTTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINKTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EQUIPMENT_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipments(ids1, sessionKey);
			}
		});
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PORT_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPorts(ids1, sessionKey);
			}
		});
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPorts(ids1, sessionKey);
			}
		});
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TRANSPATH_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPaths(ids1, sessionKey);
			}
		});
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.KIS_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISs(ids1, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElements(ids1, sessionKey);
			}
		});
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.LINK_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLETHREAD_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreads(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORTTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINKTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypesButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EQUIPMENT_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PORT_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TRANSPATH_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.KIS_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElementsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.LINK_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinksButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLETHREAD_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadsButIdsByCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveEquipmentTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.PORTTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.LINKTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.EQUIPMENT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.PORT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.TRANSPATH_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.KIS_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.LINK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
		super.saveStorableObjects(ObjectEntities.CABLETHREAD_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
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
