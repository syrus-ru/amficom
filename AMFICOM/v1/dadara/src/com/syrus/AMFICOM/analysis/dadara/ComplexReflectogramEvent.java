/*
 * $Id: ComplexReflectogramEvent.java,v 1.13 2005/04/29 09:57:53 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: saa $
 * @version $Revision: 1.13 $, $Date: 2005/04/29 09:57:53 $
 * @module dadara
 * 
 * Класс предназначен для хранения расширенной информации о
 * рефлектометрическом событии -
 * кроме стандартных {начало, конец, тип} хранятся также
 * рефлектометрические параметры - потери, отражение и пр.,
 * необходимые для отображения информации о событии пользователю.
 */
public class ComplexReflectogramEvent implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int type;

	private double aLet;
	private double mLoss;

	private double asympY0;
	private double asympY1;

	public int getBegin() { return begin; }
	public int getEnd() { return end; }
	public int getEventType() { return type; }

    public double getLength() { return end - begin; }

    /**
     * @return амплитуда всплеска, т.е. yMax - asympY0
     */
    public double getALet() { return aLet; }

    /**
     * @return потери на событии.
     * для лин. событий это (asympY0 - asympY1);
     * для остальных - то же, но скорректированное с учетом соседних
     * линейных событий. 
     */
	public double getMLoss() { return mLoss; }
    
    /**
     * @return
     * Для лин. события - ее линейная аппроксимация в начале события;
     * Для мертвой зоны - линейная экстраполяция ближайшего линейного события
     *   на начало мертвой зоны;
     * Для других событий - значение м.ф. в начале события.
     */
	public double getAsympY0() { return asympY0; }

    /**
     * @return
     * Для лин. события - ее линейная аппроксимация в конце события;
     * Для конца волокна - в принципе, не определено;
     * Для других событий - значение м.ф. в конце события.
     */
    public double getAsympY1() { return asympY1; }
	
    /**
     * @return type == LOSS || type == GAIN
     */
	protected boolean isSplice()
	{
		return type == LOSS || type == GAIN;
	}

    private void setALet(ModelTrace mt) {
        if (type == SimpleReflectogramEvent.LINEAR)
            aLet = 0;
        else {
            int N = end - begin + 1;
            double []yArr = mt.getYArray(begin, N);
            aLet = ReflectogramMath.getArrayMax(yArr) - asympY0;
        }
    }

	// Метод сделан private, т.к. он создает объект не в окончательном виде.
	// Например, для сварки потом надо будет уточнить mloss, а aLet - вообще
    // не инициализируется
	private ComplexReflectogramEvent(SimpleReflectogramEvent se, ModelTrace mt)
	{
		begin = se.getBegin();
		end = se.getEnd();
		type = se.getEventType();
		if (type == SimpleReflectogramEvent.LINEAR)
		{
            int N = end - begin + 1;
            double []yArr = mt.getYArray(begin, N);
			ModelFunction lin = ModelFunction.createLinearFrom0(yArr);
			asympY0 = lin.fun(begin);
			asympY1 = lin.fun(end);
			mLoss = asympY0 - asympY1;
		}
		else
		{
			asympY0 = mt.getY(begin);
			asympY1 = mt.getY(end);
			mLoss = asympY0 - asympY1;
		}
	}

	public static ComplexReflectogramEvent[] createEvents(SimpleReflectogramEvent[] se, ModelTrace mt)
	{
		ComplexReflectogramEvent[] ret = new ComplexReflectogramEvent[se.length];

		// создаем события
		for (int i = 0; i < ret.length; i++)
			ret[i] = new ComplexReflectogramEvent(se[i], mt);

		// корректируем mloss сварок и коннекторов (вычитаем средний наклон прямых)
		for (int i = 0; i < ret.length; i++)
		{
			if (ret[i].isSplice()
					|| ret[i].getEventType()
					== SimpleReflectogramEvent.CONNECTOR)
			{
				// берем средний (из одного или двух) наклон смежных линейных
				// событий, которые по длине больше этой сварки
				int linCount = 0;
				double linAtt = 0;
				if (i > 0 && ret[i - 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i - 1].mLoss / ret[i - 1].getLength();
				}
				if (i < ret.length - 1 && ret[i + 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i + 1].mLoss / ret[i + 1].getLength();
				}
				if (linCount > 0)
					linAtt /= linCount;
				ret[i].mLoss -= linAtt * ret[i].getLength();
			}
		}

        // корректируем asympY0 для м.з.
        if (se[0].getEventType() == SimpleReflectogramEvent.DEADZONE)
        {
            for (int j = 1; j < se.length; j++) {
                if (se[j].getEventType() == SimpleReflectogramEvent.LINEAR) {
                    double po;
                    int x1 = se[j].getBegin() + 1;
                    int x2 = se[j].getEnd() - 1;
                    if (x1 >= x2)
                    {
                        po = mt.getY(x1);
                    }
                    else
                    {
                        double y1 = mt.getY(x1);
                        double y2 = mt.getY(x2);
                        po = (x1 * y2 - x2 * y1) / (x1 - x2);
                    }
                    ret[0].asympY0 = po;
                    break;
                }
            }
        }

        // устанавливаем aLet
        for (int i = 0; i < ret.length; i++)
            ret[i].setALet(mt);

        return ret;
	}
}
