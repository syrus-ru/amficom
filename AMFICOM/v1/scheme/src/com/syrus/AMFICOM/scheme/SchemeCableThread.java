/*-
 * $Id: SchemeCableThread.java,v 1.24 2005/04/28 15:27:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemeCableThread_Transferable;
import com.syrus.util.Log;

/**
 * #12 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.24 $, $Date: 2005/04/28 15:27:03 $
 * @module scheme_v1
 */
public final class SchemeCableThread extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 4050204133015171124L;

	private String name;

	private String description;

	private Identifier cableThreadTypeId;

	private Identifier linkId;

	Identifier sourceSchemePortId;

	Identifier targetSchemePortId;

	private Identifier parentSchemeCableLinkId;

	private SchemeCableThreadDatabase schemeCableThreadDatabase;

	private Set characteristics;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableThread(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();
		this.schemeCableThreadDatabase = SchemeDatabaseContext.getSchemeCableThreadDatabase();
		try {
			this.schemeCableThreadDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
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
	 * @param cableThreadType
	 * @param link
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeCableLink
	 */
	SchemeCableThread(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final CableThreadType cableThreadType, final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.cableThreadTypeId = Identifier.possiblyVoid(cableThreadType);
		this.linkId = Identifier.possiblyVoid(link);
		this.sourceSchemePortId = Identifier.possiblyVoid(sourceSchemePort);
		this.targetSchemePortId = Identifier.possiblyVoid(targetSchemePort);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCableThread(final SchemeCableThread_Transferable transferable) throws CreateObjectException {
		this.schemeCableThreadDatabase = SchemeDatabaseContext.getSchemeCableThreadDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, CableThreadType, Link, SchemePort, SchemePort, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param cableThreadType
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(final Identifier creatorId, final String name, final CableThreadType cableThreadType, final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		return createInstance(creatorId, name, "", cableThreadType, //$NON-NLS-1$
				null, null, null, parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param cableThreadType
	 * @param link
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(
			final Identifier creatorId, final String name,
			final String description,
			final CableThreadType cableThreadType, final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert cableThreadType != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableThread schemeCableThread = new SchemeCableThread(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, cableThreadType,
					link, sourceSchemePort,
					targetSchemePort, parentSchemeCableLink);
			schemeCableThread.changed = true;
			return schemeCableThread;
		} catch (final IdentifierGenerationException ioee) {
			throw new CreateObjectException(
					"SchemeCableThread.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	public Object clone() {
		final SchemeCableThread schemeCableThread = (SchemeCableThread) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableThread;
	}

	public CableThreadType getCableThreadType() {
		assert this.cableThreadTypeId != null && !this.cableThreadTypeId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		try {
			return (CableThreadType) ConfigurationStorableObjectPool.getStorableObject(this.cableThreadTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLETHREAD;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.cableThreadTypeId != null && this.linkId != null
				&& this.sourceSchemePortId != null
				&& this.targetSchemePortId != null
				&& this.parentSchemeCableLinkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.cableThreadTypeId);
		dependencies.add(this.linkId);
		dependencies.add(this.sourceSchemePortId);
		dependencies.add(this.targetSchemePortId);
		dependencies.add(this.parentSchemeCableLinkId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public Link getLink() {
		assert this.linkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return this.linkId.isVoid()
					? null
					: (Link) ConfigurationStorableObjectPool.getStorableObject(this.linkId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeCableLink getParentSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeDevice
	 */
	public SchemePort getSchemePort(final SchemeDevice schemeDevice) {
		assert schemeDevice != null;
		final SchemePort sourceSchemePort = getSourceSchemePort();
		final SchemePort targetSchemePort = getTargetSchemePort();
		final Identifier sourceSchemeDeviceId = sourceSchemePort
				.getParentSchemeDevice().getId();
		final Identifier targetSchemeDeviceId = targetSchemePort
				.getParentSchemeDevice().getId();
		final Identifier schemeDeviceId = schemeDevice.getId();
		if (schemeDeviceId.equals(sourceSchemeDeviceId))
			return sourceSchemePort;
		else if (schemeDeviceId.equals(targetSchemeDeviceId))
			return targetSchemePort;
		else
			throw new IllegalArgumentException(
					"This scheme cable thread is in no way connected to the scheme device specified."); //$NON-NLS-1$
	}

	public SchemePort getSourceSchemePort() {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.sourceSchemePortId.isVoid()
				|| !this.sourceSchemePortId.equals(this.targetSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return this.sourceSchemePortId.isVoid()
					? null
					: (SchemePort) SchemeStorableObjectPool.getStorableObject(this.sourceSchemePortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemePort getTargetSchemePort() {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.targetSchemePortId.isVoid()
				|| !this.targetSchemePortId.equals(this.sourceSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return this.targetSchemePortId.isVoid()
					? null
					: (SchemePort) SchemeStorableObjectPool.getStorableObject(this.targetSchemePortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		this.changed = true;
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param cableThreadTypeId
	 * @param linkId
	 * @param sourceSchemePortId
	 * @param targetSchemePortId
	 * @param parentSchemeCableLinkId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier cableThreadTypeId,
			final Identifier linkId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		/**
		 * @todo Add additional assertions.
		 */
		assert cableThreadTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert linkId != null;
		assert sourceSchemePortId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert targetSchemePortId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId != null: ErrorMessages.NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.cableThreadTypeId = cableThreadTypeId;
		this.linkId = linkId;
		this.sourceSchemePortId = sourceSchemePortId;
		this.targetSchemePortId = targetSchemePortId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @param cableThreadType
	 */
	public void setCableThreadType(final CableThreadType cableThreadType) {
		assert this.cableThreadTypeId != null && !this.cableThreadTypeId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert cableThreadType != null: ErrorMessages.NON_NULL_EXPECTED;

		final Identifier newCableThreadTypeId = cableThreadType.getId();
		if (this.cableThreadTypeId.equals(newCableThreadTypeId))
			return;
		this.cableThreadTypeId = newCableThreadTypeId;
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
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
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	public void setLink(final Link link) {
		final Identifier newLinkId = Identifier.possiblyVoid(link);
		if (this.linkId.equals(newLinkId))
			return;
		this.linkId = newLinkId;
		super.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		this.changed = true;
	}

	public void setParentSchemeCableLink(final SchemeCableLink parentSchemeCableLink) {
		throw new UnsupportedOperationException();
	}

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.sourceSchemePortId.isVoid()
				|| !this.sourceSchemePortId.equals(this.targetSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		final Identifier newSourceSchemePortId = Identifier.possiblyVoid(sourceSchemePort);
		assert newSourceSchemePortId.isVoid()
				|| !newSourceSchemePortId.equals(this.targetSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		if (this.sourceSchemePortId.equals(newSourceSchemePortId))
			return;
		this.sourceSchemePortId = newSourceSchemePortId;
		super.changed = true;
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.targetSchemePortId.isVoid()
				|| !this.targetSchemePortId.equals(this.sourceSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		final Identifier newTargetSchemePortId = Identifier.possiblyVoid(targetSchemePort);
		assert newTargetSchemePortId.isVoid()
				|| !newTargetSchemePortId.equals(this.sourceSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		if (this.targetSchemePortId.equals(newTargetSchemePortId))
			return;
		this.targetSchemePortId = newTargetSchemePortId;
		super.changed = true;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
