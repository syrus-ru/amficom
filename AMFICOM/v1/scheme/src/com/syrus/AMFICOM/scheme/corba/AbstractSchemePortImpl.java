/*
 * $Id: AbstractSchemePortImpl.java,v 1.2 2004/11/23 15:46:47 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly.
 *
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/11/23 15:46:47 $
 * @deprecated Use other implementations of {@link AbstractSchemePort} instead.
 * @module schemecommon_v1
 */
final class AbstractSchemePortImpl extends AbstractSchemePort {
	private static final long serialVersionUID = 4761568761565209353L;

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
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

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public MeasurementPort_Transferable measurementPort() {
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType_Transferable measurementPortType() {
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

	public PortType_Transferable portType() {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice schemeDevice() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}
}
