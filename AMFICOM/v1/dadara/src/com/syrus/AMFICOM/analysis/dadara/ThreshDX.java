/*
 * $Id: ThreshDX.java,v 1.4 2005/03/09 11:30:01 saa Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/03/09 11:30:01 $
 * @module
 */
public class ThreshDX extends Thresh
{
	private int[] dX;
	private boolean isRise;

	protected ThreshDX()
	{
	}
	
	private int goodSign(int key)
	{
		return isRise ^ IS_KEY_UPPER[key] ? 1 : -1;
	}

	protected ThreshDX(int eventId, int xMin, int xMax, boolean isRise)
	{
		super(eventId, eventId, xMin, xMax);
		int dx = isRise ? -1 : +1;
		this.isRise = isRise;
		dX = new int[] { dx, 2 * dx, -dx, -2 * dx };
	}

	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dX[k] = dis.readInt();
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeInt(dX[k]);
	}
	protected boolean getRise()
	{
		return isRise;
	}
	protected double getDX(int key)
	{
		return dX[key];
	}
	protected void setDX(int key, double val)
	{
		dX[key] = (int )val;
		if (dX[key] * goodSign(key) < 0)
			dX[key] = 0;
	}
}
