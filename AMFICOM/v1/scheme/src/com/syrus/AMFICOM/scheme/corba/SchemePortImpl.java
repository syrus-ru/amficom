/*
 * $Id: SchemePortImpl.java,v 1.1 2004/11/24 10:03:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/24 10:03:58 $
 * @module schemecommon_v1
 */
final class SchemePortImpl extends SchemePort implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemePortImpl() {
	}

	SchemePortImpl(final Identifier id) {
		this.thisId = id;
	}

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] newCharacteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemePort cloneInstance() {
		try {
			return (SchemePort) this.clone();
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

	public void description(String newDescription) {
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

	public void name(String newName) {
		throw new UnsupportedOperationException();
	}

	public Port_Transferable port() {
		throw new UnsupportedOperationException();
	}

	public PortType_Transferable portType() {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice schemeDevice() {
		throw new UnsupportedOperationException();
	}

	public SchemeLink schemeLink() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
