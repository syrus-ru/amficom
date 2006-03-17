/*-
 * $Id: AnalysisResult.java,v 1.1 2006/03/17 15:29:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.reflectometry.Qable;

/**
 * @version $Revision: 1.1 $, $Date: 2006/03/17 15:29:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class AnalysisResult implements Qable {
	private Result[] results;
	private boolean hasQ;
	private double q;

	AnalysisResult(final Result[] results, final double q) {
		assert results != null;

		this.results = results;
		this.hasQ = true;
		this.q = q;
	}

	AnalysisResult(final Result[] results) {
		assert results != null;

		this.results = results;
		this.hasQ = false;
		this.q = Double.NaN;
	}

	Result[] getResults() {
		return this.results;
	}

	public boolean hasQ() {
		return this.hasQ;
	}

	public double getQ() {
		if (this.hasQ) {
			return this.q;
		}
		throw new IllegalStateException("Has not q");
	}
}
