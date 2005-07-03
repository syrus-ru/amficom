/*
 * $Id: MailIdentity.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.mail;

import java.io.UnsupportedEncodingException;
import javax.mail.internet.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
public class MailIdentity {
	static final String CHARSET = System.getProperty("file.encoding", "KOI8-R");

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

	public InternetAddress toInternetAddress() throws AddressException {
		try {
			return new InternetAddress(address, name, CHARSET);
		} catch (UnsupportedEncodingException uee) {
			return new InternetAddress(address, true);
		}
	}

	public String toString() {
		if ((name == null) || (name.length() == 0))
			return address;
		else
			return name + " <" + address + '>';
	}
}
