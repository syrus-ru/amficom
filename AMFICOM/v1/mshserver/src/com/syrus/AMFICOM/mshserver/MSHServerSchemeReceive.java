/*-
 * $Id: MSHServerSchemeReceive.java,v 1.2 2005/04/23 15:36:31 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
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
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableThread_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePath_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup_Transferable;
import com.syrus.AMFICOM.scheme.corba.Scheme_Transferable;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/04/23 15:36:31 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeReceive extends MSHServerMapReceive {
	/*-********************************************************************
	 * Scheme -- receive a single object.                                 *
	 **********************************************************************/

	/**
	 * @param schemeProtoGroup
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoGroup(SchemeProtoGroup_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeProtoGroup(
			final SchemeProtoGroup_Transferable schemeProtoGroup,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeProtoGroup(schemeProtoGroup), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeProtoElement
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoElement(SchemeProtoElement_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeProtoElement(
			final SchemeProtoElement_Transferable schemeProtoElement,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeProtoElement(schemeProtoElement), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param scheme
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveScheme(Scheme_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveScheme(
			final Scheme_Transferable scheme,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newScheme(scheme), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeElement
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeElement(SchemeElement_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeElement(
			final SchemeElement_Transferable schemeElement,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeElement(schemeElement), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeOptimizeInfo
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeOptimizeInfo(SchemeOptimizeInfo_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeOptimizeInfo(
			final SchemeOptimizeInfo_Transferable schemeOptimizeInfo,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeOptimizeInfo(schemeOptimizeInfo), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeMonitoringSolution(SchemeMonitoringSolution_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeMonitoringSolution(
			final SchemeMonitoringSolution_Transferable schemeMonitoringSolution,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeMonitoringSolution(schemeMonitoringSolution), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeDevice
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeDevice(SchemeDevice_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeDevice(
			final SchemeDevice_Transferable schemeDevice,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeDevice(schemeDevice), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemePort
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePort(SchemePort_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemePort(
			final SchemePort_Transferable schemePort,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemePort(schemePort), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCablePort
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCablePort(SchemeCablePort_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeCablePort(
			final SchemeCablePort_Transferable schemeCablePort,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeCablePort(schemeCablePort), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeLink
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeLink(SchemeLink_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeLink(
			final SchemeLink_Transferable schemeLink,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeLink(schemeLink), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCableLink
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableLink(SchemeCableLink_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeCableLink(
			final SchemeCableLink_Transferable schemeCableLink,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeCableLink(schemeCableLink), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemeCableThread
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableThread(SchemeCableThread_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemeCableThread(
			final SchemeCableThread_Transferable schemeCableThread,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemeCableThread(schemeCableThread), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param cableChannelingItem
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveCableChannelingItem(CableChannelingItem_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveCableChannelingItem(
			final CableChannelingItem_Transferable cableChannelingItem,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newCableChannelingItem(cableChannelingItem), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param schemePath
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePath(SchemePath_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receiveSchemePath(
			final SchemePath_Transferable schemePath,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newSchemePath(schemePath), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param pathElement
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receivePathElement(PathElement_Transferable, boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable receivePathElement(
			final PathElement_Transferable pathElement,
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			return receiveStorableObject(newPathElement(pathElement), accessIdentityT, force);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/*-********************************************************************
	 * Scheme -- receive multiple objects.                                *
	 **********************************************************************/

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoGroups(SchemeProtoGroup_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeProtoGroups(
			final SchemeProtoGroup_Transferable schemeProtoGroups[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeProtoGroups.length;
			final Set schemeProtoGroups1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeProtoGroup schemeProtoGroup = newSchemeProtoGroup(schemeProtoGroups[i]);
				SchemeStorableObjectPool.putStorableObject(schemeProtoGroup);
				schemeProtoGroups1.add(schemeProtoGroup);
			}
			return receiveStorableObjects(schemeProtoGroups1, accessIdentityT, force);
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
	 * @param schemeProtoElements
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeProtoElements(SchemeProtoElement_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeProtoElements(
			final SchemeProtoElement_Transferable schemeProtoElements[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeProtoElements.length;
			final Set schemeProtoElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeProtoElement schemeProtoElement = newSchemeProtoElement(schemeProtoElements[i]);
				SchemeStorableObjectPool.putStorableObject(schemeProtoElement);
				schemeProtoElements1.add(schemeProtoElement);
			}
			return receiveStorableObjects(schemeProtoElements1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemes(Scheme_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemes(
			final Scheme_Transferable schemes[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemes.length;
			final Set schemes1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final Scheme scheme = newScheme(schemes[i]);
				SchemeStorableObjectPool.putStorableObject(scheme);
				schemes1.add(scheme);
			}
			return receiveStorableObjects(schemes1, accessIdentityT, force);
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
	 * @param schemeElements
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeElements(SchemeElement_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeElements(
			final SchemeElement_Transferable schemeElements[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeElements.length;
			final Set schemeElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeElement schemeElement = newSchemeElement(schemeElements[i]);
				SchemeStorableObjectPool.putStorableObject(schemeElement);
				schemeElements1.add(schemeElement);
			}
			return receiveStorableObjects(schemeElements1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeOptimizeInfos(SchemeOptimizeInfo_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeOptimizeInfos(
			final SchemeOptimizeInfo_Transferable schemeOptimizeInfos[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeOptimizeInfos.length;
			final Set schemeOptimizeInfos1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeOptimizeInfo schemeOptimizeInfo = newSchemeOptimizeInfo(schemeOptimizeInfos[i]);
				SchemeStorableObjectPool.putStorableObject(schemeOptimizeInfo);
				schemeOptimizeInfos1.add(schemeOptimizeInfo);
			}
			return receiveStorableObjects(schemeOptimizeInfos1, accessIdentityT, force);
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
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeMonitoringSolutions(SchemeMonitoringSolution_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeMonitoringSolutions(
			final SchemeMonitoringSolution_Transferable schemeMonitoringSolutions[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeMonitoringSolutions.length;
			final Set schemeMonitoringSolutions1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeMonitoringSolution schemeMonitoringSolution = newSchemeMonitoringSolution(schemeMonitoringSolutions[i]);
				SchemeStorableObjectPool.putStorableObject(schemeMonitoringSolution);
				schemeMonitoringSolutions1.add(schemeMonitoringSolution);
			}
			return receiveStorableObjects(schemeMonitoringSolutions1, accessIdentityT, force);
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
	 * @param schemeDevices
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeDevices(SchemeDevice_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeDevices(
			final SchemeDevice_Transferable schemeDevices[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeDevices.length;
			final Set schemeDevices1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeDevice schemeDevice = newSchemeDevice(schemeDevices[i]);
				SchemeStorableObjectPool.putStorableObject(schemeDevice);
				schemeDevices1.add(schemeDevice);
			}
			return receiveStorableObjects(schemeDevices1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePorts(SchemePort_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemePorts(
			final SchemePort_Transferable schemePorts[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemePorts.length;
			final Set schemePorts1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemePort schemePort = newSchemePort(schemePorts[i]);
				SchemeStorableObjectPool.putStorableObject(schemePort);
				schemePorts1.add(schemePort);
			}
			return receiveStorableObjects(schemePorts1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCablePorts(SchemeCablePort_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCablePorts(
			final SchemeCablePort_Transferable schemeCablePorts[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCablePorts.length;
			final Set schemeCablePorts1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCablePort schemeCablePort = newSchemeCablePort(schemeCablePorts[i]);
				SchemeStorableObjectPool.putStorableObject(schemeCablePort);
				schemeCablePorts1.add(schemeCablePort);
			}
			return receiveStorableObjects(schemeCablePorts1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeLinks(SchemeLink_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeLinks(
			final SchemeLink_Transferable schemeLinks[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeLinks.length;
			final Set schemeLinks1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeLink schemeLink = newSchemeLink(schemeLinks[i]);
				SchemeStorableObjectPool.putStorableObject(schemeLink);
				schemeLinks1.add(schemeLink);
			}
			return receiveStorableObjects(schemeLinks1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableLinks(SchemeCableLink_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCableLinks(
			final SchemeCableLink_Transferable schemeCableLinks[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCableLinks.length;
			final Set schemeCableLinks1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCableLink schemeCableLink = newSchemeCableLink(schemeCableLinks[i]);
				SchemeStorableObjectPool.putStorableObject(schemeCableLink);
				schemeCableLinks1.add(schemeCableLink);
			}
			return receiveStorableObjects(schemeCableLinks1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemeCableThreads(SchemeCableThread_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemeCableThreads(
			final SchemeCableThread_Transferable schemeCableThreads[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemeCableThreads.length;
			final Set schemeCableThreads1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemeCableThread schemeCableThread = newSchemeCableThread(schemeCableThreads[i]);
				SchemeStorableObjectPool.putStorableObject(schemeCableThread);
				schemeCableThreads1.add(schemeCableThread);
			}
			return receiveStorableObjects(schemeCableThreads1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveCableChannelingItems(CableChannelingItem_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveCableChannelingItems(
			final CableChannelingItem_Transferable cableChannelingItems[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = cableChannelingItems.length;
			final Set cableChannelingItems1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final CableChannelingItem cableChannelingItem = newCableChannelingItem(cableChannelingItems[i]);
				SchemeStorableObjectPool.putStorableObject(cableChannelingItem);
				cableChannelingItems1.add(cableChannelingItem);
			}
			return receiveStorableObjects(cableChannelingItems1, accessIdentityT, force);
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
	 * @param schemePaths
	 * @param force
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receiveSchemePaths(SchemePath_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receiveSchemePaths(
			final SchemePath_Transferable schemePaths[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = schemePaths.length;
			final Set schemePaths1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final SchemePath schemePath = newSchemePath(schemePaths[i]);
				SchemeStorableObjectPool.putStorableObject(schemePath);
				schemePaths1.add(schemePath);
			}
			return receiveStorableObjects(schemePaths1, accessIdentityT, force);
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
	 * @param accessIdentityT
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#receivePathElements(PathElement_Transferable[], boolean, AccessIdentity_Transferable)
	 */
	public final StorableObject_Transferable[] receivePathElements(
			final PathElement_Transferable pathElements[],
			final boolean force,
			final AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		try {
			final int length = pathElements.length;
			final Set pathElements1 = new HashSet(length);
			for (int i = 0; i < length; i++) {
				final PathElement pathElement = newPathElement(pathElements[i]);
				SchemeStorableObjectPool.putStorableObject(pathElement);
				pathElements1.add(pathElement);
			}
			return receiveStorableObjects(pathElements1, accessIdentityT, force);
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
}
