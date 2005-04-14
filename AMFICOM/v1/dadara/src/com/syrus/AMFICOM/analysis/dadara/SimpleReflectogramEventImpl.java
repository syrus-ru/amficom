/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.7 2005/04/14 12:03:17 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/04/14 12:03:17 $
 * @module
 */
public class SimpleReflectogramEventImpl implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int eventType;

	protected SimpleReflectogramEventImpl()
	{ // for native use
	}

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
