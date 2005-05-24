/*
 * $Id: CMGeneralTransmit.java,v 1.25 2005/05/24 15:08:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.25 $, $Date: 2005/05/24 15:08:15 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMGeneralTransmit extends CMMeasurementReceive {
	private static final long serialVersionUID = 3185564489691408823L;

	public ParameterType_Transferable[] transmitParameterTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ParameterType_Transferable parameterTypes[] = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final ParameterType_Transferable parameterTypes[] = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable characteristicTypes[] = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable characteristicTypes[] = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public Characteristic_Transferable[] transmitCharacteristics(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Characteristic_Transferable characteristics[] = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Characteristic_Transferable characteristics[] = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}

	/*	Refresh*/

	/**
	 * @deprecated
	 */
	public Identifier_Transferable[] transmitRefreshedGeneralObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
		final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			StorableObjectPool.refresh();

			Set storableObjects = StorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}

}
