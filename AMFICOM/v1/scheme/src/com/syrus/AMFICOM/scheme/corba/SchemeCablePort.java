/*
 * $Id: SchemeCablePort.java,v 1.4 2005/03/15 17:47:57 bass Exp $
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
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class SchemeCablePort extends AbstractSchemePort {

	/**
	 * @param id
	 */
	protected SchemeCablePort(Identifier id) {
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
	protected SchemeCablePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
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

	public Object clone() {
		final SchemeCablePort schemeCablePort = (SchemeCablePort) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCablePort;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemeCablePort createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeCablePort schemeCablePort = new SchemeCablePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeCablePort.changed = true;
			return schemeCablePort;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeCablePort.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCablePort createInstance() {
		throw new UnsupportedOperationException();
	}
}
