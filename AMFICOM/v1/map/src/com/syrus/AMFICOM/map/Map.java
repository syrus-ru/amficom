/**
 * $Id: Map.java,v 1.18 2005/02/11 15:14:50 bob Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * �������������� �����, ������� �������� � ���� ����� ��������� ���� � ������
 * ����� (������� � ��������������), ����� (��������� �� ����������), ����� �� 
 * ������, ����������� (������������ � ���� �����).
 * 
 * @author $Author: bob $
 * @version $Revision: 1.18 $, $Date: 2005/02/11 15:14:50 $
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
	 * ����� ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	private static java.util.Map exportMap = null;

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
				  final long version,
				  final Identifier domainId, 
				  final String name, 
				  final String description) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.domainId = domainId;
		this.name = name;
		this.description = description;

		this.siteNodes = new LinkedList();
		this.topologicalNodes = new LinkedList();
		this.nodeLinks = new LinkedList();
		this.physicalLinks = new LinkedList();
		this.marks = new LinkedList();
		this.collectors = new LinkedList();

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
			Map map = new Map(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAP_ENTITY_CODE),
				creatorId,
				0L,
				domainId,
				name,
				description);
			map.changed = true;
			return map;
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
		this.changed = true;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		this.changed = true;
	}	
	
	public Identifier getDomainId() {
		return this.domainId;
	}
	
	protected void setDomainId0(Identifier domainId) {
		this.domainId = domainId;
	}
	
	public void setDomainId(Identifier domainId) {
		this.setDomainId0(domainId);
		this.changed = true;
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
		this.changed = true;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
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
		this.changed = true;
	}
	
	public List getPhysicalLinks() {
		return  Collections.unmodifiableList(this.physicalLinks);
	}
	
	protected void setPhysicalLinks0(List physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
		this.changed = true;
	}
	
	public void setPhysicalLinks(List physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		this.changed = true;
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
		this.changed = true;
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
		this.changed = true;
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  String name,
											  String description,
											  Identifier domainId) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version);
			this.name = name;
			this.description = description;
			this.domainId = domainId;
	}

	/**
	 * �������� ������ ���� ������� ��������� �������������� �����.
	 * @return ������ �����
	 */
	public List getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.siteNodes);
		this.nodeElements.addAll(this.topologicalNodes);
		this.nodeElements.addAll(this.marks);
		return this.nodeElements;
	}

	/**
	 * �������� ����� ����.
	 * @param node ����
	 */
	public void addNode(AbstractNode node) {
		if (node instanceof SiteNode)
			this.siteNodes.add(node);
		else if (node instanceof TopologicalNode)
			this.topologicalNodes.add(node);
		else if (node instanceof Mark)
			this.marks.add(node);
		node.setMap(this);
		node.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� ����.
	 * @param node ����
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
		this.changed = true;
	}

	/**
	 * �������� ������� �������� ���� �� ��������������.
	 * @param siteId ������������� �������� ����
	 * @return ������� ���� ��� null, ���� ���� �� ������
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
	 * �������� ������� ��������������� ���� �� ��������������.
	 * @param topologicalNodeId ������������� ��������������� ����
	 * @return �������������� ���� ��� null, ���� ���� �� ������
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
	 * �������� ����� �� ��������������.
	 * @param markId ������������� �����
	 * @return ����� ��� null, ���� ����� �� ������
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
	 * �������� ���� �� ��������������.
	 * @param nodeId ������������� �����
	 * @return ���� ��� null, ���� ���� �� ������
	 */
	public AbstractNode getNode(Identifier nodeId) {
		AbstractNode node = getSiteNode(nodeId);
		if (node == null)
			node = getTopologicalNode(nodeId);
		if (node == null)
			node = getMark(nodeId);
		return null;
	}

	/**
	 * �������� ����� ���������.
	 * @param collector ����� ���������
	 */
	public void addCollector(Collector collector) {
		this.collectors.add(collector);
		collector.setMap(this);
		collector.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� ���������.
	 * @param collector ���������
	 */
	public void removeCollector(Collector collector) {
		collector.setSelected(false);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		this.changed = true;
	}

	/**
	 * �������� ���������, � ������� �������� ���� �������� �����.
	 * @param physicalLink �����
	 * @return ���������
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
	 * �������� ������ �����, ������������ ��� ��������������� � �������� ����.
	 * 
	 * @param node ����
	 * @return ������ �����
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
	 * �������� ����� �����.
	 * @param physicalLink �����
	 */
	public void addPhysicalLink(PhysicalLink physicalLink) {

		this.physicalLinks.add(physicalLink);
		physicalLink.setMap(this);
		physicalLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� �����.
	 * @param physicalLink �����
	 */
	public void removePhysicalLink(PhysicalLink physicalLink) {

		physicalLink.setSelected(false);
		this.physicalLinks.remove(physicalLink);
		physicalLink.setRemoved(true);

		Collector coll = getCollector(physicalLink);
		if (coll != null)
			coll.removePhysicalLink(physicalLink);
		this.changed = true;
	}

	/**
	 * �������� ����� �� �� ��������������.
	 * @param phisicalLinkId ������������� �����
	 * @return �����
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
	 * �������� ����� �� �������� �����.
	 * @param startNode ���� �������� ����
	 * @param endNode ������ �������� ����
	 * @return �����
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
	 * �������� ����� �������� �����.
	 * @param nodeLink �������� �����
	 */
	public void addNodeLink(NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
		nodeLink.setMap(this);
		nodeLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� �������� �����.
	 * @param nodeLink �������� �����
	 */
	public void removeNodeLink(NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		this.changed = true;
	}

	/**
	 * �������� �������� ����� �� ��������������.
	 * @param nodeLinkId ������������� ��������� �����
	 * @return �������� �����
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
	 * �������� �������� ����� �� �������� �����.
	 * @param startNode ���� �������� ����
	 * @param endNode ������ �������� ����
	 * @return �������� �����
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
	 * �������� ������ ���� �������������� ��������� ����� ({@link MapElement}).
	 * @return ������ ���� ���������
	 */
	public List getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.marks);
		this.allElements.addAll(this.topologicalNodes);
		this.allElements.addAll(this.siteNodes);

		this.allElements.addAll(this.physicalLinks);
		this.allElements.addAll(this.nodeLinks);
		this.allElements.addAll(this.collectors);

		return Collections.unmodifiableList(this.allElements);
	}

	/**
	 * �������� ����� ���� ���������� ��������� �������������� �����. 
	 * ���������� �������� - ��, ��� �������  ����� 
	 * <code>{@link MapElement#isSelected()}</code> ���������� <code>true</code>.
	 * @return ����� ���������� ���������
	 */
	public Set getSelectedElements() {
		return this.selectedElements;
	}

	/**
	 * �������� ����� ���������� ��������� �������������� �����.
	 */
	public void clearSelection() {
		this.selectedElements.clear();
	}

	/**
	 * ���������������� ��������� ����� ��������� �������� �������������� �����.
	 * @param me �������
	 * @param selected ���� ���������
	 */
	public void setSelected(MapElement me, boolean selected) {
		if (selected)
			this.selectedElements.add(me);
		else
			this.selectedElements.remove(me);
	}

	/**
	 * ���������� ����������� ������� ����� ����������,
	 * ������� ������������ ��� ��������.
	 * @return ���-������� ���������� ��������
	 */
	public java.util.Map getExportMap() {
		if(exportMap == null)
			exportMap = new HashMap();		
		synchronized(exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			return Collections.unmodifiableMap(exportMap);
		}		
	}

	public static Map createInstance(
			Identifier creatorId,
			Identifier domainId,
			java.util.Map exportMap) 
		throws CreateObjectException 
	{
		Identifier id = (Identifier) exportMap.get(COLUMN_ID);
		String name = (String) exportMap.get(COLUMN_NAME);
		String description = (String) exportMap.get(COLUMN_DESCRIPTION);

		if (id == null || name == null || description == null
			|| creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try 
		{
			return new Map(
				id,
				creatorId,
				0L,
				domainId,
				name,
				description);
		} catch (Exception e) 
		{
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}
}

