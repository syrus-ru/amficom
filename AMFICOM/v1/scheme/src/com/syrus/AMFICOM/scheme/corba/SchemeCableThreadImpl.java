/*
 * $Id: SchemeCableThreadImpl.java,v 1.17 2005/03/10 15:06:08 bass Exp $
 * Copyright ¿ 2004 Syrus Systems. Dept. of Science & Technology. Project:
 * AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;
import java.util.Collection;

/**
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2005/03/10 15:06:08 $
 * @module scheme_v1
 */
final class SchemeCableThreadImpl extends SchemeCableThread implements
		Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler
			.getInstance();

	private static final long serialVersionUID = 3618420423407186485L;

	SchemeCableThreadImpl() {
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
	public Identifier[] getDependencies() {
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
	 * @see IStorableObject#isChanged()
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
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(
			final StorableObjectFactory storableObjectFactory,
			final boolean changed) {
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

	protected Object clone() throws CloneNotSupportedException {
		final SchemeCableThreadImpl schemeCableThread = (SchemeCableThreadImpl) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableThread;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}
}
