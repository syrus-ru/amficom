/*-
 * $Id: MSHServerSchemeReceive.java,v 1.7 2005/05/25 13:01:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
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
 * @version $Revision: 1.7 $, $Date: 2005/05/25 13:01:10 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeReceive extends MSHServerMapReceive {
	public final StorableObject_Transferable[] receiveSchemeProtoGroups(
			final SchemeProtoGroup_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeProtoElements(
			final SchemeProtoElement_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemes(
			final Scheme_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeElements(
			final SchemeElement_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeOptimizeInfos(
			final SchemeOptimizeInfo_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeOptimizeInfoSwitches(
			final SchemeOptimizeInfoSwitch_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, transferables, force, sessionKey);
	}
			
	public final StorableObject_Transferable[] receiveSchemeOptimizeInfoRtus(
			final SchemeOptimizeInfoRtu_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeMonitoringSolutions(
			final SchemeMonitoringSolution_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeDevices(
			final SchemeDevice_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemePorts(
			final SchemePort_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_PORT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeCablePorts(
			final SchemeCablePort_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeLinks(
			final SchemeLink_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_LINK_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeCableLinks(
			final SchemeCableLink_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemeCableThreads(
			final SchemeCableThread_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCableChannelingItems(
			final CableChannelingItem_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSchemePaths(
			final SchemePath_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_PATH_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePathElements(
			final PathElement_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, transferables, force, sessionKey);
	}
}
