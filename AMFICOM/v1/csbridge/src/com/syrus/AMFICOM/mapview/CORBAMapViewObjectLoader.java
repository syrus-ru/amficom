/*-
 * $Id: CORBAMapViewObjectLoader.java,v 1.1 2005/06/03 17:57:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.corba.MapView_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/03 17:57:41 $
 * @module csbridge_v1
 */
public final class CORBAMapViewObjectLoader extends CORBAObjectLoader implements MapViewObjectLoader {
	public CORBAMapViewObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public Set loadMapViews(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MAPVIEW_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMapViews(ids1, sessionKey);
			}
		});
	}

	public Set loadMapViewsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MAPVIEW_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMapViewsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public void saveMapViews(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(storableObjects, ObjectEntities.MAPVIEW_ENTITY_CODE, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveMapViews((MapView_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
