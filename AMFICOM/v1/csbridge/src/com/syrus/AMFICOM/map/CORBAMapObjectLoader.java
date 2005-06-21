/*-
 * $Id: CORBAMapObjectLoader.java,v 1.14 2005/06/21 12:44:27 bass Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/06/21 12:44:27 $
 * @module csbridge_v1
 */
public final class CORBAMapObjectLoader extends CORBAObjectLoader implements MapObjectLoader {

	public CORBAMapObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadCollectors(final Set ids) throws ApplicationException {
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

	public Set loadMaps(final Set ids) throws ApplicationException {
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

	public Set loadMarks(final Set ids) throws ApplicationException {
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

	public Set loadNodeLinks(final Set ids) throws ApplicationException {
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

	public Set loadPhysicalLinks(final Set ids) throws ApplicationException {
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

	public Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException {
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

	public Set loadSiteNodes(final Set ids) throws ApplicationException {
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

	public Set loadSiteNodeTypes(final Set ids) throws ApplicationException {
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

	public Set loadTopologicalNodes(final Set ids) throws ApplicationException {
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

	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
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

	public void saveCollectors(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.COLLECTOR_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveCollectors((Collector_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMaps(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MAP_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveMaps((Map_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMarks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MARK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveMarks((Mark_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveNodeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.NODELINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveNodeLinks((NodeLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICALLINK_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receivePhysicalLinks((PhysicalLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICALLINK_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receivePhysicalLinkTypes((PhysicalLinkType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITENODE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSiteNodes((SiteNode_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodeTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITENODE_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSiteNodeTypes((SiteNodeType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTopologicalNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TOPOLOGICALNODE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveTopologicalNodes((TopologicalNode_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
