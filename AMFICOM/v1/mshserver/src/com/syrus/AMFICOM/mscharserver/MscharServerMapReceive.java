/*-
 * $Id: MscharServerMapReceive.java,v 1.5 2005/06/25 17:07:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.IdlCollector;
import com.syrus.AMFICOM.map.corba.IdlMapView;
import com.syrus.AMFICOM.map.corba.IdlMap;
import com.syrus.AMFICOM.map.corba.IdlMark;
import com.syrus.AMFICOM.map.corba.IdlNodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLink;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:51 $
 * @module mscharserver_v1
 */
abstract class MscharServerMapReceive extends MscharServerResourceReceive {
	private static final long serialVersionUID = -8091147854406929055L;

	MscharServerMapReceive(final ORB orb) {
		super(orb);
	}

	public final IdlStorableObject[] receiveSiteNodes(
			final IdlSiteNode transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITENODE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTopologicalNodes(
			final IdlTopologicalNode transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TOPOLOGICALNODE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveNodeLinks(
			final IdlNodeLink transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.NODELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMarks(
			final IdlMark transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MARK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePhysicalLinks(
			final IdlPhysicalLink transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICALLINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCollectors(
			final IdlCollector transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.COLLECTOR_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMaps(
			final IdlMap transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSiteNodeTypes(
			final IdlSiteNodeType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SITENODE_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePhysicalLinkTypes(
			final IdlPhysicalLinkType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PHYSICALLINK_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMapViews(
			final IdlMapView transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MAPVIEW_CODE, transferables, force, sessionKey);
	}
}
