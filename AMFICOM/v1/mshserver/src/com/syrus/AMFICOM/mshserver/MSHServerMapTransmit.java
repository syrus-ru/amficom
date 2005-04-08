/*-
 * $Id: MSHServerMapTransmit.java,v 1.1 2005/04/08 09:32:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
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

import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/08 09:32:27 $
 * @module mshserver_v1
 */
abstract class MSHServerMapTransmit extends MSHServerSchemeReceive {
	abstract IDLEntity transmitStorableObject(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException;

	/*-********************************************************************
	 * Map -- transmit a single object.                                   *
	 **********************************************************************/

	public final SiteNode_Transferable transmitSiteNode(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (SiteNode_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final TopologicalNode_Transferable transmitTopologicalNode(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (TopologicalNode_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final NodeLink_Transferable transmitNodeLink(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (NodeLink_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final Mark_Transferable transmitMark(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (Mark_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final PhysicalLink_Transferable transmitPhysicalLink(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (PhysicalLink_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final Collector_Transferable transmitCollector(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (Collector_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final Map_Transferable transmitMap(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (Map_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final SiteNodeType_Transferable transmitSiteNodeType(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (SiteNodeType_Transferable) transmitStorableObject(id, accessIdentifier);
	}
	
	public final PhysicalLinkType_Transferable transmitPhysicalLinkType(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (PhysicalLinkType_Transferable) transmitStorableObject(id, accessIdentifier);
	}

	/*-********************************************************************
	 * Map -- transmit multiple objects.                                  *
	 **********************************************************************/
}
