/*
 * $Id: ModelTrace.java,v 1.10 2005/06/07 14:03:50 saa Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/06/07 14:03:50 $
 * @module
 */
public abstract class ModelTrace extends ModelTraceRange
{
	/**
	 * Возвращает длину кривой в точках
	 * @return длина
	 */
	public abstract int getLength();

    final public int getBegin() {
        return 0;
    }

    final public int getEnd() {
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
}
