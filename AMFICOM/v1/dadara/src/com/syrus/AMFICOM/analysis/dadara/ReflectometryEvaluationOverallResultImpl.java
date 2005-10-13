/*-
 * $Id: ReflectometryEvaluationOverallResultImpl.java,v 1.2 2005/10/13 17:16:06 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * Unmodifiable реализация ReflectometryEvaluationOverallResult
 * с дополнительным интерфейсом, нужным для dadara.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/13 17:16:06 $
 * @module dadara
 */
public final class ReflectometryEvaluationOverallResultImpl
implements ReflectometryEvaluationOverallResult {
	private boolean dqPresent;
	private double dParam;
	private double qParam;

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#hasDQ()
	 */
	public boolean hasDQ() {
		return this.dqPresent;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#getD()
	 */
	public double getD() {
		if (this.dqPresent)
			return this.dParam;
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#getQ()
	 */
	public double getQ() {
		if (this.dqPresent)
			return this.qParam;
		throw new IllegalStateException();
	}

	/**
	 * copy-constructor
	 */
	public ReflectometryEvaluationOverallResultImpl (
			ReflectometryEvaluationOverallResult that) {
		this.dqPresent = that.hasDQ();
		if (this.dqPresent) {
			this.dParam = that.getD();
			this.qParam = that.getQ();
		} else {
			this.dParam = 0.0; // won't be used
			this.qParam = 0.0; // won't be used
		}
	}
}
