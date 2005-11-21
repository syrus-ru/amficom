/*
 * $Id: CoreAnalysisManager.java,v 1.135 2005/11/21 13:23:34 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.135 $, $Date: 2005/11/21 13:23:34 $
 * @module
 */

import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType.TYPE_LINEBREAK;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity.SEVERITY_HARD;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.QualityComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracePreAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class CoreAnalysisManager
{
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
	private static final double MTM_DY_FACTOR_TR_BASED = 3.0;

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
	 * @param minEnd ���. ��������� ��������� ����� �������
	 * @param minEotLevel ���. ���. ������� ��������� ����� �������
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
	 * @param scaleFactor - ��������� ��� ���������� ������������ ���������.
	 * @return ������ �������
	 */
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse8(
			double[] y,
			double dX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double minEotLevel,
			double noiseFactor,
			int nReflSize,
			double rSACrit,
			int rSSmall,
			int rSBig,
			int traceLength,
			double[] noiseDB,
			double scaleFactor);

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
	 * ��������� ���������� ������� ���� � ��.
	 * @param y ������� ��������������
	 * @param lenght �����, �� ������� ������������ ����������
	 * ������� ����, ��� 0, ���� ��� ����� �� ���� y.length
	 * @return ������ [length > 0 ? length : y.length]
	 */
	private static native double[] nCalcAbsNoiseArray(double[] y, int lenght);

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
	 * @param softKeyToUpdate Thresh.SOFT_UP ��� ���������� ������� ������,
	 *   Thresh.SOFT_DOWN - ��� ������
	 * @param hardKeyToUpdate Thresh.HARD_UP ��� ���������� ������� ������,
	 *   Thresh.HARD_DOWN - ��� ������
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
	protected static double[] calcNoiseArray(double[] y, int length)
	{
		double[] ret = nCalcNoiseArray(y, length);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	//@todo: performance: generata noise and AbsNoise together
	protected static double[] calcAbsNoiseArray(double[] y, int length) {
		return nCalcAbsNoiseArray(y, length);
	}

	/**
	 * ��������� native-���������� dadara.
	 * ��������� ��� ���������� ������������ � � ���������
	 * ������ ������� ������ dadara, ���� ����� ������ public. 
	 */
	public static void loadDLL() {
		try {
			System.loadLibrary("dadara");
		} catch (final UnsatisfiedLinkError ule) {
			Log.errorMessage(ule);
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
	public static double[] calcGaussian(double[] y,
			int maxIndex,
			double[] outParams) {
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

	protected static
	ReliabilitySimpleReflectogramEventImpl[] createSimpleEvents(
			double[] y,
			double deltaX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double minEotLevel,
			double noiseFactor,
			int nReflSize,
			double rSACrit,
			int rSSmall,
			int rSBig,
			int traceLength,
			double[] noiseArray,
			double scaleFactor
			) {
		return analyse8(y, deltaX,
			minLevel, minWeld, minConnector, minEnd, minEotLevel,
			noiseFactor,
			nReflSize, rSACrit, rSSmall, rSBig,
			traceLength, noiseArray, scaleFactor);
	}

	/**
	 * �������� �/�
	 * @param y ������
	 * @param traceLength �����, ������� ���� �������������
	 * @param noiseArray ��� (1 sigma?), �������� �� ����� traceLength, not null
	 * @param sre ������ �������, ���������� � ���. IA,
	 *   ������������ ��� ����� ������ ����������� ������� ����� ��� ���������
	 * @return mf ������������ ������
	 */
	protected static ModelFunction fitTrace(double[] y,
			int traceLength,
			double[] noiseArray,
			SimpleReflectogramEvent[] sre) {
		return ModelFunction.createFitedAsBreakL(
				y, 0, traceLength - 1, noiseArray, sre);
	}

	/**
	 * �������� ����-������ ��� ����� ����-������������� ��������������: <ul>
	 *   <li> ��������� traceData ��������������
	 *   <li> �������� �� �/� ��������� ������������, ����������� ��� �������
	 *     (����������, ����� ��������, ���������� �����������)
	 *   <li> ���������� ����� �������������� �� "����� � ����"
	 *   <li> ���� ���������, ���������� ����� ������� ����
	 * @param trace ������� �������������� ����� ����-����������
	 * @param needNoise true, ���� ������ ����� ��������� ������� ���� 
	 * @return ����������, ���������� ��� �������
	 *   {@link #makeAnalysis(TracePreAnalysis, AnalysisParameters)}
	 */
	protected static TracePreAnalysis makePreAnalysis(PFTrace trace,
			boolean needNoise) {
		TracePreAnalysis res = new TracePreAnalysis();
		// ������ ��������������
		double[] yRaw = trace.getRawTrace();
		double[] yFilteredClone = trace.getFilteredTraceClone();
		res.yTrace = yFilteredClone;
		res.deltaX = trace.getResolution();
		res.ior = trace.getIOR();
		res.pulseWidth = trace.getPulsewidth();
		// ������ ����-�������
		res.traceLength = calcTraceLength(yRaw);
		if (needNoise) {
			double[] noise = calcNoiseArray(yRaw, res.traceLength); // slow
			double[] absNoise = calcAbsNoiseArray(yRaw, res.traceLength); // slow // FIXME: call one of them only
			res.setNoise(noise);
			res.yCorr = calcYCorr(res.yTrace, absNoise, res.traceLength);
		} else {
			res.setNoise(null);
		}
		Log.debugMessage("raw.length=" + trace.getRawTrace().length
				+ " filtered.traceLength=" + trace.getFilteredTrace().length
				+ " res.traceLength=" + res.traceLength, FINER);

		return res;
	}

	// ������������ �������������� �� ������ ����.
	// ��� ��������� ����������� ������������ ��������������� �����.
	// XXX: ���� �� ����������� ����������� ���� ��������������� �����.
	private static double[] calcYCorr(double[]yRaw, double[]absNoise, int len) {
		double[] yCorr = new double[len];
		final double DELTA = 0.0;
		for (int i = 0; i < len; i++) {
			yCorr[i] = yRaw[i];
			if (yCorr[i] < absNoise[i] + DELTA) {
//				System.out.println("yCorr[" + i + "] changed from " + yCorr[i] + " to " + (absNoise[i] + DELTA)); // FIXME
				yCorr[i] = absNoise[i] + DELTA;
			}
		}
		return yCorr;
	}

	/**
	 * �������� ������ � ��������� �� ��������� ����������� ����-�������.
	 * @param tpa ���������� ����-�������
	 *   {@link #makePreAnalysis}
	 * @param ap ����� ���������� ��� IA
	 * @return ��������� ������� � ���� mtae
	 */
	protected static ModelTraceAndEventsImpl makeAnalysis(
			TracePreAnalysis tpa,
			AnalysisParameters ap)
	{
		long t0 = System.nanoTime();

		// ���������� reflSize � nReflSize
		// FIXME: �������� reflSize � nReflSize � �������

//		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
//				tpa.y,
//				tpa.pulseWidth,
//				tpa.ior,
//				tpa.deltaX);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				tpa.pulseWidth,
				tpa.ior,
				tpa.deltaX);

		int rsBig = (int) (tpa.traceLength * ap.getL2rsaBig());
		if (rsBig < nReflSize * ap.getNrs2rsaBig())
			rsBig = (int) (nReflSize * ap.getNrs2rsaBig());

		long t1 = System.nanoTime();

		// ������������ ������� �� ����������� ������

		ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
				tpa.yCorr,
				tpa.deltaX,
				ap.getEventTh(),
				ap.getSpliceTh(),
				ap.getConnectorTh(),
				ap.getEndTh(),
				ap.getLevelEot(),
				ap.getNoiseFactor(),
				nReflSize,
				ap.getRsaCrit(),
				(int)(nReflSize * ap.getNrs2rsaSmall()),
				rsBig,
				tpa.traceLength,
				tpa.avNoise,
				ap.getScaleFactor());

		for (int i = 0; i < rse.length; i++) {
			Log.debugMessage("rse[" + i + "]:" + " " + rse[i], FINEST);
		}

		// ������ �������� ����� �������������� �� ����� ���������� �������
		// (����� ����� �����������)

		int traceLength = rse.length > 0
			? rse[rse.length - 1].getEnd() + 1
			: 0;

		Log.debugMessage("y.length=" + tpa.yTrace.length
				+ " tpa.traceLength=" + tpa.traceLength
				+ " rse.traceLength=" + traceLength, FINER);

		long t2 = System.nanoTime();

		// ��������

		// �������� �� yCorr, � �� tpa.yTrace.

		// --- �����������, ������� ������ ������� ---
		// ������ ����� ���� ����� ����������, ����� ������� � ��� �����:
		// 1. ��������� MTAEI ���������� �� ��� �� �/�, ��� � ����������.
		// 2. MTAEI ����� ����������� �� ������ �����, �� � � ������ ������,
		// � ������� ��� ���� yCorr ������ �� ����� (�.�. ��� ������� �� ����).
		// �� ��� ������ ������� ����� ������,
		// 1. �����?
		// 2. ������ ����� MTAEI ����������� �� ������ (������� �� ���������
		// ������ ����� � ��� �������������� ������ ������� - �� ���
		// �.�. �� ����������), � ComplexInfo ����������� ������
		// ��� �������� MTAEI

		ModelFunction mf = fitTrace(tpa.yCorr, traceLength, tpa.noiseAv, rse);

		long t3 = System.nanoTime();

		// XXX: ���� ����� �� ������ ����������� �/�, �� � ����� �������,
		// ����� �� ������� ���������� rmsDev,maxDev � �.�.
		// �� ������ ������ ��� �� ��������, �.�. ���������� �������
		// ������ �/� ������������ ������ ��� �������, � � ���� maxDev/rmsDev
		// �� ������������.
		ModelTraceAndEventsImpl mtae =
			new ModelTraceAndEventsImpl(rse, mf, tpa.yTrace, tpa.deltaX);

		long t4 = System.nanoTime();

		Log.debugMessage("makeAnalysis: "
				+ "getDataAndLengthAndNoise: " + (t1-t0)/1e6
				+ "; IA: " + (t2-t1)/1e6 + "; fit: " + (t3-t2)/1e6
				+ "; makeMTAE: " + (t4-t3)/1e6,
				FINEST);

		return mtae;
	}

	/**
	 * �������� ���������� � ������������ �������������, �����������
	 * ��� ������������ �������.
	 * see {@link TracesAverages}
	 * @param trColl ������� ������������ �/�
	 *   ����� ����-���������� ({@link PFTrace})
	 * @param needNoiseInfo ����� �� ���������� � ����
	 * @param needMFInfo ����� �� ������ ���./����. ������������ ������
	 * @param ap ��������� �������, ���� null ���� needMFInfo==false
	 * @return TracesAverages, � �������: <ul>
	 *   <li>���� av.avNoise, av.noiseAv ���������, ���� needNoiseInfo
	 *   <li>���� maxYMF, minYMF ���������, ���� needMFInfo
	 *		(� ����� ������ �������� ������ � ��������� ������ �/�
	 *		 � ���������� maxYMF, minYMF �� ������ ������� ����
	 *		 ������������ ������)
	 *   <li>��� ��������� ���� ��������� � ����� ������
	 * </ul>
	 * @throws IncompatibleTracesException ����
	 *   ��������� ������� �/�, ����������� ��� ���������� �������, �����������
	 * @throws IllegalArgumentException ���� ������� ������������ �/� �����
	 */
	protected static TracesAverages findTracesAverages(
			Collection<PFTrace> trColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
			AnalysisParameters ap)
	throws IncompatibleTracesException {
		// ���������� ����� ������� �/�
		final int N_TRACES = trColl.size();

		if (N_TRACES == 0)
			throw new IllegalArgumentException("No traces to analyse");

		// ��������� ������� �/�,
		// ���������� ���. ����� � �������� ���.

		boolean isFirst = true;

		TracesAverages res = new TracesAverages();
		res.nTraces = N_TRACES;

		double[] noiseAcc = null; // needs no initialization

		for (Iterator<PFTrace> it = trColl.iterator(); it.hasNext(); isFirst = false) {
			TracePreAnalysis tpa =
				makePreAnalysis(it.next(), needNoiseInfo);

			if (isFirst) {
				res.av = new TracePreAnalysis(tpa);
			} else {
				res.av.setMinLength(tpa.traceLength);
				addDoubleArray(res.av.yTrace, tpa.yTrace, res.av.traceLength);
				addDoubleArray(res.av.yCorr, tpa.yCorr, res.av.traceLength);
				res.av.checkTracesCompatibility(tpa);
			}

			if (needNoiseInfo) {
				if (isFirst) {
					noiseAcc = tpa.getNoise(); // ������������ �������������, �.�. ����� ��������� ����� � ���� ������� 
				} else {
					addDoubleArray(noiseAcc, tpa.getNoise(), res.av.traceLength);
				}
			}

			if (needMFInfo) {
				// �������� ������ � ����� �� ���� ��������� ������
				ModelTrace mt = makeAnalysis(tpa, ap).getModelTrace();
				double[] yMF = mt.getYArray();
				res.av.setMinLength(mt.getLength()); // ��������, ��� ����� ��������� ������ ����� ������, ��� ��������� tracelength
				if (isFirst) {
					// need to make one more copy, so cloning once only
					res.minYMF = yMF;
					res.maxYMF = yMF.clone();
				} else {
					// �������� �������� res.(min|max)YMF, ��������� �� �������
					// res.av.tracelength, �� ����� �������������� (��� ��������� �����)
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
				}
			}
		}

		// convert sum to average for avY
		for (int i = 0; i < res.av.traceLength; i++) {
			res.av.yTrace[i] /= N_TRACES;
			res.av.yCorr[i] /= N_TRACES;
		}

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.av.traceLength; i++) {
				noiseAcc[i] /= N_TRACES;
			}

			res.av.avNoise = noiseAcc;

			// make noiseAv
			res.av.noiseAv = N_TRACES == 1
				? (double[])noiseAcc.clone() // XXX: ����� �� ������������? ���� ��, ��, ��������, ��� �������� �� ����� ��� ����
				: calcNoiseArray(res.av.yTrace, res.av.traceLength);
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
	 * ��������� ������ ����� ��������������
	 * @param trace ����-������������� ��������������
	 * @param ap ��������� �������
	 * @return ���������� ������� {@link AnalysisResult}
	 */
	public static AnalysisResult performAnalysis(
			PFTrace trace,
			AnalysisParameters ap) {
		TracePreAnalysis tpa = makePreAnalysis(trace, true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(tpa, ap);
		return new AnalysisResult(tpa.yTrace.length, tpa.traceLength, mtae);
	}

	/**
	 * ��������� ���-����������, � ����� ������ ����� ��������������
	 * XXX: �������� deprecated? ������������ � mcm
	 * @param bs �������� �/�
	 * @param ap ��������� �������
	 * @return ���������� ������� {@link AnalysisResult}
	 */
	public static AnalysisResult performAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap) {
		TracePreAnalysis tpa = makePreAnalysis(new PFTrace(bs), true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(tpa, ap);
		return new AnalysisResult(tpa.yTrace.length, tpa.traceLength, mtae);
	}

	/**
	 * ������� ��������� MTM �� ��������� ������ ������������� � ����������
	 * �������.
	 * <ul>
	 * <li> ���� � ������ ������ ���� �/�,
	 *   ������ ������ �� �������� �/� (PFTrace),
	 * <li> ���� ��������� - �� ���� �� mf,
	 * <li> ���� �� ����� - ������� IllegalArgumentException
	 *      (see {@link #findTracesAverages(Collection, boolean, boolean,
	 *        AnalysisParameters)}).
	 * </ul>
	 * @todo ������������ �� ����� Collection{Trace}
	 * ������ Collection{PFTrace}, � �� ��������� ������ ������
	 * @param trColl ��������� ������� �/�.
	 *   ������ ���� �������, � �/� ������ ����� ���������� ���������
	 *   �����������, ������������ � �������
	 * @param ap ��������� �������
	 * @return MTM ���������� �������
	 * @throws IllegalArgumentException ���� ��������� ������� �/� �����
	 *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException ���� ��������� ������� �/�
	 *   �������� �/� � ������� �������, �����������, ������������� ��������
	 *   ��� ����������� ����������� ({@link #findTracesAverages})
	 */
	public static ModelTraceManager makeEtalon(
			Collection<PFTrace> trColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		// FIXME: lot of debug output
		Log.debugMessage("makeEtalon: trColl: " + trColl, Level.FINEST);
		TracesAverages av = findTracesAverages(trColl, true, true, ap);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av.av, ap);
		Log.debugMessage("makeEtalon: initial MTAE: " + mtae, Level.FINEST);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		Log.debugMessage("makeEtalon: initial MTM: " + mtm, Level.FINEST);
		if (trColl.size() > 1) {
			// extend to max dev of _mf_
			Log.debugMessage("makeEtalon: av.maxYMF: " + av.maxYMF.length
					+ ":" + getDoubleArrayHash(av.maxYMF), Level.FINEST);
			Log.debugMessage("makeEtalon: av.minYMF: " + av.minYMF.length
					+ ":" + getDoubleArrayHash(av.minYMF), Level.FINEST);
			mtm.updateThreshToContain(av.maxYMF, av.minYMF, MTM_DY_MARGIN,
					MTM_DY_FACTOR_MF_BASED);
		}
		else {
			// extend to a single curve: original (noisy) _trace_
			mtm.updateThreshToContain(av.av.yCorr, av.av.yCorr, MTM_DY_MARGIN,
					MTM_DY_FACTOR_TR_BASED);
		}
		Log.debugMessage("makeEtalon: rslting MTM: " + mtm, Level.FINEST);
		return mtm;
	}

	private static int getDoubleArrayHash(double[] arr) {
		int result = 17;
		for (int i = 0; i < arr.length; i++) {
			long bits = Double.doubleToLongBits(arr[i]);
			result = 37 * result + (int)(bits ^ (bits >>> 32));
		}
		return result;
	}

	/**
	 * ��������� ��������� MTM �� ��������� ������ ������������� � ����������
	 * �������, ����� �������� ��� ������������� ������.
	 * <p>
	 * XXX ���� �� ������� ������������ �� ����� Collection{Trace}
	 * ������ Collection{PFTrace}, ����� �� ��������� ������ ������,
	 * ������ ����� ��� �����, ����� ����������������� MTM ������ ��������
	 * �/�, ����������� ��� <b>�������</b> ��������� ���������� �������.
	 * ������� ������������ ���������� �������, ��������� � Trace, ����� �����
	 * ������ ����� Trace ������ ���-�� ������������ ������������ AP.
	 * </p>
	 * @param mtm ����������� MTM
	 * @param trColl ��������� ����-������������� �/�
	 * @param ap ���������, � �������� ������������� ��� �/�
	 * @throws IllegalArgumentException ���� trColl ����
	 *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException ���� trColl �������� �/�
	 *   � ������� �������, �����������, ������������� �������� ���
	 *   ����������� ����������� ({@link #findTracesAverages})
	 */
	public static void updateEtalon(ModelTraceManager mtm,
			Collection<PFTrace> trColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException, IllegalArgumentException {
		TracesAverages av = findTracesAverages(trColl, true, true, ap);
		// extend to max dev of _mf_
		mtm.updateThreshToContain(
				av.maxYMF, av.minYMF, MTM_DY_MARGIN, MTM_DY_FACTOR_MF_BASED);
	}

	/**
	 * ������� � ��������� ��������������, ��������� � ������������ ��
	 * ��������� ��������. ������� �/� ������ ���� ���������, �.�.
	 * ����� ���� � �� �� ������ ����������� (����������, ����� �������� � ��.)
	 * @param trColl �������� ������� ��������� �������������
	 * @param av ������� ��������� �������� �� ���� ��������� TracesAverages
	 *   (� ��� �� ����� �� noiseInfo, �� MFInfo)
	 * @return ����� ������� �������������� ����� �������
	 */
	protected static PFTrace getMostTypicalTrace(
			Collection<PFTrace> trColl,
			TracesAverages av) {
		PFTrace nearest = null;
		double bestDistance = 0;
		for (PFTrace tr: trColl) {
			double[] y = tr.getFilteredTrace();
			double distance = ReflectogramComparer.getMaxDeviation(av.av.yCorr,
					y,
					av.av.traceLength);
			if (nearest == null || distance < bestDistance) {
				nearest = tr;
				bestDistance = distance;
			}
		}
		return nearest;
	}

	/**
	 * ������� � ��������� ��������������, ��������� � ������������ ��
	 * ��������� ��������. ������� �/� ������ ���� ���������, �.�.
	 * ����� ���� � �� �� ������ ����������� (����������, ����� �������� � ��.)
	 * @param trColl ������� ��������� ����-������������� �������������
	 * @return ����� ������� �������������� ����� �������
	 * @throws IncompatibleTracesException ���� ������� ��������������
	 * �����������
	 * @throws IllegalArgumentException ���� ������� ������������ �/� �����
	 */
	public static PFTrace getMostTypicalTrace(
			Collection<PFTrace> trColl)
	throws IncompatibleTracesException {
		// ���� ������� ��������� �����, �� � ����� ������� ��� �����
		// ��������� ���������� IllegalArgumentException,
		// �.�. return null �� ����������.
		TracesAverages av = findTracesAverages(trColl, false, false, null);
		return getMostTypicalTrace(trColl, av);
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
	public static double getMedian(double[] y, int iFrom, int iToEx,
			double ratio)
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
	 * See specification of {@link #nCalcTraceLength(double[])}
	 * @param y ��������������
	 * @return ����� �� ����� �������
	 */
	protected static int calcTraceLength(double[] y)
	{
		return nCalcTraceLength(y);
	}

	/**
	 * ������������ � MTM ��� �������� �������.
	 * See specification of {@link #nExtendThreshToCoverCurve(double[],
	 *   double[], ThreshDX[], ThreshDY[], int, int, double)}
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
	 * �������� ������ �������������� �������
	 *   {@link #performAnalysis(PFTrace, AnalysisParameters)},
	 * ����� ���������� �� � �������
	 *   {@link #compareAndMakeAlarms(AnalysisResult, Etalon)}
	 * @param trace ����-������������� {@link PFTrace} ��������������
	 * @param ap ��������� �������
	 * @param breakThresh
	 * @param etMTM
	 * @param anchorer
	 * @return ������ �������
	 */
	@Deprecated
	public static List analyseCompareAndMakeAlarms(PFTrace trace,
			AnalysisParameters ap,
			double breakThresh,
			ModelTraceManager etMTM,
			EventAnchorer anchorer) {
		AnalysisResult ar = performAnalysis(trace, ap);
		Etalon etalon = new Etalon(etMTM, breakThresh, anchorer);
		return compareAndMakeAlarms(ar, etalon);
	}

	/**
	 * ���������� ���������� ������� � ������� �����������
	 * ������ � ��������� MTM, ��������� ������ �������.
	 * ������� ������ ���������� 0 ��� 1 �������.
	 * <p>
	 * ����� ����, ���� � ������� ��������� EventAnchorer,
	 * � ��� ��������� �� ���������� ������,
	 * ���������� ������� ����������� ��������� EventAnchorer �� ������ �������.
	 * <p>
	 * ���������� ����� {@link #compareToEtalon(AnalysisResult, Etalon)}
	 * ����� ������������ ���������� ��������.
	 * <p>
	 * XXX: ����� ��? �� ���-���� � ��������� deprecated
	 * @param ar ���������� �������
	 * @param etalon ��������� �������
	 * @return ������ ���������� ��������������
	 */
	public static List<ReflectogramMismatchImpl> compareAndMakeAlarms(
			AnalysisResult ar,
			Etalon etalon) {
		return compareToEtalon(ar, etalon).getAlarms();
	}

	/**
	 * ���������� ���������� ������� � ��������, �������� ��� ����������
	 * ��������� � ���� {@link EtalonComparison}.
	 * @param ar ���������� �������
	 * @param etalon ��������� �������
	 * @return ��������� ��������� {@link EtalonComparison}
	 */
	public static EtalonComparison compareToEtalon(
			AnalysisResult ar,
			Etalon etalon) {
		// ��������� �������� ������
		final List<ReflectogramMismatchImpl> alarmList =
			new ArrayList<ReflectogramMismatchImpl>();

		// �������� ��������� �������
		ModelTraceManager etMTM = etalon.getMTM();
		double breakThresh = etalon.getMinTraceLevel();

		final ModelTraceAndEventsImpl mtae = ar.getMTAE();
		ModelTrace mt = mtae.getModelTrace();

		// ������ ����� ������� �� �������
		int etMinLength = etMTM.getMTAE().getNEvents() > 0
				? etMTM.getMTAE().getSimpleEvent(
						etMTM.getMTAE().getNEvents() - 1).getBegin()
				: 0; // ���� � ������� ��� �������, ������� ��� ����� �������

		// �� ��������� � ����������� ������� ��������� ����� �/� � ������������ ������ - ��� ���� �� �����
//	  outParameters.put(CODENAME_DADARA_TRACELENGTH, ByteArray.toByteArray(traceLength));
//	  outParameters.put(CODENAME_DARARA_MODELFUNCTION, mf.toByteArray());

		// �������� ���������� ����� �������:
		// (1) �� ������� (x < traceLength) - �� ����� �.�. ���� breakThresh
		// XXX - ���� �� ���� �������������� ������� �/� �� ���������?
		int breakPos = ModelTraceComparer.compareToMinLevel(mt, breakThresh);
		// (2) �� ������� (x >= traceLength) - �� ���� �� � ��� �� ������ EOT ?

		// FIXME: ���� ��� ���������� �������� � ������ mt, � �� ������ �������� ������,
		//    ����� ��������� ������ ������������ ����� ���� �� �� �����������
//		if (breakPos < 0 && ar.getTraceLength() < etMinLength)
//		breakPos = ar.getTraceLength();
		if (breakPos < 0 && mt.getLength() < etMinLength) {
			// ������������� breakPos � ������ ����� �������
			final int nEvents = mtae.getNEvents();
			breakPos = nEvents > 0
				? mtae.getSimpleEvent(nEvents - 1).getBegin() : 0;
		}

		// [fixed] �������� - breakPos �������� ��� ������ �� ����� ���� minTraceLevel,
		// ��� ����� �������� �� ��������� ���������� ���. ���������� �/�
		// ��� ������ �� ������� ������������� �������� (see traces #38, #65)
		// ������ ��������������� ����������� [/fixed]
		if (breakPos >= 0 && breakPos < etMinLength) // ���� ��� ��������� ����� �� ������ EOT
		{
			ReflectogramMismatchImpl alarm = new ReflectogramMismatchImpl();
			alarm.setSeverity(SEVERITY_HARD);
			alarm.setAlarmType(TYPE_LINEBREAK);
			alarm.setCoord(etMTM.fixAlarmPos(breakPos, false)); // ������������ � ������ �������
			alarm.setDeltaX(etMTM.getMTAE().getDeltaX());
			// �������� ��������� ������ := ����� ��������� �/� (�� �� ����� ����� �/�)
			alarm.setEndCoord(Math.min(ar.getDataLength(), etMinLength));

			// ������������� �������� ������ � ��������
			ModelTraceComparer.setAlarmAnchors(alarm, etalon);

			// XXX - ���� �� ������ ���� �������� ���������, �� ��������� ����� ��������
			// ��, � ����� ������ �� ���� ������������ HARD ������?

			alarmList.add(alarm);

			final QualityComparer qcomp =
				new QualityComparer(mtae, etMTM, null, true);
			return new EtalonComparison() {
				public List<ReflectogramMismatchImpl> getAlarms() {
					return alarmList;
				}
				public ReflectometryEvaluationOverallResult getOverallResult() {
					return qcomp;
				}
				public EvaluationPerEventResult getPerEventResult() {
					return qcomp;
				}
			};
		}
		else // ����� �� ���������
		{
			ReliabilitySimpleReflectogramEvent[] arEvents =
				mtae.getSimpleEvents();
			ReliabilitySimpleReflectogramEvent[] etEvents =
				etMTM.getMTAE().getSimpleEvents();
			SimpleReflectogramEventComparer rcomp =
				new SimpleReflectogramEventComparer(arEvents, etEvents);
			ReflectogramMismatchImpl alarm =
					ModelTraceComparer.compareMTAEToMTM(mtae,
							etMTM, rcomp);

			// ������������ EventAnchorer-�������� ����������� �������
			ModelTraceComparer.createEventAnchor(ar, etalon, rcomp);

			// ������������� �������� ������ � ��������
			if (alarm != null) {
				ModelTraceComparer.setAlarmAnchors(alarm, etalon);
			}

			if (alarm != null) {
				alarmList.add(alarm);
			}

			final QualityComparer qcomp =
				new QualityComparer(mtae, etMTM, rcomp, false);
			return new EtalonComparison() {
				public List<ReflectogramMismatchImpl> getAlarms() {
					return alarmList;
				}
				public ReflectometryEvaluationOverallResult getOverallResult() {
					return qcomp;
				}
				public EvaluationPerEventResult getPerEventResult() {
					return qcomp;
				}
			};
		}
	}
}
