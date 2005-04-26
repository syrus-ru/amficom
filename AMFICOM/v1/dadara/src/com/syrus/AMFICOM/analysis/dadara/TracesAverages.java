/*-
 * $Id: TracesAverages.java,v 1.2 2005/04/26 16:08:55 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Хранит данные по совокупности набора рефлектограмм
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/04/26 16:08:55 $
 * @module
 */
public class TracesAverages
{
	// all of the arrays will have length >= minTraceLength;
	// the values that are beyond [0..minTraceLength-1] does not matter.

	// general info
	public int minTraceLength = 0; // min. trace length (units = sample)
	public int nTraces = 0; // number of averaged traces
	public double[] avY = null; // average Y
	
	// noise info: 3 * sigma level
	public double[] avNoise = null; // average noise of Y
	public double[] noiseAv = null; // noise of averaged Y

	// mf info
	public double[] maxYMF = null; // max Y of mf
	public double[] minYMF = null; // min Y of mf
	
	// bs info
	public double ior = 0; // units = 1
	public double deltaX = 0; // units = m
	public double pulseWidth = 0; // units = ns
}
