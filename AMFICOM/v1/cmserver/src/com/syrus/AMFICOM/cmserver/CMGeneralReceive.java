/*-
 * $Id: CMGeneralReceive.java,v 1.25 2005/06/21 12:44:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.25 $, $Date: 2005/06/21 12:44:31 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMGeneralReceive extends ServerCore implements CMServerOperations {
	private static final long serialVersionUID = 4217287655251415892L;	

	public final IdlStorableObject[] receiveParameterTypes(
			final IdlParameterType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PARAMETER_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCharacteristicTypes(
			final IdlCharacteristicType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CHARACTERISTIC_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCharacteristics(
			final IdlCharacteristic transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CHARACTERISTIC_CODE, transferables, force, sessionKey);
	}
}
