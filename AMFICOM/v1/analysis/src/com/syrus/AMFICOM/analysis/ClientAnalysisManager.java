/*
 * $Id: ClientAnalysisManager.java,v 1.7 2005/04/26 15:21:45 saa Exp $
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

/**
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/04/26 15:21:45 $
 * @module
 */
public class ClientAnalysisManager extends CoreAnalysisManager
{
	private static final String PROPERTIES_FILE_NAME = "analysis.properties";

	AnalysisParameters defaultMinuitParams = new AnalysisParameters (
			0.05, //����������� ������� �������
			0.07, //����������� ������� ������
			0.21, //����������� ������� ����������
			3, //�����. ������ ��� ����
			3  //���. ������� ��������� ����� �������
	);

	public ClientAnalysisManager() {
		AnalysisParameters minuitParams;
		Heap.setMinuitDefaultParams(defaultMinuitParams);

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
			String temp = properties.getProperty("parameters");
			minuitParams = new AnalysisParameters(temp, defaultMinuitParams);
		} catch (IOException ex) {
		    // ������ �����
			minuitParams = (AnalysisParameters)defaultMinuitParams.clone();
		}

		// ��������� � Pool
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
			// @todo: add an IOException handler
		}
	}
}
