/*
 * $Id: StringCondition.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class StringCondition {
	
	private String string;
	
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
