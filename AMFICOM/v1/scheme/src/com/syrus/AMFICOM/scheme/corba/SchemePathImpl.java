/*
 * $Id: SchemePathImpl.java,v 1.8 2004/12/21 15:35:01 bass Exp $
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
 * @version $Revision: 1.8 $, $Date: 2004/12/21 15:35:01 $
 * @module scheme_v1
 */
final class SchemePathImpl extends SchemePath implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3978988764901029432L;

	SchemePathImpl() {
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
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
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

	public long version() {
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
