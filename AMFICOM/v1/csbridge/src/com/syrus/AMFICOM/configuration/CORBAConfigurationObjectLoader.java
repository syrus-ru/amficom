/*
 * $Id: CORBAConfigurationObjectLoader.java,v 1.3 2005/04/27 13:41:27 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.AbstractLinkType_Transferable;
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
import com.syrus.AMFICOM.general.CMServerConnectionManager;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/27 13:41:27 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {

	public CORBAConfigurationObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}



	/* Load single object*/

	public EquipmentType loadEquipmentType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			EquipmentType_Transferable transferable = cmServer.transmitEquipmentType((Identifier_Transferable) id.getTransferable(), securityKey);
			EquipmentType storableObject = new EquipmentType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public PortType loadPortType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			PortType_Transferable transferable = cmServer.transmitPortType((Identifier_Transferable) id.getTransferable(), securityKey);
			PortType storableObject = new PortType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPortType_Transferable transferable = cmServer.transmitMeasurementPortType((Identifier_Transferable) id.getTransferable(), securityKey);
			MeasurementPortType storableObject = new MeasurementPortType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPathType_Transferable transferable = cmServer.transmitTransmissionPathType((Identifier_Transferable) id.getTransferable(), securityKey);
			TransmissionPathType storableObject = new TransmissionPathType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public LinkType loadLinkType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			LinkType_Transferable transferable = cmServer.transmitLinkType((Identifier_Transferable) id.getTransferable(), securityKey);
			LinkType storableObject = new LinkType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public CableLinkType loadCableLinkType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableLinkType_Transferable transferable = cmServer.transmitCableLinkType((Identifier_Transferable) id.getTransferable(), securityKey);
			CableLinkType storableObject = new CableLinkType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public CableThreadType loadCableThreadType(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThreadType_Transferable transferable = cmServer.transmitCableThreadType((Identifier_Transferable) id.getTransferable(), securityKey);
			CableThreadType storableObject = new CableThreadType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}



	public Equipment loadEquipment(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Equipment_Transferable transferable = cmServer.transmitEquipment((Identifier_Transferable) id.getTransferable(), securityKey);
			Equipment storableObject = new Equipment(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Port loadPort(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Port_Transferable transferable = cmServer.transmitPort((Identifier_Transferable) id.getTransferable(), securityKey);
			Port storableObject = new Port(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPort_Transferable transferable = cmServer.transmitMeasurementPort((Identifier_Transferable) id.getTransferable(), securityKey);
			MeasurementPort storableObject = new MeasurementPort(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPath_Transferable transferable = cmServer.transmitTransmissionPath((Identifier_Transferable) id.getTransferable(), securityKey);
			TransmissionPath storableObject = new TransmissionPath(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public KIS loadKIS(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			KIS_Transferable transferable = cmServer.transmitKIS((Identifier_Transferable) id.getTransferable(), securityKey);
			KIS storableObject = new KIS(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MonitoredElement_Transferable transferable = cmServer.transmitMonitoredElement((Identifier_Transferable) id.getTransferable(), securityKey);
			MonitoredElement storableObject = new MonitoredElement(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Link loadLink(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Link_Transferable transferable = cmServer.transmitLink((Identifier_Transferable) id.getTransferable(), securityKey);
			Link storableObject = new Link(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public CableThread loadCableThread(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThread_Transferable transferable = cmServer.transmitCableThread((Identifier_Transferable) id.getTransferable(), securityKey);
			CableThread storableObject = new CableThread(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			EquipmentType_Transferable[] transferables = cmServer.transmitEquipmentTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new EquipmentType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			PortType_Transferable[] transferables = cmServer.transmitPortTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new PortType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPortType_Transferable[] transferables = cmServer.transmitMeasurementPortTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementPortType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPathType_Transferable[] transferables = cmServer.transmitTransmissionPathTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new TransmissionPathType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			AbstractLinkType_Transferable[] transferables = cmServer.transmitLinkTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						try {
							objects.add(new CableLinkType(transferables[i].cableLinkType()));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						try {
							objects.add(new LinkType(transferables[i].linkType()));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
						break;
					default:
						assert false: "Illegal sort of abstract link: " + transferables[i].discriminator().value();
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableLinkType_Transferable[] transferables = cmServer.transmitCableLinkTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableLinkType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThreadType_Transferable[] transferables = cmServer.transmitCableThreadTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableThreadType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	public Set loadEquipments(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Equipment_Transferable[] transferables = cmServer.transmitEquipments(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Equipment(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Port_Transferable[] transferables = cmServer.transmitPorts(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Port(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPort_Transferable[] transferables = cmServer.transmitMeasurementPorts(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementPort(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPath_Transferable[] transferables = cmServer.transmitTransmissionPaths(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new TransmissionPath(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			KIS_Transferable[] transferables = cmServer.transmitKISs(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new KIS(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MonitoredElement_Transferable[] transferables = cmServer.transmitMonitoredElements(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MonitoredElement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Link_Transferable[] transferables = cmServer.transmitLinks(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Link(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThread_Transferable[] transferables = cmServer.transmitCableThreads(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableThread(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			EquipmentType_Transferable[] transferables = cmServer.transmitEquipmentTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new EquipmentType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			PortType_Transferable[] transferables = cmServer.transmitPortTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new PortType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPortType_Transferable[] transferables = cmServer.transmitMeasurementPortTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementPortType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPathType_Transferable[] transferables = cmServer.transmitTransmissionPathTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new TransmissionPathType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			AbstractLinkType_Transferable[] transferables = cmServer.transmitLinkTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				switch (transferables[i].discriminator().value()) {
					case AbstractLinkTypeSort._CABLE_LINK_TYPE:
						try {
							objects.add(new CableLinkType(transferables[i].cableLinkType()));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
						break;
					case AbstractLinkTypeSort._LINK_TYPE:
						try {
							objects.add(new LinkType(transferables[i].linkType()));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
					default:
						assert false: "Illegal sort of abstract link: " + transferables[i].discriminator().value();
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableLinkType_Transferable[] transferables = cmServer.transmitCableLinkTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableLinkType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThreadType_Transferable[] transferables = cmServer.transmitCableThreadTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableThreadType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Equipment_Transferable[] transferables = cmServer.transmitEquipmentsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Equipment(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Port_Transferable[] transferables = cmServer.transmitPortsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Port(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MeasurementPort_Transferable[] transferables = cmServer.transmitMeasurementPortsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MeasurementPort(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			TransmissionPath_Transferable[] transferables = cmServer.transmitTransmissionPathsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new TransmissionPath(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			KIS_Transferable[] transferables = cmServer.transmitKISsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new KIS(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MonitoredElement_Transferable[] transferables = cmServer.transmitMonitoredElementsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MonitoredElement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Link_Transferable[] transferables = cmServer.transmitLinksButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Link(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			CableThread_Transferable[] transferables = cmServer.transmitCableThreadsButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new CableThread(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Save single object*/

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		EquipmentType_Transferable transferable = (EquipmentType_Transferable) equipmentType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveEquipmentType(transferable, force, securityKey);
			equipmentType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + equipmentType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		PortType_Transferable transferable = (PortType_Transferable) portType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receivePortType(transferable, force, securityKey);
			portType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + portType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementPortType_Transferable transferable = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMeasurementPortType(transferable, force, securityKey);
			measurementPortType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + measurementPortType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		TransmissionPathType_Transferable transferable = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveTransmissionPathType(transferable, force, securityKey);
			transmissionPathType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + transmissionPathType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		LinkType_Transferable transferable = (LinkType_Transferable) linkType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveLinkType(transferable, force, securityKey);
			linkType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + linkType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableLinkType_Transferable transferable = (CableLinkType_Transferable) cableLinkType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCableLinkType(transferable, force, securityKey);
			cableLinkType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + cableLinkType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableThreadType_Transferable transferable = (CableThreadType_Transferable) cableThreadType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCableThreadType(transferable, force, securityKey);
			cableThreadType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + cableThreadType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Equipment_Transferable transferable = (Equipment_Transferable) equipment.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveEquipment(transferable, force, securityKey);
			equipment.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + equipment.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Port_Transferable transferable = (Port_Transferable) port.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receivePort(transferable, force, securityKey);
			port.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + port.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementPort_Transferable transferable = (MeasurementPort_Transferable) measurementPort.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMeasurementPort(transferable, force, securityKey);
			measurementPort.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + measurementPort.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		TransmissionPath_Transferable transferable = (TransmissionPath_Transferable) transmissionPath.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveTransmissionPath(transferable, force, securityKey);
			transmissionPath.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + transmissionPath.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		KIS_Transferable transferable = (KIS_Transferable) kis.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveKIS(transferable, force, securityKey);
			kis.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + kis.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MonitoredElement_Transferable transferable = (MonitoredElement_Transferable) monitoredElement.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMonitoredElement(transferable, force, securityKey);
			monitoredElement.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + monitoredElement.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Link_Transferable transferable = (Link_Transferable) link.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveLink(transferable, force, securityKey);
			link.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + link.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableThread(CableThread cableThread, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableThread_Transferable transferable = (CableThread_Transferable) cableThread.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCableThread(transferable, force, securityKey);
			cableThread.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + cableThread.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		EquipmentType_Transferable[] transferables = new EquipmentType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (EquipmentType_Transferable) ((EquipmentType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEquipmentTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		PortType_Transferable[] transferables = new PortType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (PortType_Transferable) ((PortType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePortTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementPortType_Transferable) ((MeasurementPortType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementPortTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		TransmissionPathType_Transferable[] transferables = new TransmissionPathType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (TransmissionPathType_Transferable) ((TransmissionPathType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTransmissionPathTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		AbstractLinkType_Transferable[] transferables = new AbstractLinkType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (AbstractLinkType_Transferable) ((LinkType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveLinkTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableLinkType_Transferable[] transferables = new CableLinkType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableLinkType_Transferable) ((CableLinkType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableLinkTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableThreadType_Transferable[] transferables = new CableThreadType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableThreadType_Transferable) ((CableThreadType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableThreadTypes(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Equipment_Transferable[] transferables = new Equipment_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Equipment_Transferable) ((Equipment) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveEquipments(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Port_Transferable) ((Port) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receivePorts(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MeasurementPort_Transferable) ((MeasurementPort) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMeasurementPorts(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (TransmissionPath_Transferable) ((TransmissionPath) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveTransmissionPaths(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (KIS_Transferable) ((KIS) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveKISs(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MonitoredElement_Transferable) ((MonitoredElement) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMonitoredElements(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Link_Transferable[] transferables = new Link_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Link_Transferable) ((Link) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveLinks(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		CableThread_Transferable[] transferables = new CableThread_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CableThread_Transferable) ((CableThread) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCableThreads(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public Set refresh(Set storableObjects) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		StorableObject_Transferable[] headersT = StorableObject.createHeadersTransferable(storableObjects);

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedConfigurationObjects(headersT, securityKey);

			Set refreshedIds = Identifier.fromTransferables(idsT);
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			throw new ApplicationException(are);
		}
	}

}
