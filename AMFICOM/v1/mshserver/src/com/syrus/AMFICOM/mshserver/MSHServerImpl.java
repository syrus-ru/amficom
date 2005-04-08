/*-
 * $Id: MSHServerImpl.java,v 1.6 2005/04/08 09:32:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapDatabaseContext;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.scheme.SchemeDatabaseContext;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/08 09:32:27 $
 * @author $Author: bass $
 * @module mshserver_1
 */
public final class MSHServerImpl extends MSHServerSchemeTransmit {
	private static final long serialVersionUID = 3762810480783274295L;

	/*-********************************************************************
	 * Method(s) specified by IdentifierGeneratorServer.                  *
	 **********************************************************************/

	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("MSHServerImpl.getGeneratedIdentifier | generate new Identifer for " //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO,
												"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) //$NON-NLS-1$
														+ "'"); //$NON-NLS-1$
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
												"Cannot create major/minor entries of identifier for entity: '" //$NON-NLS-1$
														+ ObjectEntities.codeToString(entityCode) + "' -- " //$NON-NLS-1$
														+ ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.getGeneratedIdentifierRange | generate new Identifer range " + size + " for " //$NON-NLS-1$ //$NON-NLS-2$
				+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO,
												"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) //$NON-NLS-1$
														+ "'"); //$NON-NLS-1$
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
												"Cannot create major/minor entries of identifier for entity: '" //$NON-NLS-1$
														+ ObjectEntities.codeToString(entityCode) + "' -- " //$NON-NLS-1$
														+ ige.getMessage());
		}
	}

	public void delete(final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		final Identifier id1 = new Identifier(id);
		Log.debugMessage("MSHServerImpl.delete | Trying to delete object '" //$NON-NLS-1$
				+ id1 + "' as requested by user '" //$NON-NLS-1$
				+ (new AccessIdentity(accessIdentifier)).getUserId() + '\'',
				Log.INFO);
		final short entityCode = id1.getMajor();
		if (ObjectGroupEntities.isInMapGroup(entityCode))
			MapStorableObjectPool.delete(id1);
		else if (ObjectGroupEntities.isInSchemeGroup(entityCode))
			SchemeStorableObjectPool.delete(id1);
		else
			Log.errorMessage("MSHServerImpl.delete | Wrong entity code: " + entityCode); //$NON-NLS-1$
	}

	public void deleteList(final Identifier_Transferable ids[],
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.deleteList | Trying to delete " //$NON-NLS-1$
				+ ids.length
				+ " object(s) as requested by user '" //$NON-NLS-1$
				+ (new AccessIdentity(accessIdentifier)).getUserId()
				+ '\'', Log.INFO);
		final Set ids1 = Identifier.fromTransferables(ids);
		final Set mapIds = new HashSet(ids.length);
		final Set schemeIds = new HashSet(ids.length);
		for (final Iterator idIterator = ids1.iterator(); idIterator.hasNext();) {
			final Identifier id = (Identifier) idIterator.next();
			final short entityCode = id.getMajor();
			if (ObjectGroupEntities.isInMapGroup(entityCode))
				mapIds.add(id);
			else if (ObjectGroupEntities.isInSchemeGroup(entityCode))
				schemeIds.add(id);
			else
				Log.errorMessage("MSHServerImpl.deleteList | Wrong entity code: " + entityCode); //$NON-NLS-1$
		}
		try {
			MapStorableObjectPool.delete(mapIds);
			SchemeStorableObjectPool.delete(schemeIds);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			throw new AMFICOMRemoteException(ErrorCode.ERROR_DELETE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} 
	}

	/**
	 * @param storableObject
	 * @param accessIdentifier
	 * @param force
	 * @throws AMFICOMRemoteException
	 * @see MSHServerMapReceive#receiveStorableObject(StorableObject, AccessIdentifier_Transferable, boolean)
	 */
	StorableObject_Transferable receiveStorableObject(
			final StorableObject storableObject,
			final AccessIdentifier_Transferable accessIdentifier, 
			final boolean force)
			throws AMFICOMRemoteException {
		try {
			final short entityCode = storableObject.getId().getMajor();
			final Identifier userId = (new AccessIdentity(accessIdentifier)).getUserId();
			Log.debugMessage("MSHServerImpl.receiveStorableObject | Receiving a(n) " //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode)
					+ " as requested by user '" + userId + '\'', //$NON-NLS-1$
					Log.INFO);
			StorableObjectDatabase storableObjectDatabase = null;
			if (ObjectGroupEntities.isInMapGroup(entityCode)) {
				storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
				MapStorableObjectPool.putStorableObject(storableObject);
			} else if (ObjectGroupEntities.isInSchemeGroup(entityCode)) {
				storableObjectDatabase = SchemeDatabaseContext.getDatabase(entityCode);
				SchemeStorableObjectPool.putStorableObject(storableObject);
			} else
				assert false;
			storableObjectDatabase.update(storableObject,
					userId,
					force
						? StorableObjectDatabase.UPDATE_FORCE
						: StorableObjectDatabase.UPDATE_CHECK);
			return storableObject.getHeaderTransferable();
		} catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (final VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param storableObjects
	 * @param accessIdentifier
	 * @param force
	 * @throws AMFICOMRemoteException
	 * @see MSHServerMapReceive#receiveStorableObjects(Set, AccessIdentifier_Transferable, boolean)
	 */
	StorableObject_Transferable[] receiveStorableObjects(
			final Set storableObjects,
			final AccessIdentifier_Transferable accessIdentifier,
			final boolean force)
			throws AMFICOMRemoteException {
		try {
			if (storableObjects.isEmpty())
				return new StorableObject_Transferable[0];
			assert StorableObject.hasSingleTypeEntities(storableObjects);
			final short entityCode = StorableObject.getEntityCodeOfStorableObjects(storableObjects);
			final Identifier userId = (new AccessIdentity(accessIdentifier)).getUserId();
			Log.debugMessage("MSHServerImpl.receiveStorableObjects | Receiving " //$NON-NLS-1$
					+ storableObjects.size() + ' '
					+ ObjectEntities.codeToString(entityCode)
					+ "(s) as requested by user '" + userId + '\'', ///$NON-NLS-1$
					Log.INFO);
			StorableObjectDatabase storableObjectDatabase = null;
			if (ObjectGroupEntities.isInMapGroup(entityCode))
				storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
			else if (ObjectGroupEntities.isInSchemeGroup(entityCode))
				storableObjectDatabase = SchemeDatabaseContext.getDatabase(entityCode);
			else
				assert false;
			storableObjectDatabase.update(storableObjects,
					userId,
					force
						? StorableObjectDatabase.UPDATE_FORCE
						: StorableObjectDatabase.UPDATE_CHECK);
			return getListHeaders(storableObjects);
		} catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		} catch (final VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param id
	 * @param accessIdentifier
	 * @throws AMFICOMRemoteException
	 * @see MSHServerMapTransmit#transmitStorableObject(Identifier_Transferable, AccessIdentifier_Transferable)
	 */
	IDLEntity transmitStorableObject(
			final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			final Identifier id1 = new Identifier(id);
			final short entityCode = id1.getMajor();
			final Identifier userId = (new AccessIdentity(accessIdentifier)).getUserId();
			Log.debugMessage("MSHServerImpl.transmitStorableObject | Transmitting a(n) " //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode)
					+ " as requested by user '" + userId + '\'', //$NON-NLS-1$
					Log.INFO);
			StorableObject storableObject = null;
			if (ObjectGroupEntities.isInMapGroup(entityCode))
				storableObject = MapStorableObjectPool.getStorableObject(id1, true);
			else if (ObjectGroupEntities.isInSchemeGroup(entityCode))
				storableObject = SchemeStorableObjectPool.getStorableObject(id1, true);
			else
				assert false;
			return storableObject.getTransferable();
		} catch (final ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		} catch (final RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (final CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (final DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Collection transmitObjects(Identifier_Transferable[] ids_Transferable, StorableObjectCondition condition)
			throws AMFICOMRemoteException {
		try {
			Collection collection = null;
			if (ids_Transferable.length > 0) {
				Set idsList = new HashSet(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));

				collection = MapStorableObjectPool.getStorableObjects(idsList, true);
			} else
				collection = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);

			return collection;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public SiteNode_Transferable[] transmitSiteNodes(	Identifier_Transferable[] ids_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitSiteNodes | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.SITE_NODE_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		SiteNode_Transferable[] transferables = new SiteNode_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			SiteNode siteNode = (SiteNode) it.next();
			transferables[i] = (SiteNode_Transferable) siteNode.getTransferable();
		}

		return transferables;
	}

	public TopologicalNode_Transferable[] transmitTopologicalNodes(	Identifier_Transferable[] ids_Transferable,
																	AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitTopologicalNodes | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		TopologicalNode_Transferable[] transferables = new TopologicalNode_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			TopologicalNode topologicalNode = (TopologicalNode) it.next();
			transferables[i] = (TopologicalNode_Transferable) topologicalNode.getTransferable();
		}

		return transferables;
	}

	public NodeLink_Transferable[] transmitNodeLinks(	Identifier_Transferable[] ids_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitNodeLinks | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.NODE_LINK_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		NodeLink_Transferable[] transferables = new NodeLink_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			NodeLink nodeLink = (NodeLink) it.next();
			transferables[i] = (NodeLink_Transferable) nodeLink.getTransferable();
		}

		return transferables;
	}

	public Mark_Transferable[] transmitMarks(	Identifier_Transferable[] ids_Transferable,
												AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitMarks | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.MARK_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		Mark_Transferable[] transferables = new Mark_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			Mark mark = (Mark) it.next();
			transferables[i] = (Mark_Transferable) mark.getTransferable();
		}

		return transferables;
	}

	public PhysicalLink_Transferable[] transmitPhysicalLinks(	Identifier_Transferable[] ids_Transferable,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitPhysicalLinks | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.MARK_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		PhysicalLink_Transferable[] transferables = new PhysicalLink_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			PhysicalLink physicalLink = (PhysicalLink) it.next();
			transferables[i] = (PhysicalLink_Transferable) physicalLink.getTransferable();
		}

		return transferables;
	}

	public Collector_Transferable[] transmitCollectors(	Identifier_Transferable[] ids_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitCollectors | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.COLLECTOR_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		Collector_Transferable[] transferables = new Collector_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			Collector collector = (Collector) it.next();
			transferables[i] = (Collector_Transferable) collector.getTransferable();
		}

		return transferables;
	}

	public Map_Transferable[] transmitMaps(	Identifier_Transferable[] ids_Transferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitMaps | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.MAP_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		Map_Transferable[] transferables = new Map_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			Map map = (Map) it.next();
			transferables[i] = (Map_Transferable) map.getTransferable();
		}

		return transferables;
	}

	public SiteNodeType_Transferable[] transmitSiteNodeTypes(	Identifier_Transferable[] ids_Transferable,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitSiteNodeTypes | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		SiteNodeType_Transferable[] transferables = new SiteNodeType_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			SiteNodeType siteNodeType = (SiteNodeType) it.next();
			transferables[i] = (SiteNodeType_Transferable) siteNodeType.getTransferable();
		}

		return transferables;
	}

	public PhysicalLinkType_Transferable[] transmitPhysicalLinkTypes(	Identifier_Transferable[] ids_Transferable,
																		AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier domainId = new Identifier(accessIdentifier.domain_id);

		Log.debugMessage("MSHServerImpl.transmitPhysicalLinkTypes | requiere " //$NON-NLS-1$
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) //$NON-NLS-1$
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$

		StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		Collection collection = this.transmitObjects(ids_Transferable, condition);

		PhysicalLinkType_Transferable[] transferables = new PhysicalLinkType_Transferable[collection.size()];

		int i = 0;
		for (Iterator it = collection.iterator(); it.hasNext(); i++) {
			PhysicalLinkType physicalLinkType = (PhysicalLinkType) it.next();
			transferables[i] = (PhysicalLinkType_Transferable) physicalLinkType.getTransferable();
		}

		return transferables;
	}
}
