package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class AnalysisCommand extends VoidCommand {

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs != null)
		{
	        RefAnalysis a = new RefAnalysis(bs);
			Heap.setRefAnalysisPrimary(a);
		}
	}
}
