/*-
 * $Id: CMConfigurationReceive.java,v 1.26 2005/06/17 11:01:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.26 $, $Date: 2005/06/17 11:01:01 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMConfigurationReceive extends CMAdministrationReceive {
	private static final long serialVersionUID = 5462858483804681509L;

	public final StorableObject_Transferable[] receiveEquipmentTypes(
			final EquipmentType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePortTypes(
			final PortType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMeasurementPortTypes(
			final MeasurementPortType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveTransmissionPathTypes(
			final TransmissionPathType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveLinkTypes(
			final LinkType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.LINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCableLinkTypes(
			final CableLinkType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLELINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCableThreadTypes(
			final CableThreadType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLETHREAD_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveEquipments(
			final Equipment_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EQUIPMENT_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePorts(
			final Port_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PORT_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMeasurementPorts(
			final MeasurementPort_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveTransmissionPaths(
			final TransmissionPath_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TRANSPATH_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveKISs(
			final KIS_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.KIS_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMonitoredElements(
			final MonitoredElement_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveLinks(
			final Link_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.LINK_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCableThreads(
			final CableThread_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLETHREAD_CODE, transferables, force, sessionKey);
	}
}
