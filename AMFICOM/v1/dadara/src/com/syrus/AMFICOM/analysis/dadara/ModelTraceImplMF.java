/*
 * $Id: ModelTraceImplMF.java,v 1.5 2005/08/02 19:36:33 arseniy Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/02 19:36:33 $
 * @module
 */
public class ModelTraceImplMF extends ModelTrace
{
	private int length;
	private ModelFunction mf;
	private double[] cachedTrace = null;

	@Override
	public int getLength()
	{
		return this.length;
	}

	@Override
	public double[] getYArray(int x0, int N)
	{
		//return mf.funFillArray(x0, 1.0, N);
		if (x0 >= 0 && x0 + N <= this.length && this.cachedTrace != null)
		{
			double[] out = new double[N];
			System.arraycopy(this.cachedTrace, x0, out, 0, N);
			return out;
		}
		return this.mf.funFillArray(x0, 1.0, N);
	}

	@Override
	public double getY(int x)
	{
		return this.mf.fun(x);
	}

	/**
	 * Creates ModelTrace based on ModelFunction object (wrapper).
	 * Assumes input ModelFunction object be not modified anywhere else.
	 * May perform caching.
	 * @param mf ModelFunction object to be wrapped
	 * @param length trace length; x will be allowed to range from 0 to length-1
	 */
	public ModelTraceImplMF(ModelFunction mf, int length)
	{
		this.mf = mf;
		this.length = length;
		this.cachedTrace = mf.funFillArray(0, 1.0, length);
	}
}
