package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class OptionsSetColorsCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public OptionsSetColorsCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new OptionsSetColorsCommand(dispatcher, aContext);
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


	}
}