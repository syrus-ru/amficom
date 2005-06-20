/*
 * $Id: SessionKey.java,v 1.4 2005/06/20 17:29:37 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/20 17:29:37 $
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

	public SessionKey(SessionKey_Transferable skT) {
		this.sessionCode = skT.session_code;
	}

	public SessionKey_Transferable getTransferable() {
		return new SessionKey_Transferable(this.sessionCode);
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (object instanceof SessionKey)
			return this.sessionCode.equals(((SessionKey) object).sessionCode);
		return false;
	}

	public int hashCode() {
		return this.sessionCode.hashCode();
	}

	public String toString() {
		return this.sessionCode;
	}
}
