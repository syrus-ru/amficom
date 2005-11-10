/*
 * $Id: ThreshDY.java,v 1.23 2005/11/10 13:16:37 saa Exp $
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
public class ThreshDY extends Thresh {
	private static final double VALUE_FRACTION = 1000; // 1/1000 dB precision

	/**
	 * “ип DY-порога.
	 * Ўаблон typesafe enum (Java1.5 здесь пока не используетс€).
	 * ¬ реализации {@link ThreshDY}, дл€ удобства native-части,
	 * используетс€ не этот enum, а byte {@link #type}
	 */
	public static class Type {
		private byte code;
		private Type(byte code) {
			this.code = code;
		}

		public byte getCode() {
			return this.code;
		}
		//  оды 0,1,2 используютс€ непосредственно в native-коде
		public static final Type dA = new Type((byte)0);
		public static final Type dL = new Type((byte)1);
		public static final Type nI = new Type((byte)2);

		private static final Type[] VALUES = { dA, dL, nI };

		static Type byCode(int code) {
			return VALUES[code];
		}
	}

	private byte type;
	private double[] values; // dA or dL values

	protected ThreshDY()
	{ // do nothing
	}
	// shallow copy-constructor
	protected ThreshDY(ThreshDY that)
	{
		super(that);
		this.type = that.type;
		this.values = that.values;
	}

	/**
	 * —оздает DY-порог, изначально нулевой.
	 * ƒл€ работы пороги надо будет расширить, см., напр.,
	 * {@link ModelTraceManager#updateThreshToContain}
	 * @param eventId номер событи€, к которому "относитс€" этот порог
	 * @param type тип порога - Type.dL, Type.dA или Type.nI
	 * @param xMin мин. X-координата доминирование порога
	 * @param xMax макс. X-координата доминирование порога
	 */
	protected ThreshDY(int eventId, Type type, int xMin, int xMax)
	{
		super(eventId, eventId, xMin, xMax);
		this.type = type.getCode();
		this.values = new double[] { 0.0, 0.0, -0.0, -0.0 };
	}

	/**
	 * —оздает DY-порог заданного уровн€ по текущему порогу.
	 * ¬ созданном пороге SOFT и HARD пороги совпадают
	 * (эта избыточность - издержки проектировани€).
	 * @param level уровень, от 0.0 до 1.0
	 * @return созданный порог запрошенного уровн€
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

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ThreshDY ret = (ThreshDY)super.clone();
		ret.values = this.values.clone();
		return ret;
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		this.values = new double[4];
		for (int k = 0; k < 4; k++)
			this.values[k] = dis.readDouble();
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeDouble(this.values[k]);
	}

	protected double getDY(int n)
	{
		return this.values[n];
	}
	protected Type getType()
	{
		return Type.byCode(this.type);
	}
	private void snapAndLimit(int key) // прив€зать к сетке и скорректировать при неправильном знаке  
	{
		if (this.values[key] * (IS_KEY_UPPER[key] ? 1 : -1) < 0)
			this.values[key] = 0;
		if (VALUE_FRACTION > 0)
			this.values[key] = Math.rint(this.values[key] * VALUE_FRACTION) / VALUE_FRACTION;
	}
	private void interLimit(int key) // наложить ограничение согласно LIMIT_KEY
	{
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (this.values[key] * compareSign < this.values[LIMIT_KEY[key]] * compareSign)
			this.values[key] = this.values[LIMIT_KEY[key]];
	}
	protected void setDY(int key, double val)
	{
		this.values[key] = val;
		snapAndLimit(key);
		interLimit(key);
	}
	@Override
	protected void arrangeLimits(int key)
	{
		int compareSign = IS_KEY_HARD[key] ^ IS_KEY_UPPER[key] ? -1 : 1;
		if (this.values[key] * compareSign < this.values[FORCEMOVE_KEY[key]] * compareSign)
			this.values[FORCEMOVE_KEY[key]] = this.values[key];
	}
	public void changeAllBy(double delta)
	{
		for (int key = 0; key < 4; key++)
		{
			this.values[key] += (IS_KEY_UPPER[key] ? delta : -delta) * (IS_KEY_HARD[key] ? 2 : 1);
			snapAndLimit(key);
		}
		for (int key = 0; key < 4; key++)
			interLimit(key);
	}
	@Override
	protected void roundUp(int key)
	{
		int sign = IS_KEY_UPPER[key] ? 1 : -1;
		if (VALUE_FRACTION > 0)
			this.values[key] = Math.ceil(this.values[key] * VALUE_FRACTION * sign) / VALUE_FRACTION / sign;
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

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.type;
		for (int i = 0; i < this.values.length; i++) {
			long bits = Double.doubleToLongBits(this.values[i]);
			result = 37 * result + (int)(bits ^ (bits >>> 32));
		}
		return result;
	}
}
