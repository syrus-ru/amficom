/*
 * $Id: Identifier.java,v 1.19 2004/12/21 13:56:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.19 $, $Date: 2004/12/21 13:56:56 $
 * @author $Author: bass $
 * @module general_v1
 */
public class Identifier implements
		Comparable,
		Cloneable,
		TransferableObject,
		Serializable {
	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#SEPARATOR
	 */
	public static final char SEPARATOR = '_';

	private static final long serialVersionUID = 1721559813677093072L;

	private String identifierString;

	private short major;
	private long minor;

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_string);
	}

	public Identifier(String identifierString) {
		this.major = ObjectEntities.stringToCode(identifierString.substring(0, identifierString.indexOf(SEPARATOR)));
		this.minor = Long.parseLong(identifierString.substring(identifierString.indexOf(SEPARATOR) + 1));
		this.identifierString = identifierString;
	}

	/*	Only for IdentifierGenerator	*/
	protected Identifier(short major, long minor) {
		this.major = major;
		this.minor = minor;
		this.identifierString = ObjectEntities.codeToString(this.major) + SEPARATOR + Long.toString(this.minor);
	}

	public Object clone() {
		Identifier id = null;
		try {
			id = (Identifier)super.clone();
		}
		catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		id.major = this.major;
		id.identifierString = new String(this.identifierString);
		return id;
	}

	/**
	 * @param anotherIdentifier
	 */
	public int compareTo(final Identifier anotherIdentifier) {
		final short thatMajor = anotherIdentifier.getMajor();
		final long thatMinor = anotherIdentifier.getMinor();
		return this.major == thatMajor
			? this.minor <= thatMinor ? this.minor < thatMinor ? -1 : 0 : 1
			: this.major - thatMajor;
	}

	/**
	 * @param o
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(final Object o) {
		return compareTo((Identifier) o);
	}

	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Identifier){
			Identifier that = (Identifier) obj;
			ret = ((that.major == this.major) && (that.minor == this.minor));
		}
		return ret;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#identifierString()
	 */
	public String getIdentifierString() {
		return this.identifierString; 
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#major()
	 */
	public short getMajor() {
		return this.major;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#minor()
	 */
	public long getMinor() {
		return this.minor;
	}

	public Object getTransferable() {
		return new Identifier_Transferable(this.identifierString);
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major;
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + this.identifierString.hashCode();
		return ret;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#hexString()
	 */
	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return this.identifierString;
	}
}
