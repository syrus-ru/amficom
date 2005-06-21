/*
 * $Id: CMGeneralTransmit.java,v 1.31 2005/06/21 12:44:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.31 $, $Date: 2005/06/21 12:44:32 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long serialVersionUID = 3185564489691408823L;


	/* Transmit multiple objects*/

	public IdlParameterType[] transmitParameterTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlParameterType[] parameterTypes = new IdlParameterType[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public IdlCharacteristicType[] transmitCharacteristicTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCharacteristicType[] characteristicTypes = new IdlCharacteristicType[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public IdlCharacteristic[] transmitCharacteristics(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCharacteristic[] characteristics = new IdlCharacteristic[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}



	/* Transmit multiple objects but ids by condition */

	public IdlParameterType[] transmitParameterTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlParameterType[] parameterTypes = new IdlParameterType[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public IdlCharacteristicType[] transmitCharacteristicTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCharacteristicType[] characteristicTypes = new IdlCharacteristicType[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public IdlCharacteristic[] transmitCharacteristicsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCharacteristic[] characteristics = new IdlCharacteristic[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}
}
