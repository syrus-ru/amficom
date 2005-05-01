/*-
 * $Id: ModelTraceRange.java,v 1.2 2005/05/01 13:50:29 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ��������� ������� ������.
 * ������� ������ ������������ ����������� ������.
 * ��������� ������� ��� ������� �������� �� ���������
 * ��������� [{@link #getBegin()}, {@link #getEnd()}]
 * �� ����������.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/05/01 13:50:29 $
 * @module
 */
public abstract class ModelTraceRange {
    public abstract int getBegin();
    public abstract int getEnd();

    /**
     * �������� �������� ������ ��� ������ ���� 
     * @param x ���
     * @return �����
     */
    public abstract double getY(int x);

    /**
     * ���������� �������� ������� �� ��������� ��������� ����� x0 .. x0+N-1.
     * ��������� �� ��������� ������� ����������� �������������� (x < 0 ��� x >= length)
     * �� ���������.
     * <p>
     * ������������� ����������� ���� ����� � ���������� �����������.
     * @param x0 ��������� ���
     * @param N ���������� �����
     * @return ������ ��������
     */
    public double[] getYArray(int x0, int N)
    {
        double[] ret = new double[N];
        for (int i = 0; i < N; i++)
            ret[i] = getY(x0 + i);
        return ret;
    }

    /**
     * ���������� �������� ������� �� ���� ������� �����������
     * @return ������ ��������
     */
    public double[] getYArray()
    {
        return getYArray(getBegin(), getEnd() - getBegin() + 1);
    }
}
