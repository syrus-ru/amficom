/*-
 * $Id: MscharServerResourceTransmit.java,v 1.3 2005/06/21 12:44:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/21 12:44:26 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceTransmit extends MscharServerSchemeReceive {
	public final ImageResource_Transferable[] transmitImageResources(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ImageResource_Transferable imageResources[] = new ImageResource_Transferable[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}

	public final ImageResource_Transferable[] transmitImageResourcesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final ImageResource_Transferable imageResources[] = new ImageResource_Transferable[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}
}
