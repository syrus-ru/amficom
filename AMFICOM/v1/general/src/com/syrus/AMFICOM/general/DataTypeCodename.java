/*
 * $Id: DataTypeCodename.java,v 1.2 2005/07/13 16:05:00 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @deprecated
 * @version $Revision: 1.2 $, $Date: 2005/07/13 16:05:00 $
 * @author $Author: arseniy $
 * @module general
 */
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

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " " + this.stringValue();
	}
}
