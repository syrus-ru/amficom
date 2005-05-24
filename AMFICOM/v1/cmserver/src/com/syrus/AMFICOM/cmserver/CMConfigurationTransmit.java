/*
 * $Id: CMConfigurationTransmit.java,v 1.26 2005/05/24 15:08:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/05/24 15:08:15 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {
	private static final long serialVersionUID = 3378072469571119667L;

	public EquipmentType_Transferable[] transmitEquipmentTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final EquipmentType_Transferable equipmentTypes[] = new EquipmentType_Transferable[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public EquipmentType_Transferable[] transmitEquipmentTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final EquipmentType_Transferable equipmentTypes[] = new EquipmentType_Transferable[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public PortType_Transferable[] transmitPortTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final PortType_Transferable portTypes[] = new PortType_Transferable[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public PortType_Transferable[] transmitPortTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final PortType_Transferable portTypes[] = new PortType_Transferable[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MeasurementPortType_Transferable measurementPortTypes[] = new MeasurementPortType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MeasurementPortType_Transferable measurementPortTypes[] = new MeasurementPortType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final TransmissionPathType_Transferable transmissionPathTypes[] = new TransmissionPathType_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final TransmissionPathType_Transferable transmissionPathTypes[] = new TransmissionPathType_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public LinkType_Transferable[] transmitLinkTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final LinkType_Transferable linkTypes[] = new LinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public LinkType_Transferable[] transmitLinkTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final LinkType_Transferable linkTypes[] = new LinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CableLinkType_Transferable cableLinkTypes[] = new CableLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CableLinkType_Transferable cableLinkTypes[] = new CableLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CableThreadType_Transferable cableThreadTypes[] = new CableThreadType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CableThreadType_Transferable cableThreadTypes[] = new CableThreadType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public Equipment_Transferable[] transmitEquipments(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Equipment_Transferable equipments[] = new Equipment_Transferable[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public Equipment_Transferable[] transmitEquipmentsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Equipment_Transferable equipments[] = new Equipment_Transferable[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public Port_Transferable[] transmitPorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Port_Transferable ports[] = new Port_Transferable[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public Port_Transferable[] transmitPortsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Port_Transferable ports[] = new Port_Transferable[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MeasurementPort_Transferable measurementPorts[] = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MeasurementPort_Transferable measurementPorts[] = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final TransmissionPath_Transferable transmissionPaths[] = new TransmissionPath_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final TransmissionPath_Transferable transmissionPaths[] = new TransmissionPath_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public KIS_Transferable[] transmitKISs(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final KIS_Transferable kiss[] = new KIS_Transferable[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public KIS_Transferable[] transmitKISsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final KIS_Transferable kiss[] = new KIS_Transferable[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MonitoredElement_Transferable monitoredElements[] = new MonitoredElement_Transferable[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MonitoredElement_Transferable monitoredElements[] = new MonitoredElement_Transferable[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public Link_Transferable[] transmitLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Link_Transferable links[] = new Link_Transferable[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public Link_Transferable[] transmitLinksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Link_Transferable links[] = new Link_Transferable[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public CableThread_Transferable[] transmitCableThreads(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CableThread_Transferable cableThreads[] = new CableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}

	public CableThread_Transferable[] transmitCableThreadsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CableThread_Transferable cableThreads[] = new CableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}

	/*	Refresh*/

	/**
	 * @deprecated
	 */
	public Identifier_Transferable[] transmitRefreshedConfigurationObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
		final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			StorableObjectPool.refresh();

			Set storableObjects = StorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}
}
