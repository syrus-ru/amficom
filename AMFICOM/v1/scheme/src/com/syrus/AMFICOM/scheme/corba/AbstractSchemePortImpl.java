/*
 * $Id: AbstractSchemePortImpl.java,v 1.10 2005/01/20 09:58:02 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemePort} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/01/20 09:58:02 $
 * @module scheme_v1
 */
final class AbstractSchemePortImpl extends AbstractSchemePort {
	private static final long serialVersionUID = 4761568761565209353L;

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	public void abstractSchemeLink(AbstractSchemeLink abstractSchemeLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	public CharacteristicSeqContainer characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	public void characteristicsImpl(final CharacteristicSeqContainer characteristics) {
		throw new UnsupportedOperationException();
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

	public StorableObject[] dependencies() {
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

	public void directionType(DirectionType directionType) {
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public MeasurementPort_Transferable measurementPort() {
		throw new UnsupportedOperationException();
	}

	public void measurementPort(MeasurementPort_Transferable measurementPort) {
		throw new UnsupportedOperationException();
	}

	public MeasurementPort measurementPortImpl() {
		throw new UnsupportedOperationException();
	}

	public void measurementPortImpl(MeasurementPort measurementPort) {
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType_Transferable measurementPortType() {
		throw new UnsupportedOperationException();
	}

	public void measurementPortType(MeasurementPortType_Transferable measurementPortType) {
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType measurementPortTypeImpl() {
		throw new UnsupportedOperationException();
	}

	public void measurementPortTypeImpl(MeasurementPortType measurementPortType) {
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

	public Port_Transferable port() {
		throw new UnsupportedOperationException();
	}

	public void port(Port_Transferable port) {
		throw new UnsupportedOperationException();
	}

	public Port portImpl() {
		throw new UnsupportedOperationException();
	}

	public void portImpl(Port port) {
		throw new UnsupportedOperationException();
	}

	public PortType_Transferable portType() {
		throw new UnsupportedOperationException();
	}

	public void portType(PortType_Transferable portType) {
		throw new UnsupportedOperationException();
	}

	public PortType portTypeImpl() {
		throw new UnsupportedOperationException();
	}

	public void portTypeImpl(PortType portType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice schemeDevice() {
		throw new UnsupportedOperationException();
	}

	public void schemeDevice(SchemeDevice schemeDevice) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}
}
