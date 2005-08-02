/*
 * $Id: ComplexReflectogramEvent.java,v 1.18 2005/08/02 19:36:33 arseniy Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.18 $, $Date: 2005/08/02 19:36:33 $
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

	public int getBegin() { return this.begin; }
	public int getEnd() { return this.end; }
	public int getEventType() { return this.type; }

	public double getLength() { return this.end - this.begin; }

	/**
	 * @return амплитуда всплеска, т.е. yMax - asympY0
	 */
	public double getALet() { return this.aLet; }

	/**
	 * @return потери на событии.
	 * для лин. событий это (asympY0 - asympY1);
	 * для м.з. и конца волокна - не определен,
	 * для остальных - то же, но скорректированное с учетом соседних
	 * линейных событий. 
	 */
	public double getMLoss() { return hasMLoss() ? this.mLoss : 0; }

	/**
	 * @return определен ли параметр mLoss
	 */
	public boolean hasMLoss() {
		return getEventType() != SimpleReflectogramEvent.DEADZONE
			&& getEventType() != SimpleReflectogramEvent.ENDOFTRACE;
	}

	/**
	 * @return
	 * Для лин. события - ее линейная аппроксимация в начале события;
	 * Для мертвой зоны - линейная экстраполяция ближайшего линейного события
	 *   на начало мертвой зоны;
	 * Для других событий - значение м.ф. в начале события.
	 */
	public double getAsympY0() { return this.asympY0; }

	/**
	 * @return
	 * Для лин. события - ее линейная аппроксимация в конце события;
	 * Для конца волокна - в принципе, не определено;
	 * Для других событий - значение м.ф. в конце события.
	 */
	public double getAsympY1() { return this.asympY1; }
	
	/**
	 * @return type == LOSS || type == GAIN
	 */
	protected boolean isSplice()
	{
		return this.type == LOSS || this.type == GAIN;
	}

	private void setALet(ModelTrace mt) {
		if (this.type == SimpleReflectogramEvent.LINEAR)
			this.aLet = 0;
		else {
			final int N = this.end - this.begin + 1;
			double []yArr = mt.getYArray(this.begin, N);
			this.aLet = ReflectogramMath.getArrayMax(yArr) - this.asympY0;
		}
	}

	// Метод сделан private, т.к. он создает объект не в окончательном виде.
	// Например, для сварки потом надо будет уточнить mloss, а aLet - вообще
	// не инициализируется
	private ComplexReflectogramEvent(SimpleReflectogramEvent se, ModelTrace mt)
	{
		this.begin = se.getBegin();
		this.end = se.getEnd();
		this.type = se.getEventType();
		if (this.type == SimpleReflectogramEvent.LINEAR)
		{
			final int N = this.end - this.begin + 1;
			double []yArr = mt.getYArray(this.begin, N);
			ModelFunction lin = ModelFunction.createLinearFrom0(yArr);
			this.asympY0 = lin.fun(this.begin);
			this.asympY1 = lin.fun(this.end);
			this.mLoss = this.asympY0 - this.asympY1;
		}
		else
		{
			this.asympY0 = mt.getY(this.begin);
			this.asympY1 = mt.getY(this.end);
			this.mLoss = this.asympY0 - this.asympY1; // для сварок будет уточнено
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

		// корректируем asympY0 и mLoss для м.з. и конца волокна
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
			for (int i = 0; i < ret.length; i++)
				if (!ret[i].hasMLoss())
					ret[i].mLoss = 0;
		}

		// устанавливаем aLet
		for (int i = 0; i < ret.length; i++)
			ret[i].setALet(mt);

		return ret;
	}
}
