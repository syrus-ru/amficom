/*
 * $Id: ArrayModelTrace.java,v 1.2 2005/07/14 14:28:38 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/07/14 14:28:38 $
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
	@Override
	public int getLength()
	{
		return y.length;
	}

	@Override
	public double getY(int x)
	{
		return y[x];
	}
}
