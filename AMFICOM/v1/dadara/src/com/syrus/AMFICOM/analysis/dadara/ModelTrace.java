/*
 * $Id: ModelTrace.java,v 1.5 2005/04/15 18:07:18 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/04/15 18:07:18 $
 * @module
 */
public abstract class ModelTrace
{
	/**
	 * ¬озвращает длину кривой в точках
	 * @return длина
	 */
	public abstract int getLength();

	/**
	 * ѕолучить значение кривой при данном иксе 
	 * @param x икс
	 * @return игрек
	 */
	public abstract double getY(int x);

	/**
	 * ¬озвращает значени€ игреков на указанном диапазоне иксов x0 .. x0+N-1.
	 * результат за пределами области определени€ рефлектограммы (x < 0 или x >= length)
	 * не определен.
	 * @param x0 Ќачальный икс
	 * @param N количество иксов
	 * @return массив значений
	 */
	public double[] getYArray(int x0, int N)
	{
		double[] ret = new double[N];
		for (int i = 0; i < N; i++)
			ret[i] = getY(x0 + i);
		return ret;
	}
	
	/**
	 * ¬озвращает значени€ игреков на диапазоне иксов x0 .. x0+N-1,
	 * полага€ нулевыми значени€ за пределами области определени€.
	 * @param x0 начальный икс, должен быть >=0
	 * @param N количество иксов
	 * @return массив значений
	 */
	public double[] getYArrayZeroPad(int x0, int N)
	{
		if (N <= 0) // на случай отрицательного N, возвращаем пустой массив
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
			// в принципе, обнул€ть не об€зательно, но так почему-то прин€то
			for (int i = toEnd; i < N; i++)
				ret[i] = 0;
			return ret;
		}
	}

	/**
	 * ¬озвращает значени€ игреков на всей длине рефлектограммы 
	 * @return представление кривой в виде массива
	 */
	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
