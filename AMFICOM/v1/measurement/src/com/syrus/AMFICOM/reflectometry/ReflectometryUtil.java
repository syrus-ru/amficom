/*-
 * $Id: ReflectometryUtil.java,v 1.1 2005/09/02 13:49:18 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/09/02 13:49:18 $
 * @module
 */
public class ReflectometryUtil {
	private static final double UPPER_AGENT_TIME = 30.0;

	/**
	 * ��������� ����� ���������� ��������� �� QP1640A
	 * @param rmp ��������� ���������
	 * @param upper true, ���� ����� ������ ������,
	 *   false, ���� ����� ����������� ������
	 * @return ����� ��������� � ��������, ������� ����������� ������� 1% + 1 ���
	 */
	public static double getEstimatedQP1640ATestTime(
			final ReflectometryMeasurementParameters rmp,
			final boolean upper) {
		double len = rmp.getTraceLength(); // km
		double dX = rmp.getResolution(); // m
		double lfd = rmp.hasLiveFiberDetection() ? 1 : 0;
		double kav = rmp.getNumberOfAverages() / 1024.0;

		if (! upper) {
			// ����������������� �������
			return 4.49 + lfd * .32 + kav * .248
				+ kav / Math.min(dX, 4) * (.495 + 0.0705 * len);
		} else {
			// � ������� ��� ������ ������
			return 5.1 + lfd * .32 + kav * .25
				+ kav / Math.min(dX, 4) * (.495 + 0.071 * len);
		}
	}

	/**
	 * ��������� ������ ����� ���������� ��������� �� ������.
	 * XXX: �� �����, ����� ������������ ����������,
	 * ������� ���������� ������ �� QP1640A
	 * @return ����� ���������� ���������, ��������� ������.
	 */
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp) {
		return getEstimatedQP1640ATestTime(rmp, true) + UPPER_AGENT_TIME;
	}
}
