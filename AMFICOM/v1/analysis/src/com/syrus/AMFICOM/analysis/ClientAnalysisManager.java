/*
 * $Id: ClientAnalysisManager.java,v 1.2 2004/12/15 12:02:20 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

import com.syrus.AMFICOM.Client.Resource.Pool;
//import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2004/12/15 12:02:20 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private final String propertiesFileName = "analysis.properties";
	private static final String OT_analysisparameters = "analysisparameters";
	private static final String OID_minuitanalysis = "minuitanalysis";
	private static final String OID_minuitinitials = "minuitinitials";
	private static final String OID_minuitdefaults = "minuitdefaults";

	double[] defaultMinuitParams = { 0.04, //минимальный уровень события
			0.06, //минимальный уровень сварки
			0.2, //минимальный уровень коннектора
			3, //минимальный уровень конца
			.1, //максимальный уровень шума
			0.3, //скорость спадания (форм-фактор) коннектора (0..1)
			4, //стратегия (int [0..3])
			0, //номер вейвлета [0..8] (предпочтителен 0)
	};

	public ClientAnalysisManager() {
		double[] minuitParams;
		Pool.put(OT_analysisparameters, OID_minuitdefaults, defaultMinuitParams);

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFileName));
			String temp = properties.getProperty("parameters");
			minuitParams = decompose(temp, defaultMinuitParams);
		} catch (IOException ex) {
		    // делаем копию массива
			minuitParams = (double[] )defaultMinuitParams.clone();
		}

		// сохраняем в Pool
		Pool.put(OT_analysisparameters, OID_minuitanalysis, minuitParams);
		// сохраняем в Pool копию (double[] копируется целиком)
		Pool.put(OT_analysisparameters, OID_minuitinitials, minuitParams.clone());
	}

	public void saveIni() {
	    double[] minuitParams = (double[]) Pool.get(OT_analysisparameters,
				OID_minuitanalysis);
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFileName));
			properties.setProperty("parameters", compose(minuitParams));
			properties.store(new FileOutputStream(propertiesFileName), null);
		} catch (IOException ex) {
		}
	}

	private double[] decompose(String val, double[] defaults) {
		int i = 0;
		int bind = -1;
		int ind = val.indexOf(";");
		double d[] = new double[defaults.length];

		for (int j = 0; j < defaults.length; j++)
			d[j] = defaults[j];
		while ((ind != -1) && (i < defaults.length)) {
			d[i++] = Double.parseDouble(val.substring(bind + 1, ind));
			bind = ind;
			ind = val.indexOf(";", bind + 1);
		}
		return d;
	}

	private String compose(double[] d) {
		String str = "";
		for (int i = 0; i < d.length; i++)
			str = str + String.valueOf(d[i]) + ";";
		return str;
	}
}
