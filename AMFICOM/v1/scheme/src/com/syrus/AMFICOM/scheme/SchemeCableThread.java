/*-
 * $Id: SchemeCableThread.java,v 1.18 2005/04/22 15:39:26 bass Exp $
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
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
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

/**
 * #12 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/04/22 15:39:26 $
 * @module scheme_v1
 */
public final class SchemeCableThread extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 4050204133015171124L;

	private String name;

	private String description;

	private Identifier cableThreadTypeId;

	private Identifier linkId;

	private Identifier sourceSchemePortId;

	private Identifier targetSchemePortId;

	private Identifier parentSchemeCableLinkId;

	private Set characteristics;

	private SchemeCableThreadDatabase schemeCableThreadDatabase;

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
		/**
		 * @todo Add additional assertions.
		 * @todo Add a shorthand #createInstance()
		 */

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
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public Link getLink() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	public SchemePort getTargetSchemePort() {
		throw new UnsupportedOperationException();
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
	 * @param newCableThreadTypeImpl
	 */
	public void setCableThreadType(CableThreadType newCableThreadTypeImpl) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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

	/**
	 * @param schemePort
	 */
	public void setSchemePort(final SchemePort schemePort) {
		/**
		 * @todo Update w/o notification.
		 */
		throw new UnsupportedOperationException();
	}

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		throw new UnsupportedOperationException();
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		throw new UnsupportedOperationException();
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
