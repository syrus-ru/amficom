/*-
 * $Id: ModelTraceRangeImplMTRSubrange.java,v 1.1 2005/05/02 08:46:23 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Sub-range of ModelTraceRange as a ModelTraceRange.
 * At this moment, does not provide caching.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/05/02 08:46:23 $
 * @module
 */
public class ModelTraceRangeImplMTRSubrange extends ModelTraceRange {
    private ModelTraceRange mtr;
    private int begin;
    private int end;

    public ModelTraceRangeImplMTRSubrange(ModelTrace mtr, int begin, int end) {
        if (mtr == null)
            throw new IllegalArgumentException("null ModelTrace");
        if (begin < mtr.getBegin())
            throw new IllegalArgumentException("begin out of range");
        if (end > mtr.getEnd())
            throw new IllegalArgumentException("end out of range");
        if (begin > end)
            throw new IllegalArgumentException("begin > end");
        this.mtr = mtr;
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public double getY(int x) {
        return mtr.getY(x);
    }

    public double[] getYArray(int x0, int N) {
        return mtr.getYArray(x0, N);
    }
}
