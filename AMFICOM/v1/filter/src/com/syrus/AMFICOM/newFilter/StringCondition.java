/*
 * $Id: StringCondition.java,v 1.6 2005/09/20 10:13:41 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/20 10:13:41 $
 * @author $Author: max $
 * @module filter
 */
public class StringCondition implements TemporalCondition {
	
	private String string;
	private boolean substring;
	
	public StringCondition() {
		this.string = new String();
		this.substring = true;
	}
	
	public String getString() {
		return this.string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public boolean isEmpty() {
		if(this.string.equals("")) {
			return true;
		}
		return false;
	}

	public boolean isSubstring() {
		return this.substring;
	}

	public void setSubstring(boolean substring) {
		this.substring = substring;
	}
}
