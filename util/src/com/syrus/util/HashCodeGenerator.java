/*
 * $Id: HashCodeGenerator.java,v 1.9 2005/06/20 14:24:40 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.Date;

/**
 * HashCodeGenerator methods have got from Effective Java: Programming Language
 * Guide by Joshua Bloch
 *
 * @version $Revision: 1.9 $, $Date: 2005/06/20 14:24:40 $
 * @author $Author: bass $
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
	 * hashCode on the field. If a more complex comparison is required,
	 * compute a "canonical representation" for this field and invoke
	 */
	public void addObject(Object value) {
		addInt(value == null ? 0 : value.hashCode());
	}

	public void addByteArray(byte[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addInt(array[i]);
	}

	public void addShortArray(short[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addInt(array[i]);
	}

	public void addIntArray(int[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addInt(array[i]);
	}

	public void addBooleanArray(boolean[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addBoolean(array[i]);
	}

	public void addLongArray(long[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addLong(array[i]);
	}

	public void addFloatArray(float[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addFloat(array[i]);
	}

	public void addDoubleArray(double[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addDouble(array[i]);
	}

	public void addObjectArray(Object[] array) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				addObject(array[i]);
	}

	public int getResult() {
		return this.result;
	}

	public static boolean equalsDate(final Date date1, final Date date2) {
		return equalsDate(date1, date2, 1000);
	}

	public static boolean equalsDate(final Date date1, final Date date2, long tolerance) {
		if (date1 == date2)
			return true;
		return (Math.abs(date1.getTime() - date2.getTime()) < tolerance);
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

	@Override
	public boolean equals(final Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "HashCodeGenerator result:" + this.result;
	}
}
