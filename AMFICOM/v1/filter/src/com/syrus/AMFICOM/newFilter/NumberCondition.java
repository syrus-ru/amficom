/*
 * $Id: NumberCondition.java,v 1.1 2005/03/15 16:11:44 max Exp $
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
public class NumberCondition {
	
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
