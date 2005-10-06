/*-
 * $Id: ModelTraceRangeImplMTRSubrange.java,v 1.5 2005/10/06 13:34:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Sub-range of ModelTraceRange as a ModelTraceRange.
 * Able to incapsulate zero-padding of underlying ModelTraceRange.
 * At this moment, does not provide caching.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class ModelTraceRangeImplMTRSubrange extends ModelTraceRange {
	private ModelTraceRange mtr;
	private int begin;
	private int end;
	private boolean zeroPad;

	/**
	 * Creates sub-range ModelTraceRange
	 * @param mtr base MTR
	 * @param begin beginning position, inclisively
	 * @param end ending position, inclusevely
	 * @param zeroPad true, if base MTR should be treated as zero-padded
	 * @throws IllegalArgumentException input mtr is null 
	 * @throws IllegalArgumentException input begin is > input end
	 * @throws IllegalArgumentException input begin or end is outside
	 *   [mtr.getBegin(),mtr.getEnd()], but zeroPad is false  
	 */
	public ModelTraceRangeImplMTRSubrange(ModelTrace mtr, int begin, int end, boolean zeroPad) {
		if (mtr == null)
			throw new IllegalArgumentException("null ModelTrace");
		if (!zeroPad) {
			if (begin < mtr.getBegin())
				throw new IllegalArgumentException("begin out of range");
			if (end > mtr.getEnd())
				throw new IllegalArgumentException("end out of range");
		}
		if (begin > end)
			throw new IllegalArgumentException("begin > end");
		this.mtr = mtr;
		this.begin = begin;
		this.end = end;
		this.zeroPad = zeroPad;
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
		// if zeroPad, we need to perform zero padding because there is no
		// underlying getYZeroPad().
		// Note, we do not check if x is within [this.begin,this.end] range. 
		if (this.zeroPad && (x < this.mtr.getBegin() || x > this.mtr.getEnd()))
			return 0.0;
		else
			return this.mtr.getY(x);
	}

	@Override
	public double[] getYArray(int x0, int N) {
		if (this.zeroPad)
			return this.mtr.getYArrayZeroPad(x0, N);
		else
			return this.mtr.getYArray(x0, N);
	}
}
