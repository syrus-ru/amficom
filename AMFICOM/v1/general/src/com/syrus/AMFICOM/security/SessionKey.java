/*
 * $Id: SessionKey.java,v 1.20 2006/04/26 12:30:11 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import java.io.Serializable;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.20 $, $Date: 2006/04/26 12:30:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class SessionKey implements IdlTransferableObject<IdlSessionKey>, Serializable, SessionData {
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

	public final String stringValue() {
		return this.sessionCode;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}

	public SessionKey getSessionKey() {
		return this;
	}

	/*-********************************************************************
	 * #eqauls() and #hashCode()                                          *
	 **********************************************************************/

	/**
	 * @param obj
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof SessionData) {
			final SessionKey that = ((SessionData) obj).getSessionKey();
			return this == that
					|| this.sessionCode.equals(that.sessionCode);
		}
		return false;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.sessionCode.hashCode();
	}
}
