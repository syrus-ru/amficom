package com.syrus.AMFICOM.analysis.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParametersStorage;
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.util.Application;
import com.syrus.util.HashCodeGenerator;

/**
 * ??????????, ??? ?? TestCase, ? ????????? ??? ???????????????????????
 * ???????? ???????? ???????
 * @author $Author: saa $
 * @version $Revision: 1.14 $, $Date: 2006/04/24 07:13:07 $
 * @module
 */
public class DetailedInitialAnalysisTestCase extends TestCase {
	final static int MAX_ERROR_CODE_P1 = 5;
	final static int NO_ERROR = MAX_ERROR_CODE_P1 - 1; // the bigger code the softer error
	//final static String A_PARAMS = "0.001;0.01;0.5;1.5;1.0;";
	//final static String A_PARAMS = "0.001;0.01;0.5;3.0;1.3;";
	//final static String A_PARAMS = "0.001;0.01;0.5;3.0;1.5;";

	final static String A_PARAMS = "0.001;0.01;0.5;3.5;1.3;"; // XXX: temp xxx mark

	static class EventHashCode {
		HashCodeGenerator hcg = new HashCodeGenerator();
		public void addEvent(ReliabilitySimpleReflectogramEvent ev) {
			this.hcg.addInt(ev.getBegin());
			this.hcg.addInt(ev.getEnd());
			this.hcg.addInt(ev.getEventType());
			if (ev.hasReliability()) {
				this.hcg.addDouble(ev.getReliability());
			}
		}
		public int getResult() {
			return this.hcg.getResult();
		}
	}

	private static class FailCounter {
		// new/changed and lost events
		private int[] newCount;
		private int[] lossCount;
		// begin/end moved
		private double connBeginPosRoughness; // usually very strict
		private double connEndPosRoughness; // usally not strict at all
		private double positionRoughness; // usually rather non-strict
		private int connBeginPosNumber; // usually very strict
		private int connEndPosNumber; // usally not strict at all
		private int positionNumber; // usually rather non-strict
		// hash code
		private HashCodeGenerator hashAcc;
		// total time
		private long timeAcc;
		public FailCounter() {
			//typeCount = new int[MAX_ERROR_CODE];
			newCount = new int[MAX_ERROR_CODE_P1];
			lossCount = new int[MAX_ERROR_CODE_P1];
			hashAcc = new HashCodeGenerator();
		}
		public int getNew(int level) {
			return newCount[level];
		}
		public int getLoss(int level) {
			return lossCount[level];
		}
//        public void incType(int level) {
//            typeCount[level]++;
//        }
		public double getAvConnBeginRoughness() {
			return connBeginPosRoughness / connBeginPosNumber;
		}
		public double getAvConnEndRoughness() {
			return connEndPosRoughness / connEndPosNumber;
		}
		public double getAvPositionRoughness() {
			return positionRoughness / positionNumber;
		}
		public int getConnBeginPosNumber() {
			return connBeginPosNumber;
		}
		public int getConnEndPosNumber() {
			return connEndPosNumber;
		}
		public int getPositionNumber() {
			return positionNumber;
		}
		public void incNew(int level) {
			newCount[level]++;
		}
		public void incLoss(int level) {
			lossCount[level]++;
		}
		public void incLoss(int level, int count) {
			lossCount[level] += count;
		}
		public void incConnBegin(double val) {
			connBeginPosRoughness += val;
			connBeginPosNumber++;
		}
		public void incConnEnd(double val) {
			connEndPosRoughness += val;
			connEndPosNumber++;
		}
		public void incPosition(double val) {
			positionRoughness += val;
			positionNumber++;
		}
		public void addHash(EventHashCode ehc) {
			hashAcc.addInt(ehc.getResult());
		}
		public int getHash() {
			return hashAcc.getResult();
		}
		public void toSmallestCounts(FailCounter that) {
			for (int i = 0; i < MAX_ERROR_CODE_P1; i++) {
				//typeCount[i] = Math.min(typeCount[i], that.typeCount[i]);
				newCount[i] = Math.min(newCount[i], that.newCount[i]);
				lossCount[i] = Math.min(lossCount[i], that.lossCount[i]);
			}
			if (that.connBeginPosRoughness < connBeginPosRoughness) {
				connBeginPosRoughness = that.connBeginPosRoughness;
				connBeginPosNumber = that.connBeginPosNumber;
			}
			if (that.connEndPosRoughness < connEndPosRoughness) {
				connEndPosRoughness = that.connEndPosRoughness;
				connEndPosNumber = that.connEndPosNumber;
			}
			if (that.positionRoughness < positionRoughness) {
				positionRoughness = that.positionRoughness;
				positionNumber = that.positionNumber;
			}
		}
		public void addCounts(FailCounter that) {
			for (int i = 0; i < MAX_ERROR_CODE_P1; i++) {
				//typeCount[i] = typeCount[i] + that.typeCount[i];
				newCount[i] = newCount[i] + that.newCount[i];
				lossCount[i] = lossCount[i] + that.lossCount[i];
			}
			connBeginPosRoughness += that.connBeginPosRoughness;
			connEndPosRoughness += that.connEndPosRoughness;
			positionRoughness += that.positionRoughness;
			connBeginPosNumber += that.connBeginPosNumber;
			connEndPosNumber += that.connEndPosNumber;
			positionNumber += that.positionNumber;
		}
		public long getTimeAcc() {
			return timeAcc;
		}
		public void addTimeAcc(long dt) {
			timeAcc += dt;
		}
	}

