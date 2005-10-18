/*-
 * $Id: ReflectometryEvaluationOverallResult.java,v 1.2 2005/10/18 13:25:25 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * ��������� ��� ������
 * ������ D � Q ��������� (��� ���� �������������) ���������� ���������.
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/18 13:25:25 $
 * @module reflectometry
 */
public interface ReflectometryEvaluationOverallResult {
	/**
	 * ���������� true, ���� D � Q ��������� ��������.
	 * ������������� ���������� �������������, ��� �� ���� ����������
	 * ����������.
	 * @return true, ���� D � Q ��������� ��������.
	 */
	boolean hasDQ();
	/**
	 * ���������� D-�������� ���������� ���������.
	 * �������� �� ������, ���������� � ����������, �������� ������� �������
	 * �������������� � �� �����.
	 * @return D-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQ() == false</tt>.
	 */
	double getD();
	/**
	 * ���������� Q-�������� ���������� ���������.
	 * �������� �����������, ������������, � ��������� [0..1], ��������
	 * ������� ��������:
	 * <ul>
	 * <li>Q=1 - ������������ 100% (������)
	 * <li>Q=0 - ������������ 0%, ������� ��������� ���������� ��������
	 * @return Q-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQ() == false</tt>.
	 */
	double getQ();
}
