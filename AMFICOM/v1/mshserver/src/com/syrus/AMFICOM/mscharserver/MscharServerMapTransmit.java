/*-
 * $Id: MscharServerMapTransmit.java,v 1.4 2005/06/24 10:41:46 bass Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/06/24 10:41:46 $
 * @module mscharserver_v1
 */
abstract class MscharServerMapTransmit extends MscharServerResourceTransmit {
	private static final long serialVersionUID = 8672802938668842557L;

	public IdlSiteNode[] transmitSiteNodes(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSiteNode siteNodes[] = new IdlSiteNode[length];
		System.arraycopy(storableObjects, 0, siteNodes, 0, length);
		return siteNodes;
	}

	public IdlSiteNode[] transmitSiteNodesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSiteNode siteNodes[] = new IdlSiteNode[length];
		System.arraycopy(storableObjects, 0, siteNodes, 0, length);
		return siteNodes;
	}

	public IdlTopologicalNode[] transmitTopologicalNodes(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlTopologicalNode topologicalNodes[] = new IdlTopologicalNode[length];
		System.arraycopy(storableObjects, 0, topologicalNodes, 0, length);
		return topologicalNodes;
	}

	public IdlTopologicalNode[] transmitTopologicalNodesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlTopologicalNode topologicalNodes[] = new IdlTopologicalNode[length];
		System.arraycopy(storableObjects, 0, topologicalNodes, 0, length);
		return topologicalNodes;
	}

	public IdlNodeLink[] transmitNodeLinks(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlNodeLink nodeLinks[] = new IdlNodeLink[length];
		System.arraycopy(storableObjects, 0, nodeLinks, 0, length);
		return nodeLinks;
	}

	public IdlNodeLink[] transmitNodeLinksButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlNodeLink nodeLinks[] = new IdlNodeLink[length];
		System.arraycopy(storableObjects, 0, nodeLinks, 0, length);
		return nodeLinks;
	}

	public IdlMark[] transmitMarks(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlMark marks[] = new IdlMark[length];
		System.arraycopy(storableObjects, 0, marks, 0, length);
		return marks;
	}

	public IdlMark[] transmitMarksButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlMark marks[] = new IdlMark[length];
		System.arraycopy(storableObjects, 0, marks, 0, length);
		return marks;
	}

	public IdlPhysicalLink[] transmitPhysicalLinks(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlPhysicalLink physicalLinks[] = new IdlPhysicalLink[length];
		System.arraycopy(storableObjects, 0, physicalLinks, 0, length);
		return physicalLinks;
	}

	public IdlPhysicalLink[] transmitPhysicalLinksButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlPhysicalLink physicalLinks[] = new IdlPhysicalLink[length];
		System.arraycopy(storableObjects, 0, physicalLinks, 0, length);
		return physicalLinks;
	}

	public IdlCollector[] transmitCollectors(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlCollector collectors[] = new IdlCollector[length];
		System.arraycopy(storableObjects, 0, collectors, 0, length);
		return collectors;
	}

	public IdlCollector[] transmitCollectorsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlCollector collectors[] = new IdlCollector[length];
		System.arraycopy(storableObjects, 0, collectors, 0, length);
		return collectors;
	}

	public IdlMap[] transmitMaps(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlMap maps[] = new IdlMap[length];
		System.arraycopy(storableObjects, 0, maps, 0, length);
		return maps;
	}

	public IdlMap[] transmitMapsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlMap maps[] = new IdlMap[length];
		System.arraycopy(storableObjects, 0, maps, 0, length);
		return maps;
	}

	public IdlSiteNodeType[] transmitSiteNodeTypes(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSiteNodeType siteNodeTypes[] = new IdlSiteNodeType[length];
		System.arraycopy(storableObjects, 0, siteNodeTypes, 0, length);
		return siteNodeTypes;
	}

	public IdlSiteNodeType[] transmitSiteNodeTypesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSiteNodeType siteNodeTypes[] = new IdlSiteNodeType[length];
		System.arraycopy(storableObjects, 0, siteNodeTypes, 0, length);
		return siteNodeTypes;
	}

	public IdlPhysicalLinkType[] transmitPhysicalLinkTypes(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlPhysicalLinkType physicalLinkTypes[] = new IdlPhysicalLinkType[length];
		System.arraycopy(storableObjects, 0, physicalLinkTypes, 0, length);
		return physicalLinkTypes;
	}

	public IdlPhysicalLinkType[] transmitPhysicalLinkTypesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlPhysicalLinkType physicalLinkTypes[] = new IdlPhysicalLinkType[length];
		System.arraycopy(storableObjects, 0, physicalLinkTypes, 0, length);
		return physicalLinkTypes;
	}

	public final IdlMapView[] transmitMapViews(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlMapView mapViews[] = new IdlMapView[length];
		System.arraycopy(storableObjects, 0, mapViews, 0, length);
		return mapViews;
	}

	public final IdlMapView[] transmitMapViewsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlMapView mapViews[] = new IdlMapView[length];
		System.arraycopy(storableObjects, 0, mapViews, 0, length);
		return mapViews;
	}
}
