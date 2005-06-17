/*-
 * $Id: MscharServerResourceTransmit.java,v 1.2 2005/06/17 13:06:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/17 13:06:59 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceTransmit extends MscharServerSchemeReceive {
	public final ImageResource_Transferable[] transmitImageResources(
			final IdlIdentifier ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ImageResource_Transferable imageResources[] = new ImageResource_Transferable[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}

	public final ImageResource_Transferable[] transmitImageResourcesButIdsCondition(
			final IdlIdentifier ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final ImageResource_Transferable imageResources[] = new ImageResource_Transferable[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}
}
