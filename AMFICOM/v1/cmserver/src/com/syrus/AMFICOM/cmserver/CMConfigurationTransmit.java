/*
 * $Id: CMConfigurationTransmit.java,v 1.24 2005/05/18 13:11:21 bass Exp $
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

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThread;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMConfigurationTransmit extends CMAdministrationTransmit {

	private static final long serialVersionUID = 3564457938839553536L;



	/* Transmit multiple objects*/

	public EquipmentType_Transferable[] transmitEquipmentTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		EquipmentType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EquipmentType) it.next();
			transferables[i] = (EquipmentType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PortType_Transferable[] transmitPortTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		PortType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PortType) it.next();
			transferables[i] = (PortType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		MeasurementPortType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementPortType) it.next();
			transferables[i] = (MeasurementPortType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		TransmissionPathType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (TransmissionPathType) it.next();
			transferables[i] = (TransmissionPathType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public LinkType_Transferable[] transmitLinkTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		LinkType_Transferable[] transferables = new LinkType_Transferable[objects.size()];
		int i = 0;
		LinkType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (LinkType) it.next();
			transferables[i] = (LinkType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		CableLinkType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableLinkType) it.next();
			transferables[i] = (CableLinkType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		CableThreadType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableThreadType) it.next();
			transferables[i] = (CableThreadType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Equipment_Transferable[] transmitEquipments(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		Equipment object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Equipment) it.next();
			transferables[i] = (Equipment_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPorts(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		Port object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Port) it.next();
			transferables[i] = (Port_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		TransmissionPath object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISs(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (KIS) it.next();
			transferables[i] = (KIS_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Link_Transferable[] transmitLinks(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		Link object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Link) it.next();
			transferables[i] = (Link_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableThread_Transferable[] transmitCableThreads(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		CableThread object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableThread) it.next();
			transferables[i] = (CableThread_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(idsT);
			Set objects = StorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}


	/* Transmit multiple objects but ids by condition*/

	public EquipmentType_Transferable[] transmitEquipmentTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		EquipmentType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EquipmentType) it.next();
			transferables[i] = (EquipmentType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PortType_Transferable[] transmitPortTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		PortType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PortType) it.next();
			transferables[i] = (PortType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		MeasurementPortType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementPortType) it.next();
			transferables[i] = (MeasurementPortType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public TransmissionPathType_Transferable[] transmitTransmissionPathTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		TransmissionPathType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (TransmissionPathType) it.next();
			transferables[i] = (TransmissionPathType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public LinkType_Transferable[] transmitLinkTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		LinkType_Transferable[] transferables = new LinkType_Transferable[objects.size()];
		int i = 0;
		LinkType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (LinkType) it.next();
			transferables[i] = (LinkType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableLinkType_Transferable[] transmitCableLinkTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		CableLinkType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableLinkType) it.next();
			transferables[i] = (CableLinkType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableThreadType_Transferable[] transmitCableThreadTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		CableThreadType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableThreadType) it.next();
			transferables[i] = (CableThreadType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Equipment_Transferable[] transmitEquipmentsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		Equipment object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Equipment) it.next();
			transferables[i] = (Equipment_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPortsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		Port object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Port) it.next();
			transferables[i] = (Port_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		TransmissionPath object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (KIS) it.next();
			transferables[i] = (KIS_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElementsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Link_Transferable[] transmitLinksButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		Link object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Link) it.next();
			transferables[i] = (Link_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CableThread_Transferable[] transmitCableThreadsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		CableThread object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CableThread) it.next();
			transferables[i] = (CableThread_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				Set ids = Identifier.fromTransferables(idsT);
				Set objects = StorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/*	Refresh*/

	public Identifier_Transferable[] transmitRefreshedConfigurationObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

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
