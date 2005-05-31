/*
 * $Id: ClientAnalysisManager.java,v 1.12 2005/05/31 07:59:40 saa Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/05/31 07:59:40 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.properties";

	private static AnalysisParameters defaultAP = new AnalysisParameters (
			0.05, //минимальный уровень события
			0.07, //минимальный уровень сварки
			0.21, //минимальный уровень коннектора
			3,  //мин. уровень отражения конца волокна
			3 //коэфф. запаса для шума
	);

    public static AnalysisParameters getDefaultAPClone() {
        return (AnalysisParameters)defaultAP.clone();
    }

	public ClientAnalysisManager() {
		AnalysisParameters minuitParams;
		Heap.setMinuitDefaultParams(getDefaultAPClone());

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			String temp = properties.getProperty("parameters");
			minuitParams = new AnalysisParameters(temp, defaultAP);
		} catch (IOException ex) {
			minuitParams = (AnalysisParameters)defaultAP.clone();
		}

		// сохраняем в Pool
		Heap.setMinuitAnalysisParams(minuitParams);
		Heap.setMinuitInitialParams((AnalysisParameters)minuitParams.clone());
        Heap.notifyAnalysisParametersUpdated();
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
