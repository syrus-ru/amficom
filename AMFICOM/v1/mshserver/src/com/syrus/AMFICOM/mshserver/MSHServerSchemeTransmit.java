/*-
 * $Id: MSHServerSchemeTransmit.java,v 1.3 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServerOperations;
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
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:47:53 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeTransmit extends MSHServerMapTransmit {
	/*-********************************************************************
	 * Scheme -- transmit a single object.                                *
	 **********************************************************************/

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeProtoGroup(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeProtoGroup_Transferable transmitSchemeProtoGroup(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeProtoGroup_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeProtoElement(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeProtoElement_Transferable transmitSchemeProtoElement(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeProtoElement_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitScheme(Identifier_Transferable, SessionKey_Transferable)
	 */
	public Scheme_Transferable transmitScheme(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (Scheme_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeElement(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeElement_Transferable transmitSchemeElement(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeElement_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeOptimizeInfo(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeOptimizeInfo_Transferable transmitSchemeOptimizeInfo(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeOptimizeInfo_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeMonitoringSolution(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeMonitoringSolution_Transferable transmitSchemeMonitoringSolution(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeMonitoringSolution_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeDevice(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeDevice_Transferable transmitSchemeDevice(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeDevice_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemePort(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemePort_Transferable transmitSchemePort(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemePort_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCablePort(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeCablePort_Transferable transmitSchemeCablePort(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeCablePort_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeLink(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeLink_Transferable transmitSchemeLink(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeLink_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCableLink(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeCableLink_Transferable transmitSchemeCableLink(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeCableLink_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCableThread(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemeCableThread_Transferable transmitSchemeCableThread(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemeCableThread_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitCableChannelingItem(Identifier_Transferable, SessionKey_Transferable)
	 */
	public CableChannelingItem_Transferable transmitCableChannelingItem(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (CableChannelingItem_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemePath(Identifier_Transferable, SessionKey_Transferable)
	 */
	public SchemePath_Transferable transmitSchemePath(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SchemePath_Transferable) transmitStorableObject(id, sessionKey);
	}

	/**
	 * @param id
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitPathElement(Identifier_Transferable, SessionKey_Transferable)
	 */
	public PathElement_Transferable transmitPathElement(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (PathElement_Transferable) transmitStorableObject(id, sessionKey);
	}

	/*-********************************************************************
	 * Scheme -- transmit multiple objects.                               *
	 **********************************************************************/

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeProtoGroups(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeProtoGroup_Transferable[] transmitSchemeProtoGroups(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeProtoElements(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeProtoElement_Transferable[] transmitSchemeProtoElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemes(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public Scheme_Transferable[] transmitSchemes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeElements(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeElement_Transferable[] transmitSchemeElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeOptimizeInfos(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeOptimizeInfo_Transferable[] transmitSchemeOptimizeInfos(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeMonitoringSolutions(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeMonitoringSolution_Transferable[] transmitSchemeMonitoringSolutions(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeDevices(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeDevice_Transferable[] transmitSchemeDevices(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemePorts(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemePort_Transferable[] transmitSchemePorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCablePorts(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeCablePort_Transferable[] transmitSchemeCablePorts(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeLinks(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeLink_Transferable[] transmitSchemeLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCableLinks(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeCableLink_Transferable[] transmitSchemeCableLinks(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemeCableThreads(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemeCableThread_Transferable[] transmitSchemeCableThreads(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitCableChannelingItems(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public CableChannelingItem_Transferable[] transmitCableChannelingItems(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitSchemePaths(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public SchemePath_Transferable[] transmitSchemePaths(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see MSHServerOperations#transmitPathElements(Identifier_Transferable[], SessionKey_Transferable)
	 */
	public PathElement_Transferable[] transmitPathElements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		throw new UnsupportedOperationException();
	}
}
