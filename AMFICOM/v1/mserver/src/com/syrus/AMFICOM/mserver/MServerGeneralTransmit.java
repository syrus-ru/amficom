/*
 * $Id: MServerGeneralTransmit.java,v 1.8 2005/06/21 12:44:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.8 $, $Date: 2005/06/21 12:44:29 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerGeneralTransmit extends MServerMeasurementReceive {

	private static final long serialVersionUID = 1312455830273815032L;


	/* Transmit multiple objects*/

	public IdlParameterType[] transmitParameterTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlParameterType[] ret = new IdlParameterType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlCharacteristicType[] transmitCharacteristicTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlCharacteristicType[] ret = new IdlCharacteristicType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlCharacteristic[] transmitCharacteristics(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlCharacteristic[] ret = new IdlCharacteristic[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}



	/* Transmit multiple objects but ids by condition*/

	public IdlParameterType[] transmitParameterTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlParameterType[] ret = new IdlParameterType[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