	public final void testAnalysisDB()
	throws IOException, InvalidAnalysisParametersException {
		Application.init("test");
		AnalysisParameters ap = new AnalysisParameters(
				A_PARAMS,
				Heap.getMinuitDefaultParams());
		double res = evaluateAnalysisDB(ap, true, null);
		System.out.println("evaluateAnalysisDB = " + res);
	}

	public final void xtestNotCrush1()
	throws IOException, InvalidAnalysisParametersException {
		AnalysisParameters ap =
			Heap.getMinuitDefaultParams();
		double[] p = true
			? new double[] {
				0.0010,
				0.01,
				0.40139146197572784,
				2.377935021636042,
				1.3,
				1.0,
				15.0,
				0.4258108386998794,
				1.5,
				9.096430843400073,
				0.1 }
			: new double[] {
				0.0010,
				0.01,
				0.5,
				3.5,
				1.3,
				1.0,
				15.0,
				0.5,
				1.5,
				10.0,
				0.1 };

		AnalysisParametersStorage aps = ap.getStorageClone();
		aps.setEventTh(p[0]);
		aps.setSpliceTh(p[1]);
		aps.setConnectorTh(p[2]);
		aps.setEndTh(p[3]);
		aps.setNoiseFactor(p[4]);
		aps.setTau2nrs(p[5]);
		aps.setNrsMin((int) p[6]);
		aps.setRsaCrit(p[7]);
		aps.setNrs2rsaSmall(p[8]);
		aps.setNrs2rsaBig(p[9]);
		aps.setL2rsaBig(p[10]);
		ap.setAllFrom(aps);

		double res = evaluateAnalysisDB(ap, false, null);
		System.out.println("ref test result = " + res);
	}

