package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class OptionsSetColorsCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public OptionsSetColorsCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new OptionsSetColorsCommand(aContext);
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