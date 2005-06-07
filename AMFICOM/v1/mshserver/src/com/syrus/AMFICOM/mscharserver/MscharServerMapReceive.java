/*-
 * $Id: MscharServerMapReceive.java,v 1.1 2005/06/07 16:47:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.MapView_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @module mscharserver_v1
 */
abstract class MscharServerMapReceive extends MscharServerResourceReceive {
	private static final long serialVersionUID = -8091147854406929055L;

	public final StorableObject_Transferable[] receiveSiteNodes(
			final SiteNode_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITE_NODE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveTopologicalNodes(
			final TopologicalNode_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveNodeLinks(
			final NodeLink_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.NODE_LINK_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMarks(
			final Mark_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MARK_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePhysicalLinks(
			final PhysicalLink_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCollectors(
			final Collector_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.COLLECTOR_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMaps(
			final Map_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAP_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSiteNodeTypes(
			final SiteNodeType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePhysicalLinkTypes(
			final PhysicalLinkType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMapViews(
			final MapView_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAPVIEW_ENTITY_CODE, transferables, force, sessionKey);
	}
}
