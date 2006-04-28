/*
 * $Id: ClientAnalysisManager.java,v 1.28 2006/04/28 11:19:21 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.util.Log;

/**
 * @author $Author: saa $
 * @version $Revision: 1.28 $, $Date: 2006/04/28 11:19:21 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = AnalysisIniFile.INI_FILE_NAME;

	public ClientAnalysisManager() {
		AnalysisParameters minuitParams = null;

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			String temp = properties.getProperty("parameters");
			if (temp != null)
				minuitParams =
					new AnalysisParameters(temp, Heap.getMinuitDefaultParams());
		} catch (IOException ex) {
			// write a error to log, then ignore
			Log.errorMessage("IOException while reading minuitParams from INI file");
		} catch (InvalidAnalysisParametersException e) {
			// write a error to log, then ignore
			Log.errorMessage("InvalidAnalysisParametersException while reading minuitParams from INI file");
		}
		if (minuitParams == null)
				minuitParams = Heap.getMinuitDefaultParams();

		// сохраняем в Pool
		Heap.setMinuitAnalysisParams(minuitParams);
		Heap.setMinuitInitialParamsFromCurrentAP();
		Heap.notifyAnalysisParametersUpdated();
	}

	public void saveIni() {
		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
		} catch (IOException ex) {
			// FIXME: exceptions: add an IOException handler
		}
		try {
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
		PFTrace pf = Heap.getPFTracePrimary();
		if (pf == null)
			return;
		double yMinAbs = ReflectogramMath.getArrayMin(pf.getFilteredTraceClone());
		double[] yMT = mtae.getModelTrace().getYArray();
		if (yMT.length == 0) {
			Heap.setMinTraceLevel(yMinAbs);
			return;
		}
		int maxIndex = ReflectogramMath.getArrayMaxIndex(yMT, 0, yMT.length - 1);
		int rMinIndex = ReflectogramMath.getArrayMinIndex(yMT, maxIndex, yMT.length - 1);
		double yMinMT = yMT[rMinIndex];
		Heap.setMinTraceLevel((yMinAbs + yMinMT) / 2);

	}
}
