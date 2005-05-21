/*-
 * $Id: MSHServerSchemeReceive.java,v 1.5 2005/05/21 19:43:37 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServerOperations;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
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
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/21 19:43:37 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeReceive extends MSHServerMapReceive {
	/*-********************************************************************
	 * Scheme -- receive multiple objects.                                *
	 **********************************************************************/

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoGroups(SchemeProtoGroup_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeProtoGroups(
			final SchemeProtoGroup_Transferable schemeProtoGroups[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeProtoGroups.length;
			final Set schemeProtoGroups1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeProtoGroup schemeProtoGroup = newSchemeProtoGroup(schemeProtoGroups[i]);
				StorableObjectPool.putStorableObject(schemeProtoGroup);
				schemeProtoGroups1.add(schemeProtoGroup);
			}
			return receiveStorableObjects(schemeProtoGroups1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeProtoElements
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoElements(SchemeProtoElement_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeProtoElements(
			final SchemeProtoElement_Transferable schemeProtoElements[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeProtoElements.length;
			final Set schemeProtoElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeProtoElement schemeProtoElement = newSchemeProtoElement(schemeProtoElements[i]);
				StorableObjectPool.putStorableObject(schemeProtoElement);
				schemeProtoElements1.add(schemeProtoElement);
			}
			return receiveStorableObjects(schemeProtoElements1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemes
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemes(Scheme_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemes(
			final Scheme_Transferable schemes[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemes.length;
			final Set schemes1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final Scheme scheme = newScheme(schemes[i]);
				StorableObjectPool.putStorableObject(scheme);
				schemes1.add(scheme);
			}
			return receiveStorableObjects(schemes1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeElements
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeElements(SchemeElement_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeElements(
			final SchemeElement_Transferable schemeElements[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeElements.length;
			final Set schemeElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeElement schemeElement = newSchemeElement(schemeElements[i]);
				StorableObjectPool.putStorableObject(schemeElement);
				schemeElements1.add(schemeElement);
			}
			return receiveStorableObjects(schemeElements1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeOptimizeInfos(SchemeOptimizeInfo_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeOptimizeInfos(
			final SchemeOptimizeInfo_Transferable schemeOptimizeInfos[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeOptimizeInfos.length;
			final Set schemeOptimizeInfos1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeOptimizeInfo schemeOptimizeInfo = newSchemeOptimizeInfo(schemeOptimizeInfos[i]);
				StorableObjectPool.putStorableObject(schemeOptimizeInfo);
				schemeOptimizeInfos1.add(schemeOptimizeInfo);
			}
			return receiveStorableObjects(schemeOptimizeInfos1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeOptimizeInfoSwitches
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.mshserver.corba.MSHServerOperations#receiveSchemeOptimizeInfoSwitches(com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoSwitch_Transferable[], boolean, com.syrus.AMFICOM.security.corba.SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeOptimizeInfoSwitches(
			final SchemeOptimizeInfoSwitch_Transferable schemeOptimizeInfoSwitches[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
	}
			
	/**
	 * @param schemeOptimizeInfoRtus
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.mshserver.corba.MSHServerOperations#receiveSchemeOptimizeInfoRtus(com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoRtu_Transferable[], boolean, com.syrus.AMFICOM.security.corba.SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeOptimizeInfoRtus(
			final SchemeOptimizeInfoRtu_Transferable schemeOptimizeInfoRtus[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeMonitoringSolutions(SchemeMonitoringSolution_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeMonitoringSolutions(
			final SchemeMonitoringSolution_Transferable schemeMonitoringSolutions[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeMonitoringSolutions.length;
			final Set schemeMonitoringSolutions1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeMonitoringSolution schemeMonitoringSolution = newSchemeMonitoringSolution(schemeMonitoringSolutions[i]);
				StorableObjectPool.putStorableObject(schemeMonitoringSolution);
				schemeMonitoringSolutions1.add(schemeMonitoringSolution);
			}
			return receiveStorableObjects(schemeMonitoringSolutions1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeDevices
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeDevices(SchemeDevice_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeDevices(
			final SchemeDevice_Transferable schemeDevices[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeDevices.length;
			final Set schemeDevices1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeDevice schemeDevice = newSchemeDevice(schemeDevices[i]);
				StorableObjectPool.putStorableObject(schemeDevice);
				schemeDevices1.add(schemeDevice);
			}
			return receiveStorableObjects(schemeDevices1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemePorts
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePorts(SchemePort_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemePorts(
			final SchemePort_Transferable schemePorts[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemePorts.length;
			final Set schemePorts1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemePort schemePort = newSchemePort(schemePorts[i]);
				StorableObjectPool.putStorableObject(schemePort);
				schemePorts1.add(schemePort);
			}
			return receiveStorableObjects(schemePorts1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCablePorts
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCablePorts(SchemeCablePort_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCablePorts(
			final SchemeCablePort_Transferable schemeCablePorts[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCablePorts.length;
			final Set schemeCablePorts1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCablePort schemeCablePort = newSchemeCablePort(schemeCablePorts[i]);
				StorableObjectPool.putStorableObject(schemeCablePort);
				schemeCablePorts1.add(schemeCablePort);
			}
			return receiveStorableObjects(schemeCablePorts1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeLinks
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeLinks(SchemeLink_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeLinks(
			final SchemeLink_Transferable schemeLinks[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeLinks.length;
			final Set schemeLinks1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeLink schemeLink = newSchemeLink(schemeLinks[i]);
				StorableObjectPool.putStorableObject(schemeLink);
				schemeLinks1.add(schemeLink);
			}
			return receiveStorableObjects(schemeLinks1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCableLinks
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableLinks(SchemeCableLink_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCableLinks(
			final SchemeCableLink_Transferable schemeCableLinks[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCableLinks.length;
			final Set schemeCableLinks1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCableLink schemeCableLink = newSchemeCableLink(schemeCableLinks[i]);
				StorableObjectPool.putStorableObject(schemeCableLink);
				schemeCableLinks1.add(schemeCableLink);
			}
			return receiveStorableObjects(schemeCableLinks1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCableThreads
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableThreads(SchemeCableThread_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCableThreads(
			final SchemeCableThread_Transferable schemeCableThreads[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCableThreads.length;
			final Set schemeCableThreads1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCableThread schemeCableThread = newSchemeCableThread(schemeCableThreads[i]);
				StorableObjectPool.putStorableObject(schemeCableThread);
				schemeCableThreads1.add(schemeCableThread);
			}
			return receiveStorableObjects(schemeCableThreads1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param cableChannelingItems
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveCableChannelingItems(CableChannelingItem_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveCableChannelingItems(
			final CableChannelingItem_Transferable cableChannelingItems[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = cableChannelingItems.length;
			final Set cableChannelingItems1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final CableChannelingItem cableChannelingItem = newCableChannelingItem(cableChannelingItems[i]);
				StorableObjectPool.putStorableObject(cableChannelingItem);
				cableChannelingItems1.add(cableChannelingItem);
			}
			return receiveStorableObjects(cableChannelingItems1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemePaths
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePaths(SchemePath_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemePaths(
			final SchemePath_Transferable schemePaths[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = schemePaths.length;
			final Set schemePaths1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemePath schemePath = newSchemePath(schemePaths[i]);
				StorableObjectPool.putStorableObject(schemePath);
				schemePaths1.add(schemePath);
			}
			return receiveStorableObjects(schemePaths1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param pathElements
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receivePathElements(PathElement_Transferable[], boolean, SessionKey_Transferable)
	 */
	public final StorableObject_Transferable[] receivePathElements(
			final PathElement_Transferable pathElements[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final int length = pathElements.length;
			final Set pathElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final PathElement pathElement = newPathElement(pathElements[i]);
				StorableObjectPool.putStorableObject(pathElement);
				pathElements1.add(pathElement);
			}
			return receiveStorableObjects(pathElements1, sessionKey, force);
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
}
