/*-
 * $Id: AnalysisParameters.java,v 1.3 2005/04/28 15:27:05 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/04/28 15:27:05 $
 * @module
 */
public class AnalysisParameters
implements DataStreamable, Cloneable
{
	private double[] param;
	private static DSReader reader;

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
			noiseFactor,
			minEnd
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

	public AnalysisParameters(String val, AnalysisParameters defaults) {
		param = (double[])defaults.param.clone();
		int i = 0;
		int bind = -1;
		int ind = val.indexOf(";");
		while ((ind != -1) && (i < param.length)) {
			param[i++] = Double.parseDouble(val.substring(bind + 1, ind));
			bind = ind;
			ind = val.indexOf(";", bind + 1);
		}
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < param.length; i++)
			str = str + String.valueOf(param[i]) + ";";
		return str;
	}

	public Object clone() {
		try {
			AnalysisParameters ret = (AnalysisParameters)super.clone();
			ret.param = (double[])this.param.clone();
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
}
