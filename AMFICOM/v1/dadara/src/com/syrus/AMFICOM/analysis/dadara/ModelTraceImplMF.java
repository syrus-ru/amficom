/*
 * $Id: ModelTraceImplMF.java,v 1.6 2006/01/11 12:10:57 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2006/01/11 12:10:57 $
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
		//try to read from cache
		if (x0 >= 0 && x0 + N <= this.length && this.cachedTrace != null)
		{
			double[] out = new double[N];
			System.arraycopy(this.cachedTrace, x0, out, 0, N);
			return out;
		}
		//cache miss - get from MF
		return this.mf.funFillArray(x0, 1.0, N);
	}

	@Override
	public double getY(int x)
	{
		//try to read from cache
		if (x >=0 && x <= this.length && this.cachedTrace != null) {
			return this.cachedTrace[x];
		}
		//cache miss - get from MF
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
		// preload cache
		this.cachedTrace = mf.funFillArray(0, 1.0, length);
	}
}
