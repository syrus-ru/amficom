/*
 * $Id: CORBAConfigurationObjectLoader.java,v 1.10 2005/05/21 19:49:41 arseniy Exp $
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
import com.syrus.AMFICOM.general.LoginException;
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
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/21 19:49:41 $
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
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			EquipmentType_Transferable[] transferables = cmServer.transmitEquipmentTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			PortType_Transferable[] transferables = cmServer.transmitPortTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MeasurementPortType_Transferable[] transferables = cmServer.transmitMeasurementPortTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			TransmissionPathType_Transferable[] transferables = cmServer.transmitTransmissionPathTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			LinkType_Transferable[] transferables = cmServer.transmitLinkTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableLinkType_Transferable[] transferables = cmServer.transmitCableLinkTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableThreadType_Transferable[] transferables = cmServer.transmitCableThreadTypes(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}



	public Set loadEquipments(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Equipment_Transferable[] transferables = cmServer.transmitEquipments(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Port_Transferable[] transferables = cmServer.transmitPorts(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MeasurementPort_Transferable[] transferables = cmServer.transmitMeasurementPorts(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			TransmissionPath_Transferable[] transferables = cmServer.transmitTransmissionPaths(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			KIS_Transferable[] transferables = cmServer.transmitKISs(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MonitoredElement_Transferable[] transferables = cmServer.transmitMonitoredElements(idsT, sessionKeyT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++)
				try {
					objects.add(new MonitoredElement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Link_Transferable[] transferables = cmServer.transmitLinks(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableThread_Transferable[] transferables = cmServer.transmitCableThreads(idsT, sessionKeyT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			EquipmentType_Transferable[] transferables = cmServer.transmitEquipmentTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			PortType_Transferable[] transferables = cmServer.transmitPortTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MeasurementPortType_Transferable[] transferables = cmServer.transmitMeasurementPortTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			TransmissionPathType_Transferable[] transferables = cmServer.transmitTransmissionPathTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			LinkType_Transferable[] transferables = cmServer.transmitLinkTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableLinkType_Transferable[] transferables = cmServer.transmitCableLinkTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableThreadType_Transferable[] transferables = cmServer.transmitCableThreadTypesButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}



	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Equipment_Transferable[] transferables = cmServer.transmitEquipmentsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Port_Transferable[] transferables = cmServer.transmitPortsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MeasurementPort_Transferable[] transferables = cmServer.transmitMeasurementPortsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			TransmissionPath_Transferable[] transferables = cmServer.transmitTransmissionPathsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			KIS_Transferable[] transferables = cmServer.transmitKISsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MonitoredElement_Transferable[] transferables = cmServer.transmitMonitoredElementsButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++)
				objects.add(new MonitoredElement(transferables[i]));
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Link_Transferable[] transferables = cmServer.transmitLinksButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CableThread_Transferable[] transferables = cmServer.transmitCableThreadsButIdsCondition(idsT, sessionKeyT, conditionT);
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
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
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



	/*	Refresh*/

	public Set refresh(Set storableObjects) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		StorableObject_Transferable[] headersT = StorableObject.createHeadersTransferable(storableObjects);

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedConfigurationObjects(headersT, sessionKeyT);

			Set refreshedIds = Identifier.fromTransferables(idsT);
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new ApplicationException(are);
		}
	}

}
