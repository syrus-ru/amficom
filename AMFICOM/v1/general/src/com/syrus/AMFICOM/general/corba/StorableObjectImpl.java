/*
 * $Id: StorableObjectImpl.java,v 1.5 2004/12/15 14:39:51 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2004/12/15 14:39:51 $
 * @module general_v1
 */
final class StorableObjectImpl extends StorableObject {
	private static final long serialVersionUID = 3675974125608086175L;

	/**
	 * @see java.util.JavaUtilIStorableObject#created()
	 */
	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		return createdImpl();
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
		return modifiedImpl();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifiable#id()
	 */
	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#modified()
	 */
	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#version()
	 */
	public long version() {
		throw new UnsupportedOperationException();
	}
}
