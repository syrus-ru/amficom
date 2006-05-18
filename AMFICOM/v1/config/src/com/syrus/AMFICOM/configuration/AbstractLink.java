/*-
 * $Id: AbstractLink.java,v 1.21.6.1 2006/05/18 17:46:15 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.21.6.1 $, $Date: 2006/05/18 17:46:15 $
 * @module config
 */
public abstract class AbstractLink
		extends DomainMember
		implements Characterizable, TypedObject<AbstractLinkType>,
		ReverseDependencyContainer {
	AbstractLinkType type;
	String name;
	String description;
	String inventoryNo;
	String supplier;
	String supplierCode;
	String mark;
	int color;

	AbstractLink() {
//		super();
	}

	AbstractLink(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			StorableObjectVersion version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version, domainId);
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
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	public final Set<Identifiable> getDependenciesTmpl() {
		return Collections.singleton((Identifiable) this.type);
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

	/**
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public final Set<Identifiable> getReverseDependencies()
	throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(this.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public final StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public final void addCharacteristic(final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public final void removeCharacteristic(
			final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public final Set<Characteristic> getCharacteristics()
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0());
	}

	/**
	 * @throws ApplicationException
	 */
	final Set<Characteristic> getCharacteristics0()
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees();
	}

	/**
	 * @param characteristics
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public final void setCharacteristics(final Set<Characteristic> characteristics)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0();

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic);
		}
	}
}
