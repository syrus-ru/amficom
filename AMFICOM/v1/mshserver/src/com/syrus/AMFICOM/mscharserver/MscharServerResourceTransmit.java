/*-
 * $Id: MscharServerResourceTransmit.java,v 1.5 2005/06/25 17:07:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:51 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceTransmit extends MscharServerSchemeReceive {
	MscharServerResourceTransmit(final ORB orb) {
		super(orb);
	}

	public final IdlImageResource[] transmitImageResources(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlImageResource imageResources[] = new IdlImageResource[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}

	public final IdlImageResource[] transmitImageResourcesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlImageResource imageResources[] = new IdlImageResource[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}
}
