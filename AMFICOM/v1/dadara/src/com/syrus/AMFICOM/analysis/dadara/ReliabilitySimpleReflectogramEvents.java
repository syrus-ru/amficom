/*-
 * $Id: ReliabilitySimpleReflectogramEvents.java,v 1.1 2005/04/14 12:03:17 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ��������� SimpleReflectogramEvent ���������� �������������.
 * ��� �������, �������� ���������� ������ ��, ������� ���
 * ��������� ����� ����������. ��� ����� ������� ����� ��������,
 * ����������� ������������� ����� ���������� ������ ����������.
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/14 12:03:17 $
 * @module
 */
public interface ReliabilitySimpleReflectogramEvents
extends SimpleReflectogramEvent {
    /**
     * ����� ������������� �������
     */
    static double RELIABLE = 0.99;

    /**
     * 
     * @return ��������� �� �������� ������������� ��� ������� �������.
     * �������� true ��������, ��� ����� �������� ����� {@link #getReliability}
     */boolean hasReliability();
    /**
     * �������� �� 0 �� 1, ������������ �������� ������������� �������
     * � ��������, ������� �� �����������. ���������� ������ ����
     * hasReliability() ���������� true.
     * <p>
     * �������� ��������:
     * <ul>
     * <li>0.0: ������� �� ����� ������ ��������
     * <li>�� RELIABLE �� 1.0: �������� ������� ������ ���������� 
     * @return �������� �� 0 �� 1
     * @throws IllegalArgumentException ������������� �� ����������
     */
    double getReliability();
}
