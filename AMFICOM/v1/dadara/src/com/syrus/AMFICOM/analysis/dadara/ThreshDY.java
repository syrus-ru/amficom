/*
 * $Id: ThreshDY.java,v 1.6 2005/03/14 10:15:43 saa Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/03/14 10:15:43 $
 * @module
 */
public class ThreshDY extends Thresh
{
	private static final double VALUE_FRACTION = 1000; // 1/1000 dB precision
	private boolean typeL; // 0: dA, 1: dL
	private double[] values; // dA or dL values

	protected ThreshDY()
	{
	}

	protected ThreshDY(int eventId, boolean typeL, int xMin, int xMax)
	{
		super(eventId, eventId, xMin, xMax);
		this.typeL = typeL;
		this.values = new double[] { 0.1, 0.2, -0.1, -0.2 }; // defaults -- XXX
	}
	
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		values = new double[4];
		for (int k = 0; k < 4; k++)
			values[k] = dis.readDouble();
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeDouble(values[k]);
	}

	protected double getDY(int n)
	{
		return values[n];
	}
	protected boolean getTypeL()
	{
		return typeL;
	}
	protected void setDY(int key, double val)
	{
		if (VALUE_FRACTION > 0)
			val = Math.rint(val * VALUE_FRACTION) / VALUE_FRACTION;
		if (val * (IS_KEY_UPPER[key] ? 1 : -1) < 0)
			val = 0;
		values[key] = val;
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (values[key] * compareSign < values[LIMIT_KEY[key]] * compareSign)
			values[key] = values[LIMIT_KEY[key]];
	}
	protected void arrangeLimits(int key)
	{
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (values[key] * compareSign < values[FORCEMOVE_KEY[key]] * compareSign)
			values[FORCEMOVE_KEY[key]] = values[key];
	}
}
