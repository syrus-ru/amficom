/*
 * $Id: CMGeneralTransmit.java,v 1.15 2005/03/30 12:53:03 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/03/30 12:53:03 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long serialVersionUID = 6832454869836527727L;

	public ParameterType_Transferable transmitParameterType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitParameterType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			ParameterType parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(id, true);
			return (ParameterType_Transferable) parameterType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public CharacteristicType_Transferable transmitCharacteristicType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			CharacteristicType characteristicType = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(id, true);
			return (CharacteristicType_Transferable) characteristicType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Characteristic_Transferable transmitCharacteristic(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristic | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Characteristic characteristic = (Characteristic) GeneralStorableObjectPool.getStorableObject(id, true);
			return (Characteristic_Transferable) characteristic.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}






	public ParameterType_Transferable[] transmitParameterTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitParameterTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType parameterType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			parameterType = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType characteristicType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristicType = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristics(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristics | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic characteristic;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristic = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
		}
		return transferables;
	}






	public ParameterType_Transferable[] transmitParameterTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitParameterTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType parameterType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			parameterType = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType characteristicType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristicType = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.CHARACTERISTIC_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic characteristic;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristic = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
		}
		return transferables;
	}





	public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitParameterTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType parameterType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			parameterType = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType characteristicType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristicType = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitCharacteristicsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic characteristic;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristic = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
		}
		return transferables;
	}

	private Collection getObjectsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		Collection ids = Identifier.fromTransferables(identifier_Transferables);

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Collection objects = null;
		try {
			objects = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		return objects;
	}




	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedGeneralObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMGeneralTransmit.transmitRefreshedGeneralObjects | Refreshing for user '"
				+ accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {

			Map storableObjectsTMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++)
				storableObjectsTMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);

			GeneralStorableObjectPool.refresh();
			Collection storableObjects = GeneralStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same
				// versions -- not only with older ones!
				if (!so.hasNewerVersion(sot.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
}
