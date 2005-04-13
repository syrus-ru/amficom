/*-
 * $Id: Map.java,v 1.36 2005/04/13 15:42:21 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;

/**
 * �������������� �����, ������� �������� � ���� ����� ��������� ���� � ������
 * ����� (������� � ��������������), ����� (��������� �� ����������), ����� �� 
 * ������, ����������� (������������ � ���� �����).
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.36 $, $Date: 2005/04/13 15:42:21 $
 * @module map_v1
 * @todo make maps persistent 
 */
public class Map extends DomainMember implements Namable {

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
	 * ����� ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
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
	/**
	 * ������������� ������ ���� ��������� �������������� �����
	 */
	protected transient List allElements;
	protected transient Set nodeElements;

	protected transient Set externalNodes;

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

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Map_Transferable mt = (Map_Transferable) transferable;
		super.fromTransferable(mt.header, new Identifier(mt.domain_id));

		this.name = mt.name;
		this.description = mt.description;

		Set ids;
		
		ids = Identifier.fromTransferables(mt.siteNodeIds);
		this.siteNodes = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.topologicalNodeIds);
		this.topologicalNodes = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.nodeLinkIds);
		this.nodeLinks = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.physicalLinkIds);
		this.physicalLinks = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.markIds);
		this.marks = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(mt.collectorIds);
		this.collectors = MapStorableObjectPool.getStorableObjects(ids, true);
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

	protected void setCollectors0(Set collectors) {
		this.collectors.clear();
		if (collectors != null) {
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

	public Set getExternalNodes() {
		return Collections.unmodifiableSet(this.externalNodes);
	}

	protected void setExternalNodes0(Set externalNodes) {
		this.externalNodes.clear();
		if (externalNodes != null)
			this.externalNodes.addAll(externalNodes);
	}

	public void setExternalNodes(Set externalNodes) {
		this.setExternalNodes0(externalNodes);
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
	 * �������� ������ ���� ������� ��������� �������������� �����.
	 * 
	 * @return ������ �����
	 */
	public Set getNodes() {
		this.nodeElements.clear();
		this.nodeElements.addAll(this.getAllSiteNodes());
		this.nodeElements.addAll(this.getAllTopologicalNodes());
		this.nodeElements.addAll(this.getAllMarks());
		return this.nodeElements;
	}

	/**
	 * �������� ����� ����.
	 * 
	 * @param node
	 *          ����
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
		node.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� ����.
	 * 
	 * @param node
	 *          ����
	 */
	public void removeNode(AbstractNode node) {
		node.setSelected(false);
		this.selectedElements.remove(node);
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
	 * �������� ������� �������� ���� �� ��������������.
	 * 
	 * @param siteId
	 *          ������������� �������� ����
	 * @return ������� ���� ��� null, ���� ���� �� ������
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
	 * �������� ������� ��������������� ���� �� ��������������.
	 * 
	 * @param topologicalNodeId
	 *          ������������� ��������������� ����
	 * @return �������������� ���� ��� null, ���� ���� �� ������
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
	 * �������� ����� �� ��������������.
	 * 
	 * @param markId
	 *          ������������� �����
	 * @return ����� ��� null, ���� ����� �� ������
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
	 * �������� ���� �� ��������������.
	 * 
	 * @param nodeId
	 *          ������������� �����
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

	public void addMap(Map map) {
		this.maps.add(map);
		this.changed = true;
	}

	public void removeMap(Map map) {
		this.maps.remove(map);
		this.changed = true;
	}

	public void addExternalNode(AbstractNode externalNode) {
		this.externalNodes.add(externalNode);
		this.changed = true;
	}

	public void removeExternalNode(AbstractNode externalNode) {
		this.externalNodes.remove(externalNode);
		this.changed = true;
	}

	/**
	 * �������� ����� ���������.
	 * 
	 * @param collector
	 *          ����� ���������
	 */
	public void addCollector(Collector collector) {
		this.collectors.add(collector);
		collector.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� ���������.
	 * 
	 * @param collector
	 *          ���������
	 */
	public void removeCollector(Collector collector) {
		collector.setSelected(false);
		this.selectedElements.remove(collector);
		this.collectors.remove(collector);
		collector.setRemoved(true);
		this.changed = true;
	}

	/**
	 * �������� ���������, � ������� �������� ���� �������� �����.
	 * 
	 * @param physicalLink
	 *          �����
	 * @return ���������
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
	 * �������� ������ �����, ������������ ��� ��������������� � �������� ����.
	 * 
	 * @param node
	 *          ����
	 * @return ������ �����
	 */
	public Set getPhysicalLinksAt(AbstractNode node) {
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
	 * �������� ����� �����.
	 * 
	 * @param physicalLink
	 *          �����
	 */
	public void addPhysicalLink(PhysicalLink physicalLink) {

		this.physicalLinks.add(physicalLink);
		physicalLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� �����.
	 * 
	 * @param physicalLink
	 *          �����
	 */
	public void removePhysicalLink(PhysicalLink physicalLink) {

		physicalLink.setSelected(false);
		this.selectedElements.remove(physicalLink);
		this.physicalLinks.remove(physicalLink);
		physicalLink.setRemoved(true);

		Collector coll = getCollector(physicalLink);
		if (coll != null)
			coll.removePhysicalLink(physicalLink);
		this.changed = true;
	}

	/**
	 * �������� ����� �� �� ��������������.
	 * 
	 * @param phisicalLinkId
	 *          ������������� �����
	 * @return �����
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
	 * �������� ����� �� �������� �����.
	 * 
	 * @param startNode
	 *          ���� �������� ����
	 * @param endNode
	 *          ������ �������� ����
	 * @return �����
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
	 * �������� ����� �������� �����.
	 * 
	 * @param nodeLink
	 *          �������� �����
	 */
	public void addNodeLink(NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
		nodeLink.setRemoved(false);
		this.changed = true;
	}

	/**
	 * ������� �������� �����.
	 * 
	 * @param nodeLink
	 *          �������� �����
	 */
	public void removeNodeLink(NodeLink nodeLink) {
		nodeLink.setSelected(false);
		this.selectedElements.remove(nodeLink);
		this.nodeLinks.remove(nodeLink);
		nodeLink.setRemoved(true);
		this.changed = true;
	}

	/**
	 * �������� �������� ����� �� ��������������.
	 * 
	 * @param nodeLinkId
	 *          ������������� ��������� �����
	 * @return �������� �����
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
	 * �������� �������� ����� �� ��������� ����.
	 * 
	 * @param node
	 *          �������� ����
	 * @return �������� �����
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
	 * �������� �������� ����� �� �������� �����.
	 * 
	 * @param startNode
	 *          ���� �������� ����
	 * @param endNode
	 *          ������ �������� ����
	 * @return �������� �����
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
	 * �������� ������ ���� �������������� ��������� ����� ({@link MapElement}).
	 * 
	 * @return ������ ���� ���������
	 */
	public List getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.getAllMarks());
		this.allElements.addAll(this.getAllTopologicalNodes());
		this.allElements.addAll(this.getAllSiteNodes());

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
	 * 
	 * @param me
	 *          �������
	 * @param selected
	 *          ���� ���������
	 */
	public void setSelected(MapElement me, boolean selected) {
		me.setSelected(selected);
		if (selected)
			this.selectedElements.add(me);
		else
			this.selectedElements.remove(me);
	}

	/**
	 * ���������� ����������� ������� ����� ����������, ������� ������������ ���
	 * ��������.
	 * 
	 * @return ���-������� ���������� ��������
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

	/**
	 * �������� ������ ���������� �����, ���������� �������� ����.
	 * @param node ����
	 * @return ������ ����������
	 */
	public Set getNodeLinks(AbstractNode node)
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
	public NodeLink getOtherNodeLink(AbstractNode node, NodeLink nodeLink)
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
	 * �������� ������ ����� �� ��������������� ������ ���� ���������� ����� 
	 * ������� ��������.
	 * @param node ����
	 * @return ������ �����
	 */
	public Set getOppositeNodes(AbstractNode node)
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
	 * �������� ������ �����, ������������ ��� ���������������
	 * �� ������ ����.
	 * @return ������ �����
	 */
	public Set getPhysicalLinks(AbstractNode node)
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

}

