/*-
 * $Id: CORBAMapViewObjectLoader.java,v 1.10 2005/07/03 19:16:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.mapview.corba.IdlMapView;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/07/03 19:16:26 $
 * @module csbridge_v1
 */
public final class CORBAMapViewObjectLoader extends CORBAObjectLoader implements MapViewObjectLoader {

	public CORBAMapViewObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadMapViews(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MAPVIEW_CODE, ids, new TransmitProcedure() {
			public IdlStorableObject[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMapViews(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadMapViewsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MAPVIEW_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IdlStorableObject[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMapViewsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveMapViews(final Set<MapView> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MAPVIEW_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IdlStorableObject transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveMapViews((IdlMapView[]) transferables, force, sessionKey);
			}
		});
	}
}
