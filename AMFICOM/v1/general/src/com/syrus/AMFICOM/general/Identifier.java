/*
 * $Id: Identifier.java,v 1.16 2004/11/29 10:24:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.16 $, $Date: 2004/11/29 10:24:29 $
 * @author $Author: bass $
 * @module general_v1
 */
public class Identifier implements
//		com.syrus.AMFICOM.general.corba.IIdentifier,
		Comparable,
		Cloneable,
		TransferableObject,
		Serializable {
	/**
	 * @see com.syrus.AMFICOM.general.corba.IIdentifier#SEPARATOR
	 */
	public static final char SEPARATOR = '_';

	private static final long serialVersionUID = 1721559813677093072L;

	private static final String[] TRUNCATABLE_IDS = {
		"IDL:com/syrus/AMFICOM/general/Identifier:1.0"
	};

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
	 * @see org.omg.CORBA.portable.ValueBase#_truncatable_ids()
	 */
	public String[] _truncatable_ids() {
		return TRUNCATABLE_IDS;
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
		return -1;
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
	 * @see com.syrus.AMFICOM.general.corba.IIdentifier#getIdentifierString()
	 */
	public String getIdentifierString() {
		return this.identifierString; 
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IIdentifier#getMajor()
	 */
	public short getMajor() {
		return this.major;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IIdentifier#getMinor()
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
	 * @see com.syrus.AMFICOM.general.corba.IIdentifier#toHexString()
	 */
	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return this.identifierString;
	}
}
