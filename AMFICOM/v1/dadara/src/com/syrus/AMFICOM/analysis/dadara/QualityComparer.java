/*-
 * $Id: QualityComparer.java,v 1.2 2005/10/12 08:06:12 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.HavingLoss;
import com.syrus.AMFICOM.analysis.dadara.events.HavingY0;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * ���������� ��������� �������� �����.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/12 08:06:12 $
 * @module dadara
 */
public class QualityComparer
implements EvaluationPerEventResult, ReflectometryEvaluationOverallResult {
	private boolean dqDefined;
	private double dParam;
	private double qParam;
	private boolean[] qkDefined;
	private double[] qiValues;
	private double[] kiValues;
	/**
	 * @param mtae ������������ ��������������
	 * @param mtm mtm �������
	 * @param rcomp ��������� ������������� ������� �/� � �������.
	 *    ����� ���� null ������ ���� hasAlarms == true.
	 *    XXX: ���� �������� �� ������ ���� ������� �������, ��������� ����
	 * @param hasAlarms ���������� �� ������. ���� true, �� � ���������� �����
	 *    "��������� ��������� ���������� ��-�� �������".
	 *    Note: ���� �������������� "������ ����������" �� �������,
	 *    � ������ ����������, �� ������������ ������ �� ���� ����� ���������
	 *    �������� false ����� �������� � ������������ ����������� ���������.
	 */
	public QualityComparer(ModelTraceAndEvents mtae,
			ModelTraceManager mtm,
			SimpleReflectogramEventComparer rcomp,
			boolean hasAlarms) {
		this.dqDefined = false;
		this.dParam = 0.0; // won't be used
		this.qParam = 0.0; // won't be used

		this.qkDefined = new boolean[mtae.getNEvents()];
		this.qiValues = new double[mtae.getNEvents()];
		this.kiValues = new double[mtae.getNEvents()];
		for (int i = 0; i < this.qkDefined.length; i++) {
			this.qkDefined[i] = false;
			this.qiValues[i] = 0.0; // won'k be used
			this.kiValues[i] = 0.0; // won'k be used
		}

		if (!hasAlarms) {
			// ��������� D, Q
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
						this.qParam = this.dParam / mtm.getDYScaleForEventBeginning(etalonEot);
//						System.out.printf("[*]: %g  %g\n", this.dParam, this.qParam);
					}
				}
			}

			// ��������� Ki, Qi
			for (int i = 0; i < curEot; i++) {
				int j = rcomp.getEtalonIdByProbeIdMostStrict(i);
				if (j < 0) {
					continue;
				}
				DetailedEvent ce = mtae.getDetailedEvent(i);
				DetailedEvent ee = mtm.getMTAE().getDetailedEvent(j);
				if (!(ce instanceof HavingLoss && ee instanceof HavingLoss)) {
					continue;
				}
				double dyc = ((HavingLoss)ce).getLoss();
				double dye = ((HavingLoss)ee).getLoss();
				double delta = Math.abs(dyc - dye);
				double t = Math.max(mtm.getDYScaleForEventBeginning(etalonEot),
						mtm.getDYScaleForEventEnd(etalonEot));
				double qi = delta / t;
				double ki = delta / Math.max(Math.abs(dyc), Math.abs(dye)); // FIXME: implement another formulae for ki
				//double ki = delta / (Math.abs(dye) + noise);
				this.qiValues[i] = qi;
				this.kiValues[i] = ki;
//				System.out.printf("[%d(%d):%d(%d)]: %g  %g\n",
//						i, ce.getEventType(),
//						j, ee.getEventType(), qi, ki);
			}
			//��������� Qi, Ki...
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
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#getQ(int)
	 */
	public double getQ(int i) {
		return this.qiValues[i];
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult#getK(int)
	 */
	public double getK(int i) {
		return this.kiValues[i];
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