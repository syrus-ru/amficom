/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.2 2005/02/21 15:19:57 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/02/21 15:19:57 $
 * @module
 */
public class SimpleReflectogramEventImpl implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int eventType;

	public SimpleReflectogramEventImpl(int begin, int end, int eventType)
	{
		this.begin = begin;
		this.end = end;
		this.eventType = eventType; // FIXME: надо отличать типы LOSS и GAIN !
	}

	public int getBegin()
	{
		return begin;
	}

	public int getEnd()
	{
		return end;
	}

	public int getEventType()
	{
		return eventType;
	}

}
