/*
 * $Id: TestTemporalStamps.java,v 1.3 2005/04/27 15:26:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/27 15:26:37 $
 * @author $Author: bob $
 * @module scheduler_v1
 */
public class TestTemporalStamps {

	private Date			endTime;
	private Date			startTime;
	private AbstractTemporalPattern	temporalPattern;

	private int				discriminator;

	public TestTemporalStamps(TestTemporalType temporalType,
			Date startTime,
			Date endTime,
			AbstractTemporalPattern temporalPattern) {
		this.discriminator = temporalType.value();
		this.startTime = startTime;
		this.endTime = endTime;
		this.temporalPattern = temporalPattern;
	}

	public TestTemporalType getTestTemporalType() {
		return TestTemporalType.from_int(this.discriminator);
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public AbstractTemporalPattern getTemporalPattern() {
		return this.temporalPattern;
	}

	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		Log.debugMessage("TestTemporalStamps.setEndTime | " + endTime, Log.FINEST);
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		Log.debugMessage("TestTemporalStamps.setStartTime | " + startTime, Log.FINEST);
	}
	
}
