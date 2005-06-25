/*
 * $Id: CMConfigurationTransmit.java,v 1.34 2005/06/25 17:07:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.34 $, $Date: 2005/06/25 17:07:49 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {

	private static final long serialVersionUID = 3378072469571119667L;

	CMConfigurationTransmit(final ORB orb) {
		super(orb);
	}

	/* Transmit multiple objects*/

	public IdlEquipmentType[] transmitEquipmentTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlEquipmentType[] equipmentTypes = new IdlEquipmentType[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public IdlPortType[] transmitPortTypes(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlPortType[] portTypes = new IdlPortType[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public IdlMeasurementPortType[] transmitMeasurementPortTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMeasurementPortType[] measurementPortTypes = new IdlMeasurementPortType[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public IdlTransmissionPathType[] transmitTransmissionPathTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlTransmissionPathType[] transmissionPathTypes = new IdlTransmissionPathType[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public IdlLinkType[] transmitLinkTypes(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlLinkType[] linkTypes = new IdlLinkType[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public IdlCableLinkType[] transmitCableLinkTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCableLinkType[] cableLinkTypes = new IdlCableLinkType[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public IdlCableThreadType[] transmitCableThreadTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCableThreadType[] cableThreadTypes = new IdlCableThreadType[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public IdlEquipment[] transmitEquipments(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlEquipment[] equipments = new IdlEquipment[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public IdlPort[] transmitPorts(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlPort[] ports = new IdlPort[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public IdlMeasurementPort[] transmitMeasurementPorts(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMeasurementPort[] measurementPorts = new IdlMeasurementPort[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public IdlTransmissionPath[] transmitTransmissionPaths(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlTransmissionPath[] transmissionPaths = new IdlTransmissionPath[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public IdlKIS[] transmitKISs(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlKIS[] kiss = new IdlKIS[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public IdlMonitoredElement[] transmitMonitoredElements(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMonitoredElement[] monitoredElements = new IdlMonitoredElement[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public IdlLink[] transmitLinks(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlLink[] links = new IdlLink[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public IdlCableThread[] transmitCableThreads(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCableThread[] cableThreads = new IdlCableThread[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}



	/* Transmit multiple objects but idsT by condition */

	public IdlEquipmentType[] transmitEquipmentTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEquipmentType[] equipmentTypes = new IdlEquipmentType[length];
		System.arraycopy(storableObjects, 0, equipmentTypes, 0, length);
		return equipmentTypes;
	}

	public IdlPortType[] transmitPortTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlPortType[] portTypes = new IdlPortType[length];
		System.arraycopy(storableObjects, 0, portTypes, 0, length);
		return portTypes;
	}

	public IdlMeasurementPortType[] transmitMeasurementPortTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementPortType[] measurementPortTypes = new IdlMeasurementPortType[length];
		System.arraycopy(storableObjects, 0, measurementPortTypes, 0, length);
		return measurementPortTypes;
	}

	public IdlTransmissionPathType[] transmitTransmissionPathTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlTransmissionPathType[] transmissionPathTypes = new IdlTransmissionPathType[length];
		System.arraycopy(storableObjects, 0, transmissionPathTypes, 0, length);
		return transmissionPathTypes;
	}

	public IdlLinkType[] transmitLinkTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlLinkType[] linkTypes = new IdlLinkType[length];
		System.arraycopy(storableObjects, 0, linkTypes, 0, length);
		return linkTypes;
	}

	public IdlCableLinkType[] transmitCableLinkTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCableLinkType[] cableLinkTypes = new IdlCableLinkType[length];
		System.arraycopy(storableObjects, 0, cableLinkTypes, 0, length);
		return cableLinkTypes;
	}

	public IdlCableThreadType[] transmitCableThreadTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCableThreadType[] cableThreadTypes = new IdlCableThreadType[length];
		System.arraycopy(storableObjects, 0, cableThreadTypes, 0, length);
		return cableThreadTypes;
	}

	public IdlEquipment[] transmitEquipmentsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEquipment[] equipments = new IdlEquipment[length];
		System.arraycopy(storableObjects, 0, equipments, 0, length);
		return equipments;
	}

	public IdlPort[] transmitPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlPort[] ports = new IdlPort[length];
		System.arraycopy(storableObjects, 0, ports, 0, length);
		return ports;
	}

	public IdlMeasurementPort[] transmitMeasurementPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementPort[] measurementPorts = new IdlMeasurementPort[length];
		System.arraycopy(storableObjects, 0, measurementPorts, 0, length);
		return measurementPorts;
	}

	public IdlTransmissionPath[] transmitTransmissionPathsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlTransmissionPath[] transmissionPaths = new IdlTransmissionPath[length];
		System.arraycopy(storableObjects, 0, transmissionPaths, 0, length);
		return transmissionPaths;
	}

	public IdlKIS[] transmitKISsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlKIS[] kiss = new IdlKIS[length];
		System.arraycopy(storableObjects, 0, kiss, 0, length);
		return kiss;
	}

	public IdlMonitoredElement[] transmitMonitoredElementsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMonitoredElement[] monitoredElements = new IdlMonitoredElement[length];
		System.arraycopy(storableObjects, 0, monitoredElements, 0, length);
		return monitoredElements;
	}

	public IdlLink[] transmitLinksButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlLink[] links = new IdlLink[length];
		System.arraycopy(storableObjects, 0, links, 0, length);
		return links;
	}

	public IdlCableThread[] transmitCableThreadsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCableThread[] cableThreads = new IdlCableThread[length];
		System.arraycopy(storableObjects, 0, cableThreads, 0, length);
		return cableThreads;
	}
}
