/*-
 * $Id: Map.java,v 1.30 2005/04/08 09:24:33 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * Топологическая схема, которая содержит в себе набор связанных друг с другом
 * узлов (сетевых и топологических), линий (состоящих из фрагментов), меток на 
 * линиях, коллекторов (объединяющих в себе линии).
 * 
 * @author $Author: bass $
 * @version $Revision: 1.30 $, $Date: 2005/04/08 09:24:33 $
 * @module map_v1
 * @todo make maps persistent 
 */
public class Map extends DomainMember {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722862181200184L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CREATOR_ID = "creatorId";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_MODIFIER_ID = "modifierId";
	public static final String COLUMN_MODIFIED = "modified";

	/**
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private String name;
	private String description;

	private Set siteNodes;
	private Set topologicalNodes;
	private Set nodeLinks;
	private Set physicalLinks;
	private Set marks;
	private Set collectors;

	protected transient Set maps;
	protected transient Set selectedElements;
	protected transient Set allElements;
	protected transient Set nodeElements;

	Map(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		MapDatabase database = MapDatabaseContext.getMapDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	Map(Map_Transferable mt) throws CreateObjectException {
		this.fromTransferable(mt);
	}

	Map(final Identifier id, 
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
			version,
			domainId);
		this.name = name;
		this.description = description;

		this.siteNodes = new HashSet();
		this.topologicalNodes = new HashSet();
		this.nodeLinks = new HashSet();
		this.physicalLinks = new HashSet();
		this.marks = new HashSet();
		this.collectors = new HashSet();

		this.maps = new HashSet();
		this.selectedElements = new HashSet();
		this.allElements = new HashSet();
		this.nodeElements = new HashSet();
	}

	public static Map createInstance(Identifier creatorId, Identifier domainId, String name, String description)
			throws CreateObjectException {
		if (name == null || description == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Map map = new Map(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAP_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description);
			map.changed = true;
			return map;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Map.createInstance | cannot generate identifier ", e);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		Map_Transferable mt = (Map_Transferable) transferable;
		super.fromTransferable(mt.header, new Identifier(mt.domain_id));

		this.name = mt.name;
		this.description = mt.description;

		try {
			this.siteNodes = new HashSet(mt.siteNodeIds.length);
			HashSet siteNodeIds = new HashSet(mt.siteNodeIds.length);
			for (int i = 0; i < mt.siteNodeIds.length; i++)
				siteNodeIds.add(new Identifier(mt.siteNodeIds[i]));
			this.siteNodes.addAll(MapStorableObjectPool.getStorableObjects(siteNodeIds, true));

			for (Iterator it = this.siteNodes.iterator(); it.hasNext();) {
				((SiteNode) it.next()).setMap(this);
			}

			this.topologicalNodes = new HashSet(mt.topologicalNodeIds.length);
			HashSet topologicalNodeIds = new HashSet(mt.topologicalNodeIds.length);
			for (int i = 0; i < mt.topologicalNodeIds.length; i++)
				topologicalNodeIds.add(new Identifier(mt.topologicalNodeIds[i]));
			this.topologicalNodes.addAll(MapStorableObjectPool.getStorableObjects(topologicalNodeIds, true));

			for (Iterator it = this.topologicalNodes.iterator(); it.hasNext();) {
				((TopologicalNode) it.next()).setMap(this);
			}

			this.nodeLinks = new HashSet(mt.nodeLinkIds.length);
			HashSet nodeLinkIds = new HashSet(mt.nodeLinkIds.length);
			for (int i = 0; i < mt.nodeLinkIds.length; i++)
				nodeLinkIds.add(new Identifier(mt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinkIds, true));

			for (Iterator it = this.nodeLinks.iterator(); it.hasNext();) {
				((NodeLink) it.next()).setMap(this);
			}

			this.physicalLinks = new HashSet(mt.physicalLinkIds.length);
			HashSet physicalNodeLinkIds = new HashSet(mt.physicalLinkIds.length);
			for (int i = 0; i < mt.physicalLinkIds.length; i++)
				physicalNodeLinkIds.add(new Identifier(mt.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalNodeLinkIds, true));

			for (Iterator it = this.physicalLinks.iterator(); it.hasNext();) {
				((PhysicalLink) it.next()).setMap(this);
			}

			this.marks = new HashSet(mt.markIds.length);
			HashSet markIds = new HashSet(mt.markIds.length);
			for (int i = 0; i < mt.markIds.length; i++)
				markIds.add(new Identifier(mt.markIds[i]));
			this.marks.addAll(MapStorableObjectPool.getStorableObjects(markIds, true));

			for (Iterator it = this.marks.iterator(); it.hasNext();) {
				((Mark) it.next()).setMap(this);
			}

			this.collectors = new HashSet(mt.collectorIds.length);
			HashSet collectorIds = new HashSet(mt.collectorIds.length);
			for (int i = 0; i < mt.collectorIds.length; i++)
				collectorIds.add(new Identifier(mt.collectorIds[i]));
			this.collectors.addAll(MapStorableObjectPool.getStorableObjects(collectorIds, true));

			for (Iterator it = this.collectors.iterator(); it.hasNext();) {
				((Collector) it.next()).setMap(this);
			}

		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.siteNodes);
		dependencies.addAll(this.topologicalNodes);
		dependencies.addAll(this.nodeLinks);
		dependencies.addAll(this.physicalLinks);
		dependencies.addAll(this.marks);
		dependencies.addAll(this.collectors);
		return dependencies;
	}

	public IDLEntity getTransferable() {
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
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				this.description,
				siteNodeIds,
				topologicalNodeIds,
				nodeLinkIds,
				physicalNodeLinkIds,
				markIds,
				collectorIds);
	}

	public Set getCollectors() {
		return Collections.unmodifiableSet(this.collectors);
	}

	protected void setCollectors0(Set collectors) {
		this.collectors.clear();
		if (collectors != null) {
			for (Iterator it = collectors.iterator(); it.hasNext();) {
				Collector collector = (Collector) it.next();
				collector.setMap(this);
			}
			this.collectors.addAll(collectors);
		}
	}

	public void setCollectors(Set collectors) {
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

	public Set getMarks() {
		return Collections.unmodifiableSet(this.marks);
	}

	protected void setMarks0(Set marks) {
		this.marks.clear();
		if (marks != null) {
			for (Iterator it = marks.iterator(); it.hasNext();) {
				Mark mark = (Mark) it.next();
				mark.setMap(this);
			}
			this.marks.addAll(marks);
		}
	}

	public void setMarks(Set marks) {
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

	public Set getNodeLinks() {
		return Collections.unmodifiableSet(this.nodeLinks);
	}

	protected void setNodeLinks0(Set nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null) {
			for (Iterator it = nodeLinks.iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink) it.next();
				nodeLink.setMap(this);
			}
			this.nodeLinks.addAll(nodeLinks);
		}
	}

	public void setNodeLinks(Set nodeLinks) {
		this.setNodeLinks0(nodeLinks);
		this.changed = true;
	}

	public Set getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(Set physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null) {
			for (Iterator it = physicalLinks.iterator(); it.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink) it.next();
				physicalLink.setMap(this);
			}
			this.physicalLinks.addAll(physicalLinks);
		}
		this.changed = true;
	}

	public void setPhysicalLinks(Set physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		this.changed = true;
	}

	public Set getSiteNodes() {
		return Collections.unmodifiableSet(this.siteNodes);
	}

	protected void setSiteNodes0(Set siteNodes) {
		this.siteNodes.clear();
		if (siteNodes != null) {
			for (Iterator it = siteNodes.iterator(); it.hasNext();) {
				SiteNode siteNode = (SiteNode) it.next();
				siteNode.setMap(this);
			}
			this.siteNodes.addAll(siteNodes);
		}
	}

	public void setSiteNodes(Set siteNodes) {
		this.setSiteNodes0(siteNodes);
		this.changed = true;
	}

	public Set getTopologicalNodes() {
		return Collections.unmodifiableSet(this.topologicalNodes);
	}

	protected void setTopologicalNodes0(Set topologicalNodes) {
		this.topologicalNodes.clear();
		if (topologicalNodes != null) {
			for (Iterator it = topologicalNodes.iterator(); it.hasNext();) {
				TopologicalNode topologicalNode = (TopologicalNode) it.next();
				topologicalNode.setMap(this);
			}
			this.topologicalNodes.addAll(topologicalNodes);
		}
	}

	public void setTopologicalNodes(Set topologicalNodes) {
		this.setTopologicalNodes0(topologicalNodes);
		this.changed = true;
	}

	public Set getMaps() {
		return Collections.unmodifiableSet(this.maps);
	}

	protected void setMaps0(Set maps) {
		this.maps.clear();
		if (maps != null)
			this.maps.addAll(maps);
	}

	public void setMaps(Set maps) {
		this.setMaps0(maps);
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
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
	}

	public Set getAllCollectors() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.collectors);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllCollectors());
		}
		return returnElements;
	}

	public Set getAllMarks() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.marks);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllMarks());
		}
		return returnElements;
	}

	public Set getAllNodeLinks() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.nodeLinks);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllNodeLinks());
		}
		return returnElements;
	}

	public Set getAllPhysicalLinks() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.physicalLinks);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllPhysicalLinks());
		}
		return returnElements;
	}

	public Set getAllSiteNodes() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.siteNodes);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllSiteNodes());
		}
		return returnElements;
	}

	public Set getAllTopologicalNodes() {
		Set returnElements = new HashSet();
		returnElements.addAll(this.topologicalNodes);
		for (Iterator iter = this.maps.iterator(); iter.hasNext();) {
			Map innerMap = (Map) iter.next();
			returnElements.addAll(innerMap.getAllTopologicalNodes());
		}
		return returnElements;
	}

	/**
	 * Получить список всех узловых элементов топологической схемы.
	 * 
	 * @return список узлов
	 */
	public Set getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.getAllSiteNodes());
		this.nodeElements.addAll(this.getAllTopologicalNodes());
		this.nodeElements.addAll(this.getAllMarks());
		return this.nodeElements;
	}

	/**
	 * Добавить новый узел.
	 * 
	 * @param node
	 *          узел
	 */
	public void addNode(AbstractNode node) {
		if (node instanceof SiteNode)
			this.siteNodes.add(node);
		else
			if (node instanceof TopologicalNode)
				this.topologicalNodes.add(node);
			else
				if (node instanceof Mark)
					this.marks.add(node);
		node.setMap(this);
		node.setRemoved(false);
		this.changed = true;
	}

	/**
	 * Удалить узел.
	 * 
	 * @param node
	 *          узел
	 */
	public void removeNode(AbstractNode node) {
		node.setSelected(false);
		if (node instanceof SiteNode)
			this.siteNodes.remove(node);
		else
			if (node instanceof TopologicalNode)
				this.topologicalNodes.remove(node);
			else
				if (node instanceof Mark)
					this.marks.remove(node);
		node.setRemoved(true);
		this.changed = true;
	}

	/**
	 * Получить элемент сетевого узла по идентификатору.
	 * 
	 * @param siteId
	 *          идентификатор сетевого узла
	 * @return сетевой узел или null, если узел не найден
	 */
	public SiteNode getSiteNode(Identifier siteId) {
		Iterator e = this.getAllSiteNodes().iterator();
		while (e.hasNext()) {
			SiteNode msne = (SiteNode) e.next();

			if (msne.getId().equals(siteId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить элемент топологического узла по идентификатору.
	 * 
	 * @param topologicalNodeId
	 *          идентификатор топологического узла
	 * @return топологический узел или null, если узел не найден
	 */
	public TopologicalNode getTopologicalNode(Identifier topologicalNodeId) {
		Iterator e = this.getAllTopologicalNodes().iterator();
		while (e.hasNext()) {
			TopologicalNode msne = (TopologicalNode) e.next();

			if (msne.getId().equals(topologicalNodeId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить метку по идентификатору.
	 * 
	 * @param markId
	 *          идентификатор метки
	 * @return метка или null, если метка не найден
	 */
	public Mark getMark(Identifier markId) {
		Iterator e = this.getAllMarks().iterator();
		while (e.hasNext()) {
			Mark msne = (Mark) e.next();

			if (msne.getId().equals(markId))
				return msne;
		}
		return null;
	}

	/**
	 * Получить узел по идентификатору.
	 * 
	 * @param nodeId
	 *          идентификатор метки
	 * @return узел или null, если узел не найден
	 */
	public AbstractNode getNode(Identifier nodeId) {
		AbstractNode node = getSiteNode(nodeId);
		if (node == null)
			node = getTopologicalNode(nodeId);
		if (node == null)
			node = getMark(nodeId);
		return null;
	}

	public void addMap(Map map) {
		this.maps.add(map);
		this.changed = true;
	}

	public void removeMap(Map map) {
		this.maps.remove(map);
		this.changed = true;
	}

	/**
	 * Добавить новый коллектор.
	 * 
	 * @param collector
	 *          новый коллектор
	 */
	public void addCollector(Collector collector) {
		this.collectors.add(collector);
		collector.setMap(this);
		collector.setRemoved(false);
		this.changed = true;
	}

	/**
	 * Удалить коллектор.
	 * 
	 * @param collector
	 *          коллектор
	 */
	public void removeCollector(Collector collector) {
		collector.setSelected(false);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		this.changed = true;
	}

	/**
	 * Получить коллектор, в составе которого есть заданная линия.
	 * 
	 * @param physicalLink
	 *          линия
	 * @return коллектор
	 */
	public Collector getCollector(PhysicalLink physicalLink) {
		for (Iterator it = this.getAllCollectors().iterator(); it.hasNext();) {
			Collector cp = (Collector) it.next();
			if (cp.getPhysicalLinks().contains(physicalLink))
				return cp;
		}
		return null;
	}

	/**
	 * Получить список линий, начинающихся или заканчивающихся в заданном узле.
	 * 
	 * @param node
	 *          узел
	 * @return список линий
	 */
	public Set getPhysicalLinksAt(AbstractNode node) {
		HashSet returnNodeLink = new HashSet();
		Iterator e = this.getAllPhysicalLinks().iterator();

		while (e.hasNext()) {
			PhysicalLink link = (PhysicalLink) e.next();
			if ((link.getEndNode().equals(node)) || (link.getStartNode().equals(node)))
				returnNodeLink.add(link);
		}
		return returnNodeLink;
	}

	/**
	 * Добавить новую линию.
	 * 
	 * @param physicalLink
	 *          линия
	 */
	public void addPhysicalLink(PhysicalLink physicalLink) {

		this.physicalLinks.add(physicalLink);
		physicalLink.setMap(this);
		physicalLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * Удалить линию.
	 * 
	 * @param physicalLink
	 *          линия
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
	 * Получить линию по ее идентификатору.
	 * 
	 * @param phisicalLinkId
	 *          идентификатор линии
	 * @return лниия
	 */
	public PhysicalLink getPhysicalLink(Identifier phisicalLinkId) {
		Iterator e = this.getAllPhysicalLinks().iterator();

		while (e.hasNext()) {
			PhysicalLink physicalLink = (PhysicalLink) e.next();
			if (physicalLink.getId().equals(phisicalLinkId))
				return physicalLink;
		}
		return null;
	}

	/**
	 * Получить линию по концевым узлам.
	 * 
	 * @param startNode
	 *          один концевой узел
	 * @param endNode
	 *          другой концевой узел
	 * @return линия
	 */
	public PhysicalLink getPhysicalLink(AbstractNode startNode, AbstractNode endNode) {
		for (Iterator it = this.getAllPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink) it.next();
			if (((link.getStartNode().equals(startNode)) && (link.getEndNode().equals(endNode)))
					|| ((link.getStartNode().equals(endNode)) && (link.getEndNode().equals(startNode)))) {
				return link;
			}
		}
		return null;
	}

	/**
	 * добавить новый фрагмент линии.
	 * 
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void addNodeLink(NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
		nodeLink.setMap(this);
		nodeLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * Удалить фрагмент линии.
	 * 
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void removeNodeLink(NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		this.changed = true;
	}

	/**
	 * Получить фрагмент линии по идентификатору.
	 * 
	 * @param nodeLinkId
	 *          идентификатор фрагмента линии
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink(Identifier nodeLinkId) {
		Iterator e = this.getAllNodeLinks().iterator();

		while (e.hasNext()) {
			NodeLink nodeLink = (NodeLink) e.next();
			if (nodeLink.getId().equals(nodeLinkId)) {
				return nodeLink;
			}
		}
		return null;
	}

	public List getNodeLinks(PhysicalLink link) {
		List nlinks = new ArrayList();
		for (Iterator it = this.nodeLinks.iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink) it.next();
			if (nodeLink.getPhysicalLink().equals(link))
				nlinks.add(nodeLink);
		}
		return nlinks;
	}

	/**
	 * Получить фрагмент линии по концевому узлу.
	 * 
	 * @param node
	 *          концевой узел
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink(AbstractNode node) {
		for (Iterator it = this.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink) it.next();
			if ((nodeLink.getStartNode().equals(node)) || (nodeLink.getEndNode().equals(node))) {
				return nodeLink;
			}
		}
		return null;
	}

	/**
	 * Получить фрагмент линии по концевым узлам.
	 * 
	 * @param startNode
	 *          один концевой узел
	 * @param endNode
	 *          другой концевой узел
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink(AbstractNode startNode, AbstractNode endNode) {
		for (Iterator it = this.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink link = (NodeLink) it.next();
			if (((link.getStartNode().equals(startNode)) && (link.getEndNode().equals(endNode)))
					|| ((link.getStartNode().equals(endNode)) && (link.getEndNode().equals(startNode)))) {
				return link;
			}
		}
		return null;
	}

	/**
	 * Получить список всех топологических элементов карты ({@link MapElement}).
	 * 
	 * @return список всех элементов
	 */
	public Set getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.getAllMarks());
		this.allElements.addAll(this.getAllTopologicalNodes());
		this.allElements.addAll(this.getAllSiteNodes());

		this.allElements.addAll(this.getAllNodeLinks());
		this.allElements.addAll(this.getAllPhysicalLinks());
		this.allElements.addAll(this.getAllCollectors());

		return Collections.unmodifiableSet(this.allElements);
	}

	/**
	 * Получить набор всех выделенных элементов топологической схемы. 
	 * Выделенные элементы - те, для которых  метод 
	 * <code>{@link MapElement#isSelected()}</code> возвращает <code>true</code>.
	 * @return набор выделенных элементов
	 */
	public Set getSelectedElements() {
		return this.selectedElements;
	}

	/**
	 * Очистить набор выделенных элементов топологической схемы.
	 */
	public void clearSelection() {
		this.selectedElements.clear();
	}

	/**
	 * Зарегистрировать изменение флага выделения элемента топологической схемы.
	 * 
	 * @param me
	 *          элемент
	 * @param selected
	 *          флаг выделения
	 */
	public void setSelected(MapElement me, boolean selected) {
		if (selected)
			this.selectedElements.add(me);
		else
			this.selectedElements.remove(me);
	}

	/**
	 * Возвращает описывающий элемент набор параметров, который используется для
	 * экспорта.
	 * 
	 * @return хэш-таблица параметров элемента
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static Map createInstance(Identifier creatorId, Identifier domainId, java.util.Map exportMap1)
			throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);

		if (id1 == null || name1 == null || description1 == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Map map = new Map(id1, creatorId, 0L, domainId, name1, description1);
			map.changed = true;
			return map;
		}
		catch (Exception e) {
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}

}

