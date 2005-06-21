/*-
 * $Id: MscharServerMapReceive.java,v 1.3 2005/06/21 12:44:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
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
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/21 12:44:26 $
 * @module mscharserver_v1
 */
abstract class MscharServerMapReceive extends MscharServerResourceReceive {
	private static final long serialVersionUID = -8091147854406929055L;

	public final IdlStorableObject[] receiveSiteNodes(
			final SiteNode_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITENODE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTopologicalNodes(
			final TopologicalNode_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TOPOLOGICALNODE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveNodeLinks(
			final NodeLink_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.NODELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMarks(
			final Mark_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MARK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePhysicalLinks(
			final PhysicalLink_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICALLINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCollectors(
			final Collector_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.COLLECTOR_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMaps(
			final Map_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSiteNodeTypes(
			final SiteNodeType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITENODE_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePhysicalLinkTypes(
			final PhysicalLinkType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICALLINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMapViews(
			final MapView_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAPVIEW_CODE, transferables, force, sessionKey);
	}
}
