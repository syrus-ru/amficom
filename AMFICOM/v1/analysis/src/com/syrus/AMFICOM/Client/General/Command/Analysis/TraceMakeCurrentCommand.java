package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.io.BellcoreStructure;

public class TraceMakeCurrentCommand extends VoidCommand
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
		try
		{
			new Checker(this.aContext.getSessionInterface());
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

        // FIXME: ерунда?
		BellcoreStructure bs = Heap.getBSReferenceTrace();
		new FileRemoveCommand(Heap.REFERENCE_TRACE_KEY, aContext).execute();
		new FileRemoveCommand(RefUpdateEvent.PRIMARY_TRACE, aContext).execute();
		Heap.setBSPrimaryTrace(bs);

		new AnalysisCommand().execute();
		
		Heap.primaryTraceOpened(bs);
		Heap.setCurrentTracePrimary();
	}
}