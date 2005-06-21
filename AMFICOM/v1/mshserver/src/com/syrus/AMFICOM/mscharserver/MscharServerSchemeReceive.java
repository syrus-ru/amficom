/*-
 * $Id: MscharServerSchemeReceive.java,v 1.3 2005/06/21 12:44:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
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
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/21 12:44:26 $
 * @module mscharserver_v1
 */
abstract class MscharServerSchemeReceive extends MscharServerMapReceive {
	private static final long serialVersionUID = 1127393868558975178L;

	public final IdlStorableObject[] receiveSchemeProtoGroups(
			final SchemeProtoGroup_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPROTOGROUP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeProtoElements(
			final SchemeProtoElement_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPROTOELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemes(
			final Scheme_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeElements(
			final SchemeElement_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeOptimizeInfos(
			final SchemeOptimizeInfo_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeOptimizeInfoSwitches(
			final SchemeOptimizeInfoSwitch_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, transferables, force, sessionKey);
	}
			
	public final IdlStorableObject[] receiveSchemeOptimizeInfoRtus(
			final SchemeOptimizeInfoRtu_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeMonitoringSolutions(
			final SchemeMonitoringSolution_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeDevices(
			final SchemeDevice_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEDEVICE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemePorts(
			final SchemePort_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCablePorts(
			final SchemeCablePort_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLEPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeLinks(
			final SchemeLink_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCableLinks(
			final SchemeCableLink_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCableThreads(
			final SchemeCableThread_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLETHREAD_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableChannelingItems(
			final CableChannelingItem_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLECHANNELINGITEM_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemePaths(
			final SchemePath_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPATH_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePathElements(
			final PathElement_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PATHELEMENT_CODE, transferables, force, sessionKey);
	}
}
