/*-
 * $Id: CORBASchemeObjectLoader.java,v 1.13 2005/06/17 13:06:58 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
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
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/06/17 13:06:58 $
 * @module csbridge_v1
 */
public final class CORBASchemeObjectLoader extends CORBAObjectLoader implements SchemeObjectLoader {

	public CORBASchemeObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CABLECHANNELINGITEM_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitCableChannelingItems(ids1, sessionKey);
			}
		});
	}

	public Set loadPathElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PATHELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPathElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCableLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMECABLELINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCableLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCablePorts(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMECABLEPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCablePorts(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeCableThreads(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMECABLETHREAD_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCableThreads(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeDevices(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEDEVICE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeDevices(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMELINK_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeLinks(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeMonitoringSolutions(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeMonitoringSolutions(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfos(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfos(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfoSwitches(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfoSwitches(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeOptimizeInfoRtus(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfoRtus(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemePaths(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEPATH_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemePaths(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemePorts(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemePorts(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeProtoElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEPROTOELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeProtoElements(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemeProtoGroups(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEMEPROTOGROUP_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeProtoGroups(ids1, sessionKey);
			}
		});
	}

	public Set loadSchemes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SCHEME_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemes(ids1, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadCableChannelingItemsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CABLECHANNELINGITEM_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitCableChannelingItemsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadPathElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PATHELEMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitPathElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCableLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMECABLELINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCableLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCablePortsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMECABLEPORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCablePortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeCableThreadsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMECABLETHREAD_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeCableThreadsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeDevicesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEDEVICE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeDevicesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEELEMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMELINK_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeLinksButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeMonitoringSolutionsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfosButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfoSwitchesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeOptimizeInfoRtusButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemePathsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEPATH_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemePathsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemePortsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEPORT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemePortsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeProtoElementsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEPROTOELEMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeProtoElementsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemeProtoGroupsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemeProtoGroupsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadSchemesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SCHEME_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).transmitSchemesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveCableChannelingItems(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CABLECHANNELINGITEM_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveCableChannelingItems((CableChannelingItem_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePathElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PATHELEMENT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receivePathElements((PathElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCableLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMECABLELINK_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeCableLinks((SchemeCableLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCablePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMECABLEPORT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeCablePorts((SchemeCablePort_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeCableThreads(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMECABLETHREAD_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeCableThreads((SchemeCableThread_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeDevices(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEDEVICE_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeDevices((SchemeDevice_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEELEMENT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeElements((SchemeElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeLinks(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMELINK_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeLinks((SchemeLink_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeMonitoringSolutions(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeMonitoringSolutions((SchemeMonitoringSolution_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfos(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeOptimizeInfos((SchemeOptimizeInfo_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfoSwitches(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeOptimizeInfoSwitches((SchemeOptimizeInfoSwitch_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeOptimizeInfoRtus(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeOptimizeInfoRtus((SchemeOptimizeInfoRtu_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemePaths(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEPATH_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemePaths((SchemePath_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemePorts(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEPORT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemePorts((SchemePort_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeProtoElements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEPROTOELEMENT_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeProtoElements((SchemeProtoElement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemeProtoGroups(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEMEPROTOGROUP_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemeProtoGroups((SchemeProtoGroup_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveSchemes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SCHEME_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MscharServer) server).receiveSchemes((Scheme_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
