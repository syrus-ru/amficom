/*
 * $Id: HashCodeGenerator.java,v 1.2 2004/08/17 05:24:16 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

/**
 * HashCodeGenerator methods have got from 
 * Effective Java: Programming Language Guide by Joshua Bloch
 * 
 * @version $Revision: 1.2 $, $Date: 2004/08/17 05:24:16 $
 * @author $Author: bob $
 * @module util
 */
public class HashCodeGenerator {

	private int	result	= 17;
	
	public void clear(){
		this.result = 17;
	}

	public void addInt(int value) {
		this.result = 37 * this.result + value;
	}

	public void addBoolean(boolean value) {
		addInt(value ? 1 : 0);
	}

	public void addLong(long value) {
		addInt((int) (value ^ (value >>> 32)));
	}

	public void addFloat(float value) {
		addInt(Float.floatToIntBits(value));
	}

	public void addDouble(double value) {
		addLong(Double.doubleToLongBits(value));
	}

	/**
	 * If the field is an object reference and this class's equals method
	 * compares the field by recursively invoking equals, recursively invoke
	 * hashCode on the field. If a more complex comparison is required, compute
	 * a "canonical representation" for this field and invoke
	 */
	public void addObject(Object value) {
		addInt(value == null ? 0 : value.hashCode());
	}

	public void addIntArray(int[] array) {
		for (int i = 0; i < array.length; i++)
			addInt(array[i]);
	}

	public void addBooleanArray(boolean[] array) {
		for (int i = 0; i < array.length; i++)
			addBoolean(array[i]);
	}

	public void addLongArray(long[] array) {
		for (int i = 0; i < array.length; i++)
			addLong(array[i]);
	}

	public void addFloatArray(float[] array) {
		for (int i = 0; i < array.length; i++)
			addFloat(array[i]);
	}

	public void addDoubleArray(double[] array) {
		for (int i = 0; i < array.length; i++)
			addDouble(array[i]);
	}

	public void addObjectArray(Object[] array) {
		for (int i = 0; i < array.length; i++)
			addObject(array[i]);
	}

	
	public int getResult(){
		return this.result;
	}
}