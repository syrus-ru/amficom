/*
 * $Id: TestTemporalStamps.java,v 1.11.4.1 2006/04/10 11:46:00 saa Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.logging.Level;

import com.syrus.AMFICOM.general.Undoable;
import com.syrus.AMFICOM.measurement.Test.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11.4.1 $, $Date: 2006/04/10 11:46:00 $
 * @author $Author: saa $
 * @module scheduler
 */
public class TestTemporalStamps implements Undoable  {

	private Date			endTime;
	private Date			startTime;
	private AbstractTemporalPattern	temporalPattern;

	private TestTemporalType	temporalType;
	
	private Date undoEndTime;
	private Date undoStartTime;
	
	public TestTemporalStamps(TestTemporalType temporalType,
			Date startTime,
			Date endTime,
			AbstractTemporalPattern temporalPattern) {
		this.temporalType = temporalType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.temporalPattern = temporalPattern;
	}

	public TestTemporalType getTestTemporalType() {
		return this.temporalType;
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
		Log.debugMessage(endTime, Level.FINEST);
	}
	
	public void setStartTime(Date startTime) {
		/* save state */
		this.undoStartTime = this.startTime;
		
		this.startTime = startTime;
		Log.debugMessage(startTime, Level.FINEST);
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
		
		Log.debugMessage("startTime is " + this.startTime, Level.FINEST);
		Log.debugMessage("endTime is " + this.endTime, Level.FINEST);

	}	
	
}
