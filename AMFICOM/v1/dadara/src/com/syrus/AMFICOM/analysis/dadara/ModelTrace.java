/*
 * $Id: ModelTrace.java,v 1.8 2005/05/26 13:32:51 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * ������� ������ {@link ModelTraceRange}:
 * ������, ������������ �� ������� �� 0 �� getLength()-1.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.8 $, $Date: 2005/05/26 13:32:51 $
 * @module
 */
public abstract class ModelTrace extends ModelTraceRange
{
	/**
	 * ���������� ����� ������ � ������
	 * @return �����
	 */
	public abstract int getLength();

    public int getBegin() {
        return 0;
    }

    public int getEnd() {
        return getLength() - 1;
    }

	/**
	 * ���������� �������� ������� �� ���� ����� �������������� 
	 * @return ������������� ������ � ���� �������
	 */
	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}

    /**
     * ��������� �������� {@link ModelTraceRange} �� �������� �����,
     * ��������� �� ������� ����������� ������.
     * 
     * @param x0 ��������� ���, ������ ���� >=0
     * @param N ���������� �����
     * @return �������� ������� �� ����������� ��������� ����� x0 .. x0+N-1,
     * ������� �������� �������� �� ��������� ������� �����������.
     * (� ��������, �������� �� �����������, �� ��� ������-�� �������)
     * 
     * @todo remove as outdated (see {@link ModelTraceRange})
     */
    private double[] getYArrayZeroPad1(int x0, int N)
    {
        if (N <= 0) // �� ������ �������������� N, ���������� ������ ������
            return new double[0];
        int toEnd = getLength() - x0;
        if (toEnd < 0)
            toEnd = 0;
        if (N <= toEnd)
            return getYArray(x0, N);
        else
        {
            double[] temp = getYArray(x0, toEnd);
            double[] ret = new double[N];
            System.arraycopy(temp, 0, ret, 0, toEnd);
            for (int i = toEnd; i < N; i++)
                ret[i] = 0;
            return ret;
        }
    }
}
