/*-
 * $Id: AbstractNode.java,v 1.29 2005/07/17 05:20:43 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * ����������� �����, ����������� ������� ������� �������������� �����
 * ({@link Map}). ������� ������ ��������������� �������� ���������
 * ({@link #location}) � ������������ ({@link #imageId}).
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.29 $, $Date: 2005/07/17 05:20:43 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode
	extends StorableObject
	implements MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

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
	 * ������������� �����������, ������� ������������ �� �������������� �����
	 * � ����� ��������� ����.
	 */
	protected Identifier imageId;

	/**
	 * �������������� ���������� ����.
	 */
	protected DoublePoint location = new DoublePoint(0, 0);


	protected transient boolean selected = false;

	protected transient boolean alarmState = false;

	protected transient boolean removed = false;

	protected AbstractNode(Identifier id) {
		super(id);
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
	}

	AbstractNode(final IdlStorableObject transferable) throws CreateObjectException {
		try {
			this.fromTransferable(transferable);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		super.fromTransferable(transferable);
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}
	
	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
	@Deprecated
	public double getLatitude() {
		return this.location.getY();
	}

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	@Deprecated
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
