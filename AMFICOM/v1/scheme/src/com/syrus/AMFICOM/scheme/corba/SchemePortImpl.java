/*
 * $Id: SchemePortImpl.java,v 1.15 2005/03/10 15:18:01 bass Exp $ Copyright ¿
 * 2004 Syrus Systems. Dept. of Science & Technology. Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;
import java.util.Collection;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/03/10 15:18:01 $
 * @module scheme_v1
 */
final class SchemePortImpl extends SchemePort implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler
			.getInstance();

	private static final long serialVersionUID = 3834586612762751537L;

	SchemePortImpl() {
	}

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newAbstractSchemeLink
	 * @see com.syrus.AMFICOM.scheme.corba.AbstractSchemePort#abstractSchemeLink(com.syrus.AMFICOM.scheme.corba.AbstractSchemeLink)
	 */
	public void abstractSchemeLink(AbstractSchemeLink newAbstractSchemeLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public DirectionType directionType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDirectionType
	 * @see com.syrus.AMFICOM.scheme.corba.AbstractSchemePort#directionType(com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType)
	 */
	public void directionType(DirectionType newDirectionType) {
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

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public MeasurementPort_Transferable measurementPort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPort
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPort(com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable)
	 */
	public void measurementPort(
			MeasurementPort_Transferable newMeasurementPort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPortImpl()
	 */
	public MeasurementPort measurementPortImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPortImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPortImpl(com.syrus.AMFICOM.configuration.MeasurementPort)
	 */
	public void measurementPortImpl(MeasurementPort newMeasurementPortImpl) {
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType_Transferable measurementPortType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPortType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPortType(com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable)
	 */
	public void measurementPortType(
			MeasurementPortType_Transferable newMeasurementPortType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPortTypeImpl()
	 */
	public MeasurementPortType measurementPortTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPortTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#measurementPortTypeImpl(com.syrus.AMFICOM.configuration.MeasurementPortType)
	 */
	public void measurementPortTypeImpl(
			MeasurementPortType newMeasurementPortTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public Port_Transferable port() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPort
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#port(com.syrus.AMFICOM.configuration.corba.Port_Transferable)
	 */
	public void port(Port_Transferable newPort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#portImpl()
	 */
	public Port portImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPortImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#portImpl(com.syrus.AMFICOM.configuration.Port)
	 */
	public void portImpl(Port newPortImpl) {
		throw new UnsupportedOperationException();
	}

	public PortType_Transferable portType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPortType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#portType(com.syrus.AMFICOM.configuration.corba.PortType_Transferable)
	 */
	public void portType(PortType_Transferable newPortType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#portTypeImpl()
	 */
	public PortType portTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPortTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort#portTypeImpl(com.syrus.AMFICOM.configuration.PortType)
	 */
	public void portTypeImpl(PortType newPortTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCableThread
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePort#schemeCableThread(com.syrus.AMFICOM.scheme.corba.SchemeCableThread)
	 */
	public void schemeCableThread(SchemeCableThread newSchemeCableThread) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice schemeDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeDevice
	 * @see com.syrus.AMFICOM.scheme.corba.AbstractSchemePort#schemeDevice(com.syrus.AMFICOM.scheme.corba.SchemeDevice)
	 */
	public void schemeDevice(SchemeDevice newSchemeDevice) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink schemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeLink
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePort#schemeLink(com.syrus.AMFICOM.scheme.corba.SchemeLink)
	 */
	public void schemeLink(SchemeLink newSchemeLink) {
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

	protected Object clone() throws CloneNotSupportedException {
		final SchemePortImpl schemePort = (SchemePortImpl) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePort;
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
