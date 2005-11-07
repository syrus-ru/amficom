package com.syrus.AMFICOM.analysis.test;
/*-
 * $Id: CoreAnalysisManagerTestCase.java,v 1.9 2005/11/07 11:21:32 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.io.BellcoreCreator;
import com.syrus.io.DataFormatException;
import com.syrus.io.SignatureMismatchException;

public class CoreAnalysisManagerTestCase extends TestCase {
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CoreAnalysisManagerTestCase.class);
	}

	private static final String TEST_FNAME = "/traces/fail.sor";
	//private static final String TEST_FNAME = "test/ref/rg0065.ref";
	private static PFTrace loadTestTrace() {
		File file = new File(TEST_FNAME); // XXX
		return new PFTrace(FileOpenCommand.readTraceFromFile(file));
	}
	private static final AnalysisParameters defaultAP;

	static {
		try {
			defaultAP = new AnalysisParameters("0.001;0.01;0.5;3.0;1.3;");
		} catch (InvalidAnalysisParametersException e) {
			throw new InternalError("init defaultAP: InvalidAnalysisParametersException");
		}
	}

	/*
	 * Class under test for double getMedian(double[])
	 */
	public final void testGetMedianDoubleArray() {
		// simple test
		double[] arr = new double[] { 4, 2, 1, 3, 0, 5, 6 };
		assertEquals(CoreAnalysisManager.getMedian(arr), 3, 1e-15);

		//assertEquals(0, CoreAnalysisManager.getMedian(new double[] {}), 1e-15);
	}

	private static final void printEventList(SimpleReflectogramEvent[] re) {
		System.out.println("NEvents=" + re.length);
		for (int i = 0; i < re.length; i++) {
			System.out.println(re[i].toString());
		}
	}

	public final void testAnalysisNotCrush() {
		PFTrace trace = loadTestTrace();
		SimpleReflectogramEvent re[] = CoreAnalysisManager.
				performAnalysis(trace, defaultAP).getMTAE().getSimpleEvents();
		printEventList(re);
	}

	// checks and returns number of bytes of byte[]
	public static int checkMTMReadability(PFTrace bs, boolean verbose)
	throws IncompatibleTracesException, SignatureMismatchException, IOException {
		// make etalon
		Collection<PFTrace> trColl =
			new ArrayList<PFTrace>();
		trColl.add(bs);
		ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(trColl, defaultAP);

		// save to byte[]
		byte[] mtmBytes = DataStreamableUtil.writeDataStreamableToBA(mtm);
		if (verbose) {
			byte[] mtaeBytes = DataStreamableUtil.writeDataStreamableToBA(
					mtm.getMTAE());
			System.out.println("MTAE bytes = " + mtaeBytes.length);
			System.out.println("MTM  bytes = " + mtmBytes.length);
			File f = new File("MTAEdata.tmp");
			f.delete();
			f.createNewFile();
			FileOutputStream os = new FileOutputStream(f);
			os.write(mtaeBytes);
			os.close();
		}

		// restore
		ByteArrayInputStream bais = new ByteArrayInputStream(mtmBytes);
		DataInputStream dis = new DataInputStream(bais);
		ModelTraceManager mtm2 = (ModelTraceManager) ModelTraceManager.getReader().readFromDIS(dis);

		// ensure there are no bytes left
		try {
			dis.readByte();
			assertFalse("There are extra bytes", true);
		} catch (EOFException e) {
			// this is an expected thing
		}

		if (verbose) {
			ReflectogramMismatchImpl res = ModelTraceComparer.compareMTAEToMTM(mtm.getMTAE(), mtm);
			System.out.println("pre-compare alarm: " + res);
			ReflectogramMismatchImpl res2 = ModelTraceComparer.compareMTAEToMTM(mtm2.getMTAE(), mtm);
			System.out.println("compare alarm: " + res2);
			assertTrue(res == null); // mtm should cover itself
			assertTrue(res2 == null); // restored mtae should be the same
		}

		return mtmBytes.length;
	}

	public final void testMTMReadability()
	throws IncompatibleTracesException, SignatureMismatchException, IOException {
		// load trace
		PFTrace trace = loadTestTrace();
		// test
		checkMTMReadability(trace, true);
	}

	private static double xRand(int pos) {
		double v = ((pos * 27) % 37) / 36.0;
		return v - 0.5;
	}
	private static double[] generateTestBellcoreYArray(int len, int[] connPos) {
		final int N = len;
		double noise = 10.0;
		double s2n = 5.0;
		double resolution = 1.0; // m
		double att = 0.22; // db/km
		double y0 = noise + s2n + N * 0.5 * att / 1e3 * resolution;
		final int NCONN = 3;
		double[] connAmpl = {15, 10, 15};
		int connLen = 50;
		double[] ret = new double[N];
		for (int i = 0; i < ret.length; i++) {
			double yc = y0 - i * att / 1e3 * resolution;
			for (int j = 0; j < NCONN; j++) {
				if (i > connPos[j] && i < connPos[j] + connLen) {
					yc += connAmpl[j];
				}
			}
			double tmp = Math.pow(10.0, noise / 5.0) * xRand(i)
				+ Math.pow(10.0, yc / 5.0);
			ret[i] = 5.0 * Math.log10(tmp > 1.0 ? tmp : 1);
		}
		return ret;
	}

	private static PFTrace generateTestTrace(int len, int[] connPos,
			boolean dumpToFile) {
		double[] y = generateTestBellcoreYArray(len, connPos);
		if (dumpToFile) {
			PrintStream str;
			try {
				str = new PrintStream("testBS.tmp");
			} catch (FileNotFoundException e) {
				throw new InternalError(e.toString());
			}
			for (int i = 0; i < y.length; i++) {
				str.println(i + " " + y[i]);
			}
		}
		return new PFTrace(new BellcoreCreator(y, 1.0, 50).getBS());
	}

	private static ReflectogramMismatchImpl getFirstMismatch(List list) {
		Iterator it = list.iterator();
		if (it.hasNext())
			return (ReflectogramMismatchImpl) it.next();
		else
			return null;
	}

	// проверка сравнения масок и определения события, обусловившего аларм
	public final void testTraceComparison()
	throws IncompatibleTracesException, DataFormatException {

		// Готовим рефлектограммы и эталон

		System.out.println("testTraceComparison():");
		final int N = 10000;
		int dist1 = N / 3;
		int dist1m4 = N / 3 - 4;
		int dist1m5 = N / 3 - 5;
		int dist1m6 = N / 3 - 6;
		int[] connPos1		= {0, dist1,   N / 3 * 2};
		int[] connPos1m4	= {0, dist1m4, N / 3 * 2};
		int[] connPos1m5	= {0, dist1m5, N / 3 * 2};
		int[] connPos1m6	= {0, dist1m6, N / 3 * 2};
		System.out.println("generating trace...");
		PFTrace trace = generateTestTrace(N, connPos1, true);
		System.out.println("Analysing trace...");
		Collection<PFTrace> trColl =
			new ArrayList<PFTrace>();
		trColl.add(trace);
		ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(trColl, defaultAP);
		double breakThresh = ReflectogramMath.getArrayMin(trace.getFilteredTraceClone());
		printEventList(mtm.getMTAE().getSimpleEvents());

		ReflectogramMismatchImpl res;

		res = ModelTraceComparer.compareMTAEToMTM(mtm.getMTAE(), mtm);
		System.out.println("compare mtae: " + res);
		assertTrue(res == null);

		// Сверяем исходную р/г

		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				trace,
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare bs: " + res);
		assertTrue(res == null); // аларма быть не должно

		// Сверяем наличие привязки при уходе начала коннектора влево в пределах маски

		PFTrace trace1 = generateTestTrace(N, connPos1m4, false); 
		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				trace1,
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // должен быть обнаружен аларм
		assertTrue(res.getEndCoord() >= res.getCoord()); // корректность аларма
		assertTrue(res.getAlarmType() == ReflectogramMismatch.AlarmType.TYPE_OUTOFMASK); // тип аларма
		assertTrue(res.getCoord() + "==" + dist1, res.getCoord() == dist1); // должна сработать привязка

		// Сверяем наличие привязки при уходе начала коннектора влево за пределами маски

		PFTrace trace2 = generateTestTrace(N, connPos1m6, false);
		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				trace2,
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // должен быть обнаружен аларм
		assertTrue(res.getEndCoord() >= res.getCoord()); // корректность аларма
		assertTrue(res.getAlarmType() == ReflectogramMismatch.AlarmType.TYPE_OUTOFMASK); // тип аларма
		assertTrue(res.getCoord() + "<" + dist1, res.getCoord() < dist1); // привязки уже быть не должно

		// Сверяем наличие привязки при уходе начала коннектора влево в спорном случае

		PFTrace trace3 = generateTestTrace(N, connPos1m5, false);
		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				trace3,
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // должен быть обнаружен аларм
		assertTrue(res.getEndCoord() >= res.getCoord()); // корректность аларма
		assertTrue(res.getAlarmType() == ReflectogramMismatch.AlarmType.TYPE_OUTOFMASK); // тип аларма
		assertTrue(res.getCoord() == dist1); // спорный случай. В текущей версии привязка быть должна

		// проверка создания объектов эталона и результата анализа

		Etalon etalon = new Etalon(mtm, breakThresh,
				new EventAnchorer(mtm.getMTAE().getNEvents()));
		assertNotNull(etalon);
		AnalysisResult ar = CoreAnalysisManager.performAnalysis(
				trace1, defaultAP);
		assertNotNull(ar);
		res = getFirstMismatch(
				CoreAnalysisManager.compareAndMakeAlarms(ar, etalon));
		assertTrue(res != null); // должен быть обнаружен аларм

		// проверка с восстановленными эталоном и результатом анализа

		Etalon etRest =
			(Etalon)DataStreamableUtil.readDataStreamableFromBA(
					DataStreamableUtil.writeDataStreamableToBA(etalon),
					Etalon.getDSReader());

		ar = CoreAnalysisManager.performAnalysis(
				trace, defaultAP);
		assertNotNull(ar);
		AnalysisResult arRest =
			(AnalysisResult)DataStreamableUtil.readDataStreamableFromBA(
					ar.toByteArray(),
					AnalysisResult.getDSReader());
		res = getFirstMismatch(
				CoreAnalysisManager.compareAndMakeAlarms(arRest, etRest));
		assertTrue(res == null); // аларма быть не должно

		ar = CoreAnalysisManager.performAnalysis(
				trace1, defaultAP);
		assertNotNull(ar);
		arRest =
				(AnalysisResult)DataStreamableUtil.readDataStreamableFromBA(
						ar.toByteArray(),
						AnalysisResult.getDSReader());
		res = getFirstMismatch(
				CoreAnalysisManager.compareAndMakeAlarms(arRest, etRest));
		assertTrue(res != null); // должен быть обнаружен аларм
	}
}
