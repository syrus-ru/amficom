/*
 * $Id: ModelTraceImplComplexRE_unused.java,v 1.1 2005/01/25 14:16:50 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/01/25 14:16:50 $
 * @module
 */
public class ModelTraceImplComplexRE_unused extends ModelTrace
{
	private ComplexReflectogramEvent[] re;

	public ModelTraceImplComplexRE_unused(ComplexReflectogramEvent[] re)
	{
		this.re = re;
	}

	public int getLength()
	{
		if (re == null || re.length == 0)
			return 0;
		else
			return re[re.length - 1].getEnd() + 1;
	}

	public double getY(int x)
	{
		throw new UnsupportedOperationException();
		/*int event = getEventByCoord(x);
		if (event >= 0)
			return re[event].getY(x);
		else
			return 0.0; // XXX*/
	}

	// XXX: slow
	// XXX: inconvenient
	private int getEventByCoord(int x) // may be -1
	{
		int ret = -1;
		for (int i = 0; i < re.length; i++)
		{
			if (x >= re[i].getBegin() && x <= re[i].getEnd())
				ret = i;
		}
		return ret;
	}

}
