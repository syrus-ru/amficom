/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.1 2005/01/27 08:41:14 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/01/27 08:41:14 $
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
		this.eventType = eventType;
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
