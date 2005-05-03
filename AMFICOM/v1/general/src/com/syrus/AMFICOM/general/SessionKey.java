/*
 * $Id: SessionKey.java,v 1.1 2005/05/03 14:19:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/03 14:19:15 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class SessionKey implements TransferableObject {
	private String sessionCode;

	/**
	 * Only for generator
	 * @param sessionCode
	 */
	protected SessionKey(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public SessionKey(SessionKey_Transferable skT) {
		this.sessionCode = skT.session_code;
	}

	public IDLEntity getTransferable() {
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
