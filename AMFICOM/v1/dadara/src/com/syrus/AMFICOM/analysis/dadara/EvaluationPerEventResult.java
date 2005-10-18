/*-
 * $Id: EvaluationPerEventResult.java,v 1.3 2005/10/18 13:14:10 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ��������� ����� (��� ��� �/�) ���������� ��������� � ��������.
 * ����� �������������� ��������� ������� ��� ��������� � ��������,
 * ��������������� ������������ �/�.
 * <p>
 * FIXME: ��������, ��������� ������� �� �/� ���� ���������� �� ��������� �� �������
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/10/18 13:14:10 $
 * @module
 */
public interface EvaluationPerEventResult {
	/**
	 * ���������� ����� ������� ���������� �������
	 * @return ����� ������� ���������� �������
	 */
	int getNEvents();

	/**
	 * ���������� true, ���� Q- � K-��������� ��� ������� ������� ��������.
	 * @param i ����� ������� �������
	 * @return true, ���� Q- � K-��������� ��� ������� ������� ��������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 */
	boolean hasQK(int i);

	/**
	 * ��������� �������, �� ������� Q- � K-��������� ��� �������
	 * ������� ����������.
	 * ���������� true, ���� �������� �������� ��������� �� ��������������,
	 * false, ���� � ������� ������� Q � K �� ���������� �� ������ �������
	 * (��������, Q � K ��� ���� ������� �� ����������).
	 * @param i ����� ������� �������
	 * @throws IllegalStateException, ���� Q � K ��� ������� �������
	 * ��������, �.�.
	 * <tt>{@link #hasQK}(i) != false</tt>
	 * @return true, ���� �������� �������� ��������� �� ��������������,
	 * false, ���� � ������� ������� Q � K �� ���������� �� ������ �������
	 * (��������, Q � K ��� ���� ������� �� ����������).
	 */
	boolean isModified(int i);

	/**
	 * ���������� Q-�������� ��� ������� �������.
	 * �������� Q-��������� ����� � ��������� [0,1].
	 * <p>
	 * <ul>
	 * <li>Q=1 - ������������ 100%, ������ ������������
	 * <li>Q=0 - ������������ 0%, ������ ��������������,
	 * ������� ����� ���� ��������� ���������� ��������
	 * </ul>
	 * @param i ����� ������� �������
	 * @return Q-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>
	 * @throws IllegalStateException, ���� <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getQ(int i);

	/**
	 * ���������� K-�������� ��� ������� �������.
	 * �������� K-��������� ����� � ��������� [0,1].
	 * <p>
	 * <ul>
	 * <li>K=0 - ��������� 0%,
	 * <li>K=1 - ��������� 100%,
	 * </ul>
	 * @param i ����� ������� �������
	 * @return K-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, ���� <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getK(int i);
}
