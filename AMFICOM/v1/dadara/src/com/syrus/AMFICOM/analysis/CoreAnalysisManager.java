/*
 * $Id: CoreAnalysisManager.java,v 1.12 2005/02/21 13:39:33 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.12 $, $Date: 2005/02/21 13:39:33 $
 * @module
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class CoreAnalysisManager
{
	protected CoreAnalysisManager()
	{ // empty
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);
	
	private static native double nMedian(double[] y, int pos);

	/**
	 * @param waveletType ����� - ����� ��������
	 * @param y ������� ��������������
	 * @param d_x ��� ������������� � ������
	 * @param formFactor ?
	 * @param min_level ���. ������� �������
	 * @param max_level_noise ����. ������� ����?
	 * @param min_level_to_find_end ���. ������� �����
	 * @param min_weld ���. ������
	 * @param min_connector ���. ���������
	 * @param out_meanAttenuation ������ ��� �������� ��������
	 * �������� ���������, ������ �� ����������
	 * @param reflectiveSize ���. ������ �����. ������� (� ������?)
	 * @param nonReflectiveSize ���. ������ �������. ������� (� ������?)
	 * @return ������ �������
	 */
	private static native ReflectogramEvent[] analyse2(int waveletType,
			double[] y, //the refl. itself
			double d_x, //dx
			double formFactor, // 1. ����-������
			double min_level, // 2. ��� ������� �������
			double max_level_noise, // 3. ���� ������� ����
			double min_level_to_find_end,// 4. ���. ������� �����
			double min_weld, // 5. ����������� ������
			double min_connector, // 6. ����������� ���������
			double[] out_meanAttenuation, // ��� �������� �������� �������� ���������
			int reflectiveSize, int nonReflectiveSize);
	
	/**
	 * ����� ��������� ������� ���� �� �������������� �� ������ 3 sigma,
	 * ����������� ������� ����, ����������� �� �������� ����������� �������.
	 * ��� ������������ �� ������ ����������� ������������� ��������
	 * fabs(2*data[n] - data[n-8] - data[n+8]) ��� ����� n-8,n,n+8, ���
	 * ��� ������������ data[], � data = 10^(y/5) - ��������
	 * (�� ���������������) �������� �������. ����� ����������� �������
	 * ������� ���� � �������� ����������� ������� y[]. ��� 8 ������
	 * �� ������ ��������� NetTest(tm) ��� ���� ������������������ �������
	 * ������� BC.
	 * 
	 * ����������� sigma ������������� ��� ������������� � ���������� 
	 * ������������� � ����������� ������� �� ���� ����.
	 * 
	 * � ������ ��������� "� �������������� ������� ���� �������
	 * �������� ������ ��������", �������� ��� ����� ������ �� �������,
	 * � ������� ��� ����� ����� ������ ��������.
	 *      
	 * @param y ������� ��������������
	 * @return 3*sigma ���������� ����, ����������
	 * � �������� ������������������� ��������
	 */
	private static native double nCalcNoise3s(double[] y);

	private static native double[] nCalcNoiseArray(double[] y);

	/**
	 * ������ ������ ���� �� ������ ��������������.
	 * �������� ����� ������������ ��� �� ������������ �������
	 * ���������������� IA ������� (ev). �� ���������, ��� �����
	 * ����������� ���� ����� ������ ���������� IA.
	 * @param y  ������� ������ �������������� � ��.
	 * @param ev (�����������) ��������������� ������ IA. ���� �� null,
	 *   �� ����� ��������������, � ����� �� ��������������.
	 *   ���� null, �� �� ������������.
	 * @return ������������� �������� ���� (��) �� ������ 3 �����  
	 */
	private static double[] calcNoiseArray(double[] y, SimpleReflectogramEvent[] ev)
	{
		double[] ret = nCalcNoiseArray(y);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	static {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

	public static double[] calcGaussian(double[] y, int maxIndex) {
		double[] gauss = new double[y.length];
		int width = 0;

		double maxValue = y[maxIndex];

		maxValue *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > maxValue)
				width++;
		maxValue /= .2;

		double[] d = gauss(y, maxIndex, maxValue, width);
		double center = d[0];
		maxValue = d[1];
		double sigmaSquared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = maxValue
					* Math.exp(-(i - center) * (i - center) / sigmaSquared);

		return gauss;
	}

	public static double[] calcThresholdCurve(double[] y, int max_index) {
		double[] threshold = new double[y.length];
		double max = 0;

		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = 0;
			for (int j = 0; j < y.length; j++)
				if (j <= max_index - i || j >= max_index + i)
					threshold[i] += y[j];

			if (max < threshold[i])
				max = threshold[i];
		}

		for (int i = 0; i < threshold.length; i++)
			threshold[i] /= max;

		return threshold;
	}

	/**
	 * ������ ������. �������� �� UI ���������, ��������� � ����������
	 * �������� ������ IA, fit, calcMutualParameters � ������������ ���. �������.
	 * 
	 * @param strategy ������� �������������. �����������������.
	 * @param bs ��������������
	 * @param pars ������������������� ����� ���������� ��� IA
	 * @param bellcoreTraces ��� ���� �������� �/� ��� ����������� �������� ���������
	 * @return ������ �������
	 */

	// strategy == 0: min fitting
	public static ModelTraceManager makeAnalysis(
			int strategy, BellcoreStructure bs,
			double[] pars, Map bellcoreTraces)
	{
		long t0 = System.currentTimeMillis();
	    // ������� ������
	    double y[] = bs.getTraceData();
	    double deltaX = bs.getResolution(); // �����

		int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				y,
				1000,
				bs.getIOR(),
				deltaX);
		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;

		long t1 = System.currentTimeMillis();
		// ������������ �������
		double[] meanAttenuation = { 0 }; // storage for meanAttenuation -- unused: XXX
		ReflectogramEvent[] ep = analyse2((int) pars[7], y, deltaX, pars[5],
				pars[0], pars[4], pars[3], pars[1], pars[2], meanAttenuation,
				reflSize, nReflSize);

		long t2 = System.currentTimeMillis();
		// ���������� ������� ���� ��� ���������
		//double noiseLevel = calcNoise3s(y);
		double[] noiseArray = calcNoiseArray(y, null);

		// ��������� ���������� ��������� � ���������
		// (� ������������ ���������� ������ ��� ������� ������ � ���������)
		//fitTrace(y, deltaX, ep, strategy, meanAttenuation[0], noiseArray); -- ����� �� ������������
		
		long t3 = System.currentTimeMillis();
		//�������� ����� ������
		ReflectogramEvent ev0 = ep[0].copy();
		ev0.setEnd(ep[ep.length - 1].getEnd());
		ReflectogramEvent[] el0 = new ReflectogramEvent[] { ev0 };
		fitTrace(y, deltaX, el0, strategy, meanAttenuation[0], noiseArray);

		long t4 = System.currentTimeMillis();
		// ����������� ���������� ������ � ��������� (�� ������� ��������)
		ReflectogramEvent.calcMutualParameters(ep, y);

		// ������ ��� � ��������� ������ -- FIXME - �������
