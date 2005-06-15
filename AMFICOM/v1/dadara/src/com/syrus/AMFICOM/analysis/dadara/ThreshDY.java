/*
 * $Id: ThreshDY.java,v 1.17 2005/06/15 15:00:01 saa Exp $
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
 * @version $Revision: 1.17 $, $Date: 2005/06/15 15:00:01 $
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
	// shallow copy-constructor
	protected ThreshDY(ThreshDY that)
	{
		super(that);
		this.typeL = that.typeL;
		this.values = that.values;
	}
	protected ThreshDY(int eventId, boolean typeL, int xMin, int xMax)
	{
		super(eventId, eventId, xMin, xMax);
		this.typeL = typeL;
		//this.values = new double[] { 0.1, 0.2, -0.1, -0.2 }; // defaults?
		this.values = new double[] { 0.0, 0.0, -0.0, -0.0 }; // FIXME: default values of thresholds
	}

	/**
	 * Создает DY-порог заданного уровня по текущему порогу.
	 * В созданном пороге SOFT и HARD пороги совпадают
	 * (эта избыточность - издержки проектирования).
	 * @param level уровень, от 0.0 до 1.0
	 * @return созданный порог запрошенного уровня
	 */
	public ThreshDY makeWeightedThresholds(double level)
	{
		ThreshDY ret = new ThreshDY(this);
		ret.values = new double[4];
		double w1 = 1.0 - level;
		double w2 = level;
		ret.values[SOFT_UP] =
			this.values[SOFT_UP] * w1 + this.values[HARD_UP] * w2;
		ret.values[SOFT_DOWN] = 
			this.values[SOFT_DOWN] * w1 + this.values[HARD_DOWN] * w2;
		ret.snapAndLimit(SOFT_UP);
		ret.snapAndLimit(SOFT_DOWN);
		ret.values[HARD_UP] = ret.values[SOFT_UP];
		ret.values[HARD_DOWN] = ret.values[SOFT_DOWN];
		return ret;
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

    /**
     * @return a threshold with same begin/end/type, but zero DY
     */
    public ThreshDY makeZeroedCopy()
    {
        try {
            ThreshDY ret = (ThreshDY)this.clone();
            for (int i = 0; i < ret.values.length; i++)
                ret.values[i] = 0;
            return ret;
        } catch (CloneNotSupportedException e) {
            throw new InternalError (e.toString());
        }
    }
}
