/*
 * Time.java
 * Created on 05.05.2004 17:29:52
 * 
 */
package com.syrus.AMFICOM.Client.Resource.Result;

/**
 * @author Vladimir Dolzhenko
 */
public class Time {
	private int scale;
	private int value;

	Time(int scale, int value) {
		this.scale = scale;
		this.value = value;
	}
	
	/**
	 * @return
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param scale
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
