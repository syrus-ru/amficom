/*
 * $Id: Identifier.java,v 1.22 2005/03/11 17:26:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @version $Revision: 1.22 $, $Date: 2005/03/11 17:26:28 $
 * @author $Author: bass $
 * @module general_v1
 */
public class Identifier implements
		Comparable,
		TransferableObject,
		Serializable {
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

	public String getIdentifierString() {
		return this.identifierString; 
	}

	public short getMajor() {
		return this.major;
	}

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

	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return this.identifierString;
	}
}
