/*-
 * $Id: Map.java,v 1.133 2006/06/23 13:36:42 stas Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
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
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * Топологическая схема, которая содержит в себе набор связанных друг с другом
 * узлов (сетевых и топологических), линий (состоящих из фрагментов), меток на
 * линиях, коллекторов (объединяющих в себе линии).
 *
 * @author $Author: stas $
 * @version $Revision: 1.133 $, $Date: 2006/06/23 13:36:42 $
 * @module map
 */
public final class Map extends DomainMember
		implements Namable, XmlTransferableObject<XmlMap>,
		IdlTransferableObjectExt<IdlMap> {

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

	private transient Set<SiteNode> siteNodes;
	private transient Set<TopologicalNode> topologicalNodes;
	private transient Set<NodeLink> nodeLinks;
	private transient Set<PhysicalLink> physicalLinks;
	private transient Set<Mark> marks;
	private transient Set<Collector> collectors;

	private transient Set<Map> maps;
	private transient Set<SiteNode> externalNodes;
	private transient Set<MapLibrary> mapLibrarys;

	private transient Set<AbstractNode> nodeElements;
	private transient Set<MapElement> selectedElements;

	private transient boolean transientFieldsInitialized = false;
	
	private void initialize() {
		if(this.nodeElements == null) {
			this.nodeElements = new HashSet<AbstractNode>();
		}
		if(this.selectedElements == null) {
			this.selectedElements = new HashSet<MapElement>();
		}
		if(!this.transientFieldsInitialized) {
			this.siteNodes = new HashSet<SiteNode>();
			this.topologicalNodes = new HashSet<TopologicalNode>();
			this.physicalLinks = new HashSet<PhysicalLink>();
			this.nodeLinks = new HashSet<NodeLink>();
			this.marks = new HashSet<Mark>();
			this.collectors = new HashSet<Collector>();
	
			this.maps = new HashSet<Map>();
			this.externalNodes = new HashSet<SiteNode>();
			this.mapLibrarys = new HashSet<MapLibrary>();
	
			try {
//				preloadCharacteristics();
				
				this.siteNodes.addAll(StorableObjectPool.<SiteNode>getStorableObjects(this.siteNodeIds, true));
				this.topologicalNodes.addAll(StorableObjectPool.<TopologicalNode>getStorableObjects(this.topologicalNodeIds, true));
				this.physicalLinks.addAll(StorableObjectPool.<PhysicalLink>getStorableObjects(this.physicalLinkIds, true));
				this.nodeLinks.addAll(StorableObjectPool.<NodeLink>getStorableObjects(this.nodeLinkIds, true));
				
				final HashSet<NodeLink> temp = new HashSet<NodeLink>(this.nodeLinks);
				for (PhysicalLink link : this.physicalLinks) {
					link.init(temp);
				}
				
				this.marks.addAll(StorableObjectPool.<Mark>getStorableObjects(this.markIds, true));
				this.collectors.addAll(StorableObjectPool.<Collector>getStorableObjects(this.collectorIds, true));
				this.maps.addAll(StorableObjectPool.<Map>getStorableObjects(this.mapIds, true));
				this.externalNodes.addAll(StorableObjectPool.<SiteNode>getStorableObjects(this.externalNodeIds, true));
				this.mapLibrarys.addAll(StorableObjectPool.<MapLibrary>getStorableObjects(this.mapLibraryIds, true));

//				for(NodeLink nodeLink : this.nodeLinks) {
//					nodeLink.getPhysicalLink().addNodeLink(nodeLink);
//				}
				
				this.transientFieldsInitialized = true;
			} catch(ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Map(final IdlMap mt) throws CreateObjectException {
		this.siteNodeIds = new HashSet<Identifier>();
		this.topologicalNodeIds = new HashSet<Identifier>();
		this.nodeLinkIds = new HashSet<Identifier>();
		this.physicalLinkIds = new HashSet<Identifier>();
		this.markIds = new HashSet<Identifier>();
		this.collectorIds = new HashSet<Identifier>();

		this.mapIds = new HashSet<Identifier>();
		this.mapLibraryIds = new HashSet<Identifier>();
		this.externalNodeIds = new HashSet<Identifier>();

		try {
			this.fromIdlTransferable(mt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
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
					INITIAL_VERSION,
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

	public synchronized void fromIdlTransferable(final IdlMap mt)
	throws IdlConversionException {
		super.fromIdlTransferable(mt, Identifier.valueOf(mt.domainId));

		this.name = mt.name;
		this.description = mt.description;

		this.setSiteNodeIds0(Identifier.fromTransferables(mt.siteNodeIds));
		this.setTopologicalNodeIds0(Identifier.fromTransferables(mt.topologicalNodeIds));
		this.setNodeLinkIds0(Identifier.fromTransferables(mt.nodeLinkIds));
		this.setPhysicalLinkIds0(Identifier.fromTransferables(mt.physicalLinkIds));
		this.setMarkIds0(Identifier.fromTransferables(mt.markIds));
		this.setCollectorIds0(Identifier.fromTransferables(mt.collectorIds));

		this.setMapIds0(Identifier.fromTransferables(mt.mapIds));
		this.setExternalNodeIds0(Identifier.fromTransferables(mt.externalNodeIds));
		this.setMapLibraryIds0(Identifier.fromTransferables(mt.mapLibraryIds));

		this.transientFieldsInitialized = false;
	}
	
	public void preloadCharacteristics() throws ApplicationException {
		LinkedIdsCondition condition = null;

		if(!this.siteNodeIds.isEmpty()) {
			condition = new LinkedIdsCondition(this.siteNodeIds, CHARACTERISTIC_CODE);
			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		}

//		if(!this.topologicalNodeIds.isEmpty()) {
//			condition.setLinkedIds(this.topologicalNodeIds);
//			StorableObjectPool.getStorableObjectsByCondition(condition, true);
//		}

//		if(!this.nodeLinkIds.isEmpty()) {
//			condition.setLinkedIds(this.nodeLinkIds);
//			StorableObjectPool.getStorableObjectsByCondition(condition, true);
//		}

		if (!this.physicalLinkIds.isEmpty()) {
			if (condition == null) {
				condition = new LinkedIdsCondition(this.physicalLinkIds, CHARACTERISTIC_CODE);
			} else {
				condition.setLinkedIdentifiables(this.physicalLinkIds);
			}
			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		}

//		if(!this.markIds.isEmpty()) {
//			condition.setLinkedIds(this.markIds);
//			StorableObjectPool.getStorableObjectsByCondition(condition, true);
//		}

		if (!this.collectorIds.isEmpty()) {
			if (condition == null) {
				condition = new LinkedIdsCondition(this.collectorIds, CHARACTERISTIC_CODE);
			} else {
				condition.setLinkedIdentifiables(this.collectorIds);
			}

			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		}

		if (!this.externalNodeIds.isEmpty()) {
			if (condition == null) {
				condition = new LinkedIdsCondition(this.externalNodeIds, CHARACTERISTIC_CODE);
			} else {
				condition.setLinkedIdentifiables(this.externalNodeIds);
			}

			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
	}

	public void open() {
		this.initialize();
		for(PhysicalLink physicalLink : this.physicalLinks) {
			physicalLink.getBinding().clear();
		}
		
		for(Map map : this.maps) {
			map.open();
		}
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMap getIdlTransferable(final ORB orb) {
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
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.getDomainId().getIdlTransferable(),
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
		this.initialize();
		return Collections.unmodifiableSet(this.collectors);
	}
	
	void setCollectorIds0(Set<Identifier> collectorIds) {
		this.collectorIds = collectorIds;
	}

	protected void setCollectors0(final Set<Collector> collectors) {
		this.collectorIds.clear();
		if(collectors != null) {
			for (Collector collector : collectors) {
				this.collectorIds.add(collector.getId());
			}
		}
	}

	public void setCollectors(final Set<Collector> collectors) {
		this.initialize();
		this.setCollectors0(collectors);
		this.collectors.clear();
		if(collectors != null) {
			for (Collector collector : collectors) {
				this.collectors.add(collector);
			}
		}
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
		this.initialize();
		return Collections.unmodifiableSet(this.mapLibrarys);
	}

	public void setMapLibraries(final Set<MapLibrary> mapLibraries) {
		this.initialize();
		this.mapLibrarys.clear();
		this.setMapLibraries0(mapLibraries);
		if (mapLibraries != null) {
			for (MapLibrary library : mapLibraries) {
				this.mapLibrarys.add(library);
			}
		}
		super.markAsChanged();
	}
	
	void setMapLibraryIds0(Set<Identifier> mapLibraryIds) {
		this.mapLibraryIds = mapLibraryIds;
	}
	
	protected void setMapLibraries0(final Set<MapLibrary> mapLibraries) {
		this.mapLibraryIds.clear();
		if (mapLibraries != null) {
			for (MapLibrary library : mapLibraries) {
				this.mapLibraryIds.add(library.getId());
			}
		}
	}

	public Set<Mark> getMarks() {
		this.initialize();
		return Collections.unmodifiableSet(this.marks);
	}

	void setMarkIds0(Set<Identifier> markIds) {
		this.markIds = markIds;
	}
	
	protected void setMarks0(final Set<Mark> marks) {
		this.markIds.clear();
		if (marks != null) {
			for (Mark mark : marks) {
				this.markIds.add(mark.getId());
			}
		}
	}

	public void setMarks(final Set<Mark> marks) {
		this.initialize();
		this.marks.clear();
		this.setMarks0(marks);
		if (marks != null) {
			for (Mark mark : marks) {
				this.marks.add(mark);
			}
		}
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
		this.initialize();
		return Collections.unmodifiableSet(this.nodeLinks);
	}

	void setNodeLinkIds0(Set<Identifier> nodeLinkIds) {
		this.nodeLinkIds = nodeLinkIds;
	}
	
	protected void setNodeLinks0(final Set<NodeLink> nodeLinks) {
		this.nodeLinkIds.clear();
		if (nodeLinks != null) {
			for (NodeLink link : nodeLinks) {
				this.nodeLinkIds.add(link.getId());
			}			
		}
	}

	public void setNodeLinks(final Set<NodeLink> nodeLinks) {
		this.initialize();
		this.nodeLinks.clear();
		this.setNodeLinks0(nodeLinks);
		if (nodeLinks != null) {
			for (NodeLink link : nodeLinks) {
				this.nodeLinks.add(link);
			}			
		}
		super.markAsChanged();
	}

	public Set<PhysicalLink> getPhysicalLinks() {
		this.initialize();
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	void setPhysicalLinkIds0(Set<Identifier> physicalLinkIds) {
		this.physicalLinkIds = physicalLinkIds;
	}
	
	protected void setPhysicalLinks0(final Set<PhysicalLink> physicalLinks) {
		this.physicalLinkIds.clear();
		if (physicalLinks != null) {
			for (PhysicalLink link : physicalLinks) {
				this.physicalLinkIds.add(link.getId());
			}
		}
	}

	public void setPhysicalLinks(final Set<PhysicalLink> physicalLinks) {
		this.initialize();
		this.physicalLinks.clear();
		this.setPhysicalLinks0(physicalLinks);
		if (physicalLinks != null) {
			for (PhysicalLink link : physicalLinks) {
				this.physicalLinks.add(link);
			}
		}
		super.markAsChanged();
	}

	public Set<SiteNode> getSiteNodes() {
		this.initialize();
		return Collections.unmodifiableSet(this.siteNodes);
	}

	void setSiteNodeIds0(Set<Identifier> siteNodeIds) {
		this.siteNodeIds = siteNodeIds;
	}
	
	protected void setSiteNodes0(final Set<SiteNode> siteNodes) {
		this.siteNodeIds.clear();
		if (siteNodes != null) {
			for (SiteNode node : siteNodes) {
				this.siteNodeIds.add(node.getId());
			}
		}
	}

	public void setSiteNodes(final Set<SiteNode> siteNodes) {
		this.initialize();
		this.siteNodes.clear();
		this.setSiteNodes0(siteNodes);
		if (siteNodes != null) {
			for (SiteNode node : siteNodes) {
				this.siteNodes.add(node);
			}
		}
		super.markAsChanged();
	}

	public Set<TopologicalNode> getTopologicalNodes() {
		this.initialize();
		return Collections.unmodifiableSet(this.topologicalNodes);
	}

	void setTopologicalNodeIds0(Set<Identifier> topologicalNodeIds) {
		this.topologicalNodeIds = topologicalNodeIds;
	}
	
	protected void setTopologicalNodes0(final Set<TopologicalNode> topologicalNodes) {
		this.topologicalNodeIds.clear();
		if (topologicalNodes != null) {
			for (TopologicalNode node : topologicalNodes) {
				this.topologicalNodeIds.add(node.getId());
			}
		}
	}

	public void setTopologicalNodes(final Set<TopologicalNode> topologicalNodes) {
		this.initialize();
		this.topologicalNodes.clear();
		this.setTopologicalNodes0(topologicalNodes);
		if (topologicalNodes != null) {
			for (TopologicalNode node : topologicalNodes) {
				this.topologicalNodes.add(node);
			}
		}
		super.markAsChanged();
	}

	public Set<Map> getMaps() {
		this.initialize();
		return Collections.unmodifiableSet(this.maps);
	}

	void setMapIds0(Set<Identifier> mapIds) {
		this.mapIds = mapIds;
	}
	
	protected void setMaps0(final Set<Map> maps) {
		this.mapIds.clear();
		if (maps != null) {
			for (Map map : maps) {
				this.mapIds.add(map.getId());
			}
		}
	}

	public void setMaps(final Set<Map> maps) {
		this.initialize();
		this.maps.clear();
		this.setMaps0(maps);
		if (maps != null) {
			for (Map map : maps) {
				this.maps.add(map);
			}
		}
		super.markAsChanged();
	}

	public Set<SiteNode> getExternalNodes() {
		this.initialize();
		return Collections.unmodifiableSet(this.externalNodes);
	}

	void setExternalNodeIds0(Set<Identifier> externalNodeIds) {
		this.externalNodeIds = externalNodeIds;
	}
	
	protected void setExternalNodes0(final Set<SiteNode> externalNodes) {
		this.externalNodeIds.clear();
		if (externalNodes != null) {
			for (SiteNode node : externalNodes) {
				this.externalNodeIds.add(node.getId());
			}
		}
	}

	public void setExternalNodes(final Set<SiteNode> externalNodes) {
		this.initialize();
		this.externalNodes.clear();
		this.setExternalNodes0(externalNodes);
		if (externalNodes != null) {
			for (SiteNode node : externalNodes) {
				this.externalNodes.add(node);
			}
		}
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
	 * Получить список всех узловых элементов топологической схемы.
	 *
	 * @return список узлов
	 */
	public Set<AbstractNode> getNodes() {
		this.initialize();
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
		this.initialize();
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
	 * Удалить узел.
	 *
	 * @param node
	 *          узел
	 */
	public void removeNode(final AbstractNode node) {
		this.initialize();
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

	public void addMapLibrary(final MapLibrary mapLibrary) {
		this.initialize();
		this.mapLibraryIds.add(mapLibrary.getId());
		this.mapLibrarys.add(mapLibrary);
		super.markAsChanged();
	}

	public void removeMapLibrary(final MapLibrary mapLibrary) {
		this.initialize();
		this.mapLibraryIds.remove(mapLibrary.getId());
		this.mapLibrarys.remove(mapLibrary);
		super.markAsChanged();
	}

	public void addMap(final Map map) {
		this.initialize();
		this.mapIds.add(map.getId());
		this.maps.add(map);
		super.markAsChanged();
	}

	public void removeMap(final Map map) {
		this.initialize();
		this.mapIds.remove(map.getId());
		this.maps.remove(map);
		super.markAsChanged();
	}

	public void addExternalNode(final SiteNode externalNode) {
		this.initialize();
		this.externalNodeIds.add(externalNode.getId());
		this.externalNodes.add(externalNode);
		super.markAsChanged();
	}

	/**
	 * Добавить новый коллектор.
	 *
	 * @param collector
	 *          новый коллектор
	 */
	public void addCollector(final Collector collector) {
		this.initialize();
		this.collectorIds.add(collector.getId());
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
		this.initialize();
		collector.setSelected(false);
		this.selectedElements.remove(collector);
		this.collectorIds.remove(collector.getId());
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
			if (collector.getPhysicalLinks().contains(physicalLink)) {
				return collector;
			}
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
	public Set<PhysicalLink> getPhysicalLinksAt(final AbstractNode node) {
		LinkedIdsCondition condition = new LinkedIdsCondition(node.getId(), PHYSICALLINK_CODE);
		try {
			return StorableObjectPool.getStorableObjectsByCondition(condition, false);
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	/**
	 * Добавить новую линию.
	 *
	 * @param physicalLink
	 *          линия
	 */
	public void addPhysicalLink(final PhysicalLink physicalLink) {
		this.initialize();
		this.physicalLinkIds.add(physicalLink.getId());
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
		this.initialize();
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
	 * добавить новый фрагмент линии.
	 *
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void addNodeLink(final NodeLink nodeLink) {
		this.initialize();
		this.nodeLinkIds.add(nodeLink.getId());
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
		this.initialize();
		nodeLink.setSelected(false);
		this.selectedElements.remove(nodeLink);
		this.nodeLinkIds.remove(nodeLink.getId());
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		super.markAsChanged();
	}

	/**
	 * Получить фрагмент линии по концевому узлу.
	 *
	 * @param node
	 *          концевой узел
	 * @return фрагмент линии
	 */
//	public NodeLink getNodeLink(final AbstractNode node) {
//		for (final NodeLink nodeLink : this.getNodeLinks()) {
//			if ((nodeLink.getStartNode().equals(node)) || (nodeLink.getEndNode().equals(node))) {
//				return nodeLink;
//			}
//		}
//		return null;
//	}

	/**
	 * Получить фрагмент линии по концевым узлам.
	 *
	 * @param startNode
	 *          один концевой узел
	 * @param endNode
	 *          другой концевой узел
	 * @return фрагмент линии
	 */
//	public NodeLink getNodeLink(final AbstractNode startNode, final AbstractNode endNode) {
//		for (final NodeLink nodeLink : this.getNodeLinks()) {
//			if (((nodeLink.getStartNode().equals(startNode)) && (nodeLink.getEndNode().equals(endNode)))
//					|| ((nodeLink.getStartNode().equals(endNode)) && (nodeLink.getEndNode().equals(startNode)))) {
//				return nodeLink;
//			}
//		}
//		return null;
//	}

	/**
	 * Получить список всех топологических элементов карты ({@link MapElement}).
	 *
	 * @return список всех элементов
	 */
//	public List<MapElement> getAllElements() {
//		this.initialize();
//		this.allElements.clear();
//
//		this.allElements.addAll(this.getAllMarks());
//		this.allElements.addAll(this.getAllTopologicalNodes());
//		this.allElements.addAll(this.getAllSiteNodes());
//		this.allElements.addAll(this.getExternalNodes());
//
//		this.allElements.addAll(this.getAllNodeLinks());
//		this.allElements.addAll(this.getAllPhysicalLinks());
//		this.allElements.addAll(this.getAllCollectors());
//
//		return Collections.unmodifiableList(this.allElements);
//	}

	/**
	 * Получить набор всех выделенных элементов топологической схемы.
	 * Выделенные элементы - те, для которых  метод
	 * <code>{@link MapElement#isSelected()}</code> возвращает <code>true</code>.
	 * @return набор выделенных элементов
	 */
	public Set<MapElement> getSelectedElements() {
		this.initialize();
		return Collections.unmodifiableSet(this.selectedElements);
	}

	/**
	 * Очистить набор выделенных элементов топологической схемы.
	 */
	public void clearSelection() {
		this.initialize();
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
		this.initialize();
		me.setSelected(selected);
		if (selected) {
			this.selectedElements.add(me);
		} else {
			this.selectedElements.remove(me);
		}
	}

//	/**
//	 * Получить список фрагментов линий, содержащих заданный узел.
//	 * @param node узел
//	 * @return Список фрагментов
//	 */
//	public Set<NodeLink> getNodeLinks(final AbstractNode node) {
//		final Set<NodeLink> returnNodeLinks = new HashSet<NodeLink>();
//		for (final NodeLink nodeLink : this.getNodeLinks()) {
//			if ((nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node))) {
//				returnNodeLinks.add(nodeLink);
//			}
//		}
//
//		return returnNodeLinks;
//	}

//	/**
//	 * Возвращает фрагмент линии, включающий данный узел, по не равный
//	 * переданному в параметре. Если фрагмент А и фрагмент Б имеют общую
//	 * точку Т, то вызов метода <code>Т.getOtherNodeLink(А)</code> вернет Б, а вызов
//	 * <code>Т.getOtherNodeLink(Б)</code> вернет А. Таким образом, для топологического
//	 * узла возвращает единственный противоположный,
//	 * для сетевого узла их может быть несколько, по этой причине метод
//	 * не должен использоваться и возвращает null
//	 * @param node узел
//	 * @param nodeLink фрагмент линии
//	 * @return другой фрагмент линии
//	 */
//	public NodeLink getOtherNodeLink(final AbstractNode node, NodeLink nodeLink) {
//		if (!node.getClass().equals(TopologicalNode.class)) {
//			return null;
//		}
//
//		NodeLink otherNodeLink = null;
//		for (final NodeLink bufNodeLink : this.getNodeLinks()) {
//			if (nodeLink != bufNodeLink) {
//				otherNodeLink = bufNodeLink;
//				break;
//			}
//		}
//
//		return otherNodeLink;
//	}

//	/**
//	 * Получить вектор узлов на противоположных концах всех фрагментов линий
//	 * данного элемента.
//	 * 
//	 * @param node
//	 *        узел
//	 * @return список узлов
//	 */
//	public Set<AbstractNode> getOppositeNodes(final AbstractNode node) {
//		final Set<AbstractNode> returnNodes = new HashSet<AbstractNode>();
//		for (final NodeLink nodeLink : this.getNodeLinks(node)) {
//			if (nodeLink.getEndNode().equals(node)) {
//				returnNodes.add(nodeLink.getStartNode());
//			}
//			else {
//				returnNodes.add(nodeLink.getEndNode());
//			}
//		}
//
//		return returnNodes;
//	}
//
	/**
	 * @param map
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlMap map,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		this.id.getXmlTransferable(map.addNewId(), importType);
		map.setName(this.name);
		if(this.description != null && this.description.length() != 0) {
			map.setDescription(this.description);
		}

		if (map.isSetTopologicalNodes()) {
			map.unsetTopologicalNodes();
		}
		final Set<TopologicalNode> topologicalNodes2 = this.getTopologicalNodes();
		if (!topologicalNodes2.isEmpty()) {
			final XmlTopologicalNodeSeq topologicalNodeSeq = map.addNewTopologicalNodes();
			for (final TopologicalNode topologicalNode : topologicalNodes2) {
				topologicalNode.getXmlTransferable(topologicalNodeSeq.addNewTopologicalNode(), importType, usePool);
			}
		}
		if (map.isSetSiteNodes()) {
			map.unsetSiteNodes();
		}
		final Set<SiteNode> siteNodes2 = this.getSiteNodes();
		if (!siteNodes2.isEmpty()) {
			final XmlSiteNodeSeq siteNodeSeq = map.addNewSiteNodes();
			// list of sitenodes already added to xml siteNodeSeq
			final Set<SiteNode> addedSiteNodes = new HashSet<SiteNode>();
			for (final SiteNode siteNode : siteNodes2) {
				final Identifier attachmentSiteNodeId = siteNode.getAttachmentSiteNodeId();
				// if siteNode has attachment site node the latter should be written
				// to xml prior to siteNode
				if(!attachmentSiteNodeId.equals(VOID_IDENTIFIER)) {
					SiteNode attachmentSiteNode = siteNode.getAttachmentSiteNode();
					// check if attachmentSiteNode has already been written to xml
					// as an attachment site node of some other site node
					if(!addedSiteNodes.contains(attachmentSiteNode)) {
						attachmentSiteNode.getXmlTransferable(siteNodeSeq.addNewSiteNode(), importType, usePool);
						addedSiteNodes.add(attachmentSiteNode);
					}
				}
				// check if siteNode has already been written to xml
				// as an attachment site node of some other site node
				if(!addedSiteNodes.contains(siteNode)) {
					siteNode.getXmlTransferable(siteNodeSeq.addNewSiteNode(), importType, usePool);
					addedSiteNodes.add(siteNode);
				}
			}
		}
		if (map.isSetPhysicalLinks()) {
			map.unsetPhysicalLinks();
		}
		final Set<PhysicalLink> physicalLinks2 = this.getPhysicalLinks();
		if (!physicalLinks2.isEmpty()) {
			final XmlPhysicalLinkSeq physicalLinkSeq = map.addNewPhysicalLinks();
			for (final PhysicalLink physicalLink : physicalLinks2) {
				physicalLink.getXmlTransferable(physicalLinkSeq.addNewPhysicalLink(), importType, usePool);
			}
		}
		if (map.isSetNodeLinks()) {
			map.unsetNodeLinks();
		}
		final Set<NodeLink> nodeLinks2 = this.getNodeLinks();
		if (!nodeLinks2.isEmpty()) {
			final XmlNodeLinkSeq nodeLinkSeq = map.addNewNodeLinks();
			for (final NodeLink nodeLink : nodeLinks2) {
				nodeLink.getXmlTransferable(nodeLinkSeq.addNewNodeLink(), importType, usePool);
			}
		}
		if (map.isSetCollectors()) {
			map.unsetCollectors();
		}
		final Set<Collector> collectors2 = this.getCollectors();
		if (!collectors2.isEmpty()) {
			final XmlCollectorSeq collectorSeq = map.addNewCollectors();
			for (final Collector collector : collectors2) {
				collector.getXmlTransferable(collectorSeq.addNewCollector(), importType, usePool);
			}
		}
		map.setImportType(importType);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private Map(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, MAP_CODE, created, creatorId);
	}

	public void fromXmlTransferable(final XmlMap xmlMap, final String importType)
	throws XmlConversionException {
		try {
			this.name = xmlMap.getName();
			if (xmlMap.isSetDescription()) {
				this.description = xmlMap.getDescription();
			} else {
				this.description = "";
			}
	
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
	
			if(xmlMap.isSetTopologicalNodes()) {
				for (final XmlTopologicalNode xmlTopologicalNode : xmlMap.getTopologicalNodes().getTopologicalNodeArray()) {
					this.addNode(TopologicalNode.createInstance(this.creatorId, importType, xmlTopologicalNode));
				}
			}
	
			if(xmlMap.isSetSiteNodes()) {
				for (final XmlSiteNode xmlSiteNode : xmlMap.getSiteNodes().getSiteNodeArray()) {
					this.addNode(SiteNode.createInstance(this.creatorId, importType, xmlSiteNode));
				}
			}
	
			if(xmlMap.isSetPhysicalLinks()) {
				for (final XmlPhysicalLink xmlPhysicalLink : xmlMap.getPhysicalLinks().getPhysicalLinkArray()) {
					this.addPhysicalLink(PhysicalLink.createInstance(this.creatorId, importType, xmlPhysicalLink));
				}
			}
	
			if(xmlMap.isSetNodeLinks()) {
				for (final XmlNodeLink xmlNodeLink : xmlMap.getNodeLinks().getNodeLinkArray()) {
					this.addNodeLink(NodeLink.createInstance(this.creatorId, importType, xmlNodeLink));
				}
			}
	
			if(xmlMap.isSetCollectors()) {
				for (final XmlCollector xmlCollector : xmlMap.getCollectors().getCollectorArray()) {
					this.addCollector(Collector.createInstance(this.creatorId, importType, xmlCollector));
				}
			}
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
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
			@Deprecated final Identifier domainId,
			final String importType,
			final XmlMap xmlMap) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlMap.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			Map map;
			if (id.isVoid()) {
				map = new Map(xmlId,
						importType,
						created,
						creatorId);
			} else {
				map = StorableObjectPool.getStorableObject(id, true);
				if (map == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					map = new Map(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			map.setDomainId0(domainId);
			map.fromXmlTransferable(xmlMap, importType);
			assert map.isValid() : OBJECT_BADLY_INITIALIZED;
			map.markAsChanged();
			return map;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	Set<Identifier> getCollectorIds() {
		return this.collectorIds;
	}

	Set<Identifier> getExternalNodeIds() {
		return this.externalNodeIds;
	}

	Set<Identifier> getMapIds() {
		return this.mapIds;
	}

	Set<Identifier> getMapLibraryIds() {
		return this.mapLibraryIds;
	}

	Set<Identifier> getMarkIds() {
		return this.markIds;
	}

	Set<Identifier> getNodeLinkIds() {
		return this.nodeLinkIds;
	}

	Set<Identifier> getPhysicalLinkIds() {
		return this.physicalLinkIds;
	}

	Set<Identifier> getSiteNodeIds() {
		return this.siteNodeIds;
	}

	Set<Identifier> getTopologicalNodeIds() {
		return this.topologicalNodeIds;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MapWrapper getWrapper() {
		return MapWrapper.getInstance();
	}
}
