/*-
 * $Id: Mark.java,v 1.64 2005/09/29 10:47:34 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.MARK_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.map.corba.IdlMark;
import com.syrus.AMFICOM.map.corba.IdlMarkHelper;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ����� �� ����� �� �������������� �����. ����� �������� ���������������
 * ����������� �����, ������ �������� � ��������� ��������� �� ����� �,
 * ��������������, �� ����� ���� �������� ��� ����� � ���������� �����,
 * � ����� � ��� ������ ������ {@link AbstractNode}, ���������� � ������� �
 * ����������� �����, �������������� � �������
 * <code>{@link UnsupportedOperationException}</code>.
 * @author $Author: krupenn $
 * @version $Revision: 1.64 $, $Date: 2005/09/29 10:47:34 $
 * @module map
 */
public final class Mark extends AbstractNode {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258126938496186164L;

	private PhysicalLink physicalLink;

	private double distance;

	private String city;
	private String street;
	private String building;

	private transient double sizeInDoubleLt;
	private transient NodeLink nodeLink;
	private transient AbstractNode startNode;

	public Mark(final IdlMark mt) throws CreateObjectException {
		super(mt);
		super.name = mt.name;
		super.description = mt.description;

		super.location = new DoublePoint(mt.longitude, mt.latitude);

		this.distance = mt.distance;

		this.city = mt.city;
		this.street = mt.street;
		this.building = mt.building;

		try {
			this.physicalLink = StorableObjectPool.getStorableObject(new Identifier(mt.physicalLinkId), true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Mark(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final PhysicalLink physicalLink,
			final double distance,
			final String city,
			final String street,
			final String building) {
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
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;
	}

	public static Mark createInstance(final Identifier creatorId, final PhysicalLink link, final double len)
			throws CreateObjectException {
		return Mark.createInstance(creatorId, "", "", 0.0D, 0.0D, link, len, "", "", "");
	}

	public static Mark createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final PhysicalLink physicalLink,
			final double distance,
			final String city,
			final String street,
			final String building) throws CreateObjectException {
		if (creatorId == null
				|| name == null
				|| description == null
				|| physicalLink == null
				|| city == null
				|| street == null
				|| building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Mark mark = new Mark(IdentifierPool.getGeneratedIdentifier(MARK_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					longitude,
					latitude,
					physicalLink,
					distance,
					city,
					street,
					building);

			assert mark.isValid() : OBJECT_STATE_ILLEGAL;

			mark.markAsChanged();

			return mark;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
		return Collections.singleton((Identifiable) this.physicalLink);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMark getTransferable(final ORB orb) {
		return IdlMarkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.physicalLink.getId().getTransferable(),
				this.distance,
				this.city,
				this.street,
				this.building);
	}

	public String getBuilding() {
		return this.building;
	}

	protected void setBuilding0(final String building) {
		this.building = building;
	}

	public void setBuilding(final String building) {
		this.setBuilding0(building);
		super.markAsChanged();
	}

	public String getCity() {
		return this.city;
	}

	protected void setCity0(final String city) {
		this.city = city;
	}

	public void setCity(final String city) {
		this.setCity0(city);
		super.markAsChanged();
	}

	public double getDistance() {
		return this.distance;
	}

	protected void setDistance0(final double distance) {
		this.distance = distance;
	}

	public void setDistance(final double distance) {
		this.setDistance0(distance);
		super.markAsChanged();
	}

	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}

	protected void setPhysicalLink0(final PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
	}

	public void setPhysicalLink(final PhysicalLink physicalLink) {
		this.setPhysicalLink0(physicalLink);
		super.markAsChanged();
	}

	public String getStreet() {
		return this.street;
	}

	protected void setStreet0(final String street) {
		this.street = street;
	}

	public void setStreet(final String street) {
		this.setStreet0(street);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final PhysicalLink physicalLink,
			final double distance,
			final String city,
			final String street,
			final String building) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		super.name = name;
		super.description = description;
		super.location.setLocation(longitude, latitude);
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;
	}

	/**
	 * ���������� ��������, �� ������� ���������� �����.
	 *
	 * @param nodeLink
	 *          �������� �����
	 */
	public void setNodeLink(final NodeLink nodeLink) {
		this.nodeLink = nodeLink;
	}

	/**
	 * ���������� ��������, �� ������� ��������� �����.
	 *
	 * @return �������� �����
	 */
	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	/**
	 * ���������� ��������� ���� ���������, �� ������� ��������� �����.
	 *
	 * @param startNode
	 *          ����
	 */
	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	/**
	 * �������� ��������� ���� ���������, �� ������� ��������� �����.
	 *
	 * @return ����
	 */
	public AbstractNode getStartNode() {
		return this.startNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoublePoint getLocation() {
		return (DoublePoint) this.location.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(final DoublePoint location) {
		super.setLocation(location);
		setDistance(this.getFromStartLengthLt());
	}

	/**
	 * �������� �������������� ��������� �� ���������� ���� ����� �� �����.
	 *
	 * @return ���������
	 */
	public double getFromStartLengthLt() {
		this.getPhysicalLink().sortNodeLinks();

		double pathLength = 0;

		for (final NodeLink nodeLink1 : this.getPhysicalLink().getNodeLinks()) {
			if (nodeLink1.equals(this.nodeLink)) {
				pathLength += this.getSizeInDoubleLt();
				break;
			}
			pathLength += nodeLink1.getLengthLt();
		}
		return pathLength;
	}

	/**
	 * �������� �������������� ��������� �� ��������� ���� ����� �� �����.
	 *
	 * @return ���������
	 */
	public double getFromEndLengthLt() {
		this.getPhysicalLink().sortNodeLinks();

		double pathLength = 0;

		final List<NodeLink> nodeLinks = getPhysicalLink().getNodeLinks();
		for (final ListIterator<NodeLink> listIterator = nodeLinks.listIterator(); listIterator.hasPrevious();) {
			final NodeLink nl = listIterator.previous();
			if (nl == this.nodeLink) {
				pathLength += nl.getLengthLt() - this.getSizeInDoubleLt();
				break;
			}
			pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	/**
	 * ���������� ��������� �� ���������� ���� ��������� �����, �� �������
	 * ��������� �����, �� �����. ���������� �������������� � ��� �����, ���
	 * �������������� ���������� ������������� �����.
	 *
	 * @param sizeInDoubleLt
	 *          ���������
	 */
	public void setSizeInDoubleLt(final double sizeInDoubleLt) {
		this.sizeInDoubleLt = sizeInDoubleLt;
	}

	/**
	 * �������� ��������� �� ���������� ���� ��������� �����, �� ������� ���������
	 * �����, �� �����.
	 *
	 * @return ���������
	 */
	public double getSizeInDoubleLt() {
		return this.sizeInDoubleLt;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		return new NodeState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(final MapElementState state) {
		final NodeState mnes = (NodeState) state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
	}

	public Characterizable getCharacterizable() {
		return null;
	}

}
