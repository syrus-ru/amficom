/*
 * $Id: ArrayModelTrace.java,v 1.3 2005/10/06 13:34:02 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/10/06 13:34:02 $
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
		this.y = data;
	}
	@Override
	public int getLength()
	{
		return this.y.length;
	}

	@Override
	public double getY(int x)
	{
		return this.y[x];
	}
}
