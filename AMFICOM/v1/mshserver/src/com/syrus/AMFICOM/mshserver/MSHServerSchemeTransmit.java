/*-
 * $Id: MSHServerSchemeTransmit.java,v 1.4 2005/05/18 13:34:16 bass Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/05/18 13:34:16 $
 * @module mshserver_v1
 */
abstract class MSHServerSchemeTransmit extends MSHServerMapTransmit {
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
