/*
 * $Id: SimpleReflectogramEvent.java,v 1.3 2005/02/08 11:46:28 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/02/08 11:46:28 $
 * @module dadara
 * 
 * ������������ ������� ���������� � ������������������� ������� -
 * ������, ����� � ���.
 * �� ����� setter'��, ����� ��������� ���������� �������
 * getSimpleReflectogramEvent � MTM � ��.
 * (����� �� ������ ������� �������� �� ������ ������� Unmodifiable) 
 * ����������: �������� ������ ��������, ������ ���������� �������
 * ��������� � ������ �����������.
 */
public interface SimpleReflectogramEvent
{
	static final int RESERVED = 0;
	static final int LINEAR = 1;
	static final int SPLICE = 2;
	static final int CONNECTOR = 3;
	static final int NOTIDENTIFIED = 4;
	int getBegin();
	int getEnd();
	int getEventType();
}
