/*
 * $Id: ThreshDX.java,v 1.13 2005/03/30 12:50:02 saa Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/03/30 12:50:02 $
 * @module
 */
public class ThreshDX extends Thresh
{
	private int[] dX; // accessed from JNI
	private boolean rise; // accessed from JNI

	protected ThreshDX()
	{ // do nothing
	}
	
	private int goodSign(int key)
	{
		return this.rise ^ IS_KEY_UPPER[key] ? 1 : -1;
	}

	protected ThreshDX(int eventId, int xMin, int xMax, boolean rise)
	{
		super(eventId, eventId, xMin, xMax);
		int dx = rise ? -1 : +1;
		this.rise = rise;
		this.dX = new int[] { dx, 2 * dx, -dx, -2 * dx };
	}

	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		for (int k = 0; k < 4; k++)
			this.dX[k] = dis.readInt();
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeInt(this.dX[k]);
	}
	protected boolean isRise()
	{
		return this.rise;
	}
	protected double getDX(int key)
	{
		return this.dX[key];
	}
	private void correctDX(int key)
	{
		if (this.dX[key] * goodSign(key) < 0)
			this.dX[key] = 0;
	}
	private void limit(int key) // impose limiting according to LIMIT_KEY
	{
		int compareSign = goodSign(key) * (IS_KEY_HARD[key] ? 1 : -1);
		if (this.dX[key] * compareSign < this.dX[LIMIT_KEY[key]] * compareSign)
			this.dX[key] = this.dX[LIMIT_KEY[key]];
	}
	protected void setDX(int key, double val)
	{
		this.dX[key] = (int )val;
		correctDX(key);
		limit(key);
	}

	protected void arrangeLimits(int key)
	{
		correctDX(key);
		int compareSign = goodSign(key) * (IS_KEY_HARD[key] ? 1 : -1);
		if (this.dX[key] * compareSign < this.dX[FORCEMOVE_KEY[key]] * compareSign)
			this.dX[FORCEMOVE_KEY[key]] = this.dX[key];
	}

	public void changeAllBy(int delta)
	{
		for (int key = 0; key < 4; key++)
		{
			this.dX[key] += goodSign(key) * delta * (IS_KEY_HARD[key] ? 2 : 1);
			correctDX(key);
		}
		for (int key = 0; key < 4; key++)
			limit(key);
	}
	protected void roundUp(int key)
	{ // empty: we do not anything
	}
}