	// @todo supply a javadoc
	// @todo? actually, this is not just a testcase - move?
	// cache may be null
	public static final double evaluateAnalysisDB(AnalysisParameters ap,
			boolean verbose,
			HashMap<String, PFTrace> cache)
	throws IOException {
		File file = new File("test/testAnalysisDB.dat"); // FIXME
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String s;
		Pattern head = Pattern.compile("FILE:\\s+(.+)");
		Pattern result = Pattern.compile("RESULTS\\s*\\{\\s*");
		Pattern resEvents = Pattern.compile("EVENTS\\b.*"); // 'correct' events
		Pattern resEvents2 = Pattern.compile("ALLOWED\\b.*"); // 'current-analysis' events
		String fName = null;
		FailCounter fails = new FailCounter();
		int[] traceStatus = new int[MAX_ERROR_CODE_P1];
		int totalTraces = 0;
		for (int i = 0; i < traceStatus.length; i++)
			traceStatus[i] = 0;

		long time0 = System.currentTimeMillis();
		while ((s = br.readLine()) != null) {
			if (s.equals("")) {
				fName = null;
				continue;
			}
			Matcher matcher = head.matcher(s);
			if (matcher.matches()) {
				fName = matcher.group(1);
				continue;
			}
			matcher = result.matcher(s);
			if (matcher.matches()) { // result section started
				// try to load several results sets
				ArrayList<ToleranceSimpleReflectogramEvent[]> resultList =
					new ArrayList<ToleranceSimpleReflectogramEvent[]>();
				while ((s = br.readLine()) != null) {
					if (s.equals("}"))
						break;
					if (resEvents.matcher(s).matches()
							|| resEvents2.matcher(s).matches()) {
						ToleranceSimpleReflectogramEvent[] res = loadEvents(br);
						resultList.add(res);
					}
				}
				if (resultList.isEmpty())
					continue; // no results
				if (fName == null)
					continue;
				ToleranceSimpleReflectogramEvent[][] etalons =
					resultList.toArray(new ToleranceSimpleReflectogramEvent
							[resultList.size()][]);
				// assert etalon correctness
				for (int i = 0; i < etalons.length; i++) {
					for (int j = 0; j < etalons[i].length; j++) {
						int beg = etalons[i][j].getBegin();
						int end = etalons[i][j].getEnd();
						assertTrue(fName + " - etalon begin<=end mismatch: "
								+ beg + ">" + end,
								beg <= end);
					}
					for (int j = 1; j < etalons[i].length; j++) {
						int end = etalons[i][j - 1].getEnd();
						int beg = etalons[i][j].getBegin();
						assertTrue(fName + " - etalon end/begin mismatch: "
								+ end + "/" + beg,
								beg == end);
					}
				}
				// test trace
				if (verbose) {
					System.out.println("Testing " + fName);
				}

				int rc = performTraceTest(fName,
						cache,
						etalons,
						fails,
						ap,
						verbose);
				traceStatus[rc]++;
				totalTraces++;
				
				if (verbose) {
					System.out.println("^^^"
							+ " Tesing " + fName
							+ " complete, rc = " + rc);
					System.out.println("");
				}
			}
		}
		if (verbose) {
			System.out.println("Total number of traces : " + totalTraces);
			System.out.println("Total fail counts:");
			s = "Lost   events: ";
			for (int i = 0; i < MAX_ERROR_CODE_P1; i++) {
				if (i != 0)
					s += "; ";
				s += fails.getLoss(i);
			}
			System.out.println(s);
			s = "New/Ch events: ";
			for (int i = 0; i < MAX_ERROR_CODE_P1; i++) {
				if (i != 0)
					s += "; ";
				s += fails.getNew(i);
			}
			System.out.println(s);
			s = "Whole  traces: ";
			for (int i = 0; i < MAX_ERROR_CODE_P1; i++) {
				if (i != 0)
					s += "; ";
				s += traceStatus[i];
			}
			System.out.println(s);
		}

		assertTrue(totalTraces > 0);

		double evaluationResult = 0.0;

		if (totalTraces > 0) {
			double weightedRoughness = 0;
			double sumRoughness = 0;
			double failedTraces = 0;
			for (int i = 0; i < NO_ERROR; i++) { // excluse NO_ERROR value
				// double weight =  //1.0 / (i + 1);
				// double weight = Math.pow(0.5, i);  // XXX: temp xxx mark
				double weight = Math.pow(0.3, i);
				weightedRoughness += fails.getLoss(i) * weight;
				weightedRoughness += fails.getNew(i) * weight;
				sumRoughness += fails.getLoss(i);
				sumRoughness += fails.getNew(i);
				failedTraces += traceStatus[i];
			}
			weightedRoughness *= 100.0 / totalTraces;
			sumRoughness *= 100.0 / totalTraces;
			failedTraces *= 100.0 / totalTraces;
			if (verbose) {
				System.out.println("Sum roughness %      : " + sumRoughness);
				System.out.println("Weighted roughness % : " + weightedRoughness);
				System.out.println("Failed traces %      : " + failedTraces);
			}
			evaluationResult = weightedRoughness; // This is our criteria
		}

		if (verbose) {
			if (fails.getConnBeginPosNumber() > 0)
				System.out.println("Connector Begin: "
						+ fails.getAvConnBeginRoughness() + " ("
						+ fails.getConnBeginPosNumber() + " records)");
			if (fails.getConnEndPosNumber() > 0)
				System.out.println("Connector End:   "
						+ fails.getAvConnEndRoughness() + " ("
						+ fails.getConnEndPosNumber() + " records)");
			if (fails.getPositionNumber() > 0)
				System.out.println("Other positions: "
						+ fails.getAvPositionRoughness() + " ("
						+ fails.getPositionNumber() + " records)");
	
			if (fails.getConnBeginPosNumber() > 0
					&& fails.getConnEndPosNumber() > 0
					&& fails.getPositionNumber() > 0) {
				System.out.println("Total roughness: " +
						(         fails.getAvConnBeginRoughness()
								+ fails.getAvConnEndRoughness()
								+ fails.getAvPositionRoughness()
						));
			}
			System.out.println("Overall hash:    "
					+ Integer.toHexString(fails.getHash()));
			long time1 = System.currentTimeMillis();
			boolean printTiming = true;
			if (printTiming) {
				long dtAn = fails.getTimeAcc();
				System.out.println();
				System.out.println("--------------");
				System.out.println("Analysis time :  " + dtAn);
				System.out.println("TestCase time :  " + (time1 - time0 - dtAn));
				System.out.println("Total time    :  " + (time1 - time0));
			}
		}

		return evaluationResult;
	}

