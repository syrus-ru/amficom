/*
 * $Id: DateCondition.java,v 1.1 2005/03/31 08:11:40 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Date;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/31 08:11:40 $
 * @author $Author: max $
 * @module filter_v1
 */
public class DateCondition {
	private Date startDate;
	private Date endDate;
	
	public DateCondition() {
		this.startDate = new Date();
		this.endDate = new Date();		
	}
	public Date getEndDate() {
		return this.endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return this.startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
