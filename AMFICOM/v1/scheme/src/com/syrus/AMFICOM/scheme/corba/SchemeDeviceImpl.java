/*
 * $Id: SchemeDeviceImpl.java,v 1.1 2004/11/24 10:03:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/24 10:03:58 $
 * @module schemecommon_v1
 */
final class SchemeDeviceImpl extends SchemeDevice implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeDeviceImpl() {
	}

	SchemeDeviceImpl(final Identifier id) {
		this.thisId = id;
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] newCharacteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice cloneInstance() {
		try {
			return (SchemeDevice) this.clone();
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

	public Identifier id() {
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

	public SchemeCablePort[] schemeCablePorts() {
		throw new UnsupportedOperationException();
	}

	public SchemePort[] schemePorts() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
