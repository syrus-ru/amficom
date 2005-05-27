/*-
 * $Id: MSHServerSchemeTransmit.java,v 1.7 2005/05/27 11:37:13 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
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
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/05/27 11:37:13 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeTransmit extends MSHServerMapTransmit {
	private static final long serialVersionUID = 6830363270405840293L;

	public SchemeProtoGroup_Transferable[] transmitSchemeProtoGroups(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeProtoGroup_Transferable schemeProtoGroups[] = new SchemeProtoGroup_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeProtoGroups, 0, length);
		return schemeProtoGroups;
	}

	public SchemeProtoGroup_Transferable[] transmitSchemeProtoGroupsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeProtoGroup_Transferable schemeProtoGroups[] = new SchemeProtoGroup_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeProtoGroups, 0, length);
		return schemeProtoGroups;
	}

	public SchemeProtoElement_Transferable[] transmitSchemeProtoElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeProtoElement_Transferable schemeProtoElements[] = new SchemeProtoElement_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeProtoElements, 0, length);
		return schemeProtoElements;
	}

	public SchemeProtoElement_Transferable[] transmitSchemeProtoElementsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeProtoElement_Transferable schemeProtoElements[] = new SchemeProtoElement_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeProtoElements, 0, length);
		return schemeProtoElements;
	}

	public Scheme_Transferable[] transmitSchemes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Scheme_Transferable schemes[] = new Scheme_Transferable[length];
		System.arraycopy(storableObjects, 0, schemes, 0, length);
		return schemes;
	}

	public Scheme_Transferable[] transmitSchemesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Scheme_Transferable schemes[] = new Scheme_Transferable[length];
		System.arraycopy(storableObjects, 0, schemes, 0, length);
		return schemes;
	}

	public SchemeElement_Transferable[] transmitSchemeElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeElement_Transferable schemeElements[] = new SchemeElement_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeElements, 0, length);
		return schemeElements;
	}

	public SchemeElement_Transferable[] transmitSchemeElementsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeElement_Transferable schemeElements[] = new SchemeElement_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeElements, 0, length);
		return schemeElements;
	}

	public SchemeOptimizeInfo_Transferable[] transmitSchemeOptimizeInfos(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeOptimizeInfo_Transferable schemeOptimizeInfos[] = new SchemeOptimizeInfo_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfos, 0, length);
		return schemeOptimizeInfos;
	}

	public SchemeOptimizeInfo_Transferable[] transmitSchemeOptimizeInfosButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeOptimizeInfo_Transferable schemeOptimizeInfos[] = new SchemeOptimizeInfo_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfos, 0, length);
		return schemeOptimizeInfos;
	}

	public SchemeOptimizeInfoSwitch_Transferable[] transmitSchemeOptimizeInfoSwitches(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeOptimizeInfoSwitch_Transferable schemeOptimizeInfoSwitches[] = new SchemeOptimizeInfoSwitch_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoSwitches, 0, length);
		return schemeOptimizeInfoSwitches;
	}

	public SchemeOptimizeInfoSwitch_Transferable[] transmitSchemeOptimizeInfoSwitchesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeOptimizeInfoSwitch_Transferable schemeOptimizeInfoSwitches[] = new SchemeOptimizeInfoSwitch_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoSwitches, 0, length);
		return schemeOptimizeInfoSwitches;
	}

	public SchemeOptimizeInfoRtu_Transferable[] transmitSchemeOptimizeInfoRtus(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeOptimizeInfoRtu_Transferable schemeOptimizeInfoRtus[] = new SchemeOptimizeInfoRtu_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoRtus, 0, length);
		return schemeOptimizeInfoRtus;
	}

	public SchemeOptimizeInfoRtu_Transferable[] transmitSchemeOptimizeInfoRtusButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeOptimizeInfoRtu_Transferable schemeOptimizeInfoRtus[] = new SchemeOptimizeInfoRtu_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeOptimizeInfoRtus, 0, length);
		return schemeOptimizeInfoRtus;
	}

	public SchemeMonitoringSolution_Transferable[] transmitSchemeMonitoringSolutions(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeMonitoringSolution_Transferable schemeMonitoringSolutions[] = new SchemeMonitoringSolution_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeMonitoringSolutions, 0, length);
		return schemeMonitoringSolutions;
	}

	public SchemeMonitoringSolution_Transferable[] transmitSchemeMonitoringSolutionsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeMonitoringSolution_Transferable schemeMonitoringSolutions[] = new SchemeMonitoringSolution_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeMonitoringSolutions, 0, length);
		return schemeMonitoringSolutions;
	}

	public SchemeDevice_Transferable[] transmitSchemeDevices(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeDevice_Transferable schemeDevices[] = new SchemeDevice_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeDevices, 0, length);
		return schemeDevices;
	}

	public SchemeDevice_Transferable[] transmitSchemeDevicesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeDevice_Transferable schemeDevices[] = new SchemeDevice_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeDevices, 0, length);
		return schemeDevices;
	}

	public SchemePort_Transferable[] transmitSchemePorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemePort_Transferable schemePorts[] = new SchemePort_Transferable[length];
		System.arraycopy(storableObjects, 0, schemePorts, 0, length);
		return schemePorts;
	}

	public SchemePort_Transferable[] transmitSchemePortsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemePort_Transferable schemePorts[] = new SchemePort_Transferable[length];
		System.arraycopy(storableObjects, 0, schemePorts, 0, length);
		return schemePorts;
	}

	public SchemeCablePort_Transferable[] transmitSchemeCablePorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeCablePort_Transferable schemeCablePorts[] = new SchemeCablePort_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCablePorts, 0, length);
		return schemeCablePorts;
	}

	public SchemeCablePort_Transferable[] transmitSchemeCablePortsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeCablePort_Transferable schemeCablePorts[] = new SchemeCablePort_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCablePorts, 0, length);
		return schemeCablePorts;
	}

	public SchemeLink_Transferable[] transmitSchemeLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeLink_Transferable schemeLinks[] = new SchemeLink_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeLinks, 0, length);
		return schemeLinks;
	}

	public SchemeLink_Transferable[] transmitSchemeLinksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeLink_Transferable schemeLinks[] = new SchemeLink_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeLinks, 0, length);
		return schemeLinks;
	}

	public SchemeCableLink_Transferable[] transmitSchemeCableLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeCableLink_Transferable schemeCableLinks[] = new SchemeCableLink_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCableLinks, 0, length);
		return schemeCableLinks;
	}

	public SchemeCableLink_Transferable[] transmitSchemeCableLinksButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeCableLink_Transferable schemeCableLinks[] = new SchemeCableLink_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCableLinks, 0, length);
		return schemeCableLinks;
	}

	public SchemeCableThread_Transferable[] transmitSchemeCableThreads(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemeCableThread_Transferable schemeCableThreads[] = new SchemeCableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCableThreads, 0, length);
		return schemeCableThreads;
	}

	public SchemeCableThread_Transferable[] transmitSchemeCableThreadsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemeCableThread_Transferable schemeCableThreads[] = new SchemeCableThread_Transferable[length];
		System.arraycopy(storableObjects, 0, schemeCableThreads, 0, length);
		return schemeCableThreads;
	}

	public CableChannelingItem_Transferable[] transmitCableChannelingItems(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CableChannelingItem_Transferable cableChannelingItems[] = new CableChannelingItem_Transferable[length];
		System.arraycopy(storableObjects, 0, cableChannelingItems, 0, length);
		return cableChannelingItems;
	}

	public CableChannelingItem_Transferable[] transmitCableChannelingItemsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CableChannelingItem_Transferable cableChannelingItems[] = new CableChannelingItem_Transferable[length];
		System.arraycopy(storableObjects, 0, cableChannelingItems, 0, length);
		return cableChannelingItems;
	}

	public SchemePath_Transferable[] transmitSchemePaths(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final SchemePath_Transferable schemePaths[] = new SchemePath_Transferable[length];
		System.arraycopy(storableObjects, 0, schemePaths, 0, length);
		return schemePaths;
	}

	public SchemePath_Transferable[] transmitSchemePathsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final SchemePath_Transferable schemePaths[] = new SchemePath_Transferable[length];
		System.arraycopy(storableObjects, 0, schemePaths, 0, length);
		return schemePaths;
	}

	public PathElement_Transferable[] transmitPathElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final PathElement_Transferable pathElements[] = new PathElement_Transferable[length];
		System.arraycopy(storableObjects, 0, pathElements, 0, length);
		return pathElements;
	}

	public PathElement_Transferable[] transmitPathElementsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final PathElement_Transferable pathElements[] = new PathElement_Transferable[length];
		System.arraycopy(storableObjects, 0, pathElements, 0, length);
		return pathElements;
	}

	/**
	 * @deprecated
	 */
	public final Identifier_Transferable[] transmitRefreshedSchemeObjects(
			final StorableObject_Transferable headers[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKey,
				new Identifier_TransferableHolder(),
				new Identifier_TransferableHolder());

		final Map headerMap = new HashMap();
		for (int i = 0; i < headers.length; i++)
			headerMap.put(new Identifier(headers[i].id), headers[i]);

		try {
			StorableObjectPool.refresh();

			final Set storableObjects = StorableObjectPool.getStorableObjects(headerMap.keySet(), true);
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				final StorableObject_Transferable header = (StorableObject_Transferable) headerMap.get(storableObject.getId());
				/*
				 * Remove objects with older versions as well as objects with the same versions.
				 * Not only with older ones!
				 */
				if (!storableObject.hasNewerVersion(header.version))
					storableObjectIterator.remove();
			}

			return Identifier.createTransferables(storableObjects);
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}
}
