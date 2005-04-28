/*
 * $Id: TestTemporalStamps.java,v 1.5 2005/04/28 16:53:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.general.Undoable;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/28 16:53:03 $
 * @author $Author: bob $
 * @module scheduler_v1
 */
public class TestTemporalStamps implements Undoable  {

	private Date			endTime;
	private Date			startTime;
	private AbstractTemporalPattern	temporalPattern;

	private int				discriminator;
	
	private Date undoEndTime;
	private Date undoStartTime;
	
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
		/* save state */
		this.undoEndTime = this.endTime;
		
		this.endTime = endTime;		
		Log.debugMessage("TestTemporalStamps.setEndTime | " + endTime, Log.FINEST);
	}
	
	public void setStartTime(Date startTime) {
		/* save state */
		this.undoStartTime = this.startTime;
		
		this.startTime = startTime;
		Log.debugMessage("TestTemporalStamps.setStartTime | " + startTime, Log.FINEST);
	}
	
	public void undo() {
	
		if (this.undoStartTime != null) {
			Date date = this.startTime;
			this.startTime = this.undoStartTime;
			this.undoStartTime = date;
		}

		if (this.undoEndTime != null) {
			Date date = this.endTime;
			this.endTime = this.undoEndTime;
			this.undoEndTime = date;
		}

		if (this.temporalPattern instanceof Undoable) {
			((Undoable) this.temporalPattern).undo();
		}
		
		Log.debugMessage("TestTemporalStamps.undo | startTime is " + this.startTime, Log.FINEST);
		Log.debugMessage("TestTemporalStamps.undo | endTime is " + this.endTime, Log.FINEST);

	}	
	
}
