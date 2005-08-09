/*
 * $Id: NumberCondition.java,v 1.3 2005/08/09 22:00:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/09 22:00:24 $
 * @author $Author: arseniy $
 * @module filter
 */
public class NumberCondition {
	
	public NumberCondition() {
		this.equals = new String();
		this.from = new String();
		this.to = new String();
		this.includeBounds = true;
	}
	
	public NumberCondition(String equals, String from, String to, boolean includeBounds) {
		this.equals = equals;
		this.from = from;
		this.to = to;
		this.includeBounds = includeBounds;
	}
	
	private String equals;
	private String from;
	private String to;
	private boolean includeBounds;
	
	
	public String getEquals() {
		return this.equals;
	}
	public String getFrom() {
		return this.from;
	}
	public String getTo() {
		return this.to;
	}
	public boolean isIncludeBounds() {
		return this.includeBounds;
	}
	public void setEquals(String equals) {
		this.equals = equals;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setIncludeBounds(boolean includeBounds) {
		this.includeBounds = includeBounds;
	}
	public void setTo(String to) {
		this.to = to;
	}
}
