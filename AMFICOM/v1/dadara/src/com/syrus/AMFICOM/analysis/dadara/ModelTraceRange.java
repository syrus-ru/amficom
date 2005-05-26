/*-
 * $Id: ModelTraceRange.java,v 1.4 2005/05/26 13:32:51 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ќписывает участок кривой.
 * ѕрирода кривой определ€етс€ реализацией класса.
 * ѕоведение объекта при запросе значени€ за пределами
 * диапазона [{@link #getBegin()}, {@link #getEnd()}]
 * не определено.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.4 $, $Date: 2005/05/26 13:32:51 $
 * @module
 */
public abstract class ModelTraceRange {
    public abstract int getBegin();
    public abstract int getEnd();

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
     * <p>
     * –екомендуетс€ перегружать этот метод в конкретных реализаци€х.
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
     * ¬озвращает значени€ игреков на диапазоне от начала до конца включительно
     * данного событи€.
     * @param ev диапазон
     * @return массив значений Y
     */
    public double[] getYRE(SimpleReflectogramEvent ev)
    {
        return getYArray(ev.getBegin(), ev.getEnd() - ev.getBegin() + 1);
    }

    /**
     * ¬озвращает значение игреков на всей области определени€
     * @return массив значений
     */
    public double[] getYArray()
    {
        return getYArray(getBegin(), getEnd() - getBegin() + 1);
    }

    /**
     * ƒоопредел€ет нулем за областью определени€
     * (не очень быстра€ реализаци€ - через промежуточный буфер)
     * @param x0 Ќачальный икс
     * @param N количество иксов
     * @return значени€ игреков на запрошенном диапазоне иксов x0 .. x0+N-1,
     * полага€ нулевыми значени€ за пределами области определени€.
     * (в принципе, обнул€ть не об€зательно, но так почему-то прин€то)
     * ≈сли запрошего отрицательное количество иксов, возвращаем пустой массив
     */
    public double[] getYArrayZeroPad(int x0, int N)
    {
        if (N <= 0) // на случай отрицательного N, возвращаем пустой массив
            return new double[0];

        int begin = getBegin();
        int end = getEnd();

        // если доопредел€ть не надо
        if (x0 >= begin && x0 + N - 1 <= end)
            return getYArray(x0, N);

        // если доопредел€ть надо
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
