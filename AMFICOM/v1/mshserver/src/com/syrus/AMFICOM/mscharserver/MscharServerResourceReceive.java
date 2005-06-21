/*-
 * $Id: MscharServerResourceReceive.java,v 1.3 2005/06/21 12:44:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerOperations;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/21 12:44:26 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceReceive extends ServerCore implements MscharServerOperations {
	public final IdlStorableObject[] receiveImageResources(
			final ImageResource_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.IMAGERESOURCE_CODE, transferables, force, sessionKey);
	}
}
