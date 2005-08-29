/*
 * $Id: ClientAnalysisManager.java,v 1.19 2005/08/29 09:39:45 saa Exp $
 * 
 * Copyright � Syrus Systems.
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
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

/**
 * @author $Author: saa $
 * @version $Revision: 1.19 $, $Date: 2005/08/29 09:39:45 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.ini";

	private static AnalysisParameters defaultAP;

	static {
		try {
			defaultAP = new AnalysisParameters (
					0.005, //����������� ������� �������
					0.02, //����������� ������� ������
					0.5, //����������� ������� ����������
					3,  //���. ������� ��������� ����� �������
					1.3 //�����. ������ ��� ����
			);
		} catch (InvalidAnalysisParametersException e) {
			throw new InternalError("couldn't initialize defaultAP");
		}
	}

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
			// write a error to log, then ignore
			Log.errorMessage("ClientAnalysisManager.ClientAnalysisManager | "
					+ "IOException while reading minuitParams from INI file");
		} catch (InvalidAnalysisParametersException e) {
			// write a error to log, then ignore
			Log.errorMessage("ClientAnalysisManager.ClientAnalysisManager | "
					+ "InvalidAnalysisParametersException while reading minuitParams from INI file");
		}
		if (minuitParams == null)
				minuitParams = (AnalysisParameters)defaultAP.clone();

		// ��������� � Pool
		Heap.setMinuitAnalysisParams(minuitParams);
		Heap.setMinuitInitialParamsFromCurrentAP();
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
	 * ���������� ������������� �������� Heap.minTracelevel �� ������
	 * ���� ��������� ������ � ���. �������� ��������������
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
