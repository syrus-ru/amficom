/*-
 * $Id: AbstractNode.java,v 1.42 2005/09/25 15:42:48 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.bugs.Crutch134;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 *
 * @author $Author: bass $
 * @version $Revision: 1.42 $, $Date: 2005/09/25 15:42:48 $
 * @module map
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode extends StorableObject
		implements Characterizable, MapElement {
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

	AbstractNode(final IdlStorableObject transferable) throws CreateObjectException {
		try {
			this.fromTransferable(transferable);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		super.fromTransferable(transferable);
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

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicContainerWrappee()
	 */
	@Crutch134(notes = "Remove subclassing here.")
	public final StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		if (this.characteristicContainerWrappee == null) {
			this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE) {
				private static final long serialVersionUID = -2741783821486426615L;

				@Override
				protected void ensureCacheBuilt(final boolean usePool)
				throws ApplicationException {
					if (!this.cacheBuilt || usePool) {
						if (this.containees == null) {
							this.containees = new HashSet<Characteristic>();
						} else {
							this.containees.clear();
						}
						this.containees.addAll(StorableObjectPool.<Characteristic>getStorableObjectsByCondition(this.condition, false));
						this.cacheBuilt = true;
					}
				}
			};
		}
		return this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public final void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public final void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public final Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	final Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public final void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
