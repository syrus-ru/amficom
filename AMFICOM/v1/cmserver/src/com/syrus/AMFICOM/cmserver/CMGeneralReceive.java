/*
 * $Id: CMGeneralReceive.java,v 1.14 2005/04/23 13:36:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServerPOA;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/04/23 13:36:32 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMGeneralReceive extends CMServerPOA {
	private static final long serialVersionUID = 4217287655251415892L;	

	final StorableObject_Transferable[] getListHeaders(Set storableObjects) {
		StorableObject_Transferable[] headers = new StorableObject_Transferable[storableObjects.size()];
		int i=0;
		for (Iterator it = storableObjects.iterator(); it.hasNext();i++) 
			headers[i] = ((StorableObject) it.next()).getHeaderTransferable();
		return headers;
	}

	public StorableObject_Transferable receiveParameterType(ParameterType_Transferable parameterType_Transferable,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMGeneralReceive.receiveParameterType | Received " + " parameterType", Log.DEBUGLEVEL07);
		try {
			ParameterType parameterType = new ParameterType(parameterType_Transferable);
			GeneralStorableObjectPool.putStorableObject(parameterType);
			ParameterTypeDatabase parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
			parameterTypeDatabase.update(parameterType, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return parameterType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveParameterTypes(ParameterType_Transferable[] parameterType_Transferables,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMGeneralReceive.receiveParameterTypes | Received "
				+ parameterType_Transferables.length
				+ " ParameterTypes", Log.DEBUGLEVEL07);
		Set parameterTypeList = new HashSet(parameterType_Transferables.length);
		try {
			for (int i = 0; i < parameterType_Transferables.length; i++) {
				ParameterType parameterType = new ParameterType(parameterType_Transferables[i]);
				GeneralStorableObjectPool.putStorableObject(parameterType);
				parameterTypeList.add(parameterType);
			}
			ParameterTypeDatabase parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
			parameterTypeDatabase.update(parameterTypeList, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(parameterTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveCharacteristic(Characteristic_Transferable characteristic_Transferable,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		Log.debugMessage("CMGeneralReceive.receiveCharacteristic | Received " + " characteristic", Log.DEBUGLEVEL07);
		try {
			Characteristic characteristic = new Characteristic(characteristic_Transferable);
			GeneralStorableObjectPool.putStorableObject(characteristic);
			CharacteristicDatabase characteristicDatabase = GeneralDatabaseContext.getCharacteristicDatabase();
			characteristicDatabase.update(characteristic, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return characteristic.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCharacteristics(Characteristic_Transferable[] characteristic_Transferables,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		Log.debugMessage("CMGeneralReceive.receiveCharacteristics | Received "
				+ characteristic_Transferables.length
				+ " characteristics", Log.DEBUGLEVEL07);
		Set characteristicList = new HashSet(characteristic_Transferables.length);
		try {
			for (int i = 0; i < characteristic_Transferables.length; i++) {
				Characteristic characteristic = new Characteristic(characteristic_Transferables[i]);
				GeneralStorableObjectPool.putStorableObject(characteristic);
				characteristicList.add(characteristic);
			}

			CharacteristicDatabase characteristicDatabase = GeneralDatabaseContext.getCharacteristicDatabase();
			characteristicDatabase.update(characteristicList, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(characteristicList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveCharacteristicType(CharacteristicType_Transferable characteristicType_Transferable,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		Log.debugMessage("CMGeneralReceive.receiveCharacteristicType | Received " + " characteristicTypes", Log.DEBUGLEVEL07);
		try {
			CharacteristicType characteristicType = new CharacteristicType(characteristicType_Transferable);
			GeneralStorableObjectPool.putStorableObject(characteristicType);
			CharacteristicTypeDatabase characteristicTypeDatabase = GeneralDatabaseContext.getCharacteristicTypeDatabase();
			characteristicTypeDatabase.update(characteristicType, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return characteristicType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCharacteristicTypes(CharacteristicType_Transferable[] characteristicType_Transferables,
			boolean force,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		Log.debugMessage("CMGeneralReceive.receivecharacteristicTypes | Received "
				+ characteristicType_Transferables.length
				+ " characteristicTypes", Log.DEBUGLEVEL07);
		Set characteristicTypeList = new HashSet(characteristicType_Transferables.length);
		try {
			for (int i = 0; i < characteristicType_Transferables.length; i++) {
				CharacteristicType characteristicType = new CharacteristicType(characteristicType_Transferables[i]);
				GeneralStorableObjectPool.putStorableObject(characteristicType);
				characteristicTypeList.add(characteristicType);
			}

			CharacteristicTypeDatabase characteristicTypeDatabase = GeneralDatabaseContext.getCharacteristicTypeDatabase();
			characteristicTypeDatabase.update(characteristicTypeList, new Identifier(accessIdentityT.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(characteristicTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

}
