/*-
 * $Id: CORBAMapObjectLoader.java,v 1.16 2005/06/24 10:41:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

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
import com.syrus.AMFICOM.map.corba.IdlCollector;
import com.syrus.AMFICOM.map.corba.IdlMap;
import com.syrus.AMFICOM.map.corba.IdlMark;
import com.syrus.AMFICOM.map.corba.IdlNodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLink;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/06/24 10:41:47 $
 * @module csbridge_v1
 */
public final class CORBAMapObjectLoader extends CORBAObjectLoader implements MapObjectLoader {

	public CORBAMapObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadCollectors(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.COLLECTOR_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitCollectors(ids1, sessionKey);
			}
		});
	}

	public Set loadMaps(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MAP_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMaps(ids1, sessionKey);
			}
		});
	}

	public Set loadMarks(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MARK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMarks(ids1, sessionKey);
			}
		});
	}

	public Set loadNodeLinks(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.NODELINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitNodeLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadPhysicalLinks(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PHYSICALLINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPhysicalLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadPhysicalLinkTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PHYSICALLINK_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPhysicalLinkTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadSiteNodes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SITENODE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSiteNodes(ids1, sessionKey);
			}
		});
	}

	public Set loadSiteNodeTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SITENODE_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSiteNodeTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadTopologicalNodes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TOPOLOGICALNODE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitTopologicalNodes(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.COLLECTOR_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitCollectorsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MAP_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMapsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MARK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitMarksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.NODELINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitNodeLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PHYSICALLINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPhysicalLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PHYSICALLINK_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPhysicalLinkTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SITENODE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSiteNodesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SITENODE_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSiteNodeTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TOPOLOGICALNODE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitTopologicalNodesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveCollectors(final Set<Collector> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.COLLECTOR_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveCollectors((IdlCollector[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMaps(final Set<Map> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MAP_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveMaps((IdlMap[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMarks(final Set<Mark> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MARK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveMarks((IdlMark[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveNodeLinks(final Set<NodeLink> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.NODELINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveNodeLinks((IdlNodeLink[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinks(final Set<PhysicalLink> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICALLINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receivePhysicalLinks((IdlPhysicalLink[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinkTypes(final Set<PhysicalLinkType> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICALLINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receivePhysicalLinkTypes((IdlPhysicalLinkType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodes(final Set<SiteNode> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITENODE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSiteNodes((IdlSiteNode[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodeTypes(final Set<SiteNodeType> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITENODE_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSiteNodeTypes((IdlSiteNodeType[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTopologicalNodes(final Set<TopologicalNode> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TOPOLOGICALNODE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveTopologicalNodes((IdlTopologicalNode[]) transferables, force, sessionKey);
			}
		});
	}
}
