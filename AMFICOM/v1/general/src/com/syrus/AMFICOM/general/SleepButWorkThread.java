/*
 * $Id: SleepButWorkThread.java,v 1.3 2004/08/06 13:43:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/06 13:43:44 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public abstract class SleepButWorkThread extends Thread {
	static final int TIME_MULTIPLIER = 2;
	public static final int MAX_FALLS = 3;

	protected long initialTimeToSleep;
	private long timeToSleep;
	private int maxFalls;
	private int fallsCounter;

	protected SleepButWorkThread(long initialTimeToSleep) {
		this (initialTimeToSleep, MAX_FALLS);
	}

	protected SleepButWorkThread(long initialTimeToSleep, int maxFalls) {
		this.initialTimeToSleep = initialTimeToSleep;
		this.timeToSleep = initialTimeToSleep;
		this.maxFalls = maxFalls;
		this.fallsCounter = 0;
	}
	
	protected void sleepCauseOfFall() {
		if (this.fallsCounter < this.maxFalls) {
			Log.debugMessage("WARNING: the fall N" + this.fallsCounter + " of " + this.maxFalls  + " maximum", Log.DEBUGLEVEL05);
			try {
				sleep(this.timeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
			this.fallsCounter ++;
			this.timeToSleep = this.timeToSleep * TIME_MULTIPLIER;
		}
		else {
			Log.errorMessage("Number of falls: " + this.fallsCounter + " reached maximum: " + this.maxFalls + ". Shutting down");
			this.shutdown();
		}
	}
	
	protected void clearFalls() {
		this.fallsCounter = 0;
		this.timeToSleep = this.initialTimeToSleep;
	}
	
	protected abstract void shutdown();
}
