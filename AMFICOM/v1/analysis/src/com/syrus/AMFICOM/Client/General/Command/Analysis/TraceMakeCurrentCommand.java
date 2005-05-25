package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.BellcoreStructure;

public class TraceMakeCurrentCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public TraceMakeCurrentCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new TraceMakeCurrentCommand(aContext);
	}

	public void execute()
	{

        // FIXME: ерунда?
		BellcoreStructure bs = Heap.getBSReferenceTrace();
		new FileRemoveCommand(Heap.REFERENCE_TRACE_KEY, aContext).execute();
		new FileRemoveCommand(Heap.PRIMARY_TRACE_KEY, aContext).execute();
		Heap.setBSPrimaryTrace(bs);

		new AnalysisCommand().execute();
		
		Heap.primaryTraceOpened(bs);
		Heap.setCurrentTracePrimary();
	}
}