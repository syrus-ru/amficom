/*
 * $Id: SimpleReflectogramEvent.java,v 1.2 2005/01/26 14:59:25 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/01/26 14:59:25 $
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
}
