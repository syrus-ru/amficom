/*
 * $Id: IdentifierImpl.java,v 1.1 2004/11/22 12:51:55 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.logging.ErrorHandler;
import com.syrus.util.HashCodeGenerator;
import java.io.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/22 12:51:55 $
 * @module general_v1
 */
final class IdentifierImpl extends Identifier implements Cloneable, Comparable {
	private static final long serialVersionUID = 8435961337633429780L;

	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

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

	IdentifierImpl(final String identifierString) {
		final int indexOfSeparator = identifierString.indexOf(SEPARATOR);
		this.thisMajor = ObjectEntities.stringToCode(identifierString.substring(0, indexOfSeparator));
		this.thisMinor = Long.parseLong(identifierString.substring(indexOfSeparator + 1));
		this.thisIdentifierString = identifierString;
	}

	IdentifierImpl(final short major, final long minor) {
		this.thisMajor = major;
		this.thisMinor = minor;
		this.thisIdentifierString = ObjectEntities.codeToString(major) + SEPARATOR + minor;
	}

	public short major() {
		if (this.thisMajor == MAJOR_UNINITIALIZED)
			this.thisMajor = ObjectEntities.stringToCode(this.thisIdentifierString.substring(0, this.thisIdentifierString.indexOf(SEPARATOR)));
		return this.thisMajor;
	}

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 * 
	 * @see #major() 
	 */
	public short getMajor() {
		return major();
	}

	public long minor() {
		if (this.thisMinor == MINOR_UNINITIALIZED)
			this.thisMinor = Long.parseLong(this.thisIdentifierString.substring(this.thisIdentifierString.indexOf(SEPARATOR) + 1));
		return this.thisMinor;
	}

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 * 
	 * @see #minor()
	 */
	public long getMinor() {
		return minor();
	}

	public String identifierString() {
//		if (this.thisIdentifierString == IDENTIFIER_STRING_UNUNITIALIZED)
//			this.thisIdentifierString = ObjectEntities.codeToString(this.thisMajor) + SEPARATOR + this.thisMinor;
		return this.thisIdentifierString;
	}

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 * 
	 * @see #identifierString()
	 */
	public String getIdentifierString() {
		return identifierString();
	}

	public Identifier cloneInstance() {
		try {
			return (Identifier) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	protected Object clone() throws CloneNotSupportedException {
		IdentifierImpl id = (IdentifierImpl) super.clone();
		id.thisMajor = this.major();
		id.thisMinor = this.minor();
		id.thisIdentifierString = this.identifierString();
		return id;
	}

	public int compareTo(final Object obj) {
		if (obj instanceof IdentifierImpl) {
			final IdentifierImpl that = (IdentifierImpl) obj;
			final short major = this.major();
			final long minor = this.minor();
			final short thatMajor = that.major();
			final long thatMinor = that.minor();
			if ((major == thatMajor) && (minor == thatMinor))
				return 0;
			else if (major < thatMajor)
				return -1;
			else if (major > thatMajor)
				return 1;
			else if (minor < thatMinor)
				return -1;
			else //if (minor > thatMinor)
				return 1;
		}
		return -1;
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

	public String toString() {
		return this.identifierString();
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
