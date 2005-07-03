/*
 * $Id: SimpleReflectogramEvent.java,v 1.6 2005/05/01 06:12:58 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/05/01 06:12:58 $
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
	int RESERVED = 0;
	int LINEAR = 1;
	int LOSS = 5;
	int GAIN = 6;
	int CONNECTOR = 3; // any reflection other than deadzone or end-of-trace
	int DEADZONE = 7;
	int ENDOFTRACE = 8;
	int NOTIDENTIFIED = 4;
	int getBegin();
	int getEnd();
	int getEventType();
}
