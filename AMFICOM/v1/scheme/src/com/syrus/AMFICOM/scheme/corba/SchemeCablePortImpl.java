/*
 * $Id: SchemeCablePortImpl.java,v 1.4 2004/12/15 13:47:41 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/12/15 13:47:41 $
 * @module schemecommon_v1
 */
final class SchemeCablePortImpl extends SchemeCablePort implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeCablePortImpl() {
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
	 * @see #characteristicsImpl()
	 */
	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristicsImpl(List)
	 */
	public void characteristics(final Characteristic_Transferable characteristics[]) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl()
	 */
	public List characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCharacteristicsImpl
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl(java.util.List)
	 */
	public void characteristicsImpl(List newCharacteristicsImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort cloneInstance() {
		try {
			return (SchemeCablePort) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
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
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
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
	public void measurementPort(MeasurementPort_Transferable newMeasurementPort) {
		throw new UnsupportedOperationException();
	}

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
	public void measurementPortType(MeasurementPortType_Transferable newMeasurementPortType) {
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
	public void measurementPortTypeImpl(MeasurementPortType newMeasurementPortTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public long modified() {
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

	public SchemeCableLink schemeCableLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCableLink
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCablePort#schemeCableLink(com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void schemeCableLink(SchemeCableLink newSchemeCableLink) {
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

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
