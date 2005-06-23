/*-
 * $Id: CoreAnalysisManagerTestCase.java,v 1.2 2005/06/23 06:39:53 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

import java.io.File;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.io.BellcoreStructure;

import junit.framework.TestCase;

public class CoreAnalysisManagerTestCase extends TestCase {
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(CoreAnalysisManagerTestCase.class);
    }

    /*
     * Class under test for double getMedian(double[])
     */
    public final void testGetMediandoubleArray() {
        // simple test
        double[] arr = new double[] { 4, 2, 1, 3, 0, 5, 6 };
        assertEquals(CoreAnalysisManager.getMedian(arr), 3, 1e-15);

        //assertEquals(0, CoreAnalysisManager.getMedian(new double[] {}), 1e-15);
    }

    public final void testAnalysisNotCrush() {
        File file = new File("/traces/fail.sor"); // XXX
        BellcoreStructure bs = FileOpenCommand.readTraceFromFile(file);
        AnalysisParameters ap = new AnalysisParameters(
                "0.001;0.01;0.5;1.5;1.0;");
        SimpleReflectogramEvent re[] = CoreAnalysisManager.
        		performAnalysis(bs, ap).getMTAE().getSimpleEvents();
        System.out.println("NEvents=" + re.length);
        for (int i = 0; i < re.length; i++) {
            System.out.println(re[i].toString());
        }
    }
}
