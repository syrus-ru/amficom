/*
 * $Id: ModelTrace.java,v 1.12 2005/07/22 06:39:51 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * ������� ������ {@link ModelTraceRange}:
 * ������, ������������ �� ������� �� 0 �� getLength()-1.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.12 $, $Date: 2005/07/22 06:39:51 $
 * @module
 */
public abstract class ModelTrace extends ModelTraceRange
{
	/**
	 * ���������� ����� ������ � ������
	 * @return �����
	 */
	public abstract int getLength();

	@Override
	final public int getBegin() {
		return 0;
	}

	@Override
	final public int getEnd() {
		return getLength() - 1;
	}

	/**
	 * ���������� �������� ������� �� ���� ����� �������������� 
	 * @return ������������� ������ � ���� �������
	 */
	@Override
	public double[] getYArray()
	{
		return getYArray(0, getLength());
	}
}