//		for (int i = 0; i < ep.length; i++)
//			ep[i].setDefaultThreshold(bs, bellcoreTraces);

		long t5 = System.currentTimeMillis();
		System.out.println("makeAnalysis: getData: " + (t1-t0) + "; IA: " + (t2-t1) + "; noiseArray:" + (t3-t2) + "; fit: " + (t4-t3) + "; postProcess: " + (t5-t4));
		return new ModelTraceManager(ep, el0[0].getMFClone());
	}

	private static ReflectogramEvent[] fitTrace(double[] y, double deltaX,
			ReflectogramEvent[] events, int strategy, double meanAttenuation, double[] noiseArray) {
		long t0 = System.currentTimeMillis();

		System.out.println("fitTrace: strategy = " + strategy);

		ReflectogramEvent.FittingParameters fPars =
//		    new ReflectogramEvent.FittingParameters(
//				y,
//				0.01 + 0.001, // XXX: 0.01: user-set; 0.001: rg precision
//				-99);
			new ReflectogramEvent.FittingParameters(
				y, noiseArray);

		/*try
		{
			FileOutputStream fos = new FileOutputStream(new File("tr_er.dat"));
			for (int i = 0; i < y.length; i++)
			{
				fos.write(("" + i + " " + y[i] + " " + noiseArray[i] + "\n").getBytes());
			}
		}
		catch(IOException e)
		{
			System.err.println("CoreAnalysismanager: debug output failed");
		}*/

		for (int i = 0; i < events.length; i++)
		    events[i].setFittingParameters(fPars);
		long t1 = System.currentTimeMillis();

		for (int i = 0; i < events.length; i++)
			events[i].doFit();

		long t2 = System.currentTimeMillis();

		System.out.println("delta time: fit: prepare: " + (t1-t0) + "; doFit: " + (t2-t1));

		return events;
	}
	
	// ���������� ������� ���� � �/� -- ������ �����
	public static double calcNoise3s(double[] y)
	{
		return nCalcNoise3s(y);
	}

	public static double getMedian(double[] y, int pos)
	{
		return nMedian(y, pos);
	}
	public static double getMedian(double[] y)
	{
		return nMedian(y, y.length / 2);
	}

	/**
	 * �������� ��������� ����������. ����� ��������� �������� �������� ���������
	 * L-��������������� �� �����, � ����� ���� �����. ���. � ��������� ���������
	 * ����������, ����� ����� ���� �������� ����� �� �����.
	 * @param mf ��������� ������
	 * @param evBegin ��������� ������ �������
	 * @param evEnd ��������� ����� �������
	 * @return int[3] { ������ ������� (���������� ������), ��������, ��������� ������� (���������� �����) } 
	 */
	public static int[] getConnectorMinMaxMin(ModelFunction mf, int evBegin, int evEnd)
	{
		int x0 = evBegin;
		int N = evEnd - evBegin + 1;
		double[] arr = mf.funFillArray(x0, 1.0, N);
		int iMax = ReflectogramMath.getArrayMaxIndex(arr, 0, N - 1);
		int iLMin = ReflectogramMath.getArrayMinIndex(arr, 0, iMax);
		int iRMin = ReflectogramMath.getArrayMinIndex(arr, iMax, N - 1);
		return new int[] {evBegin + iLMin, evBegin + iMax, evBegin + iRMin};
	}
}
