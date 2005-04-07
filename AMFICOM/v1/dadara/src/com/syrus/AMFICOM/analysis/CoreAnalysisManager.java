/*
 * $Id: CoreAnalysisManager.java,v 1.31 2005/04/07 15:58:59 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.31 $, $Date: 2005/04/07 15:58:59 $
 * @module
 */

import java.util.Iterator;
import java.util.Map;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;

public class CoreAnalysisManager
{
	protected CoreAnalysisManager()
	{ // empty
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);
	
	private static native double nMedian(double[] y, int pos);

	/**
	 * @param y ��������������, ����������� � �������������
	 * @param dX ��� ������������� � ������ �� DX (�� ������������?)
	 * @param minLevel ������� ���������������� ��� ����������� ������ �������
	 * @param minWeld ����������� �����. �������
	 * @param minConnector ����������� �����. �������
	 * @param minEnd ���. ������� ����� (���� �� ������������?)
	 * @param reflSize ���. ������ �����. ������� (� ������) - ������ �� ����. ����� ���������� 
	 * @param nReflSize ���. ������ �������. ������� (� ������)
	 * @param traceLength ����� �������������� �� ����� �������,
	 * ����� ���� 0, ����� ����� ������� �������������
	 * @param noiseDB ������� ���� �� 1 �����, � ���. ��;
	 * ����� ���� null - ����� ����� ������ �������������
	 * @return ������ �������
	 */
	private static native SimpleReflectogramEventImpl[] analyse3(
			double[] y,
			double dX, 
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			int reflSize,
			int nReflSize,
			int traceLength,
			double[] noiseDB);

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

	/**
	 * ��������� ������� ���� � ���� "��������� ��������� ���������� ��
	 * ������ 1 �����". 
	 * @param y ������� ��������������
	 * @param lenght �����, �� ������� ������������ ����������
	 * ������� ����, ��� 0, ���� ��� ����� �� ���� y.length
	 * @return ������ [length > 0 ? length : y.length]
	 */
	private static native double[] nCalcNoiseArray(double[] y, int lenght);
	
	/**
	 * ����� ���������� ����� �������������� "�� ����� �������".
	 * �������� ����������� �������� �����, ��� ������ "������� ����",
	 * �� ��� ��� ����� ����� ����� ������� � native-���
	 * @param y ������� ��������������
	 * @return ����� �� "������� ����" ��� �� ������� ��������
	 */
	private static native int nCalcTraceLength(double[] y);

	/**
	 * testing...
	 */
	public static native void nExtendThreshToCoverCurve( // XXX: public native?
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate);

