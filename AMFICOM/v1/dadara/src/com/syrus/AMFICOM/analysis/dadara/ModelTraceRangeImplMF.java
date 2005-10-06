/*-
 * $Id: ModelTraceRangeImplMF.java,v 1.5 2005/10/06 13:34:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Может быть полезен для описания участка модельной кривой
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class ModelTraceRangeImplMF extends ModelTraceRange {
	private int begin;
	private int end;
	private ModelFunction mf;
	private double[] cache = null;

	public ModelTraceRangeImplMF(ModelFunction mf, int begin, int end) {
		this.begin = begin;
		this.end = end;
		this.mf = mf;
	}

	@Override
	public int getBegin() {
		return this.begin;
	}

	@Override
	public int getEnd() {
		return this.end;
	}

	@Override
	public double getY(int x) {
		return this.mf.fun(x);
	}

	@Override
	public double[] getYArray(int x0, int N) {
		if (this.cache == null) {
			this.cache = this.mf.funFillArray(
					this.begin, 1.0, this.end - this.begin + 1); 
		}
		double[] yRet = new double[N];
		// possible exceptions during arraycopy will just mean
		// that input x0 and N are out of range
		System.arraycopy(this.cache, x0 - this.begin, yRet, 0, N);
		return yRet;
	}
}
