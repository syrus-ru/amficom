package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ShortReflectogramEvent;
import com.syrus.io.BellcoreStructure;

public class ReflectoEventContainer
{
	public String id;
	public ShortReflectogramEvent []sre;
	public ReflectogramEvent []re;
	public BellcoreStructure bs;
	public long date;


	public ReflectoEventContainer(String id, ShortReflectogramEvent []sre,
																ReflectogramEvent []re, BellcoreStructure bs,
																long date)
	{
		this.id = id;
		this.bs = bs;
		this.sre = sre;
		this.re = re;
		this.date = date;
	}
}