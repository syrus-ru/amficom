/*
 * $Id: AbstractSchemePortImpl.java,v 1.6 2004/12/15 14:56:08 bass Exp $
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
import java.util.*;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemePort} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/12/15 14:56:08 $
 * @module schemecommon_v1
 */
final class AbstractSchemePortImpl extends AbstractSchemePort {
	private static final long serialVersionUID = 4761568761565209353L;

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	public void abstractSchemeLink(AbstractSchemeLink abstractSchemeLink) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	public List characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	public void characteristicsImpl(List characteristics) {
		throw new UnsupportedOperationException();
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
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

	public Date getCreated() {
		return createdImpl();
	}

	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		return modifiedImpl();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
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
	 * @see JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
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
