/*
 * $Id: SchemeCableThreadImpl.java,v 1.9 2004/12/21 16:41:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2004/12/21 16:41:17 $
 * @module scheme_v1
 */
final class SchemeCableThreadImpl extends SchemeCableThread implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3618420423407186485L;

	SchemeCableThreadImpl() {
	}

	public CableThreadType_Transferable cableThreadType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadType(com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable)
	 */
	public void cableThreadType(CableThreadType_Transferable newCableThreadType) {
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

	/**
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl()
	 */
	public CharacteristicSeqContainer characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl(CharacteristicSeqContainer)
	 */
	public void characteristicsImpl(final CharacteristicSeqContainer characteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread cloneInstance() {
		try {
			return (SchemeCableThread) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#dependencies()
	 */
	public StorableObject[] dependencies() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
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

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeCableThreadImpl schemeCableThread = (SchemeCableThreadImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableThread;
	}

	/**
	 * @param schemeDevice
	 * @see SchemeCableThread#getSchemePort(SchemeDevice)
	 */
	public SchemePort getSchemePort(final SchemeDevice schemeDevice) {
		assert schemeDevice != null;
		final SchemePort sourceSchemePort = sourceSchemePort();
		final SchemePort targetSchemePort = targetSchemePort();
		final Identifier sourceSchemeDeviceId = sourceSchemePort.schemeDevice().id();
		final Identifier targetSchemeDeviceId = targetSchemePort.schemeDevice().id();
		final Identifier schemeDeviceId = schemeDevice.id();
		if (schemeDeviceId.equals(sourceSchemeDeviceId))
			return sourceSchemePort;
		else if (schemeDeviceId.equals(targetSchemeDeviceId))
			return targetSchemePort;
		else
			throw new IllegalArgumentException("This scheme cable thread is in no way connected to the scheme device specified.");
	}

	/**
	 * @param schemeDevice
	 * @param schemePort
	 * @see SchemeCableThread#setSchemePort(SchemeDevice, SchemePort)
	 */
	public void setSchemePort(final SchemeDevice schemeDevice, final SchemePort schemePort) {
		/**
		 * @todo Update w/o notification.
		 */
		throw new UnsupportedOperationException();
	}
}
