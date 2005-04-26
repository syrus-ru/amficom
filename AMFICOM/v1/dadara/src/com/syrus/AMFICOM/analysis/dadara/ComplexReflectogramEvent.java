/*
 * $Id: ComplexReflectogramEvent.java,v 1.12 2005/04/26 07:35:20 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: saa $
 * @version $Revision: 1.12 $, $Date: 2005/04/26 07:35:20 $
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

	public double getALet() { return aLet; }
	public double getMLoss() { return mLoss; }
	public double getLength() { return end - begin; }
	public double getAsympY0() { return asympY0; }
	public double getAsympY1() { return asympY1; }
	
	protected boolean isSplice()
	{
		return type == LOSS || type == GAIN;
	}

	// Метод сделан private, т.к. он создает объект не в окончательном виде.
	// Например, для сварки потом надо будет уточнить mloss.
	private ComplexReflectogramEvent(SimpleReflectogramEvent se, ModelTrace mt)
	{
		begin = se.getBegin();
		end = se.getEnd();
		type = se.getEventType();
		int N = end - begin + 1;
		double []yArr = mt.getYArray(begin, N);
		if (type == SimpleReflectogramEvent.LINEAR)
		{
			ModelFunction lin = ModelFunction.createLinearFrom0(yArr);
			asympY0 = lin.fun(begin);
			asympY1 = lin.fun(end);
			mLoss = asympY0 - asympY1;
			aLet = 0;
		}
		else
		{
			asympY0 = yArr[0];
			asympY1 = yArr[N - 1];
			mLoss = asympY0 - asympY1;
			aLet = ReflectogramMath.getArrayMax(yArr) - asympY0;
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
		return ret;
	}
}
