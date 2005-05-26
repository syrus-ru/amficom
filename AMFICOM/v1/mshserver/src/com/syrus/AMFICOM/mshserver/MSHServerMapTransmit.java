/*-
 * $Id: MSHServerMapTransmit.java,v 1.6 2005/05/26 17:52:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/26 17:52:55 $
 * @module mshserver_v1
 */
abstract class MSHServerMapTransmit extends MSHServerSchemeReceive {
	public SiteNode_Transferable[] transmitSiteNodes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SiteNode_Transferable siteNodes[] = new SiteNode_Transferable[length];
		System.arraycopy(storableObjects, 0, siteNodes, 0, length);
		return siteNodes;
	}

	public SiteNode_Transferable[] transmitSiteNodesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SiteNode_Transferable siteNodes[] = new SiteNode_Transferable[length];
		System.arraycopy(storableObjects, 0, siteNodes, 0, length);
		return siteNodes;
	}

	public TopologicalNode_Transferable[] transmitTopologicalNodes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final TopologicalNode_Transferable topologicalNodes[] = new TopologicalNode_Transferable[length];
		System.arraycopy(storableObjects, 0, topologicalNodes, 0, length);
		return topologicalNodes;
	}

	public TopologicalNode_Transferable[] transmitTopologicalNodesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final TopologicalNode_Transferable topologicalNodes[] = new TopologicalNode_Transferable[length];
		System.arraycopy(storableObjects, 0, topologicalNodes, 0, length);
		return topologicalNodes;
	}

	public NodeLink_Transferable[] transmitNodeLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final NodeLink_Transferable nodeLinks[] = new NodeLink_Transferable[length];
		System.arraycopy(storableObjects, 0, nodeLinks, 0, length);
		return nodeLinks;
	}

	public NodeLink_Transferable[] transmitNodeLinksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final NodeLink_Transferable nodeLinks[] = new NodeLink_Transferable[length];
		System.arraycopy(storableObjects, 0, nodeLinks, 0, length);
		return nodeLinks;
	}

	public Mark_Transferable[] transmitMarks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Mark_Transferable marks[] = new Mark_Transferable[length];
		System.arraycopy(storableObjects, 0, marks, 0, length);
		return marks;
	}

	public Mark_Transferable[] transmitMarksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Mark_Transferable marks[] = new Mark_Transferable[length];
		System.arraycopy(storableObjects, 0, marks, 0, length);
		return marks;
	}

	public PhysicalLink_Transferable[] transmitPhysicalLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final PhysicalLink_Transferable physicalLinks[] = new PhysicalLink_Transferable[length];
		System.arraycopy(storableObjects, 0, physicalLinks, 0, length);
		return physicalLinks;
	}

	public PhysicalLink_Transferable[] transmitPhysicalLinksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final PhysicalLink_Transferable physicalLinks[] = new PhysicalLink_Transferable[length];
		System.arraycopy(storableObjects, 0, physicalLinks, 0, length);
		return physicalLinks;
	}

	public Collector_Transferable[] transmitCollectors(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Collector_Transferable collectors[] = new Collector_Transferable[length];
		System.arraycopy(storableObjects, 0, collectors, 0, length);
		return collectors;
	}

	public Collector_Transferable[] transmitCollectorsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Collector_Transferable collectors[] = new Collector_Transferable[length];
		System.arraycopy(storableObjects, 0, collectors, 0, length);
		return collectors;
	}

	public Map_Transferable[] transmitMaps(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Map_Transferable maps[] = new Map_Transferable[length];
		System.arraycopy(storableObjects, 0, maps, 0, length);
		return maps;
	}

	public Map_Transferable[] transmitMapsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Map_Transferable maps[] = new Map_Transferable[length];
		System.arraycopy(storableObjects, 0, maps, 0, length);
		return maps;
	}

	public SiteNodeType_Transferable[] transmitSiteNodeTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SiteNodeType_Transferable siteNodeTypes[] = new SiteNodeType_Transferable[length];
		System.arraycopy(storableObjects, 0, siteNodeTypes, 0, length);
		return siteNodeTypes;
	}

	public SiteNodeType_Transferable[] transmitSiteNodeTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SiteNodeType_Transferable siteNodeTypes[] = new SiteNodeType_Transferable[length];
		System.arraycopy(storableObjects, 0, siteNodeTypes, 0, length);
		return siteNodeTypes;
	}

	public PhysicalLinkType_Transferable[] transmitPhysicalLinkTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final PhysicalLinkType_Transferable physicalLinkTypes[] = new PhysicalLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, physicalLinkTypes, 0, length);
		return physicalLinkTypes;
	}

	public PhysicalLinkType_Transferable[] transmitPhysicalLinkTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final PhysicalLinkType_Transferable physicalLinkTypes[] = new PhysicalLinkType_Transferable[length];
		System.arraycopy(storableObjects, 0, physicalLinkTypes, 0, length);
		return physicalLinkTypes;
	}

	/**
	 * @deprecated
	 */
	public final Identifier_Transferable[] transmitRefreshedMapObjects(
			final StorableObject_Transferable headers[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKey,
				new Identifier_TransferableHolder(),
				new Identifier_TransferableHolder());

		final Map headerMap = new HashMap();
		for (int i = 0; i < headers.length; i++)
			headerMap.put(new Identifier(headers[i].id), headers[i]);

		try {
			StorableObjectPool.refresh();

			final Set storableObjects = StorableObjectPool.getStorableObjects(headerMap.keySet(), true);
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				final StorableObject_Transferable header = (StorableObject_Transferable) headerMap.get(storableObject.getId());
				/*
				 * Remove objects with older versions as well as objects with the same versions.
				 * Not only with older ones!
				 */
				if (!storableObject.hasNewerVersion(header.version))
					storableObjectIterator.remove();
			}

			return Identifier.createTransferables(storableObjects);
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}
}
