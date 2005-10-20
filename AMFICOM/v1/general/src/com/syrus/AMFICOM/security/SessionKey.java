/*
 * $Id: SessionKey.java,v 1.11 2005/10/20 13:17:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.11 $, $Date: 2005/10/20 13:17:14 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class SessionKey implements TransferableObject<IdlSessionKey> {
	private static final long serialVersionUID = -2395582378323499911L;

	private String sessionCode;

	/**
	 * Only for generator and database driver
	 * @param sessionCode
	 */
	SessionKey(final String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public SessionKey(final IdlSessionKey skT) {
		this.sessionCode = skT.sessionCode;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlSessionKey getTransferable(final ORB orb) {
		return this.getTransferable();
	}

	public IdlSessionKey getTransferable() {
		return new IdlSessionKey(this.sessionCode);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}

		if (object instanceof SessionKey) {
			return this.sessionCode.equals(((SessionKey) object).sessionCode);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.sessionCode.hashCode();
	}

	@Override
	public String toString() {
		return this.sessionCode;
	}
}
