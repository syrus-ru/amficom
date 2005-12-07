/*-
 * $Id: Mark.java,v 1.70 2005/12/07 17:17:18 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.IdlMark;
import com.syrus.AMFICOM.map.corba.IdlMarkHelper;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Метка на линии на топологической схеме. Метка частично характеризуется
 * абстрактным узлом, одняко привязан к некоторой дистанции на линии и,
 * соответственно, не может быть концевым для линий и фрагментов линий,
 * в связи с чем методы класса {@link AbstractNode}, работающие с линиями и
 * фрагментами линий, переопределены и бросают
 * <code>{@link UnsupportedOperationException}</code>.
 * @author $Author: bass $
 * @version $Revision: 1.70 $, $Date: 2005/12/07 17:17:18 $
 * @module map
 */
public final class Mark extends AbstractNode<Mark> {

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
					StorableObjectVersion.INITIAL_VERSION,
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMark getIdlTransferable(final ORB orb) {
		return IdlMarkHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.physicalLink.getId().getIdlTransferable(),
				this.distance,
				this.city,
				this.street,
				this.building);
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) throws ApplicationException {
		IdlMark idlMark = (IdlMark) transferable; 
		super.fromTransferable(idlMark);
		this.distance = idlMark.distance;
		this.city = idlMark.city;
		this.street = idlMark.street;
		this.building = idlMark.building;
		try {
			this.physicalLink = StorableObjectPool.getStorableObject(new Identifier(idlMark.physicalLinkId), true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
	 * Установить фрагмент, на который помещается метка.
	 *
	 * @param nodeLink
	 *          фрагмент линии
	 */
	public void setNodeLink(final NodeLink nodeLink) {
		this.nodeLink = nodeLink;
	}

	/**
	 * Установить фрагмент, на котором находится метка.
	 *
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	/**
	 * Установить начальный узел фрагмента, на котором находится метка.
	 *
	 * @param startNode
	 *          узел
	 */
	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	/**
	 * Получить начальный узел фрагмента, на котором находится метка.
	 *
	 * @return узел
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
	 * Получить топологическую дистанцию от начального узла линии до метки.
	 *
	 * @return дистанция
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
	 * Получить топологическую дистанцию от концевого узла линии до метки.
	 *
	 * @return дистанция
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
	 * Установить листанцию от начального узла фрагмента линии, на которой
	 * находится метка, до метки. Вычисление осуществляется в том месте, где
	 * осуществляется управление передвижением метки.
	 *
	 * @param sizeInDoubleLt
	 *          дистанция
	 */
	public void setSizeInDoubleLt(final double sizeInDoubleLt) {
		this.sizeInDoubleLt = sizeInDoubleLt;
	}

	/**
	 * Получить листанцию от начального узла фрагмента линии, на которой находится
	 * метка, до метки.
	 *
	 * @return дистанция
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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MarkWrapper getWrapper() {
		return MarkWrapper.getInstance();
	}
}
