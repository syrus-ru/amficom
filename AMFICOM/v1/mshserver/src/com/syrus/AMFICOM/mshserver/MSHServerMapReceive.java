/*-
 * $Id: MSHServerMapReceive.java,v 1.3 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServerOperations;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectFactory;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:47:53 $
 * @module mshserver_v1
 */
abstract class MSHServerMapReceive extends SchemeStorableObjectFactory implements MSHServerOperations {
	final StorableObject_Transferable[] getListHeaders(final Set storableObjects) {
		StorableObject_Transferable headers[] = new StorableObject_Transferable[storableObjects.size()];
		int i = 0;
		for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++) 
			headers[i] = ((StorableObject) storableObjectIterator.next()).getHeaderTransferable();
		return headers;
	}

	abstract StorableObject_Transferable receiveStorableObject(
			final StorableObject storableObject,
			final SessionKey_Transferable sessionKey, 
			final boolean force)
			throws AMFICOMRemoteException;

	abstract StorableObject_Transferable[] receiveStorableObjects(
			final Set storableObjects,
			final SessionKey_Transferable sessionKey,
			final boolean force)
			throws AMFICOMRemoteException;

	/*-********************************************************************
	 * Map -- receive a single object.                                    *
	 **********************************************************************/

	public final StorableObject_Transferable receiveSiteNode(
			final SiteNode_Transferable siteNode,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSiteNode(siteNode), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveTopologicalNode(
			final TopologicalNode_Transferable topologicalNode,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newTopologicalNode(topologicalNode), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveNodeLink(
			final NodeLink_Transferable nodeLink,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newNodeLink(nodeLink), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveMark(
			final Mark_Transferable mark,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newMark(mark), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receivePhysicalLink(
			final PhysicalLink_Transferable physicalLink,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newPhysicalLink(physicalLink), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveCollector(
			final Collector_Transferable collector,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newCollector(collector), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveMap(
			final Map_Transferable map,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newMap(map), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receiveSiteNodeType(
			final SiteNodeType_Transferable siteNodeType,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSiteNodeType(siteNodeType), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable receivePhysicalLinkType(
			final PhysicalLinkType_Transferable physicalLinkType,
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newPhysicalLinkType(physicalLinkType), sessionKey, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/*-********************************************************************
	 * Map -- receive multiple objects.                                   *
	 **********************************************************************/

	public final StorableObject_Transferable[] receiveSiteNodes(
			final SiteNode_Transferable siteNodes[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set siteNodes1 = new HashSet(siteNodes.length);
			for (int i = 0; i < siteNodes.length; i++) {
				final SiteNode siteNode = newSiteNode(siteNodes[i]);
				MapStorableObjectPool.putStorableObject(siteNode);
				siteNodes1.add(siteNode);
			}
			return receiveStorableObjects(siteNodes1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveTopologicalNodes(
			final TopologicalNode_Transferable topologicalNodes[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set topologicalNodes1 = new HashSet(topologicalNodes.length);
			for (int i = 0; i < topologicalNodes.length; i++) {
				final TopologicalNode topologicalNode = newTopologicalNode(topologicalNodes[i]);
				MapStorableObjectPool.putStorableObject(topologicalNode);
				topologicalNodes1.add(topologicalNode);
			}
			return receiveStorableObjects(topologicalNodes1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveNodeLinks(
			final NodeLink_Transferable nodeLinks[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set nodeLinks1 = new HashSet(nodeLinks.length);
			for (int i = 0; i < nodeLinks.length; i++) {
				final NodeLink nodeLink = newNodeLink(nodeLinks[i]);
				MapStorableObjectPool.putStorableObject(nodeLink);
				nodeLinks1.add(nodeLink);
			}
			return receiveStorableObjects(nodeLinks1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveMarks(
			final Mark_Transferable marks[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set marks1 = new HashSet(marks.length);
			for (int i = 0; i < marks.length; i++) {
				final Mark mark = newMark(marks[i]);
				MapStorableObjectPool.putStorableObject(mark);
				marks1.add(mark);
			}
			return receiveStorableObjects(marks1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receivePhysicalLinks(
			final PhysicalLink_Transferable physicalLinks[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set physicalLinks1 = new HashSet(physicalLinks.length);
			for (int i = 0; i < physicalLinks.length; i++) {
				final PhysicalLink physicalLink = newPhysicalLink(physicalLinks[i]);
				MapStorableObjectPool.putStorableObject(physicalLink);
				physicalLinks1.add(physicalLink);
			}
			return receiveStorableObjects(physicalLinks1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveCollectors(
			final Collector_Transferable collectors[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set collectors1 = new HashSet(collectors.length);
			for (int i = 0; i < collectors.length; i++) {
				final Collector collector = newCollector(collectors[i]);
				MapStorableObjectPool.putStorableObject(collector);
				collectors1.add(collector);
			}
			return receiveStorableObjects(collectors1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveMaps(
			final Map_Transferable maps[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set maps1 = new HashSet(maps.length);
			for (int i = 0; i < maps.length; i++) {
				final Map map = newMap(maps[i]);
				MapStorableObjectPool.putStorableObject(map);
				maps1.add(map);
			}
			return receiveStorableObjects(maps1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receiveSiteNodeTypes(
			final SiteNodeType_Transferable siteNodeTypes[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set siteNodeTypes1 = new HashSet(siteNodeTypes.length);
			for (int i = 0; i < siteNodeTypes.length; i++) {
				final SiteNodeType siteNodeType = newSiteNodeType(siteNodeTypes[i]);
				MapStorableObjectPool.putStorableObject(siteNodeType);
				siteNodeTypes1.add(siteNodeType);
			}
			return receiveStorableObjects(siteNodeTypes1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public final StorableObject_Transferable[] receivePhysicalLinkTypes(
			final PhysicalLinkType_Transferable physicalLinkTypes[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Set physicalLinkTypes1 = new HashSet(physicalLinkTypes.length);
			for (int i = 0; i < physicalLinkTypes.length; i++) {
				final PhysicalLinkType physicalLinkType = newPhysicalLinkType(physicalLinkTypes[i]);
				MapStorableObjectPool.putStorableObject(physicalLinkType);
				physicalLinkTypes1.add(physicalLinkType);
			}
			return receiveStorableObjects(physicalLinkTypes1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
}
