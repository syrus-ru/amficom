/*
 * $Id: Map.java,v 1.3 2004/12/01 16:16:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/01 16:16:03 $
 * @author $Author: bob $
 * @module map_v1
 */
public class Map extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256722862181200184L;

	private Identifier				domainId;

	private String					name;
	private String					description;

	private List					siteNodes;
	private List					topologicalNodes;
	private List					nodeLinks;
	private List					physicalLinks;
	private List					marks;
	private List					collectors;

	private StorableObjectDatabase	mapDatabase;

	public Map(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.mapDatabase = MapDatabaseContext.getMapDatabase();
		try {
			this.mapDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Map(Map_Transferable mt) throws CreateObjectException {
		super(mt.header);
		this.name = mt.name;
		this.description = mt.description;

		try {
			this.domainId = new Identifier(mt.domain_id); 
				
			this.siteNodes = new ArrayList(mt.siteNodeIds.length);
			ArrayList siteNodeIds = new ArrayList(mt.siteNodeIds.length);
			for (int i = 0; i < mt.siteNodeIds.length; i++)
				siteNodeIds.add(new Identifier(mt.siteNodeIds[i]));
			this.siteNodes.addAll(MapStorableObjectPool.getStorableObjects(siteNodeIds, true));
			
			this.topologicalNodes = new ArrayList(mt.topologicalNodeIds.length);
			ArrayList topologicalNodeIds = new ArrayList(mt.topologicalNodeIds.length);
			for (int i = 0; i < mt.topologicalNodeIds.length; i++)
				topologicalNodeIds.add(new Identifier(mt.topologicalNodeIds[i]));
			this.topologicalNodes.addAll(MapStorableObjectPool.getStorableObjects(topologicalNodeIds, true));

			this.nodeLinks = new ArrayList(mt.nodeLinkIds.length);
			ArrayList nodeLinkIds = new ArrayList(mt.nodeLinkIds.length);
			for (int i = 0; i < mt.nodeLinkIds.length; i++)
				nodeLinkIds.add(new Identifier(mt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinkIds, true));

			this.physicalLinks = new ArrayList(mt.physicalLinkIds.length);
			ArrayList physicalNodeLinkIds = new ArrayList(mt.physicalLinkIds.length);
			for (int i = 0; i < mt.physicalLinkIds.length; i++)
				physicalNodeLinkIds.add(new Identifier(mt.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalNodeLinkIds, true));
			
			this.marks = new ArrayList(mt.markIds.length);
			ArrayList markIds = new ArrayList(mt.markIds.length);
			for (int i = 0; i < mt.markIds.length; i++)
				markIds.add(new Identifier(mt.markIds[i]));
			this.marks.addAll(MapStorableObjectPool.getStorableObjects(markIds, true));
			
			this.collectors = new ArrayList(mt.collectorIds.length);
			ArrayList collectorIds = new ArrayList(mt.collectorIds.length);
			for (int i = 0; i < mt.collectorIds.length; i++)
				collectorIds.add(new Identifier(mt.collectorIds[i]));
			this.collectors.addAll(MapStorableObjectPool.getStorableObjects(collectorIds, true));

		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Map(final Identifier id, 
				  final Identifier creatorId, 
				  final String name, 
				  final String description) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;

		this.siteNodes = new LinkedList();
		this.topologicalNodes = new LinkedList();
		this.nodeLinks = new LinkedList();
		this.physicalLinks = new LinkedList();
		this.marks = new LinkedList();
		this.collectors = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.mapDatabase = MapDatabaseContext.getMapDatabase();
	}

	public static Map getInstance(Map_Transferable mt) throws CreateObjectException {
		Map map = new Map(mt);

		map.mapDatabase = MapDatabaseContext.getMapDatabase();
		try {
			if (map.mapDatabase != null)
				map.mapDatabase.insert(map);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return map;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.siteNodes);
		dependencies.addAll(this.topologicalNodes);
		dependencies.addAll(this.nodeLinks);
		dependencies.addAll(this.physicalLinks);
		dependencies.addAll(this.marks);
		dependencies.addAll(this.collectors);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] siteNodeIds = new Identifier_Transferable[this.siteNodes.size()];
		for (Iterator iterator = this.siteNodes.iterator(); iterator.hasNext();)
			siteNodeIds[i++] = (Identifier_Transferable) ((SiteNode) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] topologicalNodeIds = new Identifier_Transferable[this.topologicalNodes.size()];
		for (Iterator iterator = this.topologicalNodes.iterator(); iterator.hasNext();)
			topologicalNodeIds[i++] = (Identifier_Transferable) ((TopologicalNode) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] nodeLinkIds = new Identifier_Transferable[this.nodeLinks.size()];
		for (Iterator iterator = this.nodeLinks.iterator(); iterator.hasNext();)
			nodeLinkIds[i++] = (Identifier_Transferable) ((NodeLink) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] physicalNodeLinkIds = new Identifier_Transferable[this.physicalLinks.size()];
		for (Iterator iterator = this.physicalLinks.iterator(); iterator.hasNext();)
			physicalNodeLinkIds[i++] = (Identifier_Transferable) ((PhysicalLink) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] markIds = new Identifier_Transferable[this.marks.size()];
		for (Iterator iterator = this.marks.iterator(); iterator.hasNext();)
			markIds[i++] = (Identifier_Transferable) ((Mark) iterator.next()).getId().getTransferable();

		i = 0;
		Identifier_Transferable[] collectorIds = new Identifier_Transferable[this.collectors.size()];
		for (Iterator iterator = this.collectors.iterator(); iterator.hasNext();)
			collectorIds[i++] = (Identifier_Transferable) ((Collector) iterator.next()).getId().getTransferable();

		return new Map_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable)this.domainId.getTransferable(),
				this.name,
				this.description,
				siteNodeIds,
				topologicalNodeIds,
				nodeLinkIds,
				physicalNodeLinkIds,
				markIds,
				collectorIds);
	}

	public List getCollectors() {
		return this.collectors;
	}
	
	public void setCollectors(List collectors) {
		this.collectors.clear();
		if (collectors != null)
			this.collectors.addAll(collectors);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public Identifier getDomainId() {
		return this.domainId;
	}
	
	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getMarks() {
		return this.marks;
	}
	
	public void setMarks(List marks) {
		this.marks.clear();
		if (marks != null)
			this.marks.addAll(marks);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getNodeLinks() {
		return this.nodeLinks;
	}
	
	public void setNodeLinks(List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getPhysicalLinks() {
		return this.physicalLinks;
	}
	
	public void setPhysicalLinks(List physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getSiteNodes() {
		return this.siteNodes;
	}
	
	public void setSiteNodes(List siteNodes) {
		this.siteNodes.clear();
		if (siteNodes != null)
			this.siteNodes.addAll(siteNodes);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getTopologicalNodes() {
		return this.topologicalNodes;
	}
	
	public void setTopologicalNodes(List topologicalNodes) {
		this.topologicalNodes.clear();
		if (topologicalNodes != null)
			this.topologicalNodes.addAll(topologicalNodes);
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description,
											  Identifier domainId) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;
			this.domainId = domainId;
	}
}
