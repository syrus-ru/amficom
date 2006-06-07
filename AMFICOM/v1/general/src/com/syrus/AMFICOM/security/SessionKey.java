/*-
 * $Id: SessionKey.java,v 1.21.2.1 2006/06/07 08:32:59 arseniy Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.security;

import java.io.Serializable;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.LRUMap;
import com.syrus.util.LRUMap.Retainable;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.21.2.1 $, $Date: 2006/06/07 08:32:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class SessionKey implements IdlTransferableObject<IdlSessionKey>, Serializable, SessionData, Retainable {
	private static final long serialVersionUID = 8663656100029518421L;

	public static final SessionKey VOID_SESSION_KEY = new SessionKey("");

	private final String sessionCode;

	private IdlSessionKey idlSessionKey;


	/**
	 * Закэшированные ключи сессий.
	 * 
	 * @see #valueOf(IdlSessionKey)
	 */
	private static final LRUMap<String, SessionKey> sessionKeyCache = new LRUMap<String, SessionKey>(20);


	/**
	 * Только для создания новых ключей и восстановления существующих из БД.
	 * 
	 * @param sessionCode
	 */
	SessionKey(final String sessionCode) {
		this.sessionCode = sessionCode;
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

	public String stringValue() {
		return this.sessionCode;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}

	public SessionKey getSessionKey() {
		return this;
	}

	/**
	 * Использует объекты, закэшированные в {@link #sessionKeyCache}.
	 * 
	 * @param idlSessionKey
	 * @return Объект {@link SessionKey}, соответствующий данному
	 *         {@code idlSessionKey}.
	 */
	public static SessionKey valueOf(final IdlSessionKey idlSessionKey) {
		final String sessionCode = idlSessionKey.sessionCode;

		SessionKey sessionKey = sessionKeyCache.get(sessionCode);
		if (sessionKey == null) {
			sessionKey = new SessionKey(sessionCode);
			sessionKeyCache.put(sessionCode, sessionKey);
		}
		
		return sessionKey;
	}

	public boolean retain() {
		return false;
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
