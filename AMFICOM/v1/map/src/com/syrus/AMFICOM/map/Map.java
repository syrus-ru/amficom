/**
 * $Id: Map.java,v 1.17 2005/02/02 15:17:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
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
 * Топологическая схема, которая содержит в себе набор связанных друг с другом
 * узлов (сетевых и топологических), линий (состоящих из фрагментов), меток на 
 * линиях, коллекторов (объединяющих в себе линии).
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/02/02 15:17:13 $
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
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
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
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		super.currentVersion = super.getNextVersion();
	}	
	
	public Identifier getDomainId() {
		return this.domainId;
	}
	
	protected void setDomainId0(Identifier domainId) {
		this.domainId = domainId;
	}
	
	public void setDomainId(Identifier domainId) {
		this.setDomainId0(domainId);
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
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
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

	/**
	 * Получить список всех узловых элементов топологической схемы.
	 * @return список узлов
	 */
	public List getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.siteNodes);
		this.nodeElements.addAll(this.topologicalNodes);
		this.nodeElements.addAll(this.marks);
		return this.nodeElements;
	}

	/**
	 * Добавить новый узел.
	 * @param node узел
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
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить узел.
	 * @param node узел
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
	 * Получить элемент сетевого узла по идентификатору.
	 * @param siteId идентификатор сетевого узла
	 * @return сетевой узел или null, если узел не найден
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
	 * Получить элемент топологического узла по идентификатору.
	 * @param topologicalNodeId идентификатор топологического узла
	 * @return топологический узел или null, если узел не найден
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
	 * Получить метку по идентификатору.
	 * @param markId идентификатор метки
	 * @return метка или null, если метка не найден
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
	 * Получить узел по идентификатору.
	 * @param nodeId идентификатор метки
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

	/**
	 * Добавить новый коллектор.
	 * @param collector новый коллектор
	 */
	public void addCollector(Collector collector) {
		this.collectors.add(collector);
		collector.setMap(this);
		collector.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить коллектор.
	 * @param collector коллектор
	 */
	public void removeCollector(Collector collector) {
		collector.setSelected(false);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получить коллектор, в составе которого есть заданная линия.
	 * @param physicalLink линия
	 * @return коллектор
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
	 * Получить список линий, начинающихся или заканчивающихся в заданном узле.
	 * 
	 * @param node узел
	 * @return список линий
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
	 * Добавить новую линию.
	 * @param physicalLink линия
	 */
	public void addPhysicalLink(PhysicalLink physicalLink) {

		this.physicalLinks.add(physicalLink);
		physicalLink.setMap(this);
		physicalLink.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить линию.
	 * @param physicalLink линия
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
	 * Получить линию по ее идентификатору.
	 * @param phisicalLinkId идентификатор линии
	 * @return лниия
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
	 * Получить линию по концевым узлам.
	 * @param startNode один концевой узел
	 * @param endNode другой концевой узел
	 * @return линия
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
	 * добавить новый фрагмент линии.
	 * @param nodeLink фрагмент линии
	 */
	public void addNodeLink(NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
		nodeLink.setMap(this);
		nodeLink.setRemoved(false);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Удалить фрагмент линии.
	 * @param nodeLink фрагмент линии
	 */
	public void removeNodeLink(NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Получить фрагмент линии по идентификатору.
	 * @param nodeLinkId идентификатор фрагмента линии
	 * @return фрагмент линии
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
	 * Получить фрагмент линии по концевым узлам.
	 * @param startNode один концевой узел
	 * @param endNode другой концевой узел
	 * @return фрагмент линии
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
	 * Получить список всех топологических элементов карты ({@link MapElement}).
	 * @return список всех элементов
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
	 * @param me элемент
	 * @param selected флаг выделения
	 */
	public void setSelected(MapElement me, boolean selected) {
		if (selected)
			this.selectedElements.add(me);
		else
			this.selectedElements.remove(me);
	}

	/**
	 * Возвращает описывающий элемент набор параметров,
	 * который используется для экспорта.
	 * @return хэш-таблица параметров элемента
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
				domainId,
				name,
				description);
		} catch (Exception e) 
		{
			throw new CreateObjectException("Map.createInstance |  ", e);
		}
	}
}

