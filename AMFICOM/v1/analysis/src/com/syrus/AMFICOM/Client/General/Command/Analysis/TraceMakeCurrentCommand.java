package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.io.BellcoreStructure;

public class TraceMakeCurrentCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public TraceMakeCurrentCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new TraceMakeCurrentCommand(dispatcher, aContext);
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

		BellcoreStructure bs = Heap.getBSReferenceTrace();
		new FileRemoveCommand(dispatcher, Heap.REFERENCE_TRACE_KEY, aContext).execute();
		new FileRemoveCommand(dispatcher, RefUpdateEvent.PRIMARY_TRACE, aContext).execute();
		Heap.setBSPrimaryTrace(bs);

		new InitialAnalysisCommand().execute();

		dispatcher.notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE,
											RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}