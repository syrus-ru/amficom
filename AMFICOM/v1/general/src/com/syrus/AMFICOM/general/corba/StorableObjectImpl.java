/*
 * $Id: StorableObjectImpl.java,v 1.4 2004/11/29 10:24:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import java.util.*;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link StorableObject} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/11/29 10:24:29 $
 * @module general_v1
 */
final class StorableObjectImpl extends StorableObject {
	private static final long serialVersionUID = 3675974125608086175L;

	public long created() {
		throw new UnsupportedOperationException();
	}

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	public com.syrus.AMFICOM.general.Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public com.syrus.AMFICOM.general.Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	public com.syrus.AMFICOM.general.Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}
}
