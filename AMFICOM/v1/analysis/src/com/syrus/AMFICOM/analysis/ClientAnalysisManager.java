/*
 * $Id: ClientAnalysisManager.java,v 1.5 2005/04/07 15:22:54 saa Exp $
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

import com.syrus.AMFICOM.Client.Analysis.Heap;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/04/07 15:22:54 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private final String propertiesFileName = "analysis.properties";

	double[] defaultMinuitParams = {
			0.05, //минимальный уровень событи€
			0.07, //минимальный уровень сварки
			0.21, //минимальный уровень коннектора
			3 //коэфф. запаса дл€ шума
	};

	public ClientAnalysisManager() {
		double[] minuitParams;
		Heap.setMinuitDefaultParams(defaultMinuitParams);

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFileName));
			String temp = properties.getProperty("parameters");
			minuitParams = decompose(temp, defaultMinuitParams);
		} catch (IOException ex) {
		    // делаем копию массива
			minuitParams = (double[] )defaultMinuitParams.clone();
		}

		// сохран€ем в Pool
		Heap.setMinuitAnalysisParams(minuitParams);
		// сохран€ем в Pool копию (double[] копируетс€ целиком)
		Heap.setMinuitInitialParams((double[])minuitParams.clone());
	}

	public void saveIni() {
	    double[] minuitParams = Heap.getMinuitAnalysisParams();
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
