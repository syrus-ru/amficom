/*-
 * $Id: ModelTraceRange.java,v 1.3 2005/05/05 11:11:46 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Описывает участок кривой.
 * Природа кривой определяется реализацией класса.
 * Поведение объекта при запросе значения за пределами
 * диапазона [{@link #getBegin()}, {@link #getEnd()}]
 * не определено.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.3 $, $Date: 2005/05/05 11:11:46 $
 * @module
 */
public abstract class ModelTraceRange {
    public abstract int getBegin();
    public abstract int getEnd();

    /**
     * Получить значение кривой при данном иксе 
     * @param x икс
     * @return игрек
     */
    public abstract double getY(int x);

    /**
     * Возвращает значения игреков на указанном диапазоне иксов x0 .. x0+N-1.
     * результат за пределами области определения рефлектограммы (x < 0 или x >= length)
     * не определен.
     * <p>
     * Рекомендуется перегружать этот метод в конкретных реализациях.
     * @param x0 Начальный икс
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
     * Возвращает значения игреков на диапазоне от начала до конца включительно
     * данного события.
     * @param ev диапазон
     * @return массив значений Y
     */
    public double[] getYRE(SimpleReflectogramEvent ev)
    {
        return getYArray(ev.getBegin(), ev.getEnd() - ev.getBegin() + 1);
    }

    /**
     * Возвращает значение игреков на всей области определения
     * @return массив значений
     */
    public double[] getYArray()
    {
        return getYArray(getBegin(), getEnd() - getBegin() + 1);
    }
}
