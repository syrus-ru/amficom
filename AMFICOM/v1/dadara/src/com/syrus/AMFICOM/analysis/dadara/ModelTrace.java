/*
 * $Id: ModelTrace.java,v 1.2 2005/01/27 08:41:14 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/01/27 08:41:14 $
 * @module
 */
public abstract class ModelTrace
{
	public abstract int getLength();

	public abstract double getY(int x);

	// if x0 + N > trace length, should fill with zero
	public double[] getYArray(int x0, int N)
	{
		double[] ret = new double[N];
		for (int i = 0; i < N; i++)
			ret[i] = getY(x0 + i);
		return ret;
	}

	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
