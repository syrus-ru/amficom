/*-
 * $Id: TraceAndMTAE.java,v 1.1 2005/07/08 10:08:37 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.io.BellcoreStructure;

public class TraceAndMTAE {
	// original bellcore
	private BellcoreStructure bs;

	// y[] trace (do not protect it too much, hope nobody will change this)
	private double[] y;

	// params used to analyse this trace, may be null
	AnalysisParameters ap;

	// cached analysis result, may be null
	private ModelTraceAndEvents mtae;
//
//	public TraceAndMTAE(BellcoreStructure bs) {
//		this.bs = bs;
//		this.y = bs.getTraceData();
//		this.ap = null;
//		this.mtae = null;
//	}

	public TraceAndMTAE(BellcoreStructure bs, AnalysisParameters ap) {
		this.bs = bs;
		this.y = bs.getTraceData();
		this.ap = ap;
		this.mtae = null;
	}

	public double getDeltaX() {
		return this.bs.getResolution();
	}

	public BellcoreStructure getBS() {
		return this.bs;
	}

	/**
	 * We hope clients will not modify this array
	 */
	public double[] getTraceData() {
		return this.y;
	}

	public void setAnalysisParameters(AnalysisParameters ap) {
		this.ap = ap;
		this.mtae = null;
	}

	/**
	 * @return analysed trace (if AnalysisParameters are set to not null),
	 * null (if AnalysisParameters are null or are not set).
	 */
	public ModelTraceAndEvents getMTAE() {
		if (this.ap == null)
			return null;
		if (this.mtae == null) {
			this.mtae = CoreAnalysisManager.performAnalysis(bs, ap).getMTAE();
		}
		return this.mtae;
	}
}
