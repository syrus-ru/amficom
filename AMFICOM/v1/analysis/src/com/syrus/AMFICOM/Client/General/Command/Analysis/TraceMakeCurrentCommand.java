package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.BellcoreStructure;

public class TraceMakeCurrentCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;


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
			this.checker = new Checker(this.aContext.getSessionInterface());
		/*
			The code for administrating should be placed here
		*/
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		bs = (BellcoreStructure)Pool.get("bellcorestructure", "referencetrace");
		new FileRemoveCommand(dispatcher, "referencetrace", aContext).execute();
		new FileRemoveCommand(dispatcher, "primarytrace", aContext).execute();
		Pool.put("bellcorestructure", "primarytrace", bs);

		new InitialAnalysisCommand().execute();

		dispatcher.notify(new RefChangeEvent("primarytrace",
											RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}