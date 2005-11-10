/*
 * $Id: ThreshDX.java,v 1.23 2005/11/10 13:16:37 saa Exp $
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
 * @version $Revision: 1.23 $, $Date: 2005/11/10 13:16:37 $
 * @module
 */
public class ThreshDX extends Thresh
{
	// включает для этого DX-порога коррекцию нижних пороговых кривых
	// от А.Б., 26/09/2005
	private static final byte FLAG_AB_CORR = 0x1;

	private int[] dX; // accessed from JNI
	private boolean rise; // accessed from JNI
	private byte flags; // accessed from JNI

	protected ThreshDX()
	{ // do nothing -- for DIS reading
	}

	// shallow copy-constructor
	protected ThreshDX(ThreshDX that)
	{
		super(that);
		this.dX = that.dX;
		this.rise = that.rise;
		this.flags = that.flags;
	}

	/**
	 * Создает DX-порог заданного уровня по текущему порогу.
	 * В созданном пороге SOFT и HARD пороги совпадают
	 * (эта избыточность - издержки проектирования).
	 * @param level уровень, от 0.0 до 1.0
	 * @return созданный порог запрошенного уровня
	 */
	public ThreshDX makeWeightedThresholds(double level)
	{
		ThreshDX ret = new ThreshDX(this);
		ret.dX = new int[4];
		double w1 = 1.0 - level;
		double w2 = level;
		ret.dX[SOFT_UP] = (int)Math.round(
				(this.dX[SOFT_UP] * w1 + this.dX[HARD_UP] * w2));
		ret.dX[SOFT_DOWN] = (int)Math.round(
				(this.dX[SOFT_DOWN] * w1 + this.dX[HARD_DOWN] * w2));
		ret.dX[HARD_UP] = ret.dX[SOFT_UP];
		ret.dX[HARD_DOWN] = ret.dX[SOFT_DOWN];
		return ret;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ThreshDX ret = (ThreshDX)super.clone();
		ret.dX = this.dX.clone();
		return ret;
	}

	private int goodSign(int key)
	{
		return this.rise ^ IS_KEY_UPPER[key] ? 1 : -1;
	}

	protected ThreshDX(int eventId, int xMin, int xMax, boolean rise, boolean useABCorr)
	{
		super(eventId, eventId, xMin, xMax);
		int dx = rise ? -1 : +1;
		this.rise = rise;
		this.dX = new int[] { dx, 2 * dx, -dx, -2 * dx };
		this.flags = 0;
		if (useABCorr) {
			this.flags |= FLAG_AB_CORR;
		}
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		this.rise = dis.readBoolean();
		this.flags = dis.readByte();
		this.dX = new int[4];
		for (int k = 0; k < 4; k++)
			this.dX[k] = dis.readInt();
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		dos.writeBoolean(this.rise);
		dos.writeByte(this.flags);
		for (int k = 0; k < 4; k++)
			dos.writeInt(this.dX[k]);
	}
	protected boolean isRise()
	{
		return this.rise;
	}
	protected int getDX(int key)
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
	protected void setDX(int key, double val) // XXX: getter returnes int, setter gets double 
	{
		this.dX[key] = (int )val;
		correctDX(key);
		limit(key);
	}

	@Override
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
	@Override
	protected void roundUp(int key)
	{ // empty: we do not anything
	}

	/**
	 * @return a threshold with same begin/end/type, but zero DX
	 */
	public ThreshDX makeZeroedCopy()
	{
		try {
			ThreshDX ret = (ThreshDX)this.clone();
			for (int i = 0; i < ret.dX.length; i++)
				ret.dX[i] = 0;
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError (e.toString());
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.flags;
		result = 37 * result + (this.rise ? 1 : 0);
		for (int i = 0; i < this.dX.length; i++) {
			result = 37 * result + this.dX[i];
		}
		return result;
	}
}
