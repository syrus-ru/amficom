/*
 * $Id: CMConfigurationTransmit.java,v 1.31 2005/06/17 13:07:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.31 $, $Date: 2005/06/17 13:07:00 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {

	private static final long serialVersionUID = 3378072469571119667L;


	/* Transmit multiple objects*/

	public EquipmentType_Transferable[] transmitEquipmentTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final EquipmentType_Transferable[] equipmentTypes = new EquipmentType_Transferable[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public PortType_Transferable[] transmitPortTypes(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final PortType_Transferable[] portTypes = new PortType_Transferable[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MeasurementPortType_Transferable[] measurementPortTypes = new MeasurementPortType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final TransmissionPathType_Transferable[] transmissionPathTypes = new TransmissionPathType_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public LinkType_Transferable[] transmitLinkTypes(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final LinkType_Transferable[] linkTypes = new LinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CableLinkType_Transferable[] cableLinkTypes = new CableLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CableThreadType_Transferable[] cableThreadTypes = new CableThreadType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public Equipment_Transferable[] transmitEquipments(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Equipment_Transferable[] equipments = new Equipment_Transferable[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public Port_Transferable[] transmitPorts(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Port_Transferable[] ports = new Port_Transferable[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MeasurementPort_Transferable[] measurementPorts = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final TransmissionPath_Transferable[] transmissionPaths = new TransmissionPath_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public KIS_Transferable[] transmitKISs(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final KIS_Transferable[] kiss = new KIS_Transferable[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MonitoredElement_Transferable[] monitoredElements = new MonitoredElement_Transferable[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public Link_Transferable[] transmitLinks(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Link_Transferable[] links = new Link_Transferable[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public CableThread_Transferable[] transmitCableThreads(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CableThread_Transferable[] cableThreads = new CableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}



	/* Transmit multiple objects but idsT by condition */

	public EquipmentType_Transferable[] transmitEquipmentTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final EquipmentType_Transferable[] equipmentTypes = new EquipmentType_Transferable[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public PortType_Transferable[] transmitPortTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final PortType_Transferable[] portTypes = new PortType_Transferable[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementPortType_Transferable[] measurementPortTypes = new MeasurementPortType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final TransmissionPathType_Transferable[] transmissionPathTypes = new TransmissionPathType_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public LinkType_Transferable[] transmitLinkTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final LinkType_Transferable[] linkTypes = new LinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CableLinkType_Transferable[] cableLinkTypes = new CableLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CableThreadType_Transferable[] cableThreadTypes = new CableThreadType_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public Equipment_Transferable[] transmitEquipmentsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Equipment_Transferable[] equipments = new Equipment_Transferable[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public Port_Transferable[] transmitPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Port_Transferable[] ports = new Port_Transferable[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementPort_Transferable[] measurementPorts = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final TransmissionPath_Transferable[] transmissionPaths = new TransmissionPath_Transferable[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public KIS_Transferable[] transmitKISsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final KIS_Transferable[] kiss = new KIS_Transferable[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MonitoredElement_Transferable[] monitoredElements = new MonitoredElement_Transferable[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public Link_Transferable[] transmitLinksButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Link_Transferable[] links = new Link_Transferable[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public CableThread_Transferable[] transmitCableThreadsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CableThread_Transferable[] cableThreads = new CableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}
}
