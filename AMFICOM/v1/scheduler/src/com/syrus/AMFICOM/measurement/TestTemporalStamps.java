/*
 * $Id: TestTemporalStamps.java,v 1.4 2005/04/28 16:04:28 bob Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/04/28 16:04:28 $
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
	
	private Date redoEndTime;
	private Date redoStartTime;
	
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
			this.redoStartTime = this.startTime;
			this.startTime = this.undoStartTime;
		}

		if (this.undoEndTime != null) {
			this.redoEndTime = this.endTime;
			this.endTime = this.undoEndTime;
		}

		if (this.temporalPattern instanceof Undoable) {
			((Undoable) this.temporalPattern).undo();
		}
		
		Log.debugMessage("TestTemporalStamps.undo | startTime is " + this.startTime, Log.FINEST);
		Log.debugMessage("TestTemporalStamps.undo | endTime is " + this.endTime, Log.FINEST);

	}
	
	public void redo() {
		if (this.redoStartTime != null) {
			this.undoStartTime = this.startTime;
			this.startTime = this.redoStartTime;
		}

		if (this.redoEndTime != null) {
			this.undoEndTime = this.endTime;
			this.endTime = this.redoEndTime;
		}

		if (this.temporalPattern instanceof Undoable) {
			((Undoable) this.temporalPattern).redo();
		}
		
		Log.debugMessage("TestTemporalStamps.redo | startTime is " + this.startTime, Log.FINEST);
		Log.debugMessage("TestTemporalStamps.redo | endTime is " + this.endTime, Log.FINEST);
	}
	
}
