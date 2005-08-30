/*-
 * $Id: NodeLink.java,v 1.75 2005/08/30 13:20:44 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUIDMapDatabase;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlNodeLink;
import com.syrus.AMFICOM.map.corba.IdlNodeLinkHelper;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * �������� ����� �� �������������� �����. �������� ������������ ����� ��������
 * �������, ����������� ��� �������� ���� ({@link AbstractNode}). ���������
 * �� ����� ���� �� ����, � ������ � ������ ����� � ������ ����� �����
 * ({@link PhysicalLink}).
 * @author $Author: krupenn $
 * @version $Revision: 1.75 $, $Date: 2005/08/30 13:20:44 $
 * @module map
 */
public final class NodeLink extends StorableObject implements MapElement, XmlBeansTransferable<XmlNodeLink> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257290240262617393L;

	private String name;
	private PhysicalLink physicalLink;
	private AbstractNode startNode;
	private AbstractNode endNode;
	private double length;

	private transient CharacterizableDelegate characterizableDelegate;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	NodeLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(NODELINK_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public NodeLink(final IdlNodeLink nlt) throws CreateObjectException {
		try {
			this.fromTransferable(nlt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	NodeLink(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final PhysicalLink physicalLink,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final double length) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.name = name;
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;

		this.selected = false;
	}

	public static NodeLink createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final AbstractNode stNode,
			final AbstractNode eNode) throws CreateObjectException {
		return NodeLink.createInstance(creatorId, "", physicalLink, stNode, eNode, 0.0D);
	}

	public static NodeLink createInstance(final Identifier creatorId,
			final String name,
			final PhysicalLink physicalLink,
			final AbstractNode starNode,
			final AbstractNode endNode,
			final double length) throws CreateObjectException {
		if (name == null || physicalLink == null || starNode == null || endNode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final NodeLink nodeLink = new NodeLink(IdentifierPool.getGeneratedIdentifier(NODELINK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					physicalLink,
					starNode,
					endNode,
					length);
			physicalLink.addNodeLink(nodeLink);

			assert nodeLink.isValid() : OBJECT_STATE_ILLEGAL;

			nodeLink.markAsChanged();

			return nodeLink;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlNodeLink nlt = (IdlNodeLink) transferable;
		super.fromTransferable(nlt);

		this.length = nlt.length;

		this.physicalLink = StorableObjectPool.getStorableObject(new Identifier(nlt.physicalLinkId), true);

		this.startNode = StorableObjectPool.getStorableObject(new Identifier(nlt.startNodeId), true);
		this.endNode = StorableObjectPool.getStorableObject(new Identifier(nlt.endNodeId), true);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlNodeLink getTransferable(final ORB orb) {
		return IdlNodeLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.physicalLink.getId().getTransferable(),
				this.startNode.getId().getTransferable(),
				this.endNode.getId().getTransferable(),
				this.length);
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	protected void setEndNode0(final AbstractNode endNode) {
		this.endNode = endNode;
	}

	public void setEndNode(final AbstractNode endNode) {
		this.setEndNode0(endNode);
		super.markAsChanged();
	}

	/**
	 * ���� ���� �� �������� ����� ����� - ��������� ����, � ������ - 
	 * ���������� ���� ��� ������, �� ��� - �������� �� ����. � ����
	 * ������ ����� ��������� ��� �������� ����� �������� �� ����,
	 * � ������ ����� ���������� ����� 0.
	 * @return �������������� ����� ���������, � 0, ���� �������� �� ������
	 */
	public double getLength() {
		AbstractNode startLinkNode = this.physicalLink.getStartNode();
		boolean hasCableInlet = false;
		boolean hasBuilding = false;
		if(startLinkNode instanceof SiteNode) {
			SiteNode startLinkSite = (SiteNode) startLinkNode;
			SiteNodeTypeSort startLinkSiteTypeSort = startLinkSite.getType().getSort();
			if(startLinkSiteTypeSort.equals(SiteNodeTypeSort.BUILDING)
					|| startLinkSiteTypeSort.equals(SiteNodeTypeSort.ATS) ) {
				hasBuilding = true;
			}
			if(startLinkSiteTypeSort.equals(SiteNodeTypeSort.CABLE_INLET) ) {
				hasCableInlet = true;
			}
		}

		AbstractNode endLinkNode = this.physicalLink.getEndNode();
		if(endLinkNode instanceof SiteNode) {
			SiteNode endLinkSite = (SiteNode) endLinkNode;
			SiteNodeTypeSort endLinkSiteTypeSort = endLinkSite.getType().getSort();
			if(endLinkSiteTypeSort.equals(SiteNodeTypeSort.BUILDING)
					|| endLinkSiteTypeSort.equals(SiteNodeTypeSort.ATS) ) {
				hasBuilding = true;
			}
			if(endLinkSiteTypeSort.equals(SiteNodeTypeSort.CABLE_INLET) ) {
				hasCableInlet = true;
			}
		}
		if(hasCableInlet && hasBuilding) {
			return 0D;
		}
		return this.length;
	}

	protected void setLength0(final double length) {
		this.length = length;
	}

	public void setLength(final double length) {
		this.setLength0(length);
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

	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}

	protected void setPhysicalLink0(final PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		if (getStartNode() instanceof TopologicalNode)
			((TopologicalNode) getStartNode()).setPhysicalLink(physicalLink);
		if (getEndNode() instanceof TopologicalNode)
			((TopologicalNode) getEndNode()).setPhysicalLink(physicalLink);
	}

	public void setPhysicalLink(final PhysicalLink physicalLink) {
		if(this.physicalLink != null) {
			this.physicalLink.removeNodeLink(this);
		}

		this.setPhysicalLink0(physicalLink);

		if(physicalLink != null) {
			physicalLink.addNodeLink(this);
		}
		super.markAsChanged();
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	protected void setStartNode0(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	public void setStartNode(final AbstractNode startNode) {
		this.setStartNode0(startNode);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final PhysicalLink physicalLink,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final double length) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;
	}

	/**
	 * �������� ������ �������� ���� ���������.
	 *
	 * @param node
	 *          �������� ����
	 * @return ������ �������� ����. � ������, ���� node �� �������� �������� ���
	 *         ������� ���������, ������������ <code>null</code>.
	 */
	public AbstractNode getOtherNode(final AbstractNode node) {
		if (this.getEndNode() == node) {
			return getStartNode();
		}
		if (this.getStartNode() == node) {
			return getEndNode();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return getPhysicalLink().getAlarmState();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		return new DoublePoint((getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
				(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new NodeLinkState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(final MapElementState state) {
		final NodeLinkState mnles = (NodeLinkState) state;

		this.setName(mnles.name);
		this.setStartNode(mnles.startNode);
		this.setEndNode(mnles.endNode);

		try {
			final PhysicalLink physicalLink1 = StorableObjectPool.getStorableObject(mnles.physicalLinkId, false);
			setPhysicalLink(physicalLink1);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed) {
		this.removed = removed;
	}

	/**
	 * �������� �������������� ������ ���������.
	 *
	 * @return �������������� �����
	 */
	public double getLengthLt() {
		return getLength();
	}

	/**
	 * ���������� �������������� ������ ���������. ������������� � �����, �
	 * ������� �������������� ���������� ���������� ���������� �����.
	 *
	 * @param length
	 *          �������������� �����
	 */
	public void setLengthLt(final double length) {
		this.setLength(length);
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.physicalLink);
		dependencies.add(this.startNode);
		dependencies.add(this.endNode);
		return dependencies;
	}

	public XmlNodeLink getXmlTransferable() {
		final XmlNodeLink xmlNodeLink = XmlNodeLink.Factory.newInstance();
		XmlIdentifier uid = xmlNodeLink.addNewId();
		uid.setStringValue(this.id.toString());
		
		xmlNodeLink.setLength(this.length);
		
		uid = xmlNodeLink.addNewPhysicalLinkId();
		uid.setStringValue(this.physicalLink.getId().toString());
		
		uid = xmlNodeLink.addNewStartNodeId();
		uid.setStringValue(this.startNode.getId().toString());
		
		uid = xmlNodeLink.addNewEndNodeId();
		uid.setStringValue(this.endNode.getId().toString());
		return xmlNodeLink;
	}

	NodeLink(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlNodeLink xmlNodeLink,
			final ClonedIdsPool clonedIdsPool, 
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(NODELINK_CODE, xmlNodeLink.getId().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.selected = false;
		this.fromXmlTransferable(xmlNodeLink, clonedIdsPool, importType);
	}

	public void fromXmlTransferable(final XmlNodeLink xmlNodeLink, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.length = xmlNodeLink.getLength();

		final Identifier physicalLinkId1 = clonedIdsPool.getClonedId(PHYSICALLINK_CODE,
				xmlNodeLink.getPhysicalLinkId().getStringValue());
		final Identifier startNodeId1 = clonedIdsPool.getClonedId(SITENODE_CODE,
				xmlNodeLink.getStartNodeId().getStringValue());
		final Identifier endNodeId1 = clonedIdsPool.getClonedId(SITENODE_CODE,
				xmlNodeLink.getEndNodeId().getStringValue());

		this.physicalLink = StorableObjectPool.getStorableObject(physicalLinkId1, false);
		this.startNode = StorableObjectPool.getStorableObject(startNodeId1, true);
		this.endNode = StorableObjectPool.getStorableObject(endNodeId1, true);
		this.physicalLink.addNodeLink(this);
	}

	public static NodeLink createInstance(
			final Identifier creatorId, 
			final String importType, 
			final XmlNodeLink xmlNodeLink, 
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			String uid = xmlNodeLink.getId().getStringValue();
			Identifier existingIdentifier = ImportUIDMapDatabase.retrieve(importType, uid);
			NodeLink nodeLink = null;
			if(existingIdentifier != null) {
				nodeLink = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(nodeLink != null) {
					clonedIdsPool.setExistingId(uid, existingIdentifier);
					nodeLink.fromXmlTransferable(xmlNodeLink, clonedIdsPool, importType);
				}
				else{
					ImportUIDMapDatabase.delete(importType, uid);
				}
			}
			if(nodeLink == null) {
				nodeLink = new NodeLink(creatorId, StorableObjectVersion.createInitial(), xmlNodeLink, clonedIdsPool, importType);
				ImportUIDMapDatabase.insert(importType, uid, nodeLink.id);
			}
			assert nodeLink.isValid() : OBJECT_STATE_ILLEGAL;
			nodeLink.markAsChanged();
			return nodeLink;
		} catch (Exception e) {
			throw new CreateObjectException("NodeLink.createInstance |  ", e);
		}
	}
}
