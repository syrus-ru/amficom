/*
 * $Id: IdentifierImpl.java,v 1.5 2004/12/21 13:56:56 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.logging.ErrorHandler;
import java.io.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/12/21 13:56:56 $
 * @module general_v1
 */
final class IdentifierImpl extends Identifier implements Cloneable, Comparable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 8435961337633429780L;

/*/	// #ifdef NUMERIC_IDENTIFIER
	private transient String thisIdentifierString = IDENTIFIER_STRING_UNINITIALIZED; 
/*/	// #else // NUMERIC_IDENTIFIER
	private transient short thisMajor = MAJOR_UNINITIALIZED;
	private transient long thisMinor = MINOR_UNINITIALIZED;
//*/	// #endif // NUMERIC_IDENTIFIER

	/**
	 * This constructor is only used by value factory and shouldn't be
	 * invoked directly.
	 */
	IdentifierImpl() {
	}

	IdentifierImpl(final Identifier_Transferable id) {
		this(id.identifier_string);
	}

	IdentifierImpl(final short major, final long minor) {
		this.thisMajor = major;
		this.thisMinor = minor;
		this.thisIdentifierString = ObjectEntities.codeToString(major) + SEPARATOR + minor;
	}

	IdentifierImpl(final String identifierString) {
		final int indexOfSeparator = identifierString.indexOf(SEPARATOR);
		this.thisMajor = ObjectEntities.stringToCode(identifierString.substring(0, indexOfSeparator));
		this.thisMinor = Long.parseLong(identifierString.substring(indexOfSeparator + 1));
		this.thisIdentifierString = identifierString;
	}

	/**
	 * @see Identifier#cloneInstance()
	 */
	public Identifier cloneInstance() {
		try {
			return (Identifier) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	/**
	 * @param anotherIdentifierImpl
	 */
	public int compareTo(final IdentifierImpl anotherIdentifierImpl) {
		final short thisMajor = this.major();
		final long thisMinor = this.minor();
		final short thatMajor = anotherIdentifierImpl.major();
		final long thatMinor = anotherIdentifierImpl.minor();
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
			return (this.major() == that.major())
				&& (this.minor() == that.minor());
		}
		return false;
	}

	public int hashCode() {
		final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addInt(this.major());
		hashCodeGenerator.addLong(this.minor());
		hashCodeGenerator.addObject(this.identifierString());
		return hashCodeGenerator.getResult();
	}

	/**
	 * @see Identifier#hexString()
	 */
	public String hexString() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifier#identifierString()
	 */
	public String identifierString() {
//		if (this.thisIdentifierString == IDENTIFIER_STRING_UNUNITIALIZED)
//			this.thisIdentifierString = ObjectEntities.codeToString(this.thisMajor) + SEPARATOR + this.thisMinor;
		return this.thisIdentifierString;
	}

	public short major() {
		if (this.thisMajor == MAJOR_UNINITIALIZED)
			this.thisMajor = ObjectEntities.stringToCode(this.thisIdentifierString.substring(0, this.thisIdentifierString.indexOf(SEPARATOR)));
		return this.thisMajor;
	}

	public long minor() {
		if (this.thisMinor == MINOR_UNINITIALIZED)
			this.thisMinor = Long.parseLong(this.thisIdentifierString.substring(this.thisIdentifierString.indexOf(SEPARATOR) + 1));
		return this.thisMinor;
	}

	public String toString() {
		return this.identifierString();
	}

	/**
	 * @see Identifier#transferable()
	 */
	public Identifier_Transferable transferable() {
/*/		// #ifdef NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.major(), this.minor());
/*/		// #else // NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.identifierString());
//*/		// #endif // NUMERIC_IDENTIFIER
	}

	protected Object clone() throws CloneNotSupportedException {
		IdentifierImpl id = (IdentifierImpl) super.clone();
		id.thisMajor = this.major();
		id.thisMinor = this.minor();
		id.thisIdentifierString = this.identifierString();
		return id;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*/		// #ifdef NUMERIC_IDENTIFIER
		this.thisIdentifierString = IDENTIFIER_STRING_UNINITIALIZED; 
/*/		// #else // NUMERIC_IDENTIFIER
		this.thisMajor = MAJOR_UNINITIALIZED;
		this.thisMinor = MINOR_UNINITIALIZED;
//*/		// #endif // NUMERIC_IDENTIFIER
	}
}