	private static ToleranceSimpleReflectogramEvent[] loadEvents(
			BufferedReader br) throws IOException {
//        ToleranceSimpleReflectogramEvent[] ret =
//            new ToleranceSimpleReflectogramEvent[nEvents];
		Pattern endOfEvents = Pattern.compile("\\s*\\.\\s*");
		Pattern lineWithSpacesAndMaybeComment = Pattern.compile("\\s*(.*?)\\s*(#.*)?");
		Pattern eventRec = Pattern.compile(
				"T=(\\d+)\\s+" +
				"B=(\\d+)\\s+" +
				"E=(\\d+)\\s+" +
				"N=(\\d+)\\s+" +
				"L=(\\d+)\\b" +
				".*"); // possible other records (t2=\\d+)
		Pattern t2Rec = Pattern.compile("\\bT2=(\\d+)\\b");
		Pattern bMinRec = Pattern.compile("\\bBMIN=(\\d+)\\b");
		Pattern bMaxRec = Pattern.compile("\\bBMAX=(\\d+)\\b");
		Pattern eMinRec = Pattern.compile("\\bEMIN=(\\d+)\\b");
		Pattern eMaxRec = Pattern.compile("\\bEMAX=(\\d+)\\b");
		ArrayList<ToleranceSimpleReflectogramEvent> ret =
			new ArrayList<ToleranceSimpleReflectogramEvent>();
		for (;;){
			String s = br.readLine();
			Matcher matcher;
			// ignore comments
			matcher = lineWithSpacesAndMaybeComment.matcher(s);
			if (matcher.matches()) {
				// actually, any string matches this pattern
				s = matcher.group(1);
			}
			// skip empty and comment-only lines
			if (s.equals(""))
				continue;

			if (endOfEvents.matcher(s).matches())
				break;
			matcher = eventRec.matcher(s);
			assertTrue(matcher.matches());
			ToleranceSimpleReflectogramEvent tse =
				new ToleranceSimpleReflectogramEvent(
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)),
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(4)),
					Integer.parseInt(matcher.group(5)));
			ret.add(tse);
			matcher = t2Rec.matcher(s);
			if (matcher.find()) {
				tse.setOtherType(Integer.parseInt(matcher.group(1)));
			}
			matcher = bMinRec.matcher(s);
			if (matcher.find()) {
				tse.setBeginMin(Integer.parseInt(matcher.group(1)));
			}
			matcher = bMaxRec.matcher(s);
			if (matcher.find()) {
				tse.setBeginMax(Integer.parseInt(matcher.group(1)));
			}
			matcher = eMinRec.matcher(s);
			if (matcher.find()) {
				tse.setEndMin(Integer.parseInt(matcher.group(1)));
			}
			matcher = eMaxRec.matcher(s);
			if (matcher.find()) {
				tse.setEndMax(Integer.parseInt(matcher.group(1)));
			}
		}
		return ret.toArray(new ToleranceSimpleReflectogramEvent[ret.size()]);
	}

	// cache may be null
	private static int performTraceTest(String fName,
			HashMap<String, PFTrace> cache,
			ToleranceSimpleReflectogramEvent[][] ets,
			FailCounter fails, AnalysisParameters ap, boolean verbose) {
		String fPrefix = "test/ref/";
		boolean compareBeginEnd = true;

		PFTrace trace;
		if (cache != null && cache.containsKey(fName))
			trace = cache.get(fName);
		else {
			trace = new PFTrace(FileOpenCommand.readTraceFromFileEx(
				new File(fPrefix + fName), true));
			if (cache != null && trace != null)
				cache.put(fName, trace);
		}
		double dxkm = trace.getResolution() / 1000;
		long t0 = System.currentTimeMillis();
		ReliabilitySimpleReflectogramEvent re[] = CoreAnalysisManager.
				performAnalysis(trace, ap).getMTAE().getSimpleEvents();
		long t1 = System.currentTimeMillis();
		fails.addTimeAcc(t1 - t0);

		assertNotNull(re);

		boolean veryVerbose = verbose && false;

		if (veryVerbose) {
			System.out.println("NEvents=" + re.length);
			for (int i = 0; i < re.length; i++) {
				System.out.println(re[i].toString());
			}
		}

		// ???????? ??????????????? ?????????? ???????

		for (int i = 0; i < re.length; i++) {
			int evType = re[i].getEventType();
			boolean shouldHaveReliability =
				   evType == SimpleReflectogramEvent.CONNECTOR
				|| evType == SimpleReflectogramEvent.GAIN
				|| evType == SimpleReflectogramEvent.LOSS;
			if (shouldHaveReliability)
				assertTrue(re[i].hasReliability());
		}

		// ?????????? ???-????????
		EventHashCode ehc = new EventHashCode();
		for (int i = 0; i < re.length; i++) {
			ehc.addEvent(re[i]);
		}
		fails.addHash(ehc);
		if (verbose) {
			System.out.println("Hash: " + Integer.toHexString(ehc.getResult()));
		}

		// ????????? ? ?????????

		int eventBeyondEtalon = 0; // XXX
		int eventTypeChanged = 0; // XXX
		int eventNewLinearForgive = 1; // ??????? ? ?????? ?????? "????? ???????" ? ?????? ?????? ???. ???.
		// error levels (0 = max) for each new/changed probe event
		int[] ncELs = new int[re.length];
		for (int i = 0; i < re.length; i++) { // init with worst level
			ncELs[i] = 0;
		}

		int bestWorstLevel = 0;

		// lost events count per each level; min for all etalons
		int[] lELc = new int[MAX_ERROR_CODE_P1];

		// lost event counter per level for current etalon
		int[] lELcTemp = new int[MAX_ERROR_CODE_P1];

		// iterate over all etalons
		for (int i = 0; i < ets.length; i++) {
			SimpleReflectogramEventComparer rcomp =
				new SimpleReflectogramEventComparer(re, ets[i]);

			int worstLevel = NO_ERROR;

			if (verbose) {
				System.out.println("- Comparing to etalon # " + i);
			}
			// process new/changed events
			for (int k = 0; k < re.length; k++) {
				int level = NO_ERROR; 
				int et = rcomp.getEtalonIdByProbeIdNonStrict(k);
				if (et < 0) {
					// new event: etalon does not cover this region
					level = eventBeyondEtalon;
					if (verbose)
						System.out.println("["+level+"]"
								+ "uncovered new event"
								//+ " # " + k
								+ " T=" + re[k].getEventType()
								+ " B=" + re[k].getBegin()
								+ " km=" + re[k].getBegin()*dxkm);
				} else if (rcomp.getProbeIdByEtalonIdNonStrict(et) != k) {
					// new event: etalon2probe mapping gives another probe event
					level = ets[i][et].getNewLevel();
					if (re[k].getEventType() == SimpleReflectogramEvent.LINEAR) {
						level += eventNewLinearForgive;
						if (level > NO_ERROR)
							level = NO_ERROR;
					}
					if (verbose)
						System.out.println("["+level+"]"
								+ "new event   "
								//+ " # " + k
								+ " T=" + re[k].getEventType()
								+ " B=" + re[k].getBegin()
								+ " km=" + re[k].getBegin()*dxkm);
				} else {
					// changed/not changed event:
					// (1) compare type
					if (ets[i][et].getEventType() != re[k].getEventType()
							&& ets[i][et].getOtherType() != re[k].getEventType()) {
						level = Math.min(level, Math.max(eventTypeChanged,
								ets[i][et].getNewLevel()));
						if (verbose)
							System.out.println("["+level+"]"
									+ "type changed"
									//+ " # " + k
									+ " T=" + re[k].getEventType()
									+ " OT=" + ets[i][et].getEventType()
									+ " B=" + re[k].getBegin()
									+ " km=" + re[k].getBegin()*dxkm);
					}
					// (2) comparison of event begin and end:
					// -- for precise type match only
					// -- for first etalon only
					if (compareBeginEnd
							&& ets[i][et].getEventType() == re[k].getEventType()
							&& i == 0) {
						ToleranceSimpleReflectogramEvent ete = ets[i][et];
						double etBegin = ete.getBegin();
						double etEnd = ete.getEnd();
						double len = etEnd - etBegin;
						double dBeginS = re[k].getBegin() - etBegin;
						double dEndS = re[k].getEnd() - etEnd;
						double dBMin;
						double dBMax;
						double dEMin;
						double dEMax;
						// calculate default begin/end tolerance for this type of event
						{
							double unitBegin;
							double unitEnd;
							if (ete.getEventType() // ? ??????? ?????????? ??????
									== SimpleReflectogramEvent.CONNECTOR) {
								unitBegin = 1;      // 1 unit ~ 1 point
								unitEnd = len / 5.0;
							} else if (ete.getEventType() // ? ?/?? - ?? ??????
									== SimpleReflectogramEvent.NOTIDENTIFIED) {
								unitBegin = len / 5.0;
								unitEnd = len / 5.0;
							} else {
								unitBegin = len / 10.0;
								unitEnd = len / 10.0;
							}
							dBMin = -unitBegin * 10;
							dBMax = unitBegin * 10;
							dEMin = -unitEnd * 10;
							dEMax = unitEnd * 10;
						}
						// ?? ????????? begin ? end ??? ???. ???????,
						// ? ??????? ?? ?????????? ???? ???????
						// ????????? begin/end 
						// -- begin processing
						if (ete.hasBeginMin() || ete.hasBeginMax()
								|| ete.getEventType()
									!= SimpleReflectogramEvent.LINEAR)
						{
							// update dbmin/dbmax acc. to etalon
							if (ete.hasBeginMin())
								dBMin = ete.getBeginMin() - etBegin;
							if (ete.hasBeginMax())
								dBMax = ete.getBeginMax() - etBegin;
							String problem;
							problem = null;
							if (dBeginS < dBMin)
								problem = "begin moved too much to left ";
							if (dBeginS > dBMax)
								problem = "begin moved too much to right";
							if (problem != null && verbose) {
								System.out.println(problem
										+ " T=" + re[k].getEventType()
										+ " B=" + re[k].getBegin()
										+ " BE= " + ete.getBegin()
										+ " Bkm=" + re[k].getBegin()*dxkm
										+ " BEkm=" + ete.getBegin()*dxkm);
							}

							// ???? ??????? ????????? ??????? ????, ?????????
							// ?????????? ??? ????????? ?????? ????? ???????
							if (ete.hasBeginMin() && dBeginS >= dBMin && dBeginS < 0)
									dBeginS = 0;
							if (ete.hasBeginMax() && dBeginS <= dBMax && dBeginS > 0)
									dBeginS = 0;

							double beginRoughness = Math.abs(dBeginS) / (dBMax - dBMin);
							// 0.5 / ( 1/x + 1/sqrt(x) ) == 1 * x / (1 + sqrt(x))
							// ????? ?????????? - ???. ? ????, sqrt ?? +inf
							beginRoughness *= 1.0 / (1.0 + Math.sqrt(beginRoughness));
							//System.out.println("beginRoughness = " + beginRoughness); // FIXME
							incRoughnessCounter(ets[i][et], re[k], dxkm,
									true, fails, beginRoughness, verbose);
						}
						// -- end processing
						if (ete.hasEndMin() || ete.hasEndMax()
								|| ete.getEventType()
									!= SimpleReflectogramEvent.LINEAR
									&& ete.getEventType()
										!= SimpleReflectogramEvent.ENDOFTRACE)
						{
							if (ete.hasEndMin())
								dEMin = ete.getEndMin() - etEnd;
							if (ete.hasEndMax())
								dEMax = ete.getEndMax() - etEnd;
							String problem = null;
							if (dEndS < dEMin)
								problem = "end   moved too much to left ";
							if (dEndS > dEMax)
								problem = "end   moved too much to right";
							if (problem != null && verbose) {
								System.out.println("end   moved too much "
										+ " T=" + re[k].getEventType()
										+ " B=" + re[k].getBegin()
										+ " E=" + re[k].getEnd()
										+ " EE=" + ete.getEnd()
										+ " Bkm=" + re[k].getBegin()*dxkm
										+ " Ekm=" + re[k].getEnd()*dxkm
										+ " EEkm=" + ete.getEnd()*dxkm);
							}

							// ???? ??????? ????????? ??????? ????, ?????????
							// ?????????? ??? ????????? ?????? ????? ???????
							if (ete.hasEndMin() && dEndS >= dEMin && dEndS < 0)
									dEndS = 0;
							if (ete.hasEndMax() && dEndS <= dEMax && dEndS > 0)
									dEndS = 0;

							double endRoughness =
								Math.abs(dEndS) / (dEMax - dEMin);
							endRoughness *= 1.0 / (1.0 + Math.sqrt(endRoughness));
							incRoughnessCounter(ets[i][et], re[k], dxkm,
									false, fails, endRoughness, verbose);
						}
					}
				}
				// choose softest level for this event
				ncELs[k] = Math.max(ncELs[k], level);
				// set worst level for this etalon
				worstLevel = Math.min(worstLevel, level);
			}

			// process lost events
			for (int k = 0; k < lELcTemp.length; k++) {
				lELcTemp[k] = 0;
			}
			for (int et = 0; et < ets[i].length; et++) {
				int k = rcomp.getProbeIdByEtalonIdNonStrict(et);
				if (k >= 0 && rcomp.getEtalonIdByProbeIdNonStrict(k) == et)
					continue;
				int level = ets[i][et].getLossLevel();
				if (verbose)
					System.out.println("["+level+"]"
							+ "lost event  "
							//+ " e# " + et
							+ " T=" + ets[i][et].getEventType()
							+ " B=" + ets[i][et].getBegin()
							+ " km=" + ets[i][et].getBegin()*dxkm);
				lELcTemp[level]++;
				worstLevel = Math.min(worstLevel, level);
			}
			if (i == 0)
				System.arraycopy(lELcTemp, 0, lELc, 0, lELc.length);
			else {
				for (int k = 0; k < lELcTemp.length; k++) {
					lELc[k] = Math.min(lELc[k], lELcTemp[k]);
				}
			}
			bestWorstLevel = Math.max(bestWorstLevel, worstLevel);
		}

		int ncTEL = NO_ERROR;
		for (int i = 0; i < ncELs.length; i++) {
			ncTEL = Math.min(ncTEL, ncELs[i]);
			fails.incNew(ncELs[i]);
		}

		for (int k = 0; k < lELcTemp.length; k++) {
			fails.incLoss(k, lELc[k]);
		}

		return bestWorstLevel;
	}

	private static void incRoughnessCounter(SimpleReflectogramEvent et,
			SimpleReflectogramEvent re, double dxkm,
			boolean isBegin, FailCounter fails, double roughness0,
			boolean verbose) {
		double roughness = roughness0 / (1.0 + Math.sqrt(roughness0));
		if (roughness > 0.1 && true) { // if verbose
			int etPos = isBegin ? et.getBegin() : et.getEnd();
			int rePos = isBegin ? re.getBegin() : re.getEnd();
			if (verbose) {
				System.out.println("incRoughness: " + roughness
						+ " for " + (isBegin ? "begin" : "end  ")
						+ " T=" + re.getEventType() + "/" + et.getEventType()
						+ " etPos=" + etPos
						+ " rePos=" + rePos
						+ " km=" + rePos * dxkm
						);
			}
		}
		if (isBegin) {
			if (et.getEventType()
					== SimpleReflectogramEvent.CONNECTOR)
				fails.incConnBegin(roughness);
			else
				fails.incPosition(roughness);
		} else {
			if (et.getEventType()
					== SimpleReflectogramEvent.CONNECTOR)
				fails.incConnEnd(roughness);
			else
				fails.incPosition(roughness);
		}
	}
}
