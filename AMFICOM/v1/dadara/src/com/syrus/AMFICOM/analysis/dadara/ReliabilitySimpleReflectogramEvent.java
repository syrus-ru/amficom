/*-
 * $Id: ReliabilitySimpleReflectogramEvent.java,v 1.2 2005/05/01 06:12:58 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/01 06:12:58 $
 * @module
 */
public interface ReliabilitySimpleReflectogramEvent
extends SimpleReflectogramEvent {
    /**
     * ����� ������������� �������
     */
    double RELIABLE = 0.99;

    /**
     * 
     * @return ��������� �� �������� ������������� ��� ������� �������.
     * �������� true ��������, ��� ����� �������� ����� {@link #getReliability}
     */
    boolean hasReliability();
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
