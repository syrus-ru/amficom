/*
 * $Id: Identifier.java,v 1.6 2004/08/06 13:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2004/08/06 13:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class Identifier implements Comparable, Cloneable, TransferableObject {
	public static final String SEPARATOR = "_";

	private String major;
	private long minor;
	private String identifierString;

	public Identifier(String identifierString) {
		this.major = identifierString.substring(0, identifierString.indexOf(SEPARATOR));
		this.minor = Long.parseLong(identifierString.substring(identifierString.indexOf(SEPARATOR) + 1));
		this.identifierString = identifierString;
	}

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_string);
	}

	protected Identifier(String major, long minor) {
		this.major = major;
		this.minor = minor;
		this.identifierString = this.major + SEPARATOR + Long.toString(this.minor);
	}

	public Object clone() {
		Identifier id = null;
		try {
			id = (Identifier)super.clone();
		}
		catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		id.major = new String(this.major);
		id.identifierString = new String(this.identifierString);
		return id;
	}

	public int compareTo(Object obj) {
		if(obj instanceof Identifier) {
			String major1 = ((Identifier)obj).getMajor();
			long minor1 = ((Identifier)obj).getMinor();
			if (this.major.equals(major1))
				if (this.minor < minor1)
					return -1;
				else
					if (this.minor > minor1)
						return 1;
					else
						return 0;
			else
				return this.major.compareTo(major1);
		}
		else {
			return -1;
		}
	}

	public boolean equals(Object obj) {
		if(obj instanceof Identifier)
			return ((Identifier)obj).getIdentifierString().equals(this.identifierString);
		else
			return false;
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major.hashCode();
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + this.identifierString.hashCode();
		return ret;
	}

	public Object getTransferable() {
		return new Identifier_Transferable(this.identifierString);
	}

	public String getIdentifierString() {
		return this.identifierString; 
	}

	public String getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}
	
	public String getObjectEntity() {
		return this.major;
	}

	public String toString() {
		return this.identifierString;
	}

	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toSQLString() {
		return "'" + this.identifierString + "'";
	}

	public String getCode() {
		return this.identifierString;
	}

	public static String getNullSQLString() {
		return "''";
	}
}
