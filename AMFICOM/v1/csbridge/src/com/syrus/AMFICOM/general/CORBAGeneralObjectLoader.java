/*-
 * $Id: CORBAGeneralObjectLoader.java,v 1.14 2005/05/31 14:54:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/05/31 14:54:42 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAGeneralObjectLoader extends CORBAObjectLoader implements GeneralObjectLoader {
	public CORBAGeneralObjectLoader(ServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristicTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCharacteristics(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids*/

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			ParameterType_Transferable[] transferables = cmServer.transmitParameterTypesButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new ParameterType(transferables[i]));
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

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			CharacteristicType_Transferable[] transferables = cmServer.transmitCharacteristicTypesButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new CharacteristicType(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Characteristic_Transferable[] transferables = cmServer.transmitCharacteristicsButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Characteristic(transferables[i]));
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

	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ParameterType_Transferable) ((ParameterType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveParameterTypes(transferables, force, sessionKeyT);
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

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CharacteristicType_Transferable) ((CharacteristicType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCharacteristicTypes(transferables, force, sessionKeyT);
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

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Characteristic_Transferable) ((Characteristic) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCharacteristics(transferables, force, sessionKeyT);
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
}
