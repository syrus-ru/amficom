/*-
 * $Id: MscharServerSchemeTransmit.java,v 1.5 2005/06/25 17:07:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
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
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:51 $
 * @module mscharserver_v1
 */
abstract class MscharServerSchemeTransmit extends MscharServerMapTransmit {
	private static final long serialVersionUID = 6830363270405840293L;

	MscharServerSchemeTransmit(final ORB orb) {
		super(orb);
	}

	public IdlSchemeProtoGroup[] transmitSchemeProtoGroups(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeProtoGroup schemeProtoGroups[] = new IdlSchemeProtoGroup[length];
		System.arraycopy(storableObjects, 0, schemeProtoGroups, 0, length);
		return schemeProtoGroups;
	}

	public IdlSchemeProtoGroup[] transmitSchemeProtoGroupsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeProtoGroup schemeProtoGroups[] = new IdlSchemeProtoGroup[length];
		System.arraycopy(storableObjects, 0, schemeProtoGroups, 0, length);
		return schemeProtoGroups;
	}

	public IdlSchemeProtoElement[] transmitSchemeProtoElements(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeProtoElement schemeProtoElements[] = new IdlSchemeProtoElement[length];
		System.arraycopy(storableObjects, 0, schemeProtoElements, 0, length);
		return schemeProtoElements;
	}

	public IdlSchemeProtoElement[] transmitSchemeProtoElementsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeProtoElement schemeProtoElements[] = new IdlSchemeProtoElement[length];
		System.arraycopy(storableObjects, 0, schemeProtoElements, 0, length);
		return schemeProtoElements;
	}

	public IdlScheme[] transmitSchemes(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlScheme schemes[] = new IdlScheme[length];
		System.arraycopy(storableObjects, 0, schemes, 0, length);
		return schemes;
	}

	public IdlScheme[] transmitSchemesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlScheme schemes[] = new IdlScheme[length];
		System.arraycopy(storableObjects, 0, schemes, 0, length);
		return schemes;
	}

	public IdlSchemeElement[] transmitSchemeElements(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeElement schemeElements[] = new IdlSchemeElement[length];
		System.arraycopy(storableObjects, 0, schemeElements, 0, length);
		return schemeElements;
	}

	public IdlSchemeElement[] transmitSchemeElementsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeElement schemeElements[] = new IdlSchemeElement[length];
		System.arraycopy(storableObjects, 0, schemeElements, 0, length);
		return schemeElements;
	}

	public IdlSchemeOptimizeInfo[] transmitSchemeOptimizeInfos(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfo schemeOptimizeInfos[] = new IdlSchemeOptimizeInfo[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfos, 0, length);
		return schemeOptimizeInfos;
	}

	public IdlSchemeOptimizeInfo[] transmitSchemeOptimizeInfosButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfo schemeOptimizeInfos[] = new IdlSchemeOptimizeInfo[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfos, 0, length);
		return schemeOptimizeInfos;
	}

	public IdlSchemeOptimizeInfoSwitch[] transmitSchemeOptimizeInfoSwitches(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfoSwitch schemeOptimizeInfoSwitches[] = new IdlSchemeOptimizeInfoSwitch[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoSwitches, 0, length);
		return schemeOptimizeInfoSwitches;
	}

	public IdlSchemeOptimizeInfoSwitch[] transmitSchemeOptimizeInfoSwitchesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfoSwitch schemeOptimizeInfoSwitches[] = new IdlSchemeOptimizeInfoSwitch[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoSwitches, 0, length);
		return schemeOptimizeInfoSwitches;
	}

	public IdlSchemeOptimizeInfoRtu[] transmitSchemeOptimizeInfoRtus(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfoRtu schemeOptimizeInfoRtus[] = new IdlSchemeOptimizeInfoRtu[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoRtus, 0, length);
		return schemeOptimizeInfoRtus;
	}

	public IdlSchemeOptimizeInfoRtu[] transmitSchemeOptimizeInfoRtusButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeOptimizeInfoRtu schemeOptimizeInfoRtus[] = new IdlSchemeOptimizeInfoRtu[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoRtus, 0, length);
		return schemeOptimizeInfoRtus;
	}

	public IdlSchemeMonitoringSolution[] transmitSchemeMonitoringSolutions(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeMonitoringSolution schemeMonitoringSolutions[] = new IdlSchemeMonitoringSolution[length];
		System.arraycopy(storableObjects, 0, schemeMonitoringSolutions, 0, length);
		return schemeMonitoringSolutions;
	}

	public IdlSchemeMonitoringSolution[] transmitSchemeMonitoringSolutionsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeMonitoringSolution schemeMonitoringSolutions[] = new IdlSchemeMonitoringSolution[length];
		System.arraycopy(storableObjects, 0, schemeMonitoringSolutions, 0, length);
		return schemeMonitoringSolutions;
	}

	public IdlSchemeDevice[] transmitSchemeDevices(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeDevice schemeDevices[] = new IdlSchemeDevice[length];
		System.arraycopy(storableObjects, 0, schemeDevices, 0, length);
		return schemeDevices;
	}

	public IdlSchemeDevice[] transmitSchemeDevicesButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeDevice schemeDevices[] = new IdlSchemeDevice[length];
		System.arraycopy(storableObjects, 0, schemeDevices, 0, length);
		return schemeDevices;
	}

	public IdlSchemePort[] transmitSchemePorts(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemePort schemePorts[] = new IdlSchemePort[length];
		System.arraycopy(storableObjects, 0, schemePorts, 0, length);
		return schemePorts;
	}

	public IdlSchemePort[] transmitSchemePortsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemePort schemePorts[] = new IdlSchemePort[length];
		System.arraycopy(storableObjects, 0, schemePorts, 0, length);
		return schemePorts;
	}

	public IdlSchemeCablePort[] transmitSchemeCablePorts(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeCablePort schemeCablePorts[] = new IdlSchemeCablePort[length];
		System.arraycopy(storableObjects, 0, schemeCablePorts, 0, length);
		return schemeCablePorts;
	}

	public IdlSchemeCablePort[] transmitSchemeCablePortsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeCablePort schemeCablePorts[] = new IdlSchemeCablePort[length];
		System.arraycopy(storableObjects, 0, schemeCablePorts, 0, length);
		return schemeCablePorts;
	}

	public IdlSchemeLink[] transmitSchemeLinks(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeLink schemeLinks[] = new IdlSchemeLink[length];
		System.arraycopy(storableObjects, 0, schemeLinks, 0, length);
		return schemeLinks;
	}

	public IdlSchemeLink[] transmitSchemeLinksButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeLink schemeLinks[] = new IdlSchemeLink[length];
		System.arraycopy(storableObjects, 0, schemeLinks, 0, length);
		return schemeLinks;
	}

	public IdlSchemeCableLink[] transmitSchemeCableLinks(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeCableLink schemeCableLinks[] = new IdlSchemeCableLink[length];
		System.arraycopy(storableObjects, 0, schemeCableLinks, 0, length);
		return schemeCableLinks;
	}

	public IdlSchemeCableLink[] transmitSchemeCableLinksButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeCableLink schemeCableLinks[] = new IdlSchemeCableLink[length];
		System.arraycopy(storableObjects, 0, schemeCableLinks, 0, length);
		return schemeCableLinks;
	}

	public IdlSchemeCableThread[] transmitSchemeCableThreads(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemeCableThread schemeCableThreads[] = new IdlSchemeCableThread[length];
		System.arraycopy(storableObjects, 0, schemeCableThreads, 0, length);
		return schemeCableThreads;
	}

	public IdlSchemeCableThread[] transmitSchemeCableThreadsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemeCableThread schemeCableThreads[] = new IdlSchemeCableThread[length];
		System.arraycopy(storableObjects, 0, schemeCableThreads, 0, length);
		return schemeCableThreads;
	}

	public IdlCableChannelingItem[] transmitCableChannelingItems(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlCableChannelingItem cableChannelingItems[] = new IdlCableChannelingItem[length];
		System.arraycopy(storableObjects, 0, cableChannelingItems, 0, length);
		return cableChannelingItems;
	}

	public IdlCableChannelingItem[] transmitCableChannelingItemsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlCableChannelingItem cableChannelingItems[] = new IdlCableChannelingItem[length];
		System.arraycopy(storableObjects, 0, cableChannelingItems, 0, length);
		return cableChannelingItems;
	}

	public IdlSchemePath[] transmitSchemePaths(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlSchemePath schemePaths[] = new IdlSchemePath[length];
		System.arraycopy(storableObjects, 0, schemePaths, 0, length);
		return schemePaths;
	}

	public IdlSchemePath[] transmitSchemePathsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlSchemePath schemePaths[] = new IdlSchemePath[length];
		System.arraycopy(storableObjects, 0, schemePaths, 0, length);
		return schemePaths;
	}

	public IdlPathElement[] transmitPathElements(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IdlPathElement pathElements[] = new IdlPathElement[length];
		System.arraycopy(storableObjects, 0, pathElements, 0, length);
		return pathElements;
	}

	public IdlPathElement[] transmitPathElementsButIdsCondition(
			final IdlIdentifier ids[],
			final IdlSessionKey sessionKey,
			final IdlStorableObjectCondition storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IdlPathElement pathElements[] = new IdlPathElement[length];
		System.arraycopy(storableObjects, 0, pathElements, 0, length);
		return pathElements;
	}
}
