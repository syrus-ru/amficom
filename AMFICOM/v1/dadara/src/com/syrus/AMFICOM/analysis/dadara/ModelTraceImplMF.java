/*
 * $Id: ModelTraceImplMF.java,v 1.2 2005/02/15 06:51:48 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/02/15 06:51:48 $
 * @module
 */
public class ModelTraceImplMF extends ModelTrace
{
	private int length;
	private ModelFunction mf;

	public int getLength()
	{
		return length;
	}

	public double[] getYArray(int x0, int N)
	{
//		Throwable st = new Throwable("getYArray: stack trace:");
//		st.printStackTrace();
		return mf.funFillArray(x0, 1.0, N);
	}

	public double getY(int x)
	{
		return mf.fun(x);
	}

	public ModelTraceImplMF(ModelFunction mf, int length)
	{
		this.mf = mf;
		this.length = length;
	}
}
