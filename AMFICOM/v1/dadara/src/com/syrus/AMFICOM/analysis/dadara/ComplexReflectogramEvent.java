/*
 * $Id: ComplexReflectogramEvent.java,v 1.3 2005/02/08 11:46:27 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/02/08 11:46:27 $
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
	public double getWidth0() { return end - begin; }
	public double getAsympY0() { return asympY0; }
	public double getAsympY1() { return asympY1; }

	//private ModelFunction mf; // FIXME

	/*double getY(int x)
	{
		return mf.fun(x);
	}*/
	
	public ComplexReflectogramEvent(ReflectogramEvent re)
	{
		begin = re.getBegin();
		end = re.getEnd();
		type = re.getEventType();
		//mf = re.getMFClone();
		aLet = re.getALet();
		mLoss = re.getMLoss();
		asympY0 = re.getAsympY0();
		asympY1 = re.getAsympY1();
		// XXX: теряем LR-связь
	}
}
