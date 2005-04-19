/*
 * $Id: CoreAnalysisManager.java,v 1.44 2005/04/19 13:38:03 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.44 $, $Date: 2005/04/19 13:38:03 $
 * @module
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;

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
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse4(
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
	 * �� ��� ��� ����� ����� ����� ������� � native-���.
	 * @param y ������� ��������������
	 * @return ����� �� "������� ����" ��� �� ������� ��������
	 */
	private static native int nCalcTraceLength(double[] y);

	/**
	 * ��������� ������� (��� ������) DX- � DY-������ �������, ���,
	 * ����� ��������� ������ ��������� ��� (��� ���) ��������� ������.
	 * ���������� ������ ������� ����� �������� �� ��������� ��������
	 * ������, � ��������. ��� ������� ������ �������� � ������� ������:
	 * � ��������� ������� ������� DX-��������� ������ �� ��������� ������
	 * ������, � ��������. ����� ����, ���������� ������ ������� �����
	 * ������������� ��������� ������� ������, � ��������. ��� �����������
	 * ������ ��� ��������� ���� ���������� �������. (������� ����������
	 * ��������� ������ ������� � ������ DX ������).
	 * @param yBase ������� ������ ������������ �������
	 * @param yCover ������� ������, ������� ������ ��������� ������ �������
	 * @param thDX ������ ���������� DX-������� �������
	 * @param thDY ������ ���������� DY-������� �������
	 * @param softKeyToUpdate Thresh.SOFT_UP ��� ���������� ������� ������, Thresh.SOFT_DOWN - ��� ������
	 * @param hardKeyToUpdate Thresh.HARD_UP ��� ���������� ������� ������, Thresh.HARD_DOWN - ��� ������
	 */
	private static native void nExtendThreshToCoverCurve(
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

	public static ReliabilitySimpleReflectogramEventImpl[] createSimpleEvents(
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
		return analyse4(y, deltaX,
			minLevel, minWeld, minConnector, noiseFactor,
			reflSize, nReflSize,
			0, null); // FIXME
		//traceLength, noiseArray);
	}

	/**
	 * �������� �/�
	 * @param y ������
	 * @param traceLength �����, ������� ���� �������������
	 * @param noiseArray ��� (1 sigma?), �������� �� ����� traceLength (not null)
	 * @return mf ������������ ������
	 */
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
	 * �������� ���������� � ������������ �������������
	 * see {@link TracesAverages}
	 * @param bsColl ������� ������������ �/�
	 * @param needNoiseInfo ����� �� ���������� � ����
	 * @param needMFInfo ����� �� ������ ���./����. ������������ ������ 
	 * @param needBSInfo ����� �� ���. � ���������� bs
	 * @return ��������� ���� TracesAverages � ������������ ������ ����
	 * general Info � ����������� ������������� �����  
	 * @throws IncompatibleTracesException ���� needBSInfo, ��
	 * ��������������� ��������� ������� bs �����������.
	 */
	public static TracesAverages findTracesAverages(Collection bsColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
			boolean needBSInfo)
	throws IncompatibleTracesException
	{
		// ���������� ����� ������� �/�
		final int N_TRACES = bsColl.size();

		if (N_TRACES == 0)
			throw new IllegalArgumentException("No traces to analyse");

		// ��������� ������� �/�,
		// ���������� ���. ����� � �������� ���.

		boolean isFirst = true;
		
		TracesAverages res = new TracesAverages();
		res.nTraces = N_TRACES;

		for (Iterator it = bsColl.iterator(); it.hasNext(); isFirst = false)
		{
			BellcoreStructure bs = (BellcoreStructure)it.next();

			// general info
		    double[] yCur = bs.getTraceData();
		    int curLength = calcTraceLength(yCur);

		    // general info
		    if (isFirst) {
				res.minTraceLength = curLength;
				res.avY = (double[])yCur.clone(); // double[] array copying
		    }
		    else {
				addDoubleArray(res.avY, yCur, res.avY.length);
				res.minTraceLength = Math.min(res.minTraceLength, curLength);
		    }

		    // NB: noiseData ����� ������� �������� �� traceLength, �������
		    // ��� ������� noiseArray ���������� curLength, � �� traceLength,
		    // ���� ������ �� traceLength ��� �� ���� ������� �������
		    double[] noiseData = needNoiseInfo || needMFInfo
		    	? calcNoiseArray(yCur, curLength)
		    	: null;

	    	if (needNoiseInfo) {
			    if (isFirst)
			    	res.avNoise = noiseData; // cloning is not necessary
			    else
					addDoubleArray(res.avNoise, noiseData, res.minTraceLength);
		    }

		    if (needMFInfo) {
		    	ModelFunction mf = fitTrace(yCur, curLength, noiseData);
		    	double[] yMF = mf.funFillArray(0, 1, res.minTraceLength);
		    	if (isFirst) {
		    		// need to make one more copy, so close once only 
		    		res.minYMF = yMF;
		    		res.maxYMF = (double[])yMF.clone();
		    	}
		    	else {
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
		    	}
		    }

		    if (needBSInfo) {
		    	double deltaXCur = bs.getResolution(); // �����
		    	double pulseWidthCur = bs.getPulsewidth(); // ��
		    	double iorCur = bs.getIOR();
		    	if (isFirst) {
					res.deltaX = deltaXCur;
					res.pulseWidth = pulseWidthCur;
					res.ior = iorCur;
		    	}
		    	else {
					if (deltaXCur != res.deltaX)
						throw new IncompatibleTracesException("different deltaX");
					if (pulseWidthCur != res.pulseWidth)
						throw new IncompatibleTracesException("different pulse width");
					if (iorCur != res.ior)
						throw new IncompatibleTracesException("different IOR");
		    	}
			}
		}

		// convert sum to average for avY
		for (int i = 0; i < res.minTraceLength; i++)
			res.avY[i] /= N_TRACES;

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.minTraceLength; i++)
				res.avNoise[i] /= N_TRACES;

			// make noiseAv
			res.noiseAv = N_TRACES == 1
			? (double[])res.avNoise.clone()
			: calcNoiseArray(res.avY, res.minTraceLength);
		}

		return res;
	}

	/**
	 * �������� ������ ����������� �/�.
	 * � �������� ������� ������ ������� ����������� �/�,
	 * �������� ��������� ������������ ������������ ����������� �/�,
	 * � �������� ����������� ������� - ������������ ������������ ��������
	 * �/�.
	 * @param av ���������� � ������ �/� � ������������ noise � bs ������, 
	 * @param pars ����� ���������� ��� IA: { level, weld, connector, noiseFactor }
	 * @return ��������� ������� � ���� mtae
	 */
	public static ModelTraceAndEventsImpl makeAnalysis(
			TracesAverages av,
			double[] pars
			)
	{
		long t0 = System.currentTimeMillis();

		// ���������� reflSize � nReflSize
        // FIXME: �������� reflSize � nReflSize � �������

		int reflSize = ReflectogramMath.getReflectiveEventSize(av.avY, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				av.avY,
				av.pulseWidth,
				av.ior,
				av.deltaX);

		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;
		reflSize *= 5;

		System.out.println("reflSize="+reflSize+"; nReflSize="+nReflSize);

		long t1 = System.currentTimeMillis();

		// ������������ ������� �� ����������� ������

		ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
				av.avY, av.deltaX,
				pars[0], pars[1], pars[2], pars[3],
				reflSize, nReflSize,
				av.minTraceLength, av.avNoise);

		// ������ �������� ����� �������������� �� ����� ���������� �������
		// (����� ����� �����������)

		int traceLength = rse.length > 0
			? rse[rse.length - 1].getEnd() + 1
			: 0;

		long t2 = System.currentTimeMillis();

		// ��������

		ModelFunction mf = fitTrace(av.avY, traceLength, av.noiseAv);

		long t3 = System.currentTimeMillis();

		ModelTraceAndEventsImpl mtae = new ModelTraceAndEventsImpl(rse, mf, av.deltaX);

		long t4 = System.currentTimeMillis();

		System.out.println("makeAnalysis: "
			+ "getDataAndLengthAndNoise: " + (t1-t0)
			+ "; IA: " + (t2-t1) + "; fit: " + (t3-t2)
			+ "; makeMTM: " + (t4-t3)
			);

		return mtae;
	}

	/**
	 *  note: later it may be turned to native for performance
	 */
	private static void addDoubleArray(double[] acc, double[] input, int length)
	{
		for (int i = 0; i < length; i++)
			acc[i] += input[i]; 
	}

	/**
	 * ������ ������. �������� ���������, ��������� � ����������
	 * �������� ������ IA, fit, calcMutualParameters � ������������ ���. �������.
	 * @todo: declare to throw "invalid parameters exception"
	 * 
	 * @param bs ��������������
	 * @param pars ��������� ������� {@link #makeAnalysis(TracesAverages, double[])}
	 * @return ������ �������
	 */
	public static ModelTraceAndEventsImpl makeAnalysis(
			BellcoreStructure bs,
			double[] pars)
	{
		Set bsSet = new HashSet(1);
		bsSet.add(bs);
		try	{
			// ���������� ��� ����������� ��� ��������� ������������ �/�
			TracesAverages av = findTracesAverages(bsSet, true, false, true);
			return makeAnalysis(av, pars);
		} catch (IncompatibleTracesException e) {
			// ���� �������������� ������ ���������� � ����� �����
			throw new InternalError("Unexpected exception: " + e);
		}
	}

	/**
	 * ������� ��������� MTM �� ������ ������������� � ���������� �������
	 * @param bsColl ��������� ������� �/�.
	 *   ������ ���� �������, � �/� ������ ����� ���������� �����.
	 * @param pars ��������� ������� {@link #makeAnalysis(TracesAverages, double[])}
	 * @return MTM ���������� �������
	 * @throws IllegalArgumentException ���� bsColl ����
	 * @throws IncompatibleTracesException ���� bsColl �������� �/�
	 *   � ������� �������, �����������, ������������� �������� ���
	 *   ����������� �����������.
	 */
	public static ModelTraceManager makeEtalon(Collection bsColl, double[] pars)
	throws IncompatibleTracesException
	{
		TracesAverages av = findTracesAverages(bsColl, true, true, true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av, pars);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		updateMTMThresholdsByBSColl(mtm, av);
		return mtm;
	}

	/**
	 * ��������� ������ MTM �������������� ������
	 * ����. � ���. �������� MF �� TracesAverages
	 * @param mtm ModelTraceManager, ������ �������� ���� ���������
	 * @param av TracesAverages � ������������ ������ MF
	 */
	private static void updateMTMThresholdsByBSColl(ModelTraceManager mtm, TracesAverages av)
	{
		updateMTMThresholdsByRange(mtm, av.maxYMF, av.minYMF);
	}

	/**
	 * ��������� ������ ������� ModelTraceManager ���, ����� ��� ��������
	 * ��������� ������� � ������ ������. ������� ������ ������
	 * ���� �� ���� ������ ����� �� �� ���������� ������� �����������.
	 * @param mtm
	 * @param yMax ������� ������
	 * @param yMin ������ ������
	 */
	private static void updateMTMThresholdsByRange(ModelTraceManager mtm,
			double[] yMax,
			double[] yMin)
	{
		System.err.println("updateMTMThresholdsByRange:"
			+ " mtm.length = " + mtm.getMTAE().getModelTrace().getLength()
			+ " yMax.length = " + yMax.length
			+ " yMin.length = " + yMin.length
			);
		// FIXME: ��-�� ������ ����������, �������������� ����� ����� ��� �� ����-���� "�� ���������" �� yMax/yMin
		mtm.updateUpperThreshToContain(yMax);
		mtm.updateLowerThreshToContain(yMin);
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

	/**
	 * See specification of {@link #nExtendThreshToCoverCurve(double[], double[], ThreshDX[], ThreshDY[], int, int)}
	 */
	public static void extendThreshToCoverCurve(
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate)
	{
		nExtendThreshToCoverCurve(yBase,
			yCover,
			thDX,
			thDY,
			softKeyToUpdate,
			hardKeyToUpdate);
	}
}