	/**
	 * ������ ������ ���� �� ������ ��������������.
	 * �������� ����� ������������ ��� �� ������������ �������
	 * ���������������� IA ������� (ev). �� ���������, ��� �����
	 * ����������� ���� ����� ������ ���������� IA.
	 * @param y  ������� ������ �������������� � ��.
	 * @param length ����� �������, > 0
	 * @return ������������� �������� ���� (��) �� ������ 3 �����, ����� ������� length  
	 */
	public static double[] calcNoiseArray(double[] y, int length)
	{
		double[] ret = nCalcNoiseArray(y, length);
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

	public static SimpleReflectogramEventImpl[] createSimpleEvents(
			double[] y,
			double deltaX,
			double minLevel,
			double minWeld,
			double minConnector,
			double noiseFactor,
			int reflSize,
			int nReflSize,
			int traceLength,
			double[] noiseArray)
	{
		return analyse3(y, deltaX,
			minLevel, minWeld, minConnector, noiseFactor,
			reflSize, nReflSize, traceLength, noiseArray);
	}
	
	public static ModelFunction fitTrace(double[] y, int traceLength, double[] noiseArray)
	{
		return ModelFunction.CreateFitedAsBreakL(y, 0, traceLength, noiseArray);
	}

	/**
	 * ��������� ��������� ��� �������.
	 * @param pars ����������� ���������
	 * @return true, ���� ����� ���������, false, ���� ����� �����������
	 */
	public static boolean checkAnalysisParameters(double[] pars)
	{
		if (pars.length < 3)
			return false;
		if (pars[0] < 0.01) // minThreshold
			return false;
		if (pars[1] < pars[0]) // minSplice
			return false;
		if (pars[2] < pars[1]) // minReflective
			return false;
		return true;
	}

	/**
	 * ������ ������. �������� ���������, ��������� � ����������
	 * �������� ������ IA, fit, calcMutualParameters � ������������ ���. �������.
	 * @todo: declare to throw "invalid parameters exception"
	 * 
	 * @param bs ��������������
	 * @param pars ����� ���������� ��� IA: { level, weld, connector, noiseFactor }
	 * @param bellcoreTraces ��� ���� �������� �/� ��� ����������� �������� ���������
	 * @return ������ �������
	 */

	// strategy == 0: min fitting
	public static ModelTraceManager makeAnalysis(
			BellcoreStructure bs,
			double[] pars, Map bellcoreTraces)
	{
		long t0 = System.currentTimeMillis();
	    // ������� ������
	    double y[] = bs.getTraceData();
	    double deltaX = bs.getResolution(); // �����
	    double pulseWidth = bs.getPulsewidth(); // ��

		int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				y,
				pulseWidth,
				bs.getIOR(),
				deltaX);
		if (nReflSize > 3 * reflSize / 5) // @todo: remove?
			nReflSize = 3 * reflSize / 5;

		reflSize *= 5; // XXX -- ?

		System.out.println("reflSize="+reflSize+"; nReflSize="+nReflSize);

		// ���������� ������� ����� �� ����� �������
		int traceLength = calcTraceLength(y);

		long t1 = System.currentTimeMillis();

		// ���������� ������� ���� ��� ���������
		double[] noiseArray = calcNoiseArray(y, traceLength);

		long t2 = System.currentTimeMillis();

		// ������������ �������
		SimpleReflectogramEventImpl[] se = createSimpleEvents(
				y, deltaX,
				pars[0], pars[1], pars[2], pars[3],
				reflSize, nReflSize,
				0, null); // FIXME: ������ � native: IA - ������ ����
				//traceLength, noiseArray);

		// ������ �������� ����� �������������� �� ����� ���������� �������
		// (����� ����� �����������)
		traceLength = se.length > 0
			? se[se.length - 1].getEnd() + 1
			: 0;

		long t3 = System.currentTimeMillis();
		ModelFunction mf = fitTrace(y, traceLength, noiseArray);

		long t4 = System.currentTimeMillis();

		// ������ ��� � ��������� ������ -- FIXME - �������(?)
//		for (int i = 0; i < ep.length; i++)
//			ep[i].setDefaultThreshold(bs, bellcoreTraces);

		ModelTraceManager mtm = new ModelTraceManager(se, mf, deltaX);

		long t5 = System.currentTimeMillis();
		// ������ ��������� ������, ���� ������ ��� �����
		// FIXME: testing...
		if (bellcoreTraces != null)
			updateMTMThresholdsByBSMap(mtm, bellcoreTraces);
		// @todo: �������� ����� � ������� - � �� DX, � �� DY

		long t6 = System.currentTimeMillis();
		System.out.println("makeAnalysis: getDataAndLength: " + (t1-t0) + "; noiseArray:" + (t2-t1)
			+ "; IA: " + (t3-t2) + "; fit: " + (t4-t3)
			+ "; makeMTM: " + (t5-t4)
			+ "; updThresh: " + (t6-t5)
			);

		return mtm;
	}

	/**
	 * ��������� ������ ������� ModelTraceManager ���, ����� ��� �������� ��� �/�
	 * �� ��������� ������.
	 * @param mtm
	 * @param bellcoreTraces ����� �/�
	 */
	private static void updateMTMThresholdsByBSMap(ModelTraceManager mtm, Map bellcoreTraces)
	{
		// ���������� ������� � ������ �������
		double[] yBase = mtm.getModelTrace().getYArray();
		double[] yMax = new double[yBase.length];
		double[] yMin = new double[yBase.length];
		System.arraycopy(yBase, 0, yMax, 0, yBase.length);
		System.arraycopy(yBase, 0, yMin, 0, yBase.length);
		for (Iterator it = bellcoreTraces.keySet().iterator(); it.hasNext(); )
		{
			BellcoreStructure bs = (BellcoreStructure )(bellcoreTraces.get(it.next()));
			double[] y = bs.getTraceData();
			ReflectogramMath.updateMaxArray(yMax, y);
			ReflectogramMath.updateMinArray(yMin, y);
		}
		// ������������ ������ �� ���� ��������
		mtm.updateUpperThreshToContain(yMax);
		mtm.updateLowerThreshToContain(yMin);
	}

	/**
	 * @deprecated
	 */
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
	
	/**
	 * See specification of {@link #nCalcTraceLength(double[])}
	 * @param y ��������������
	 * @return ����� �� ����� �������
	 */
	public static int calcTraceLength(double[] y)
	{
		return nCalcTraceLength(y);
	}
}
