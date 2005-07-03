/*-
 * $Id: AbstractNode.java,v 1.26 2005/06/21 12:44:27 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 *
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/06/21 12:44:27 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode
	extends StorableObject
	implements MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

	protected Set<Characteristic> characteristics;

	protected String	name;

	protected String	description;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	@Deprecated
	protected double	longitude;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	@Deprecated
	protected double	latitude;

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

	protected AbstractNode(Identifier id) {
		super(id);
		this.characteristics = new HashSet();
	}

	AbstractNode(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String desription,
			final DoublePoint location) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = desription;
		this.location.setLocation(location.getX(), location.getY());
		this.characteristics = new HashSet();
	}

	AbstractNode(final IdlStorableObject transferable) throws CreateObjectException {
		try {
			this.fromTransferable(transferable);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlStorableObject sot = (IdlStorableObject) transferable;
		super.fromTransferable(sot);
		this.characteristics = new HashSet();
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}
	
	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
		super.markAsChanged();
	}

	public void removeCharacteristic(final Characteristic ch)
	{
		this.characteristics.remove(ch);
		super.markAsChanged();
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	public double getLatitude() {
		return this.location.getY();
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	public double getLongitude() {
		return this.location.getX();
	}
	
	protected void setLongitude(final double longitude) {
		this.location.setLocation(longitude, this.location.getY());
	}

	protected void setLatitude(final double latitude) {
		this.location.setLocation(this.location.getX(), latitude);
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

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	public DoublePoint getLocation() {
		return (DoublePoint)this.location.clone();
	}

	public void setLocation(final DoublePoint location)
	{
		this.location.setLocation(location.getX(), location.getY());
		super.markAsChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(final boolean selected)
	{
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed)
	{
		this.removed = removed;
	}
}
