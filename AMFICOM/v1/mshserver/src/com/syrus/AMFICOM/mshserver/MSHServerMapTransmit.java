/*-
 * $Id: MSHServerMapTransmit.java,v 1.3 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:47:53 $
 * @module mshserver_v1
 */
abstract class MSHServerMapTransmit extends MSHServerSchemeReceive {
	abstract IDLEntity transmitStorableObject(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException;

	/*-********************************************************************
	 * Map -- transmit a single object.                                   *
	 **********************************************************************/

	public final SiteNode_Transferable transmitSiteNode(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SiteNode_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final TopologicalNode_Transferable transmitTopologicalNode(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (TopologicalNode_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final NodeLink_Transferable transmitNodeLink(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (NodeLink_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final Mark_Transferable transmitMark(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (Mark_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final PhysicalLink_Transferable transmitPhysicalLink(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (PhysicalLink_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final Collector_Transferable transmitCollector(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (Collector_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final Map_Transferable transmitMap(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (Map_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final SiteNodeType_Transferable transmitSiteNodeType(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (SiteNodeType_Transferable) transmitStorableObject(id, sessionKey);
	}
	
	public final PhysicalLinkType_Transferable transmitPhysicalLinkType(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return (PhysicalLinkType_Transferable) transmitStorableObject(id, sessionKey);
	}

	/*-********************************************************************
	 * Map -- transmit multiple objects.                                  *
	 **********************************************************************/
}
