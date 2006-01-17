/*-
 * $Id: TracesAverages.java,v 1.5 2006/01/17 12:22:28 saa Exp $
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
 * @version $Revision: 1.5 $, $Date: 2006/01/17 12:22:28 $
 * @module
 */
public class TracesAverages
{
	// усредненная рефлектограмма, ее шум и усредненный шум
	public TracePreAnalysis av;

	// general info
	public int nTraces = 0; // number of averaged traces

	// mf info, длина этих массивов может быть меньше av.traceLength
	public double[] maxYMF = null; // max Y of mf
	public double[] minYMF = null; // min Y of mf
}
