/*
 * $Id: Thresh.java,v 1.5 2005/03/15 13:40:21 saa Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/03/15 13:40:21 $
 * @module
 */

// поля изменяются также через JNI-методы ModelFunction
public abstract class Thresh
{
	public final static boolean[] IS_KEY_UPPER = new boolean[] { true, true, false, false }; // используется в native-коде
	public final static boolean[] IS_KEY_HARD = new boolean[] { false, true, false, true };
	protected final static int[] CONJ_KEY = new int[] { 2, 3, 0, 1 }; // upper <-> lower - парный key для данного
	protected final static int[] LIMIT_KEY = new int[] { 1, 1, 3, 3 }; // key параметра, ограничивающим данный (self если ограничения нет)
	protected final static int[] FORCEMOVE_KEY = new int[] { 0, 0, 2, 2 }; // key параметра, который принудительно двигается вместе с данным (self если принуждения нет)

	protected int eventId0;
	protected int eventId1;
	protected int xMin; // внутри xMin..xMax р/г смещается равномерно, а вне - согласно dA/dL 
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
			// XXX: это вообще не очено здорово, что такие эксепшны нигде не ловятся 
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

	// при необходимости упорядочить все warn/soft параметры, предполагая что редактировался только key 
	protected void arrangeLimits(int key)
	{ // empty
	}
}
