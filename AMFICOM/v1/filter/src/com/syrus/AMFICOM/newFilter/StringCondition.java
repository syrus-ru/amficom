/*
 * $Id: StringCondition.java,v 1.5 2005/08/25 10:30:33 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/25 10:30:33 $
 * @author $Author: max $
 * @module filter
 */
public class StringCondition implements TemporalCondition {
	
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
	
	public boolean isEmpty() {
		if(this.string.equals("")) {
			return true;
		}
		return false;
	}
}
