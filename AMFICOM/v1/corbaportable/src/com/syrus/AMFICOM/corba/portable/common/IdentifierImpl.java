/*
 * $Id: IdentifierImpl.java,v 1.1 2004/06/22 12:27:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.common;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public class IdentifierImpl extends Identifier implements Comparable {
	{
		id = "";
	}

	IdentifierImpl() {
	}

	public IdentifierImpl(String id) {
		setId(id);
	}

	public void setId(String id) {
		if (id == null)
			throw new IllegalArgumentException();
		this.id = id;
	}

	public int compareTo(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Identifier)
			return ((Identifier) obj).id.equals(id);
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return id;
	}
}
