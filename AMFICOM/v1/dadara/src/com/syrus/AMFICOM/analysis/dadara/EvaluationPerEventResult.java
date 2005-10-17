/*-
 * $Id: EvaluationPerEventResult.java,v 1.2 2005/10/17 13:45:11 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/10/17 13:45:11 $
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
	 * @param i ����� ������� �������
	 * @return Q-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, ���� <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getQ(int i);

	/**
	 * ���������� K-�������� ��� ������� �������.
	 * @param i ����� ������� �������
	 * @return K-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, ���� <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getK(int i);
}
