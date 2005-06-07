/*-
 * $Id: CORBASchemeObjectLoader.java,v 1.9 2005/06/07 13:22:44 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableThread_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoRtu_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoSwitch_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePath_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup_Transferable;
import com.syrus.AMFICOM.scheme.corba.Scheme_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/06/07 13:22:44 $
 * @module csbridge_v1
 */
public final class CORBASchemeObjectLoader extends CORBAObjectLoader implements SchemeObjectLoader {

	public CORBASchemeObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_LINK_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_PATH_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_PORT_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, ids, new TransmitProcedure() {
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
		return super.loadStorableObjects(ObjectEntities.SCHEME_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).transmitSchemes(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadCableChannelingItemsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_LINK_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_PATH_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_PORT_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_ENTITY_CODE, ids, condition, new TransmitButIdsConditionProcedure() {
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



	/*	Save multiple objects*/

	public void saveCableChannelingItems(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveCableChannelingItems((CableChannelingItem_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePathElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receivePathElements((PathElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCableLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeCableLinks((SchemeCableLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCablePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeCablePorts((SchemeCablePort_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCableThreads(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeCableThreads((SchemeCableThread_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeDevices(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeDevices((SchemeDevice_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeElements((SchemeElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_LINK_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeLinks((SchemeLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeMonitoringSolutions(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeMonitoringSolutions((SchemeMonitoringSolution_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfos(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeOptimizeInfos((SchemeOptimizeInfo_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfoSwitches(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeOptimizeInfoSwitches((SchemeOptimizeInfoSwitch_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfoRtus(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeOptimizeInfoRtus((SchemeOptimizeInfoRtu_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemePaths(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_PATH_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemePaths((SchemePath_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_PORT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemePorts((SchemePort_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeProtoElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeProtoElements((SchemeProtoElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeProtoGroups(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemeProtoGroups((SchemeProtoGroup_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MSHServer) server).receiveSchemes((Scheme_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
