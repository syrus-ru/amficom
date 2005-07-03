/*-
 * $Id: Mark.java,v 1.48 2005/07/03 19:16:28 bass Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlMark;
import com.syrus.AMFICOM.map.corba.IdlMarkHelper;

/**
 * ����� �� ����� �� �������������� �����. ����� �������� ���������������
 * ����������� �����, ������ �������� � ��������� ��������� �� ����� �,
 * ��������������, �� ����� ���� �������� ��� ����� � ���������� �����,
 * � ����� � ��� ������ ������ {@link AbstractNode}, ���������� � ������� �
 * ����������� �����, �������������� � �������
 * <code>{@link UnsupportedOperationException}</code>.
 * @author $Author: bass $
 * @version $Revision: 1.48 $, $Date: 2005/07/03 19:16:28 $
 * @module map_v1
 */
public final class Mark extends AbstractNode {

	public static final String IMAGE_NAME = "mark";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3258126938496186164L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_BUILDING = "building";

	/**
	 * ����� ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	private static java.util.Map exportMap = null;

	private PhysicalLink physicalLink;

	private double distance;

	private String city;
	private String street;
	private String building;

	protected transient double sizeInDoubleLt;
	protected transient NodeLink nodeLink;
	protected transient AbstractNode startNode;

	Mark(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		MarkDatabase database = (MarkDatabase) DatabaseContext.getDatabase(ObjectEntities.MARK_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	Mark(final IdlMark mt) throws CreateObjectException {
		super(mt);
		super.name = mt.name;
		super.description = mt.description;

		super.location = new DoublePoint(mt.longitude, mt.latitude);

		this.distance = mt.distance;

		this.city = mt.city;
		this.street = mt.street;
		this.building = mt.building;

		try {
			this.physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(new Identifier(mt.physicalLinkId), true);

			super.characteristics = new HashSet(mt.characteristicIds.length);
			Set characteristicIds = new HashSet(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			super.characteristics.addAll(StorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Mark(final Identifier id,
			final Identifier creatorId,
			final long version,
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

		super.characteristics = new HashSet();
	}

	public static Mark createInstance(final Identifier creatorId,
			final PhysicalLink link,
			final double len)
			throws CreateObjectException {
		return Mark.createInstance(creatorId,
				"",
				"",
				0.0D,
				0.0D,
				link,
				len,
				"",
				"",
				"");
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
			Mark mark = new Mark(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_CODE),
					creatorId,
					0L,
					name,
					description,
					longitude,
					latitude,
					physicalLink,
					distance,
					city,
					street,
					building);

			assert mark.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			mark.markAsChanged();

			return mark;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.singleton((Identifiable) this.physicalLink);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMark getTransferable(final ORB orb) {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return IdlMarkHelper.init(orb,
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
				this.distance,
				this.city,
				this.street,
				this.building,
				charIds);
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
			final long version,
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
	public DoublePoint getLocation() {
		return (DoublePoint) this.location.clone();
	}

	/**
	 * {@inheritDoc}
	 */
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
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;

		for (Iterator it = getPhysicalLink().getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nl = (NodeLink) it.next();
			if (nl.equals(this.nodeLink)) {
				pathLength += this.getSizeInDoubleLt();
				break;
			}
			pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	/**
	 * �������� �������������� ��������� �� ��������� ���� ����� �� �����.
	 *
	 * @return ���������
	 */
	public double getFromEndLengthLt() {
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;

		List nodeLinks = getPhysicalLink().getNodeLinks();
		for (ListIterator listIterator = nodeLinks.listIterator(); listIterator.hasPrevious();) {
			NodeLink nl = (NodeLink) listIterator.previous();
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
		NodeState mnes = (NodeState) state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			exportMap.put(COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(COLUMN_DISTANCE, String.valueOf(this.distance));
			exportMap.put(COLUMN_X, String.valueOf(this.location.getX()));
			exportMap.put(COLUMN_Y, String.valueOf(this.location.getY()));
			exportMap.put(COLUMN_CITY, this.city);
			exportMap.put(COLUMN_STREET, this.street);
			exportMap.put(COLUMN_BUILDING, this.building);
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static Mark createInstance(final Identifier creatorId, final java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);
		Identifier physicalLinkId1 = (Identifier) exportMap1.get(COLUMN_PHYSICAL_LINK_ID);
		double distance1 = Double.parseDouble((String) exportMap1.get(COLUMN_DISTANCE));
		String city1 = (String) exportMap1.get(COLUMN_CITY);
		String street1 = (String) exportMap1.get(COLUMN_STREET);
		String building1 = (String) exportMap1.get(COLUMN_BUILDING);
		double x1 = Double.parseDouble((String) exportMap1.get(COLUMN_X));
		double y1 = Double.parseDouble((String) exportMap1.get(COLUMN_Y));

		if (id1 == null
				|| creatorId == null
				|| name1 == null
				|| description1 == null
				|| physicalLinkId1 == null
				|| city1 == null
				|| street1 == null
				|| building1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLink physicalLink1 = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId1, false);
			Mark mark = new Mark(
					id1,
					creatorId,
					0L,
					name1,
					description1,
					x1,
					y1,
					physicalLink1,
					distance1,
					city1,
					street1,
					building1);
			
			assert mark.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			mark.markAsChanged();
			
			return mark;
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

}
