/*
 * $Id: ClientAnalysisManager.java,v 1.13 2005/06/15 10:15:13 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: saa $
 * @version $Revision: 1.13 $, $Date: 2005/06/15 10:15:13 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.properties";

	private static AnalysisParameters defaultAP = new AnalysisParameters (
			0.05, //����������� ������� �������
			0.07, //����������� ������� ������
			0.21, //����������� ������� ����������
			3,  //���. ������� ��������� ����� �������
			3 //�����. ������ ��� ����
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

		// ��������� � Pool
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
		System.out.println("yMinAbs = " + yMinAbs + "; yMinMT = " + yMinMT);
		Heap.setMinTraceLevel((yMinAbs + yMinMT) / 2);

	}
}
