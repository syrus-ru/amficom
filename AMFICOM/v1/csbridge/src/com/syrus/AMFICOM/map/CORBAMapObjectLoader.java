/*-
 * $Id: CORBAMapObjectLoader.java,v 1.4 2005/05/31 14:54:42 bass Exp $
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
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

public final class CORBAMapObjectLoader extends CORBAObjectLoader implements MapObjectLoader {
	public CORBAMapObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public Set loadCollectors(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.COLLECTOR_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.MAP_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.MARK_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.NODE_LINK_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.SITE_NODE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, new TransmitProcedure() {
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
		return super.loadStorableObjects(ids, ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitTopologicalNodes(ids1, sessionKey);
			}
		});
	}

	public Set loadCollectorsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMapsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMarksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveCollectors(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveMaps(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveMarks(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveNodeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void savePhysicalLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void savePhysicalLinkTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSiteNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSiteNodeTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveTopologicalNodes(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
