/*-
 * $Id: Map.java,v 1.87 2005/09/07 12:19:12 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.corba.IdlMap;
import com.syrus.AMFICOM.map.corba.IdlMapHelper;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.AMFICOM.map.xml.XmlCollectorSeq;
import com.syrus.AMFICOM.map.xml.XmlMap;
import com.syrus.AMFICOM.map.xml.XmlMapLibraryEntrySeq;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;
import com.syrus.AMFICOM.map.xml.XmlNodeLinkSeq;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLink;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNode;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeSeq;
import com.syrus.AMFICOM.map.xml.XmlTopologicalNode;
import com.syrus.AMFICOM.map.xml.XmlTopologicalNodeSeq;
import com.syrus.util.Log;

/**
 * �������������� �����, ������� �������� � ���� ����� ��������� ���� � ������
 * ����� (������� � ��������������), ����� (��������� �� ����������), ����� ��
 * ������, ����������� (������������ � ���� �����).
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.87 $, $Date: 2005/09/07 12:19:12 $
 * @module map
 */
public final class Map extends DomainMember implements Namable, XmlBeansTransferable<XmlMap> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722862181200184L;

	private String name;
	private String description;

	private Set<Identifier> siteNodeIds;
	private Set<Identifier> topologicalNodeIds;
	private Set<Identifier> nodeLinkIds;
	private Set<Identifier> physicalLinkIds;
	private Set<Identifier> markIds;
	private Set<Identifier> collectorIds;

	private Set<Identifier> mapIds;
	private Set<Identifier> externalNodeIds;
	private Set<Identifier> mapLibraryIds;

	private transient Set<SiteNode> siteNodes = new HashSet<SiteNode>();
	private transient Set<TopologicalNode> topologicalNodes = new HashSet<TopologicalNode>();
	private transient Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
	private transient Set<PhysicalLink> physicalLinks = new HashSet<PhysicalLink>();
	private transient Set<Mark> marks = new HashSet<Mark>();
	private transient Set<Collector> collectors = new HashSet<Collector>();

	private transient Set<Map> maps = new HashSet<Map>();
	private transient Set<SiteNode> externalNodes = new HashSet<SiteNode>();
	private transient Set<MapLibrary> mapLibrarys = new HashSet<MapLibrary>();

	/**
	 * ������������� ������ ���� ��������� �������������� �����
	 */
	private transient List<MapElement> allElements = new LinkedList<MapElement>();
	private transient Set<AbstractNode> nodeElements = new HashSet<AbstractNode>();
	private transient Set<MapElement> selectedElements = new HashSet<MapElement>();

	Map(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(MAP_CODE).retrieve(this);
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

		this.siteNodeIds = new HashSet<Identifier>();
		this.topologicalNodeIds = new HashSet<Identifier>();
		this.nodeLinkIds = new HashSet<Identifier>();
		this.physicalLinkIds = new HashSet<Identifier>();
		this.markIds = new HashSet<Identifier>();
		this.collectorIds = new HashSet<Identifier>();

		this.mapIds = new HashSet<Identifier>();
		this.mapLibraryIds = new HashSet<Identifier>();
		this.externalNodeIds = new HashSet<Identifier>();
	}

	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description) throws CreateObjectException {
		if (name == null || description == null || creatorId == null || domainId == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final Map map = new Map(IdentifierPool.getGeneratedIdentifier(MAP_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description);

			assert map.isValid() : OBJECT_BADLY_INITIALIZED;

			map.markAsChanged();

			return map;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMap mt = (IdlMap) transferable;
		super.fromTransferable(mt, new Identifier(mt.domainId));

		this.name = mt.name;
		this.description = mt.description;

		this.siteNodeIds = Identifier.fromTransferables(mt.siteNodeIds);
		this.topologicalNodeIds = Identifier.fromTransferables(mt.topologicalNodeIds);
		this.nodeLinkIds = Identifier.fromTransferables(mt.nodeLinkIds);
		this.physicalLinkIds = Identifier.fromTransferables(mt.physicalLinkIds);
		this.markIds = Identifier.fromTransferables(mt.markIds);
		this.collectorIds = Identifier.fromTransferables(mt.collectorIds);
		this.mapIds = Identifier.fromTransferables(mt.mapIds);
		this.externalNodeIds = Identifier.fromTransferables(mt.externalNodeIds);
		this.mapLibraryIds = Identifier.fromTransferables(mt.mapLibraryIds);

		this.siteNodes.addAll(StorableObjectPool.<SiteNode>getStorableObjects(this.siteNodeIds, true));
		this.topologicalNodes.addAll(StorableObjectPool.<TopologicalNode>getStorableObjects(this.topologicalNodeIds, true));
		this.nodeLinks.addAll(StorableObjectPool.<NodeLink>getStorableObjects(this.nodeLinkIds, true));
		this.physicalLinks.addAll(StorableObjectPool.<PhysicalLink>getStorableObjects(this.physicalLinkIds, true));
		this.marks.addAll(StorableObjectPool.<Mark>getStorableObjects(this.markIds, true));
		this.collectors.addAll(StorableObjectPool.<Collector>getStorableObjects(this.collectorIds, true));
		this.maps.addAll(StorableObjectPool.<Map>getStorableObjects(this.mapIds, true));
		this.externalNodes.addAll(StorableObjectPool.<SiteNode>getStorableObjects(this.externalNodeIds, true));
		this.mapLibrarys.addAll(StorableObjectPool.<MapLibrary>getStorableObjects(this.mapLibraryIds, true));
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.siteNodeIds);
		dependencies.addAll(this.topologicalNodeIds);
		dependencies.addAll(this.nodeLinkIds);
		dependencies.addAll(this.physicalLinkIds);
		dependencies.addAll(this.markIds);
		dependencies.addAll(this.collectorIds);
		dependencies.addAll(this.mapIds);
		dependencies.addAll(this.externalNodeIds);
		dependencies.addAll(this.mapLibraryIds);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMap getTransferable(final ORB orb) {
		final IdlIdentifier[] idlSiteNodeIds = Identifier.createTransferables(this.siteNodeIds);
		final IdlIdentifier[] idlTopologicalNodeIds = Identifier.createTransferables(this.topologicalNodeIds);
		final IdlIdentifier[] idlNodeLinkIds = Identifier.createTransferables(this.nodeLinkIds);
		final IdlIdentifier[] idlPhysicalNodeLinkIds = Identifier.createTransferables(this.physicalLinkIds);
		final IdlIdentifier[] idlMarkIds = Identifier.createTransferables(this.markIds);
		final IdlIdentifier[] idlCollectorIds = Identifier.createTransferables(this.collectorIds);
		final IdlIdentifier[] idlMapIds = Identifier.createTransferables(this.mapIds);
		final IdlIdentifier[] idlExternalNodesIds = Identifier.createTransferables(this.externalNodeIds);
		final IdlIdentifier[] idlMapLibraryIds = Identifier.createTransferables(this.mapLibraryIds);
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
				idlSiteNodeIds,
				idlTopologicalNodeIds,
				idlNodeLinkIds,
				idlPhysicalNodeLinkIds,
				idlMarkIds,
				idlCollectorIds,
				idlMapIds,
				idlExternalNodesIds,
				idlMapLibraryIds);
	}

	public Set<Collector> getCollectors() {
		return Collections.unmodifiableSet(this.collectors);
	}

	protected void setCollectors0(final Set<Collector> collectors) {
		this.collectorIds.clear();
		this.collectors.clear();
		if (collectors != null) {
			for (Collector collector : collectors) {
				this.collectorIds.add(collector.getId());
				this.collectors.add(collector);
			}
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

	public Set<MapLibrary> getMapLibraries() {
		return Collections.unmodifiableSet(this.mapLibrarys);
	}

	public void setMapLibraries(final Set<MapLibrary> mapLibraries) {
		this.setMapLibraries0(mapLibraries);
		super.markAsChanged();
	}
	
	protected void setMapLibraries0(final Set<MapLibrary> mapLibraries) {
		this.mapLibraryIds.clear();
		this.mapLibrarys.clear();
		if (mapLibraries != null) {
			for (MapLibrary library : mapLibraries) {
				this.mapLibraryIds.add(library.getId());
				this.mapLibrarys.add(library);
			}
		}
	}

	public Set<Mark> getMarks() {
		return Collections.unmodifiableSet(this.marks);
	}

	protected void setMarks0(final Set<Mark> marks) {
		this.markIds.clear();
		this.marks.clear();
		if (marks != null) {
			for (Mark mark : marks) {
				this.markIds.add(mark.getId());
				this.marks.add(mark);
			}
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
		this.nodeLinkIds.clear();
		this.nodeLinks.clear();
		if (nodeLinks != null) {
			for (NodeLink link : nodeLinks) {
				this.nodeLinkIds.add(link.getId());
				this.nodeLinks.add(link);
			}			
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
		this.physicalLinkIds.clear();
		this.physicalLinks.clear();
		if (physicalLinks != null) {
			for (PhysicalLink link : physicalLinks) {
				this.physicalLinkIds.add(link.getId());
				this.physicalLinks.add(link);
			}
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
		this.siteNodeIds.clear();
		this.siteNodes.clear();
		if (siteNodes != null) {
			for (SiteNode node : siteNodes) {
				this.siteNodeIds.add(node.getId());
				this.siteNodes.add(node);
			}
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
		this.topologicalNodeIds.clear();
		this.topologicalNodes.clear();
		if (topologicalNodes != null) {
			for (TopologicalNode node : topologicalNodes) {
				this.topologicalNodeIds.add(node.getId());
				this.topologicalNodes.add(node);
			}
		}
	}

	public void setTopologicalNodes(final Set<TopologicalNode> topologicalNodes) {
		this.setTopologicalNodes0(topologicalNodes);
		super.markAsChanged();
	}

	public Set<Map> getMaps() {
		return Collections.unmodifiableSet(this.maps);
	}

	protected void setMaps0(final Set<Map> maps) {
		this.mapIds.clear();
		this.maps.clear();
		if (maps != null) {
			for (Map map : maps) {
				this.mapIds.add(map.getId());
				this.maps.add(map);
			}
		}
	}

	public void setMaps(final Set<Map> maps) {
		this.setMaps0(maps);
		super.markAsChanged();
	}

	public Set<SiteNode> getExternalNodes() {
		return Collections.unmodifiableSet(this.externalNodes);
	}

	protected void setExternalNodes0(final Set<SiteNode> externalNodes) {
		this.externalNodeIds.clear();
		this.externalNodes.clear();
		if (externalNodes != null) {
			for (SiteNode node : externalNodes) {
				this.externalNodeIds.add(node.getId());
				this.externalNodes.add(node);
			}
		}
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
		returnElements.addAll(this.getCollectors());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllCollectors());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	public Set<Mark> getAllMarks() {
		final Set<Mark> returnElements = new HashSet<Mark>();
		returnElements.addAll(this.getMarks());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllMarks());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	public Set<NodeLink> getAllNodeLinks() {
		final Set<NodeLink> returnElements = new HashSet<NodeLink>();
		returnElements.addAll(this.getNodeLinks());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllNodeLinks());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	public Set<PhysicalLink> getAllPhysicalLinks() {
		final Set<PhysicalLink> returnElements = new HashSet<PhysicalLink>();
		returnElements.addAll(this.getPhysicalLinks());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllPhysicalLinks());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	public Set<SiteNode> getAllSiteNodes() {
		final Set<SiteNode> returnElements = new HashSet<SiteNode>();
		returnElements.addAll(this.getSiteNodes());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllSiteNodes());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	public Set<TopologicalNode> getAllTopologicalNodes() {
		final Set<TopologicalNode> returnElements = new HashSet<TopologicalNode>();
		returnElements.addAll(this.getTopologicalNodes());
		for (final Map innerMap : this.getMaps()) {
			returnElements.addAll(innerMap.getAllTopologicalNodes());
		}
		return Collections.unmodifiableSet(returnElements);
	}

	/**
	 * �������� ������ ���� ������� ��������� �������������� �����.
	 *
	 * @return ������ �����
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
	 * �������� ����� ����.
	 *
	 * @param node
	 *          ����
	 */
	public void addNode(final AbstractNode node) {
		if (node instanceof SiteNode) {
			this.siteNodeIds.add(node.getId());
			this.siteNodes.add((SiteNode)node);
		} else if (node instanceof TopologicalNode) {
			this.topologicalNodeIds.add(node.getId());
			this.topologicalNodes.add((TopologicalNode) node);
		} else if (node instanceof Mark) {
			this.markIds.add(node.getId());
			this.marks.add((Mark) node);
		}
		node.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * ������� ����.
	 *
	 * @param node
	 *          ����
	 */
	public void removeNode(final AbstractNode node) {
		node.setSelected(false);
		this.selectedElements.remove(node);
		if (node instanceof SiteNode) {
			this.siteNodeIds.remove(node.getId());
			this.siteNodes.remove(node);
			this.externalNodeIds.remove(node.getId());
			this.externalNodes.remove(node);
		} else if (node instanceof TopologicalNode) {
			this.topologicalNodeIds.remove(node.getId());
			this.topologicalNodes.remove(node);
		} else if (node instanceof Mark) {
			this.markIds.remove(node.getId());
			this.marks.remove(node);
		}
		node.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * �������� ������� �������� ���� �� ��������������.
	 *
	 * @param siteId
	 *          ������������� �������� ����
	 * @return ������� ���� ��� null, ���� ���� �� ������
	 */
	public SiteNode getSiteNode(final Identifier siteId) {
		for (final SiteNode siteNode : this.getAllSiteNodes()) {
			if (siteNode.getId().equals(siteId)) {
				return siteNode;
			}
		}
		return null;
	}

	/**
	 * �������� ������� ��������������� ���� �� ��������������.
	 *
	 * @param topologicalNodeId
	 *          ������������� ��������������� ����
	 * @return �������������� ���� ��� null, ���� ���� �� ������
	 */
	public TopologicalNode getTopologicalNode(final Identifier topologicalNodeId) {
		for (final TopologicalNode topologicalNode : this.getAllTopologicalNodes()) {
			if (topologicalNode.getId().equals(topologicalNodeId)) {
				return topologicalNode;
			}
		}
		return null;
	}

	/**
	 * �������� ����� �� ��������������.
	 *
	 * @param markId
	 *          ������������� �����
	 * @return ����� ��� null, ���� ����� �� ������
	 */
	public Mark getMark(final Identifier markId) {
		for (final Mark mark : this.getAllMarks()) {
			if (mark.getId().equals(markId)) {
				return mark;
			}
		}
		return null;
	}

	/**
	 * �������� ���� �� ��������������.
	 *
	 * @param nodeId
	 *          ������������� �����
	 * @return ���� ��� null, ���� ���� �� ������
	 */
	public AbstractNode getNode(final Identifier nodeId) {
		AbstractNode node = this.getSiteNode(nodeId);
		if (node == null) {
			node = this.getTopologicalNode(nodeId);
		}
		if (node == null) {
			node = this.getMark(nodeId);
		}
		return null;
	}

	public void addMapLibrary(final MapLibrary mapLibrary) {
		this.mapLibraryIds.add(mapLibrary.getId());
		this.mapLibrarys.add(mapLibrary);
		super.markAsChanged();
	}

	public void removeMapLibrary(final MapLibrary mapLibrary) {
		this.mapLibraryIds.remove(mapLibrary.getId());
		this.mapLibrarys.remove(mapLibrary);
		super.markAsChanged();
	}

	public void addMap(final Map map) {
		this.mapIds.add(map.getId());
		this.maps.add(map);
		super.markAsChanged();
	}

	public void removeMap(final Map map) {
		this.mapIds.remove(map.getId());
		this.maps.remove(map);
		super.markAsChanged();
	}

	public void addExternalNode(final SiteNode externalNode) {
		this.externalNodeIds.add(externalNode.getId());
		this.externalNodes.add(externalNode);
		super.markAsChanged();
	}

	/**
	 * �������� ����� ���������.
	 *
	 * @param collector
	 *          ����� ���������
	 */
	public void addCollector(final Collector collector) {
		this.collectorIds.add(collector.getId());
		this.collectors.add(collector);
		collector.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * ������� ���������.
	 *
	 * @param collector
	 *          ���������
	 */
	public void removeCollector(final Collector collector) {
		collector.setSelected(false);
		this.selectedElements.remove(collector);
		this.collectorIds.remove(collector.getId());
		this.collectors.remove(collector);
		collector.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * �������� ���������, � ������� �������� ���� �������� �����.
	 *
	 * @param physicalLink
	 *          �����
	 * @return ���������
	 */
	public Collector getCollector(final PhysicalLink physicalLink) {
		for (final Collector collector : this.getAllCollectors()) {
			if (collector.getPhysicalLinks().contains(physicalLink)) {
				return collector;
			}
		}
		return null;
	}

	/**
	 * �������� ������ �����, ������������ ��� ��������������� � �������� ����.
	 *
	 * @param node
	 *          ����
	 * @return ������ �����
	 */
	public Set<PhysicalLink> getPhysicalLinksAt(final AbstractNode node) {
		final HashSet<PhysicalLink> returnLinks = new HashSet<PhysicalLink>();
		for (final PhysicalLink physicalLink : this.getAllPhysicalLinks()) {
			if ((physicalLink.getEndNode().equals(node)) || (physicalLink.getStartNode().equals(node))) {
				returnLinks.add(physicalLink);
			}
		}
		return returnLinks;
	}

	/**
	 * �������� ����� �����.
	 *
	 * @param physicalLink
	 *          �����
	 */
	public void addPhysicalLink(final PhysicalLink physicalLink) {
		this.physicalLinkIds.add(physicalLink.getId());
		this.physicalLinks.add(physicalLink);
		physicalLink.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * ������� �����.
	 *
	 * @param physicalLink
	 *          �����
	 */
	public void removePhysicalLink(final PhysicalLink physicalLink) {
		physicalLink.setSelected(false);
		this.selectedElements.remove(physicalLink);
		this.physicalLinkIds.remove(physicalLink.getId());
		this.physicalLinks.remove(physicalLink);
		physicalLink.setRemoved(true);

		final Collector coll = this.getCollector(physicalLink);
		if (coll != null) {
			coll.removePhysicalLink(physicalLink);
		}
		super.markAsChanged();
	}

	/**
	 * �������� ����� �� �� ��������������.
	 *
	 * @param phisicalLinkId
	 *          ������������� �����
	 * @return �����
	 */
	public PhysicalLink getPhysicalLink(final Identifier phisicalLinkId) {
		for (final PhysicalLink physicalLink : this.getAllPhysicalLinks()) {
			if (physicalLink.getId().equals(phisicalLinkId)) {
				return physicalLink;
			}
		}
		return null;
	}

	/**
	 * �������� ����� �� �������� �����.
	 *
	 * @param startNode
	 *          ���� �������� ����
	 * @param endNode
	 *          ������ �������� ����
	 * @return �����
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
	 * �������� ����� �������� �����.
	 *
	 * @param nodeLink
	 *          �������� �����
	 */
	public void addNodeLink(final NodeLink nodeLink) {
		this.nodeLinkIds.add(nodeLink.getId());
		this.nodeLinks.add(nodeLink);
		nodeLink.setRemoved(false);
		super.markAsChanged();
	}

	/**
	 * ������� �������� �����.
	 *
	 * @param nodeLink
	 *          �������� �����
	 */
	public void removeNodeLink(final NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.selectedElements.remove(nodeLink);
		this.nodeLinkIds.remove(nodeLink.getId());
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * �������� �������� ����� �� ��������������.
	 *
	 * @param nodeLinkId
	 *          ������������� ��������� �����
	 * @return �������� �����
	 */
	public NodeLink getNodeLink(final Identifier nodeLinkId) {
		for (final NodeLink nodeLink : this.getAllNodeLinks()) {
			if (nodeLink.getId().equals(nodeLinkId)) {
				return nodeLink;
			}
		}
		return null;
	}

	/**
	 * �������� �������� ����� �� ��������� ����.
	 *
	 * @param node
	 *          �������� ����
	 * @return �������� �����
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
	 * �������� �������� ����� �� �������� �����.
	 *
	 * @param startNode
	 *          ���� �������� ����
	 * @param endNode
	 *          ������ �������� ����
	 * @return �������� �����
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
	 * �������� ������ ���� �������������� ��������� ����� ({@link MapElement}).
	 *
	 * @return ������ ���� ���������
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
	 * �������� ����� ���� ���������� ��������� �������������� �����.
	 * ���������� �������� - ��, ��� �������  �����
	 * <code>{@link MapElement#isSelected()}</code> ���������� <code>true</code>.
	 * @return ����� ���������� ���������
	 */
	public Set<MapElement> getSelectedElements() {
		return Collections.unmodifiableSet(this.selectedElements);
	}

	/**
	 * �������� ����� ���������� ��������� �������������� �����.
	 */
	public void clearSelection() {
		this.selectedElements.clear();
	}

	/**
	 * ���������������� ��������� ����� ��������� �������� �������������� �����.
	 *
	 * @param me
	 *          �������
	 * @param selected
	 *          ���� ���������
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
	 * �������� ������ ���������� �����, ���������� �������� ����.
	 * @param node ����
	 * @return ������ ����������
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
	 * ���������� �������� �����, ���������� ������ ����, �� �� ������
	 * ����������� � ���������. ���� �������� � � �������� � ����� �����
	 * ����� �, �� ����� ������ <code>�.getOtherNodeLink(�)</code> ������ �, � �����
	 * <code>�.getOtherNodeLink(�)</code> ������ �. ����� �������, ��� ���������������
	 * ���� ���������� ������������ ���������������,
	 * ��� �������� ���� �� ����� ���� ���������, �� ���� ������� �����
	 * �� ������ �������������� � ���������� null
	 * @param node ����
	 * @param nodeLink �������� �����
	 * @return ������ �������� �����
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
	 * �������� ������ ����� �� ��������������� ������ ���� ���������� �����
	 * ������� ��������.
	 * 
	 * @param node
	 *        ����
	 * @return ������ �����
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
	 * �������� ������ �����, ������������ ��� ��������������� �� ������ ����.
	 * 
	 * @return ������ �����
	 */
	public Set<PhysicalLink> getPhysicalLinks(final AbstractNode node) {
		final Set<PhysicalLink> returnLinks = new HashSet<PhysicalLink>();
		for (final PhysicalLink physicalLink : this.getPhysicalLinks()) {
			if ((physicalLink.getEndNode().equals(node)) || (physicalLink.getStartNode().equals(node))) {
				returnLinks.add(physicalLink);
			}
		}

		return returnLinks;
	}

	public XmlMap getXmlTransferable(final String importType) {
		final XmlMap xmlMap = XmlMap.Factory.newInstance();
		xmlMap.setId(this.id.getXmlTransferable(importType));
		xmlMap.setName(this.name);
		xmlMap.setDescription(this.description);

		final XmlTopologicalNodeSeq xmlTopologicalNodes = xmlMap.addNewTopologicalNodes();
		final XmlSiteNodeSeq xmlSiteNodes = xmlMap.addNewSiteNodes();
		final XmlPhysicalLinkSeq xmlPhysicalLinks = xmlMap.addNewPhysicalLinks();
		final XmlNodeLinkSeq xmlNodeLinks = xmlMap.addNewNodeLinks();
		final XmlCollectorSeq xmlCollectors = xmlMap.addNewCollectors();

		final Collection<XmlObject> xmlTopologicalNodesArray = new LinkedList<XmlObject>();
		for (final TopologicalNode topologicalNode : this.getTopologicalNodes()) {
			xmlTopologicalNodesArray.add(topologicalNode.getXmlTransferable(importType));
		}
		xmlTopologicalNodes.setTopologicalNodeArray(xmlTopologicalNodesArray.toArray(new XmlTopologicalNode[xmlTopologicalNodesArray.size()]));

		final Collection<XmlObject> xmlSiteNodesArray = new LinkedList<XmlObject>();
		for (final SiteNode siteNode : this.getSiteNodes()) {
			xmlSiteNodesArray.add(siteNode.getXmlTransferable(importType));
		}
		xmlSiteNodes.setSiteNodeArray(xmlSiteNodesArray.toArray(new XmlSiteNode[xmlSiteNodesArray.size()]));

		final Collection<XmlObject> xmlPhysicalLinksArray = new LinkedList<XmlObject>();
		for (final PhysicalLink physicalLink : this.getPhysicalLinks()) {
			xmlPhysicalLinksArray.add(physicalLink.getXmlTransferable(importType));
		}
		xmlPhysicalLinks.setPhysicalLinkArray(xmlPhysicalLinksArray.toArray(new XmlPhysicalLink[xmlPhysicalLinksArray.size()]));

		final Collection<XmlObject> xmlNodeLinksArray = new LinkedList<XmlObject>();
		for (final NodeLink nodeLink : this.getNodeLinks()) {
			xmlNodeLinksArray.add(nodeLink.getXmlTransferable(importType));
		}
		xmlNodeLinks.setNodeLinkArray(xmlNodeLinksArray.toArray(new XmlNodeLink[xmlNodeLinksArray.size()]));

		final Collection<XmlObject> xmlCollectorsArray = new LinkedList<XmlObject>();
		for (final Collector collector : this.getCollectors()) {
			xmlCollectorsArray.add(collector.getXmlTransferable(importType));
		}
		xmlCollectors.setCollectorArray(xmlCollectorsArray.toArray(new XmlCollector[xmlCollectorsArray.size()]));

		xmlMap.setImportType("amficom");

		return xmlMap;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 * @param domainId
	 */
	private Map(final Identifier id,
			final Date created,
			final Identifier creatorId,
			final Identifier domainId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial(),
				domainId);
	}

	public void fromXmlTransferable(final XmlMap xmlMap, final String importType) throws ApplicationException {
		this.name = xmlMap.getName();
		this.description = xmlMap.getDescription();

		this.siteNodeIds = new HashSet<Identifier>();
		this.topologicalNodeIds = new HashSet<Identifier>();
		this.nodeLinkIds = new HashSet<Identifier>();
		this.physicalLinkIds = new HashSet<Identifier>();
		this.markIds = new HashSet<Identifier>();
		this.collectorIds = new HashSet<Identifier>();

		this.mapIds = new HashSet<Identifier>();
		this.externalNodeIds = new HashSet<Identifier>();
		this.mapLibraryIds = new HashSet<Identifier>();

		final XmlMapLibraryEntrySeq mapLibraryEntries = xmlMap.getMapLibraryEntries();
		if (mapLibraryEntries != null) {
			for (final String xmlMapLibraryEntry : mapLibraryEntries.getMapLibraryEntryArray()) {
				final StorableObjectCondition pTypeCondition = new TypicalCondition(xmlMapLibraryEntry,
						OperationSort.OPERATION_EQUALS,
						MAPLIBRARY_CODE,
						StorableObjectWrapper.COLUMN_CODENAME);

				final Collection<MapLibrary> pTypes = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
				if (pTypes.size() == 0) {
					throw new ApplicationException("Library " + xmlMapLibraryEntry + " does not exist. Cannot proceed with import");
				}
				this.addMapLibrary(pTypes.iterator().next());
			}
		}

		for (final XmlTopologicalNode xmlTopologicalNode : xmlMap.getTopologicalNodes().getTopologicalNodeArray()) {
			this.addNode(TopologicalNode.createInstance(this.creatorId, importType, xmlTopologicalNode));
		}

		for (final XmlSiteNode xmlSiteNode : xmlMap.getSiteNodes().getSiteNodeArray()) {
			this.addNode(SiteNode.createInstance(this.creatorId, importType, xmlSiteNode));
		}

		for (final XmlPhysicalLink xmlPhysicalLink : xmlMap.getPhysicalLinks().getPhysicalLinkArray()) {
			this.addPhysicalLink(PhysicalLink.createInstance(this.creatorId, importType, xmlPhysicalLink));
		}

		for (final XmlNodeLink xmlNodeLink : xmlMap.getNodeLinks().getNodeLinkArray()) {
			this.addNodeLink(NodeLink.createInstance(this.creatorId, importType, xmlNodeLink));
		}

		for (final XmlCollector xmlCollector : xmlMap.getCollectors().getCollectorArray()) {
			this.addCollector(Collector.createInstance(this.creatorId, importType, xmlCollector));
		}
	}

	/**
	 * @param creatorId
	 * @param domainId
	 * @param importType
	 * @param xmlMap
	 * @throws CreateObjectException
	 */
	public static Map createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String importType,
			final XmlMap xmlMap) throws CreateObjectException {
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlMap.getId(), MAP_CODE, importType);
			Map map = StorableObjectPool.getStorableObject(id, true);
			if (map == null) {
				map = new Map(id, new Date(), creatorId, domainId);
			}
			map.fromXmlTransferable(xmlMap, importType);
			assert map.isValid() : OBJECT_BADLY_INITIALIZED;
			map.markAsChanged();
			return map;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}
}
