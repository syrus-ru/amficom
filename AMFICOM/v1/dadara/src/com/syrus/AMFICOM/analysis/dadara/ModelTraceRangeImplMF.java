/*-
 * $Id: ModelTraceRangeImplMF.java,v 1.1 2005/05/01 09:35:16 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/05/01 09:35:16 $
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

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public double getY(int x) {
        return mf.fun(x);
    }

    public double[] getYArray(int x0, int N) {
        if (x0 == begin && N == end - begin + 1) {
            if (cache == null)
                cache = mf.funFillArray(x0, 1.0, N);
            return cache;
        } else
            return mf.funFillArray(x0, 1.0, N);
    }
}
