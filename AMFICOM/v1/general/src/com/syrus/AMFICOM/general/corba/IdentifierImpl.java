/*
 * $Id: IdentifierImpl.java,v 1.7 2005/03/01 13:59:24 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.HashCodeGenerator;
import java.io.*;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/03/01 13:59:24 $
 * @module general_v1
 */
final class IdentifierImpl extends Identifier implements Comparable {
	private static final long serialVersionUID = 8435961337633429780L;

/*/	// #ifdef NUMERIC_IDENTIFIER
	private transient String identifierString = IDENTIFIER_STRING_UNINITIALIZED; 
/*/	// #else // NUMERIC_IDENTIFIER
	private transient short major = MAJOR_UNINITIALIZED;
	private transient long minor = MINOR_UNINITIALIZED;
//*/	// #endif // NUMERIC_IDENTIFIER

	/**
	 * This constructor is only used by value factory and shouldn't be
	 * invoked directly.
	 */
	IdentifierImpl() {
		// empty
	}

	IdentifierImpl(final Identifier_Transferable id) {
		this(id.identifier_string);
	}

	IdentifierImpl(final short major, final long minor) {
		this.major = major;
		this.minor = minor;
		this.identifierString = ObjectEntities.codeToString(major) + SEPARATOR + minor;
	}

	IdentifierImpl(final String identifierString) {
		final int indexOfSeparator = identifierString.indexOf(SEPARATOR);
		this.major = ObjectEntities.stringToCode(identifierString.substring(0, indexOfSeparator));
		this.minor = Long.parseLong(identifierString.substring(indexOfSeparator + 1));
		this.identifierString = identifierString;
	}

	/**
	 * @param anotherIdentifierImpl
	 */
	public int compareTo(final IdentifierImpl anotherIdentifierImpl) {
		final short thisMajor = this.getMajor();
		final long thisMinor = this.getMinor();
		final short thatMajor = anotherIdentifierImpl.getMajor();
		final long thatMinor = anotherIdentifierImpl.getMinor();
		return thisMajor == thatMajor
			? thisMinor <= thatMinor ? thisMinor < thatMinor ? -1 : 0 : 1
			: thisMajor - thatMajor;
	}

	/**
	 * @param o
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(final Object o) {
		return compareTo((IdentifierImpl) o);
	}

	public boolean equals(final Object obj) {
		if (obj instanceof IdentifierImpl) {
			final IdentifierImpl that = (IdentifierImpl) obj;
			return (this.getMajor() == that.getMajor())
				&& (this.getMinor() == that.getMinor());
		}
		return false;
	}

	public int hashCode() {
		final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addInt(this.getMajor());
		hashCodeGenerator.addLong(this.getMinor());
		hashCodeGenerator.addObject(this.getIdentifierString());
		return hashCodeGenerator.getResult();
	}

	/**
	 * @see Identifier#toHexString()
	 */
	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifier#getIdentifierString()
	 */
	public String getIdentifierString() {
//		if (this.identifierString == IDENTIFIER_STRING_UNUNITIALIZED)
//			this.identifierString = ObjectEntities.codeToString(this.major) + SEPARATOR + this.minor;
		return this.identifierString;
	}

	public short getMajor() {
		if (this.major == MAJOR_UNINITIALIZED)
			this.major = ObjectEntities.stringToCode(this.identifierString.substring(0, this.identifierString.indexOf(SEPARATOR)));
		return this.major;
	}

	public long getMinor() {
		if (this.minor == MINOR_UNINITIALIZED)
			this.minor = Long.parseLong(this.identifierString.substring(this.identifierString.indexOf(SEPARATOR) + 1));
		return this.minor;
	}

	public String toString() {
		return this.getIdentifierString();
	}

	/**
	 * @see Identifier#getTransferable()
	 */
	public Identifier_Transferable getTransferable() {
/*/		// #ifdef NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.getMajor(), this.getMinor());
/*/		// #else // NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.getIdentifierString());
//*/		// #endif // NUMERIC_IDENTIFIER
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
/*/		// #ifdef NUMERIC_IDENTIFIER
		this.identifierString = IDENTIFIER_STRING_UNINITIALIZED; 
/*/		// #else // NUMERIC_IDENTIFIER
		this.major = MAJOR_UNINITIALIZED;
		this.minor = MINOR_UNINITIALIZED;
//*/		// #endif // NUMERIC_IDENTIFIER
	}
}
