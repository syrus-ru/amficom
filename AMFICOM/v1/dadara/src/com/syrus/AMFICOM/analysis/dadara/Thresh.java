/*
 * $Id: Thresh.java,v 1.1 2005/02/08 11:46:28 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/02/08 11:46:28 $
 * @module
 */

public class Thresh
{
	// пол€ измен€ютс€ также через JNI-методы ModelFunction
	protected int eventId;
	protected int typeId; // 0: dA, 1: dL+dXL+dXR
	protected double[] values;
	protected final static boolean[] IS_KEY_UPPER = new boolean[] { true, true, false, false }; // используетс€ в native-коде
	protected int[] dxL; // null if not required
	protected int[] dxR; // null if not required
	protected int xMin;
	protected int xMax;
	private Thresh()
	{ // empty and very private
	}
	protected Thresh(int eventId, int subId, int xMin, int xMax)
	{
		this.eventId = eventId;
		this.typeId = subId;
		this.values = new double[] { 0.1, 0.2, -0.1, -0.2 }; // defaults -- XXX
		//this.values = new double[] { 0.0, 0.0, -0.0, -0.0 }; // defaults -- XXX
		if (subId != 0)
		{
			dxL = new int[] { 1, 2, 1, 2 };
			dxR = new int[] { 1, 2, 1, 2 };
		}
		else
		{
			dxL = null;
			dxR = null;
		}
		this.xMin = xMin;
		this.xMax = xMax;
	}
	public static Thresh readFromDIS(DataInputStream dis)
	throws IOException
	{
		Thresh ret = new Thresh();
		ret.eventId = dis.readInt();
		ret.typeId = dis.readByte();
		ret.values = new double[4];
		for (int k = 0; k < 4; k++)
			ret.values[k] = dis.readDouble();
		for (int k = 0; k < 4; k++)
			ret.dxL[k] = dis.readInt();
		for (int k = 0; k < 4; k++)
			ret.dxR[k] = dis.readInt();
		ret.xMin = dis.readInt();
		ret.xMax = dis.readInt();
		return ret;
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		dos.writeInt(eventId);
		dos.writeInt(typeId);
		for (int k = 0; k < 4; k++)
			dos.writeDouble(values[k]);
		for (int k = 0; k < 4; k++)
			dos.writeInt(dxL[k]);
		for (int k = 0; k < 4; k++)
			dos.writeInt(dxR[k]);
		dos.writeInt(xMin);
		dos.writeInt(xMax);
	}

	public static Thresh[] readArrayFromDIS(DataInputStream dis)
	throws IOException
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
