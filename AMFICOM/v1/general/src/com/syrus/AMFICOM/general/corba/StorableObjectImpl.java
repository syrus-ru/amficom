/*
 * $Id: StorableObjectImpl.java,v 1.2 2004/11/23 09:05:53 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly.
 *
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/11/23 09:05:53 $
 * @deprecated Use other implementations of {@link StorableObject} instead.
 * @module general_v1
 */
final class StorableObjectImpl extends StorableObject {
	private static final long serialVersionUID = 3675974125608086175L;

	public long created() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}
}
