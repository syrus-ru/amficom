/*
 * $Id: HashCodeGenerator.java,v 1.3 2004/08/17 10:02:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

/**
 * HashCodeGenerator methods have got from Effective Java: Programming Language
 * Guide by Joshua Bloch
 * 
 * @version $Revision: 1.3 $, $Date: 2004/08/17 10:02:15 $
 * @author $Author: bob $
 * @module util
 */
public class HashCodeGenerator {

	private int	result	= 17;

	public void clear() {
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

	public void addByteArray(byte[] array) {
		for (int i = 0; i < array.length; i++)
			addInt(array[i]);
	}

	public void addShortArray(short[] array) {
		for (int i = 0; i < array.length; i++)
			addInt(array[i]);
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

	public int getResult() {
		return this.result;
	}

	public static boolean equalsArray(final Object[] array1, final Object[] array2) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (!array1[i].equals(array2[i]))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean equalsArray(final byte[] array1, final byte[] array2) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i])
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean equalsArray(final short[] array1, final short[] array2) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i])
					return false;
			}
			return true;
		}
		return false;
	}
	
	public static boolean equalsArray(final int[] array1, final int[] array2) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i])
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean equalsArray(final long[] array1, final long[] array2) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i])
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean equalsArray(final float[] array1, final float[] array2, float tolerance) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (Math.abs(array1[i] - array2[i]) > tolerance)
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean equalsArray(final double[] array1, final double[] array2, double tolerance) {
		if (array1 == array2)
			return true;
		if (array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (Math.abs(array1[i] - array2[i]) > tolerance)
					return false;
			}
			return true;
		}
		return false;
	}

	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return "HashCodeGenerator result:" + this.result;
	}
}