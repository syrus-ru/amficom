package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class AnalysisCommand extends AbstractCommand {
	@Override
	public void execute()
	{
		Heap.makeAnalysis();
	}
}
