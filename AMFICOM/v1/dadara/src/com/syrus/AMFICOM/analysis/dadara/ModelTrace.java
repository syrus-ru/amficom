/*
 * $Id: ModelTrace.java,v 1.4 2005/02/15 14:19:18 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/02/15 14:19:18 $
 * @module
 */
public abstract class ModelTrace
{
	/**
	 * ���������� ����� ������ � ������
	 * @return �����
	 */
	public abstract int getLength();

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
	 * ���������� �������� ������� �� ��������� ����� x0 .. x0+N-1,
	 * ������� �������� �������� �� ��������� ������� �����������.
	 * @param x0 ��������� ���, ������ ���� >=0
	 * @param N ���������� �����
	 * @return ������ ��������
	 */
	public double[] getYArrayZeroPad(int x0, int N)
	{
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
			// � ��������, �������� �� �����������, �� ��� ������-�� �������
			for (int i = toEnd; i < N; i++)
				ret[i] = 0;
			return ret;
		}
	}

	/**
	 * ���������� �������� ������� �� ���� ����� �������������� 
	 * @return ������������� ������ � ���� �������
	 */
	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
