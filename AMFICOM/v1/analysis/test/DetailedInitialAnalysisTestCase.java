/*-
 * $Id: DetailedInitialAnalysisTestCase.java,v 1.4 2005/06/06 12:44:32 saa Exp $
 * 
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.io.BellcoreStructure;

import junit.framework.TestCase;

/**
 * Фактически, это не TestCase, а программа для полуавтоматизированного
 * контроля качества анализа
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/06/06 12:44:32 $
 * @module
 */
public class DetailedInitialAnalysisTestCase extends TestCase {
    final static int MAX_ERROR_CODE_P1 = 5;
    final static int NO_ERROR = MAX_ERROR_CODE_P1 - 1; // the bigger code the softer error
    //final static String A_PARAMS = "0.001;0.01;0.5;1.5;1.0;";
    //final static String A_PARAMS = "0.001;0.01;0.5;3.0;1.3;";
    //final static String A_PARAMS = "0.001;0.01;0.5;3.0;1.5;";
    final static String A_PARAMS = "0.001;0.01;0.5;3.5;1.3;"; // XXX: temp xxx mark

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
        // total time
        private long timeAcc;
        public FailCounter() {
            //typeCount = new int[MAX_ERROR_CODE];
            newCount = new int[MAX_ERROR_CODE_P1];
            lossCount = new int[MAX_ERROR_CODE_P1];
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
                ArrayList resultList = new ArrayList();
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
                // test trace
                int rc = performTraceTest(fName,
                        (ToleranceSimpleReflectogramEvent[][])resultList.
                            toArray(new ToleranceSimpleReflectogramEvent
                                [resultList.size()][]),
                        fails,
                        true);
                traceStatus[rc]++;
                totalTraces++;
                System.out.println("^^^"
                        + " Tesing " + fName
                        + " complete, rc = " + rc);
                System.out.println("");
            }
        }
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

        assertTrue(totalTraces > 0);

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
            System.out.println("Sum roughness %      : " + sumRoughness);
            System.out.println("Weighted roughness % : " + weightedRoughness);
            System.out.println("Failed traces %      : " + failedTraces);
        }

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
        long time1 = System.currentTimeMillis();
        boolean printTiming = true;
        if (printTiming) {
            long dtAn = fails.getTimeAcc();
            System.out.println("Analysis time : " + dtAn);
            System.out.println("TestCase time : " + (time1 - time0 - dtAn));
            System.out.println("Total time    : " + (time1 - time0));
        }
    }

    private ToleranceSimpleReflectogramEvent[] loadEvents(
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
        ArrayList ret = new ArrayList();
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
        return (ToleranceSimpleReflectogramEvent[])
            ret.toArray(new ToleranceSimpleReflectogramEvent[ret.size()]);
    }

    private int performTraceTest(String fName,
            ToleranceSimpleReflectogramEvent[][] ets,
            FailCounter fails, boolean verbose) {
        String fPrefix = "test/ref/";
        boolean compareBeginEnd = true;

        BellcoreStructure bs = FileOpenCommand.readTraceFromFile(
                new File(fPrefix + fName));
        double dxkm = bs.getResolution() / 1000;
        AnalysisParameters ap = new AnalysisParameters(
                A_PARAMS,
                ClientAnalysisManager.getDefaultAPClone());
        long t0 = System.currentTimeMillis();
        ReliabilitySimpleReflectogramEvent re[] =
            (ReliabilitySimpleReflectogramEvent[])
            CoreAnalysisManager.makeAnalysis(bs, ap).getSimpleEvents();
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

        // проверка согласованности результата анализа

        for (int i = 0; i < re.length; i++) {
            int evType = re[i].getEventType();
            boolean shouldHaveReliability =
                   evType == SimpleReflectogramEvent.CONNECTOR
                || evType == SimpleReflectogramEvent.GAIN
                || evType == SimpleReflectogramEvent.LOSS;
            if (shouldHaveReliability)
                assertTrue(re[i].hasReliability());
        }

        // сравнение с эталонами

        int eventBeyondEtalon = 0; // XXX
        int eventTypeChanged = 0; // XXX
        int eventNewLinearForgive = 1; // добавка к уровню ошибки "новое событие" в случае нового лин. соб.
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
                new SimpleReflectogramEventComparer(re, ets[i], false);

            int worstLevel = NO_ERROR;

            if (verbose) {
                System.out.println("- Comparing to etalon # " + i);
            }
            // process new/changed events
            for (int k = 0; k < re.length; k++) {
                int level = NO_ERROR; 
                int et = rcomp.getEtalonIdByProbeId(k);
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
                }
                else if (rcomp.getProbeIdByEtalonId(et) != k) {
                    // new event: etalon2probe mapping gives another probe event
                    if (verbose)
                        level = ets[i][et].getNewLevel();
                    if (re[k].getEventType() == SimpleReflectogramEvent.LINEAR) {
                        level += eventNewLinearForgive;
                        if (level > NO_ERROR)
                            level = NO_ERROR;
                    }
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
                            if (ete.getEventType()
                                    == SimpleReflectogramEvent.CONNECTOR) {
                                unitBegin = 1;      // 1 unit ~ 1 point
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
                        // не оцениваем begin и end для лин. событий,
                        // у которых не определены явно границы
                        // колебаний begin/end 
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
                            if (problem != null) {
                                System.out.println(problem
                                        + " T=" + re[k].getEventType()
                                        + " B=" + re[k].getBegin()
                                        + " BE= " + ete.getBegin()
                                        + " Bkm=" + re[k].getBegin()*dxkm
                                        + " BEkm=" + ete.getBegin()*dxkm);
                            }
                            double beginRoughness =
                                Math.abs(dBeginS) / (dBMax - dBMin);
                            // 0.5 / ( 1/x + 1/sqrt(x) ) == 1 * x / (1 + sqrt(x))
                            // имеет асимпотики - лин. в нуле, sqrt на +inf
                            beginRoughness *= 1.0 / (1.0 + Math.sqrt(beginRoughness));
                            //System.out.println("beginRoughness = " + beginRoughness); // FIXME
                            incRoughnessCounter(ets[i][et], re[k], dxkm, true, fails, beginRoughness);
                        }
                        // -- end processing
                        if (ete.hasEndMin() || ete.hasEndMax()
                                || ete.getEventType()
                                    != SimpleReflectogramEvent.LINEAR)
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
                            if (problem != null) {
                                System.out.println("end   moved too much "
                                        + " T=" + re[k].getEventType()
                                        + " B=" + re[k].getBegin()
                                        + " E=" + re[k].getEnd()
                                        + " EE=" + ete.getEnd()
                                        + " Bkm=" + re[k].getBegin()*dxkm
                                        + " Ekm=" + re[k].getEnd()*dxkm
                                        + " EEkm=" + ete.getEnd()*dxkm);
                            }
                            double endRoughness =
                                Math.abs(dEndS) / (dEMax - dEMin);
                            endRoughness *= 1.0 / (1.0 + Math.sqrt(endRoughness));
                            incRoughnessCounter(ets[i][et], re[k], dxkm, false, fails, endRoughness);
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
                int k = rcomp.getProbeIdByEtalonId(et);
                if (k >= 0 && rcomp.getEtalonIdByProbeId(k) == et)
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

    private void incRoughnessCounter(SimpleReflectogramEvent et,
            SimpleReflectogramEvent re, double dxkm,
            boolean isBegin, FailCounter fails, double roughness0) {
        double roughness = roughness0 / (1.0 + Math.sqrt(roughness0));
        if (roughness > 0.1 && true) { // if verbose
            int etPos = isBegin ? et.getBegin() : et.getEnd();
            int rePos = isBegin ? re.getBegin() : re.getEnd();
            System.out.println("incRoughness: " + roughness
                    + " for " + (isBegin ? "begin" : "end  ")
                    + " T=" + re.getEventType() + "/" + et.getEventType()
                    + " etPos=" + etPos
                    + " rePos=" + rePos
                    + " km=" + rePos * dxkm
                    );
        }
        if (isBegin) {
            if (et.getEventType()
                    == SimpleReflectogramEvent.CONNECTOR)
                fails.incConnBegin(roughness);
            else
                fails.incPosition(roughness);
        }
        else {
            if (et.getEventType()
                    == SimpleReflectogramEvent.CONNECTOR)
                fails.incConnEnd(roughness);
            else
                fails.incPosition(roughness);
        }
    }
}
