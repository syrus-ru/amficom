/*
 * $Id: Identifier.java,v 1.13 2004/11/17 08:22:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2004/11/17 08:22:45 $
 * @author $Author: bob $
 * @module general_v1
 */

public class Identifier implements Comparable, Cloneable, TransferableObject, Serializable {
	static final long serialVersionUID = 1721559813677093072L;

	public static final String SEPARATOR = "_";

	private short major;
	private long minor;
	private String identifierString;
	private String majorString;

	public Identifier(String identifierString) {
		this.majorString = identifierString.substring(0, identifierString.indexOf(SEPARATOR));
		this.major = ObjectEntities.stringToCode(this.majorString);
		this.minor = Long.parseLong(identifierString.substring(identifierString.indexOf(SEPARATOR) + 1));
		this.identifierString = identifierString;
	}

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_string);
	}

//	/*	Only for IdentifierGenerator	*/
//	protected Identifier(String majorString, long minor) {
//		this.major = ObjectEntities.codeForString(majorString);
//		this.majorString = majorString;
//		this.minor = minor;
//		this.identifierString = this.majorString + SEPARATOR + Long.toString(this.minor);
//	}

	/*	Only for IdentifierGenerator	*/
	protected Identifier(short major, long minor) {
		this.major = major;
		this.minor = minor;
		this.majorString = ObjectEntities.codeToString(this.major);
		this.identifierString = this.majorString + SEPARATOR + Long.toString(this.minor);
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
		id.majorString = new String(this.majorString);
		id.identifierString = new String(this.identifierString);
		return id;
	}

	public int compareTo(Object obj) {
		if(obj instanceof Identifier) {
			short major1 = ((Identifier)obj).getMajor();
			long minor1 = ((Identifier)obj).getMinor();
			if (this.major == major1 && this.minor == minor1)
				return 0;
			else
				if (this.major < major1)
					return -1;
				else
					if (this.major > major1)
						return 1;
					else
						if (this.minor < minor1)
							return -1;
						else
							return 1;
		}
		else {
			return -1;
		}
	}

	public boolean equals(Object obj) {
		if(obj instanceof Identifier)
			return ((Identifier)obj).getIdentifierString().equals(this.identifierString);
		return false;
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major;
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + this.identifierString.hashCode();
		ret = 37 * ret + this.majorString.hashCode();
		return ret;
	}

	public Object getTransferable() {
		return new Identifier_Transferable(this.identifierString);
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
	
	/**
	 * @deprecated use {@link #getMajor()} and {@link ObjectEntities#codeToString(short)}
	 * @return
	 */
	public String getObjectEntity() {
		return this.majorString;
	}

	public String toString() {
		return this.identifierString;
	}

	public String toHexString() {
		throw new UnsupportedOperationException();
	}
	
}
