/*
 * $Id: Map.java,v 1.10 2005/01/18 06:22:21 bob Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ.
 * рТПЕЛФ: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.10 $, $Date: 2005/01/18 06:22:21 $
 * @author $Author: bob $
 * @module map_v1
 */
public class Map extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256722862181200184L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CREATOR_ID = "creatorId";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_MODIFIER_ID = "modifierId";
	public static final String COLUMN_MODIFIED = "modified";

	/** 
	 * массив параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	public static Object[][] exportColumns = null;

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

	protected transient Set selectedElements = new HashSet();

	protected transient List allElements = new LinkedList();
	protected transient List nodeElements = new LinkedList();

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
			
			for(Iterator it = this.siteNodes.iterator(); it.hasNext();)
			{
				((SiteNode )it.next()).setMap(this);
			}
			
			this.topologicalNodes = new ArrayList(mt.topologicalNodeIds.length);
			ArrayList topologicalNodeIds = new ArrayList(mt.topologicalNodeIds.length);
			for (int i = 0; i < mt.topologicalNodeIds.length; i++)
				topologicalNodeIds.add(new Identifier(mt.topologicalNodeIds[i]));
			this.topologicalNodes.addAll(MapStorableObjectPool.getStorableObjects(topologicalNodeIds, true));

			for(Iterator it = this.topologicalNodes.iterator(); it.hasNext();)
			{
				((TopologicalNode )it.next()).setMap(this);
			}

			this.nodeLinks = new ArrayList(mt.nodeLinkIds.length);
			ArrayList nodeLinkIds = new ArrayList(mt.nodeLinkIds.length);
			for (int i = 0; i < mt.nodeLinkIds.length; i++)
				nodeLinkIds.add(new Identifier(mt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinkIds, true));

			for(Iterator it = this.nodeLinks.iterator(); it.hasNext();)
			{
				((NodeLink )it.next()).setMap(this);
			}

			this.physicalLinks = new ArrayList(mt.physicalLinkIds.length);
			ArrayList physicalNodeLinkIds = new ArrayList(mt.physicalLinkIds.length);
			for (int i = 0; i < mt.physicalLinkIds.length; i++)
				physicalNodeLinkIds.add(new Identifier(mt.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalNodeLinkIds, true));
			
			for(Iterator it = this.physicalLinks.iterator(); it.hasNext();)
			{
				((PhysicalLink )it.next()).setMap(this);
			}

			this.marks = new ArrayList(mt.markIds.length);
			ArrayList markIds = new ArrayList(mt.markIds.length);
			for (int i = 0; i < mt.markIds.length; i++)
				markIds.add(new Identifier(mt.markIds[i]));
			this.marks.addAll(MapStorableObjectPool.getStorableObjects(markIds, true));
			
			for(Iterator it = this.marks.iterator(); it.hasNext();)
			{
				((Mark )it.next()).setMap(this);
			}

			this.collectors = new ArrayList(mt.collectorIds.length);
			ArrayList collectorIds = new ArrayList(mt.collectorIds.length);
			for (int i = 0; i < mt.collectorIds.length; i++)
				collectorIds.add(new Identifier(mt.collectorIds[i]));
			this.collectors.addAll(MapStorableObjectPool.getStorableObjects(collectorIds, true));

			for(Iterator it = this.collectors.iterator(); it.hasNext();)
			{
				((Collector )it.next()).setMap(this);
			}

		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Map(final Identifier id, 
				  final Identifier creatorId, 
				  final Identifier domainId, 
				  final String name, 
				  final String description) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.domainId = domainId;
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

	
	public void insert() throws CreateObjectException {
		this.mapDatabase = MapDatabaseContext.getMapDatabase();
		try {
			if (this.mapDatabase != null)
				this.mapDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static Map createInstance(
			Identifier creatorId,
			Identifier domainId,
			String name,
			String description) 
		throws CreateObjectException 
	{
		if (name == null || description == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try 
		{
			return new Map(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAP_ENTITY_CODE),
				creatorId,
				domainId,
				name,
				description);
		} catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("Map.createInstance | cannot generate identifier ", e);
		}
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
		return  Collections.unmodifiableList(this.collectors);
	}
	
	protected void setCollectors0(List collectors) {
		this.collectors.clear();
		if (collectors != null)
			this.collectors.addAll(collectors);
	}
	
	public void setCollectors(List collectors) {
		this.setCollectors0(collectors);
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
		return  Collections.unmodifiableList(this.marks);
	}
	
	protected void setMarks0(List marks) {
		this.marks.clear();
		if (marks != null)
			this.marks.addAll(marks);
	}
	
	public void setMarks(List marks) {
		this.setMarks0(marks);
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
		return  Collections.unmodifiableList(this.nodeLinks);
	}
	
	protected void setNodeLinks0(List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
	}
	
	public void setNodeLinks(List nodeLinks) {
		this.setNodeLinks0(nodeLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getPhysicalLinks() {
		return  Collections.unmodifiableList(this.physicalLinks);
	}
	
	protected void setPhysicalLinks0(List physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public void setPhysicalLinks(List physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getSiteNodes() {
		return  Collections.unmodifiableList(this.siteNodes);
	}
	
	protected void setSiteNodes0(List siteNodes) {
		this.siteNodes.clear();
		if (siteNodes != null)
			this.siteNodes.addAll(siteNodes);
	}
	
	public void setSiteNodes(List siteNodes) {
		this.setSiteNodes0(siteNodes);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getTopologicalNodes() {
		return  Collections.unmodifiableList(this.topologicalNodes);
	}
	
	protected void setTopologicalNodes0(List topologicalNodes) {
		this.topologicalNodes.clear();
		if (topologicalNodes != null)
			this.topologicalNodes.addAll(topologicalNodes);
	}
	
	public void setTopologicalNodes(List topologicalNodes) {
		this.setTopologicalNodes0(topologicalNodes);
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

	public List getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.siteNodes);
		this.nodeElements.addAll(this.topologicalNodes);
		this.nodeElements.addAll(this.marks);
		return this.nodeElements;
	}

	/**
	 * Добавить новый MapNodeElement
	 */
	public void addNode(AbstractNode ob) {
		if (ob instanceof SiteNode)
			this.siteNodes.add(ob);
		else if (ob instanceof TopologicalNode)
			this.topologicalNodes.add(ob);
		else if (ob instanceof Mark)
			this.marks.add(ob);
		ob.setMap(this);
		ob.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить MapNodeElement
	 */
	public void removeNode(AbstractNode node) {
		node.setSelected(false);
		if (node instanceof SiteNode)
			this.siteNodes.remove(node);
		else if (node instanceof TopologicalNode)
			this.topologicalNodes.remove(node);
		else if (node instanceof Mark)
			this.marks.remove(node);
		node.setRemoved(true);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получить элемент сетевого узла по ID
	 */
	public SiteNode getSiteNode(Identifier siteId) {
		Iterator e = this.getSiteNodes().iterator();
		while (e.hasNext()) {
			SiteNode msne = (SiteNode) e.next();

			if (msne.getId().equals(siteId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить элемент сетевого узла по ID
	 */
	public TopologicalNode getTopologicalNode(Identifier topologicalNodeId) {
		Iterator e = this.getTopologicalNodes().iterator();
		while (e.hasNext()) {
			TopologicalNode msne = (TopologicalNode) e.next();

			if (msne.getId().equals(topologicalNodeId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить элемент сетевого узла по ID
	 */
	public Mark getMark(Identifier markId) {
		Iterator e = this.getMarks().iterator();
		while (e.hasNext()) {
			Mark msne = (Mark) e.next();

			if (msne.getId().equals(markId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить точечный объект по ID
	 */
	public AbstractNode getNode(Identifier nodeId) {
		AbstractNode node = getSiteNode(nodeId);
		if (node == null)
			node = getTopologicalNode(nodeId);
		if (node == null)
			node = getMark(nodeId);
		return null;
	}

	public void addCollector(Collector collector) {
		this.collectors.add(collector);
		collector.setMap(this);
		collector.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCollector(Collector collector) {
		collector.setSelected(false);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * получить коллектор, в составе которого есть тоннель mple
	 */
	public Collector getCollector(PhysicalLink physicalLink) {
		for (Iterator it = this.getCollectors().iterator(); it.hasNext();) {
			Collector cp = (Collector) it.next();
			if (cp.getPhysicalLinks().contains(physicalLink))
				return cp;
		}
		return null;
	}

	/**
	 * Получить список физических линий, начинающихся или заканчивающихся в узле
	 * node
	 */
	public List getPhysicalLinksAt(AbstractNode node) {
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = this.getPhysicalLinks().iterator();

		while (e.hasNext()) {
			PhysicalLink link = (PhysicalLink) e.next();
			if ((link.getEndNode().equals(node)) || (link.getStartNode().equals(node)))
				returnNodeLink.add(link);
		}
		return returnNodeLink;
	}

	/**
	 * Добавить новую физическую линию
	 */
	public void addPhysicalLink(PhysicalLink physicalLink) {

		this.physicalLinks.add(physicalLink);
		physicalLink.setMap(this);
		physicalLink.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить физическую линию
	 */
	public void removePhysicalLink(PhysicalLink physicalLink) {

		physicalLink.setSelected(false);
		this.physicalLinks.remove(physicalLink);
		physicalLink.setRemoved(true);

		Collector coll = getCollector(physicalLink);
		if (coll != null)
			coll.removePhysicalLink(physicalLink);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получение MapPhysicalLinkElement по его ID
	 */
	public PhysicalLink getPhysicalLink(Identifier phisicalLinkId) {
		Iterator e = this.getPhysicalLinks().iterator();

		while (e.hasNext()) {
			PhysicalLink physicalLink = (PhysicalLink) e.next();
			if (physicalLink.getId().equals(phisicalLinkId))
				return physicalLink;
		}
		return null;
	}

	/**
	 * Получить MapPhysicalLinkElement по начальному и конечному узлам
	 */
	public PhysicalLink getPhysicalLink(AbstractNode startNode, AbstractNode endNode) {
		for (Iterator it = this.getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink) it.next();
			if (((link.getStartNode().equals(startNode)) && (link.getEndNode().equals(endNode)))
					|| ((link.getStartNode().equals(endNode)) && (link.getEndNode().equals(startNode)))) { return link; }
		}
		return null;
	}

	/**
	 * добавить новый MapNodeLinkElement
	 */
	public void addNodeLink(NodeLink ob) {
		this.nodeLinks.add(ob);
		ob.setMap(this);
		ob.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить MapNodeLinkElement
	 */
	public void removeNodeLink(NodeLink ob) {
		ob.setSelected(false);
		this.nodeLinks.remove(ob);
		ob.setRemoved(true);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получение MapNodeLinkElement по его ID
	 */
	public NodeLink getNodeLink(Identifier nodeLinkId) {
		Iterator e = this.getNodeLinks().iterator();

		while (e.hasNext()) {
			NodeLink nodeLink = (NodeLink) e.next();
			if (nodeLink.getId().equals(nodeLinkId)) { return nodeLink; }
		}
		return null;
	}

	/**
	 * Получить NodeLink по начальному и конечному узлам
	 */
	public NodeLink getNodeLink(AbstractNode startNode, AbstractNode endNode) {
		for (Iterator it = this.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink link = (NodeLink) it.next();
			if (((link.getStartNode().equals(startNode)) && (link.getEndNode().equals(endNode)))
					|| ((link.getStartNode().equals(endNode)) && (link.getEndNode().equals(startNode)))) { return link; }
		}
		return null;
	}

	/**
	 * Получить список всех топологических элементов карты
	 */
	public List getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.marks);
		this.allElements.addAll(this.topologicalNodes);
		this.allElements.addAll(this.siteNodes);

		this.allElements.addAll(this.nodeLinks);
		this.allElements.addAll(this.physicalLinks);
		this.allElements.addAll(this.collectors);

		return Collections.unmodifiableList(this.allElements);
	}

	public Set getSelectedElements() {
		return this.selectedElements;
	}

	public void clearSelection() {
		this.selectedElements.clear();
	}

	public void setSelected(MapElement me, boolean selected) {
		if (selected)
			this.selectedElements.add(me);
		else
			this.selectedElements.remove(me);
	}

	public Object[][] exportColumns() {
		if (exportColumns == null) {
			exportColumns = new Object[3][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();

		return exportColumns;
	}

	public static Map createInstance(
			Identifier creatorId,
			Identifier domainId,
			Object[][] exportColumns)
		throws CreateObjectException 
	{
		Identifier id = null;
		String name = null;
		String description = null;

		Object field;
		Object value;

		if (creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		for(int i = 0; i < exportColumns.length; i++)
		{
			field = exportColumns[i][0];
			value = exportColumns[i][1];

			if(field.equals(COLUMN_ID))
				id = (Identifier )value;
			else
			if(field.equals(COLUMN_NAME))
				name = (String )value;
			else
			if(field.equals(COLUMN_DESCRIPTION))
				description = (String )value;
		}

		if (id == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try 
		{
			return new Map(
				id,
				creatorId,
				domainId,
				name,
				description);
		} catch (Exception e) 
		{
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}

}

