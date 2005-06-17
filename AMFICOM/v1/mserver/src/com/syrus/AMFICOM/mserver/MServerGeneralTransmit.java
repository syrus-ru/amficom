/*
 * $Id: MServerGeneralTransmit.java,v 1.7 2005/06/17 13:07:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/17 13:07:01 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerGeneralTransmit extends MServerMeasurementReceive {

	private static final long serialVersionUID = 1312455830273815032L;


	/* Transmit multiple objects*/

	public ParameterType_Transferable[] transmitParameterTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final ParameterType_Transferable[] ret = new ParameterType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final CharacteristicType_Transferable[] ret = new CharacteristicType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public Characteristic_Transferable[] transmitCharacteristics(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Characteristic_Transferable[] ret = new Characteristic_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}



	/* Transmit multiple objects but ids by condition*/

	public ParameterType_Transferable[] transmitParameterTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ParameterType_Transferable[] ret = new ParameterType_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
