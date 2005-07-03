/*-
 * $Id: MscharServerResourceReceive.java,v 1.5 2005/06/25 17:07:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerOperations;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:51 $
 * @module mscharserver_v1
 */
abstract class MscharServerResourceReceive extends ServerCore implements MscharServerOperations {
	MscharServerResourceReceive(final ORB orb) {
		super(orb);
	}

	public final IdlStorableObject[] receiveImageResources(
			final IdlImageResource transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.IMAGERESOURCE_CODE, transferables, force, sessionKey);
	}
}
