/*
 * $Id: SchemePathImpl.java,v 1.14 2005/03/10 06:58:50 bass Exp $
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
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;
import java.util.Collection;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/03/10 06:58:50 $
 * @module scheme_v1
 */
final class SchemePathImpl extends SchemePath implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3978988764901029432L;

	SchemePathImpl() {
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(
			Characteristic_Transferable[] characteristics) {
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

	public SchemePath cloneInstance() {
		try {
			return (SchemePath) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement endDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEndDevice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#endDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
	 */
	public void endDevice(SchemeElement newEndDevice) {
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

	public long getCreated() {
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

	public long getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
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

	public PathElement[] links() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinks
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#links(com.syrus.AMFICOM.scheme.corba.PathElement[])
	 */
	public void links(PathElement[] newLinks) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPath_Transferable path() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPath
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#path(com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable)
	 */
	public void path(TransmissionPath_Transferable newPath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#pathImpl()
	 */
	public TransmissionPath pathImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPathImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#pathImpl(com.syrus.AMFICOM.configuration.TransmissionPath)
	 */
	public void pathImpl(TransmissionPath newPathImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemePath#scheme()
	 */
	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see SchemePath#scheme(Scheme)
	 */
	public void scheme(final Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
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

	public SchemeElement startDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newStartDevice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#startDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
	 */
	public void startDevice(SchemeElement newStartDevice) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPathType_Transferable type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#type(com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable)
	 */
	public void type(TransmissionPathType_Transferable newType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#typeImpl()
	 */
	public TransmissionPathType typeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#typeImpl(com.syrus.AMFICOM.configuration.TransmissionPathType)
	 */
	public void typeImpl(TransmissionPathType newTypeImpl) {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemePathImpl schemePath = (SchemePathImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePath;
	}
}
