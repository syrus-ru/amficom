/*-
 * $Id: CORBAConfigurationObjectLoader.java,v 1.14 2005/06/01 13:02:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
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
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.14 $, $Date: 2005/06/01 13:02:06 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {
	public CORBAConfigurationObjectLoader(ServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.PORTTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.LINKTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.CABLELINKTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.EQUIPMENT_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.PORT_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.TRANSPATH_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.KIS_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.LINK_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.CABLETHREAD_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreads(ids1, sessionKey);
			}
		});
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.PORTTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.LINKTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinkTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.CABLELINKTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableLinkTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.EQUIPMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEquipmentsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.PORT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementPortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.TRANSPATH_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTransmissionPathsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.KIS_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitKISsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMonitoredElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.LINK_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.CABLETHREAD_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCableThreadsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (EquipmentType_Transferable) ((EquipmentType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEquipmentTypes(transferables, force, sessionKeyT);
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

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (PortType_Transferable) ((PortType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePortTypes(transferables, force, sessionKeyT);
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

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementPortType_Transferable) ((MeasurementPortType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementPortTypes(transferables, force, sessionKeyT);
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

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (TransmissionPathType_Transferable) ((TransmissionPathType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTransmissionPathTypes(transferables, force, sessionKeyT);
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

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		LinkType_Transferable[] transferables = new LinkType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (LinkType_Transferable) ((LinkType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveLinkTypes(transferables, force, sessionKeyT);
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

	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableLinkType_Transferable) ((CableLinkType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableLinkTypes(transferables, force, sessionKeyT);
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

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableThreadType_Transferable) ((CableThreadType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableThreadTypes(transferables, force, sessionKeyT);
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



	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Equipment_Transferable) ((Equipment) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEquipments(transferables, force, sessionKeyT);
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

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Port_Transferable) ((Port) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePorts(transferables, force, sessionKeyT);
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

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementPort_Transferable) ((MeasurementPort) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementPorts(transferables, force, sessionKeyT);
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

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (TransmissionPath_Transferable) ((TransmissionPath) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTransmissionPaths(transferables, force, sessionKeyT);
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

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (KIS_Transferable) ((KIS) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveKISs(transferables, force, sessionKeyT);
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

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MonitoredElement_Transferable) ((MonitoredElement) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMonitoredElements(transferables, force, sessionKeyT);
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

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Link_Transferable) ((Link) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveLinks(transferables, force, sessionKeyT);
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

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableThread_Transferable) ((CableThread) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableThreads(transferables, force, sessionKeyT);
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
