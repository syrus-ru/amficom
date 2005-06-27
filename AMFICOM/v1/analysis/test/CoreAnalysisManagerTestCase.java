/*-
 * $Id: CoreAnalysisManagerTestCase.java,v 1.3 2005/06/27 06:45:36 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.SignatureMismatchException;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.io.BellcoreStructure;

import junit.framework.TestCase;

public class CoreAnalysisManagerTestCase extends TestCase {
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(CoreAnalysisManagerTestCase.class);
    }

    private static final String TEST_FNAME = "/traces/fail.sor";
    private BellcoreStructure loadTestBS() {
        File file = new File(TEST_FNAME); // XXX
        return FileOpenCommand.readTraceFromFile(file);
    }
    private AnalysisParameters defaultAP = new AnalysisParameters(
    	"0.001;0.01;0.5;1.5;1.0;");
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
    	BellcoreStructure bs = loadTestBS();
        SimpleReflectogramEvent re[] = CoreAnalysisManager.
        		performAnalysis(bs, defaultAP).getMTAE().getSimpleEvents();
        System.out.println("NEvents=" + re.length);
        for (int i = 0; i < re.length; i++) {
            System.out.println(re[i].toString());
        }
    }

    public final void testMTMReadability()
    throws IncompatibleTracesException, SignatureMismatchException, IOException {
    	// load bs
    	BellcoreStructure bs = loadTestBS();

    	// make etalon
    	Collection<BellcoreStructure> bsColl =
    		new ArrayList<BellcoreStructure>();
    	bsColl.add(bs);
    	ModelTraceManager mtm = CoreAnalysisManager.makeEtalon(bsColl, defaultAP);

    	// save to byte[]
    	byte[] mtmBytes = DataStreamableUtil.writeDataStreamableToBA(mtm);
        System.out.println("MTM bytes = " + mtmBytes.length);
        
        // restore
        ByteArrayInputStream bais = new ByteArrayInputStream(mtmBytes);
        DataInputStream dis = new DataInputStream(bais);
        ModelTraceManager.getReader().readFromDIS(dis);
        
        // ensure there are no bytes left
        try {
        	dis.readByte();
        	assertFalse("There are extra bytes", true);
        } catch (EOFException e) {
        	// this is an expected thing
        }
    }
}
