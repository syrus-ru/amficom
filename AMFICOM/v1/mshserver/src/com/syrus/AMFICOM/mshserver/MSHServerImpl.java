/*
 * $Id: MSHServerImpl.java,v 1.3 2005/02/21 13:02:38 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.CollectorDatabase;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapDatabase;
import com.syrus.AMFICOM.map.MapDatabaseContext;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.MarkDatabase;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.NodeLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLinkTypeDatabase;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeDatabase;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNodeTypeDatabase;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.TopologicalNodeDatabase;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServerOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/21 13:02:38 $
 * @author $Author: bob $
 * @module mshserver_1
 */
public class MSHServerImpl implements MSHServerOperations {

	// ////////////////////////////////Name
	// Resolver/////////////////////////////////////////////////

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long								serialVersionUID		= 3762810480783274295L;


	public String lookupDomainName(Identifier_Transferable idTransferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(idTransferable);
			return ( (Domain)AdministrationStorableObjectPool.getStorableObject(id, true) ).getName();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public String lookupUserLogin(Identifier_Transferable identifier_Transferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(identifier_Transferable);
			return ( (User)AdministrationStorableObjectPool.getStorableObject(id, true) ).getLogin();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public String lookupUserName(Identifier_Transferable identifier_Transferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(identifier_Transferable);
			return ( (User)AdministrationStorableObjectPool.getStorableObject(id, true) ).getName();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupDomainName(String domainName) throws AMFICOMRemoteException {
		try {
			TypicalCondition condition = new TypicalCondition(domainName, OperationSort.OPERATION_EQUALS,
																ObjectEntities.DOMAIN_ENTITY_CODE,
																StorableObjectWrapper.COLUMN_NAME);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (collection.isEmpty())
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
													"list is empty");
			Identifier id = ((Domain) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupUserLogin(String userLogin) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.reverseLookupUserLogin | userLogin " + userLogin, Log.DEBUGLEVEL07);
			TypicalCondition condition = new TypicalCondition(userLogin, OperationSort.OPERATION_EQUALS,
																ObjectEntities.USER_ENTITY_CODE,
																UserWrapper.COLUMN_LOGIN);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			Identifier id = ((User) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupUserName(final String userName)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.reverseLookupUserName | userName = " + userName, Log.DEBUGLEVEL07);
		try {
			TypicalCondition condition = new TypicalCondition(userName, OperationSort.OPERATION_EQUALS,
																ObjectEntities.USER_ENTITY_CODE,
																StorableObjectWrapper.COLUMN_NAME);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			Identifier id = ((User) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
				.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	// /////////////////////////////////////// Identifier Generator
	// ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("MSHServerImpl.getGeneratedIdentifier | generate new Identifer for "
					+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO,
												"Illegal object entity: '" + ObjectEntities.codeToString(entityCode)
														+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
												"Cannot create major/minor entries of identifier for entity: '"
														+ ObjectEntities.codeToString(entityCode) + "' -- "
														+ ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.getGeneratedIdentifierRange | generate new Identifer range " + size + " for "
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
												"Illegal object entity: '" + ObjectEntities.codeToString(entityCode)
														+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
												"Cannot create major/minor entries of identifier for entity: '"
														+ ObjectEntities.codeToString(entityCode) + "' -- "
														+ ige.getMessage());
		}
	}

	public void delete(Identifier_Transferable idTransferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.delete | trying to delete... ", Log.DEBUGLEVEL03);
		Identifier id = new Identifier(idTransferable);
		short entityCode = id.getMajor();
		if (ObjectGroupEntities.isInMapGroup(entityCode))
			MapStorableObjectPool.delete(id);
			Log.errorMessage("MSHServerImpl.delete | Wrong entity code: " + entityCode);
	}

	public void deleteList(Identifier_Transferable[] idTransferables, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.deleteList | Trying to delete... ", Log.DEBUGLEVEL03);
		List idList = new ArrayList(idTransferables.length);
		List mapList = new ArrayList(idTransferables.length);
		for (int i = 0; i < idTransferables.length; i++) {
			idList.add(new Identifier(idTransferables[i]));
		}
		for (Iterator iter = idList.iterator(); iter.hasNext();) {
			Identifier id = (Identifier) iter.next();
			short entityCode = id.getMajor();
			if (ObjectGroupEntities.isInMapGroup(entityCode))
				mapList.add(id);
			Log.errorMessage("MSHServerImpl.deleteList | Wrong entity code: " + entityCode);
		}
		try {
			MapStorableObjectPool.delete(mapList);
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} 
	}

	private StorableObject_Transferable receiveStorableObject(StorableObject storableObject, StorableObjectDatabase database, AccessIdentifier_Transferable accessIdentifier, 
	                                   boolean force)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveStorableObject | Received siteNode", Log.DEBUGLEVEL07);
		try {
			MapStorableObjectPool.putStorableObject(storableObject);
			database.update(storableObject, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return storableObject.getHeaderTransferable();

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	protected StorableObject_Transferable[] getListHeaders(List storableObjects) {
		StorableObject_Transferable[] headers = new StorableObject_Transferable[storableObjects.size()];
		int i=0;
		for (Iterator it = storableObjects.iterator(); it.hasNext();i++) 
			headers[i] = ((StorableObject) it.next()).getHeaderTransferable();
		return headers;
	}

	public StorableObject_Transferable receiveSiteNode(SiteNode_Transferable siteNodeTransferable,
								boolean force,
								AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveSiteNode | Received siteNode", Log.DEBUGLEVEL07);
		SiteNode siteNode;
		try {
			siteNode = new SiteNode(siteNodeTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		return this.receiveStorableObject(siteNode, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveTopologicalNode(	TopologicalNode_Transferable topologicalNodeTransferable,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveTopologicalNode | Received topologicalNode", Log.DEBUGLEVEL07);

		TopologicalNode topologicalNode;
		try {
			topologicalNode = new TopologicalNode(topologicalNodeTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		return this.receiveStorableObject(topologicalNode, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveNodeLink(NodeLink_Transferable nodeLinkTransferable,
								boolean force,
								AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveNodeLink | Received nodeLink", Log.DEBUGLEVEL07);
		NodeLink nodeLink;
		try {
			nodeLink = new NodeLink(nodeLinkTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		return this.receiveStorableObject(nodeLink, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveMark(Mark_Transferable markTransferable,
							boolean force,
							AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveMark | Received mark", Log.DEBUGLEVEL07);
		Mark mark;
		try {
			mark = new Mark(markTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		return this.receiveStorableObject(mark, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receivePhysicalLink(PhysicalLink_Transferable physicalLinkTransferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receivePhysicalLink | Received physicalLink", Log.DEBUGLEVEL07);
		PhysicalLink physicalLink;
		try {
			physicalLink = new PhysicalLink(physicalLinkTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		return this.receiveStorableObject(physicalLink, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveCollector(	Collector_Transferable collectorTransferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveCollector | Received collector", Log.DEBUGLEVEL07);
		Collector collector;
		try {
			collector = new Collector(collectorTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		return this.receiveStorableObject(collector, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveMap(	Map_Transferable mapTransferable,
							boolean force,
							AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveMap | Received map", Log.DEBUGLEVEL07);

		Map map;
		try {
			map = new Map(mapTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		return this.receiveStorableObject(map, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receiveSiteNodeType(SiteNodeType_Transferable siteNodeTypeTransferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveSiteNodeType | Received siteNodeType", Log.DEBUGLEVEL07);
		SiteNodeType siteNodeType;
		try {
			siteNodeType = new SiteNodeType(siteNodeTypeTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		return this.receiveStorableObject(siteNodeType, database, accessIdentifier, force);
	}

	public StorableObject_Transferable receivePhysicalLinkType(PhysicalLinkType_Transferable physicalLinkTypeTransferable,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receivePhysicalLinkType | Received physicalLinkType", Log.DEBUGLEVEL07);
		PhysicalLinkType physicalLinkType;
		try {
			physicalLinkTypeTransferable.header.modifier_id = accessIdentifier.user_id;
			physicalLinkType = new PhysicalLinkType(physicalLinkTypeTransferable);
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		return this.receiveStorableObject(physicalLinkType, database, accessIdentifier, force);
	}

	public StorableObject_Transferable[] receiveSiteNodes(	SiteNode_Transferable[] siteNodeTransferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveSiteNodes | Received " + siteNodeTransferables.length + " site node(s)",
			Log.DEBUGLEVEL07);
		List siteNodeList = new ArrayList(siteNodeTransferables.length);
		try {

			for (int i = 0; i < siteNodeTransferables.length; i++) {
				SiteNode siteNode = new SiteNode(siteNodeTransferables[i]);
				MapStorableObjectPool.putStorableObject(siteNode);
				siteNodeList.add(siteNode);
			}

			SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
			database.update(siteNodeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			
			return this.getListHeaders(siteNodeList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTopologicalNodes(TopologicalNode_Transferable[] topologicalNodeTransferables,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveTopologicalNodes | Received " + topologicalNodeTransferables.length
				+ " topological node(s)", Log.DEBUGLEVEL07);
		List siteNodeList = new ArrayList(topologicalNodeTransferables.length);
		try {

			for (int i = 0; i < topologicalNodeTransferables.length; i++) {
				TopologicalNode topologicalNode = new TopologicalNode(topologicalNodeTransferables[i]);
				MapStorableObjectPool.putStorableObject(topologicalNode);
				siteNodeList.add(topologicalNode);
			}

			TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext
					.getTopologicalNodeDatabase();
			database.update(siteNodeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			
			return this.getListHeaders(siteNodeList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveNodeLinks(	NodeLink_Transferable[] nodeLinkTransferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveNodeLinks | Received " + nodeLinkTransferables.length + " node link(s)",
			Log.DEBUGLEVEL07);
		List nodeLinkList = new ArrayList(nodeLinkTransferables.length);
		try {

			for (int i = 0; i < nodeLinkTransferables.length; i++) {
				NodeLink nodeLink = new NodeLink(nodeLinkTransferables[i]);
				MapStorableObjectPool.putStorableObject(nodeLink);
				nodeLinkList.add(nodeLink);
			}

			NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
			database.update(nodeLinkList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(nodeLinkList);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMarks(	Mark_Transferable[] markTransferables,
								boolean force,
								AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveMarks | Received " + markTransferables.length + " mark(s)",
			Log.DEBUGLEVEL07);
		List markList = new ArrayList(markTransferables.length);
		try {

			for (int i = 0; i < markTransferables.length; i++) {
				Mark mark = new Mark(markTransferables[i]);
				MapStorableObjectPool.putStorableObject(mark);
				markList.add(mark);
			}

			MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
			database.update(markList,
				new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
						: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(markList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePhysicalLinks(	PhysicalLink_Transferable[] physicalLinkTransferables,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receivePhysicalLinks | Received " + physicalLinkTransferables.length
				+ " physical link(s)", Log.DEBUGLEVEL07);
		List physicalLinkList = new ArrayList(physicalLinkTransferables.length);
		try {

			for (int i = 0; i < physicalLinkTransferables.length; i++) {
				PhysicalLink physicalLink = new PhysicalLink(physicalLinkTransferables[i]);
				MapStorableObjectPool.putStorableObject(physicalLink);
				physicalLinkList.add(physicalLink);
			}

			PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
			database.update(physicalLinkList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(physicalLinkList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCollectors(	Collector_Transferable[] collectorTransferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveCollectors | Received " + collectorTransferables.length
				+ " collector(s)", Log.DEBUGLEVEL07);
		List collectorList = new ArrayList(collectorTransferables.length);
		try {

			for (int i = 0; i < collectorTransferables.length; i++) {
				Collector collector = new Collector(collectorTransferables[i]);
				MapStorableObjectPool.putStorableObject(collector);
				collectorList.add(collector);
			}

			CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
			database.update(collectorList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(collectorList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMaps(Map_Transferable[] mapTransferables,
							boolean force,
							AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveMaps | Received " + mapTransferables.length + " map(s)",
			Log.DEBUGLEVEL07);
		List mapList = new ArrayList(mapTransferables.length);
		try {

			for (int i = 0; i < mapTransferables.length; i++) {
				Map map = new Map(mapTransferables[i]);
				MapStorableObjectPool.putStorableObject(map);
				mapList.add(map);
			}

			MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
			database.update(mapList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(mapList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveSiteNodeTypes(	SiteNodeType_Transferable[] siteNodeTypeTransferables,
										boolean force,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receiveSiteNodeTypes | Received " + siteNodeTypeTransferables.length
				+ " site node type(s)", Log.DEBUGLEVEL07);
		List siteNodeTypeList = new ArrayList(siteNodeTypeTransferables.length);
		try {

			for (int i = 0; i < siteNodeTypeTransferables.length; i++) {
				SiteNodeType siteNodeType = new SiteNodeType(siteNodeTypeTransferables[i]);
				MapStorableObjectPool.putStorableObject(siteNodeType);
				siteNodeTypeList.add(siteNodeType);
			}

			SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
			database.update(siteNodeTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);

			return this.getListHeaders(siteNodeTypeList);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receivePhysicalLinkTypes(	PhysicalLinkType_Transferable[] physicalLinkTypeTransferables,
											boolean force,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("MSHServerImpl.receivePhysicalLinkTypes | Received " + physicalLinkTypeTransferables.length
				+ " physical link type(s)", Log.DEBUGLEVEL07);
		List physicalLinkTypeList = new ArrayList(physicalLinkTypeTransferables.length);
		try {

			for (int i = 0; i < physicalLinkTypeTransferables.length; i++) {
				physicalLinkTypeTransferables[i].header.modifier_id = accessIdentifier.user_id;
				PhysicalLinkType physicalLinkType = new PhysicalLinkType(physicalLinkTypeTransferables[i]);
				MapStorableObjectPool.putStorableObject(physicalLinkType);
				physicalLinkTypeList.add(physicalLinkType);
			}

			PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext
					.getPhysicalLinkTypeDatabase();

			database.update(physicalLinkTypeList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return this.getListHeaders(physicalLinkTypeList);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	private Object transmitObject(Identifier_Transferable idTransferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(idTransferable);
		Log.debugMessage("MSHServerImpl.transmitObject | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			StorableObject storableObject = MapStorableObjectPool.getStorableObject(id, true);
			return storableObject.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public SiteNode_Transferable transmitSiteNode(	Identifier_Transferable idTransferable,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (SiteNode_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public TopologicalNode_Transferable transmitTopologicalNode(Identifier_Transferable id_Transferable,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (TopologicalNode_Transferable) this.transmitObject(id_Transferable, accessIdentifier);
	}

	public NodeLink_Transferable transmitNodeLink(	Identifier_Transferable idTransferable,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (NodeLink_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public Mark_Transferable transmitMark(	Identifier_Transferable idTransferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (Mark_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public PhysicalLink_Transferable transmitPhysicalLink(	Identifier_Transferable idTransferable,
															AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (PhysicalLink_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public Collector_Transferable transmitCollector(Identifier_Transferable idTransferable,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (Collector_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public Map_Transferable transmitMap(Identifier_Transferable idTransferable,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		return (Map_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public SiteNodeType_Transferable transmitSiteNodeType(	Identifier_Transferable idTransferable,
															AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (SiteNodeType_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	public PhysicalLinkType_Transferable transmitPhysicalLinkType(	Identifier_Transferable idTransferable,
																	AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		return (PhysicalLinkType_Transferable) this.transmitObject(idTransferable, accessIdentifier);
	}

	private Collection transmitObjects(Identifier_Transferable[] ids_Transferable, StorableObjectCondition condition)
			throws AMFICOMRemoteException {
		try {
			Collection collection = null;
			if (ids_Transferable.length > 0) {
				Collection idsList = new ArrayList(ids_Transferable.length);
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

		Log.debugMessage("MSHServerImpl.transmitSiteNodes | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitTopologicalNodes | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitNodeLinks | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitMarks | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitPhysicalLinks | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitCollectors | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitMaps | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitSiteNodeTypes | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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

		Log.debugMessage("MSHServerImpl.transmitPhysicalLinkTypes | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
				+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

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
