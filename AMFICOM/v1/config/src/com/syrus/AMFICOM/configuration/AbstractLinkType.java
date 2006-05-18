/*
 * $Id: AbstractLinkType.java,v 1.33.6.1 2006/05/18 17:46:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
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

import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * @version $Revision: 1.33.6.1 $, $Date: 2006/05/18 17:46:15 $
 * @author $Author: bass $
 * @module config
 */
public abstract class AbstractLinkType
		extends StorableObjectType
		implements Namable, Characterizable, ReverseDependencyContainer {
	private static final long serialVersionUID = 6276017738364160981L;

	AbstractLinkType(/*IdlAbstractLinkType*/) {
		// super();
	}

	protected AbstractLinkType(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, created, modified, creatorId, modifierId, version, codename, description);		
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
	AbstractLinkType(final XmlIdentifier id,
			final String importType, final short entityCode,
			final Date created, final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	public abstract Identifier getImageId();

	public abstract String getManufacturer();

	public abstract void setManufacturer(final String manufacturer);

	public abstract String getManufacturerCode();

	public abstract void setManufacturerCode(final String manufacturerCode);

	public abstract LinkTypeSort getSort();

	public abstract String getName();

	public abstract void setName(final String Name);

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
