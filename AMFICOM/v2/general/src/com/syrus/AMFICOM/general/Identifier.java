package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

public class Identifier implements Comparable, Cloneable, TransferableObject {
	private short major;
	private long minor;
	private long identifier_code;

	public Identifier(long identifier_code) {
		this.major = (short)((identifier_code & 0x1111000000000000L) >> 48);
		this.minor = identifier_code & 0x0000111111111111L;
		this.identifier_code = identifier_code;
	}

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_code);
	}

	protected Identifier(short major, long minor) {
		this.major = major;
		this.minor = minor;
		this.identifier_code = (((long)this.major) << 48) | this.minor;
	}

	public Object clone() {
		return new Identifier(this.identifier_code);
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
			return ((Identifier)obj).getCode() == this.identifier_code;
		else
			return false;
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + (int)this.major;
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + (int)(this.identifier_code ^ (this.identifier_code >>> 32));
		return ret;
	}

	public Object getTransferable() {
		return new Identifier_Transferable(this.identifier_code);
	}

	public long getCode() {
		return this.identifier_code; 
	}

	public short getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}

	public String toString() {
		return Long.toString(this.identifier_code);
	}

	public String toHexString() {
		return Long.toHexString(this.identifier_code);
	}
}

/*
public class Identifier implements Comparable, Cloneable {
	private String identifierString;

	public Identifier(String identifierString) {
		this.identifierString = new String(identifierString);
	}

	public Object clone() {
		return new Identifier(this.identifierString);
	}

	public String toString() {
		return new String(this.identifierString);
	}

	public int compareTo(Object obj) {
		if(obj instanceof Identifier) {
			String s = ((Identifier)obj).toString();
			return this.identifierString.compareTo(s);
		}
		else {
			return -1;
		}
	}

	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Identifier) {
			int i = obj.toString().compareTo(this.identifierString);
			if(i == 0)
				ret = true;
			else
				ret = false;
		}
		return ret;
	}

	public int hashCode() {
		return this.identifierString.hashCode();
	}
}*/
