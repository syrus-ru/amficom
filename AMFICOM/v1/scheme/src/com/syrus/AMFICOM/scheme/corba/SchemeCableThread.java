/*
 * $Id: SchemeCableThread.java,v 1.3 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public final class SchemeCableThread extends CloneableStorableObject implements Namable,
		Describable, Characterizable,
		ComSyrusAmficomConfigurationSchemeCableThread {

	protected Identifier cableThreadTypeId = null;

	protected Identifier characteristicIds[] = null;

	protected Identifier schemeCableLinkId = null;

	protected Identifier sourceSchemePortId = null;

	protected Identifier targetSchemePortId = null;

	protected String thisDescription = null;

	protected String thisName = null;

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

	public CableThreadType_Transferable cableThreadType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadType(com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable)
	 */
	public void cableThreadType(
			CableThreadType_Transferable newCableThreadType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadTypeImpl()
	 */
	public CableThreadType cableThreadTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadTypeImpl(com.syrus.AMFICOM.configuration.CableThreadType)
	 */
	public void cableThreadTypeImpl(CableThreadType newCableThreadTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
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

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeDevice
	 * @see SchemeCableThread#getSchemePort(SchemeDevice)
	 */
	public SchemePort getSchemePort(final SchemeDevice schemeDevice) {
		assert schemeDevice != null;
		final SchemePort sourceSchemePort = sourceSchemePort();
		final SchemePort targetSchemePort = targetSchemePort();
		final Identifier sourceSchemeDeviceId = sourceSchemePort
				.schemeDevice().getId();
		final Identifier targetSchemeDeviceId = targetSchemePort
				.schemeDevice().getId();
		final Identifier schemeDeviceId = schemeDevice.getId();
		if (schemeDeviceId.equals(sourceSchemeDeviceId))
			return sourceSchemePort;
		else if (schemeDeviceId.equals(targetSchemeDeviceId))
			return targetSchemePort;
		else
			throw new IllegalArgumentException(
					"This scheme cable thread is in no way connected to the scheme device specified."); //$NON-NLS-1$
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableThread#schemeCablelink(com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
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

	/**
	 * @see SchemeCableThread#sourceSchemePort()
	 */
	public SchemePort sourceSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param sourceSchemePort
	 * @see SchemeCableThread#sourceSchemePort(SchemePort)
	 */
	public void sourceSchemePort(final SchemePort sourceSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCableThread#targetSchemePort()
	 */
	public SchemePort targetSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param targetSchemePort
	 * @see SchemeCableThread#targetSchemePort(SchemePort)
	 */
	public void targetSchemePort(final SchemePort targetSchemePort) {
		throw new UnsupportedOperationException();
	}

	public Link_Transferable thread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newThread
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#thread(com.syrus.AMFICOM.configuration.corba.Link_Transferable)
	 */
	public void thread(Link_Transferable newThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#threadImpl()
	 */
	public Link threadImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newThreadImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#threadImpl(com.syrus.AMFICOM.configuration.Link)
	 */
	public void threadImpl(Link newThreadImpl) {
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

	public static SchemeCableThread createInstance(final Identifier creatorId)
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
}
