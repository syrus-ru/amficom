/*
 * $Id: ThreshDX.java,v 1.10 2005/03/21 18:06:53 saa Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/03/21 18:06:53 $
 * @module
 */
public class ThreshDX extends Thresh
{
	private int[] dX;
	private boolean isRise;

	protected ThreshDX()
	{ // do nothing
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
	private void correctDX(int key)
	{
		if (dX[key] * goodSign(key) < 0)
			dX[key] = 0;
	}
	private void limit(int key) // impose limiting according to LIMIT_KEY
	{
		int compareSign = goodSign(key) * (IS_KEY_HARD[key] ? 1 : -1);
		if (dX[key] * compareSign < dX[LIMIT_KEY[key]] * compareSign)
			dX[key] = dX[LIMIT_KEY[key]];
	}
	protected void setDX(int key, double val)
	{
		dX[key] = (int )val;
		correctDX(key);
		limit(key);
	}

	protected void arrangeLimits(int key)
	{
		correctDX(key);
		int compareSign = goodSign(key) * (IS_KEY_HARD[key] ? 1 : -1);
		if (dX[key] * compareSign < dX[FORCEMOVE_KEY[key]] * compareSign)
			dX[FORCEMOVE_KEY[key]] = dX[key];
	}

	public void changeAllBy(int delta)
	{
		for (int key = 0; key < 4; key++)
		{
			dX[key] += goodSign(key) * delta * (IS_KEY_HARD[key] ? 2 : 1);
			correctDX(key);
		}
		for (int key = 0; key < 4; key++)
			limit(key);
	}
	protected void roundUp(int key)
	{ // empty: we do not anything
	}
}
