/*
 * $Id: CMGeneralTransmit.java,v 1.28 2005/06/07 13:55:59 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.28 $, $Date: 2005/06/07 13:55:59 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMGeneralTransmit extends CMMeasurementReceive {

	private static final long serialVersionUID = 3185564489691408823L;


	/* Transmit multiple objects*/

	public ParameterType_Transferable[] transmitParameterTypes(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ParameterType_Transferable[] parameterTypes = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable[] characteristicTypes = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public Characteristic_Transferable[] transmitCharacteristics(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Characteristic_Transferable[] characteristics = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}



	/* Transmit multiple objects but ids by condition */

	public ParameterType_Transferable[] transmitParameterTypesButIdsByCondition(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT,
			final StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ParameterType_Transferable[] parameterTypes = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, parameterTypes, 0, length);
		return parameterTypes;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypesButIdsByCondition(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT,
			final StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CharacteristicType_Transferable[] characteristicTypes = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristicTypes, 0, length);
		return characteristicTypes;
	}

	public Characteristic_Transferable[] transmitCharacteristicsButIdsByCondition(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT,
			final StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Characteristic_Transferable[] characteristics = new Characteristic_Transferable[length];
		System.arraycopy(storableObjects, 0, characteristics, 0, length);
		return characteristics;
	}
}
