/*
 * $Id: StringCondition.java,v 1.2 2005/03/25 10:29:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/25 10:29:31 $
 * @author $Author: max $
 * @module misc
 */
public class StringCondition {
	
	private String string;
	
	StringCondition() {
		this.string = new String();
	}
	
	StringCondition (String string) {
		this.string = string;
	}
	
	public String getString() {
		return this.string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
}
