package com.syrus.util.mail;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/01 17:45:22 $
 * @author $Author: cvsadmin $
 * @module util
 */
public class MailIdentity {
	private String name;
	private String address;
	
	public MailIdentity(String address) {
		this(null, address);
	}

	/**
	 * @todo Check the e-mail address for validity. Addresses may be local
	 *       (jsmith) or remote (jsmith@mydomain.com).
	 * @todo Implement filtering of quotation marks in name.
	 */
	public MailIdentity(String name, String address) {
		if ((address == null) || (address.length() == 0))
			throw new IllegalArgumentException("E-mail address is invalid.");
		this.name = name;
		this.address = address;
	}

	String getAddress() {
		return address;
	}

	public String toString() {
		if ((name == null) || (name.length() == 0))
			return address;
		else
			return name + " <" + address + '>';
	}
}
