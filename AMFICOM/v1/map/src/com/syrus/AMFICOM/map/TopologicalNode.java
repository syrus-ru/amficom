/*-
 * $Id: TopologicalNode.java,v 1.50 2005/07/04 13:00:48 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlTopologicalNodeHelper;

/**
 * �������������� ���� �� �������������� �����. �������������� ���� �����
 * ���� �������� ��� ����� � ��� ��������� �����. � ���������� ������
 * �������������� ���� ������������� ����� ������ ����� � �� �������
 * �������������� ������������ ����������.
 * @author $Author: bass $
 * @version $Revision: 1.50 $, $Date: 2005/07/04 13:00:48 $
 * @module map_v1
 * @todo physicalLink should be transient
 */
public final class TopologicalNode extends AbstractNode implements XMLBeansTransferable{

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258130254244885554L;

	/**
	 * ����� ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	private static java.util.Map exportMap = null;

	/**
	 * ���� ������������ ������ �� ���� true ������ ��� �� ���� ������� ��� �����,
	 * false ����
	 */
	private boolean active;

	private transient PhysicalLink physicalLink = null;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	TopologicalNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		TopologicalNodeDatabase database = (TopologicalNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.TOPOLOGICALNODE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	TopologicalNode(final IdlTopologicalNode tnt) throws CreateObjectException {
		super(tnt);
		super.name = tnt.name;
		super.description = tnt.description;
		super.location = new DoublePoint(tnt.longitude, tnt.latitude);
		this.active = tnt.active;

		try {
			this.characteristics = new HashSet(tnt.characteristicIds.length);
			Set<Identifier> characteristicIds = new HashSet(tnt.characteristicIds.length);
			for (int i = 0; i < tnt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(tnt.characteristicIds[i]));

			final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(characteristicIds, true);
			this.setCharacteristics0(characteristics0);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final boolean active) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				name,
				description,
				new DoublePoint(longitude, latitude));
		this.active = active;

		this.characteristics = new HashSet();
	}

