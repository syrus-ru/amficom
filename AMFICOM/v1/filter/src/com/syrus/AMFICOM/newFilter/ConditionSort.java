/*
 * $Id: ConditionSort.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class ConditionSort {
	
	private        int value;
	private static int size = 7;
	private static ConditionSort[] array = new ConditionSort [size];

	public static final int _TYPE_NUMBER_INT = 0;
	public static final ConditionSort TYPE_NUMBER_INT = new ConditionSort(_TYPE_NUMBER_INT);
	public static final int _TYPE_NUMBER_LONG = 1;
	public static final ConditionSort TYPE_NUMBER_LONG = new ConditionSort(_TYPE_NUMBER_LONG);
	public static final int _TYPE_NUMBER_DOUBLE = 2;
	public static final ConditionSort TYPE_NUMBER_DOUBLE = new ConditionSort(_TYPE_NUMBER_DOUBLE);
	public static final int _TYPE_STRING = 3;
	public static final ConditionSort TYPE_STRING = new ConditionSort(_TYPE_STRING);
	public static final int _TYPE_DATE = 4;
	public static final ConditionSort TYPE_DATE = new ConditionSort(_TYPE_DATE);
	public static final int _TYPE_CONSTRAINT = 5;
	public static final ConditionSort TYPE_CONSTRAINT = new ConditionSort(_TYPE_CONSTRAINT);
	public static final int _TYPE_LINKED_IDS = 5;
	public static final ConditionSort TYPE_LINKED_IDS = new ConditionSort(_TYPE_LINKED_IDS);

	private ConditionSort (int value) {
	    this.value = value;
	    array[value] = this;
	}
	
	public int value () {
	    return this.value;
	}

	public static ConditionSort from_int (int value) {
		if (value >= 0 && value < size)
			return array[value];
		throw new org.omg.CORBA.BAD_PARAM ();
	}
} 

