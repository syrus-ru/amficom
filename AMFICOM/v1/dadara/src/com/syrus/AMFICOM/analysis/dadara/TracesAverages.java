/*-
 * $Id: TracesAverages.java,v 1.3 2005/06/09 08:00:19 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Хранит данные по совокупности набора рефлектограмм,
 * необходимую для построения эталона.
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/06/09 08:00:19 $
 * @module
 */
public class TracesAverages
{
    // усредненная рефлектограмма, ее шум и усредненный шум
    public TracePreAnalysis av;

    // all of the arrays will have length >= av.traceLength;
	// the values that are beyond [0..av.traceLength-1] does not matter.

	// general info
	public int nTraces = 0; // number of averaged traces

	// mf info
	public double[] maxYMF = null; // max Y of mf
	public double[] minYMF = null; // min Y of mf
}
