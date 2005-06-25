/*-
 * $Id: CMConfigurationReceive.java,v 1.29 2005/06/25 17:07:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.ORB;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.29 $, $Date: 2005/06/25 17:07:50 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {
	private static final long serialVersionUID = 5462858483804681509L;

	CMConfigurationReceive(final ORB orb) {
		super(orb);
	}

	public final IdlStorableObject[] receiveEquipmentTypes(
			final IdlEquipmentType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePortTypes(
			final IdlPortType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementPortTypes(
			final IdlMeasurementPortType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTransmissionPathTypes(
			final IdlTransmissionPathType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveLinkTypes(
			final IdlLinkType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.LINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableLinkTypes(
			final IdlCableLinkType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableThreadTypes(
			final IdlCableThreadType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveEquipments(
			final IdlEquipment transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePorts(
			final IdlPort transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementPorts(
			final IdlMeasurementPort transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTransmissionPaths(
			final IdlTransmissionPath transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveKISs(
			final IdlKIS transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.KIS_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMonitoredElements(
			final IdlMonitoredElement transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveLinks(
			final IdlLink transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.LINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableThreads(
			final IdlCableThread transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLETHREAD_CODE, transferables, force, sessionKey);
	}
}
