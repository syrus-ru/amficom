/*-
 * $Id: AbstractSchemeElement.java,v 1.78 2005/12/17 12:11:19 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemeElement;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.78 $, $Date: 2005/12/17 12:11:19 $
 * @module scheme
 */
public abstract class AbstractSchemeElement<T extends AbstractSchemeElement<T>>
		extends AbstractCloneableStorableObject<T>
		implements Describable, Characterizable,
		ReverseDependencyContainer {
	static final long serialVersionUID = 4644766113809681630L;

	private String name;

	private String description;

	/**
	 * @todo It may be necessary to allow accessor and modifier be
	 *       overridden by descendants to add extra checks as SchemeElement
	 *       and SchemeLink may be enclosed not by Scheme only.
	 */
	Identifier parentSchemeId;

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
			final Identifier modifierId, 
			final StorableObjectVersion version, final String name,
			final String description, final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);
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
	AbstractSchemeElement(final XmlIdentifier id,
			final String importType, final short entityCode,
			final Date created, final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemeElement() {
		// super();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeId);
		return dependencies;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeId() {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeId.isVoid() || this.parentSchemeId.getMajor() == SCHEME_CODE;
		return this.parentSchemeId;
	}

	/**
	 * <p>A wrapper around {@link #getParentSchemeId()}.</p>
	 *
	 * <p>Returns <code>Scheme</code> parent to this <code>SchemeLink</code>
	 * or <code>SchemeCableLlink</code> or <code>SchemeElement</code>.
	 * Descendants almost always need to override
	 * {@link #getParentSchemeId()}.</p>
	 */
	public final Scheme getParentScheme() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentScheme(Scheme, boolean)}.
	 *
	 * @param parentSchemeId
	 * @param usePool
	 * @throws ApplicationException
	 */
	final void setParentSchemeId(final Identifier parentSchemeId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeId != null : NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() || parentSchemeId.getMajor() == SCHEME_CODE;

		if (this.parentSchemeId.equals(parentSchemeId)) {
			return;
		}

		this.setParentScheme(
				StorableObjectPool.<Scheme>getStorableObject(parentSchemeId, true),
				usePool);
	}

	/**
	 * @param parentScheme
	 * @param usePool
	 * @throws ApplicationException
	 */
	public abstract void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException;

	/**
	 * @param abstractSchemeElement
	 * @throws CreateObjectException
	 */
	final void fromTransferable(
			final IdlAbstractSchemeElement abstractSchemeElement)
	throws CreateObjectException {
		try {
			super.fromTransferable(abstractSchemeElement);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = abstractSchemeElement.name;
		this.description = abstractSchemeElement.description;
		this.parentSchemeId = new Identifier(abstractSchemeElement.parentSchemeId);
	}

	/**
	 * @param abstractSchemeElement
	 * @param importType
	 * @throws CreateObjectException
	 */
	final void fromXmlTransferable(
			final XmlAbstractSchemeElement abstractSchemeElement,
			final String importType)
	throws CreateObjectException {
		this.name = abstractSchemeElement.getName();
		this.description = abstractSchemeElement.isSetDescription()
				? abstractSchemeElement.getDescription()
				: "";
		if (abstractSchemeElement.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : abstractSchemeElement.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}
	}

	final void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version, final String name,
			final String description,
			final Identifier parentSchemeId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
			assert description != null: NON_NULL_EXPECTED;
			assert parentSchemeId != null: NON_NULL_EXPECTED;
	
			this.name = name;
			this.description = description;
			this.parentSchemeId = parentSchemeId;
		}
	}

	/**
	 * @param abstractSchemeElement
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @throws ApplicationException 
	 */
	final void getXmlTransferable(
			final XmlAbstractSchemeElement abstractSchemeElement,
			final String importType,
			final boolean usePool)
	throws XmlConversionException, ApplicationException {
		super.id.getXmlTransferable(abstractSchemeElement.addNewId(), importType);
		abstractSchemeElement.setName(this.name);
		if (abstractSchemeElement.isSetDescription()) {
			abstractSchemeElement.unsetDescription();
		}
		if (this.description.length() != 0) {
			abstractSchemeElement.setDescription(this.description);
		}
		final Set<Characteristic> characteristics = this.getCharacteristics0(usePool);
		if (abstractSchemeElement.isSetCharacteristics()) { 
			abstractSchemeElement.unsetCharacteristics();
		}
		if (!characteristics.isEmpty()) {
			final XmlCharacteristicSeq characteristicSeq = abstractSchemeElement.addNewCharacteristics();
			for (final Characteristic characteristic : characteristics) {
				characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
			}
		}
	}

	/**
	 * Returns the nearest {@link Scheme} that&apos;s parent to this object.
	 * On the contrary to {@link #getParentScheme()}, this method is
	 * guaranteed to return a non-{@code null} value.
	 *
	 * @return the nearest {@link Scheme} that&apos;s parent to this object.
	 * @throws ApplicationException
	 */
	public abstract Scheme getNearestParentScheme() throws ApplicationException;

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

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

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop.
	 *
	 * @todo Check whether this attribute is necessary.
	 */
	private boolean alarmed;

	/**
	 * Transient attribute
	 */
	public final boolean isAlarmed() {
		return this.alarmed;
	}

	/**
	 * Transient attribute
	 */
	public final void setAlarmed(final boolean alarmed) {
		this.alarmed = alarmed;
	}
}
