/*
 * $Id: StringCondition.java,v 1.3 2005/06/15 08:09:45 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/15 08:09:45 $
 * @author $Author: max $
 * @module misc
 */
public class StringCondition {
	
	private String string;
	
	public StringCondition() {
		this.string = new String();
	}
	
	public StringCondition(String string) {
		this.string = string;
	}
	
	public String getString() {
		return this.string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
}
