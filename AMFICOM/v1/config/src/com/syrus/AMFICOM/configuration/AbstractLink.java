/*-
 * $Id: AbstractLink.java,v 1.1 2005/06/22 15:05:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TypedObject;

public abstract class AbstractLink extends DomainMember implements Characterizable, TypedObject {
	AbstractLinkType type;
	String name;
	String description;
	String inventoryNo;
	String supplier;
	String supplierCode;
	String mark;
	int color;

	Set<Characteristic> characteristics;

	AbstractLink(final Identifier id) {
		super(id);
	}

	AbstractLink() {
//		super();
	}

	AbstractLink(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, long version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version,
				domainId);
	}

	public void setType(final AbstractLinkType type) {
		this.type = type;
		super.markAsChanged();
	}

	public AbstractLinkType getType() {
		return this.type;
	}

	/**
	 * @param name The name to set.
	 */
	public final void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param supplier The supplier to set.
	 */
	public final void setSupplier(final String supplier) {
		this.supplier = supplier;
		super.markAsChanged();
	}

	/**
	 * @param supplierCode The supplierCode to set.
	 */
	public final void setSupplierCode(final String supplierCode) {
		this.supplierCode = supplierCode;
		super.markAsChanged();
	}

	/**
	 * @param color The color to set.
	 */
	public final void setColor(final int color) {
		this.color = color;
		super.markAsChanged();
	}

	/**
	 * @param mark The mark to set.
	 */
	public final void setMark(final String mark) {
		this.mark = mark;
		super.markAsChanged();
	}

	public final int getColor() {
		return this.color;
	}

	public final String getMark() {
		return this.mark;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public final Set<Identifiable> getDependencies() {
		return Collections.singleton((Identifiable) this.type);
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public final void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public final void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public final Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Set)
	 */
	public final void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Set)
	 */
	public final void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	public final String getDescription() {
		return this.description;
	}

	public final void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public final String getInventoryNo() {
		return this.inventoryNo;
	}

	public final String getName() {
		return this.name;
	}

	public final String getSupplier() {
		return this.supplier;
	}

	public final String getSupplierCode() {
		return this.supplierCode;
	}
}
