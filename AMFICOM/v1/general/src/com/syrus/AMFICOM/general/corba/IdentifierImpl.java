/*
 * $Id: IdentifierImpl.java,v 1.3 2004/11/29 10:24:29 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/11/29 10:24:29 $
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
	 * @see Comparable#compareTo(Object)
	 */
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

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 *
	 * @see #identifierString()
	 * @see IIdentifier#getIdentifierString()
	 */
	public String getIdentifierString() {
		return identifierString();
	}

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 * 
	 * @see #major()
	 * @see IIdentifier#getMajor() 
	 */
	public short getMajor() {
		return major();
	}

	/**
	 * Getter method whose only purpose is to pacify Vladimir Alexandrovich.
	 * 
	 * @see #minor()
	 * @see IIdentifier#getMinor()
	 */
	public long getMinor() {
		return minor();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Identifier#getTransferable()
	 */
	public Identifier_Transferable getTransferable() {
/*/		// #ifdef NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.major(), this.minor());
/*/		// #else // NUMERIC_IDENTIFIER
		return new Identifier_Transferable(this.identifierString());
//*/		// #endif // NUMERIC_IDENTIFIER
	}

	public int hashCode() {
		final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addInt(this.major());
		hashCodeGenerator.addLong(this.minor());
		hashCodeGenerator.addObject(this.identifierString());
		return hashCodeGenerator.getResult();
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

	/**
	 * @see IIdentifier#toHexString()
	 */
	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return this.identifierString();
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
