/*
 * $Id: ClientAnalysisManager.java,v 1.10 2005/05/19 12:05:29 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;

/**
 * @author $Author: saa $
 * @version $Revision: 1.10 $, $Date: 2005/05/19 12:05:29 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.properties";

	private static AnalysisParameters DEFAULT_AP = new AnalysisParameters (
			0.05, //минимальный уровень события
			0.07, //минимальный уровень сварки
			0.21, //минимальный уровень коннектора
			3,  //мин. уровень отражения конца волокна
			3 //коэфф. запаса для шума
	);

    public static AnalysisParameters getDefaultAPClone() {
        return (AnalysisParameters)DEFAULT_AP.clone();
    }

	public ClientAnalysisManager() {
		AnalysisParameters minuitParams;
		Heap.setMinuitDefaultParams(getDefaultAPClone());

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			String temp = properties.getProperty("parameters");
			minuitParams = new AnalysisParameters(temp, DEFAULT_AP);
		} catch (IOException ex) {
			minuitParams = (AnalysisParameters)DEFAULT_AP.clone();
		}

		// сохраняем в Pool
		Heap.setMinuitAnalysisParams(minuitParams);
		Heap.setMinuitInitialParams((AnalysisParameters)minuitParams.clone());
	}

	public void saveIni() {
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			properties.setProperty("parameters", ap.toString());
			properties.store(new FileOutputStream(PROPERTIES_FILE_NAME), null);
		} catch (IOException ex) {
			// FIXME: exceptions: add an IOException handler
		}
	}
}
