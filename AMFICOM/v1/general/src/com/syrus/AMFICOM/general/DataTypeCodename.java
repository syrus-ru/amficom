/*
 * $Id: DataTypeCodename.java,v 1.1 2005/06/20 17:35:54 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

public enum DataTypeCodename {
	INTEGER("integer"),
	DOUBLE("double"),
	STRING("string"),
	DATE("date"),
	LONG("long"),
	RAW("raw"),
	BOOLEAN("boolean");

	private final String codename;

	private DataTypeCodename(String codename) {
		this.codename = codename;
	}

	@Override
	public String toString() {
		return this.codename;
	}
}
