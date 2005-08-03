/*-
 * $Id: Map.java,v 1.68 2005/08/03 16:25:13 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.IdlMap;
import com.syrus.AMFICOM.map.corba.IdlMapHelper;

/**
 * Топологическая схема, которая содержит в себе набор связанных друг с другом
 * узлов (сетевых и топологических), линий (состоящих из фрагментов), меток на
 * линиях, коллекторов (объединяющих в себе линии).
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.68 $, $Date: 2005/08/03 16:25:13 $
 * @module map_v1
 * @todo make maps persistent
 * @todo make externalNodes persistent
 * @todo make mapLibraries persistent
 */
public final class Map extends DomainMember implements Namable, XMLBeansTransferable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722862181200184L;

	private String name;
	private String description;

	private Set<SiteNode> siteNodes;
	private Set<TopologicalNode> topologicalNodes;
	private Set<NodeLink> nodeLinks;
	private Set<PhysicalLink> physicalLinks;
	private Set<Mark> marks;
	private Set<Collector> collectors;

	protected Set<Map> maps;
	protected Set<SiteNode> externalNodes;
	protected Set<MapLibrary> mapLibraries;

	/**
	 * Сортированный список всех элементов топологической схемы
	 */
	protected transient List<MapElement> allElements;
	protected transient Set<AbstractNode> nodeElements;
	protected transient Set<MapElement> selectedElements;

	Map(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.MAP_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Map(final IdlMap mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Map(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version, domainId);
		this.name = name;
		this.description = description;

		this.siteNodes = new HashSet<SiteNode>();
		this.topologicalNodes = new HashSet<TopologicalNode>();
		this.nodeLinks = new HashSet<NodeLink>();
		this.physicalLinks = new HashSet<PhysicalLink>();
		this.marks = new HashSet<Mark>();
		this.collectors = new HashSet<Collector>();

		this.maps = new HashSet<Map>();
		this.mapLibraries = new HashSet<MapLibrary>();
		this.selectedElements = new HashSet<MapElement>();
		this.allElements = new LinkedList<MapElement>();
		this.nodeElements = new HashSet<AbstractNode>();
		this.externalNodes = new HashSet<SiteNode>();
	}

	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description) throws CreateObjectException {
		if (name == null || description == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Map map = new Map(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAP_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description);

			assert map.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			map.markAsChanged();

			return map;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMap mt = (IdlMap) transferable;
		super.fromTransferable(mt, new Identifier(mt.domainId));

		this.name = mt.name;
		this.description = mt.description;

		Set<Identifier> ids;
		
		ids = Identifier.fromTransferables(mt.siteNodeIds);
		this.siteNodes = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.topologicalNodeIds);
		this.topologicalNodes = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.nodeLinkIds);
		this.nodeLinks = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.physicalLinkIds);
		this.physicalLinks = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.markIds);
		this.marks = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.collectorIds);
		this.collectors = StorableObjectPool.getStorableObjects(ids, true);
		
		ids = Identifier.fromTransferables(mt.mapIds);
		this.maps = StorableObjectPool.getStorableObjects(ids, true);
		
		ids = Identifier.fromTransferables(mt.externalNodeIds);
		this.externalNodes = StorableObjectPool.getStorableObjects(ids, true);
		
		ids = Identifier.fromTransferables(mt.mapLibraryIds);
		this.mapLibraries = StorableObjectPool.getStorableObjects(ids, true);
		
		this.allElements = new LinkedList<MapElement>();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.siteNodes);
		dependencies.addAll(this.topologicalNodes);
		dependencies.addAll(this.nodeLinks);
		dependencies.addAll(this.physicalLinks);
		dependencies.addAll(this.marks);
		dependencies.addAll(this.collectors);
		dependencies.addAll(this.maps);
		dependencies.addAll(this.externalNodes);
		dependencies.addAll(this.mapLibraries);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMap getTransferable(final ORB orb) {
		final IdlIdentifier[] siteNodeIds = Identifier.createTransferables(this.siteNodes);
		final IdlIdentifier[] topologicalNodeIds = Identifier.createTransferables(this.topologicalNodes);
		final IdlIdentifier[] nodeLinkIds = Identifier.createTransferables(this.nodeLinks);
		final IdlIdentifier[] physicalNodeLinkIds = Identifier.createTransferables(this.physicalLinks);
		final IdlIdentifier[] markIds = Identifier.createTransferables(this.marks);
		final IdlIdentifier[] collectorIds = Identifier.createTransferables(this.collectors);
		final IdlIdentifier[] mapIds = Identifier.createTransferables(this.maps);
		final IdlIdentifier[] externalNodesIds = Identifier.createTransferables(this.externalNodes);
		final IdlIdentifier[] mapLibraryIds = Identifier.createTransferables(this.mapLibraries);
		return IdlMapHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				siteNodeIds,
				topologicalNodeIds,
				nodeLinkIds,
				physicalNodeLinkIds,
				markIds,
				collectorIds,
				mapIds,
				externalNodesIds,
				mapLibraryIds);
	}

	public Set<Collector> getCollectors() {
		return Collections.unmodifiableSet(this.collectors);
	}

	protected void setCollectors0(final Set<Collector> collectors) {
		this.collectors.clear();
		if (collectors != null) {
			this.collectors.addAll(collectors);
		}
	}

	public void setCollectors(final Set<Collector> collectors) {
		this.setCollectors0(collectors);
		super.markAsChanged();
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}	

	public void setMapLibraries(final Set<MapLibrary> mapLibraries) {
		this.setMapLibraries0(mapLibraries);
		super.markAsChanged();
	}
	
	protected void setMapLibraries0(final Set<MapLibrary> mapLibraries) {
		this.mapLibraries.clear();
		if (mapLibraries != null) {
			this.mapLibraries.addAll(mapLibraries);
		}
	}
	
	public Set<Mark> getMarks() {
		return Collections.unmodifiableSet(this.marks);
	}

	protected void setMarks0(final Set<Mark> marks) {
		this.marks.clear();
		if (marks != null) {
			this.marks.addAll(marks);
		}
	}

	public void setMarks(final Set<Mark> marks) {
		this.setMarks0(marks);
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(final String name) {
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	public Set<NodeLink> getNodeLinks() {
		return Collections.unmodifiableSet(this.nodeLinks);
	}

	protected void setNodeLinks0(final Set<NodeLink> nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null) {
			this.nodeLinks.addAll(nodeLinks);
		}
	}

	public void setNodeLinks(final Set<NodeLink> nodeLinks) {
		this.setNodeLinks0(nodeLinks);
		super.markAsChanged();
	}

	public Set<PhysicalLink> getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(final Set<PhysicalLink> physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null) {
			this.physicalLinks.addAll(physicalLinks);
		}
		super.markAsChanged();
	}

	public void setPhysicalLinks(final Set<PhysicalLink> physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.markAsChanged();
	}

	public Set<SiteNode> getSiteNodes() {
		return Collections.unmodifiableSet(this.siteNodes);
	}

	protected void setSiteNodes0(final Set<SiteNode> siteNodes) {
		this.siteNodes.clear();
		if (siteNodes != null) {
			this.siteNodes.addAll(siteNodes);
		}
	}

	public void setSiteNodes(final Set<SiteNode> siteNodes) {
		this.setSiteNodes0(siteNodes);
		super.markAsChanged();
	}

	public Set<TopologicalNode> getTopologicalNodes() {
		return Collections.unmodifiableSet(this.topologicalNodes);
	}

	protected void setTopologicalNodes0(final Set<TopologicalNode> topologicalNodes) {
		this.topologicalNodes.clear();
		if (topologicalNodes != null) {
			this.topologicalNodes.addAll(topologicalNodes);
		}
	}

	public void setTopologicalNodes(final Set<TopologicalNode> topologicalNodes) {
		this.setTopologicalNodes0(topologicalNodes);
		super.markAsChanged();
	}

	public Set<MapLibrary> getMapLibraries() {
		return Collections.unmodifiableSet(this.mapLibraries);
	}

	public Set<Map> getMaps() {
		return Collections.unmodifiableSet(this.maps);
	}

	protected void setMaps0(final Set<Map> maps) {
		this.maps.clear();
		if (maps != null)
			this.maps.addAll(maps);
	}

	public void setMaps(final Set<Map> maps) {
		this.setMaps0(maps);
		super.markAsChanged();
	}

	public Set<SiteNode> getExternalNodes() {
		return Collections.unmodifiableSet(this.externalNodes);
	}

	protected void setExternalNodes0(final Set<SiteNode> externalNodes) {
		this.externalNodes.clear();
		if (externalNodes != null)
			this.externalNodes.addAll(externalNodes);
	}

	public void setExternalNodes(final Set<SiteNode> externalNodes) {
		this.setExternalNodes0(externalNodes);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier domainId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
	}

	public Set<Collector> getAllCollectors() {
		final Set<Collector> returnElements = new HashSet<Collector>();
		returnElements.addAll(this.collectors);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllCollectors());
		}
		return returnElements;
	}

	public Set<Mark> getAllMarks() {
		final Set<Mark> returnElements = new HashSet<Mark>();
		returnElements.addAll(this.marks);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllMarks());
		}
		return returnElements;
	}

	public Set<NodeLink> getAllNodeLinks() {
		final Set<NodeLink> returnElements = new HashSet<NodeLink>();
		returnElements.addAll(this.nodeLinks);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllNodeLinks());
		}
		return returnElements;
	}

	public Set<PhysicalLink> getAllPhysicalLinks() {
		final Set<PhysicalLink> returnElements = new HashSet<PhysicalLink>();
		returnElements.addAll(this.physicalLinks);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllPhysicalLinks());
		}
		return returnElements;
	}

	public Set<SiteNode> getAllSiteNodes() {
		final Set<SiteNode> returnElements = new HashSet<SiteNode>();
		returnElements.addAll(this.siteNodes);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllSiteNodes());
		}
		return returnElements;
	}

	public Set<TopologicalNode> getAllTopologicalNodes() {
		final Set<TopologicalNode> returnElements = new HashSet<TopologicalNode>();
		returnElements.addAll(this.topologicalNodes);
		for (final Map innerMap : this.maps) {
			returnElements.addAll(innerMap.getAllTopologicalNodes());
		}
		return returnElements;
	}

	/**
	 * Получить список всех узловых элементов топологической схемы.
	 *
	 * @return список узлов
	 */
	public Set<AbstractNode> getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.getAllSiteNodes());
		this.nodeElements.addAll(this.getAllTopologicalNodes());
		this.nodeElements.addAll(this.getAllMarks());
		this.nodeElements.addAll(this.getExternalNodes());
		return Collections.unmodifiableSet(this.nodeElements);
	}

	/**
	 * Добавить новый узел.
	 *
	 * @param node
	 *          узел
	 */
	public void addNode(final AbstractNode node) {
		if (node instanceof SiteNode)
			this.siteNodes.add((SiteNode) node);
		else if (node instanceof TopologicalNode)
			this.topologicalNodes.add((TopologicalNode) node);
		else if (node instanceof Mark)
			this.marks.add((Mark) node);
		node.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * Удалить узел.
	 *
	 * @param node
	 *          узел
	 */
	public void removeNode(final AbstractNode node) {
		node.setSelected(false);
		this.selectedElements.remove(node);
		if (node instanceof SiteNode) {
			this.siteNodes.remove(node);
			this.externalNodes.remove(node);
		} else if (node instanceof TopologicalNode)
			this.topologicalNodes.remove(node);
		else if (node instanceof Mark)
			this.marks.remove(node);
		node.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * Получить элемент сетевого узла по идентификатору.
	 *
	 * @param siteId
	 *          идентификатор сетевого узла
	 * @return сетевой узел или null, если узел не найден
	 */
	public SiteNode getSiteNode(final Identifier siteId) {
		for (final SiteNode siteNode : this.getAllSiteNodes()) {
			if (siteNode.getId().equals(siteId))
				return siteNode;
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
	public TopologicalNode getTopologicalNode(final Identifier topologicalNodeId) {
		for (final TopologicalNode topologicalNode : this.getAllTopologicalNodes()) {
			if (topologicalNode.getId().equals(topologicalNodeId))
				return topologicalNode;
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
	public Mark getMark(final Identifier markId) {
		for (final Mark mark : this.getAllMarks()) {
			if (mark.getId().equals(markId))
				return mark;
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
	public AbstractNode getNode(final Identifier nodeId) {
		AbstractNode node = this.getSiteNode(nodeId);
		if (node == null)
			node = this.getTopologicalNode(nodeId);
		if (node == null)
			node = this.getMark(nodeId);
		return null;
	}

	public void addMapLibrary(final MapLibrary mapLibrary) {
		this.mapLibraries.add(mapLibrary);
		super.markAsChanged();
	}

	public void removeMapLibrary(final MapLibrary mapLibrary) {
		this.mapLibraries.remove(mapLibrary);
		super.markAsChanged();
	}

	public void addMap(final Map map) {
		this.maps.add(map);
		super.markAsChanged();
	}

	public void removeMap(final Map map) {
		this.maps.remove(map);
		super.markAsChanged();
	}

	public void addExternalNode(final SiteNode externalNode) {
		this.externalNodes.add(externalNode);
		super.markAsChanged();
	}

	public void removeExternalNode(final SiteNode externalNode) {
		this.externalNodes.remove(externalNode);
		super.markAsChanged();
	}

	/**
	 * Добавить новый коллектор.
	 *
	 * @param collector
	 *          новый коллектор
	 */
	public void addCollector(final Collector collector) {
		this.collectors.add(collector);
		collector.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * Удалить коллектор.
	 *
	 * @param collector
	 *          коллектор
	 */
	public void removeCollector(final Collector collector) {
		collector.setSelected(false);
		this.selectedElements.remove(collector);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * Получить коллектор, в составе которого есть заданная линия.
	 *
	 * @param physicalLink
	 *          линия
	 * @return коллектор
	 */
	public Collector getCollector(final PhysicalLink physicalLink) {
		for (final Collector collector : this.getAllCollectors()) {
			if (collector.getPhysicalLinks().contains(physicalLink))
				return collector;
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
	public Set getPhysicalLinksAt(final AbstractNode node) {
		final HashSet<PhysicalLink> returnLinks = new HashSet<PhysicalLink>();
		for (final PhysicalLink physicalLink : this.getAllPhysicalLinks()) {
			if ((physicalLink.getEndNode().equals(node)) || (physicalLink.getStartNode().equals(node)))
				returnLinks.add(physicalLink);
		}
		return returnLinks;
	}

	/**
	 * Добавить новую линию.
	 *
	 * @param physicalLink
	 *          линия
	 */
	public void addPhysicalLink(final PhysicalLink physicalLink) {
		this.physicalLinks.add(physicalLink);
		physicalLink.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * Удалить линию.
	 *
	 * @param physicalLink
	 *          линия
	 */
	public void removePhysicalLink(final PhysicalLink physicalLink) {
		physicalLink.setSelected(false);
		this.selectedElements.remove(physicalLink);
		this.physicalLinks.remove(physicalLink);
		physicalLink.setRemoved(true);

		final Collector coll = getCollector(physicalLink);
		if (coll != null)
			coll.removePhysicalLink(physicalLink);
		super.markAsChanged();
	}

	/**
	 * Получить линию по ее идентификатору.
	 *
	 * @param phisicalLinkId
	 *          идентификатор линии
	 * @return лниия
	 */
	public PhysicalLink getPhysicalLink(final Identifier phisicalLinkId) {
		for (final PhysicalLink physicalLink : this.getAllPhysicalLinks()) {
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
	public PhysicalLink getPhysicalLink(final AbstractNode startNode, final AbstractNode endNode) {
		for (final PhysicalLink physicalLink : this.getAllPhysicalLinks()) {
			if (((physicalLink.getStartNode().equals(startNode)) && (physicalLink.getEndNode().equals(endNode)))
					|| ((physicalLink.getStartNode().equals(endNode)) && (physicalLink.getEndNode().equals(startNode)))) {
				return physicalLink;
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
	public void addNodeLink(final NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
		nodeLink.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * Удалить фрагмент линии.
	 *
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void removeNodeLink(final NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.selectedElements.remove(nodeLink);
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * Получить фрагмент линии по идентификатору.
	 *
	 * @param nodeLinkId
	 *          идентификатор фрагмента линии
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink(final Identifier nodeLinkId) {
		for (final NodeLink nodeLink : this.getAllNodeLinks()) {
			if (nodeLink.getId().equals(nodeLinkId)) {
				return nodeLink;
			}
		}
		return null;
	}

	public List<NodeLink> getNodeLinks(final PhysicalLink link) {
		final List<NodeLink> nlinks = new ArrayList<NodeLink>();
		for (final NodeLink nodeLink : this.nodeLinks) {
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
	public NodeLink getNodeLink(final AbstractNode node) {
		for (final NodeLink nodeLink : this.getNodeLinks()) {
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
	public NodeLink getNodeLink(final AbstractNode startNode, final AbstractNode endNode) {
		for (final NodeLink nodeLink : this.getNodeLinks()) {
			if (((nodeLink.getStartNode().equals(startNode)) && (nodeLink.getEndNode().equals(endNode)))
					|| ((nodeLink.getStartNode().equals(endNode)) && (nodeLink.getEndNode().equals(startNode)))) {
				return nodeLink;
			}
		}
		return null;
	}

	/**
	 * Получить список всех топологических элементов карты ({@link MapElement}).
	 *
	 * @return список всех элементов
	 */
	public List<MapElement> getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.getAllMarks());
		this.allElements.addAll(this.getAllTopologicalNodes());
		this.allElements.addAll(this.getAllSiteNodes());
		this.allElements.addAll(this.getExternalNodes());

		this.allElements.addAll(this.getAllNodeLinks());
		this.allElements.addAll(this.getAllPhysicalLinks());
		this.allElements.addAll(this.getAllCollectors());
		
		return Collections.unmodifiableList(this.allElements);
	}

	/**
	 * Получить набор всех выделенных элементов топологической схемы.
	 * Выделенные элементы - те, для которых  метод
	 * <code>{@link MapElement#isSelected()}</code> возвращает <code>true</code>.
	 * @return набор выделенных элементов
	 */
	public Set<MapElement> getSelectedElements() {
		return Collections.unmodifiableSet(this.selectedElements);
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
	public void setSelected(final MapElement me, final boolean selected) {
		me.setSelected(selected);
		if (selected) {
			this.selectedElements.add(me);
		}
		else {
			this.selectedElements.remove(me);
		}
	}

	/**
	 * Получить список фрагментов линий, содержащих заданный узел.
	 * @param node узел
	 * @return Список фрагментов
	 */
	public Set<NodeLink> getNodeLinks(final AbstractNode node) {
		final Set<NodeLink> returnNodeLinks = new HashSet<NodeLink>();
		for (final NodeLink nodeLink : this.getNodeLinks()) {
			if ((nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node))) {
				returnNodeLinks.add(nodeLink);
			}
		}

		return returnNodeLinks;
	}

	/**
	 * Возвращает фрагмент линии, включающий данный узел, по не равный
	 * переданному в параметре. Если фрагмент А и фрагмент Б имеют общую
	 * точку Т, то вызов метода <code>Т.getOtherNodeLink(А)</code> вернет Б, а вызов
	 * <code>Т.getOtherNodeLink(Б)</code> вернет А. Таким образом, для топологического
	 * узла возвращает единственный противоположный,
	 * для сетевого узла их может быть несколько, по этой причине метод
	 * не должен использоваться и возвращает null
	 * @param node узел
	 * @param nodeLink фрагмент линии
	 * @return другой фрагмент линии
	 */
	public NodeLink getOtherNodeLink(final AbstractNode node, NodeLink nodeLink) {
		if (!node.getClass().equals(TopologicalNode.class)) {
			return null;
		}

		NodeLink startNodeLink = null;
		for (final NodeLink nl : this.getNodeLinks()) {
			if (nodeLink != nl) {
				startNodeLink = nl;
				break;
			}
		}

		return startNodeLink;
	}

	/**
	 * Получить вектор узлов на противоположных концах всех фрагментов линий
	 * данного элемента.
	 * 
	 * @param node
	 *        узел
	 * @return список узлов
	 */
	public Set<AbstractNode> getOppositeNodes(final AbstractNode node) {
		final Set<AbstractNode> returnNodes = new HashSet<AbstractNode>();
		for (final NodeLink nodeLink : this.getNodeLinks(node)) {
			if (nodeLink.getEndNode().equals(node)) {
				returnNodes.add(nodeLink.getStartNode());
			}
			else {
				returnNodes.add(nodeLink.getEndNode());
			}
		}

		return returnNodes;
	}

	/**
	 * Получить список линий, начинающихся или заканчивающихся на данном узле.
	 * 
	 * @return список линий
	 */
	public Set<PhysicalLink> getPhysicalLinks(final AbstractNode node) {
		final Set<PhysicalLink> returnLinks = new HashSet<PhysicalLink>();
		for (final PhysicalLink physicalLink : this.getPhysicalLinks()) {
			if ((physicalLink.getEndNode().equals(node)) || (physicalLink.getStartNode().equals(node)))
				returnLinks.add(physicalLink);
		}

		return returnLinks;
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.Map xmlMap = com.syrus.amficom.map.xml.Map.Factory.newInstance();
		this.fillXMLTransferable(xmlMap);
		return xmlMap;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map) xmlObject;

		final com.syrus.amficom.general.xml.UID uid = xmlMap.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlMap.setName(this.name);
		xmlMap.setDescription(this.description);

		final com.syrus.amficom.map.xml.TopologicalNodes xmlTopologicalNodes = xmlMap.addNewTopologicalnodes();
		final com.syrus.amficom.map.xml.SiteNodes xmlSiteNodes = xmlMap.addNewSitenodes();
		final com.syrus.amficom.map.xml.PhysicalLinks xmlPhysicalLinks = xmlMap.addNewPhysicallinks();
		final com.syrus.amficom.map.xml.NodeLinks xmlNodeLinks = xmlMap.addNewNodelinks();
		final com.syrus.amficom.map.xml.Collectors xmlCollectors = xmlMap.addNewCollectors();
				
		final Collection<XmlObject> xmlTopologicalNodesArray = new LinkedList<XmlObject>();
		for (final TopologicalNode topologicalNode : this.getTopologicalNodes()) {
			xmlTopologicalNodesArray.add(topologicalNode.getXMLTransferable());
		}
		xmlTopologicalNodes.setTopologicalnodeArray(xmlTopologicalNodesArray.toArray(new com.syrus.amficom.map.xml.TopologicalNode[xmlTopologicalNodesArray.size()]));

		final Collection<XmlObject> xmlSiteNodesArray = new LinkedList<XmlObject>();
		for (final SiteNode siteNode : this.getSiteNodes()) {
			xmlSiteNodesArray.add(siteNode.getXMLTransferable());
		}
		xmlSiteNodes.setSitenodeArray(xmlSiteNodesArray.toArray(new com.syrus.amficom.map.xml.SiteNode[xmlSiteNodesArray.size()]));

		final Collection<XmlObject> xmlPhysicalLinksArray = new LinkedList<XmlObject>();
		for (final PhysicalLink physicalLink : this.getPhysicalLinks()) {
			xmlPhysicalLinksArray.add(physicalLink.getXMLTransferable());
		}
		xmlPhysicalLinks.setPhysicallinkArray(xmlPhysicalLinksArray.toArray(new com.syrus.amficom.map.xml.PhysicalLink[xmlPhysicalLinksArray.size()]));

		final Collection<XmlObject> xmlNodeLinksArray = new LinkedList<XmlObject>();
		for (final NodeLink nodeLink : this.getNodeLinks()) {
			xmlNodeLinksArray.add(nodeLink.getXMLTransferable());
		}
		xmlNodeLinks.setNodelinkArray(xmlNodeLinksArray.toArray(new com.syrus.amficom.map.xml.NodeLink[xmlNodeLinksArray.size()]));

		final Collection<XmlObject> xmlCollectorsArray = new LinkedList<XmlObject>();
		for (final Collector collector : this.getCollectors()) {
			xmlCollectorsArray.add(collector.getXMLTransferable());
		}
		xmlCollectors.setCollectorArray(xmlCollectorsArray.toArray(new com.syrus.amficom.map.xml.Collector[xmlCollectorsArray.size()]));
	}

	Map(final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final com.syrus.amficom.map.xml.Map xmlMap,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.MAP_CODE, xmlMap.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.fromXMLTransferable(xmlMap, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map) xmlObject; 
		
		this.name = xmlMap.getName();
		this.description = xmlMap.getDescription();

		this.siteNodes = new HashSet<SiteNode>();
		this.topologicalNodes = new HashSet<TopologicalNode>();
		this.nodeLinks = new HashSet<NodeLink>();
		this.physicalLinks = new HashSet<PhysicalLink>();
		this.marks = new HashSet<Mark>();
		this.collectors = new HashSet<Collector>();

		this.maps = new HashSet<Map>();
		this.selectedElements = new HashSet<MapElement>();
		this.allElements = new LinkedList<MapElement>();
		this.nodeElements = new HashSet<AbstractNode>();
		this.externalNodes = new HashSet<SiteNode>();
		
		final com.syrus.amficom.map.xml.TopologicalNode[] xmlTopologicalNodesArray = xmlMap.getTopologicalnodes().getTopologicalnodeArray();
		for (int i = 0; i < xmlTopologicalNodesArray.length; i++) {
			final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = xmlTopologicalNodesArray[i];
			this.addNode(TopologicalNode.createInstance(this.creatorId, xmlTopologicalNode, clonedIdsPool));
		}

		final com.syrus.amficom.map.xml.SiteNode[] xmlSiteNodesArray = xmlMap.getSitenodes().getSitenodeArray();
		for (int i = 0; i < xmlSiteNodesArray.length; i++) {
			final com.syrus.amficom.map.xml.SiteNode xmlSiteNode = xmlSiteNodesArray[i];
			this.addNode(SiteNode.createInstance(this.creatorId, xmlSiteNode, clonedIdsPool));
		}

		final com.syrus.amficom.map.xml.PhysicalLink[] xmlPhysicalLinksArray = xmlMap.getPhysicallinks().getPhysicallinkArray();
		for (int i = 0; i < xmlPhysicalLinksArray.length; i++) {
			final com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = xmlPhysicalLinksArray[i];
			this.addPhysicalLink(PhysicalLink.createInstance(this.creatorId, xmlPhysicalLink, clonedIdsPool));
		}

		final com.syrus.amficom.map.xml.NodeLink[] xmlNodeLinksArray = xmlMap.getNodelinks().getNodelinkArray();
		for (int i = 0; i < xmlNodeLinksArray.length; i++) {
			final com.syrus.amficom.map.xml.NodeLink xmlNodeLink = xmlNodeLinksArray[i];
			this.addNodeLink(NodeLink.createInstance(this.creatorId, xmlNodeLink, clonedIdsPool));
		}

		final com.syrus.amficom.map.xml.Collector[] xmlCollectorsArray = xmlMap.getCollectors().getCollectorArray();
		for (int i = 0; i < xmlCollectorsArray.length; i++) {
			final com.syrus.amficom.map.xml.Collector xmlCollector = xmlCollectorsArray[i];
			this.addCollector(Collector.createInstance(this.creatorId, xmlCollector, clonedIdsPool));
		}
	}

	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		final com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map) xmlObject;

		try {
			final Map map = new Map(creatorId, StorableObjectVersion.createInitial(), domainId, xmlMap, clonedIdsPool);
			assert map.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			map.markAsChanged();

			return map;
		} catch (Exception e) {
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}

}

