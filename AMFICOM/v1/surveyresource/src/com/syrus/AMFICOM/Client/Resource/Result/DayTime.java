/*
 * DayTime.java
 * Created on 05.05.2004 17:34:05
 * 
 */
package com.syrus.AMFICOM.Client.Resource.Result;

/**
 * @author Vladimir Dolzhenko
 */
public class DayTime {
	protected int hour;
	protected int minute;
	protected int second;

	DayTime(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	/**
	 * @return
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * @param hour
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @param minute
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * @param second
	 */
	public void setSecond(int second) {
		this.second = second;
	}

}

