/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.4 2005/03/03 15:10:55 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.awt.Event;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/03/03 15:10:55 $
 * @module
 */
public class SimpleReflectogramEventImpl implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int eventType;

	private SimpleReflectogramEventImpl()
	{ // empty and very private
	}

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

	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		dos.writeInt(begin);
		dos.writeInt(end);
		dos.writeInt(eventType);
	}

	public static SimpleReflectogramEventImpl createFromDIS(DataInputStream dis)
	throws IOException
	{
		SimpleReflectogramEventImpl ret = new SimpleReflectogramEventImpl();
		ret.begin = dis.readInt();
		ret.end = dis.readInt();
		ret.eventType = dis.readInt();
		return ret;
	}

}
