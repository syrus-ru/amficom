/*
 * $Id: AbstractSchemeElement.java,v 1.8 2005/04/08 09:26:11 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.*;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/04/08 09:26:11 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeElement extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
	static final long serialVersionUID = 4644766113809681630L;

	/**
	 * @todo Check whether this attribute is necessary.
	 */
	private transient boolean alarmed;

	private Set characteristics;

	private String description;

	private String name;

	/**
	 * @todo It may be necessary to allow accessor and modifier be
	 *       overridden by descendants to add extra checks as SchemeElement
	 *       and SchemeLink may be enclosed not by Scheme only.
	 */
	private Identifier parentSchemeId;

	/**
	 * @param id
	 */
	protected AbstractSchemeElement(final Identifier id) {
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
	 */
	protected AbstractSchemeElement(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	protected AbstractSchemeElement() {
		// super();
	}

	/**
	 * @param characteristic
	 * @see Characterizable#addCharacteristic(Characteristic)
	 */
	public final void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert !getCharacteristics().contains(characteristic): ErrorMessages.COLLECTION_IS_A_SET;
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public final Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * Getter returns scheme parent to this scheme link or scheme cable link
	 * or scheme element.
	 * 
	 * @see #parentSchemeId
	 */
	public final Scheme getParentScheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Transient attribute
	 */
	public final boolean isAlarmed() {
		return this.alarmed;
	}

	/**
	 * @param characteristic
	 * @see Characterizable#removeCharacteristic(Characteristic)
	 */
	public final void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		this.changed = true;
	}

	/**
	 * Transient attribute
	 */
	public final void setAlarmed(boolean alarmed) {
		this.alarmed = alarmed;
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics(Set)
	 */
	public final void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics0(Set)
	 */
	public final void setCharacteristics0(final Set characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.clear();
		this.characteristics.addAll(characteristics);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * Getter returns scheme parent to this scheme link or scheme cable link
	 * or scheme element.
	 * 
	 * @see #parentSchemeId
	 */
	public final void setParentScheme(final Scheme parentScheme) {
		throw new UnsupportedOperationException();
	}
}
