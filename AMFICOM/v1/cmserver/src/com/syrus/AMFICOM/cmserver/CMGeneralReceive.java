/*
 * $Id: CMGeneralReceive.java,v 1.19 2005/05/18 13:11:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.19 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMGeneralReceive implements CMServerOperations {
	private static final long serialVersionUID = 4217287655251415892L;	

	/**
	 * TODO Meaningful implementation*/
	final Identifier validateAccess(SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		try {
			LoginServer loginServer = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getLoginServerReference();

			Identifier_TransferableHolder userIdTHolder = new Identifier_TransferableHolder();
			Identifier_TransferableHolder domainIdTHolder = new Identifier_TransferableHolder();
			loginServer.validateAccess(sessionKeyT, userIdTHolder, domainIdTHolder);
			return new Identifier(userIdTHolder.value);
		}
		catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (final Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	public StorableObject_Transferable[] receiveParameterTypes(ParameterType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = this.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			ParameterType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (ParameterType) GeneralStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new ParameterType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCharacteristicTypes(CharacteristicType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = this.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			CharacteristicType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (CharacteristicType) GeneralStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new CharacteristicType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCharacteristics(Characteristic_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = this.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Characteristic object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Characteristic) GeneralStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Characteristic(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
