/*
 * $Id: ModelTrace.java,v 1.1 2005/01/25 14:16:50 saa Exp $
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

	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
