/*
 * $Id: SchemeCableThread.java,v 1.6 2005/03/25 13:24:52 bass Exp $ Copyright ¿
 * 2004 Syrus Systems. Dept. of Science & Technology. Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/03/25 13:24:52 $
 * @module scheme_v1
 */
public final class SchemeCableThread extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 4050204133015171124L;

	protected Identifier cableThreadTypeId = null;

	protected Identifier characteristicIds[] = null;

	protected Identifier schemeCableLinkId = null;

	protected Identifier sourceSchemePortId = null;

	protected Identifier targetSchemePortId = null;

	private String description;

	private String name;

	protected Identifier threadId = null;

	/**
	 * @param id
	 */
	protected SchemeCableThread(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected SchemeCableThread(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public CableThreadType getCableThreadType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadTypeImpl
	 */
	public void setCableThreadType(CableThreadType newCableThreadTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Collection getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
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

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink schemeCablelink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCablelink
	 * @see com.syrus.AMFICOM.scheme.SchemeCableThread#schemeCablelink(com.syrus.AMFICOM.scheme.SchemeCableLink)
	 */
	public void schemeCablelink(SchemeCableLink newSchemeCablelink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePort
	 * @see SchemeCableThread#setSchemePort(SchemePort)
	 */
	public void setSchemePort(final SchemePort schemePort) {
		/**
		 * @todo Update w/o notification.
		 */
		throw new UnsupportedOperationException();
	}

	public SchemePort getSourceSchemePort() {
		throw new UnsupportedOperationException();
	}

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		throw new UnsupportedOperationException();
	}

	public SchemePort getTargetSchemePort() {
		throw new UnsupportedOperationException();
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		throw new UnsupportedOperationException();
	}

	public Link getThread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newThreadImpl
	 */
	public void setThread(Link newThreadImpl) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeCableThread schemeCableThread = (SchemeCableThread) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableThread;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemeCableThread createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeCableThread schemeCableThread = new SchemeCableThread(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeCableThread.changed = true;
			return schemeCableThread;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeCableThread.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCableThread createInstance() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}
}
