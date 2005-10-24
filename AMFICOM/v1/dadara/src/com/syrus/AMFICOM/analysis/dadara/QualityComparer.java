/*-
 * $Id: QualityComparer.java,v 1.7 2005/10/24 15:43:25 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.HavingLoss;
import com.syrus.AMFICOM.analysis.dadara.events.HavingY0;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * Определяет параметры качества линии.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.7 $, $Date: 2005/10/24 15:43:25 $
 * @module dadara
 */
public class QualityComparer
implements EvaluationPerEventResult, ReflectometryEvaluationOverallResult {
	private boolean dqDefined;
	private double dParam;
	private double qParam;
	private boolean[] qkDefined;
	private boolean[] qkModified;
	private double[] qiValues;
	private double[] kiValues;
	/**
	 * @param mtae сравниваемая рефлектограмма
	 * @param mtm mtm эталона
	 * @param rcomp результат сопоставления событий р/г и эталона.
	 *    Может быть null только если hasAlarms == true.
	 *    XXX: если вычислен по другой паре наборов событий, возникнет сбой
	 * @param hasAlarms обнаружены ли алармы. Если true, то в результате будет
	 *    "параметры сравнения недоступны из-за отличий".
	 *    Note: Если рефлектограмма "сильно отличается" от эталона,
	 *    и алармы обнаружены, то некорректная подача на вход этого параметра
	 *    значения false может привести к неправильным результатам сравнения.
	 */
	public QualityComparer(ModelTraceAndEvents mtae,
			ModelTraceManager mtm,
			SimpleReflectogramEventComparer rcomp,
			boolean hasAlarms) {
		this.dqDefined = false;
		this.dParam = 0.0; // won't be used
		this.qParam = 0.0; // won't be used

		this.qkDefined = new boolean[mtae.getNEvents()];
		this.qkModified = new boolean[mtae.getNEvents()];
		this.qiValues = new double[mtae.getNEvents()];
		this.kiValues = new double[mtae.getNEvents()];
		for (int i = 0; i < this.qkDefined.length; i++) {
			this.qkDefined[i] = false;
			this.qkModified[i] = false;
			this.qiValues[i] = 0.0; // won't be used
			this.kiValues[i] = 0.0; // won't be used
		}

		if (!hasAlarms) {
			// вычисляем D, Q
			int etalonEot = mtm.getMTAE().getNEvents() - 1;
			int curEot = -1;
			if (etalonEot >= 0) {
				curEot = rcomp.getProbeIdByEtalonId(etalonEot);
				if (curEot > 0) {
					DetailedEvent ee = mtm.getMTAE().getDetailedEvent(etalonEot);
					DetailedEvent ce = mtae.getDetailedEvent(curEot);
					if (ee instanceof HavingY0 && ce instanceof HavingY0) {
						double eY0 = ((HavingY0)ee).getY0();
						double cY0 = ((HavingY0)ce).getY0();
						this.dqDefined = true;
						this.dParam = cY0 - eY0;
						final double delta = Math.abs(this.dParam);
						final double wt = mtm.getDYScaleForEventBeginning(etalonEot);
						this.qParam = delta >= wt ? 0.0 : 1.0 - delta / wt;
//						System.out.println("delta " + delta + " wt " + wt);
//						System.out.printf("[*]: %g  %g\n", this.dParam, this.qParam);
					}
				}
			}

			// вычисляем Ki, Qi
			for (int i = 0; i <= curEot; i++) {
				int j = rcomp.getEtalonIdByProbeIdMostStrict(i);
				if (j < 0) {
					this.qkModified[i] = true;
					continue;
				}
				DetailedEvent ce = mtae.getDetailedEvent(i);
				DetailedEvent ee = mtm.getMTAE().getDetailedEvent(j);
				if (!(ce instanceof HavingLoss && ee instanceof HavingLoss)) {
					this.qkModified[i] = false;
					continue;
				}
				double dyc = ((HavingLoss)ce).getLoss();
				double dye = ((HavingLoss)ee).getLoss();
				double delta = Math.abs(dyc - dye);
				double wt = Math.max(mtm.getDYScaleForEventBeginning(j),
						mtm.getDYScaleForEventEnd(j));
				double qi = delta >= wt ? 0.0 : 1.0 - delta / wt;
				double ki = delta / Math.max(Math.abs(dyc), Math.abs(dye));
				if (ki > 1) {
					ki = 1;
				}
				// qi is in [0..1]
				// ki is in [0..1]
				this.qkDefined[i] = true;
				this.qiValues[i] = qi;
				this.kiValues[i] = ki;
//				System.out.printf("[%d(%d):%d(%d)]: {%g %g} %g  %g\n",
//						i, ce.getEventType(),
//						j, ee.getEventType(), delta, wt, qi, ki);
			}
			//выставить Qi, Ki...
		}
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#getNEvents()
	 */
	public int getNEvents() {
		return this.qkDefined.length;
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#hasQK(int)
	 */
	public boolean hasQK(int i) {
		return this.qkDefined[i];
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#isModified(int)
	 */
	public boolean isModified(int i) {
		if (!this.qkDefined[i]) {
			return this.qkModified[i];
		}
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#getQ(int)
	 */
	public double getQ(int i) {
		if (this.qkDefined[i]) {
			return this.qiValues[i];
		}
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#getK(int)
	 */
	public double getK(int i) {
		if (this.qkDefined[i]) {
			return this.kiValues[i];
		}
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#hasDQ()
	 */
	public boolean hasDQ() {
		return this.dqDefined;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#getD()
	 */
	public double getD() {
		return this.dParam;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult#getQ()
	 */
	public double getQ() {
		return this.qParam;
	}
}
