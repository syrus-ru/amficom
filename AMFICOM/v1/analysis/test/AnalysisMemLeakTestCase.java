/*-
 * $Id: AnalysisMemLeakTestCase.java,v 1.1 2005/06/23 06:41:55 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.io.BellcoreStructure;

import junit.framework.TestCase;

public class AnalysisMemLeakTestCase extends TestCase {
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(AnalysisMemLeakTestCase.class);
    }
    public final void testAnalysisNotCrush()
    throws IncompatibleTracesException {
        File file = new File("/traces/fail.sor"); // XXX
        BellcoreStructure bs = FileOpenCommand.readTraceFromFile(file);
        AnalysisParameters ap = new AnalysisParameters(
                "0.001;0.01;0.5;1.5;1.0;");

        boolean makeGC = true;
        if (makeGC) {
        	System.gc();
        }
        //String x = " ";
        long used0 = Runtime.getRuntime().totalMemory()
        		- Runtime.getRuntime().freeMemory();
        long t0 = System.currentTimeMillis();
        long usedPrev = used0;
        long iPrev = 0;
        for (int i = 1; i <= 10000000; i++) {
        	int p = i;
        	while (p > 0 && p % 10 == 0) {
        		p /= 10;
        	}
        	if (p < 10) {
                if (makeGC) {
                	System.gc();
                }
                long used1 = Runtime.getRuntime().totalMemory()
                		- Runtime.getRuntime().freeMemory();
                long t1 = System.currentTimeMillis();
        		System.out.println("Pass " + i
        				+ ", avTime " + (t1-t0) * 1000 / i + " us"
        				+ ", dMem " + (used1 - used0) + " b"
        				+ ", leakAcc " + (used1 - used0) * 10 / i / 10.0 + " b"
        				+ ", leakCur " + (used1 - usedPrev) * 10 / (i - iPrev) / 10.0 + " b"
        				);
        		iPrev = i;
        		usedPrev = used1;
        	}

        	//x += ' ';

        	boolean testAnalysis = true;
        	boolean testEtalonAndComparison = false;

        	if (testAnalysis) {
	        	CoreAnalysisManager.performAnalysis(bs, ap).getMTAE().
	        			getSimpleEvents();
        	}
        	if (testEtalonAndComparison) {
	        	Collection<BellcoreStructure> col = new ArrayList<BellcoreStructure>();
	        	col.add(bs);
	        	ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(col, ap);
	        	CoreAnalysisManager.analyseCompareAndMakeAlarms(bs, ap, -99, mtm);
        	}
        }

    }
}
