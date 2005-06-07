/*-
 * $Id: MscharServerResourceTransmit.java,v 1.1 2005/06/07 16:47:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceTransmit extends MscharServerSchemeReceive {
	public final ImageResource_Transferable[] transmitImageResources(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ImageResource_Transferable imageResources[] = new ImageResource_Transferable[length];
		System.arraycopy(storableObjects, 0, imageResources, 0, length);
		return imageResources;
	}

	public final ImageResource_Transferable[] transmitImageResourcesButIdsCondition(
			final Identifier_Transferable ids[],
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
