/*
 * $Id: StringCondition.java,v 1.4 2005/08/09 22:00:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/09 22:00:24 $
 * @author $Author: arseniy $
 * @module filter
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
