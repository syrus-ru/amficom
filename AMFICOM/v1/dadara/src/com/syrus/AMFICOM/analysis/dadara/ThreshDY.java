/*
 * $Id: ThreshDY.java,v 1.14 2005/04/30 07:33:28 saa Exp $
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
 * @version $Revision: 1.14 $, $Date: 2005/04/30 07:33:28 $
 * @module
 */
public class ThreshDY extends Thresh
{
	private static final double VALUE_FRACTION = 1000; // 1/1000 dB precision
	private boolean typeL; // 0: dA, 1: dL
	private double[] values; // dA or dL values

	protected ThreshDY()
	{ // do nothing
	}

	protected ThreshDY(int eventId, boolean typeL, int xMin, int xMax)
	{
		super(eventId, eventId, xMin, xMax);
		this.typeL = typeL;
		//this.values = new double[] { 0.1, 0.2, -0.1, -0.2 }; // defaults -- XXX
		this.values = new double[] { 0.0, 0.0, -0.0, -0.0 }; // FIXME: put default values of thresholds back
	}

    public Object clone() throws CloneNotSupportedException
    {
        ThreshDY ret = (ThreshDY)super.clone();
        ret.values = (double[])this.values.clone();
        return ret;
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
	private void snapAndLimit(int key) // привязать к сетке и скорректировать при неправильном знаке  
	{
		if (values[key] * (IS_KEY_UPPER[key] ? 1 : -1) < 0)
			values[key] = 0;
		if (VALUE_FRACTION > 0)
			values[key] = Math.rint(values[key] * VALUE_FRACTION) / VALUE_FRACTION;
	}
	private void interLimit(int key) // наложить ограничение согласно LIMIT_KEY
	{
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (values[key] * compareSign < values[LIMIT_KEY[key]] * compareSign)
			values[key] = values[LIMIT_KEY[key]];
	}
	protected void setDY(int key, double val)
	{
		values[key] = val;
		snapAndLimit(key);
		interLimit(key);
	}
	protected void arrangeLimits(int key)
	{
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (values[key] * compareSign < values[FORCEMOVE_KEY[key]] * compareSign)
			values[FORCEMOVE_KEY[key]] = values[key];
	}
	public void changeAllBy(double delta)
	{
		for (int key = 0; key < 4; key++)
		{
			values[key] += (IS_KEY_UPPER[key] ? delta : -delta) * (IS_KEY_HARD[key] ? 2 : 1);
			snapAndLimit(key);
		}
		for (int key = 0; key < 4; key++)
			interLimit(key);
	}
	protected void roundUp(int key)
	{
		int sign = IS_KEY_UPPER[key] ? 1 : -1;
		if (VALUE_FRACTION > 0)
			values[key] = Math.ceil(values[key] * VALUE_FRACTION * sign) / VALUE_FRACTION / sign;
	}
}
