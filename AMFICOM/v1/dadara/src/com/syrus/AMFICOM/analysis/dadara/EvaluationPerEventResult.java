/*-
 * $Id: EvaluationPerEventResult.java,v 1.1 2005/10/11 16:42:01 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/10/11 16:42:01 $
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
	 * ���������� Q-�������� ��� ������� �������.
	 * @param i ����� ������� �������
	 * @return Q-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, ���� {@link #hasQK}(i) == false
	 */
	double getQ(int i);
	/**
	 * ���������� K-�������� ��� ������� �������.
	 * @param i ����� ������� �������
	 * @return K-�������� ��� ������� �������.
	 * @throws IndexOutOfBoundsException, ����
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, ���� {@link #hasQK}(i) == false
	 */
	double getK(int i);
}
