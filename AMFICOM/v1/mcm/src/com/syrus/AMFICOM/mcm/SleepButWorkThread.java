package com.syrus.AMFICOM.mcm;

import com.syrus.util.Log;

public abstract class SleepButWorkThread extends Thread {
	private static final int MAX_FALLS = 3;
	private static final int TIME_MULTIPLIER = 2;

	private int maxFalls;
	private int fallsCounter;
	long initialTimeToSleep;
	private long timeToSleep;

	SleepButWorkThread(long initialTimeToSleep) {
		this (MAX_FALLS, initialTimeToSleep);
	}

	SleepButWorkThread(int maxFalls, long initialTimeToSleep) {
		this.maxFalls = maxFalls;
		this.fallsCounter = 0;
		this.initialTimeToSleep = initialTimeToSleep;
		this.timeToSleep = initialTimeToSleep;
	}
	
	void sleepCauseOfFall() {
		if (this.fallsCounter < this.maxFalls) {
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
	
	void clearFalls() {
		this.fallsCounter = 0;
		this.timeToSleep = this.initialTimeToSleep;
	}
	
	abstract void shutdown();
}
