/*
 * $Id: ModelTrace.java,v 1.3 2005/02/08 11:46:27 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/02/08 11:46:27 $
 * @module
 */
public abstract class ModelTrace
{
	public abstract int getLength();

	public abstract double getY(int x);

	public double[] getYArray(int x0, int N)
	{
		double[] ret = new double[N];
		for (int i = 0; i < N; i++)
			ret[i] = getY(x0 + i);
		return ret;
	}
	
	// if x0 + N > trace length, should fill with zero
	public double[] getYArrayZeroPad(int x0, int N)
	{
		int toEnd = getLength() - x0;
		if (toEnd < 0)
			toEnd = 0;
		if (N <= toEnd)
			return getYArray(x0, N);
		else
		{
			double[] temp = getYArray(x0, toEnd);
			double[] ret = new double[N];
			System.arraycopy(temp, 0, ret, 0, toEnd);
			// в принципе, обнулять не обязательно, но так почему-то принято
			for (int i = toEnd; i < N; i++)
				ret[i] = 0;
			return ret;
		}
	}

	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
