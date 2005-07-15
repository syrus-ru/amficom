/*-
 * $Id: AnalysisParameters.java,v 1.9 2005/07/15 11:57:25 saa Exp $
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
 * @author $Author: saa $
 * @version $Revision: 1.9 $, $Date: 2005/07/15 11:57:25 $
 * @module
 */
public class AnalysisParameters
implements DataStreamable, Cloneable
{
	private double[] param; // основные параметры анализа

	// дополнительные параметры анализа - экспериментальная версия
	private double tau2nrs = 1.0;
	private int nrsMin = 15;
	private double rsaCrit = 0.5;
	private double nrs2rsaSmall = 1.5;
	private double nrs2rsaBig = 10.0;
	private double l2rsaBig = 0.1;

	private static DSReader reader;
	private static final double[] RECOMMENDED_NOISE_FACTORS = new double[] {
		0.7, 1.0, 1.3, 1.5, 2.0, 2.5, 3.0 }; // XXX: remove 0.7 and 3.0

    /**
     * Определяет допустимость набора параметров.
     * @return true, если набор допустим, false, если недопустим
     */
    public boolean isCorrect() {
    	// проверяем основные параметры
        final double MIN_MIN_THRESHOLD = 0.0001; // FIXME: debug: MIN_MIN_THRESHOLD should be >= 0.001
		if (getMinThreshold() < MIN_MIN_THRESHOLD)
			return false;
		if (getMinSplice() < getMinThreshold())
			return false;
		if (getMinConnector() < getMinSplice())
			return false;
        if (getMinEnd() < getMinConnector())
            return false;

        // проверяем дополнительные параметры
        if (tau2nrs < 0)
        	return false;
        if (nrsMin < 0)
        	return false;
        if (tau2nrs == 0 && nrsMin == 0)
        	return false;
        if (rsaCrit < 0)
        	return false;
        if (nrs2rsaSmall <= 0)
        	return false;
        if (nrs2rsaBig < nrs2rsaSmall)
        	return false;
        if (l2rsaBig < 0)
        	return false;

        return true;
    }

	public double getMinThreshold() {
		return param[0];
	}
	public double getMinSplice() {
		return param[1];
	}
	public double getMinConnector() {
		return param[2];
	}
	public double getMinEnd() {
		return param[3];
	}
	public double getNoiseFactor() {
		return param[4];
	}

	public void setMinThreshold(double v) {
		param[0] = v;
	}

	public void setMinSplice(double v) {
		param[1] = v;
	}

	public void setMinConnector(double v) {
		param[2] = v;
	}

	public void setMinEnd(double v) {
		param[3] = v;
	}

	public void setNoiseFactor(double v) {
		param[4] = v;
	}

    /**
     * @return список рекомендуемых значений noiseFactor
     */
    public static double[] getRecommendedNoiseFactors() {
        return RECOMMENDED_NOISE_FACTORS.clone();
    }

	public AnalysisParameters(double minThreshold,
			double minSplice,
			double minConnector,
			double minEnd,
			double noiseFactor)
	{
		param = new double[] {
			minThreshold,
			minSplice,
			minConnector,
			minEnd,
			noiseFactor
		};
	}

	public AnalysisParameters(DataInputStream dis)
	throws IOException {
		param = new double[5];
		param[0] = dis.readDouble();
		param[1] = dis.readDouble();
		param[2] = dis.readDouble();
		param[3] = dis.readDouble();
		param[4] = dis.readDouble();
	}

    // returns true if all fields were initialized,
    // false otherwise.
    private boolean setParamsFromString(String val) {
        int i = 0;
        int bind = -1;
        int ind = val.indexOf(";");
        while ((ind != -1) && (i < param.length)) {
            param[i++] = Double.parseDouble(val.substring(bind + 1, ind));
            bind = ind;
            ind = val.indexOf(";", bind + 1);
        }
        return i == param.length;
    }

    /**
     * creates via string of parameters using the default values
     * @param val text representation of parameters
     * @param defaults default values
     */
	public AnalysisParameters(String val, AnalysisParameters defaults) {
		param = defaults.param.clone();
        setParamsFromString(val);
	}

    /**
     * creates via string of parameters
     * @param val text representation of parameters
     * @throws IllegalArgumentException if input string is malformed
     */
    public AnalysisParameters(String val) {
        param = new double[5];
        if (!setParamsFromString(val))
            throw new IllegalArgumentException(
                    "couldn't parse analysis parameters string");
    }

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < param.length; i++)
			str = str + String.valueOf(param[i]) + ";";
		return str;
	}

	@Override
	public Object clone() {
		try {
			AnalysisParameters ret = (AnalysisParameters)super.clone();
			ret.param = this.param.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Unexpected exception: " + e.getMessage());
		}
	}
	private static class DSReader implements DataStreamable.Reader {
		public DataStreamable readFromDIS(DataInputStream dis)
		throws IOException, SignatureMismatchException {
			return new AnalysisParameters (dis);
		}
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeDouble(param[0]);
		dos.writeDouble(param[1]);
		dos.writeDouble(param[2]);
		dos.writeDouble(param[3]);
		dos.writeDouble(param[4]);
	}
	
	public static DataStreamable.Reader getReader() {
		if (reader == null) {
			reader = new DSReader();
		}
		return reader;
	}
	public double getL2rsaBig() {
		return l2rsaBig;
	}
	public void setL2rsaBig(double big) {
		l2rsaBig = big;
	}
	public double getNrs2rsaBig() {
		return nrs2rsaBig;
	}
	public void setNrs2rsaBig(double nrs2rsaBig) {
		this.nrs2rsaBig = nrs2rsaBig;
	}
	public double getNrs2rsaSmall() {
		return nrs2rsaSmall;
	}
	public void setNrs2rsaSmall(double nrs2rsaSmall) {
		this.nrs2rsaSmall = nrs2rsaSmall;
	}
	public int getNrsMin() {
		return nrsMin;
	}
	public void setNrsMin(int nrsMin) {
		this.nrsMin = nrsMin;
	}
	public double getRsaCrit() {
		return rsaCrit;
	}
	public void setRsaCrit(double rsaCrit) {
		this.rsaCrit = rsaCrit;
	}
	public double getTau2nrs() {
		return tau2nrs;
	}
	public void setTau2nrs(double tau2nrs) {
		this.tau2nrs = tau2nrs;
	}
}
