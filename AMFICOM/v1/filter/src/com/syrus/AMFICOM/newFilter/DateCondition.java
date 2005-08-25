/*
 * $Id: DateCondition.java,v 1.4 2005/08/25 10:28:12 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Date;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/25 10:28:12 $
 * @author $Author: max $
 * @module filter
 */
public class DateCondition implements TemporalCondition {
	
	private Date startDate;
	private Date endDate;
	private boolean	empty;
	
	public DateCondition() {
		this.startDate = new Date();
		this.endDate = new Date();
		this.empty = true;
	}
	public Date getEndDate() {
		return this.endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.empty = false;
	}
	public Date getStartDate() {
		return this.startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.empty = false;
	}
	
	public boolean isEmpty() {
		return empty;
	}
}
