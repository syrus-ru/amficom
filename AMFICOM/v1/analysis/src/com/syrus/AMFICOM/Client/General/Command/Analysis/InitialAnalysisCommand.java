package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class InitialAnalysisCommand extends VoidCommand {

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs != null)
		{
			double[] y = bs.getTraceData();

			AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			if (ap == null) {
				new ClientAnalysisManager();
				ap = Heap.getMinuitAnalysisParams();
			}

			ModelTraceAndEventsImpl mtae =
				CoreAnalysisManager.makeAnalysis(bs, ap);

	        RefAnalysis a = new RefAnalysis();
			a.decode(y, mtae);

			Heap.setRefAnalysisByKey(RefUpdateEvent.PRIMARY_TRACE, a);
			Heap.setMTAEPrimary(mtae);
		}
	}
}
