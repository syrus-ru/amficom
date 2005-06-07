/*-
 * $Id: CORBAMapObjectLoader.java,v 1.10 2005/06/07 13:53:51 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
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
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/06/07 13:53:51 $
 * @module csbridge_v1
 */
public final class CORBAMapObjectLoader extends CORBAObjectLoader implements MapObjectLoader {

	public CORBAMapObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadCollectors(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.COLLECTOR_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitCollectors(ids1, sessionKey);
			}
		});
	}

	public Set loadMaps(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MAP_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMaps(ids1, sessionKey);
			}
		});
	}

	public Set loadMarks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MARK_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMarks(ids1, sessionKey);
			}
		});
	}

	public Set loadNodeLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.NODE_LINK_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitNodeLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadPhysicalLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPhysicalLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPhysicalLinkTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadSiteNodes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SITE_NODE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSiteNodes(ids1, sessionKey);
			}
		});
	}

	public Set loadSiteNodeTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSiteNodeTypes(ids1, sessionKey);
			}
		});
	}

	public Set loadTopologicalNodes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitTopologicalNodes(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.COLLECTOR_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitCollectorsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MAP_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMapsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MARK_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitMarksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.NODE_LINK_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitNodeLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPhysicalLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPhysicalLinkTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SITE_NODE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSiteNodesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSiteNodeTypesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitTopologicalNodesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveCollectors(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.COLLECTOR_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveCollectors((Collector_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMaps(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MAP_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveMaps((Map_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMarks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MARK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveMarks((Mark_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveNodeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.NODE_LINK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveNodeLinks((NodeLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receivePhysicalLinks((PhysicalLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePhysicalLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receivePhysicalLinkTypes((PhysicalLinkType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITE_NODE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSiteNodes((SiteNode_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSiteNodeTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSiteNodeTypes((SiteNodeType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTopologicalNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveTopologicalNodes((TopologicalNode_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
