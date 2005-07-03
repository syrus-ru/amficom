package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class ReflectoEventContainer
{
	public ShortReflectogramEvent []sre;
	public ReflectogramEvent []re;
	public BellcoreStructure bs;
	public long date;


	public ReflectoEventContainer(ShortReflectogramEvent []sre,
																ReflectogramEvent []re, BellcoreStructure bs,
																long date)
	{
		this.bs = bs;
		this.sre = sre;
		this.re = re;
		this.date = date;
	}
}