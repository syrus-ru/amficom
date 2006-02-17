/*-
 * $Id: ReflectometryUtil.java,v 1.5 2006/02/17 12:55:22 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2006/02/17 12:55:22 $
 * @module
 */
public final class ReflectometryUtil {
	
	private ReflectometryUtil() {
		assert false;
	}

	/**
	 * ������ ������ ��� ������� ��������� ��������������� �� ������
	 * � �������� ����� ��� � �������.
	 * �������.
	 */
	private static final double UPPER_AGENT_TIME = 30.0;

	/**
	 * ����� ������.
	 * ����� �������������� ����������������� ������� ������������ OTAU.
	 */
	private static final double SAFE_TIME = 20.0;

	/**
	 * ��������� ����� ���������� ��������� �� QP1640A.
	 * @param rmp ��������� ���������
	 * @param upper true, ���� ����� ������ ������,
	 *   false, ���� ����� ����������� ������
	 * @return ����� ��������� � ��������,
	 *   ������� ����������� ������� 1% + 1 ���
	 *   ����. ����������� 6% + 2 ��� (��. ����)
	 */
	public static double getEstimatedQP1640ATestTime(
			final ReflectometryMeasurementParameters rmp,
			final boolean upper) {
		double len = rmp.getTraceLength(); // km
		double dX = rmp.getResolution(); // m
		double lfd = rmp.hasLiveFiberDetection() ? 1 : 0;
		double kav = rmp.getNumberOfAverages() / 1024.0;

		double estimatedTime;
		if (! upper) {
			// ����������������� �������
			estimatedTime = 4.49 + lfd * .32 + kav * .248
				+ kav / Math.min(dX, 4) * (.495 + 0.0705 * len);
		} else {
			// � ������� ��� ������ ������
			estimatedTime = 5.1 + lfd * .32 + kav * .25
				+ kav / Math.min(dX, 4) * (.495 + 0.071 * len);
		}

		return estimatedTime;
	}

	/**
	 * ��������� ����� ���������� ��������� �� QP1640MR
	 * FIXME: ������ �������� (����������) ���������� ��� ������ 320 ��)
	 * @param rmp ��������� ���������
	 * @param upper true, ���� ����� ������� ������
	 * @return ����� ��������� � ��������
	 */
	public static double getEstimatedQP1640MRTestTime(
			final ReflectometryMeasurementParameters rmp,
			final boolean upper) {
		double len = rmp.getTraceLength(); // km
		double dX = rmp.getResolution(); // m
		double av = rmp.getNumberOfAverages();
		double estimatedTime =
			av * len / 131 * .4 * 8 / Math.min(8,dX) + av * 35 / 1600;
		estimatedTime += upper ? 2.0 : 0.5;
		return estimatedTime;
	}

	/**
	 * Estimator for 1640A and 1643A. Tested for 1643A only.
	 */ 
	private static final MeasurementTimeEstimator QP1643A_ESTIMATOR =
		new MeasurementTimeEstimator() {
			final public double getEstimatedMeasurementTime(
					final ReflectometryMeasurementParameters rmp,
					final boolean upper) {
				return getEstimatedQP1640ATestTime(rmp, upper);
			}
	};

	/**
	 * Estimator for 1640MR.
	 */ 
	private static final MeasurementTimeEstimator QP1640MR_ESTIMATOR =
		new MeasurementTimeEstimator() {
			final public double getEstimatedMeasurementTime(
					final ReflectometryMeasurementParameters rmp,
					final boolean upper) {
				return getEstimatedQP1640MRTestTime(rmp, upper);
			}
	};

	public static MeasurementTimeEstimator getQP1640AEstimator() {
		return QP1643A_ESTIMATOR; // use timing for 1643A
	}

	public static MeasurementTimeEstimator getQP1643AEstimator() {
		return QP1643A_ESTIMATOR;
	}

	public static MeasurementTimeEstimator getQP1640MREstimator() {
		return QP1640MR_ESTIMATOR;
	}

	/**
	 * ��������� ������ ����� ���������� ��������� �������.
	 * <p> XXX: �� �����, ����� ������������ ����������,
	 * ������� ���������� ������ �� QP1640A/1643A.
	 * @return ����� ���������� ���������, ��������� ������, ���������� � ��������
	 * @see #getUpperEstimatedAgentTestTime(ReflectometryMeasurementParameters, MeasurementTimeEstimator)
	 * @deprecated use {@link #getUpperEstimatedAgentTestTime(ReflectometryMeasurementParameters, MeasurementTimeEstimator)}
	 */
	@Deprecated
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp) {
		return getUpperEstimatedAgentTestTime(rmp, QP1643A_ESTIMATOR);
	}

	/**
	 * ��������� ������ ����� ���������� ��������� �������.
	 * ����������� ����� ��������:
	 * <ul>
	 * <li> ����� ��������� ������� ����� ���������
	 * <li> ����� �������� �� ������ � ���
	 * <li> ����� ��������� �� ���
	 * <li> ����� �������� �� ��� � ������
	 * <li> ����� ��������� ���������� �� ������
	 * </ul>
	 * <p> ������ �������� ������, "��� �� ������", ��������� �� �������
	 * �������� �� ����. �������� �� ������� �������.
	 * <p> ������� ������ ��������, ���������� �� 10-20 ���.
	 * @param rmp ��������� ���������
	 * @param estimator ����������� ������� ��������� ������� �������������
	 * @return ����� ���������� ���������, ��������� ������, ���������� � ��������
	 */
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp,
			final MeasurementTimeEstimator estimator) {
		// ���������� ����� ���������� ���������
		// � ����� ����������� ��������� � ������� ��������
		return estimator.getEstimatedMeasurementTime(rmp, true)
				+ UPPER_AGENT_TIME
				+ SAFE_TIME;
	}
}
