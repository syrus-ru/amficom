/*
 * $Id: Thresh.java,v 1.3 2005/02/21 13:39:33 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/02/21 13:39:33 $
 * @module
 */

// пол€ измен€ютс€ также через JNI-методы ModelFunction
public abstract class Thresh
{
	protected final static boolean[] IS_KEY_UPPER = new boolean[] { true, true, false, false }; // используетс€ в native-коде
	protected final static int[] CONJ_KEY = new int[] { 2, 3, 0, 1 };

	protected int eventId0;
	protected int eventId1;
	protected int xMin; // внутри xMin..xMax р/г смещаетс€ равномерно, а вне - согласно dA/dL 
	protected int xMax;

	protected Thresh()
	{
	}
	protected Thresh(int eventId0, int eventId1, int xMin, int xMax)
	{
		this.eventId0 = eventId0;
		this.eventId1 = eventId1;
		this.xMin = xMin;
		this.xMax = xMax;
	}

	public static Thresh readFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException
	{
		Thresh ret = null;
		switch (dis.readByte())
		{
		case 'X':
			ret = new ThreshDX();
			break;
		case 'Y':
			ret = new ThreshDY();
			break;
		default:
			// XXX: это вообще не очено здорово, что такие эксепшны нигде не лов€тс€ 
			throw new SignatureMismatchException("Thresh: readFromDIS: Unrecognized format");
		}
		ret.eventId0 = dis.readInt();
		ret.eventId1 = dis.readInt();
		ret.xMin = dis.readInt();
		ret.xMax = dis.readInt();
		ret.readSpecificFromDIS(dis);
		return ret;
	}

	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{ // empty
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{ // empty
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		if (this instanceof ThreshDX)
			dos.writeByte('X');
		else if (this instanceof ThreshDY)
			dos.writeByte('Y');
		else
			throw new UnsupportedOperationException("Unknown Thresh implementation");
		dos.writeInt(eventId0);
		dos.writeInt(eventId1);
		dos.writeInt(xMin);
		dos.writeInt(xMax);
		writeSpecificToDOS(dos);
	}

	public static Thresh[] readArrayFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException
	{
		int len = dis.readInt();
		Thresh[] ret = new Thresh[len];
		for (int i = 0; i < len; i++)
			ret[i] = readFromDIS(dis);
		return ret;
	}
	public static void writeArrayToDOS(Thresh[] th, DataOutputStream dos)
	throws IOException
	{
		dos.writeInt(th.length);
		for (int i = 0; i < th.length; i++)
		{
			th[i].writeToDOS(dos);
		}
	}

}
