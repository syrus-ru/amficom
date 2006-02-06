package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

//FIXME: unused
public class TraceMakeCurrentCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public TraceMakeCurrentCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new TraceMakeCurrentCommand(this.aContext);
	}

	@Override
	public void execute()
	{

		// FIXME: ерунда?
		Trace tr = Heap.getReferenceTrace();
		new FileRemoveCommand(Heap.REFERENCE_TRACE_KEY, this.aContext).execute();
		new FileRemoveCommand(Heap.PRIMARY_TRACE_KEY, this.aContext).execute();
		Heap.setPrimaryTrace(tr);
		Heap.makePrimaryAnalysis();
	}
}