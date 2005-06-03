/*-
 * $Id: Map.java,v 1.46 2005/06/03 20:38:45 arseniy Exp $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;

/**
 * Топологическая схема, которая содержит в себе набор связанных друг с другом
 * узлов (сетевых и топологических), линий (состоящих из фрагментов), меток на
 * линиях, коллекторов (объединяющих в себе линии).
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.46 $, $Date: 2005/06/03 20:38:45 $
 * @module map_v1
 * @todo make maps persistent
 * @todo make externalNodes persistent
 */
public class Map extends DomainMember implements Namable, XMLBeansTransferable {

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
	 * Набор параметров для экспорта. инициализируется только в случае
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
	protected transient Set externalNodes;

	protected transient Set selectedElements;
	/**
	 * Сортированный список всех элементов топологической схемы
	 */
	protected transient List allElements;
	protected transient Set nodeElements;


	Map(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		final MapDatabase database = (MapDatabase) DatabaseContext.getDatabase(ObjectEntities.MAP_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	Map(final Map_Transferable mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
		this.allElements = new LinkedList();
		this.nodeElements = new HashSet();
		this.externalNodes = new HashSet();
	}

	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description) throws CreateObjectException {
		if (name == null || description == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Map map = new Map(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAP_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description);

			assert map.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			map.markAsChanged();

			return map;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Map_Transferable mt = (Map_Transferable) transferable;
		super.fromTransferable(mt.header, new Identifier(mt.domain_id));

		this.name = mt.name;
		this.description = mt.description;

		Set ids;
		
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
		Identifier_Transferable[] siteNodeIds = Identifier.createTransferables(this.siteNodes);
		Identifier_Transferable[] topologicalNodeIds = Identifier.createTransferables(this.topologicalNodes);
		Identifier_Transferable[] nodeLinkIds = Identifier.createTransferables(this.nodeLinks);
		Identifier_Transferable[] physicalNodeLinkIds = Identifier.createTransferables(this.physicalLinks);
		Identifier_Transferable[] markIds = Identifier.createTransferables(this.marks);
		Identifier_Transferable[] collectorIds = Identifier.createTransferables(this.collectors);
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

	protected void setCollectors0(final Set collectors) {
		this.collectors.clear();
		if (collectors != null) {
			this.collectors.addAll(collectors);
		}
	}

	public void setCollectors(final Set collectors) {
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

	public Set getMarks() {
		return Collections.unmodifiableSet(this.marks);
	}

	protected void setMarks0(final Set marks) {
		this.marks.clear();
		if (marks != null) {
			this.marks.addAll(marks);
		}
	}

	public void setMarks(final Set marks) {
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

	public Set getNodeLinks() {
		return Collections.unmodifiableSet(this.nodeLinks);
	}

	protected void setNodeLinks0(final Set nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null) {
			this.nodeLinks.addAll(nodeLinks);
		}
	}

	public void setNodeLinks(final Set nodeLinks) {
		this.setNodeLinks0(nodeLinks);
		super.markAsChanged();
	}

	public Set getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(final Set physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null) {
			this.physicalLinks.addAll(physicalLinks);
		}
		super.markAsChanged();
	}

	public void setPhysicalLinks(final Set physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.markAsChanged();
	}

	public Set getSiteNodes() {
		return Collections.unmodifiableSet(this.siteNodes);
	}

	protected void setSiteNodes0(final Set siteNodes) {
		this.siteNodes.clear();
		if (siteNodes != null) {
			this.siteNodes.addAll(siteNodes);
		}
	}

	public void setSiteNodes(final Set siteNodes) {
		this.setSiteNodes0(siteNodes);
		super.markAsChanged();
	}

	public Set getTopologicalNodes() {
		return Collections.unmodifiableSet(this.topologicalNodes);
	}

	protected void setTopologicalNodes0(final Set topologicalNodes) {
		this.topologicalNodes.clear();
		if (topologicalNodes != null) {
			this.topologicalNodes.addAll(topologicalNodes);
		}
	}

	public void setTopologicalNodes(final Set topologicalNodes) {
		this.setTopologicalNodes0(topologicalNodes);
		super.markAsChanged();
	}

	public Set getMaps() {
		return Collections.unmodifiableSet(this.maps);
	}

	protected void setMaps0(final Set maps) {
		this.maps.clear();
		if (maps != null)
			this.maps.addAll(maps);
	}

	public void setMaps(final Set maps) {
		this.setMaps0(maps);
		super.markAsChanged();
	}

	public Set getExternalNodes() {
		return Collections.unmodifiableSet(this.externalNodes);
	}

	protected void setExternalNodes0(final Set externalNodes) {
		this.externalNodes.clear();
		if (externalNodes != null)
			this.externalNodes.addAll(externalNodes);
	}

	public void setExternalNodes(final Set externalNodes) {
		this.setExternalNodes0(externalNodes);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String description,
			final Identifier domainId) {
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
		this.nodeElements.addAll(this.getExternalNodes());
		return this.nodeElements;
	}

	/**
	 * Добавить новый узел.
	 *
	 * @param node
	 *          узел
	 */
	public void addNode(final AbstractNode node) {
		if (node instanceof SiteNode)
			this.siteNodes.add(node);
		else if (node instanceof TopologicalNode)
			this.topologicalNodes.add(node);
		else if (node instanceof Mark)
			this.marks.add(node);
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
		}
		else if (node instanceof TopologicalNode)
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
	public TopologicalNode getTopologicalNode(final Identifier topologicalNodeId) {
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
	public Mark getMark(final Identifier markId) {
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
	public AbstractNode getNode(final Identifier nodeId) {
		AbstractNode node = getSiteNode(nodeId);
		if (node == null)
			node = getTopologicalNode(nodeId);
		if (node == null)
			node = getMark(nodeId);
		return null;
	}

	public void addMap(final Map map) {
		this.maps.add(map);
		super.markAsChanged();
	}

	public void removeMap(final Map map) {
		this.maps.remove(map);
		super.markAsChanged();
	}

	public void addExternalNode(final AbstractNode externalNode) {
		this.externalNodes.add(externalNode);
		super.markAsChanged();
	}

	public void removeExternalNode(final AbstractNode externalNode) {
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
	public Set getPhysicalLinksAt(final AbstractNode node) {
		HashSet returnLinks = new HashSet();
		Iterator e = this.getAllPhysicalLinks().iterator();

		while (e.hasNext()) {
			PhysicalLink link = (PhysicalLink) e.next();
			if ((link.getEndNode().equals(node)) || (link.getStartNode().equals(node)))
				returnLinks.add(link);
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

		Collector coll = getCollector(physicalLink);
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
	public PhysicalLink getPhysicalLink(final AbstractNode startNode, final AbstractNode endNode) {
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
		Iterator e = this.getAllNodeLinks().iterator();

		while (e.hasNext()) {
			NodeLink nodeLink = (NodeLink) e.next();
			if (nodeLink.getId().equals(nodeLinkId)) {
				return nodeLink;
			}
		}
		return null;
	}

	public List getNodeLinks(final PhysicalLink link) {
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
	public NodeLink getNodeLink(final AbstractNode node) {
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
	public NodeLink getNodeLink(final AbstractNode startNode, final AbstractNode endNode) {
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
	public List getAllElements() {
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
	public void setSelected(final MapElement me, final boolean selected) {
		me.setSelected(selected);
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

	public static Map createInstance(final Identifier creatorId, final Identifier domainId, final java.util.Map exportMap1)
			throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);

		if (id1 == null || name1 == null || description1 == null || creatorId == null || domainId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Map map = new Map(id1, creatorId, 0L, domainId, name1, description1);
			assert map.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			map.markAsChanged();

			return map;
		}
		catch (Exception e) {
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}

	/**
	 * Получить список фрагментов линий, содержащих заданный узел.
	 * @param node узел
	 * @return Список фрагментов
	 */
	public Set getNodeLinks(final AbstractNode node)
	{
		Set returnNodeLinks = new HashSet();
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			
			if ( (nodeLink.getEndNode().equals(node))
				|| (nodeLink.getStartNode().equals(node)))
			{
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
	public NodeLink getOtherNodeLink(final AbstractNode node, NodeLink nodeLink)
	{
		if(!node.getClass().equals(TopologicalNode.class))
		{
			return null;
		}
	
		NodeLink startNodeLink = null;
		for(Iterator it = getNodeLinks(node).iterator(); it.hasNext();)
			{
				NodeLink nl = (NodeLink )it.next();
				if(nodeLink != nl)
				{
					startNodeLink = nl;
					break;
				}
			}
			
		return startNodeLink;
	}

	/**
	 * Получить вектор узлов на противоположных концах всех фрагментов линий
	 * данного элемента.
	 * @param node узел
	 * @return список узлов
	 */
	public Set getOppositeNodes(final AbstractNode node)
	{
		Iterator e = getNodeLinks(node).iterator();
		Set returnNodes = new HashSet();
	
		while (e.hasNext())
		{
			NodeLink nodeLink = (NodeLink )e.next();
	
			if ( nodeLink.getEndNode().equals(node) )
				returnNodes.add(nodeLink.getStartNode());
			else
				returnNodes.add(nodeLink.getEndNode());
		}
	
		return returnNodes;
	}

	/**
	 * Получить список линий, начинающихся или заканчивающихся
	 * на данном узле.
	 * @return список линий
	 */
	public Set getPhysicalLinks(final AbstractNode node)
	{
		Set returnLinks = new HashSet();

		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();)
		{
			PhysicalLink physicalLink = (PhysicalLink )it.next();
			
			if ( (physicalLink.getEndNode().equals(node))
					|| (physicalLink.getStartNode().equals(node)) )
				returnLinks.add(physicalLink);
		}

		return returnLinks;
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.Map xmlMap = com.syrus.amficom.map.xml.Map.Factory.newInstance();
		fillXMLTransferable(xmlMap);
		return xmlMap;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map )xmlObject; 

		com.syrus.amficom.general.xml.UID uid = xmlMap.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlMap.setName(this.name);
		xmlMap.setDescription(this.description);

		com.syrus.amficom.map.xml.TopologicalNodes xmlTopologicalNodes = xmlMap.addNewTopologicalnodes();
		com.syrus.amficom.map.xml.SiteNodes xmlSiteNodes = xmlMap.addNewSitenodes();
		com.syrus.amficom.map.xml.PhysicalLinks xmlPhysicalLinks = xmlMap.addNewPhysicallinks();
		com.syrus.amficom.map.xml.NodeLinks xmlNodeLinks = xmlMap.addNewNodelinks();
		com.syrus.amficom.map.xml.Collectors xmlCollectors = xmlMap.addNewCollectors();

		Collection xmlTopologicalNodesArray = new LinkedList();
		for(Iterator it = getTopologicalNodes().iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			xmlTopologicalNodesArray.add(topologicalNode.getXMLTransferable());
//			com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = xmlTopologicalNodes.addNewTopologicalnode();
//			topologicalNode.fillXMLTransferable(xmlTopologicalNode);
		}
		xmlTopologicalNodes.setTopologicalnodeArray(
				(com.syrus.amficom.map.xml.TopologicalNode[] )
				xmlTopologicalNodesArray.toArray(
						new com.syrus.amficom.map.xml.TopologicalNode[xmlTopologicalNodesArray.size()]));

		Collection xmlSiteNodesArray = new LinkedList();
		for(Iterator it = getSiteNodes().iterator(); it.hasNext();) {
			SiteNode siteNode = (SiteNode )it.next();
			xmlSiteNodesArray.add(siteNode.getXMLTransferable());
//			com.syrus.amficom.map.xml.SiteNode xmlSiteNode = xmlSiteNodes.addNewSitenode();
//			siteNode.fillXMLTransferable(xmlSiteNode);
		}
		xmlSiteNodes.setSitenodeArray(
				(com.syrus.amficom.map.xml.SiteNode[] )
				xmlSiteNodesArray.toArray(
						new com.syrus.amficom.map.xml.SiteNode[xmlSiteNodesArray.size()]));

		Collection xmlPhysicalLinksArray = new LinkedList();
		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink physicalLink = (PhysicalLink )it.next();
			xmlPhysicalLinksArray.add(physicalLink.getXMLTransferable());
//			com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = xmlPhysicalLinks.addNewPhysicallink();
//			physicalLink.fillXMLTransferable(xmlPhysicalLink);
		}
		xmlPhysicalLinks.setPhysicallinkArray(
				(com.syrus.amficom.map.xml.PhysicalLink[] )
				xmlPhysicalLinksArray.toArray(
						new com.syrus.amficom.map.xml.PhysicalLink[xmlPhysicalLinksArray.size()]));

		Collection xmlNodeLinksArray = new LinkedList();
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink )it.next();
			xmlNodeLinksArray.add(nodeLink.getXMLTransferable());
//			com.syrus.amficom.map.xml.NodeLink xmlNodeLink = xmlNodeLinks.addNewNodelink();
//			nodeLink.fillXMLTransferable(xmlNodeLink);
		}
		xmlNodeLinks.setNodelinkArray(
				(com.syrus.amficom.map.xml.NodeLink[] )
				xmlNodeLinksArray.toArray(
						new com.syrus.amficom.map.xml.NodeLink[xmlNodeLinksArray.size()]));

		Collection xmlCollectorsArray = new LinkedList();
		for(Iterator it = getCollectors().iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			xmlCollectorsArray.add(collector.getXMLTransferable());
//			com.syrus.amficom.map.xml.Collector xmlCollector = xmlCollectors.addNewCollector();
//			collector.fillXMLTransferable(xmlCollector);
		}
		xmlCollectors.setCollectorArray(
				(com.syrus.amficom.map.xml.Collector[] )
				xmlCollectorsArray.toArray(
						new com.syrus.amficom.map.xml.Collector[xmlCollectorsArray.size()]));
	}

	Map(
			Identifier creatorId, 
			Identifier domainId, 
			com.syrus.amficom.map.xml.Map xmlMap, 
			ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.MAP_ENTITY_CODE, 
						xmlMap.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0,
				domainId);
		this.fromXMLTransferable(xmlMap, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map )xmlObject; 
		
		this.name = xmlMap.getName();
		this.description = xmlMap.getDescription();

		this.siteNodes = new HashSet();
		this.topologicalNodes = new HashSet();
		this.nodeLinks = new HashSet();
		this.physicalLinks = new HashSet();
		this.marks = new HashSet();
		this.collectors = new HashSet();

		this.maps = new HashSet();
		this.selectedElements = new HashSet();
		this.allElements = new LinkedList();
		this.nodeElements = new HashSet();
		this.externalNodes = new HashSet();
		
		com.syrus.amficom.map.xml.TopologicalNode[] xmlTopologicalNodesArray = 
			xmlMap.getTopologicalnodes().getTopologicalnodeArray();
		for(int i = 0; i < xmlTopologicalNodesArray.length; i++) {
			com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = 
				xmlTopologicalNodesArray[i];
			this.addNode(TopologicalNode.createInstance(this.creatorId, xmlTopologicalNode, clonedIdsPool));
		}

		com.syrus.amficom.map.xml.SiteNode[] xmlSiteNodesArray = 
			xmlMap.getSitenodes().getSitenodeArray();
		for(int i = 0; i < xmlSiteNodesArray.length; i++) {
			com.syrus.amficom.map.xml.SiteNode xmlSiteNode = 
				xmlSiteNodesArray[i];
			this.addNode(SiteNode.createInstance(this.creatorId, xmlSiteNode, clonedIdsPool));
		}

		com.syrus.amficom.map.xml.PhysicalLink[] xmlPhysicalLinksArray = 
			xmlMap.getPhysicallinks().getPhysicallinkArray();
		for(int i = 0; i < xmlPhysicalLinksArray.length; i++) {
			com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = 
				xmlPhysicalLinksArray[i];
			this.addPhysicalLink(PhysicalLink.createInstance(this.creatorId, xmlPhysicalLink, clonedIdsPool));
		}

		com.syrus.amficom.map.xml.NodeLink[] xmlNodeLinksArray = 
			xmlMap.getNodelinks().getNodelinkArray();
		for(int i = 0; i < xmlNodeLinksArray.length; i++) {
			com.syrus.amficom.map.xml.NodeLink xmlNodeLink = 
				xmlNodeLinksArray[i];
			this.addNodeLink(NodeLink.createInstance(this.creatorId, xmlNodeLink, clonedIdsPool));
		}

		com.syrus.amficom.map.xml.Collector[] xmlCollectorsArray = 
			xmlMap.getCollectors().getCollectorArray();
		for(int i = 0; i < xmlCollectorsArray.length; i++) {
			com.syrus.amficom.map.xml.Collector xmlCollector = 
				xmlCollectorsArray[i];
			this.addCollector(Collector.createInstance(this.creatorId, xmlCollector, clonedIdsPool));
		}
	}

	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		com.syrus.amficom.map.xml.Map xmlMap = (com.syrus.amficom.map.xml.Map )xmlObject;

		try {
			Map map = new Map(creatorId, domainId, xmlMap, clonedIdsPool);
			assert map.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			map.markAsChanged();

			return map;
		}
		catch (Exception e) {
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}

}

