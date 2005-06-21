/*
 * $Id: AbstractSchemeElement.java,v 1.25 2005/06/21 12:44:31 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.25 $, $Date: 2005/06/21 12:44:31 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeElement extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
	static final long serialVersionUID = 4644766113809681630L;

	private String name;

	private String description;

	/**
	 * @todo It may be necessary to allow accessor and modifier be
	 *       overridden by descendants to add extra checks as SchemeElement
	 *       and SchemeLink may be enclosed not by Scheme only.
	 */
	Identifier parentSchemeId;

	private Set<Characteristic> characteristics;

	/**
	 * @param id
	 */
	AbstractSchemeElement(final Identifier id) {
		super(id);
		this.characteristics = new HashSet();
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param parentScheme
	 */
	AbstractSchemeElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);

		this.characteristics = new HashSet();
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemeElement() {
		// super();
	}

	/**
	 * @param characteristic
	 * @see Characterizable#addCharacteristic(Characteristic)
	 */
	public final void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public final Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.parentSchemeId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * Returns <code>Scheme</code> parent to this <code>SchemeLink</code>
	 * or <code>SchemeCableLlink</code> or <code>SchemeElement</code>.
	 * Descendants almost always need to override this.
	 *
	 * @see #parentSchemeId
	 */
	public Scheme getParentScheme() {
		try {
			return (Scheme) StorableObjectPool.getStorableObject(this.parentSchemeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @param characteristic
	 * @see Characterizable#removeCharacteristic(Characteristic)
	 */
	public final void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics(Set)
	 */
	public final void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics0(Set)
	 */
	public final void setCharacteristics0(final Set characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet(characteristics.size());
		else
			this.characteristics.clear();
		this.characteristics.addAll(characteristics);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * Descendants almost always need to override this.
	 *
	 * @see #parentSchemeId
	 */
	public void setParentScheme(final Scheme parentScheme) {
		if (parentScheme == null) {
			Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
			StorableObjectPool.delete(this.id);
			return;
		}
		final Identifier newParentSchemeId = parentScheme.getId();
		if (this.parentSchemeId.equals(newParentSchemeId))
			return;
		this.parentSchemeId = newParentSchemeId;
		super.markAsChanged();
	}

	/**
	 * @param header
	 * @param name1
	 * @param description1
	 * @param parentSchemeId1
	 * @param characteristicIds
	 * @throws CreateObjectException
	 */
	void fromTransferable(final IdlStorableObject header,
			final String name1, final String description1,
			final IdlIdentifier parentSchemeId1,
			final IdlIdentifier characteristicIds[])
			throws CreateObjectException {
		try {
			super.fromTransferable(header);
			this.setCharacteristics0(StorableObjectPool.getStorableObjects(Identifier.fromTransferables(characteristicIds), true));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = name1;
		this.description = description1;
		this.parentSchemeId = new Identifier(parentSchemeId1);
	}

	final synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description, final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeId != null: ErrorMessages.NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.parentSchemeId = parentSchemeId;
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * @todo Check whether this attribute is necessary.
	 */
	private transient boolean alarmed;

	/**
	 * Transient attribute
	 */
	public final boolean isAlarmed() {
		return this.alarmed;
	}

	/**
	 * Transient attribute
	 */
	public final void setAlarmed(boolean alarmed) {
		this.alarmed = alarmed;
	}
}
