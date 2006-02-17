/*
 * $Id: SessionKey.java,v 1.15.2.1 2006/02/17 11:25:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.15.2.1 $, $Date: 2006/02/17 11:25:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class SessionKey implements IdlTransferableObject<IdlSessionKey> {
	private static final long serialVersionUID = -2395582378323499911L;

	public static final SessionKey VOID_SESSION_KEY = new SessionKey("");

	private final String sessionCode;

	private IdlSessionKey idlSessionKey;

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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlSessionKey getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlSessionKey getIdlTransferable() {
		if (this.idlSessionKey == null) {
			this.idlSessionKey = new IdlSessionKey(this.sessionCode);	//NOTE: this.sessionCode is final
		}
		return this.idlSessionKey;
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
