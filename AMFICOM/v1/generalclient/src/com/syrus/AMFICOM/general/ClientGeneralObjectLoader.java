/*
 * $Id: ClientGeneralObjectLoader.java,v 1.8 2005/02/25 09:16:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2005/02/25 09:16:15 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ClientGeneralObjectLoader implements GeneralObjectLoader {

	private CMServer								server;

	public ClientGeneralObjectLoader(CMServer server) {
		this.server = server;
	}
	
	private AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new ParameterType(this.server.transmitParameterType((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.transmitParameterType | server.transmitParameterType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new CharacteristicType(this.server.transmitCharacteristicType((Identifier_Transferable) id
					.getTransferable(), getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristicType | server.transmitCharacteristicType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Characteristic(this.server.transmitCharacteristic(
				(Identifier_Transferable) id.getTransferable(), getAccessIdentifierTransferable()));
		} catch (CreateObjectException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristic | new Characteristic(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristic | server.transmitCharacteristic("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Collection loadParameterTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ParameterType_Transferable[] transferables = this.server.transmitParameterTypes(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ParameterType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCharacteristics(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Characteristic_Transferable[] transferables = this.server.transmitCharacteristics(identifierTransferables,
				getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Characteristic(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCharacteristicTypes(Collection ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CharacteristicType_Transferable[] transferables = this.server.transmitCharacteristicTypes(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CharacteristicType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			ParameterType_Transferable[] transferables = this.server.transmitParameterTypesButIdsCondition(
				identifierTransferables, getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ParameterType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CharacteristicType_Transferable[] transferables = this.server.transmitCharacteristicTypesButIds(
				identifierTransferables, getAccessIdentifierTransferable());
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CharacteristicType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Characteristic_Transferable[] transferables = this.server.transmitCharacteristicsButIdsCondition(
				identifierTransferables, getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Collection list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Characteristic(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	private void updateStorableObjectHeader(Collection storableObjects, StorableObject_Transferable[] transferables) {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier_Transferable id = (Identifier_Transferable) storableObject.getId().getTransferable();
			for (int i = 0; i < transferables.length; i++) {
				if (transferables[i].id.equals(id)) {
					storableObject.updateFromHeaderTransferable(transferables[i]);
				}
			}
		}
	}

	public void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		ParameterType_Transferable transferables = (ParameterType_Transferable) parameterType.getTransferable();
		try {
			parameterType.updateFromHeaderTransferable(this.server.receiveParameterType(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveParameterType | receiveParameterTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}

	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		CharacteristicType_Transferable transferables = (CharacteristicType_Transferable) characteristicType
				.getTransferable();
		try {
			characteristicType.updateFromHeaderTransferable(this.server.receiveCharacteristicType(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristicType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Characteristic_Transferable transferables = (Characteristic_Transferable) characteristic.getTransferable();
		try {
			characteristic.updateFromHeaderTransferable(this.server.receiveCharacteristic(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristic ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveParameterTypes(Collection parameterTypes, boolean force) throws DatabaseException,
			CommunicationException, VersionCollisionException {
		ParameterType_Transferable[] transferables = new ParameterType_Transferable[parameterTypes.size()];
		int i = 0;
		for (Iterator it = parameterTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (ParameterType_Transferable) ((ParameterType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(parameterTypes, this.server.receiveParameterTypes(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveParameterTypes | receiveParameterTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristicTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CharacteristicType_Transferable) ((CharacteristicType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveCharacteristicTypes(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristicTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristics(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Characteristic_Transferable) ((Characteristic) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(list, this.server.receiveCharacteristics(transferables, force,
				getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristics ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException {
		try {
			java.util.Set refreshedIds = new HashSet();
			Identifier_Transferable[] identifier_Transferables;
			StorableObject_Transferable[] storableObject_Transferables = new StorableObject_Transferable[storableObjects
					.size()];
			int i = 0;
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObject_Transferables[i] = storableObject.getHeaderTransferable();
			}
			identifier_Transferables = this.server.transmitRefreshedMeasurementObjects(storableObject_Transferables,
				getAccessIdentifierTransferable());

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public void delete(Identifier id) throws IllegalDataException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new IllegalDataException(msg, e);
		}
	}

	public void delete(Collection ids) throws IllegalDataException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.delete | AMFICOMRemoteException ";
			throw new IllegalDataException(msg, e);
		}
	}

}
