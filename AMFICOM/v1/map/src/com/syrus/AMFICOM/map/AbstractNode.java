/*-
 * $Id: AbstractNode.java,v 1.50 2006/03/15 14:47:33 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlAbstractNode;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 *
 * @author $Author: bass $
 * @version $Revision: 1.50 $, $Date: 2006/03/15 14:47:33 $
 * @module map
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode
		extends StorableObject
		implements MapElement {
	static final long serialVersionUID = -2623880496462305233L;

	protected String	name;

	protected String	description;

	/**
	 * Идентификатор изображения, которое отображается на топологической схеме
	 * в точке координат узла.
	 */
	protected Identifier imageId;

	/**
	 * Географические координаты узла.
	 */
	protected DoublePoint location = new DoublePoint(0, 0);

	protected transient boolean selected = false;

	protected transient boolean alarmState = false;

	protected transient boolean removed = false;

	protected AbstractNode(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String desription,
			final DoublePoint location) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = desription;
		this.location.setLocation(location.getX(), location.getY());
	}

	AbstractNode(/*IdlAbstractNode*/) {
		// super();
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	AbstractNode(final XmlIdentifier id,
			final String importType, final short entityCode,
			final Date created, final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param idlAbstractNode
	 */
	final void fromIdlTransferable(final IdlAbstractNode idlAbstractNode) {
		super.fromIdlTransferable(idlAbstractNode);
		this.name = idlAbstractNode.name;
		this.description = idlAbstractNode.description;		
		this.location = new DoublePoint(idlAbstractNode.longitude, idlAbstractNode.latitude);
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	protected void setLongitude(final double longitude) {
		this.location.setLocation(longitude, this.location.getY());
		super.markAsChanged();
	}

	protected void setLatitude(final double latitude) {
		this.location.setLocation(this.location.getX(), latitude);
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

	public DoublePoint getLocation() {
		return (DoublePoint) this.location.clone();
	}

	public void setLocation(final DoublePoint location) {
		this.location.setLocation(location.getX(), location.getY());
		super.markAsChanged();
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
		return this.alarmState;
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

}
