/*-
 * $Id: CORBAResourceObjectLoader.java,v 1.14 2005/06/21 12:44:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/06/21 12:44:28 $
 * @module csbridge_v1
 */
public final class CORBAResourceObjectLoader extends CORBAObjectLoader implements ResourceObjectLoader {

	public CORBAResourceObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadImageResources(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.IMAGERESOURCE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitImageResources(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.IMAGERESOURCE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitImageResourcesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveImageResources(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.IMAGERESOURCE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveImageResources((ImageResource_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