	TopologicalNode(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final double longitude,
			final double latitude,
			final boolean active) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				name,
				description,
				new DoublePoint(longitude, latitude));
		this.physicalLink = physicalLink;
		this.active = active;

		this.characteristics = new HashSet();

		this.selected = false;
	}

	protected static TopologicalNode createInstance0(
			final Identifier creatorId,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final DoublePoint location)
		throws CreateObjectException {

		if (creatorId == null || name == null || description == null || location == null || physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TopologicalNode topologicalNode = new TopologicalNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TOPOLOGICALNODE_CODE),
					creatorId,
					0L,
					name,
					description,
					physicalLink,
					location.getX(),
					location.getY(),
					false);

			assert topologicalNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			topologicalNode.markAsChanged();

			return topologicalNode;

		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public static TopologicalNode createInstance(
			final Identifier creatorId,
			final String name,
			final String description,
			final PhysicalLink physicalLink,
			final DoublePoint location)
		throws CreateObjectException {

		return TopologicalNode.createInstance0(
				creatorId,
				name,
				description,
				physicalLink,
				location);
		}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final PhysicalLink physicalLink,
			final DoublePoint location) throws CreateObjectException {

		return TopologicalNode.createInstance0(creatorId, "", "", physicalLink, location);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTopologicalNode getTransferable(final ORB orb) {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);
		return IdlTopologicalNodeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version,
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.physicalLink.getId().getTransferable(),
				this.active,
				charIds);
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * ���������� ���������� ��������������� ����. ���� �������, ���� �� ���������
	 * � �������� �����, � �� �������, ���� �� ��������� �� ����� �����. ��������
	 * � ���������� �������������� ���� ������������ ������� ��������
	 *
	 * @param active
	 *          ���� ����������
	 */
	public void setActive(final boolean active) {
		this.active = active;
		super.markAsChanged();
	}

	/**
	 * @todo initial physicalLink
	 */
	public PhysicalLink getPhysicalLink() {
		if (this.physicalLink == null)
			this.physicalLink = findPhysicalLink();
		return this.physicalLink;
	}

	private PhysicalLink findPhysicalLink() {
		try {
			StorableObjectCondition condition = new LinkedIdsCondition(this.getId(), ObjectEntities.NODELINK_CODE);
			Set nlinks;

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			nlinks = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			NodeLink nodeLink = (NodeLink )nlinks.iterator().next();
			return nodeLink.getPhysicalLink();
		} catch(ApplicationException e) {
			// TODO how to work it over?!
			e.printStackTrace();
		}
		return null;
//	return this.map.getNodeLink(this).getPhysicalLink();
}

	public void setPhysicalLink(final PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		// do not change version due to physical link is not dependence object
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final boolean active) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.name = name;
		this.description = description;
		this.location.setLocation(longitude, latitude);
		this.active = active;
	}

	/**
	 * ���������� ���� ����������� �������� ��������������� ���� � �������� �/���
	 * �������������� ����.
	 *
	 * @param canBind
	 *          ���� ����������� ��������
	 */
	public void setCanBind(final boolean canBind) {
		this.canBind = canBind;
	}

	/**
	 * �������� ���� ����������� �������� ��������������� ���� � �������� �/���
	 * �������������� ����.
	 *
	 * @return ���� ����������� ��������
	 */
	public boolean isCanBind() {
		return this.canBind;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new TopologicalNodeState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(final MapElementState state) {
		TopologicalNodeState mpnes = (TopologicalNodeState) state;

		setName(mpnes.name);
		setDescription(mpnes.description);
		setImageId(mpnes.imageId);
		setLocation(mpnes.location);
		setActive(mpnes.active);
		try {
			setPhysicalLink((PhysicalLink) StorableObjectPool.getStorableObject(mpnes.physicalLinkId, false));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(StorableObjectWrapper.COLUMN_ID, this.id);
			exportMap.put(StorableObjectWrapper.COLUMN_NAME, this.name);
			exportMap.put(StorableObjectWrapper.COLUMN_DESCRIPTION, this.description);
			exportMap.put(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(TopologicalNodeWrapper.COLUMN_X, String.valueOf(this.location.getX()));
			exportMap.put(TopologicalNodeWrapper.COLUMN_Y, String.valueOf(this.location.getY()));
			exportMap.put(TopologicalNodeWrapper.COLUMN_ACTIVE, String.valueOf(this.active));
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static TopologicalNode createInstance(final Identifier creatorId, final java.util.Map exportMap1)
			throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(StorableObjectWrapper.COLUMN_ID);
		String name1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_NAME);
		String description1 = (String) exportMap1.get(StorableObjectWrapper.COLUMN_DESCRIPTION);
		Identifier physicalLinkId1 = (Identifier) exportMap1.get(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID);
		double x1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_X));
		double y1 = Double.parseDouble((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_Y));
		boolean active1 = Boolean.valueOf((String) exportMap1.get(TopologicalNodeWrapper.COLUMN_ACTIVE)).booleanValue();

		if (id1 == null || creatorId == null || name1 == null || description1 == null || physicalLinkId1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink1 = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId1, false);
			TopologicalNode node1 = new TopologicalNode(id1, creatorId, 0L, name1, description1, x1, y1, active1);
			node1.setPhysicalLink(physicalLink1);

			assert node1.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			node1.markAsChanged();

			return node1;
		} catch (ApplicationException e) {
			throw new CreateObjectException("Mark.createInstance |  ", e);
		}
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = com.syrus.amficom.map.xml.TopologicalNode.Factory.newInstance();
		fillXMLTransferable(xmlTopologicalNode);
		return xmlTopologicalNode;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode )xmlObject; 

		com.syrus.amficom.general.xml.UID uid = xmlTopologicalNode.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlTopologicalNode.setX(this.location.getX());
		xmlTopologicalNode.setY(this.location.getY());
		xmlTopologicalNode.setActive(this.active);
	}

	TopologicalNode(
			final Identifier creatorId, 
			final com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode, 
			final ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.TOPOLOGICALNODE_CODE, 
						xmlTopologicalNode.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0,
				"",
				"",
				new DoublePoint(0, 0));
		this.characteristics = new HashSet();
		this.selected = false;
		this.fromXMLTransferable(xmlTopologicalNode, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode )xmlObject;

		this.active = xmlTopologicalNode.getActive();
		super.location.setLocation(xmlTopologicalNode.getX(), xmlTopologicalNode.getY());
	}

	public static TopologicalNode createInstance(final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		com.syrus.amficom.map.xml.TopologicalNode xmlTopologicalNode = (com.syrus.amficom.map.xml.TopologicalNode )xmlObject;

		try {
			TopologicalNode topologicalNode = new TopologicalNode(creatorId, xmlTopologicalNode, clonedIdsPool);
			assert topologicalNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			topologicalNode.markAsChanged();
			return topologicalNode;
		} catch (Exception e) {
			throw new CreateObjectException("TopologicalNode.createInstance |  ", e);
		}
	}
}
