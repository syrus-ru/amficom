package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

public class Identifier implements Comparable, Cloneable, TransferableObject {
	public static final String SEPARATOR = "-";

	private String major;
	private long minor;
	private String identifier_string;

	public Identifier(String identifier_string) {
		this.major = identifier_string.substring(0, identifier_string.indexOf(SEPARATOR));
		this.minor = Long.parseLong(identifier_string.substring(identifier_string.indexOf(SEPARATOR) + 1));
		this.identifier_string = identifier_string;
	}

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_string);
	}

	protected Identifier(String major, long minor) {
		this.major = major;
		this.minor = minor;
		this.identifier_string = this.major + SEPARATOR + Long.toString(this.minor);
	}

	public Object clone() {
		return new Identifier(this.identifier_string);
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
			return ((Identifier)obj).getIdentifierString().equals(this.identifier_string);
		else
			return false;
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major.hashCode();
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + this.identifier_string.hashCode();
		return ret;
	}

	public Object getTransferable() {
		return new Identifier_Transferable(this.identifier_string);
	}

	public String getIdentifierString() {
		return this.identifier_string; 
	}

	public String getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}

	public String toString() {
		return this.identifier_string;
	}

	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toSQLString() {
		return "'" + this.identifier_string + "'";
	}

	public String getCode() {
		return this.identifier_string;
	}

	public static String getNullSQLString() {
		return "''";
	}
}
