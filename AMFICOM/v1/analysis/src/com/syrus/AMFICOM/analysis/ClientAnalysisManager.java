/*
 * $Id: ClientAnalysisManager.java,v 1.15 2005/06/20 15:57:55 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2005/06/20 15:57:55 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.ini";

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
		AnalysisParameters minuitParams = null;
		Heap.setMinuitDefaultParams(getDefaultAPClone());

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			String temp = properties.getProperty("parameters");
			if (temp != null)
				minuitParams = new AnalysisParameters(temp, defaultAP);
		} catch (IOException ex) {
			// just ignore, that's ok
		}
		if (minuitParams == null)
				minuitParams = (AnalysisParameters)defaultAP.clone();

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

	/**
	 * выставляет рекомендуемое значение Heap.minTracelevel на основе
	 * хода модельной кривой и абс. минимума рефлектограммы
	 */
	public static void setDefaultMinTraceLevel() {
		ModelTraceAndEventsImpl mtae = Heap.getMTAEPrimary();
		if (mtae == null)
			return;
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null)
			return;
		double yMinAbs = ReflectogramMath.getArrayMin(bs.getTraceData());
		double[] yMT = mtae.getModelTrace().getYArray();
		int maxIndex = ReflectogramMath.getArrayMaxIndex(yMT, 0, yMT.length - 1);
		int rMinIndex = ReflectogramMath.getArrayMinIndex(yMT, maxIndex, yMT.length - 1);
		double yMinMT = yMT[rMinIndex];
		Heap.setMinTraceLevel((yMinAbs + yMinMT) / 2);

	}
}
