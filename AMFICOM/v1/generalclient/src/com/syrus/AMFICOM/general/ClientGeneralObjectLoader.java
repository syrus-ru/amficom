/*
 * $Id: ClientGeneralObjectLoader.java,v 1.1 2005/01/20 08:11:59 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/20 08:11:59 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ClientGeneralObjectLoader implements GeneralObjectLoader {

	private CMServer								server;

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	public ClientGeneralObjectLoader(CMServer server) {
		this.server = server;
	}
	
	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new ParameterType(this.server.transmitParameterType((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
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
					.getTransferable(), accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristicType | server.transmitCharacteristicType("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Characteristic(this.server.transmitCharacteristic(
				(Identifier_Transferable) id.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristic | new Characteristic(" + id.toString()
					+ ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.loadCharacteristic | server.transmitCharacteristic("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ParameterType_Transferable[] transferables = this.server.transmitParameterTypes(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ParameterType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Characteristic_Transferable[] transferables = this.server.transmitCharacteristics(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
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

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CharacteristicType_Transferable[] transferables = this.server.transmitCharacteristicTypes(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CharacteristicType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadParameterTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			ParameterType_Transferable[] transferables;
			if (condition instanceof StringFieldCondition) {
				StringFieldCondition stringFieldCondition = (StringFieldCondition) condition;
				transferables = this.server.transmitParameterTypesButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (StringFieldCondition_Transferable) stringFieldCondition
							.getTransferable());
			} else
				transferables = this.server.transmitParameterTypesButIds(identifierTransferables,
					accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new ParameterType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CharacteristicType_Transferable[] transferables = this.server.transmitCharacteristicTypesButIds(
				identifierTransferables, accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new CharacteristicType(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Characteristic_Transferable[] transferables;
			if (condition instanceof LinkedIdsCondition) {
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) condition;
				transferables = this.server.transmitCharacteristicsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (LinkedIdsCondition_Transferable) linkedIdsCondition
							.getTransferable());
			} else
				transferables = this.server.transmitCharacteristicsButIds(identifierTransferables,
					accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
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

	public void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		ParameterType_Transferable transferables = (ParameterType_Transferable) parameterType.getTransferable();
		try {
			this.server.receiveParameterType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveParameterType | receiveParameterTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}

	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		CharacteristicType_Transferable transferables = (CharacteristicType_Transferable) characteristicType
				.getTransferable();
		try {
			this.server.receiveCharacteristicType(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristicType ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		Characteristic_Transferable transferables = (Characteristic_Transferable) characteristic.getTransferable();
		try {
			this.server.receiveCharacteristic(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristic ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveParameterTypes(List parameterTypes, boolean force) throws DatabaseException,
			CommunicationException, VersionCollisionException {
		ParameterType_Transferable[] transferables = new ParameterType_Transferable[parameterTypes.size()];
		int i = 0;
		for (Iterator it = parameterTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (ParameterType_Transferable) ((ParameterType) it.next()).getTransferable();
		}
		try {
			this.server.receiveParameterTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveParameterTypes | receiveParameterTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (CharacteristicType_Transferable) ((CharacteristicType) it.next()).getTransferable();
		}
		try {
			this.server.receiveCharacteristicTypes(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristicTypes ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Characteristic_Transferable[] transferables = new Characteristic_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Characteristic_Transferable) ((Characteristic) it.next()).getTransferable();
		}
		try {
			this.server.receiveCharacteristics(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.saveCharacteristics ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

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
				accessIdentifierTransferable);

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(List ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientGeneralObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

}
