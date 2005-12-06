/*
 * $Id: SessionKey.java,v 1.13 2005/12/06 09:42:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.IdlTransferableObject;

/**
 * @version $Revision: 1.13 $, $Date: 2005/12/06 09:42:52 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class SessionKey implements IdlTransferableObject<IdlSessionKey> {
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
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlSessionKey getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlSessionKey getIdlTransferable() {
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

	public final String stringValue() {
		return this.sessionCode;
	}

	@Override
	public int hashCode() {
		return this.sessionCode.hashCode();
	}

	@Override
	public String toString() {
		return this.stringValue();
	}
}
