/*
 * $Id: StorableObjectImpl.java,v 1.7 2004/12/21 13:56:56 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link StorableObject} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/12/21 13:56:56 $
 * @module general_v1
 */
final class StorableObjectImpl extends StorableObject {
	private static final long serialVersionUID = 3675974125608086175L;

	/**
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#created()
	 */
	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#dependencies()
	 */
	public StorableObject[] dependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#id()
	 */
	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modified()
	 */
	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#version()
	 */
	public long version() {
		throw new UnsupportedOperationException();
	}
}
