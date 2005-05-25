package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;


public class OptionsSetColorsCommand extends AbstractCommand
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

	}
}