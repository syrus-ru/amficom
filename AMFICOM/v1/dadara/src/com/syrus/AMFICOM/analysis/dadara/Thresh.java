/*
 * $Id: Thresh.java,v 1.15 2005/05/06 11:46:21 saa Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/05/06 11:46:21 $
 * @module
 */

// поля изменяются также через JNI-методы ModelFunction
public abstract class Thresh
implements Cloneable
{
	protected static final boolean[] IS_KEY_UPPER = new boolean[] { true, true, false, false };
	protected static final boolean[] IS_KEY_HARD = new boolean[] { false, true, false, true };
    protected static final int[] CONJ_KEY = new int[] { 2, 3, 0, 1 }; // upper <-> lower - парный key для данного
//    protected static final int[] LIMIT_KEY = new int[] { 1, 1, 3, 3 }; // key параметра, ограничивающим данный (self если ограничения нет)
//    protected static final int[] FORCEMOVE_KEY = new int[] { 0, 0, 2, 2 }; // key параметра, который принудительно двигается вместе с данным (self если принуждения нет)
    protected static final int[] LIMIT_KEY = new int[] { 0, 1, 2, 3 }; // key параметра, ограничивающим данный (self если ограничения нет)
    protected static final int[] FORCEMOVE_KEY = new int[] { 1, 0, 3, 2 }; // key параметра, который принудительно двигается вместе с данным (self если принуждения нет)

    public static final int SOFT_UP = 0;
	public static final int HARD_UP = 1;
	public static final int SOFT_DOWN = 2;
	public static final int HARD_DOWN = 3;

	protected int eventId0;
	protected int eventId1;
	protected int xMin; // границы доминирования данного порога. При DY - внутри xMin..xMax р/г смещается равномерно, а вне - согласно dA/dL 
	protected int xMax;

	protected Thresh()
	{ // do nothing
	}
	protected Thresh(int eventId0, int eventId1, int xMin, int xMax)
	{
		this.eventId0 = eventId0;
		this.eventId1 = eventId1;
		this.xMin = xMin;
		this.xMax = xMax;
	}

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
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
			throw new SignatureMismatchException("Thresh: readFromDIS: Unrecognized format");
		}
		ret.eventId0 = dis.readInt();
		ret.eventId1 = dis.readInt();
		ret.xMin = dis.readInt();
		ret.xMax = dis.readInt();
		ret.readSpecificFromDIS(dis);
		return ret;
	}

	protected abstract void readSpecificFromDIS(DataInputStream dis)
    throws IOException;

    protected abstract void writeSpecificToDOS(DataOutputStream dos)
    throws IOException;

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

	/**
	 * Определяет, относится ли данный порог к данному событию.
	 * Один порог может относиться к нескольким событиям,
	 * как и несколько порогов - к одному событию.
	 * @param nEvent номер события
	 */
	public boolean isRelevantToNEvent(int nEvent)
	{
		return eventId0 <= nEvent && eventId1 >= nEvent;
	}

	// при необходимости упорядочить все warn/soft параметры, предполагая что редактировался только key 
	protected abstract void arrangeLimits(int key);

	// увеличить порог до совпадения с сеткой
	// используется в native-коде
	protected abstract void roundUp(int key);

    public static boolean isKeyUpper(int key) {
        return IS_KEY_UPPER[key];
    }

    public static boolean isKeyHard(int key) {
        return IS_KEY_HARD[key];
    }
}
