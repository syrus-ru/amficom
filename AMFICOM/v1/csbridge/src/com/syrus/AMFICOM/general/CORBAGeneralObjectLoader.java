/*
 * $Id: CORBAGeneralObjectLoader.java,v 1.2 2005/04/22 14:53:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 14:53:58 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class CORBAGeneralObjectLoader extends AbstractObjectLoader implements GeneralObjectLoader {
	private CMServerConnectionManager cmServerConnectionManager;

	public CORBAGeneralObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		this.cmServerConnectionManager = cmServerConnectionManager;
	}




	public ParameterType loadParameterType(Identifier id) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			ParameterType_Transferable transferable = cmServer.transmitParameterType((Identifier_Transferable) id.getTransferable(),
					ait);
			ParameterType storableObject = new ParameterType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			CharacteristicType_Transferable transferable = cmServer.transmitCharacteristicType((Identifier_Transferable) id.getTransferable(),
					ait);
			CharacteristicType storableObject = new CharacteristicType(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Characteristic loadCharacteristic(Identifier id) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			Characteristic_Transferable transferable = cmServer.transmitCharacteristic((Identifier_Transferable) id.getTransferable(), ait);
			Characteristic storableObject = new Characteristic(transferable);
			return storableObject;
			
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}




	public Set loadParameterTypes(Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			ParameterType_Transferable[] transferables = cmServer.transmitParameterTypes(idsT, ait);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			CharacteristicType_Transferable[] transferables = cmServer.transmitCharacteristicTypes(idsT, ait);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new CharacteristicType(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			Characteristic_Transferable[] transferables = cmServer.transmitCharacteristics(idsT, ait);
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
			throw new RetrieveObjectException(are.message);
		}
	}




	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			ParameterType_Transferable[] transferables = cmServer.transmitParameterTypesButIdsCondition(idsT, ait, conditionT);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			CharacteristicType_Transferable[] transferables = cmServer.transmitCharacteristicTypesButIdsCondition(idsT, ait, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new CharacteristicType(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		try {
			Characteristic_Transferable[] transferables = cmServer.transmitCharacteristicsButIdsCondition(idsT, ait, conditionT);
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
			throw new RetrieveObjectException(are.message);
		}
	}




	public void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		ParameterType_Transferable transferable = (ParameterType_Transferable) parameterType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveParameterType(transferable, force, ait);
			parameterType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + parameterType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		CharacteristicType_Transferable transferable = (CharacteristicType_Transferable) characteristicType.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCharacteristicType(transferable, force, ait);
			characteristicType.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + characteristicType.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		Characteristic_Transferable transferable = (Characteristic_Transferable) characteristic.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveCharacteristic(transferable, force, ait);
			characteristic.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + characteristic.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}




	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ParameterType_Transferable) ((ParameterType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveParameterTypes(transferables, force, ait);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (CharacteristicType_Transferable) ((CharacteristicType) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCharacteristicTypes(transferables, force, ait);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();
		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Characteristic_Transferable) ((Characteristic) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveCharacteristics(transferables, force, ait);
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
		CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
		AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

		StorableObject_Transferable[] headersT = new StorableObject_Transferable[storableObjects.size()];
		int i = 0;
		for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
			final StorableObject storableObject = (StorableObject) it.next();
			headersT[i] = storableObject.getHeaderTransferable();
		}

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedGeneralObjects(headersT, ait);

			Set refreshedIds = new HashSet(idsT.length);
			for (i = 0; i < idsT.length; i++) 
				refreshedIds.add(new Identifier(idsT[i]));
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			throw new ApplicationException(are);
		}
	}



	public void delete(Identifier id) {
		try {
			CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
			AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

			Identifier_Transferable idT = (Identifier_Transferable) id.getTransferable();
			cmServer.delete(idT, ait);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CORBAGeneralObjectLoader.delete | Cannot delete object '" + id + "'" + are.message);
		}
	}

	public void delete(Set identifiables) {
		try {
			CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
			AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

			Identifier_Transferable[] idsT = Identifier.createTransferables(identifiables);
			cmServer.deleteList(idsT, ait);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CORBAGeneralObjectLoader.delete | Cannot delete objects '" + identifiables + "'" + are.message);
		}
	}

}
