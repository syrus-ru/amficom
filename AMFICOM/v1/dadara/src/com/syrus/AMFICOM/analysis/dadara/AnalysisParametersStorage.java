/*-
 * $Id: AnalysisParametersStorage.java,v 1.3 2005/08/29 09:57:11 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * ’ранилище набора параметров анализа, не реализующее контрол€ допустимости.
 * –екомендуетс€ использовать {@link AnalysisParameters},
 * а {@link AnalysisParametersStorage} использовать тогда, когда нужно изменить
 * сразу несколько параметров.
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/08/29 09:57:11 $
 * @todo add extended parameters save to DOS / restore from DIS
 * @module
 */
public class AnalysisParametersStorage
implements DataStreamable, Cloneable
{
	private static final double DEFAULT_THRESHOLD_TO_SPLICE_RATIO = 0.4;
	private static final double[] RECOMMENDED_NOISE_FACTORS = new double[] {
		1.0, 1.3, 1.5, 2.0, 2.5 };
	private static final double MIN_MIN_THRESHOLD = 0.001 * DEFAULT_THRESHOLD_TO_SPLICE_RATIO;

	private double[] param; // основные параметры анализа

	// дополнительные параметры анализа - экспериментальна€ верси€
	private double tau2nrs = 1.0;
	private int nrsMin = 15;
	private double rsaCrit = 0.5;
	private double nrs2rsaSmall = 1.5;
	private double nrs2rsaBig = 10.0;
	private double l2rsaBig = 0.1;

	// еще дополнительный параметр
	private double scaleFactor = 1.0;

	/**
	 * ”станавливает все свои свойства так же, как и у другого экземпл€р€
	 * @param that другой экземпл€р
	 */
	public void setAllFrom(AnalysisParametersStorage that) {
		System.arraycopy(that.param, 0, this.param, 0, this.param.length);
		this.tau2nrs = that.tau2nrs;
		this.nrsMin = that.nrsMin;
		this.rsaCrit = that.rsaCrit;
		this.nrs2rsaSmall = that.nrs2rsaSmall;
		this.nrs2rsaBig = that.nrs2rsaBig;
		this.l2rsaBig = that.l2rsaBig;
	}

	/**
	 * ќпредел€ет допустимость набора параметров.
	 * @return true, если набор допустим, false, если недопустим
	 */
	public boolean isCorrect() {
		// провер€ем основные параметры
		if (getMinThreshold() < MIN_MIN_THRESHOLD)
			return false;
		if (getMinSplice() < getMinThreshold())
			return false;
		if (getMinConnector() < getMinSplice())
			return false;
		if (getMinEnd() < getMinConnector())
			return false;

		// провер€ем дополнительные параметры
		if (this.tau2nrs < 0)
			return false;
		if (this.nrsMin < 0)
			return false;
		if (this.tau2nrs == 0 && this.nrsMin == 0)
			return false;
		if (this.rsaCrit < 0)
			return false;
		if (this.nrs2rsaSmall <= 0)
			return false;
		if (this.nrs2rsaBig < this.nrs2rsaSmall)
			return false;
		if (this.l2rsaBig < 0)
			return false;

		if (this.scaleFactor < 1.0)
			return false;
		if (this.scaleFactor > 10) // XXX
			return false;

		return true;
	}

	public double getMinThreshold() {
		return this.param[0];
	}
	public double getMinSplice() {
		return this.param[1];
	}
	public double getMinConnector() {
		return this.param[2];
	}
	public double getMinEnd() {
		return this.param[3];
	}
	public double getNoiseFactor() {
		return this.param[4];
	}

	public void setMinThreshold(double v) {
		this.param[0] = v;
	}

	public void setMinSplice(double v) {
		this.param[1] = v;
	}

	public void setSensitivity(double v) {
		this.setMinSplice(v);
		this.setMinThreshold(v * DEFAULT_THRESHOLD_TO_SPLICE_RATIO);
	}

	public double getSentitivity() {
		return getMinSplice();
	}

	public void setMinConnector(double v) {
		this.param[2] = v;
	}

	public void setMinEnd(double v) {
		this.param[3] = v;
	}

	public void setNoiseFactor(double v) {
		this.param[4] = v;
	}

	/**
	 * @return список рекомендуемых значений noiseFactor
	 */
	public static double[] getRecommendedNoiseFactors() {
		return RECOMMENDED_NOISE_FACTORS.clone();
	}

	public AnalysisParametersStorage(double minThreshold,
			double minSplice,
			double minConnector,
			double minEnd,
			double noiseFactor)
	{
		this.param = new double[] {
			minThreshold,
			minSplice,
			minConnector,
			minEnd,
			noiseFactor
		};
	}

	public AnalysisParametersStorage(DataInputStream dis)
	throws IOException {
		this.param = new double[5];
		this.param[0] = dis.readDouble();
		this.param[1] = dis.readDouble();
		this.param[2] = dis.readDouble();
		this.param[3] = dis.readDouble();
		this.param[4] = dis.readDouble();
	}

	// returns true if all fields were initialized,
	// false otherwise.
	private boolean setParamsFromString(String val) {
		int i = 0;
		int bind = -1;
		int ind = val.indexOf(";");
		while ((ind != -1) && (i < this.param.length)) {
			this.param[i++] = Double.parseDouble(val.substring(bind + 1, ind));
			bind = ind;
			ind = val.indexOf(";", bind + 1);
		}
		return i == this.param.length;
	}

	/**
	 * creates via string of parameters using the default values
	 * @param val text representation of parameters
	 * @param defaults default values
	 */
	public AnalysisParametersStorage(String val, AnalysisParametersStorage defaults) {
		this.param = defaults.param.clone();
		setParamsFromString(val);
	}

	/**
	 * creates via string of parameters
	 * @param val text representation of parameters
	 * @throws IllegalArgumentException if input string is malformed
	 */
	public AnalysisParametersStorage(String val) {
		this.param = new double[5];
		if (!setParamsFromString(val))
			throw new IllegalArgumentException(
					"couldn't parse analysis parameters string");
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.param.length; i++)
			str = str + String.valueOf(this.param[i]) + ";";
		return str;
	}

	@Override
	public Object clone() {
		try {
			AnalysisParametersStorage ret = (AnalysisParametersStorage)super.clone();
			ret.param = this.param.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Unexpected exception: " + e.getMessage());
		}
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeDouble(this.param[0]);
		dos.writeDouble(this.param[1]);
		dos.writeDouble(this.param[2]);
		dos.writeDouble(this.param[3]);
		dos.writeDouble(this.param[4]);
	}
	
	public double getL2rsaBig() {
		return this.l2rsaBig;
	}
	public void setL2rsaBig(double big) {
		this.l2rsaBig = big;
	}
	public double getNrs2rsaBig() {
		return this.nrs2rsaBig;
	}
	public void setNrs2rsaBig(double nrs2rsaBig) {
		this.nrs2rsaBig = nrs2rsaBig;
	}
	public double getNrs2rsaSmall() {
		return this.nrs2rsaSmall;
	}
	public void setNrs2rsaSmall(double nrs2rsaSmall) {
		this.nrs2rsaSmall = nrs2rsaSmall;
	}
	public int getNrsMin() {
		return this.nrsMin;
	}
	public void setNrsMin(int nrsMin) {
		this.nrsMin = nrsMin;
	}
	public double getRsaCrit() {
		return this.rsaCrit;
	}
	public void setRsaCrit(double rsaCrit) {
		this.rsaCrit = rsaCrit;
	}
	public double getTau2nrs() {
		return this.tau2nrs;
	}
	public void setTau2nrs(double tau2nrs) {
		this.tau2nrs = tau2nrs;
	}

	public double getScaleFactor() {
		return this.scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
}
