/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.8 2005/04/14 16:01:28 saa Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/04/14 16:01:28 $
 * @module
 */
public class SimpleReflectogramEventImpl
implements SimpleReflectogramEvent
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

    public SimpleReflectogramEventImpl(DataInputStream dis)
    throws IOException
    {
        this.begin = dis.readInt();
        this.end = dis.readInt();
        this.eventType = dis.readInt();
    }
/*
	public static SimpleReflectogramEventImpl createFromDIS(DataInputStream dis)
	throws IOException
	{
        return new SimpleReflectogramEventImpl(dis);
	}
*/
}
