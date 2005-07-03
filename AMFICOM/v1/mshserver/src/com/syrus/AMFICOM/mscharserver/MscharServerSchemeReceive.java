/*-
 * $Id: MscharServerSchemeReceive.java,v 1.5 2005/06/25 17:07:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlCableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.IdlPathElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThread;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDevice;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtu;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoSwitch;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlScheme;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:50 $
 * @module mscharserver_v1
 */
abstract class MscharServerSchemeReceive extends MscharServerMapReceive {
	private static final long serialVersionUID = 1127393868558975178L;

	MscharServerSchemeReceive(final ORB orb) {
		super(orb);
	}

	public final IdlStorableObject[] receiveSchemeProtoGroups(
			final IdlSchemeProtoGroup transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPROTOGROUP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeProtoElements(
			final IdlSchemeProtoElement transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPROTOELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemes(
			final IdlScheme transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEME_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeElements(
			final IdlSchemeElement transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEELEMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeOptimizeInfos(
			final IdlSchemeOptimizeInfo transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeOptimizeInfoSwitches(
			final IdlSchemeOptimizeInfoSwitch transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, transferables, force, sessionKey);
	}
			
	public final IdlStorableObject[] receiveSchemeOptimizeInfoRtus(
			final IdlSchemeOptimizeInfoRtu transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeMonitoringSolutions(
			final IdlSchemeMonitoringSolution transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeDevices(
			final IdlSchemeDevice transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEDEVICE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemePorts(
			final IdlSchemePort transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCablePorts(
			final IdlSchemeCablePort transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLEPORT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeLinks(
			final IdlSchemeLink transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCableLinks(
			final IdlSchemeCableLink transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLELINK_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemeCableThreads(
			final IdlSchemeCableThread transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMECABLETHREAD_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCableChannelingItems(
			final IdlCableChannelingItem transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CABLECHANNELINGITEM_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveSchemePaths(
			final IdlSchemePath transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SCHEMEPATH_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePathElements(
			final IdlPathElement transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PATHELEMENT_CODE, transferables, force, sessionKey);
	}
}
