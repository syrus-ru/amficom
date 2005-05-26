/*
 * $Id: ModelTrace.java,v 1.8 2005/05/26 13:32:51 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * Частный случай {@link ModelTraceRange}:
 * кривая, определенная на участке от 0 до getLength()-1.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.8 $, $Date: 2005/05/26 13:32:51 $
 * @module
 */
public abstract class ModelTrace extends ModelTraceRange
{
	/**
	 * Возвращает длину кривой в точках
	 * @return длина
	 */
	public abstract int getLength();

    public int getBegin() {
        return 0;
    }

    public int getEnd() {
        return getLength() - 1;
    }

	/**
	 * Возвращает значения игреков на всей длине рефлектограммы 
	 * @return представление кривой в виде массива
	 */
	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}

    /**
     * Расширяет контракт {@link ModelTraceRange} на значения иксов,
     * выходящих за область определения кривой.
     * 
     * @param x0 начальный икс, должен быть >=0
     * @param N количество иксов
     * @return значения игреков на запрошенном диапазоне иксов x0 .. x0+N-1,
     * полагая нулевыми значения за пределами области определения.
     * (в принципе, обнулять не обязательно, но так почему-то принято)
     * 
     * @todo remove as outdated (see {@link ModelTraceRange})
     */
    private double[] getYArrayZeroPad1(int x0, int N)
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
            for (int i = toEnd; i < N; i++)
                ret[i] = 0;
            return ret;
        }
    }
}
