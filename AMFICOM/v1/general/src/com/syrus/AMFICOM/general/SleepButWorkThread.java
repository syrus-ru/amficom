/*
 * $Id: SleepButWorkThread.java,v 1.11 2005/06/17 12:38:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/17 12:38:53 $
 * @author $Author: bass $
 * @module general_v1
 */

public abstract class SleepButWorkThread extends Thread {
	static final int TIME_MULTIPLIER = 2;
	public static final int MAX_FALLS = 3;
	public static final int FALL_CODE_NO_ERROR = 0;

	protected long initialTimeToSleep;
	private long timeToSleep;
	protected int maxFalls;
	private int fallsCounter;
	protected int fallCode;

	protected SleepButWorkThread(long initialTimeToSleep) {
		this (initialTimeToSleep, MAX_FALLS);
	}

	protected SleepButWorkThread(long initialTimeToSleep, int maxFalls) {
		this.initialTimeToSleep = initialTimeToSleep;
		this.timeToSleep = initialTimeToSleep;
		this.maxFalls = maxFalls;
		this.fallsCounter = 0;
		this.fallCode = FALL_CODE_NO_ERROR;
	}
	
	protected void sleepCauseOfFall() {
		this.fallsCounter++;
		if (this.fallsCounter <= this.maxFalls) {
			Log.debugMessage("SleepButWorkThread | WARNING: the fall No." + this.fallsCounter + " of " + this.maxFalls  + " maximum -- sleeping on " + (int)(this.timeToSleep/1000) + " seconds", Log.DEBUGLEVEL05);
			try {
				sleep(this.timeToSleep);
			} catch (InterruptedException ie) {
				Log.errorException(ie);
			}
			this.timeToSleep = this.timeToSleep * TIME_MULTIPLIER;
		} else {
			Log.errorMessage("SleepButWorkThread | Number of falls: " + this.fallsCounter + " exceeded maximum: " + this.maxFalls + ". Processing fall");
			this.processFall();
			this.clearFalls();
		}
	}
	
	protected void clearFalls() {
		this.fallsCounter = 0;
		this.timeToSleep = this.initialTimeToSleep;
		this.fallCode = FALL_CODE_NO_ERROR;
	}
	
	protected abstract void processFall();
}
