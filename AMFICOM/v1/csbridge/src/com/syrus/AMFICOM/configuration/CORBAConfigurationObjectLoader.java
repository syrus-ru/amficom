/*
 * $Id: CORBAConfigurationObjectLoader.java,v 1.5 2005/05/01 17:29:09 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/05/01 17:29:09 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAConfigurationObjectLoader extends CORBAObjectLoader implements ConfigurationObjectLoader {

	public CORBAConfigurationObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
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
			LinkType_Transferable[] transferables = cmServer.transmitLinkTypes(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new LinkType(transferables[i]));
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
			LinkType_Transferable[] transferables = cmServer.transmitLinkTypesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new LinkType(transferables[i]));
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

		LinkType_Transferable[] transferables = new LinkType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (LinkType_Transferable) ((LinkType) it.next()).getTransferable();

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



	/*	Refresh*/

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
