/*-
 * $Id: ModelTraceRangeImplMF.java,v 1.2 2005/05/01 12:55:32 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/01 12:55:32 $
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
        if (cache == null) {
            cache = mf.funFillArray(begin, 1.0, end - begin + 1); 
        }
        double[] yRet = new double[N];
        // possible exceptions during arraycopy will just mean
        // that input x0 and N are out of range
        System.arraycopy(cache, x0 - begin, yRet, 0, N);
        return yRet;
    }
}
