package com.syrus.AMFICOM.analysis.test;
/*-
 * $Id: CoreAnalysisManagerTestCase.java,v 1.3 2005/08/29 09:39:45 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
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
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.analysis.dadara.SignatureMismatchException;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.io.BellcoreCreator;
import com.syrus.io.BellcoreStructure;

public class CoreAnalysisManagerTestCase extends TestCase {
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CoreAnalysisManagerTestCase.class);
	}

	private static final String TEST_FNAME = "/traces/fail.sor";
	//private static final String TEST_FNAME = "test/ref/rg0065.ref";
	private static BellcoreStructure loadTestBS() {
		File file = new File(TEST_FNAME); // XXX
		return FileOpenCommand.readTraceFromFile(file);
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
		BellcoreStructure bs = loadTestBS();
		SimpleReflectogramEvent re[] = CoreAnalysisManager.
				performAnalysis(bs, defaultAP).getMTAE().getSimpleEvents();
		printEventList(re);
	}

	// checks and returns number of bytes of byte[]
	public static int checkMTMReadability(BellcoreStructure bs, boolean verbose)
	throws IncompatibleTracesException, SignatureMismatchException, IOException {
		// make etalon
		Collection<BellcoreStructure> bsColl =
			new ArrayList<BellcoreStructure>();
		bsColl.add(bs);
		ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(bsColl, defaultAP);

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
			ReflectogramMismatch res = ModelTraceComparer.compareMTAEToMTM(mtm.getMTAE(), mtm);
			System.out.println("pre-compare alarm: " + res);
			ReflectogramMismatch res2 = ModelTraceComparer.compareMTAEToMTM(mtm2.getMTAE(), mtm);
			System.out.println("compare alarm: " + res2);
			assertTrue(res == null); // mtm should cover itself
			assertTrue(res2 == null); // restored mtae should be the same
		}

		return mtmBytes.length;
	}

	public final void testMTMReadability()
	throws IncompatibleTracesException, SignatureMismatchException, IOException {
		// load bs
		BellcoreStructure bs = loadTestBS();
		// test
		checkMTMReadability(bs, true);
	}

	private static double xRand(int pos) {
		double v = ((pos * 27) % 37) / 36.0;
		return v - 0.5;
	}
	private static double[] generateTestBellcoreYArray(int len, int dist) {
		final int N = len;
		double noise = 10.0;
		double resolution = 1.0; // m
		double att = 0.22; // db/km
		double y0 = noise + N * 0.5 * att / 1e3 * resolution;
		final int NCONN = 3;
		int[] connPos = {0, dist, N / 3 * 2}; // FIXME
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

	private static BellcoreStructure generateTestBellcore(int len, int dist,
			boolean dumpToFile) {
		double[] y = generateTestBellcoreYArray(len, dist);
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
		return new BellcoreCreator(y).getBS();
	}

	private static ReflectogramMismatch getFirstMismatch(List list) {
		Iterator it = list.iterator();
		if (it.hasNext())
			return (ReflectogramMismatch) it.next();
		else
			return null;    	
	}

	// �������� ��������� ����� � ����������� �������, ������������� �����
	public final void testTraceComparison() throws IncompatibleTracesException {
		System.out.println("testTraceComparison():");
		final int N = 10000;
		final int dist1 = N / 3;
		System.out.println("generating trace...");
		BellcoreStructure bs = generateTestBellcore(N, dist1, false);
		System.out.println("Analysing trace...");
		Collection<BellcoreStructure> bsColl =
			new ArrayList<BellcoreStructure>();
		bsColl.add(bs);
		ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(bsColl, defaultAP);
		double breakThresh = ReflectogramMath.getArrayMin(bs.getTraceData());
		printEventList(mtm.getMTAE().getSimpleEvents());

		ReflectogramMismatch res;

		res = ModelTraceComparer.compareMTAEToMTM(mtm.getMTAE(), mtm);
		System.out.println("compare mtae: " + res);
		assertTrue(res == null);

		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				bs,
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare bs: " + res);
		assertTrue(res == null);

		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				generateTestBellcore(N, dist1 - 4, false),
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // ������ ���� ��������� �����
		assertTrue(res.getEndCoord() >= res.getCoord()); // ������������ ������
		assertTrue(res.getCoord() + "==" + dist1, res.getCoord() == dist1); // ������ ��������� ��������

		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				generateTestBellcore(N, dist1 - 6, false),
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // ������ ���� ��������� �����
		assertTrue(res.getEndCoord() >= res.getCoord()); // ������������ ������
		assertTrue(res.getCoord() + "<" + dist1, res.getCoord() < dist1); // �������� ��� ���� �� ������

		res = getFirstMismatch(CoreAnalysisManager.analyseCompareAndMakeAlarms(
				generateTestBellcore(N, dist1 - 5, false),
				defaultAP, breakThresh, mtm, null));
		System.out.println("compare diff: " + res);
		assertTrue(res != null); // ������ ���� ��������� �����
		assertTrue(res.getEndCoord() >= res.getCoord()); // ������������ ������
		assertTrue(res.getCoord() == dist1); // ������� ������. � ������� ������ �������� ���� ������
	}
}
