/*-
 * $Id: ModelTraceRange.java,v 1.4 2005/05/26 13:32:51 saa Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/05/26 13:32:51 $
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
     * ���������� �������� ������� �� ��������� �� ������ �� ����� ������������
     * ������� �������.
     * @param ev ��������
     * @return ������ �������� Y
     */
    public double[] getYRE(SimpleReflectogramEvent ev)
    {
        return getYArray(ev.getBegin(), ev.getEnd() - ev.getBegin() + 1);
    }

    /**
     * ���������� �������� ������� �� ���� ������� �����������
     * @return ������ ��������
     */
    public double[] getYArray()
    {
        return getYArray(getBegin(), getEnd() - getBegin() + 1);
    }

    /**
     * ������������ ����� �� �������� �����������
     * (�� ����� ������� ���������� - ����� ������������� �����)
     * @param x0 ��������� ���
     * @param N ���������� �����
     * @return �������� ������� �� ����������� ��������� ����� x0 .. x0+N-1,
     * ������� �������� �������� �� ��������� ������� �����������.
     * (� ��������, �������� �� �����������, �� ��� ������-�� �������)
     * ���� ��������� ������������� ���������� �����, ���������� ������ ������
     */
    public double[] getYArrayZeroPad(int x0, int N)
    {
        if (N <= 0) // �� ������ �������������� N, ���������� ������ ������
            return new double[0];

        int begin = getBegin();
        int end = getEnd();

        // ���� ������������ �� ����
        if (x0 >= begin && x0 + N - 1 <= end)
            return getYArray(x0, N);

        // ���� ������������ ����
        double[] ret = new double[N];
        int from = Math.max(begin, x0);
        int toex = Math.min(end + 1, x0 + N); // exclusively
        for (int i = 0; i < from; i++)
            ret[i] = 0;
        System.arraycopy(
                getYArray(from, toex - from),
                0,
                ret,
                from - x0,
                toex - from);
        for (int i = toex - x0; i < N; i++)
            ret[i] = 0;
        return ret;
    }
}
