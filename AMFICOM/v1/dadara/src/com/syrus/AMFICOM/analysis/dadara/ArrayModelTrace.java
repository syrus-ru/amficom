/*
 * $Id: ArrayModelTrace.java,v 1.1 2005/01/25 14:16:50 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/01/25 14:16:50 $
 * @module
 */
public class ArrayModelTrace extends ModelTrace
{
	private double y[];

	/**
	 * Constructs ModelTrace with given data array.
	 * Does not make a copy of the array.
	 * @param data array to use as a trace
	 */
	public ArrayModelTrace(double[] data)
	{
		y = data;
	}
	public int getLength()
	{
		return y.length;
	}

	public double getY(int x)
	{
		return y[x];
	}
}
