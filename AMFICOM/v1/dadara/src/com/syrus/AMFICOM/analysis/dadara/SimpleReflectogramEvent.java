/*
 * $Id: SimpleReflectogramEvent.java,v 1.5 2005/04/26 07:35:21 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/04/26 07:35:21 $
 * @module dadara
 * 
 * Обеспечивает базовую информацию о рефлектометрическом событии -
 * начало, конец и тип.
 * Не имеет setter'ов, чтобы упростить реализацию методов
 * getSimpleReflectogramEvent в MTM и пр.
 * (иначе во многих случаях пришлось бы делать обертку Unmodifiable) 
 * Примечание: Согласно модели АМФИКОМа, начало следующего события
 * совпадает с концом предыдущего.
 */
public interface SimpleReflectogramEvent
{
	static final int RESERVED = 0;
	static final int LINEAR = 1;
	static final int LOSS = 5;
	static final int GAIN = 6;
	static final int CONNECTOR = 3; // any reflection other than deadzone or end-of-trace
	static final int DEADZONE = 7;
	static final int ENDOFTRACE = 8;
	static final int NOTIDENTIFIED = 4;
	int getBegin();
	int getEnd();
	int getEventType();
}
