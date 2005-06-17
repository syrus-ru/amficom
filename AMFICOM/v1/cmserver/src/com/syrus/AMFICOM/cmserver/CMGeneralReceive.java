/*-
 * $Id: CMGeneralReceive.java,v 1.24 2005/06/17 11:01:01 bass Exp $
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
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.24 $, $Date: 2005/06/17 11:01:01 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMGeneralReceive extends ServerCore implements CMServerOperations {
	private static final long serialVersionUID = 4217287655251415892L;	

	public final StorableObject_Transferable[] receiveParameterTypes(
			final ParameterType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PARAMETER_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCharacteristicTypes(
			final CharacteristicType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CHARACTERISTIC_TYPE_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCharacteristics(
			final Characteristic_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CHARACTERISTIC_CODE, transferables, force, sessionKey);
	}
}
