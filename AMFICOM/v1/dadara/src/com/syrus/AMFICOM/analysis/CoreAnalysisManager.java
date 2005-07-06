/*
 * $Id: CoreAnalysisManager.java,v 1.91 2005/07/06 08:11:10 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.91 $, $Date: 2005/07/06 08:11:10 $
 * @module
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracePreAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;
import com.syrus.io.BellcoreStructure;

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
	 * @param minEnd ���. ������� ����� �������
	 * @param noiseFactor ��������� ��� ������ ���� - ��� ������ ���������,
	 *   ��� ������ ���������������� � ������ �������������.
	 *   (1.0 - ����. ����������������, �� ������ 3 �����; �������������
	 *   �������� ������� 1.5 .. 3.0)
	 * @param nReflSize ���. ������ �������. ������� (� ������),
     *   ���������� ���. ������ ��������
     * @param rSACrit ����������� ��������� (��), ���� ������� ��������
     *    ������� ���������� ������������� �������������� �������
     * @param rSSmall ����. ����� (� ������) �������������� �������
     *    � ���������� ����� rSACrit
     * @param rSBig ����. ����� (� ������) �������������� �������
     *    � ���������� ����� rSACrit (������ ���� ������ rSSmall)
	 * @param traceLength ����� �������������� �� ����� �������,
	 * ����� ���� 0, ����� ����� ������� �������������
	 * @param noiseDB ������� ���� �� 3 �����, � ���. ��;
	 * ����� ���� null - ����� ����� ������ �������������
	 * @return ������ �������
	 */
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse6(
			double[] y,
			double dX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double noiseFactor,
			int nReflSize,
            double rSACrit,
            int rSSmall,
            int rSBig,
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
	 * @param dyFactor ����������� ������ DY, �� ����� 1.0
	 */
	private static native void nExtendThreshToCoverCurve(
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate,
			double dyFactor);

	/**
	 * ������ ������ ���� �� ������ ��������������.
	 * @param y ������� ������ �������������� � ��.
	 * @param length ����� �������, > 0
	 * @return ������������� �������� ���� (��) �� ������ 3 ����� � ������
     *  �����, ����� ������� length  
	 */
	public static double[] calcNoiseArray(double[] y, int length)
	{
		double[] ret = nCalcNoiseArray(y, length);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	public static void loadDLL() {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

	static {
		loadDLL();
	}

    /**
     * �������� ������ ���������� y = amplitude * exp(-(x-center)**2 / sigma**2)
     * @param y ������� ������
     * @param maxIndex ��������� �������� ��������� ���������
     * @param outParams ���� �� null, �� ����������� ������������ ���������
     *   outParams[0] = center
     *   outParams[1] = amplitude
     *   outParams[2] = sigma (� sqrt(2) ��� �������, ��� �����������)
     * @return ���������� ������
     */
	public static double[] calcGaussian(double[] y, int maxIndex, double[] outParams) {
		double[] gauss = new double[y.length];

		double maxValue = y[maxIndex];

        int width = 0;
		for (int i = 0; i < y.length; i++)
			if (y[i] > maxValue * 0.6)
				width++;

        double[] d = gauss(y, maxIndex, maxValue, width);

        if (outParams != null) {
            outParams[0] = d[0];
            outParams[1] = d[1];
            outParams[2] = d[2];
        }

        double center = d[0];
		maxValue = d[1];
		double sigmaSquared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = maxValue
					* Math.exp(-(i - center) * (i - center) / sigmaSquared);

		return gauss;
	}

    public static double[] calcGaussian(double[] y, int maxIndex) {
        return calcGaussian(y, maxIndex, null);
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
			double minEnd,
			double noiseFactor,
			int reflSize,
			int nReflSize,
			int traceLength,
			double[] noiseArray)
	{
		return analyse6(y, deltaX,
			minLevel, minWeld, minConnector, minEnd, noiseFactor,
			nReflSize, 0.5, (int)(nReflSize * 1.5), reflSize, 
			traceLength, noiseArray);
	}

	/**
	 * �������� �/�
	 * @param y ������
	 * @param traceLength �����, ������� ���� �������������
	 * @param noiseArray ��� (1 sigma?), �������� �� ����� traceLength (not null)
	 * @param sre ������ �������, ���������� � ���. IA,
     *   ������������ ��� ����� ������ ����������� ������� ����� ��� ���������
	 * @return mf ������������ ������
	 */
	public static ModelFunction fitTrace(double[] y, int traceLength, double[] noiseArray, SimpleReflectogramEvent[] sre)
	{
		return ModelFunction.createFitedAsBreakL(y, 0, traceLength, noiseArray, sre);
	}

	/**
	 * ��������� ��������� ��� �������.
	 * @param ap ����������� ���������
	 * @return true, ���� ����� ���������, false, ���� ����� �����������
	 */
	public static boolean checkAnalysisParameters(AnalysisParameters ap)
	{
        final double MIN_MIN_THRESHOLD = 0.001; // FIXME: debug: MIN_MIN_THRESHOLD should be 0.01 or 0.005 or 0.001 (?)
		if (ap.getMinThreshold() < MIN_MIN_THRESHOLD)
			return false;
		if (ap.getMinSplice() < ap.getMinThreshold())
			return false;
		if (ap.getMinConnector() < ap.getMinSplice())
			return false;
        if (ap.getMinEnd() < ap.getMinConnector())
            return false;
		return true;
	}

    /**
     * �������� ����-������ ��� ����� ��������������: <ul>
     *   <li> ��������� traceData ��������������
     *   <li> �������� �� �/� ��������� ������������, ����������� ��� �������
     *     (����������, ����� ��������, ���������� �����������)
     *   <li> ���������� ����� �������������� �� "����� � ����"
     *   <li> ���� ���������, ���������� ����� ������� ����
     * @param bs ������� ��������������
     * @param needNoise true, ���� ������ ����� ��������� ������� ���� 
     * @return ����������, ���������� ��� �������
     *   {@link #makeAnalysis(TracePreAnalysis, AnalysisParameters)}
     */
    public static TracePreAnalysis makePreAnalysis(BellcoreStructure bs,
            boolean needNoise) {
        TracePreAnalysis res = new TracePreAnalysis();
        // ������ ��������������
        double[] y = bs.getTraceData(); // rather slow
        res.y = y;
        res.deltaX = bs.getResolution();
        res.ior = bs.getIOR();
        res.pulseWidth = bs.getPulsewidth();
        // ������ �������
        res.traceLength = calcTraceLength(y);
        if (needNoise) {
            double[] noise = calcNoiseArray(y, res.traceLength); // slow
            res.setNoise(noise);
        } else {
            res.setNoise(null);
        }
        return res;
    }

    /**
     * �������� ������ � ��������� �� ��������� ����������� ����-�������.
     * @param tpa ���������� ����-�������
     *   {@link #makePreAnalysis(BellcoreStructure, boolean)}
     * @param ap ����� ���������� ��� IA
     * @return ��������� ������� � ���� mtae
     */
    public static ModelTraceAndEventsImpl makeAnalysis(
            TracePreAnalysis tpa,
            AnalysisParameters ap)
    {
        long t0 = System.currentTimeMillis();

        // ���������� reflSize � nReflSize
        // FIXME: �������� reflSize � nReflSize � �������

        int reflSize = ReflectogramMath.getReflectiveEventSize(tpa.y, 0.5);
        int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
                tpa.y,
                tpa.pulseWidth,
                tpa.ior,
                tpa.deltaX);

        reflSize = tpa.traceLength / 10; // FIXME!
        if (reflSize < nReflSize * 10)
            reflSize = nReflSize * 10; // FIXME!

        long t1 = System.currentTimeMillis();

        // ������������ ������� �� ����������� ������

        ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
                tpa.y, tpa.deltaX,
                ap.getMinThreshold(), // XXX: 0.010
                ap.getMinSplice(),
                ap.getMinConnector(),
                ap.getMinEnd(),
                ap.getNoiseFactor(),
                reflSize, nReflSize,
                tpa.traceLength, tpa.avNoise);

        // FIX//ME: debug output of IA results
//      for (int i = 0; i < rse.length; i++)
//          System.out.println("rse[" + i + "]:"
//              + " " + rse[i].toString());

        // ������ �������� ����� �������������� �� ����� ���������� �������
        // (����� ����� �����������)

        int traceLength = rse.length > 0
            ? rse[rse.length - 1].getEnd() + 1
            : 0;

        long t2 = System.currentTimeMillis();

        // ��������

        ModelFunction mf = fitTrace(tpa.y, traceLength, tpa.noiseAv, rse);

        long t3 = System.currentTimeMillis();

        double[] yTypical = tpa.y; // FIXME: ���� ����� ����, ����� ������� �/�, � �� �����������, �.�. ����� �� ���� ������ ����� ��������� rms

        ModelTraceAndEventsImpl mtae =
            new ModelTraceAndEventsImpl(rse, mf, yTypical, tpa.deltaX);

        long t4 = System.currentTimeMillis();

        // FIXME: debug output of analysis timing
//      System.out.println("makeAnalysis: "
//          + "getDataAndLengthAndNoise: " + (t1-t0)
//          + "; IA: " + (t2-t1) + "; fit: " + (t3-t2)
//          + "; makeMTAE: " + (t4-t3)
//          );

        return mtae;
    }

	/**
	 * �������� ���������� � ������������ �������������, �����������
     * ��� ������������ �������.
	 * see {@link TracesAverages}
	 * @param bsColl ������� ������������ �/� ({@link BellcoreStructure})
	 * @param needNoiseInfo ����� �� ���������� � ����
	 * @param needMFInfo ����� �� ������ ���./����. ������������ ������
     * @param ap ��������� �������, ���� null ���� needMFInfo==false
	 * @return TracesAverages, � �������: <ul>
     *   <li>���� av.avNoise, av.noiseAv ���������, ���� needNoiseInfo
     *   <li>���� maxYMF, minYMF ���������, ���� needMFInfo
     *        (� ����� ������ �������� ������ � ��������� ������ �/�
     *         � ���������� maxYMF, minYMF �� ������ ������� ����
     *         ������������ ������)
     *   <li>��� ��������� ���� ��������� � ����� ������
     * </ul>
	 * @throws IncompatibleTracesException ����
     *   ��������� ������� bs, ����������� ��� ���������� �������, �����������
	 * @throws IllegalArgumentException ���� ������� ������������ �/� �����
	 */
	public static TracesAverages findTracesAverages(Collection bsColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
            AnalysisParameters ap)
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

        double[] noiseAcc = null; // needs no initialization

		for (Iterator it = bsColl.iterator(); it.hasNext(); isFirst = false)
		{
            TracePreAnalysis tpa =
                makePreAnalysis((BellcoreStructure)it.next(), needNoiseInfo);

		    if (isFirst) {
                double[] y = (double[])tpa.y.clone(); // double[] array copying
                res.av = new TracePreAnalysis(tpa, y);
		    }
		    else {
				res.av.setMinLength(tpa.traceLength);
				addDoubleArray(res.av.y, tpa.y, res.av.traceLength);
                res.av.checkTracesCompatibility(tpa);
		    }

	    	if (needNoiseInfo) {
			    if (isFirst)
			    	noiseAcc = tpa.getNoise(); // ������������ �������������, �.�. ����� ��������� ����� � ���� ������� 
			    else
					addDoubleArray(noiseAcc, tpa.getNoise(), res.av.traceLength);
		    }

		    if (needMFInfo) {
                // �������� ������ � ����� �� ���� ��������� ������
		    	ModelTrace mt = makeAnalysis(tpa, ap).getModelTrace();
		    	double[] yMF = mt.getYArray();
                res.av.setMinLength(mt.getLength()); // ��������, ��� ����� ��������� ������ ����� ������, ��� ��������� tracelength
		    	if (isFirst) {
		    		// need to make one more copy, so cloning once only
		    		res.minYMF = yMF;
		    		res.maxYMF = (double[])yMF.clone();
		    	}
		    	else {
                    // �������� �������� res.(min|max)YMF, ��������� �� �������
                    // res.av.tracelength, �� ����� �������������� (��� ��������� �����)
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
		    	}
		    }
		}

		// convert sum to average for avY
		for (int i = 0; i < res.av.traceLength; i++)
			res.av.y[i] /= N_TRACES;

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.av.traceLength; i++)
				noiseAcc[i] /= N_TRACES;

            res.av.avNoise = noiseAcc;

            // make noiseAv
			res.av.noiseAv = N_TRACES == 1
			    ? (double[])noiseAcc.clone() // XXX: ����� �� ������������? ���� ��, ��, ��������, ��� �������� �� ����� ��� ����
			    : calcNoiseArray(res.av.y, res.av.traceLength);
		}

        // XXX: � ��������, ����� �� ������� �������� �������� ��� ��������
        // res, ������� ������� res.av.traceLength, ��� ����, �����
        // �� ������� � ����� ������������ ���� ������. ������, ���������
        // ��� ������� ��������� ������ �����������, �
        // ������������� ���� ������ �������������� ������ � ���� ������,
        // �� ����� �� ������.

        return res;
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
	 * @todo declare to throw "invalid parameters exception"
	 * @deprecated use {@link #performAnalysis(BellcoreStructure,
	 *     AnalysisParameters)}
	 *   .{@link AnalysisResult#getMTAE()}
	 * @param bs ��������������
	 * @param ap ��������� �������
	 * @return ������ �������
	 */
	@Deprecated
	public static ModelTraceAndEventsImpl makeAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap)
	{
        TracePreAnalysis tpa = makePreAnalysis(bs, true);
        return makeAnalysis(tpa, ap);
	}

	/**
	 * ��������� ������ ����� ��������������
	 * @param bs ��������������
	 * @param ap ��������� �������
	 * @return ���������� ������� {@link AnalysisResult}
	 */
	public static AnalysisResult performAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap) {
        TracePreAnalysis tpa = makePreAnalysis(bs, true);
        ModelTraceAndEventsImpl mtae = makeAnalysis(tpa, ap);
        return new AnalysisResult(tpa.y.length, tpa.traceLength, mtae);
	}

	/**
	 * ���������� ��������� ������ (��) ������������ DY-�������
	 */
	private static final double MTM_DY_MARGIN = 0.03;
	/**
	 * ������ ������ (����) DY-�������, ������������ �� ������ (2 � �����) �.�.
	 */
	private static final double MTM_DY_FACTOR_MF_BASED = 1.2;
	/**
	 * ������ ������ (����) DY-�������, ������������ �� ������ ����� �/� � �� �.�.
	 */
	private static final double MTM_DY_FACTOR_BS_BASED = 3.0;

	/**
	 * ������� ��������� MTM �� ��������� ������ ������������� � ����������
	 * �������.
	 * <ul>
	 * <li> ���� � ������ ������ ���� �/�, ������ ������ �� �� bs,
	 * <li> ���� ��������� - �� ���� �� mf,
	 * <li> ���� �� ����� - ������� IllegalArgumentException
	 *      (see {@link #findTracesAverages(Collection, boolean, boolean, AnalysisParameters)}).
	 * </ul>
	 * @param bsColl ��������� ������� �/�.
	 *   ������ ���� �������, � �/� ������ ����� ���������� ���������
     *   �����������, ������������ � �������
	 * @param ap ��������� �������
	 * @return MTM ���������� �������
	 * @throws IllegalArgumentException ���� bsColl ����
     *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException ���� bsColl �������� �/�
	 *   � ������� �������, �����������, ������������� �������� ���
	 *   ����������� ����������� ({@link #findTracesAverages})
	 */
	public static ModelTraceManager makeEtalon(Collection bsColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		TracesAverages av = findTracesAverages(bsColl, true, true, ap);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av.av, ap);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		if (bsColl.size() > 1) {
			// extend to max dev of _mf_
			mtm.updateThreshToContain(av.maxYMF, av.minYMF, MTM_DY_MARGIN, MTM_DY_FACTOR_MF_BASED); 
		}
		else {
			// extend to a single curve: original (noisy) _bs_
			mtm.updateThreshToContain(av.av.y, av.av.y, MTM_DY_MARGIN, MTM_DY_FACTOR_BS_BASED);
		}
		return mtm;
	}

    /**
     * ������� � ��������� ��������������, ��������� � ������������ ��
     * ��������� ��������. ������� �/� ������ ���� ���������, �.�.
     * ����� ���� � �� �� ������ ����������� (����������, ����� �������� � ��.)
     * @param bsColl ������� ��������� �������������
     * @return ����� ������� �������������� ����� �������
     * @throws IncompatibleTracesException ���� ������� ��������������
     * �����������
     * @throws IllegalArgumentException ���� ������� ������������ �/� �����
     */
    public static BellcoreStructure getMostTypicalTrace(Collection bsColl)
    throws IncompatibleTracesException
    {
        TracesAverages av = findTracesAverages(bsColl, false, false, null);

        // ���� ������� ��������� �����, �� � ����� ������� ��� �����
        // ��������� ���������� IllegalArgumentException,
        // �.�. return null �� ����������.

        BellcoreStructure nearest = null;
        double bestDistance = 0;
        for (Iterator it = bsColl.iterator(); it.hasNext();)
        {
            BellcoreStructure bs = (BellcoreStructure)it.next();
            double[] yBS = bs.getTraceData();
            double distance = ReflectogramComparer.getMaxDeviation(av.av.y,
                    yBS,
                    av.av.traceLength);
            if (nearest == null || distance < bestDistance)
            {
                nearest = bs;
                bestDistance = distance;
            }
        }
        return nearest;
    }

	public static double getMedian(double[] y, int pos)
	{
        if (pos < 0 || pos >= y.length)
            throw new IllegalArgumentException("Invalid pos for getMedian");
		return nMedian(y, pos);
	}
    /**
     * @throws IllegalArgumentException is possible if ratio is outside [0,1]
     */
    public static double getMedian(double[] y, int iFrom, int iToEx, double ratio)
    {
        double[] temp = new double[iToEx - iFrom];
        System.arraycopy(y, iFrom, temp, 0, temp.length);
        return nMedian(temp, (int)(temp.length * ratio));
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
		final int X0 = evBegin;
		final int N = evEnd - evBegin + 1;
		double[] arr = mf.funFillArray(X0, 1.0, N);
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
	 * See specification of {@link #nExtendThreshToCoverCurve(double[], double[], ThreshDX[], ThreshDY[], int, int, double)}
	 */
	public static void extendThreshToCoverCurve(
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate,
			double dyFactor)
	{
		nExtendThreshToCoverCurve(yBase,
			yCover,
			thDX,
			thDY,
			softKeyToUpdate,
			hardKeyToUpdate,
			dyFactor);
	}

	/**
     * �������� ������ �������������� ������� {@link #performAnalysis(BellcoreStructure, AnalysisParameters)},
     * ����� ���������� �� � ������� {@link #compareAndMakeAlarms(AnalysisResult, Etalon)}
	 * @param bs ��������������
	 * @param ap ��������� �������
	 * @param breakThresh
	 * @param etMTM
	 * @param anchorer
	 * @return ������ �������
	 */
    public static List analyseCompareAndMakeAlarms(BellcoreStructure bs,
    		AnalysisParameters ap,
            double breakThresh,
            ModelTraceManager etMTM,
            EventAnchorer anchorer) {
    	AnalysisResult ar = performAnalysis(bs, ap);
    	Etalon etalon = new Etalon(etMTM, breakThresh, anchorer);
    	return compareAndMakeAlarms(ar, etalon);
    }

    /**
     * ���������� ���������� ������� � ������� �����������
     * ������ � ��������� MTM, ��������� ������ �������.
     * ������� ������ ���������� 0 ��� 1 �������.
     * <p>
     * @todo ����� ����, ���� � ������� ��������� EventAnchorer,
     * � ��� ��������� �� ���������� ������,
     * ���������� ������� ����������� ��������� EventAnchorer �� ������ �������.
     * @param ar ���������� �������
     * @param etalon ��������� �������
     */
    public static List compareAndMakeAlarms(AnalysisResult ar,
            Etalon etalon) {
        // ��������� �������� ������
        List alarmList = new ArrayList();

        // �������� ��������� �������
        ModelTraceManager etMTM = etalon.getMTM();
        double breakThresh = etalon.getMinTraceLevel();

        ModelTrace mt = ar.getMTAE().getModelTrace();

        // ������ ����� ������� �� ������� 
        int etMinLength = etMTM.getMTAE().getSimpleEvent(
                etMTM.getMTAE().getNEvents() - 1).getBegin();

        // �� ��������� � ����������� ������� ��������� ����� �/� � ������������ ������ - ��� ���� �� �����
//      outParameters.put(CODENAME_DADARA_TRACELENGTH, ByteArray.toByteArray(traceLength));
//      outParameters.put(CODENAME_DARARA_MODELFUNCTION, mf.toByteArray());

        // �������� ���������� ����� �������:
        // (1) �� ������� �� ����� � ��� (x < traceLength) - �� ����� �.�. ���� ������ breakThresh
        // XXX - ���� �� ���� �������������� ������� �/� �� ���������?
        int breakPos = ModelTraceComparer.compareToMinLevel(mt, breakThresh);
        // (2) �� ������� ���� (x >= traceLength) - �� ���� �� � ��� �� ������ EOT ?
        if (breakPos < 0 && ar.getTraceLength() < etMinLength)
            breakPos = ar.getTraceLength();

        // �������� - breakPos �������� ��� ������ �� ����� ���� minTraceLevel, ��� ����� �������� �� ��������� ���������� ���. ���������� �/� ��� ������ �� ������� ������������� �������� (see traces #38, #65)
        if (breakPos >= 0 && breakPos < etMinLength) // ���� ��� ��������� ����� �� ������ EOT
        {
            ReflectogramAlarm alarm = new ReflectogramAlarm();
            alarm.level = ReflectogramAlarm.LEVEL_HARD;
            alarm.alarmType = ReflectogramAlarm.TYPE_LINEBREAK;
            alarm.pointCoord = etMTM.fixAlarmPos(breakPos, false); // ������������ � ������ �������
            alarm.deltaX = etMTM.getMTAE().getDeltaX();
            // �������� ��������� ������ := ����� ��������� �/� (�� �� ����� ����� �/�)
            alarm.endPointCoord = Math.min(ar.getDataLength(), etMinLength);

            // ������������� �������� ������ � ��������
            ModelTraceComparer.setAlarmAnchors(alarm, etalon);

            // XXX - ���� �� ������ ���� �������� ���������, �� ��������� ����� ��������
            // ��, � ����� ������ �� ���� ������������ HARD ������?

            alarmList.add(alarm);
        }
        else // ����� �� ���������
        {
            ReflectogramAlarm alarm =
            		ModelTraceComparer.compareMTAEToMTM(ar.getMTAE(), etMTM);

            // ������������ EventAnchorer-�������� ����������� �������
            ModelTraceComparer.createEventAnchor(ar, etalon);

            // ������������� �������� ������ � ��������
            if (alarm != null) {
            	ModelTraceComparer.setAlarmAnchors(alarm, etalon);
            }

            if (alarm != null) {
                alarmList.add(alarm);
            }
        }
        return alarmList;
    }
}
