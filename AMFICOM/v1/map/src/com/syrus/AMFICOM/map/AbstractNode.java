/*-
 * $Id: AbstractNode.java,v 1.22 2005/05/18 11:48:20 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * Абстрактный класс, описывающий узловой элемент топологической схемы
 * ({@link Map}). Узловой объект характеризуется наличием координат
 * ({@link #location}) и изображением ({@link #imageId}).
 *
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/05/18 11:48:20 $
 * @module map_v1
 * @see SiteNode
 * @see TopologicalNode
 */
public abstract class AbstractNode
	extends StorableObject
	implements MapElement
{

	static final long serialVersionUID = -2623880496462305233L;

	protected Set		characteristics;

	protected String	name;

	protected String	description;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getX() getX()}
	 */
	protected double	longitude;

	/**
	 * @deprecated use {@link #location location}.{@link DoublePoint#getY() getY()}
	 */
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

	AbstractNode(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			String desription,
			DoublePoint location) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = desription;
		this.location.setLocation(location.getX(), location.getY());
		this.characteristics = new HashSet();
	}

	AbstractNode(StorableObject_Transferable transferable) throws CreateObjectException {
		try {
			this.fromTransferable(transferable);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		StorableObject_Transferable sot = (StorableObject_Transferable) transferable;
		super.fromTransferable(sot);
		this.characteristics = new HashSet();
	}

	public Identifier getImageId() {
		return this.imageId;
	}
	
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		this.changed = true;
	}
	
	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
		this.changed = true;
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
	
	protected void setLongitude(double longitude) {
		this.location.setLocation(longitude, this.location.getY());
	}

	protected void setLatitude(double latitude) {
		this.location.setLocation(this.location.getX(), latitude);
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

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		this.changed = true;
	}

	public DoublePoint getLocation(){
		return (DoublePoint)this.location.clone();
	}

	public void setLocation(DoublePoint location)
	{
		this.location.setLocation(location.getX(), location.getY());
		this.changed = true;
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
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState)
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
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
}
