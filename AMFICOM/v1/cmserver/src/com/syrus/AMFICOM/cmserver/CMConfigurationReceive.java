/*-
 * $Id: CMConfigurationReceive.java,v 1.27 2005/06/21 12:44:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThread;
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.27 $, $Date: 2005/06/21 12:44:32 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {
	private static final long serialVersionUID = 5462858483804681509L;

	public final IdlStorableObject[] receiveEquipmentTypes(
			final EquipmentType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePortTypes(
			final PortType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementPortTypes(
			final MeasurementPortType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTransmissionPathTypes(
			final TransmissionPathType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveLinkTypes(
			final LinkType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.LINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableLinkTypes(
			final CableLinkType_Transferable transferables[],
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
			final Equipment_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePorts(
			final Port_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementPorts(
			final MeasurementPort_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTransmissionPaths(
			final TransmissionPath_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveKISs(
			final KIS_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.KIS_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMonitoredElements(
			final MonitoredElement_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveLinks(
			final Link_Transferable transferables[],
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
