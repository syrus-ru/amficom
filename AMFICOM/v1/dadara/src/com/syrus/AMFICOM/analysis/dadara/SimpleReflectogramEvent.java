/*
 * $Id: SimpleReflectogramEvent.java,v 1.1 2005/01/25 14:16:50 saa Exp $
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
	void setBegin(int begin);
	void setEnd(int end);
	void setEventType(int type);
}
