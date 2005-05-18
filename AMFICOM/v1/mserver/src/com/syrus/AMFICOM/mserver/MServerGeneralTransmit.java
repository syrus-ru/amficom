/*
 * $Id: MServerGeneralTransmit.java,v 1.2 2005/05/18 13:25:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:25:44 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerGeneralTransmit extends MServerMeasurementReceive {

	private static final long serialVersionUID = 1312455830273815032L;

	/* Transmit multiple objects*/

	public ParameterType_Transferable[] transmitParameterTypes(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getGeneralObjects(idsT);

		ParameterType_Transferable[] transferables = new ParameterType_Transferable[objects.size()];
		int i = 0;
		ParameterType parameterType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			parameterType = (ParameterType) it.next();
			transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
		}
		return transferables;
	}

	public CharacteristicType_Transferable[] transmitCharacteristicTypes(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getGeneralObjects(idsT);

		CharacteristicType_Transferable[] transferables = new CharacteristicType_Transferable[objects.size()];
		int i = 0;
		CharacteristicType characteristicType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristicType = (CharacteristicType) it.next();
			transferables[i] = (CharacteristicType_Transferable) characteristicType.getTransferable();
		}
		return transferables;
	}

	public Characteristic_Transferable[] transmitCharacteristics(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getGeneralObjects(idsT);

		Characteristic_Transferable[] transferables = new Characteristic_Transferable[objects.size()];
		int i = 0;
		Characteristic characteristic;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			characteristic = (Characteristic) it.next();
			transferables[i] = (Characteristic_Transferable) characteristic.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getGeneralObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = StorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
