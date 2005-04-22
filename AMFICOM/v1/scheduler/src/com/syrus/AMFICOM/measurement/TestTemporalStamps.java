/*
 * $Id: TestTemporalStamps.java,v 1.2 2005/04/22 16:15:42 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 16:15:42 $
 * @author $Author: arseniy $
 * @module scheduler_v1
 */
public class TestTemporalStamps {

	private Date			endTime;
	private Date			startTime;
	private CronTemporalPattern	temporalPattern;

	private int				discriminator;

	public TestTemporalStamps(TestTemporalType temporalType,
			Date startTime,
			Date endTime,
			CronTemporalPattern temporalPattern) {
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

	public CronTemporalPattern getTemporalPattern() {
		return this.temporalPattern;
	}
}
