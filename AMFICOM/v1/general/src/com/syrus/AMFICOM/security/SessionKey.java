/*
 * $Id: SessionKey.java,v 1.6 2005/06/25 17:07:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/25 17:07:47 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class SessionKey implements TransferableObject {
	private String sessionCode;

	/**
	 * Only for generator and database driver
	 * @param sessionCode
	 */
	protected SessionKey(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public SessionKey(IdlSessionKey skT) {
		this.sessionCode = skT.sessionCode;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlSessionKey getTransferable(final ORB orb) {
		return this.getTransferable();
	}

	public IdlSessionKey getTransferable() {
		return new IdlSessionKey(this.sessionCode);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (object instanceof SessionKey)
			return this.sessionCode.equals(((SessionKey) object).sessionCode);
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
