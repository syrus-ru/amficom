/*
 * $Id: TestTemporalStamps.java,v 1.1 2004/12/20 15:45:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/20 15:45:14 $
 * @author $Author: arseniy $
 * @module scheduler_v1
 */
public class TestTemporalStamps {

	private Date			endTime;
	private Date			startTime;
	private TemporalPattern	temporalPattern;

	private int				discriminator;

	public TestTemporalStamps(TestTemporalType temporalType,
			Date startTime,
			Date endTime,
			TemporalPattern temporalPattern) {
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

	public TemporalPattern getTemporalPattern() {
		return this.temporalPattern;
	}
}
