/*-
 * $Id: MeasurementTimeEstimator.java,v 1.1 2006/02/17 12:47:36 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/02/17 12:47:36 $
 * @module reflectometry
 */
public interface MeasurementTimeEstimator {
	/**
	 * ��������� ����� ���������� ���������.
	 * @param rmp ��������� ���������
	 * @param upper true, ���� ����� ������ ������;
	 *   false, ���� ����� ����������� ������.
	 * @return ����� ��������� � ��������
	 */
	double getEstimatedMeasurementTime (
			final ReflectometryMeasurementParameters rmp,
			final boolean upper);
}
