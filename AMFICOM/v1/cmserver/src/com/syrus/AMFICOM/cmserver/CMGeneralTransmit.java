/*
 * $Id: CMGeneralTransmit.java,v 1.30 2005/06/17 13:07:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.30 $, $Date: 2005/06/17 13:07:00 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long serialVersionUID = 3185564489691408823L;


	/* Transmit multiple objects*/

	public ParameterType_Transferable[] transmitParameterTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ParameterType_Transferable[] parameterTypes = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable[] characteristicTypes = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public Characteristic_Transferable[] transmitCharacteristics(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Characteristic_Transferable[] characteristics = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}



	/* Transmit multiple objects but ids by condition */

	public ParameterType_Transferable[] transmitParameterTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ParameterType_Transferable[] parameterTypes = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable[] characteristicTypes = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Characteristic_Transferable[] characteristics = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}
}
