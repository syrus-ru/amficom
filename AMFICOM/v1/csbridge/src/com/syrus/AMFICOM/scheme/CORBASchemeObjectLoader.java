/*-
 * $Id: CORBASchemeObjectLoader.java,v 1.7 2005/06/02 14:44:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

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
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

public final class CORBASchemeObjectLoader extends CORBAObjectLoader implements SchemeObjectLoader {
	public CORBASchemeObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitCableChannelingItems(ids1, sessionKey);
			}
		});
	}

	public Set loadPathElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PATH_ELEMENT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPathElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCableLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCableLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCablePorts(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCablePorts(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCableThreads(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCableThreads(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeDevices(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeDevices(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_LINK_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeMonitoringSolutions(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeMonitoringSolutions(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfos(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfos(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfoSwitches(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfoSwitches(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfoRtus(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfoRtus(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemePaths(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_PATH_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemePaths(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemePorts(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_PORT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemePorts(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeProtoElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeProtoElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeProtoGroups(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeProtoGroups(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SCHEME_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemes(ids1, sessionKey);
			}
		});
	}

	public Set loadCableChannelingItemsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitCableChannelingItemsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPathElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.PATH_ELEMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitPathElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCableLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCableLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCablePortsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCablePortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCableThreadsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeCableThreadsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeDevicesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeDevicesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_LINK_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeMonitoringSolutionsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfosButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfoSwitchesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeOptimizeInfoRtusButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemePathsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_PATH_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemePathsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemePortsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_PORT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemePortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeProtoElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeProtoElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeProtoGroupsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemeProtoGroupsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SCHEME_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public void saveCableChannelingItems(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void savePathElements(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeCableLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeCablePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeCableThreads(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeDevices(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeElements(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeMonitoringSolutions(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeOptimizeInfos(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeOptimizeInfoSwitches(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeOptimizeInfoRtus(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemePaths(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeProtoElements(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemeProtoGroups(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSchemes(final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
